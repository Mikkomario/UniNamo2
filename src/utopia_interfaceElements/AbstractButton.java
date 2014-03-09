package utopia_interfaceElements;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import utopia_gameobjects.DimensionalDrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_graphic.Sprite;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_listeners.AdvancedMouseListener;
import utopia_listeners.RoomListener;
import utopia_worlds.Room;

/**
 * AbstractButton class implements some of the most common functions all the 
 * buttons in the game have while leaving important functionalities to the 
 * subclasses to handle.<p>
 * 
 * The class uses a sprite to draw itself though it doesn't handle animation
 *
 * @author Mikko Hilpinen.
 *         Created 30.11.2013.
 */
public abstract class AbstractButton extends DimensionalDrawnObject implements 
		AdvancedMouseListener, RoomListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private SingleSpriteDrawer spritedrawer;
	private boolean active;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new button to the given position with the given statistics. 
	 * The button uses the given sprite for drawing itself.
	 *
	 * @param x The new x-coordinate of the button (in pixels)
	 * @param y The new y-coordinate of teh button (in pixels)
	 * @param depth The drawing depth of the button
	 * @param sprite The sprite that is used for drawing the button
	 * @param drawer The drawablehandler that will draw the button (optional)
	 * @param mousehandler The mouseListenerHandler that will inform the object 
	 * about mouse events (optional)
	 * @param room The room that will hold the button (optional)
	 */
	public AbstractButton(int x, int y, int depth, Sprite sprite, 
			DrawableHandler drawer, MouseListenerHandler mousehandler, Room room)
	{
		super(x, y, depth, false, CollisionType.BOX, drawer, null);
		
		// Initializes attributes
		this.active = true;
		
		// Initializes spritedrawer
		this.spritedrawer = new SingleSpriteDrawer(sprite, null, this);
		
		// Adds the button to the handlers
		if (room != null)
			room.addObject(this);
		if (mousehandler != null)
			mousehandler.addMouseListener(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public int getWidth()
	{
		if (this.spritedrawer != null)
			return this.spritedrawer.getSprite().getWidth();
		
		return 0;
	}

	@Override
	public int getHeight()
	{
		if (this.spritedrawer != null)
			return this.spritedrawer.getSprite().getHeight();
		
		return 0;
	}

	@Override
	public int getOriginX()
	{
		if (this.spritedrawer != null)
			return this.spritedrawer.getSprite().getOriginX();
		
		return 0;
	}

	@Override
	public int getOriginY()
	{
		if (this.spritedrawer != null)
			return this.spritedrawer.getSprite().getOriginY();
		
		return 0;
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		if (this.spritedrawer == null)
			return;
		
		// Draws the sprite
		this.spritedrawer.drawSprite(g2d, 0, 0);
	}

	@Override
	public void onRoomStart(Room room)
	{
		// Does nothing
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Dies at the end of the room
		kill();
	}

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
	public boolean listensPosition(Point2D testPosition)
	{
		return pointCollides(testPosition);
	}

	@Override
	public void onMouseMove(Point2D newMousePosition)
	{
		// Does nothing
	}
	
	@Override
	public MouseButtonEventScale getCurrentButtonScaleOfInterest()
	{
		return MouseButtonEventScale.LOCAL;
	}
	
	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		// Doens't limit collided classes
		return null;
	}
	
	/*
	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Point2D mousePosition, double eventStepTime)
	{
		// Changes sprite index when mouse enters or exits the button
		if (eventType == MousePositionEventType.ENTER)
			this.spritedrawer.setImageIndex(1);
		else if (eventType == MousePositionEventType.EXIT)
			this.spritedrawer.setImageIndex(0);
	}
	*/
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * @return The spritedrawer used in drawing the button or null if the button 
	 * is dead
	 */
	protected SingleSpriteDrawer getSpriteDrawer()
	{
		return this.spritedrawer;
	}
}
