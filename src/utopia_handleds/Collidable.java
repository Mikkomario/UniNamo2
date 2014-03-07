package utopia_handleds;

import java.awt.geom.Point2D;

/**
 * Collidable objects can collide with each other. They can also be made unsolid 
 * so that collision detection will be ignored.
 *
 * @author Mikko Hilpinen.
 *         Created 18.6.2013.
 */
public interface Collidable extends Handled
{
	/**
	 * Checks whether a point collides with the object
	 * @param absolutepoint The absolute (not in the object's relative space) 
	 * point to be tested
	 * @return Does the point collide with the object
	 */
	public boolean pointCollides(Point2D absolutepoint);
	
	/**
	 * @return Can the object be collided with at this time
	 */
	public boolean isSolid();
	
	/**
	 * Tries to make the object solid so that the objects will collide with it
	 */
	public void makeSolid();
	
	/**
	 * Tries to make the object unsolid so that no object can collide with it
	 */
	public void makeUnsolid();
	
	/**
	 * @return A table containing all the listener classes that should be 
	 * informed about the object. Return null if you don't want to limit 
	 * the class selection.
	 */
	public Class<?>[] getSupportedListenerClasses();
}
