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

@SuppressWarnings("javadoc")
public abstract class RotatingBasicPhysicDrawnObject2 extends BasicPhysicDrawnObject
{

	public RotatingBasicPhysicDrawnObject2(int x, int y, int depth,
			boolean isSolid, CollisionType collisiontype,
			DrawableHandler drawer, CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, ActorHandler actorhandler)
	{
		super(x, y, depth, isSolid, collisiontype, drawer, collidablehandler,
				collisionhandler, actorhandler);
		// TODO Auto-generated constructor stub
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
	 * @param compenstationMovementFactor How much the colliding object is 
	 * pushed back from the other object at the moment of collision. The pushed 
	 * amount depends on the collision force and is calculated with in the 
	 * following way: x = opposing force * compoensationMovementFactor, 0 means 
	 * that there is no compensation movement
	 * @param steps How many steps does the collision take to happen
	 */
	public void bounceWithRotationFrom(PhysicalCollidable p, 
			Point2D.Double collisionpoint, double bounciness, 
			double frictionmodifier, double compenstationMovementFactor, 
			double steps)
	{	
		// If there's no speed, doesn't do anything
		if (getMovement().getSpeed() == 0)
			return;
		
		// Calculates the direction, towards which the force is applied
		double forcedir = p.getCollisionForceDirection(collisionpoint);
		
		// Calculates the actual amount of force applied to the object
		Movement oppmovement = Movement.getMultipliedMovement(
				getMovement().getOpposingMovement().getDirectionalMovement(
				forcedir), steps);
		
		// If the object would be pushed inside the collided object, doesn't 
		// do anything
		if (HelpMath.getAngleDifference180(oppmovement.getDirection(), forcedir) >= 45)
			return;
		
		// Applies some of the force as compensation movement
		if (compenstationMovementFactor != 0)
		{
			addPosition(Movement.getMultipliedMovement(oppmovement, 
					compenstationMovementFactor));
		}
		
		bounce(bounciness, frictionmodifier, oppmovement, forcedir);
		
		// Next calculates the rotation stopper
		addOpposingRotation(collisionpoint, forcedir, steps);
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
	
	private void addOpposingRotation(Point2D.Double collisionPoint, 
			double oppForceDirection, double steps)
	{
		// If there is no rotation, does nothing
		if (getRotation() == 0)
			return;
		
		// Calculates the movement of the point (based on the rotation of the object)
		// V = r*w
		double directionToPoint = HelpMath.pointDirection(getX(), getY(), collisionPoint.getX(), 
				collisionPoint.getY());
		double r = HelpMath.pointDistance(getX(), getY(), 
				collisionPoint.getX(), collisionPoint.getY());
		Movement pointMovement = Movement.createMovement(directionToPoint + 90, 
				r * getRotation());
		
		// Calculates the opposing force for the movement
		Movement oppMovement = 
				pointMovement.getOpposingMovement().getDirectionalMovement(
				oppForceDirection);
		
		// If the rotation would push the object to the wrong direction, doesn't 
		// do anything
		if (HelpMath.getAngleDifference180(oppMovement.getDirection(), 
				oppForceDirection) >= 45)
			return;
		
		// Calculates the tangentual force ft (for some reason the tangent goes to the other direction this time)
		Movement tangentualOppForce = oppMovement.getDirectionalMovement(directionToPoint - 90);
		
		// Checks the direction of the tangentualOppForce and checks if it affects the rotation direction
		int rotationSign = -1;
		if (HelpMath.getAngleDifference180(tangentualOppForce.getDirection(), directionToPoint - 90) > 45)
			rotationSign = 1;
		
		double deltaRotSpeed = (12 * tangentualOppForce.getSpeed() * r) / 
				(steps * (Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2)));
		
		// Applies the rotation speed change
		addRotation(rotationSign * deltaRotSpeed);
		
		// Adds compoensation angle
		addAngle(rotationSign * deltaRotSpeed);
	}
}