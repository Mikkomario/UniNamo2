package uninamo_components;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import uninamo_main.GameSettings;
import utopia_gameobjects.DimensionalDrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_listeners.AdvancedMouseListener;
import utopia_listeners.RoomListener;
import utopia_listeners.TransformationListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Room;

/**
 * CableConnectors are attached to certain components. They handle the signal 
 * processing between the component and the cable. Cables can be attached 
 * to the connectors at will.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 * @see Component
 */
public abstract class CableConnector extends DimensionalDrawnObject implements
		AdvancedMouseListener, TransformationListener, SignalSender, 
		SignalReceiver, RoomListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private SingleSpriteDrawer spritedrawer;
	private Component host;
	private Point2D.Double relativePoint;
	private ArrayList<Cable> connectedCables;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new cableConnector that is tied to the given component.
	 * 
	 * @param relativex The x-coordinate of the object in relation to the 
	 * component's top-left corner (pixels)
	 * @param relativey The y-coordinate of the object in relation to the 
	 * component's top-left corner (pixels)
	 * @param drawer The drawableHandler that will draw the connector
	 * @param mousehandler The mouseListenerHandler that will inform the object 
	 * about mouse events
	 * @param room The room where the connector resides at
	 * @param relay The ConnectorRelay that will keep track of the connector
	 * @param host The component to which the connector is tied to
	 */
	public CableConnector(int relativex, int relativey, DrawableHandler drawer, 
			MouseListenerHandler mousehandler, Room room, ConnectorRelay relay, 
			Component host)
	{
		super(0, 0, host.getDepth() - 1, false, CollisionType.CIRCLE, drawer, 
				null);
		
		// Initializes attributes
		this.relativePoint = new Point2D.Double(relativex, relativey);
		this.host = host;
		this.spritedrawer = new SingleSpriteDrawer(
				MultiMediaHolder.getSpriteBank("components").getSprite(
				"cableconnector"), null, this);
		this.connectedCables = new ArrayList<Cable>();
		
		// Updates radius
		setRadius(this.spritedrawer.getSprite().getWidth() / 2);
		
		// Updates the position
		updateAbsolutePosition();
		
		// Adds the object to the handler(s)
		if (room != null)
			room.addObject(this);
		if (mousehandler != null)
			mousehandler.addMouseListener(this);
		if (relay != null)
			relay.addConnector(this);
	
		host.getTransformationListenerHandler().addListener(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public boolean isActive()
	{
		return this.host.isActive();
	}

	@Override
	public void activate()
	{
		// Cannot be activated separately
	}

	@Override
	public void inactivate()
	{
		// Cannot be inactivated separately
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
		// Updates the position
		updateAbsolutePosition();
		
		// May scale the object as well
		if (e.getType() == TransformationType.SCAlING && getXScale() 
				<= GameSettings.interfaceScaleFactor)
			setScale(this.host.getXScale(), this.host.getYScale());
	}

	@Override
	public boolean listensPosition(Point2D testedPosition)
	{
		return pointCollides(testedPosition);
	}

	@Override
	public void onMouseMove(Point2D newMousePosition)
	{
		// Doesn't react to mouse movement
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
	public boolean isDead()
	{
		// Is considered dead if the component is dead
		return (this.host.isDead() || super.isDead());
	}
	
	@Override
	public boolean isVisible()
	{
		// Is considered invisible if the component is invisible
		return (this.host.isVisible() && super.isVisible());
	}
	
	@Override
	public MouseButtonEventScale getCurrentButtonScaleOfInterest()
	{
		return MouseButtonEventScale.LOCAL;
	}
	
	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Point2D mousePosition, double eventStepTime)
	{
		// Scales the object on enter, rescales on exit
		if (eventType == MousePositionEventType.ENTER)
			largen();
		else if (eventType == MousePositionEventType.EXIT)
			rescale();
	}
	
	@Override
	public boolean listensMouseEnterExit()
	{
		return true;
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

	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * @return The component the connector is connected to
	 */
	protected Component getHost()
	{
		return this.host;
	}
	
	/**
	 * @return The spriteDrawer the connector uses
	 */
	protected SingleSpriteDrawer getSpriteDrawer()
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
		if (this.connectedCables.contains(c))
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
		double newscale = Math.pow(GameSettings.interfaceScaleFactor, 2);
		setScale(newscale, newscale);
	}
	
	/**
	 * Rescales the component back to the component's levels
	 */
	private void rescale()
	{
		setScale(this.host.getXScale(), this.host.getYScale());
	}
	
	// Updates the object's absolute position
	private void updateAbsolutePosition()
	{
		Point2D.Double newPosition = this.host.transform(this.relativePoint);
		setPosition(newPosition.getX(), newPosition.getY());
	}
}
