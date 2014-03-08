package uninamo_components;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import uninamo_main.GameSettings;
import utopia_gameobjects.DimensionalDrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.HelpMath;
import utopia_listeners.AdvancedMouseListener;
import utopia_listeners.TransformationListener;
import utopia_resourcebanks.MultiMediaHolder;

/**
 * Cables are elements that connect components together and that deliver 
 * signals from one connector to other.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public class Cable extends DimensionalDrawnObject implements
		AdvancedMouseListener, TransformationListener, SignalSender, SignalReceiver
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private SingleSpriteDrawer spritedrawer;
	private boolean active, lastSignalStatus;
	private OutputCableConnector start;
	private InputCableConnector end;
	private Point2D lastMousePosition;
	
	/**
	 * Is there currently a cable that's being dragged around
	 */
	protected static boolean cableIsBeingDragged = false;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new cable that starts from the given connector. The cable will 
	 * hover over the mouse until it has been connected to another connector.
	 * 
	 * @param drawer The drawableHandler that will draw the cable
	 * @param mousehandler The mouseListenerHandler that will inform the 
	 * cable about mouse events
	 * @param startConnector The connector the cable starts from
	 */
	public Cable(DrawableHandler drawer, MouseListenerHandler mousehandler, 
			OutputCableConnector startConnector)
	{
		super(0, 0, startConnector.getDepth() - 1, false, CollisionType.BOX, 
				drawer, null);
		
		// Initializes attributes
		this.spritedrawer = new SingleSpriteDrawer(
				MultiMediaHolder.getSpriteBank("components").getSprite(
				"cable"), null, this);
		this.active = true;
		this.start = startConnector;
		this.end = null;
		this.lastMousePosition = new Point2D.Double(0, 0);
		this.lastSignalStatus = this.start.getSignalStatus();
		
		// The cable starts as being dragged
		cableIsBeingDragged = true;
		
		updateTransformations();
		this.spritedrawer.setImageSpeed(0);
		this.spritedrawer.setImageIndex(0);
		
		// Adds the object to the handler(s)
		if (mousehandler != null)
			mousehandler.addMouseListener(this);
		startConnector.getTransformationListenerHandler().addListener(this);
	}
	
	
	// IMPLEMENTED METHODS	-----------------------------------------------

	@Override
	public boolean isActive()
	{
		return this.active;
	}

	@Override
	public void activate()
	{
		this.active = true;
	}

	@Override
	public void inactivate()
	{
		this.active = false;
	}

	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		// Doesn't limit listeners
		return null;
	}

	@Override
	public void onTransformationEvent(TransformationEvent e)
	{
		// Resets the position
		updateTransformations();
	}

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D mousePosition,
			double eventStepTime)
	{
		// On mouse release tries to place the cable on a connector
		// TODO: Implement cable placing
		// TODO: Create a connectorHandler
		
		// On mouse press removes the cable from either end
		if (button == MouseButton.LEFT && eventType == 
				MouseButtonEventType.PRESSED && !cableIsBeingDragged)
		{
			cableIsBeingDragged = true;
			
			// If the button was pressed closer to start, removes the cable 
			// from there, otherwise removes the cable from the end point
			if (HelpMath.pointDistance(mousePosition.getX(), 
					mousePosition.getY(), this.start.getX(), this.start.getY()) 
					< HelpMath.pointDistance(mousePosition.getX(), 
					mousePosition.getY(), this.end.getX(), this.end.getY()))
			{
				this.start.removeCable(this);
				this.start = null;
			}
			else
			{
				this.end.removeCable(this);
				this.end = null;
			}
			
			setYScale(1);
		}
	}

	@Override
	public boolean listensPosition(Point2D testedPosition)
	{
		return pointCollides(testedPosition);
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		// Listens to enter and exit events if is not being dragged
		return !isBeingDragged();
	}

	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Point2D mousePosition, double eventStepTime)
	{
		// Scales the object on enter, rescales on exit
		if (eventType == MousePositionEventType.ENTER)
			setYScale(GameSettings.interfaceScaleFactor);
		else if (eventType == MousePositionEventType.EXIT)
			setYScale(1);
	}

	@Override
	public void onMouseMove(Point2D newMousePosition)
	{
		// Updates the last known mouse position if being dragged
		if (isBeingDragged())
			this.lastMousePosition = newMousePosition;
	}

	@Override
	public MouseButtonEventScale getCurrentButtonScaleOfInterest()
	{
		if (!isBeingDragged())
			return MouseButtonEventScale.LOCAL;
		else
			return MouseButtonEventScale.GLOBAL;
	}

	@Override
	public int getWidth()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getWidth();
	}

	@Override
	public int getHeight()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getHeight();
	}

	@Override
	public int getOriginX()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getOriginX();
	}

	@Override
	public int getOriginY()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getOriginY();
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		if (this.spritedrawer == null)
			return;
		this.spritedrawer.drawSprite(g2d, 0, 0);
	}

	@Override
	public void onSignalChange(boolean newSignalStatus, SignalSender source)
	{
		// Informs the end point about the change (if there is one)
		if (this.end != null)
			this.end.onSignalChange(newSignalStatus, this);
		
		this.lastSignalStatus = newSignalStatus;
		
		// Changes sprite index
		if (newSignalStatus)
			this.spritedrawer.setImageIndex(1);
		else
			this.spritedrawer.setImageIndex(0);
	}

	@Override
	public boolean getSignalStatus()
	{
		return this.lastSignalStatus;
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	// Updates the position and form of the cable
	private void updateTransformations()
	{
		Point2D startPoint, endPoint;
		
		// Updates starting position
		if (this.start != null)
			startPoint = this.start.getPosition();
		else
			startPoint = this.lastMousePosition;
		
		// Updates scaling and rotation
		if (this.end != null)
			endPoint = this.end.getPosition();
		else
			endPoint = this.lastMousePosition;
		
		// TODO: Debug this since it can cause problems
		setPosition((Point2D.Double) startPoint);
		setAngle(HelpMath.pointDirection(startPoint.getX(), startPoint.getY(), 
				endPoint.getX(), endPoint.getY()));
		setXScale(HelpMath.pointDistance(startPoint.getX(), startPoint.getY(), 
				endPoint.getX(), endPoint.getY()) / getWidth());
	}
	
	private boolean isBeingDragged()
	{
		return (cableIsBeingDragged && this.start != null && this.end != null);
	}
}
