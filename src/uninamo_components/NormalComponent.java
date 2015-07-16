package uninamo_components;

import genesis_event.EventSelector;
import genesis_event.HandlerRelay;
import genesis_event.MouseEvent;
import genesis_event.MouseEvent.MouseMovementEventType;
import genesis_event.MouseListener;
import genesis_event.MultiEventSelector;
import genesis_event.StrictEventSelector;
import genesis_event.MouseEvent.MouseButton;
import genesis_event.MouseEvent.MouseButtonEventScale;
import genesis_event.MouseEvent.MouseButtonEventType;
import genesis_util.HelpMath;
import genesis_util.StateOperator;
import genesis_util.StateOperatorListener;
import genesis_util.Vector3D;
import uninamo_gameplaysupport.TestEvent;
import uninamo_gameplaysupport.TestListener;
import uninamo_main.GameSettings;
import uninamo_userinterface.CurrentCostDrawer;

/**
 * NormalComponents are components that aren't machineComponents. 
 * NormalComponents can be saved as a string from which they can later be 
 * recreated. under normal circumstances, NormalComponents can be moved around 
 * by dragging and dropping.
 * 
 * @author Mikko Hilpinen
 * @since 15.3.2014
 */
public abstract class NormalComponent extends Component implements 
		MouseListener, TestListener, StateOperatorListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private String id;
	private CurrentCostDrawer costDrawer;
	private boolean dragged, diesWhenDropped, testing;
	// Notice that this is relative to the origin, not top-left corner 
	// (in other words is a relative absolute point)
	private Vector3D lastRelativeMouseGrabPosition;
	private ConnectorRelay relay;
	private EventSelector<MouseEvent> selector;
	
	private static boolean componentDragged = false;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new component to the given position.
	 * 
	 * @param handlers The handlers that will handle the component
	 * @param position The component's position
	 * @param connectorRelay A connectorRelay that will keep track of the 
	 * connectors
	 * @param costDrawer The costDrawer that will be affected by the component 
	 * (optional)
	 * @param spriteName The name of the component sprite used to draw the 
	 * component
	 * @param inputs How many input connectors the component has
	 * @param outputs How many output connectors the component has
	 * @param fromBox Was the component created by pulling it from a componentBox
	 * @param isForTesting If this is true, the component will go to test mode 
	 * where it won't react to mouse but will create test cables to its connectors
	 */
	public NormalComponent(HandlerRelay handlers, Vector3D position, 
			ConnectorRelay connectorRelay, CurrentCostDrawer costDrawer, 
			String spriteName, int inputs, int outputs, boolean fromBox, 
			boolean isForTesting)
	{
		super(handlers, position, connectorRelay, spriteName, inputs, outputs, fromBox, 
				isForTesting);
		
		// Initializes attributes
		String typeName = getType().toString();
		if (typeName.length() > 4)
			typeName = typeName.substring(0, 4);
		this.id = typeName + super.getID();
		this.costDrawer = costDrawer;
		
		this.dragged = fromBox && !isForTesting;
		this.diesWhenDropped = fromBox && !isForTesting;
		this.lastRelativeMouseGrabPosition = Vector3D.zeroVector();
		if (fromBox && !isForTesting)
			componentDragged = true;
		this.testing = false;
		this.relay = connectorRelay;
		
		// Listens to mouse movement, mouse clicks (local) and releases (global)
		if (isForTesting)
			this.selector = new MultiEventSelector<>();
		else
		{
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
			this.selector = selector;
		}
		
		// Informs the costDrawer about increased component costs
		if (this.costDrawer != null)
		{
			costDrawer.addCosts(getType().getPrice());
			getIsDeadStateOperator().getListenerHandler().add(this);
		}
	}
	
	
	// ABSTRACT METHODS	--------------------------------------------------
	
	/**
	 * @return What componentType this component represents
	 */
	public abstract ComponentType getType();

	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public String getID()
	{
		return this.id;
	}

	@Override
	public void onTestEvent(TestEvent event)
	{
		this.testing = event.testRunning();
	}

	@Override
	public StateOperator getListensToMouseEventsOperator()
	{
		// TODO: Use a separate operator here?
		return getIsActiveStateOperator();
	}

	@Override
	public EventSelector<MouseEvent> getMouseEventSelector()
	{
		return this.selector;
	}

	@Override
	public boolean isInAreaOfInterest(Vector3D position)
	{
		Vector3D relativePosition = getTransformation().inverseTransform(position).plus(
				getSpriteDrawer().getSprite().getOrigin());
		return HelpMath.pointIsInRange(relativePosition, Vector3D.zeroVector(), 
				getSpriteDrawer().getSprite().getDimensions());
	}

	@Override
	public void onMouseEvent(MouseEvent event)
	{
		// If testing, doesn't react to mouse
		if (this.testing)
			return;
			
		if (event.getMovementEventType() == MouseMovementEventType.MOVE && this.dragged)
		{
			// If being dragged, jumps into the mouse's position
			setTrasformation(getTransformation().withPosition(event.getPosition().minus(
					this.lastRelativeMouseGrabPosition)));
			// If the position is near an edge of the screen, prepares to die, 
			// may also return from this state
			if (HelpMath.pointIsInRange(event.getPosition(), new Vector3D(64, 64), 
					GameSettings.resolution.minus(new Vector3D(64, 64))))
			{
				if (this.diesWhenDropped)
				{
					this.diesWhenDropped = false;
					setScale(GameSettings.interfaceScaleFactor);
				}
			}
			else
			{
				if (!this.diesWhenDropped)
				{
					this.diesWhenDropped = true;
					setScale(0.5);
				}
			}
		}
		else if (event.getMovementEventType() == MouseMovementEventType.ENTER && !this.dragged)
			setScale(GameSettings.interfaceScaleFactor);
		else if (event.getMovementEventType() == MouseMovementEventType.EXIT && !this.dragged)
			setScale(1);
		// If the component (and not one of its connectors) is clicked, it 
		// is considered a grab
		else if (event.getButtonEventType() == MouseButtonEventType.PRESSED)
		{
			if (!componentDragged && this.relay.getConnectorAtPoint(event.getPosition(), 
					null) == null)
			{
				this.dragged = true;
				componentDragged = true;
				this.lastRelativeMouseGrabPosition = event.getPosition().minus(
						getTransformation().getPosition());
			}
		}
		else if (event.getButtonEventType() == MouseButtonEventType.RELEASED && this.dragged)
		{
				// If the button is released, also releases the grab
				this.dragged = false;
				componentDragged = false;
				
				// If the component was supposed to die upon drop, dies
				if (this.diesWhenDropped)
					getIsDeadStateOperator().setState(true);
		}	
	}

	@Override
	public void onStateChange(StateOperator source, boolean newState)
	{
		if (source.equals(getIsDeadStateOperator()) && newState)
			this.costDrawer.addCosts(-getType().getPrice());
	}

	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * @return Returns the data needed for recreating the component in a 
	 * string format. The string will have the following structure:<br>
	 * ID#ComponentType#x#y
	 */
	public String getSaveData()
	{
		return getID() + "#" + getType().toString().toLowerCase() + "#" + 
				getTransformation().getPosition().getFirstInt() + "#" + 
				getTransformation().getPosition().getSecondInt();
	}
	
	/**
	 * Changes the components unique ID.
	 * 
	 * @param ID The component's new unique ID.
	 */
	public void setID(String ID)
	{
		this.id = ID;
	}
	
	/**
	 * Stops the component from being dragged
	 */
	public void stopDrag()
	{
		this.dragged = false;
		componentDragged = false;
	}
	
	private void setScale(double scale)
	{
		setTrasformation(getTransformation().withScaling(new Vector3D(scale, scale)));
	}
}
