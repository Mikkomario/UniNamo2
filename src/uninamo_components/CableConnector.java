package uninamo_components;

import genesis_event.Drawable;
import genesis_event.EventSelector;
import genesis_event.HandlerRelay;
import genesis_event.MouseEvent;
import genesis_event.MouseEvent.MouseButton;
import genesis_event.MouseEvent.MouseButtonEventScale;
import genesis_event.MouseEvent.MouseButtonEventType;
import genesis_event.MouseEvent.MouseEventType;
import genesis_event.MouseEvent.MouseMovementEventType;
import genesis_event.MouseListener;
import genesis_event.MultiEventSelector;
import genesis_event.StrictEventSelector;
import genesis_util.StateOperator;
import genesis_util.StateOperatorListener;
import genesis_util.Vector3D;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import arc_bank.Bank;
import omega_util.DependentGameObject;
import omega_util.Transformable;
import omega_util.Transformation;
import uninamo_gameplaysupport.TestEvent;
import uninamo_gameplaysupport.TestListener;
import uninamo_main.GameSettings;
import vision_sprite.MultiSpriteDrawer;
import vision_sprite.Sprite;
import vision_sprite.SpriteBank;

/**
 * CableConnectors are attached to certain components. They handle the signal 
 * processing between the component and the cable. Cables can be attached 
 * to the connectors at will.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 * @see Component
 */
public abstract class CableConnector extends DependentGameObject<Component> implements
		MouseListener, SignalSender, SignalReceiver, Drawable, StateOperatorListener, 
		Transformable, TestListener
{
	// TODO: Reove the separate classes?
	
	// ATTRIBUTES	-----------------------------------------------------
	
	private MultiSpriteDrawer spritedrawer;
	private Vector3D relativePosition;
	private List<Cable> connectedCables;
	private boolean testing;
	private Transformation transformation;
	private EventSelector<MouseEvent> selector;
	private HandlerRelay handlers;
	private String id;
	private double scaling;
	private ConnectorRelay connectorRelay;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new cableConnector that is tied to the given component.
	 * @param handlers The handlers that will handle the connector
	 * @param relativePosition The connectors position relative to the component
	 * @param host The component to which the connector is tied to
	 * @param isForTesting If this is true, the connector won't react to mouse 
	 * at all.
	 * @param id The identifier of the connector
	 * @param relay The connector relay that keeps track of the connectors
	 */
	public CableConnector(HandlerRelay handlers, Vector3D relativePosition, Component host, 
			boolean isForTesting, String id, ConnectorRelay relay)
	{
		super(host, handlers);
		
		// Initializes attributes
		this.id = id;
		this.connectorRelay = relay;
		this.relativePosition = relativePosition;
		this.transformation = new Transformation();
		this.handlers = handlers;
		this.scaling = 1;
		if (isForTesting)
			this.selector = new MultiEventSelector<>();
		else
		{
			MultiEventSelector<MouseEvent> selector = new MultiEventSelector<>();
			selector.addOption(MouseEvent.createEnterExitSelector());
			StrictEventSelector<MouseEvent, MouseEvent.Feature> clickSelector = 
					MouseEvent.createMouseButtonSelector(MouseButton.LEFT);
			clickSelector.addRequiredFeature(MouseButtonEventScale.LOCAL);
			clickSelector.addRequiredFeature(MouseButtonEventType.PRESSED);
			selector.addOption(clickSelector);
			
			this.selector = selector;
		}
		
		Bank<Sprite> spriteBank = SpriteBank.getSpriteBank("components");
		Sprite[] sprites = {spriteBank.get("inputconnector"), 
				spriteBank.get("outputconnector")};
		this.spritedrawer = new MultiSpriteDrawer(sprites, this, handlers);
		this.connectedCables = new ArrayList<>();
		
		getSpriteDrawer().setImageSpeed(0);
	}
	
	
	// ABSTRACT METHODS	-------------------------------------------------
	
	/**
	 * This method creates a cable starting from this connector
	 * @param handlers The handlers that will handle the cable
	 * @param relay The connectorRelay that keeps track of the connectors
	 * @param mousePosition The current mouse position
	 */
	protected abstract void createCable(HandlerRelay handlers, ConnectorRelay relay, 
			Vector3D mousePosition);
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public StateOperator getListensToMouseEventsOperator()
	{
		// TODO: Use a separate operator here
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
		return getTransformation().inverseTransform(position).getLength() < getRadius();
	}

	@Override
	public void onMouseEvent(MouseEvent event)
	{
		// Doesn't react to mouse when testing
		if (this.testing)
			return;
		
		// Scales the object on enter, rescales on exit
		if (event.getType() == MouseEventType.MOVEMENT)
		{
			if (event.getMovementEventType() == MouseMovementEventType.ENTER)
				largen();
			else
				rescale();
		}
		// On click, creates a new cable
		else
			createCable(this.handlers, this.connectorRelay, event.getPosition());
	}

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		AffineTransform lastTransform = getTransformation().transform(g2d);
		getSpriteDrawer().drawSprite(g2d);
		g2d.setTransform(lastTransform);
	}

	@Override
	public int getDepth()
	{
		return getMaster().getDepth() - 2;
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return getMaster().getIsVisibleStateOperator();
	}

	@Override
	public void onStateChange(StateOperator source, boolean newState)
	{
		// Also kills the connected cables
		if (source.equals(getIsDeadStateOperator()) && newState)
		{
			for (Cable cable : this.connectedCables)
			{
				cable.getIsDeadStateOperator().setState(true);
			}
		}
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
		
		for (Cable cable : this.connectedCables)
		{
			cable.updateTransformations();
		}
	}
	
	@Override
	public void onTestEvent(TestEvent event)
	{
		this.testing = event.testRunning();
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Updates the connector's transformations. This should be called when the comonent's 
	 * transformations are updated
	 */
	public void updateTransformations()
	{
		Vector3D position = getMaster().getTransformation().transform(this.relativePosition);
		setTrasformation(getMaster().getTransformation().withPosition(position).plus(
				Transformation.scalingTransformation(this.scaling)));
	}
	
	/**
	 * @return The identifier of the connector
	 */
	public String getID()
	{
		return this.id;
	}
	
	/**
	 * @return The spriteDrawer the connector uses
	 */
	protected MultiSpriteDrawer getSpriteDrawer()
	{
		return this.spritedrawer;
	}
	
	/**
	 * Returns a cable from the list of connected cables
	 * 
	 * @param index The index of the cable in the list (starts from 0)
	 * @return A cable with the given index or null if no such index exists
	 */
	protected Cable getCable(int index)
	{
		if (index >= 0 && index < this.connectedCables.size())
			return this.connectedCables.get(index);
		else
			return null;
	}
	
	/**
	 * Connects a new cable to the cable connector
	 * 
	 * @param c The cable to be connected
	 */
	public void connectCable(Cable c)
	{
		if (!this.connectedCables.contains(c))
			this.connectedCables.add(c);
	}
	
	/**
	 * Removes a cable from the connector
	 * @param c The cable to be removed from the connector
	 */
	public void removeCable(Cable c)
	{
		// If the component is dead, it is probably already removing the 
		// cables
		if (!getIsDeadStateOperator().getState() && this.connectedCables.contains(c))
			this.connectedCables.remove(c);
	}
	
	/**
	 * @return How many cables are connected to this connector
	 */
	protected int getCableAmount()
	{
		return this.connectedCables.size();
	}
	
	/**
	 * Makes the connector appear larger
	 */
	private void largen()
	{
		this.scaling = Math.pow(GameSettings.interfaceScaleFactor, 2);
		updateTransformations();
	}
	
	/**
	 * Rescales the component back to the component's levels
	 */
	private void rescale()
	{
		// TODO: WET WET
		this.scaling = 1;
		updateTransformations();
	}
	
	private double getRadius()
	{
		return getSpriteDrawer().getSprite().getDimensions().getFirst() / 2;
	}
}
