package uninamo_gameplay;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import utopia_gameobjects.DimensionalDrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.DepthConstants;
import utopia_listeners.AdvancedMouseListener;
import utopia_resourcebanks.MultiMediaHolder;

/**
 * Components are key elements in the game. Components can be placed on the 
 * workbench and together they form logical systems. Components are connected 
 * with cables and send, process and / or receive signal(s) using 
 * signalprocessors. Components can be moved around by dragging and dropping.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public class Component extends DimensionalDrawnObject implements
		AdvancedMouseListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private SingleSpriteDrawer spritedrawer;
	// TODO: Add signalprocessorlists
	private boolean active;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new component to the given position.
	 * 
	 * @param x The new x-coordinate of the component's origin (pixels)
	 * @param y The new y-coordinate of the component's origin (pixels)
	 * @param drawer The drawableHandler that will draw the component
	 * @param actorhandler The actorHandler that will animate the component
	 * @param mousehandler The mouseListenerHandler that will inform the 
	 * object about mouse events
	 * @param spritename The name of the component sprite used to draw the 
	 * component
	 */
	public Component(int x, int y, DrawableHandler drawer, 
			ActorHandler actorhandler, MouseListenerHandler mousehandler, 
			String spritename)
	{
		super(x, y, DepthConstants.NORMAL, false, CollisionType.BOX, drawer, 
				null);
		
		// Initializes attributes
		this.spritedrawer = new SingleSpriteDrawer(
				MultiMediaHolder.getSpriteBank("components").getSprite(
				spritename), actorhandler, this);
		this.active = true;
		
		// Adds the object to the handler(s)
		if (mousehandler != null)
			mousehandler.addMouseListener(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

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
		// Doesn't limit collisionListeners
		return null;
	}

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D mousePosition,
			double eventStepTime)
	{
		// TODO: Add drag & drop and other features
	}

	@Override
	public boolean listensPosition(Point2D testedPosition)
	{
		return pointCollides(testedPosition);
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		return true;
	}

	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Point2D mousePosition, double eventStepTime)
	{
		// TODO: Limit this if the component is being dragged
		if (eventType == MousePositionEventType.ENTER)
			setScale(1.2, 1.2);
		else if (eventType == MousePositionEventType.EXIT)
			setScale(1, 1);
	}

	@Override
	public void onMouseMove(Point2D newMousePosition)
	{
		// Doesn't react to mouse movement
	}

	@Override
	public MouseButtonEventScale getCurrentButtonScaleOfInterest()
	{
		// TODO Change to global when the component is being dragged
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
		// Draws the sprite
		if (this.spritedrawer == null)
			return;
		this.spritedrawer.drawSprite(g2d, 0, 0);
	}
}
