package utopia_listeners;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import utopia_handleds.Collidable;
import utopia_handleds.LogicalHandled;
import utopia_handlers.CollisionHandler;

/**
 * Collisionlisteners are interested in collisions and react to them somehow. 
 * Each collisionlistener provides a set of collision points it listens to.<br>
 * Remember to add the object to a CollisionHandler
 *
 * @author Mikko Hilpinen.
 *         Created 18.6.2013.
 * @see CollisionHandler
 */
public interface CollisionListener extends LogicalHandled
{
	/**
	 * @return The points which are used in the collision tests. Larger tables 
	 * are a lot more precise but also much slower. The points should be 
	 * absolute in-game pixels
	 */
	public Point2D.Double[] getCollisionPoints();
	
	/**
	 * This method is called each time the listening object collides with 
	 * an object
	 * @param colpoints The points in which the collision(s) happened (absolute)
	 * @param collided The object with which the collision(s) happened
	 */
	public void onCollision(ArrayList<Point2D.Double> colpoints, Collidable collided);
}
