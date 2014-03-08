package uninamo_components;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import utopia_gameobjects.DimensionalDrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.CollisionType;
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
		// TODO: Add new signal interfaces
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private SingleSpriteDrawer spritedrawer;
	private boolean active;
	
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
		
		// TODO: Update position
		
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
		// TODO: Update position
	}

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D mousePosition,
			double eventStepTime)
	{
		// TODO: Place or remove the cable
	}

	@Override
	public boolean listensPosition(Point2D testedPosition)
	{
		return pointCollides(testedPosition);
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		// TODO: Change later
		return false;
	}

	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Point2D mousePosition, double eventStepTime)
	{
		// TODO: Add scaling if removable
	}

	@Override
	public void onMouseMove(Point2D newMousePosition)
	{
		// TODO: Update transformations if being placed
	}

	@Override
	public MouseButtonEventScale getCurrentButtonScaleOfInterest()
	{
		// TODO: Change to global when being placed
		return MouseButtonEventScale.LOCAL;
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
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean getSignalStatus()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
