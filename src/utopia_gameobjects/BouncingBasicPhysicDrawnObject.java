package utopia_gameobjects;

import java.awt.geom.Point2D;

import utopia_handleds.PhysicalCollidable;
import utopia_handlers.ActorHandler;
import utopia_handlers.CollidableHandler;
import utopia_handlers.CollisionHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.HelpMath;
import utopia_helpAndEnums.Movement;

/**
 * This is a direct extension of the basicphysicobject class that can bounce 
 * from other objects (without moments) but doesn't cause rotation upon 
 * collision nor takes rotation speed into account.
 *
 * @author Mikko Hilpinen.
 *         Created 24.8.2013.
 */
public abstract class BouncingBasicPhysicDrawnObject extends BasicPhysicDrawnObject
{
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new physicobject with the given information. The object will
	 * be static until motion is applied. There's no friction or rotation friction 
	 * either until those are added. The object is active at default.
	 *
	 * @param x The ingame x-coordinate of the new object
	 * @param y The ingame y-coordinate of the new object
	 * @param depth How 'deep' the object is drawn
	 * @param isSolid Can the object be collided with
	 * @param collisiontype What is the shape of the object collisionwise
	 * @param drawer The drawablehandler that draws the object (optional)
	 * @param collidablehandler The collidablehandler that handles the object's 
	 * collision checking (optional)
	 * @param collisionhandler Collisionhandler that informs the object about collisions (optional)
	 * @param actorhandler The actorhandler that calls the object's act event (optional)
	 */
	public BouncingBasicPhysicDrawnObject(int x, int y, int depth,
			boolean isSolid, CollisionType collisiontype,
			DrawableHandler drawer, CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, ActorHandler actorhandler)
	{
		super(x, y, depth, isSolid, collisiontype, drawer, collidablehandler,
				collisionhandler, actorhandler);
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Slows the objects directional movement (tangentual to the opposing force) 
	 * with the given modifier. Should be used upon collision and is for example, 
	 * used in the bounce methods.
	 *
	 * @param oppmovement The opposing force (N) that causes the friction
	 * @param frictionmodifier f with which the friction is calculated [0, 1]
	 * @see bounceWithoutRotationFrom
	 */
	protected void addWallFriction(Movement oppmovement, double frictionmodifier)
	{
		double friction = oppmovement.getSpeed() * frictionmodifier;
		// Diminishes the speed that was not affected by the oppposing force
		setMovement(getMovement().getDirectionalllyDiminishedMovement(
				oppmovement.getDirection() + 90, friction));
	}
	
	/**
	 * The object bounces from a certain object it collides with. This doesn't 
	 * cause rotational movement.
	 *
	 * @param p The object collided with
	 * @param collisionpoint The point in which the collision happens (absolute)
	 * @param bounciness How much the object bounces away from the given 
	 * object (0+) (1 means that the object doesn't lose speed in the collision 
	 * and a smaller number means that the object loses speed upon the collision)
	 * @param frictionmodifier How much the collision affects speed that isn't 
	 * directional to the opposing force (0+).
	 * @param steps How many steps does the collision take to happen
	 */
	public void bounceWithoutRotationFrom(PhysicalCollidable p, 
			Point2D.Double collisionpoint, double bounciness, 
			double frictionmodifier, double steps)
	{	
		// If there's no speed, doesn't do anything
		if (getMovement().getSpeed() == 0)
			return;
		
		// Calculates the direction, towards which the force is applied
		double forcedir = p.getCollisionForceDirection(collisionpoint);
		Point2D.Double relativeColPoint = negateTransformations(collisionpoint);
		
		bounceWithoutRotation(p, forcedir, bounciness, frictionmodifier, steps, relativeColPoint);
	}
	
	/**
	 * Causes opposing movement towards the given direction.
	 * @param p The physical object that was collided with
	 * @param forceDirection The direction towards which the opposing movement 
	 * is applied
	 * @param bounciness How much the object bounces away from the given 
	 * object (0+) (1 means that the object doesn't lose speed in the collision 
	 * and a smaller number means that the object loses speed upon the collision)
	 * @param frictionModifier How much the collision affects speed that isn't 
	 * directional to the opposing force (0+).
	 * @param steps How many steps does the collision take to happen
	 * @param relativeColPoint What was the relative collision position this object 
	 * collided the other with
	 */
	protected void bounceWithoutRotation(PhysicalCollidable p, double forceDirection, 
			double bounciness, double frictionModifier, double steps, 
			Point2D.Double relativeColPoint)
	{
		// Calculates the actual amount of force applied to the object
		Movement oppmovement = Movement.getMultipliedMovement(
				getMovement().getOpposingMovement().getDirectionalMovement(
				forceDirection), steps);
		
		// If the object would be pushed inside the collided object, doesn't 
		// do anything
		//if (HelpMath.getAngleDifference180(oppmovement.getDirection(), forceDirection) >= 45)
		if (oppmovement.getDirectionalSpeed(forceDirection) <= 0)
			return;
		
		// Applies some of the force as compensation movement
		addPosition(oppmovement);
		int moves = 0;
		while (p.pointCollides(transform(relativeColPoint)) && moves < 10)
		{
			moves ++;
			addPosition(Movement.createMovement(forceDirection, 1));
			//if (getMovement().getDirectionalSpeed(forceDirection) > 0)
			//	setMovement(getMovement().getDirectionalllyDiminishedMovement(forceDirection, 1));
		}
		
		// Adds a bit extra to push the object away
		//addPosition(Movement.createMovement(oppmovement.getDirection(), 1));
		
		// Adds the actual force
		bounce(bounciness, frictionModifier, oppmovement, forceDirection);
	}
	
	private void bounce(double bounciness, double frictionmodifier, 
			Movement oppmovement, double forcedir)
	{
		double force = bounciness * (oppmovement.getSpeed());
		
		// Adds the opposing force and the force (if they are not negative)
		if (HelpMath.getAngleDifference180(
				oppmovement.getDirection(), forcedir) < 90)
		{
			addMotion(forcedir, oppmovement.getSpeed());
			if (force > 0)
				addMotion(forcedir, force);
			
			// Also adds friction if needed
			if (frictionmodifier > 0)
				addWallFriction(oppmovement, frictionmodifier);
		}
	}
	
	/*
	private void addCollisionFriction()
	{
		// The object has a speed (to oppforce +- 90 direction) (1)
		// The other object also has speed (to that direction) (2)
		// The object should move itself along the speed difference vector 1 - 2 multiplied 
		// with friction modifier a
	}
	*/
}
