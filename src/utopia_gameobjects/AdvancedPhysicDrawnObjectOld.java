package utopia_gameobjects;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import utopia_handlers.ActorHandler;
import utopia_handlers.CollidableHandler;
import utopia_handlers.CollisionHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.Material;
import utopia_helpAndEnums.Movement;

/**
 * AdvancedPhysicDrawnObject is a rotating physic object that also handles 
 * momentums and interactive collisions with other advancedPhysicDrawnObjects
 * 
 * @author Mikko Hilpinen
 * @since 20.3.2014
 * @deprecated This version of the class is old and doesn't work too well
 */
public abstract class AdvancedPhysicDrawnObjectOld extends RotatingBasicPhysicDrawnObject
{
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new object with the given statistics and settings. Remember to 
	 * call the setupRotationOrigin -method after the subclass' costructor is done. 
	 * Also remember to set up the collision points.
	 * 
	 * @param x The x-coordinate of the object's origin
	 * @param y The y-coordinate of the object's origin
	 * @param depth The drawing depth of the object
	 * @param isSolid Can the object currently be collided with
	 * @param collisiontype What kind of shape represents the object
	 * @param drawer The drawer that will draw the object
	 * @param collidablehandler The collidableHandler that will handle the object's 
	 * collision checking
	 * @param collisionhandler The collisionHandler that will inform the object 
	 * about collisions.
	 * @param actorhandler The actorHandler that will inform the object about 
	 * step events
	 */
	public AdvancedPhysicDrawnObjectOld(int x, int y, int depth, boolean isSolid,
			CollisionType collisiontype, DrawableHandler drawer,
			CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, ActorHandler actorhandler)
	{
		super(x, y, depth, isSolid, collisiontype, drawer, collidablehandler,
				collisionhandler, actorhandler);
	}
	

	// ABSTRACT METHODS	------------------------------------------------
	
	/**
	 * @return The "depth" of the object towards the third dimension. For a 
	 * cube it would be same as width or height. (> 0)
	 */
	public abstract int getZHeight();
	
	/**
	 * @return The density of the object. This should also contain, how 
	 * effectively the object contains its collisionType's area (a box or a circle). 
	 * For example, a half circle made of wood would have half the density of wood.
	 * @see Material#getDensity()
	 */
	public abstract double getDensity();
	
	
	// GETTERS & SETTERS 	--------------------------------------------
	
	/**
	 * @return How much the object weights (Kg-ish)
	 */
	public double getMass()
	{
		// For boxes the mass is width * height * depth * density
		if (getCollisionType() == CollisionType.BOX)
			return getWidth() * getHeight() * getZHeight() * getDensity();
		// For circles, returns (4 * Pi * r^3) / 3
		else if (getCollisionType() == CollisionType.CIRCLE)
			return (4 * Math.PI * Math.pow(getRadius(), 3)) / 3.0;
		// Not defined for walls
		System.err.println("Can't calculate mass for object that is neither a box nor a ball");
		return 0;
	}
	
	// TODO: Take scaling into account with mass and moment mass
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Collides with another advanced physic object interactively so that 
	 * both of the objects are affected by the collision.
	 * 
	 * @param p The collided object
	 * @param collisionPoints The points at which the collision happens
	 * @param steps How many steps does the collision take to happen
	 */
	public void collideInteractivelyWith(AdvancedPhysicDrawnObjectOld p, 
			ArrayList<Point2D.Double> collisionPoints/*, double bounciness, 
			double frictionModifier, */,double steps)
	{
		// Collects the necessary data about the collision
		CollisionData data = new CollisionData(collisionPoints, p);
		
		// Applies collisions to each affected point
		for (Point2D.Double effectPoint : data.getData().keySet())
		{
			collideInteractivelyWith(p, effectPoint, 
					data.getData().get(effectPoint), steps);
		}
	}
	
	private void collideInteractivelyWith(AdvancedPhysicDrawnObjectOld p, 
			Point2D.Double collisionPoint, double collisionForceDirection, 
			/*double bounciness, double frictionModifier, */double steps)
	{
		// TODO: Add friction later if this happens to work
		
		// Calculates the momentums of the objects
		double momentumStartThis = getDirectionalMomentum(collisionForceDirection);
		double momentumStartOther = p.getDirectionalMomentum(collisionForceDirection);
		
		// Calculates the total momentum of the objects
		//double totalMomentum = momentumStartThis + momentumStartOther;
		
		// Calculates the new momentums ((2 * m1 * p2 + (m1 - m2) * p1) / (m1 + m2))
		double momentumEndThis = (2 * getMass() * momentumStartOther + 
				(getMass() - p.getMass() * momentumStartThis)) / (getMass() + p.getMass());
		double momentumEndOther = (2 * p.getMass() * momentumStartThis + 
				(p.getMass() - getMass() * momentumStartOther)) / (p.getMass() + getMass());
		
		// Calculates the speed changes of each object
		// Ptot / 2 - Pstart
		//double magicImpulseForceThis = (totalMomentum / 2 - momentumStartThis) / getMass();
		//double magicImpulseForceOther = (totalMomentum / 2 - momentumStartOther) / p.getMass();
		// TODO: Change the 0.8 into a nice variable
		double magicImpulseForceThis = 0.8 * (momentumEndThis - momentumStartThis) / getMass();
		double magicImpulseForceOther = 0.8 * (momentumEndOther - momentumStartOther) / p.getMass();
		
		Movement impulseMovementThis = Movement.createMovement(collisionForceDirection, 
				magicImpulseForceThis);
		Movement impulseMovementOther = Movement.createMovement(collisionForceDirection, 
				magicImpulseForceOther);
		
		// Applies the changes as impulses
		addImpulse(impulseMovementThis, collisionPoint, steps);
		p.addImpulse(impulseMovementOther, collisionPoint, steps);
		
		addPosition(impulseMovementThis);
		p.addPosition(impulseMovementOther);
		
		Point2D.Double relativeColPoint = negateTransformations(collisionPoint);
		int moves = 0;
		while (p.pointCollides(transform(relativeColPoint)) && moves < 10)
		{
			moves ++;
			addPosition(Movement.createMovement(collisionForceDirection, 1));
			//if (getMovement().getDirectionalSpeed(forceDirection) > 0)
			//	setMovement(getMovement().getDirectionalllyDiminishedMovement(forceDirection, 1));
		}
	}
	
	// Calculates the object's momentum on the given axis
	private double getDirectionalMomentum(double direction)
	{
		// P = m * v
		return getMass() * getMovement().getDirectionalSpeed(direction);
		// Also adds the momentum caused by rotation: P2 = vp * J (* m because not taken into J)
		// TODO: test adding this
	}
}
