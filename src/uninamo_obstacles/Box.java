package uninamo_obstacles;

import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.Wall;
import utopia_handleds.Collidable;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.Material;
import utopia_worlds.Area;

/**
 * Box is a simple obstacle that does pretty much nothing but collides with walls
 * 
 * @author Mikko Hilpinen
 */
public class Box extends Obstacle implements Wall
{
	/**
	 * Creates a new box to the given position
	 * 
	 * @param area The area where the object will reside at
	 * @param x The box's x-coordinate
	 * @param y The box's y-coordinate
	 * @param testHandler The testHandler that will inform the object about 
	 * test events
	 */
	public Box(Area area, int x, int y, TestHandler testHandler)
	{
		super(area, x, y, true, CollisionType.BOX, testHandler, "boxdesign", 
				"boxreal");
		
		setBoxCollisionPrecision(3, 0);
		//setAngle(30);
		disableRotation();
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	protected void resetStatus()
	{
		// Does nothing
		//getMovement().setHSpeed(-5);
	}

	@Override
	protected void onSpecialCollision(ArrayList<Double> colpoints,
			Collidable collided, double steps)
	{
		// Does nothing
	}

	@Override
	public double getDensity()
	{
		return Material.WOOD.getDensity() * 0.2;
	}
}
