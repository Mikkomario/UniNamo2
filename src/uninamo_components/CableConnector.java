package uninamo_components;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import uninamo_main.GameSettings;
import utopia_gameobjects.DimensionalDrawnObject;
import utopia_graphic.MultiSpriteDrawer;
import utopia_graphic.Sprite;
import utopia_helpAndEnums.CollisionType;
import utopia_listeners.AdvancedMouseListener;
import utopia_listeners.RoomListener;
import utopia_listeners.TransformationListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
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
	
	private MultiSpriteDrawer spritedrawer;
	private Component host;
	private Point2D.Double relativePoint;
	private ArrayList<Cable> connectedCables;
	private boolean testing, testVersion;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new cableConnector that is tied to the given component.
	 * 
	 * @param area The area where the object will reside at
	 * @param relativex The x-coordinate of the object in relation to the 
	 * component's top-left corner (pixels)
	 * @param relativey The y-coordinate of the object in relation to the 
	 * component's top-left corner (pixels)
	 * @param relay The ConnectorRelay that will keep track of the connector
	 * @param host The component to which the connector is tied to
	 * @param isForTesting If this is true, the connector won't react to mouse 
	 * at all.
	 */
	public CableConnector(Area area, int relativex, int relativey, 
			ConnectorRelay relay, Component host, boolean isForTesting)
	{
		super(0, 0, host.getDepth() - 1, false, CollisionType.CIRCLE, area);
		
		// Initializes attributes
		this.relativePoint = new Point2D.Double(relativex, relativey);
		this.host = host;
		this.testVersion = isForTesting;
		
		Sprite[] sprites = new Sprite[2];
		sprites[0] = MultiMediaHolder.getSpriteBank(
				"components").getSprite("inputconnector");
		sprites[1] = MultiMediaHolder.getSpriteBank(
				"components").getSprite("outputconnector");
		
		this.spritedrawer = new MultiSpriteDrawer(sprites, null, this);
		this.connectedCables = new ArrayList<Cable>();
		
		// Updates radius
		setRadius(2 * this.spritedrawer.getSprite().getWidth() / 3);
		
		// Updates the position
		updateAbsolutePosition();
		
		getSpriteDrawer().setImageSpeed(0);
		
		// Adds the object to the handler(s)
		if (area.getMouseHandler() != null)
			area.getMouseHandler().addMouseListener(this);
		if (relay != null)
			relay.addConnector(this);
	
		host.getTransformationListenerHandler().addListener(this);
	}
	
	
	// ABSTRACT METHODS	-------------------------------------------------
	
	/**
	 * @return The unique identifier of the connector
	 */
	public abstract String getID();
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public boolean isActive()
	{
		return !this.testing;
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
	public boolean listensPosition(Point2D.Double testedPosition)
	{
		return pointCollides(testedPosition);
	}

	@Override
	public void onMouseMove(Point2D.Double newMousePosition)
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
	public boolean isVisible()
	{
		// Is considered invisible if the component is invisible
		// TODO: Throws a nullPointException
		return (this.host != null && this.host.isVisible() && super.isVisible());
	}
	
	@Override
	public MouseButtonEventScale getCurrentButtonScaleOfInterest()
	{
		// Functions differently if is just a test version
		if (this.testVersion)
			return MouseButtonEventScale.NONE;
		
		return MouseButtonEventScale.LOCAL;
	}
	
	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Point2D.Double mousePosition, double eventStepTime)
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
		// Listens to mouse enter & exit except if is just a test version
		return !this.testVersion;
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
	public void kill()
	{
		super.kill();
		
		// Also kills the connected cables
		for (Cable cable : this.connectedCables)
		{
			cable.kill();
		}
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
		if (!isDead() && this.connectedCables.contains(c))
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
