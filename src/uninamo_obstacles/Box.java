package uninamo_obstacles;

import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.Wall;
import utopia_handleds.Collidable;
import utopia_handlers.ActorHandler;
import utopia_handlers.CollidableHandler;
import utopia_handlers.CollisionHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.Material;
import utopia_worlds.Room;

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
	 * @param x The box's x-coordinate
	 * @param y The box's y-coordinate
	 * @param drawer The drawableHandler that will draw the box
	 * @param collidablehandler The collidableHandler that will handle the 
	 * box's collision checking
	 * @param collisionhandler The collisionHandler that will inform the box 
	 * about collision events
	 * @param actorhandler The actorHandler that will inform the box about 
	 * act events
	 * @param room The room where the box resides at
	 * @param testHandler The testHandler that will inform the object about 
	 * test events
	 */
	public Box(int x, int y, DrawableHandler drawer, 
			CollidableHandler collidablehandler, 
			CollisionHandler collisionhandler, ActorHandler actorhandler, 
			Room room, TestHandler testHandler)
	{
		super(x, y, true, CollisionType.BOX, drawer, collidablehandler,
				collisionhandler, actorhandler, room, testHandler, "boxdesign", 
				"boxreal");
		
		setBoxCollisionPrecision(3, 0);
		
		//getMovement().setHSpeed(-5);
		//setScale(1.5, 1.5);
		//System.out.println(getMass());
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
