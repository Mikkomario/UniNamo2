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
 * RotatingBasicPhysicDrawnObject collides with other objects and those 
 * collisions may cause rotation it the object
 * 
 * @author Mikko Hilpinen
 * @since 17.3.2014
 */
public abstract class RotatingBasicPhysicDrawnObject extends BasicPhysicDrawnObject
{
	@SuppressWarnings("javadoc")
	public RotatingBasicPhysicDrawnObject(int x, int y, int depth,
			boolean isSolid, CollisionType collisiontype,
			DrawableHandler drawer, CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, ActorHandler actorhandler)
	{
		super(x, y, depth, isSolid, collisiontype, drawer, collidablehandler,
				collisionhandler, actorhandler);
	}
	
	
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
		// Calculates the direction, towards which the force is applied
		double forcedir = p.getCollisionForceDirection(collisionpoint);
		
		// Divides the object into smaller parts that have their own movements
		MovementParticle[] particles = getMovementParticles();
		
		//System.out.println("--------------------");
		// Tests the particles
		for (int i = 0; i < particles.length; i++)
		{
			//System.out.println("Particle speed: " + 
			//		particles[i].getMovement().getSpeed() + ", direction: " + 
			//		particles[i].getMovement().getDirection());
		}
		
		// Calculates the forces applied to the particles
		Movement[] oppMovements = getOpposingMovements(forcedir, steps, particles);
		
		//System.out.println("--------------------");
		
		// Goes through the movements and applies the effects
		for (int i = 0; i < oppMovements.length; i++)
		{
			// Prints the movements for testing
			//System.out.println(oppMovements[i].getSpeed());
			
			// Applies some of the forces as compensation movement
			if (compenstationMovementFactor != 0)
				addPosition(Movement.getMultipliedMovement(oppMovements[i], 
						compenstationMovementFactor));
			
			// Applies the movements
			bounce(bounciness, frictionmodifier, oppMovements[i], forcedir);
			
			// Applies the momentums
			addMomentum(oppMovements[i], particles[i].getPosition());
		}
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
	
	private void addMomentum(Movement oppMovement, Point2D.Double absoluteForcePosition)
	{
		// Calculates the range
		double r = HelpMath.pointDistance(getX(), getY(), 
				absoluteForcePosition.getX(), absoluteForcePosition.getY());
		
		// Calculates the force tangentual to the object
		double tangentDirection = HelpMath.pointDirection(getX(), getY(), 
				absoluteForcePosition.getX(), absoluteForcePosition.getY()) + 90;
		Movement tangentualMovement = oppMovement.getDirectionalMovement(tangentDirection);
		
		// Adds rotation (M = Fr)
		addRotation(r * tangentualMovement.getSpeed());
	}
	
	private MovementParticle[] getMovementParticles()
	{
		MovementParticle[] particles = new MovementParticle[getCollisionPoints().length];
		
		for (int i = 0; i < getCollisionPoints().length; i++)
		{
			particles[i] = new MovementParticle(getCollisionPoints()[i], this);
		}
		
		return particles;
	}
	
	private static Movement[] getOpposingMovements(double oppForceDirection, 
			double steps, MovementParticle[] particles)
	{
		Movement[] oppMovements = new Movement[particles.length];
		
		for (int i = 0; i < oppMovements.length; i++)
		{
			// The movement is divided with the number of particles because 
			// their mass becomes smaller if there's more particles.
			oppMovements[i] = getOpposingMovement(
					Movement.getMultipliedMovement(particles[i].getMovement(), 
					1.0 / particles.length), oppForceDirection, steps);
		}
		
		return oppMovements;
	}
	
	private static Movement getOpposingMovement(Movement baseMovement, 
			double oppForceDirection, double steps)
	{
		// If there's no speed, doesn't do anything
		if (baseMovement.getSpeed() == 0)
			return new Movement(0, 0);
		
		// Calculates the actual amount of force applied to the object
		Movement oppMovement = Movement.getMultipliedMovement(
				baseMovement.getOpposingMovement().getDirectionalMovement(
				oppForceDirection), steps);
		
		// Checks if the movment would have the right direction 
		// (same as oppForceDirection), otherwise nagates it
		if (HelpMath.getAngleDifference180(oppMovement.getDirection(), 
				oppForceDirection) >= 45)
			return new Movement(0, 0);
		
		// Returns the calculated movement
		return oppMovement;
	}
	
	
	// SUBCLASSES	-----------------------------------------------------
	
	private class MovementParticle
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private Movement movement;
		private Point2D.Double absolutePosition;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		public MovementParticle(Point2D.Double absolutePosition, 
				RotatingBasicPhysicDrawnObject host)
		{
			// Initializes attrbutes
			this.absolutePosition = absolutePosition;
			
			// The movement is the sum of the host's movement and the movement 
			// caused by the rotation of the object
			double tangentualdirection = HelpMath.pointDirection(host.getX(), 
					host.getY(), this.absolutePosition.getX(), 
					this.absolutePosition.getY()) + 90;
			double r = HelpMath.pointDistance(host.getX(), host.getY(), 
					this.absolutePosition.getX(), this.absolutePosition.getY());
			
			//System.out.println("Particle r: " + r + ", dir: " + tangentualdirection);
			
			// V = rw, ei tod oo!
			this.movement = Movement.movementSum(host.getMovement(), 
					Movement.createMovement(tangentualdirection, 
					r * host.getRotation()));
		}
		
		
		// GETTERS & SETTERS	----------------------------------------
		
		public Movement getMovement()
		{
			return this.movement;
		}
		
		public Point2D.Double getPosition()
		{
			return this.absolutePosition;
		}
	}
}
