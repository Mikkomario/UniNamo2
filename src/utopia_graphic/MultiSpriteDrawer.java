package utopia_graphic;

import java.util.HashMap;

import utopia_gameobjects.DrawnObject;
import utopia_handlers.ActorHandler;

/**
 * MultiSpriteDrawer can easily change between multiple different sprites to 
 * draw.
 * 
 * @author Mikko Hilpinen. 
 * Created 16.1.2014
 */
public class MultiSpriteDrawer extends SpriteDrawer
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Sprite[] sprites;
	private int currentid;
	private HashMap<String, Integer> keywords;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new MultiSpriteDrawer with the given data
	 * 
	 * @param sprites A table containing the sprites the drawer will draw
	 * @param animator The actorHandler that will animate the sprites (optional)
	 * @param user The object the drawer is tied into. The spritedrawer will 
	 * automatically die when the user dies. (Optional)
	 */
	public MultiSpriteDrawer(Sprite[] sprites, ActorHandler animator, DrawnObject user)
	{
		super(animator, user);
		
		// Initializes attributes
		this.sprites = sprites;
		this.currentid = 0;
		this.keywords = new HashMap<String, Integer>();
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	protected Sprite getCurrentSprite()
	{
		return this.sprites[this.currentid];
	}

	
	// OTHER METHODS	---------------------------------------------------
	
	/**
	 * Changes the currently shown sprite to the sprite with the given 
	 * index in the table
	 * 
	 * @param newIndex The index of the new shown sprite
	 * @param resetImageIndex Should the sprite's animation restart from 
	 * the beginning
	 */
	public void setSpriteIndex(int newIndex, boolean resetImageIndex)
	{
		if (this.sprites == null || this.sprites.length == 0)
			this.currentid = 0;
		
		// If the index is too large / small, loops through the list
		this.currentid = Math.abs(newIndex % this.sprites.length);
		
		if (resetImageIndex)
			setImageIndex(0);
	}
	
	/**
	 * Changes the currently shown sprite to the sprite tied to the given 
	 * keyword
	 * 
	 * @param keyword The keyword that tells which sprite should be changed to. 
	 * Use {@link #setKeyword(String, int)} to add a keyword to a sprite
	 * @param resetImageIndex Should the sprite's animation restart from 
	 * the beginning
	 * @see #setKeyword(String, int)
	 */
	public void setSpriteIndex(String keyword, boolean resetImageIndex)
	{
		// Only works if the keyword has been created
		if (!this.keywords.containsKey(keyword))
		{
			System.err.println("The spritedrawer doesn't have the keyword " + keyword);
			return;
		}
		
		setSpriteIndex(this.keywords.get(keyword), resetImageIndex);
	}
	
	/**
	 * Changes the shown sprite to the next one in the table. If the end of the 
	 * table was reached the index loops
	 * 
	 * @param resetImageIndex 
	 */
	public void changeToNextSprite(boolean resetImageIndex)
	{
		setSpriteIndex(this.currentid + 1, resetImageIndex);
	}
	
	/**
	 * Changes the shown sprite to the last one in the table. If the start of 
	 * the table was reached the index loops
	 * 
	 * @param resetImageIndex
	 */
	public void changeToPreviousSprite(boolean resetImageIndex)
	{
		setSpriteIndex(this.currentid - 1, resetImageIndex);
	}
	
	/**
	 * This method ties a certain keyword to a certain sprite index so that 
	 * the index can be accessed with the keyword.
	 * 
	 * @param keyword The new keyword to be tied to the given index
	 * @param spriteindex The index of the sprite the keyword is tied to
	 * @see #setSpriteIndex(String, boolean)
	 */
	public void setKeyword(String keyword, int spriteindex)
	{
		this.keywords.put(keyword, spriteindex);
	}
}
