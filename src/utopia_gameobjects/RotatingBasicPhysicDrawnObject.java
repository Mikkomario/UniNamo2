package utopia_gameobjects;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import utopia_handleds.PhysicalCollidable;
import utopia_handlers.ActorHandler;
import utopia_handlers.CollidableHandler;
import utopia_handlers.CollisionHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.HelpMath;
import utopia_helpAndEnums.Movement;

/**
 * This class represents physical objects that can be rotated upon collision or 
 * applied force.
 * 
 * @author Mikko Hilpinen
 * @since 18.3.2014
 */
public abstract class RotatingBasicPhysicDrawnObject extends BouncingBasicPhysicDrawnObject
{
	// ATTRIBUTES	------------------------------------------------------
	
	private Point2D.Double currentRotationAxis;
	private int actsSinceLastCollision;
	private double defaultMomentMass, currentMomentMass;
	private ArrayList<Point2D.Double> rotationAxisCandidates;
	
	
	// CONSTRUCTOR	------------------------------------------------------

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
	public RotatingBasicPhysicDrawnObject(int x, int y, int depth,
			boolean isSolid, CollisionType collisiontype,
			DrawableHandler drawer, CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, ActorHandler actorhandler)
	{
		super(x, y, depth, isSolid, collisiontype, drawer, collidablehandler,
				collisionhandler, actorhandler);
		
		// Initializes attributes (Most of these need to be initialized after 
		// the subclass is done)
		this.currentRotationAxis = new Point2D.Double(0, 0);
		this.rotationAxisCandidates = new ArrayList<Point2D.Double>();
		this.actsSinceLastCollision = 1000;
		this.defaultMomentMass = 0;
		this.currentMomentMass = 0;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void act(double steps)
	{
		super.act(steps);
		
		// Also checks if the rotation axis should be set back to origin
		if (this.actsSinceLastCollision < 6)
		{
			this.actsSinceLastCollision ++;
			
			// After 5 acts without collisions, resets the axis back to origin
			if (this.actsSinceLastCollision == 5)
			{
				resetRotationAxisToOrigin();
				//System.out.println("Resets to origin");
			}
		}
		
		// If there are rotation axis candidates, sets their average as the new axis
		if (this.rotationAxisCandidates.size() > 0)
		{
			//System.out.println(rotationAxisCandidates.size());
			changeRotationAxisTo(HelpMath.getAveragePoint(this.rotationAxisCandidates));
			this.rotationAxisCandidates.clear();
		}
	}
	
	@Override
	protected void rotate(double steps)
	{
		// If the current rotation axis is the origin, works just normally
		if (currentAxisIsOrigin())
			super.rotate(steps);
		
		// Otherwise rotates the object around a specific axis
		else
			rotateAroundRelativePoint(getRotation() * steps, 
					this.currentRotationAxis);
	}
		
		
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Sets up the statistics required to run the physics. This method should 
	 * be called as soon as getOriginX(), getOriginY(), getWidth() and getHeight() 
	 * methods work as they are supposed to.
	 */
	protected void setupRotationOrigin()
	{
		this.currentRotationAxis = new Point2D.Double(getOriginX(), getOriginY());
		this.defaultMomentMass = (1.0 / 12.0) * getW2PlusH2();
		this.currentMomentMass = this.defaultMomentMass;
		
		//changeRotationAxisTo(new Point2D.Double(0, 0));
	}
	
	/**
	 * Adds movement to the object but also might make the object rotate. The 
	 * impulse that causes the movement has a specific position and duration
	 * 
	 * @param forceMovement The movement vector of the impulse. Includes the 
	 * direction of the impulse as well as the force (don't apply time modifier here)
	 * @param absoluteForcePosition To which position the force is applied to 
	 * (usually would be inside the object)
	 * @param steps How long the impulse affects the object in steps
	 */
	public void addImpulse(Movement forceMovement, 
			Point2D.Double absoluteForcePosition, double steps)
	{
		// Applies movement like normally
		addMotion(forceMovement.getDirection(), forceMovement.getSpeed() * steps);
		
		// Calculates necessary stats TODO: Create a nice method for these
		
		Point2D.Double absoluteRotationAxis = transform(this.currentRotationAxis);
		double directionToPoint = HelpMath.pointDirection(
				absoluteRotationAxis.getX(), absoluteRotationAxis.getY(), 
				absoluteForcePosition.getX(), absoluteForcePosition.getY());
		double r = HelpMath.pointDistance(absoluteRotationAxis.getX(), 
				absoluteRotationAxis.getY(), absoluteForcePosition.getX(), 
				absoluteForcePosition.getY());
		
		//System.out.println(absoluteRotationAxis);
		//System.out.println(r);
		
		// Testing stronger movements
		//Movement extra = Movement.getMultipliedMovement(forceMovement, 3);
		
		// Also causes rotation
		addMomentWithMovement(forceMovement, directionToPoint, steps, r);		
	}
	
	/**
	 * The object collides with the other object. This collision may change 
	 * the object's rotation. Multiple collision points are used in the 
	 * calculations
	 *
	 * @param p The object collided with
	 * @param collisionPoints The points in which the collision happens (absolute)
	 * @param bounciness How much the object bounces away from the given 
	 * object (0+) (1 means that the object doesn't lose speed in the collision 
	 * and a smaller number means that the object loses speed upon the collision)
	 * @param frictionModifier How much the collision affects speed that isn't 
	 * directional to the opposing force (0+).
	 * @param steps How many steps does the collision take to happen
	 */
	public void bounceWithRotationFrom(PhysicalCollidable p, 
			ArrayList<Point2D.Double> collisionPoints, double bounciness, 
			double frictionModifier, double steps)
	{
		boolean pointsFormALine = false;
		double lineDirection = 0;
		
		// If there are multiple points, checks if they form a line
		if (collisionPoints.size() > 1)
		{
			pointsFormALine = true;
			lineDirection = HelpMath.pointDirection(
					collisionPoints.get(0).getX(), 
					collisionPoints.get(0).getY(), 
					collisionPoints.get(1).getX(), 
					collisionPoints.get(1).getY());
			
			for (int i = 2; i < collisionPoints.size(); i++)
			{
				double line2Direction = HelpMath.pointDirection(
						collisionPoints.get(0).getX(), 
						collisionPoints.get(0).getY(), 
						collisionPoints.get(i).getX(), 
						collisionPoints.get(i).getY());
				if (Math.abs(line2Direction - lineDirection) > 2)
				{
					// TODO: In practice there's always a line here
					pointsFormALine = false;
					break;
				}
			}
		}
		
		// If the collisionPoints form a line, calculates the force direction 
		// a bit differently
		if (pointsFormALine)
		{
			// The direction is tangentual to a line defined by the two points
			double forceDirection = lineDirection + 90;
			double defaultDirection1 = p.getCollisionForceDirection(collisionPoints.get(0));
			double defaultDirection2 = p.getCollisionForceDirection(collisionPoints.get(1));
			// May flip the direction around since it can only be known with the object
			if (HelpMath.getAngleDifference180(forceDirection, defaultDirection1) > 90 
					|| HelpMath.getAngleDifference180(forceDirection, defaultDirection2) > 90)
				forceDirection -= 180;
			
			// If the force direction would "almost" be one of the default force directions, chooses that
			
			if (HelpMath.getAngleDifference180(forceDirection, defaultDirection1) < 10)
				forceDirection = defaultDirection1;
			else if (HelpMath.getAngleDifference180(forceDirection, defaultDirection2) < 10)
				forceDirection = defaultDirection2;
			
			// TODO: Also, if colliding with multiple objects, can get bad
			
			// Adds collisions
			BounceWithRotation(p, forceDirection, HelpMath.getAveragePoint(collisionPoints), bounciness, 
					frictionModifier, steps);
			
			return;
		}
		
		// Collides in all the positions
		for (Point2D.Double colpoint : collisionPoints)
		{
			bounceWithRotationFrom(p, colpoint, 0, 0.25, steps);
		}
	}
	
	/**
	 * The object collides with the other object. This collision may change 
	 * the object's rotation.
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
	public void bounceWithRotationFrom(PhysicalCollidable p, 
			Point2D.Double collisionpoint, double bounciness, 
			double frictionmodifier, double steps)
	{	
		// Calculates the direction, towards which the force is applied
		double forcedir = p.getCollisionForceDirection(collisionpoint);
		
		BounceWithRotation(p, forcedir, collisionpoint, bounciness, 
				frictionmodifier, steps);
	}
	
	private void BounceWithRotation(PhysicalCollidable p, double forceDirection, 
			Point2D.Double collisionpoint, double bounciness, 
			double frictionmodifier, double steps)
	{
		// Collides with the object like usually
		bounceWithoutRotation(p, forceDirection, bounciness, frictionmodifier, 
				steps, negateTransformations(collisionpoint));
		
		// Calculates necessary stuff
		Point2D.Double absoluteRotationAxis = transform(this.currentRotationAxis);
		double directionToPoint = HelpMath.pointDirection(
				absoluteRotationAxis.getX(), absoluteRotationAxis.getY(), 
				collisionpoint.getX(), collisionpoint.getY());
		double r = HelpMath.pointDistance(absoluteRotationAxis.getX(), 
				absoluteRotationAxis.getY(), collisionpoint.getX(), collisionpoint.getY());
		
		// v = rw, dir = pointDir + 90
		Movement oppmovement = Movement.createMovement(directionToPoint + 90, 
				r * getRotation()).getOpposingMovement();
		
		// If the object would be pushed inside the collided object, doesn't 
		// do anything
		//if (HelpMath.getAngleDifference180(oppmovement.getDirection(), forcedir) >= 45)
		//	return;
		
		//if (oppmovement.getSpeed() > 1)
		//	System.out.println(oppmovement.getSpeed());
		
		// Next steps only affect rotating objects
		//if (getRotation() != 0)
		//{	
		// Remembers that the object collided
		this.actsSinceLastCollision = 0;
		
		if (r > 1)
		{
			// Next calculates the rotation stopper
			addOpposingRotation(oppmovement, forceDirection, steps, directionToPoint, r);
			//slowRotationWithMovement(oppmovement, directionToPoint, steps, r);
			// Also rotation friction
			addRotationFriction(oppmovement, frictionmodifier, directionToPoint, 
					steps, r);
			
			//}
			
			// Changes the rotation axis to the collision point and transforms the 
			// rotation
			this.rotationAxisCandidates.add(negateTransformations(collisionpoint));
			//changeRotationAxisTo(negateTransformations(collisionpoint));
		}
	}
	
	private void addOpposingRotation(Movement colPointOppMovement, 
			double oppForceDirection, double steps, 
			double directionToPoint, double r)
	{
		// Calculates the movement of the point (based on the rotation of the object)
		// V = r*w
		//Movement pointMovement = Movement.createMovement(directionToPoint + 90, 
		//		r * getRotation());
		
		// Calculates the opposing force for the movement
		/*
		Movement oppMovement = 
				pointMovement.getOpposingMovement().getDirectionalMovement(
				oppForceDirection);
		*/
		Movement oppMovement = 
				colPointOppMovement.getDirectionalMovement(oppForceDirection);
		
		// If the rotation would push the object to the wrong direction, doesn't 
		// do anything
		if (oppMovement.getDirectionalSpeed(oppForceDirection) < 0)
			return;
		
		// Applies the force
		addMomentWithMovement(oppMovement, directionToPoint, steps, r);
	}
	
	private void addRotationFriction(Movement oppMovement, 
			double frictionModifier, double directionToPoint, double steps, double r)
	{
		// Calculates the friction movement (/ force)
		
		// Checks towards which direction the friction pushes the object
		double frictionDirection = directionToPoint - 90;
		if (getRotation() < 0)
			frictionDirection = directionToPoint + 90;
		
		Movement frictionMovement = Movement.createMovement(frictionDirection, 
				oppMovement.getSpeed() * frictionModifier);
		
		/*
		double frictionSpeed = frictionMovement.getSpeed();
		if (frictionSpeed > 1)
		System.out.println(frictionSpeed);
		*/
		
		// Applies the force
		addMomentWithMovement(frictionMovement, directionToPoint, steps, r);
	}
	
	// Adds the actual rotation
	private void addMomentWithMovement(Movement oppMovement, 
			double directionToPoint, double steps, double r)
	{
		// Calculates the tangentual force ft that actually causes rotation
		Movement tangentualOppForce = oppMovement.getDirectionalMovement(directionToPoint - 90);
		
		// Checks the direction of the tangentualOppForce and checks how it 
		// affects the rotation (positive or negative)
		int rotationSign = -1;
		if (HelpMath.getAngleDifference180(tangentualOppForce.getDirection(), 
				directionToPoint - 90) > 45)
			rotationSign = 1;
		
		// Calculates how much the rotation speed is affected
		// dw = (TanForce * r) / (MomentMass * dt)
		double deltaRotSpeed = (tangentualOppForce.getSpeed() * r) / 
				(this.currentMomentMass * steps);
				/*(12 * tangentualOppForce.getSpeed() * r) / 
				(steps * (Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2)));*/
		
		// Applies the rotation speed change
		addRotation(rotationSign * deltaRotSpeed);
		
		// Adds compensation angle
		rotateAroundRelativePoint(rotationSign * deltaRotSpeed, this.currentRotationAxis);
	}
	
	private void changeRotationAxisTo(Point2D.Double newRelativeAxis)
	{	
		// If the axis is that already, does nothing
		//if (newRelativeAxis.equals(this.currentRotationAxis))
		//	return;
		if (Math.abs(newRelativeAxis.getX() - this.currentRotationAxis.getX()) 
				< 5 && Math.abs(newRelativeAxis.getY() - this.currentRotationAxis.getY()) < 5)
			return;
		
		//System.out.println(Math.abs(newRelativeAxis.getY() - this.currentRotationAxis.getY()));
		
		// If the rotation axis is not the origin, changes it back first
		if (!currentAxisIsOrigin())
			resetRotationAxisToOrigin();
		
		// Calculates the distance to the new axis
		double r2 = Math.pow(HelpMath.pointDistance(getOriginX(), getOriginY(), 
				newRelativeAxis.getX(), newRelativeAxis.getY()), 2);
		
		// Changes the moment mass (Ja = J1 + r^2)
		this.currentMomentMass = this.defaultMomentMass + r2;
		
		// Remembers the current / old rotation speed
		double oldRotSpeed2 = Math.pow(getRotation(), 2);
		
		// Calculates the object's new rotation speed around the new axis
		// wa = sqrt(J1 * w1^2 / Ja)
		double newSpeed = Math.sqrt(this.defaultMomentMass * oldRotSpeed2 / 
				this.currentMomentMass);
		
		/*
		double w2plush2 = getW2PlusH2();
		
		// Calculates the new rotation speed (using a secret method)
		double newSpeed = Math.sqrt(((1.0 / 12.0) * oldRotSpeed2 * w2plush2) 
				/ ((1.0 / 12.0) * w2plush2 + r2));
		*/
		
		// Remembers the new axis
		this.currentRotationAxis = newRelativeAxis;
		
		// Changes the rotation to the new amount
		setRotation(newSpeed);
	}
	
	private void resetRotationAxisToOrigin()
	{
		// wo = sqrt(Ja * wa^2 / J1)
		
		// Calculates the old rotation speed ^2 (wa^2)
		double oldRotSpeed2 = Math.pow(getRotation(), 2);
		
		// Calculates the new speed
		double newSpeed = Math.sqrt(this.currentMomentMass * oldRotSpeed2 / 
				this.defaultMomentMass);
		
		/*
		double r2 = Math.pow(HelpMath.pointDistance(getOriginX(), getOriginY(), 
				this.currentRotationAxis.getX(), this.currentRotationAxis.getY()), 2);
		
		double w2plush2 = getW2PlusH2();
		
		double newSpeed = Math.sqrt(((1.0 / 12.0) * w2plush2 + r2) * 
				oldRotSpeed2 / ((1.0 / 12.0) * w2plush2));
		*/
		
		// Changes back to normal rotation
		this.currentRotationAxis = new Point2D.Double(getOriginX(), getOriginY());
		setRotation(newSpeed);
	}
	
	private double getW2PlusH2()
	{
		return Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2);
	}
	
	private boolean currentAxisIsOrigin()
	{
		return (this.currentRotationAxis.getX() == getOriginX() && 
				this.currentRotationAxis.getY() == getOriginY());
	}
}
