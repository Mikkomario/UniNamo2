package uninamo_components;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import genesis_event.Drawable;
import genesis_event.EventSelector;
import genesis_event.HandlerRelay;
import genesis_event.MouseEvent;
import genesis_event.MouseEvent.MouseButton;
import genesis_event.MouseEvent.MouseButtonEventScale;
import genesis_event.MouseEvent.MouseButtonEventType;
import genesis_event.MouseEvent.MouseMovementEventType;
import genesis_event.MouseListener;
import genesis_event.MultiEventSelector;
import genesis_event.StrictEventSelector;
import genesis_util.DepthConstants;
import genesis_util.HelpMath;
import genesis_util.StateOperator;
import genesis_util.StateOperatorListener;
import genesis_util.Vector3D;
import omega_util.SimpleGameObject;
import omega_util.Transformable;
import omega_util.Transformation;
import uninamo_gameplaysupport.TestEvent;
import uninamo_gameplaysupport.TestListener;
import uninamo_main.GameSettings;
import vision_sprite.SingleSpriteDrawer;
import vision_sprite.SpriteBank;

/**
 * Cables are elements that connect components together and that deliver 
 * signals from one connector to other.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public class Cable extends SimpleGameObject implements MouseListener, Transformable, 
		SignalSender, SignalReceiver, TestListener, Drawable, StateOperatorListener
{
	// TODO: Split this into multiple classes (placed, dragged, testing)
	
	// ATTRIBUTES	-----------------------------------------------------
	
	private SingleSpriteDrawer spritedrawer;
	private boolean lastSignalStatus, dragged, testing, testVersion;
	private OutputCableConnector start;
	private InputCableConnector end;
	private Vector3D lastMousePosition;
	private ConnectorRelay connectorRelay;
	private StateOperator isVisibleOperator;
	private EventSelector<MouseEvent> mouseEventSelector;
	private Transformation transformation;
	
	/**
	 * Is there currently a cable that's being dragged around
	 */
	protected static boolean cableIsBeingDragged = false;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new cable that starts from the given connector. The cable will 
	 * hover over the mouse until it has been connected to another connector.
	 * 
	 * @param handlers The handlers that will handle the cable
	 * @param connectorRelay A connectorRelay that will inform the cable about 
	 * connector positions
	 * @param startConnector The connector the cable starts from (Optional if endConnector is provided)
	 * @param endConnector The connector the cable ends to (optional if startConnector is provided)
	 * @param isForTesting If this is true, the cable will go to testing mode 
	 * where it can't be moved but the type of it's signal can be changed by 
	 * clicking (provided it doesn't have an input that would dominate the 
	 * signal type)
	 * @param mousePosition The mouse's position at the creation time
	 */
	public Cable(HandlerRelay handlers, ConnectorRelay connectorRelay, 
			OutputCableConnector startConnector, InputCableConnector endConnector, 
			boolean isForTesting, Vector3D mousePosition)
	{
		super(handlers);
		
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
		
		// Initializes attributes
		this.spritedrawer = new SingleSpriteDrawer(SpriteBank.getSprite("components", "cable"), 
				this, handlers);
		this.start = startConnector;
		this.end = endConnector;
		this.testVersion = isForTesting;
		this.lastMousePosition = mousePosition;
		this.lastSignalStatus = false;
		this.connectorRelay = connectorRelay;
		this.testing = false;
		this.isVisibleOperator = new StateOperator(true, true);
		this.transformation = new Transformation();
		
		updateTransformations();
		this.spritedrawer.setImageSpeed(0);
		this.spritedrawer.setImageIndex(0);
		
		// Updates the signal status and informs the end connector (if possible)
		if (this.start != null)
			this.start.connectCable(this);
		if (this.end != null)
			this.end.connectCable(this);
		
		// Listens to movement events as well as local presses & global releases
		MultiEventSelector<MouseEvent> selector = new MultiEventSelector<>();
		selector.addOption(MouseEvent.createEnterExitSelector());
		selector.addOption(MouseEvent.createMouseMoveSelector());
		StrictEventSelector<MouseEvent, MouseEvent.Feature> localLeft = 
				MouseEvent.createMouseButtonSelector(MouseButton.LEFT);
		localLeft.addRequiredFeature(MouseButtonEventScale.LOCAL);
		localLeft.addRequiredFeature(MouseButtonEventType.PRESSED);
		selector.addOption(localLeft);
		StrictEventSelector<MouseEvent, MouseEvent.Feature> globalLeftRelease = 
				MouseEvent.createMouseButtonSelector(MouseButton.LEFT);
		globalLeftRelease.addRequiredFeature(MouseButtonEventType.RELEASED);
		selector.addOption(globalLeftRelease);
		this.mouseEventSelector = selector;
		
		getIsDeadStateOperator().getListenerHandler().add(this);
	}
	
	
	// IMPLEMENTED METHODS	-----------------------------------------------

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
	public void drawSelf(Graphics2D g2d)
	{
		AffineTransform lastTransform = getTransformation().transform(g2d);
		this.spritedrawer.drawSprite(g2d);
		g2d.setTransform(lastTransform);
	}

	@Override
	public int getDepth()
	{
		return DepthConstants.NORMAL - 10;
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return this.isVisibleOperator;
	}

	@Override
	public void onTestEvent(TestEvent event)
	{
		this.testing = event.testRunning();
	}
	
	@Override
	public Transformation getTransformation()
	{
		return this.transformation;
	}

	@Override
	public void setTrasformation(Transformation t)
	{
		this.transformation = t;
	}

	@Override
	public StateOperator getListensToMouseEventsOperator()
	{
		// TODO: Use a separate operator?
		return getIsActiveStateOperator();
	}

	@Override
	public EventSelector<MouseEvent> getMouseEventSelector()
	{
		return this.mouseEventSelector;
	}

	@Override
	public boolean isInAreaOfInterest(Vector3D position)
	{
		Vector3D relativePos = getTransformation().inverseTransform(position).plus(
				this.spritedrawer.getSprite().getOrigin());
		return HelpMath.pointIsInRange(relativePos, Vector3D.zeroVector(), 
				this.spritedrawer.getSprite().getDimensions());
	}

	@Override
	public void onMouseEvent(MouseEvent event)
	{
		// If the tests are running, cables don't work
		if (this.testing && !this.testVersion)
			return;
		
		this.lastMousePosition = event.getPosition();
		
		
		// Updates the last known mouse position if being dragged
		if (event.getMovementEventType() == MouseMovementEventType.MOVE && isBeingDragged())
			updateTransformations();
		// Scales the object on enter, rescales on exit
		else if (event.getMovementEventType() == MouseMovementEventType.ENTER)
			setYScale(GameSettings.interfaceScaleFactor);
		else if (event.getMovementEventType() == MouseMovementEventType.EXIT)
			setYScale(1);
		else if (event.getButtonEventType() == MouseButtonEventType.PRESSED)
		{
			// If the cable is a test version, it fuctions differently
			if (this.testVersion)
			{
				// On left click the signal type is changed (if the cable has no input)
				if (this.start == null)
					onSignalChange(!this.lastSignalStatus, null);
			}
			else if (!cableIsBeingDragged)
			{
				cableIsBeingDragged = true;
				this.dragged = true;
				
				// If the button was pressed closer to start, removes the cable 
				// from there, otherwise removes the cable from the end point
				if (HelpMath.pointDistance2D(event.getPosition(), 
						this.start.getTransformation().getPosition())
						< HelpMath.pointDistance2D(event.getPosition(), 
						this.end.getTransformation().getPosition()))
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
		// On mouse release tries to place the cable on a connector
		else if (event.getButtonEventType() == MouseButtonEventType.RELEASED && isBeingDragged())
		{
			// Ends drag
			cableIsBeingDragged = false;
			this.dragged = false;
			
			// Tries to find a suitable new start- / endpoint
			if (this.start == null)
			{
				OutputCableConnector newstart = (OutputCableConnector) 
						this.connectorRelay.getConnectorAtPoint(
						this.lastMousePosition, OutputCableConnector.class);
				// If one couldn't be found, dies
				if (newstart == null)
					getIsDeadStateOperator().setState(true);
				else
				{
					this.start = newstart;
					this.start.connectCable(this);
				}
			}
			else //if (this.end == null)
			{
				InputCableConnector newend = (InputCableConnector) 
						this.connectorRelay.getConnectorAtPoint(
						this.lastMousePosition, InputCableConnector.class);
				// If one couldn't be found, dies
				if (newend == null)
					getIsDeadStateOperator().setState(true);
				else
				{
					this.end = newend;
					this.end.connectCable(this);
				}
			}
		}
	}

	@Override
	public void onStateChange(StateOperator source, boolean newState)
	{
		if (source.equals(getIsDeadStateOperator()) && newState)
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
		}
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
	/**
	 * Updates the cables transformations. This should be called when a connected connector's 
	 * transformations change
	 */
	public void updateTransformations()
	{
		Vector3D startPoint, endPoint;
		
		// Updates starting position
		if (this.start != null)
			startPoint = this.start.getTransformation().getPosition();
		else
		{
			if (!this.testVersion)
				startPoint = this.lastMousePosition;
			else if (this.end != null)
				startPoint = this.end.getTransformation().getPosition().minus(
						new Vector3D(50, 0));
			else
				startPoint = Vector3D.zeroVector();
		}
		
		// Updates scaling and rotation
		if (this.end != null)
			endPoint = this.end.getTransformation().getPosition();
		else
		{
			if (!this.testVersion)
				endPoint = this.lastMousePosition;
			else if (this.start != null)
				endPoint = this.start.getTransformation().getPosition().plus(
						new Vector3D(50, 0));
			else
				endPoint = Vector3D.zeroVector();
		}
		
		double angle = HelpMath.pointDirection(startPoint, endPoint);
		double xScale = HelpMath.pointDistance2D(startPoint, endPoint) / 
				(this.spritedrawer.getSprite().getDimensions().getFirst() - 2 * 
				this.spritedrawer.getSprite().getOrigin().getFirst());
		
		Transformation t = new Transformation(startPoint, new Vector3D(xScale, 
				getTransformation().getScaling().getSecond()), Vector3D.identityVector(), 
				angle);
		setTrasformation(t);
	}
	
	private void setYScale(double scale)
	{
		setTrasformation(getTransformation().withScaling(
				new Vector3D(getTransformation().getScaling().getFirst(), scale)));
	}
	
	private boolean isBeingDragged()
	{
		return this.dragged;//(cableIsBeingDragged && (this.start != null || this.end != null));
	}
}
