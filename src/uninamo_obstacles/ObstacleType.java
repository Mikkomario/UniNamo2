package uninamo_obstacles;

import utopia_graphic.Sprite;
import utopia_resourcebanks.MultiMediaHolder;

/**
 * ObstacleTypes represents different types of obstacles that often have their 
 * own classes as well.
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public enum ObstacleType
{
	/**
	 * Box is simply a piece of wood (?)
	 */
	BOX;
	
	
	// METHODS	----------------------------------------------------------
	
	/**
	 * @return The sprite used to draw the "real" version of an 
	 * instance of this obstacle type
	 */
	public Sprite getSprite()
	{
		String spritename = null;
		
		switch (this)
		{
			case BOX: spritename = "boxreal"; break;
		}
		
		if (spritename != null)
			return MultiMediaHolder.getSpriteBank("obstacles").getSprite(spritename);
		else
		{
			System.out.println("Couldn't find the sprite for " + this + 
					", please update the method");
			return null;
		}
	}
	
	/**
	 * @return The name used for the obstacles of this type
	 */
	public String getName()
	{
		switch (this)
		{
			case BOX: return "The (epic) box";
		}
		
		System.err.println("Couldn't find a name for " + this + 
				", please update the method");
		return "Name unknown";
	}
}
