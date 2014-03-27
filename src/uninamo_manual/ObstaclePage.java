package uninamo_manual;

import uninamo_gameplaysupport.SpriteDrawerObject;
import uninamo_obstacles.ObstacleType;
import utopia_gameobjects.GameObject;
import utopia_helpAndEnums.DepthConstants;
import utopia_worlds.Area;

/**
 * ObstaclePage shows information about a certain obstacle
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public class ObstaclePage extends DescriptionPage
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private SpriteDrawerObject imageDrawer;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new obstaclePage to the given position
	 * 
	 * @param x The x-coordinate of the center of the page
	 * @param y The y-coordinate of the center of the page
	 * @param area The area where the page is located at
	 * @param featuredType The type of obstacle shown on the page
	 * @param dataHodler The obstacleInfoHolder that provides information 
	 * shown on the page
	 */
	public ObstaclePage(int x, int y, Area area, ObstacleType featuredType, 
			ObstacleInfoHolder dataHodler)
	{
		super(x, y, area, dataHodler.getObstacleDescription(featuredType), 
				featuredType.getName());
		
		// Initializes attributes
		this.imageDrawer = new SpriteDrawerObject(area, DepthConstants.FOREGROUND, 
				this, featuredType.getSprite());
		double scale = 150.0 / this.imageDrawer.getSpriteDrawer().getSprite().getHeight();
		this.imageDrawer.setScale(scale, scale);
		this.imageDrawer.setPosition(getX(), getY() - 100);
		
		setInvisible();
	}
	
	
	// IMPLEMENTED METHODS	-----------------------------------------------

	@Override
	protected GameObject createTestObject(Area area)
	{
		// Doesn't create a testObject
		return null;
	}

	@Override
	public void kill()
	{
		// Also kills the imageDrawer
		this.imageDrawer.kill();
		
		super.kill();
	}
}
