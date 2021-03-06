package uninamo_obstacles;

import omega_graphic.OpenSpriteBank;
import omega_graphic.Sprite;
import omega_world.Area;
import uninamo_gameplaysupport.TestHandler;

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
			return OpenSpriteBank.getSpriteBank("obstacles").getSprite(spritename);
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
	
	/**
	 * @return The class that represents obstacles of this type
	 */
	public Class<?> getObstacleClass()
	{
		switch (this)
		{
			case BOX: return Box.class;
		}
		
		System.err.println("Couldn't find a class for the obstacleType " + 
				this + ", please update the method");
		return null;
	}
	
	/**
	 * Creates a new obstacle of this type
	 * 
	 * @param x The x-coordinate of the created obstacle
	 * @param y The y-coordinate of the created obstacle
	 * @param area The area where the obstacle will be located at
	 * @param testHandler The testHandler that will inform the obstacle about 
	 * test events
	 * @return A new obstacle of this type
	 */
	public Obstacle getNewObstacle(int x, int y, Area area, TestHandler testHandler)
	{	
		switch (this)
		{
			case BOX: return new Box(area, x, y, testHandler);
		}
		
		System.err.println("Could not create an obstacle of type " + this + 
				", please update the method");
		return null;
	}
}
