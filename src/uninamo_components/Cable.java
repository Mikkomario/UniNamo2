package uninamo_components;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TestListener;
import uninamo_main.GameSettings;
import utopia_gameobjects.DimensionalDrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.DepthConstants;
import utopia_helpAndEnums.HelpMath;
import utopia_listeners.AdvancedMouseListener;
import utopia_listeners.RoomListener;
import utopia_listeners.TransformationListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Room;

/**
 * Cables are elements that connect components together and that deliver 
 * signals from one connector to other.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public class Cable extends DimensionalDrawnObject implements
		AdvancedMouseListener, TransformationListener, SignalSender, 
		SignalReceiver, RoomListener, TestListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private SingleSpriteDrawer spritedrawer;
	private boolean active, lastSignalStatus, dragged, testing, testVersion;
	private OutputCableConnector start;
	private InputCableConnector end;
	private Point2D.Double lastMousePosition;
	private ConnectorRelay connectorRelay;
	
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
	 * @param room The room where the cable resides at
	 * @param testHandler The testHandler that will inform the cable about test events
	 * @param connectorRelay A connectorRelay that will inform the cable about 
	 * connector positions
	 * @param startConnector The connector the cable starts from (Optional if endConnector is provided)
	 * @param endConnector The connector the cable ends to (optional if startConnector is provided)
	 * @param isForTesting If this is true, the cable will go to testing mode 
	 * where it can't be moved but the type of it's signal can be changed by 
	 * clicking (provided it doesn't have an input that would dominate the 
	 * signal type)
	 */
	public Cable(DrawableHandler drawer, MouseListenerHandler mousehandler, 
			Room room, TestHandler testHandler, ConnectorRelay connectorRelay, 
			OutputCableConnector startConnector, InputCableConnector endConnector, 
			boolean isForTesting)
	{
		super(0, 0, DepthConstants.NORMAL - 5, false, CollisionType.BOX, 
				drawer, null);
		
		//System.out.println(startConnector);
		//System.out.println(endConnector);
		
		if (!isForTesting && (startConnector == null || endConnector == null))
		{
			// The cable starts as being dragged
			if (cableIsBeingDragged)
				System.out.println("CABLE ALREADY DRAGGED!");
			
			cableIsBeingDragged = true;
			this.dragged = true;
		}
		else
			this.dragged = false;
		
		/*
		System.out.println("-------");
		System.out.println("start: " + startConnector);
		System.out.println("end: " + endConnector);
		System.out.println(this.dragged);
		*/
		
		// Initializes attributes
		this.spritedrawer = new SingleSpriteDrawer(
				MultiMediaHolder.getSpriteBank("components").getSprite(
				"cable"), null, this);
		this.active = true;
		this.start = startConnector;
		this.end = endConnector;
		this.testVersion = isForTesting;
		this.lastMousePosition = new Point2D.Double(
				mousehandler.getMousePosition().getX(), 
				mousehandler.getMousePosition().getY());
		this.lastSignalStatus = false;
		this.connectorRelay = connectorRelay;
		this.testing = false;
		
		updateTransformations();
		this.spritedrawer.setImageSpeed(0);
		this.spritedrawer.setImageIndex(0);
		
		// Updates the signal status and informs the end connector (if possible)
		if (this.start != null)
			this.start.connectCable(this);
		if (this.end != null)
			this.end.connectCable(this);
		
		// Adds the object to the handler(s)
		if (room != null)
			room.addObject(this);
		if (mousehandler != null)
			mousehandler.addMouseListener(this);
		if (startConnector != null)
			startConnector.getTransformationListenerHandler().addListener(this);
		if (endConnector != null)
			endConnector.getTransformationListenerHandler().addListener(this);
		if (testHandler != null)
			testHandler.addTestable(this);
	}
	
	
	// IMPLEMENTED METHODS	-----------------------------------------------

	@Override
	public boolean isActive()
	{
		// If the cable is in testing mode it doesn't react to mouse
		return this.active && !this.testing;
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
			MouseButtonEventType eventType, Point2D.Double mousePosition,
			double eventStepTime)
	{
		// If the cable is a test version, it fuctions differently
		if (this.testVersion)
		{
			// On left click the signal type is changed (if the cable has no input)
			if (this.start == null && button == MouseButton.LEFT && 
					eventType == MouseButtonEventType.PRESSED)
				onSignalChange(!this.lastSignalStatus, null);
			
			return;
		}
		
		// On mouse release tries to place the cable on a connector
		if (button == MouseButton.LEFT && eventType == 
				MouseButtonEventType.RELEASED && isBeingDragged())
		{
			// Ends drag
			cableIsBeingDragged = false;
			this.dragged = false;
			
			//System.out.println("Ends drag on cable " + this + ": " + (this.start == null) + ", " + (this.end == null));
			
			// Tries to find a suitable new start- / endpoint
			if (this.start == null)
			{
				OutputCableConnector newstart = (OutputCableConnector) 
						this.connectorRelay.getConnectorAtPoint(
						this.lastMousePosition, OutputCableConnector.class);
				// If one couldn't be found, dies
				if (newstart == null)
				{
					//System.out.println("Cable dies since start is null");
					this.end.removeCable(this);
					this.end = null;
					kill();
				}
				else
				{
					this.start = newstart;
					this.start.connectCable(this);
					this.start.getTransformationListenerHandler().addListener(this);
				}
			}
			else //if (this.end == null)
			{
				InputCableConnector newend = (InputCableConnector) 
						this.connectorRelay.getConnectorAtPoint(
						this.lastMousePosition, InputCableConnector.class);
				// If one couldn't be found, dies
				if (newend == null)
				{
					//System.out.println("Dies since end is null");
					this.start.removeCable(this);
					this.start = null;
					kill();
				}
				else
				{
					this.end = newend;
					this.end.connectCable(this);
					this.end.getTransformationListenerHandler().addListener(this);
				}
			}
			
			//System.out.println("Cable is still without end: " + (this.start == null) + ", " + (this.end == null));
		}
		
		// On mouse press removes the cable from either end
		else if (button == MouseButton.LEFT && eventType == 
				MouseButtonEventType.PRESSED && !cableIsBeingDragged)
		{
			cableIsBeingDragged = true;
			this.dragged = true;
			
			//System.out.println("Releases a cable");
			
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
	public boolean listensPosition(Point2D.Double testedPosition)
	{
		return pointCollides(testedPosition);
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		// Listens to enter and exit events if is not being dragged 
		// (enter / exit is also disabled on test mode for output cables)
		return !isBeingDragged() && !(this.testVersion && this.end == null);
	}

	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Point2D.Double mousePosition, double eventStepTime)
	{
		// Scales the object on enter, rescales on exit
		if (eventType == MousePositionEventType.ENTER)
			setYScale(GameSettings.interfaceScaleFactor);
		else if (eventType == MousePositionEventType.EXIT)
			setYScale(1);
	}

	@Override
	public void onMouseMove(Point2D.Double newMousePosition)
	{
		// Updates the last known mouse position if being dragged
		if (isBeingDragged())
		{
			this.lastMousePosition = newMousePosition;
			updateTransformations();
		}
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
		return (int) (this.spritedrawer.getSprite().getHeight() * 1.25);
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
		// If the status is same as previously, does nothing
		if (newSignalStatus == this.lastSignalStatus)
			return;
		
		this.lastSignalStatus = newSignalStatus;
		
		// Changes sprite index
		if (newSignalStatus)
			this.spritedrawer.setImageIndex(1);
		else
			this.spritedrawer.setImageIndex(0);
		
		// Informs the end point about the change (if there is one)
		if (this.end != null)
			this.end.onSignalChange(newSignalStatus, this);
	}

	@Override
	public boolean getSignalStatus()
	{
		return this.lastSignalStatus;
	}
	
	@Override
	public void onRoomStart(Room room)
	{
		// Does nothing
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Dies
		kill();
	}
	
	@Override
	public void onTestStart()
	{
		this.testing = true;
	}

	@Override
	public void onTestEnd()
	{
		this.testing = false;
	}
	
	@Override
	public void kill()
	{
		// Removes the cable from its connectors first
		if (this.start != null)
		{
			this.start.removeCable(this);
			this.start = null;
		}
		if (this.end != null)
		{
			this.end.removeCable(this);
			this.end = null;
		}
		
		super.kill();
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * @return Returns the data needed for recreating the cable. The data is 
	 * in string format and has the following syntax:<br>
	 * startConnectorID#endConnectorID<br>
	 * If one of the IDs can't be obtained, a warning is given and it is 
	 * replaced with 'NONE'
	 */
	public String getSaveData()
	{
		String start = "NONE";
		String end = "NONE";
		
		if (this.start != null)
			start = this.start.getID();
		else
			System.err.println("Trying to save a cable with no start");
		
		if (this.end != null)
			end = this.end.getID();
		else
			System.err.println("Trying to save a cable with no end");
		
		return start + "#" + end;
	}
	
	// Updates the position and form of the cable
	private void updateTransformations()
	{
		Point2D.Double startPoint, endPoint;
		
		// Updates starting position
		if (this.start != null)
			startPoint = this.start.getPosition();
		else
		{
			if (!this.testVersion)
				startPoint = this.lastMousePosition;
			else if (this.end != null)
				startPoint = new Point2D.Double(this.end.getX() - 50, 
						this.end.getY());
			else
				startPoint = new Point2D.Double();
		}
		
		// Updates scaling and rotation
		if (this.end != null)
			endPoint = this.end.getPosition();
		else
		{
			if (!this.testVersion)
				endPoint = this.lastMousePosition;
			else if (this.start != null)
				endPoint = new Point2D.Double(this.start.getX() + 50, 
						this.start.getY());
			else
				endPoint = new Point2D.Double();
		}
		
		setPosition(startPoint);
		setAngle(HelpMath.pointDirection(startPoint.getX(), startPoint.getY(), 
				endPoint.getX(), endPoint.getY()));
		setXScale(HelpMath.pointDistance(startPoint.getX(), startPoint.getY(), 
				endPoint.getX(), endPoint.getY()) / (getWidth() - 2 * getOriginX()));
	}
	
	private boolean isBeingDragged()
	{
		return this.dragged;//(cableIsBeingDragged && (this.start != null || this.end != null));
	}
}
