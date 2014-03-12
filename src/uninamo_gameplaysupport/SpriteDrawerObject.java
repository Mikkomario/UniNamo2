package uninamo_gameplaysupport;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import utopia_gameobjects.DrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_graphic.Sprite;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;

/**
 * SpriteDrawerObject is a drawnObject that draws a single sprite. It can be 
 * used in situations where multiple sprites need to be drawed with different 
 * transformations
 * 
 * @author Mikko Hilpinen
 */
public class SpriteDrawerObject extends DrawnObject
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private SingleSpriteDrawer spriteDrawer;
	private DrawnObject user;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new SpriteDrawerObject with the given sprite
	 * 
	 * @param depth The used drawing depth
	 * @param drawer The drawableHandler that will draw the sprite
	 * @param animator The actorHandler that will animate the sprite (optional)
	 * @param user The object that uses the drawer
	 * @param sprite The sprite that will be drawn
	 */
	public SpriteDrawerObject(int depth, DrawableHandler drawer, 
			ActorHandler animator, DrawnObject user, Sprite sprite)
	{
		super((int) user.getX(), (int) user.getY(), depth, drawer);
		
		// Initializes attributes
		this.user = user;
		this.spriteDrawer = new SingleSpriteDrawer(sprite, animator, this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public int getOriginX()
	{
		if (this.spriteDrawer == null)
			return 0;
		else
			return this.spriteDrawer.getSprite().getOriginX();
	}

	@Override
	public int getOriginY()
	{
		if (this.spriteDrawer == null)
			return 0;
		else
			return this.spriteDrawer.getSprite().getOriginY();
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// Draws the sprite
		if (this.spriteDrawer != null)
			this.spriteDrawer.drawSprite(g2d, 0, 0);
	}
	
	@Override
	public boolean isDead()
	{
		return super.isDead() || this.user.isDead();
	}
	
	@Override
	public boolean isVisible()
	{
		return super.isVisible() && this.user.isVisible();
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return The spriteDrawer that draws the object's sprite
	 */
	public SingleSpriteDrawer getSpriteDrawer()
	{
		return this.spriteDrawer;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Jumps to a given relative position of another object
	 * 
	 * @param relativeposition The drawer's new position in a relative space
	 * @param other The object who's relative space is used
	 */
	public void goToRelativePosition(Point2D.Double relativeposition, 
			DrawnObject other)
	{
		if (other != null && relativeposition != null)
			setPosition(other.transform(relativeposition));
	}
	
	/**
	 * Changes the object's scaling to cover the given area
	 * 
	 * @param width The new width of the drawn sprite's area
	 * @param height The new height of the drawn sprite's area
	 */
	public void setSize(int width, int height)
	{
		setScale(width / ((double) getSpriteDrawer().getSprite().getWidth()), 
				height / ((double) getSpriteDrawer().getSprite().getHeight()));
	}
}