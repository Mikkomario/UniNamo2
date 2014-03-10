package utopia_handleds;

import java.awt.geom.Point2D;

/**
 * Physical collidables are collidables that can be collided with in a physical 
 * way. In other words, they can apply force to another object and are 
 * dimensionally restricted.
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public interface PhysicalCollidable extends Collidable
{
	/**
	 * Calculates the direction towards which the force caused by a collision 
	 * applies.
	 * 
	 * @param collisionpoint The point at which the collision happens
	 * @return Towards which direction should the force apply to
	 */
	public double getCollisionForceDirection(Point2D.Double collisionpoint);
}
