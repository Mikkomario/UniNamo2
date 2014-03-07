package utopia_graphic;

import utopia_gameobjects.DrawnObject;
import utopia_handlers.ActorHandler;

/**
 * SingleSpriteDrawer is a spritedrawer that only draws a single sprite.
 * 
 * @author Mikko Hilpinen. 
 * Created 16.1.2014
 */
public class SingleSpriteDrawer extends SpriteDrawer
{
	// ATTRIBUTES	----------------------------------------------------
	
	private Sprite sprite;
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new singleSpriteDrawer that is bound to the given sprite
	 * 
	 * @param sprite The sprite that the drawer will draw / animate
	 * @param animator The actorhandler that will animate the drawer (optional)
	 * @param user The object the drawer is tied into. The spritedrawer will 
	 * automatically die when the user dies. (Optional) 
	 */
	public SingleSpriteDrawer(Sprite sprite, ActorHandler animator, DrawnObject user)
	{
		super(animator, user);

		// Initializes attributes
		this.sprite = sprite;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	protected Sprite getCurrentSprite()
	{
		return this.sprite;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	

	/**
	 *This method changes the sprite with which the object is represented. The 
	 *image index will be set to 0 in the process.
	 * @param newSprite The new sprite that will be drawn
	 */
	public void setSprite(Sprite newSprite)
	{
		if (newSprite == null)
			return;
		
		this.sprite = newSprite;
		setImageIndex(0);
	}
}
