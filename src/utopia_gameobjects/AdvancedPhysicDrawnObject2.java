package utopia_gameobjects;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import utopia_handleds.PhysicalCollidable;
import utopia_handlers.ActorHandler;
import utopia_handlers.CollidableHandler;
import utopia_handlers.CollisionHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.HelpMath;
import utopia_helpAndEnums.Material;
import utopia_helpAndEnums.Movement;

/**
 * AdvancedPhysicDrawnObject is an object that handles advanced physics like 
 * moments and momentums
 * 
 * @author Mikko Hilpinen
 * @since 20.3.2014
 */
public abstract class AdvancedPhysicDrawnObject2 extends BasicPhysicDrawnObject
{
	// ATTRIBUTES	------------------------------------------------------
	
	private Point2D.Double currentRotationAxis;
	private int actsSinceLastCollision;
	private double defaultMomentMass, currentMomentMass;
	private HashMap<Point2D.Double, Double> rotationAxisCandidates;
	private boolean rotationAllowed;
	
	
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
	public AdvancedPhysicDrawnObject2(int x, int y, int depth, boolean isSolid,
			CollisionType collisiontype, DrawableHandler drawer,
			CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, ActorHandler actorhandler)
	{
		super(x, y, depth, isSolid, collisiontype, drawer, collidablehandler,
				collisionhandler, actorhandler);
		
		// Initializes attributes (Most of these need to be initialized after 
		// the subclass is done)
		this.currentRotationAxis = new Point2D.Double(0, 0);
		//this.rotationAxisCandidates = new ArrayList<Point2D.Double>();
		this.rotationAxisCandidates = new HashMap<Point2D.Double, Double>();
		this.actsSinceLastCollision = 1000;
		this.defaultMomentMass = 0;
		this.currentMomentMass = 0;
		this.rotationAllowed = true;
	}
	

	// ABSTRACT METHODS	------------------------------------------------
	
	/**
	 * @return The density of the object. This should also contain, how 
	 * effectively the object contains its collisionType's area (a box or a circle). 
	 * For example, a half circle made of wood would have half the density of wood.
	 * @see Material#getDensity()
	 */
	public abstract double getDensity();
	
	
	// IMPLEMENTED METHODS	--------------------------------------------
	
	@Override
	public void act(double steps)
	{
		super.act(steps);
		
		//System.out.println(getMovement().getSpeed() + " towards " + getMovement().getDirection());
		
		// If rotation is not allowed, doesn't bother to do the rest
		if (!this.rotationAllowed)
			return;
		
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
			//changeRotationAxisTo(HelpMath.getAveragePoint(this.rotationAxisCandidates));
			
			// Uses the candidate wich had the largest point movement
			Point2D.Double bestCandidate = null;
			double bestDistance = -1;
			for (Point2D.Double candidate : this.rotationAxisCandidates.keySet())
			{
				if (this.rotationAxisCandidates.get(candidate) > bestDistance)
				{
					bestCandidate = candidate;
					bestDistance = this.rotationAxisCandidates.get(candidate);
				}
			}
			
			//System.out.println(this.rotationAxisCandidates.size());
			
			//if (bestMovement > 0.1)
			changeRotationAxisTo(bestCandidate);
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
	
	@Override
	public void setXScale(double xScale)
	{
		super.setXScale(xScale);
		// Also updates the moment mass
		updateDefaultMomentMass();
	}
	
	@Override
	public void setYScale(double yScale)
	{
		super.setYScale(yScale);
		// Also updates the moment mass
		updateDefaultMomentMass();
	}
	
	
	// GETTERS & SETTERS 	--------------------------------------------
	
	/**
	 * @return How much the object weights (Kg-ish)
	 */
	public double getMass()
	{
		// For boxes the mass is width * height * depth * density
		if (getCollisionType() == CollisionType.BOX)
			return getWidth() * getXScale() * getHeight() * getYScale() * getDensity();
		// For circles, returns (4 * Pi * r^3) / 3
		else if (getCollisionType() == CollisionType.CIRCLE)
			return Math.PI * Math.pow(getRadius(), 2) * ((getXScale() + getYScale()) / 2);
		// Not defined for walls
		System.err.println("Can't calculate mass for object that is neither a box nor a circle");
		return 0;
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Makes the object never rotate in collision situations. Can be used if 
	 * the object only requires very simple physics
	 */
	protected void disableRotation()
	{
		this.rotationAllowed = false;
	}
	
	/**
	 * Makes the object rotate in collision situations (which is on by default). 
	 * With this you get more realistic physics.
	 */
	protected void allowRotation()
	{
		this.rotationAllowed = true;
	}
	
	/**
	 * Sets up the statistics required to run the physics. This method should 
	 * be called as soon as getOriginX(), getOriginY(), getWidth() and getHeight() 
	 * methods work as they are supposed to.
	 */
	protected void setupRotationOrigin()
	{
		this.currentRotationAxis = new Point2D.Double(getOriginX(), getOriginY());
		updateDefaultMomentMass();
		this.currentMomentMass = this.defaultMomentMass;
		
		//changeRotationAxisTo(new Point2D.Double(0, 0));
	}
	
	/**
	 * Adds movement to the object but also might make the object rotate. The 
	 * impulse that causes the movement has a specific position and duration
	 * 
	 * @param force the force that causes the impulse
	 * @param absoluteForcePosition To which position the force is applied to 
	 * (usually would be inside the object)
	 * @param steps How long the impulse affects the object in steps
	 */
	public void addImpulse(Movement force, 
			Point2D.Double absoluteForcePosition, double steps)
	{
		// Changes the object's velocity (a = F / m), (dv = a * dt) -> dv = F * dt / m
		addMotion(force.getDirection(), force.getSpeed() * steps / getMass());
		
		// Only applies moments if it's allowed
		if (!this.rotationAllowed)
			return;
		
		// Calculates necessary stats
		Point2D.Double absoluteRotationAxis = transform(this.currentRotationAxis);
		double directionToPoint = HelpMath.pointDirection(absoluteRotationAxis, 
				absoluteForcePosition);
		double r = HelpMath.pointDistance(absoluteRotationAxis, absoluteForcePosition);
		
		// Applies moment to the object
		addMoment(force, directionToPoint, r, steps);		
	}
	
	/**
	 * The object collides with the other object but the other object isn't 
	 * affected by the collision. Multiple collision points are used in the 
	 * calculations
	 *
	 * @param p The object collided with
	 * @param collisionPoints The points in which the collision happens (absolute)
	 * @param frictionModifier The friction modifier between the objects
	 * @param steps How many steps does the collision take to happen
	 */
	public void titaniumCollision(PhysicalCollidable p, 
			ArrayList<Point2D.Double> collisionPoints, double frictionModifier, 
			double steps)
	{
		// Collects the necessary data about the collision
		CollisionData data = new CollisionData(collisionPoints, p);
		
		// Applies collisions to each affected point
		for (Point2D.Double effectPoint : data.getData().keySet())
		{
			titaniumCollision(p, data.getData().get(effectPoint), 
					effectPoint, frictionModifier, 0, steps);
		}
	}
	
	private void titaniumCollision(PhysicalCollidable p, double forceDirection, 
			Point2D.Double collisionPoint, double frictionModifier, double energyLossModifier, double steps)
	{
		//System.out.println("Speed before titanium: " + getMovement().getSpeed());
		
		// Changes the rotation axis (if needed)
		if (this.rotationAllowed)
			addCollisionPointAsRotationAxisCandidate(collisionPoint);
		
		// Calculates the new momentum
		double newDirectionalMomentumThis = 
				-getDirectionalMomentum(forceDirection) * energyLossModifier;
		
		// Momentum directional to the normal force goes to 0
		setDirectionalMomentumTo(null, forceDirection, newDirectionalMomentumThis, 0, frictionModifier, 
				steps, collisionPoint);
		
		// Only does the rotations if they are allowed
		
		
		if (this.rotationAllowed)
		{
			//System.out.println("Old Rotation momentum: " + getRotationMomentum());
			
			// Rotation momentum directional to the normal force goes to 0
			// TODO: Return and debug
			double newRotationMomentumThis = 
					-getRotationMomentum() * energyLossModifier;
			
			//System.out.println("New Rotation momentum: " + newRotationMomentumThis);
			
			setRotationMomentumTo(null, forceDirection, newRotationMomentumThis, 0, frictionModifier, 
					steps, collisionPoint);
			
			//System.out.println("Rotation momentum after collision: " + getRotationMomentum());
		}
		
		
		// Also forces the object away from the collided object
		getRelativePointAwayFromObject(p, negateTransformations(collisionPoint), 
				forceDirection, steps);
		/*
		System.out.println("Speed after titanium: " + getMovement().getSpeed());
		System.out.println("**********");
		*/
	}
	
	private void addCollisionPointAsRotationAxisCandidate(Point2D.Double collisionPoint)
	{
		// Changes the rotation axis
		this.actsSinceLastCollision = 0;
		// Calculates the point movement at the collision point and adds it as an axis candidate
		Point2D.Double absoluteRotationAxis = transform(this.currentRotationAxis);
		//double pointMovement = HelpMath.pointDistance(
		//		absoluteRotationAxis, collisionPoint) * Math.abs(getRotation());
		double pointDistance = HelpMath.pointDistance(absoluteRotationAxis, collisionPoint);
		
		if (pointDistance > 10)
			this.rotationAxisCandidates.put(negateTransformations(collisionPoint), pointDistance);
	}
	
	// Note: other can be left null
	private void setDirectionalMomentumTo(AdvancedPhysicDrawnObject2 other, 
			double direction, double newMomentum, 
			double energyLossModifier, double frictionModifier, double steps, 
			Point2D.Double absoluteEffectPoint)
	{
		double momentumStart = getDirectionalMomentum(direction);
		// F = dp / dt
		double momentumDifference = newMomentum * (1 - energyLossModifier) - momentumStart;
		Movement normalForce = Movement.createMovement(direction, momentumDifference / steps);
		
		// If the force would push the object into the wrong direction, stops
		//double normalForceDirectionalSpeed = normalForce.getDirectionalSpeed(direction);
		// TODO: Removing this causes problems when the other object's route is 
		// blocked but needs to be removed otherwise
		// TODO: So add a check for that
		//if (normalForce.getDirectionalSpeed(direction) > 0)
		//{
			// Applies the force and adds friction as well
			addImpulse(normalForce, absoluteEffectPoint, steps);
			addCollisionFriction(other, normalForce, frictionModifier, 
					absoluteEffectPoint, steps);
			/*
			if (this.rotationAllowed)
				addCollisionRotationFriction(other, normalForce, frictionModifier, 
						absoluteEffectPoint, steps);
			*/
			// Also adds compensation movement
			//addPosition(Movement.getMultipliedMovement(normalForce, 1.0 / getMass()));
		//}
	}
	
	// Other can be left null
	private void setRotationMomentumTo(AdvancedPhysicDrawnObject2 other, 
			double forceDirection, double newRotationMomentum, 
			double energyLossModifier, double frictionModifier, double steps, 
			Point2D.Double absoluteEffectPoint)
	{
		double rotationMomentumStart = getRotationMomentum();
		Point2D.Double absoluteAxisPosition = transform(this.currentRotationAxis);
		
		//System.out.println("Start momentum: " + rotationMomentumStart);
		
		// Calculates the direction the effect point would have if it was 
		// rotated by the object
		double pointMovementDirection = HelpMath.pointDirection(absoluteAxisPosition, 
				absoluteEffectPoint) - 90;
		if (getRotation() < 0)
			pointMovementDirection += 180;
		
		//System.out.println(pointMovementDirection);
		// If the point is moving away from the force direction already, does nothing
		/*
		if (HelpMath.getAngleDifference180(pointMovementDirection, forceDirection) < 90)
		{
			System.out.println("Skips force");
			return;
		}
		*/
		
		//System.out.println("Point move direction: " + pointMovementDirection);
		
		// Calculates the force that created the momentum change (F = dp / dt)
		//Movement normalForce = Movement.createMovement(pointMovementDirection, 
		//		(newRotationMomentum * energyLossModifier - rotationMomentumStart) / steps);
		
		// TODO: Testing the new method: F = dp / (r * dt) Where r is from the range from rotation axis
		// Almost works... Or not
		
		double r = HelpMath.pointDistance(absoluteAxisPosition, absoluteEffectPoint);
		
		if (r < 1)
			return;
		
		// TODO: Change direction
		double normalForceAmount = (newRotationMomentum * energyLossModifier - rotationMomentumStart) / (r * steps);
		Movement normalForce = Movement.createMovement(pointMovementDirection + 180, 
				normalForceAmount);
		normalForce = normalForce.getDirectionalMovement(forceDirection);
		
		//System.out.println("R is " + r);
		//System.out.println("causes a force: " + normalForceAmount);
		
		//System.out.println(normalForce.getSpeed());
		
		//System.out.println("Normal force: " + normalForce.getSpeed());
		//System.out.println("-----------------------");
		
		// Applies the force and adds friction as well
		//addImpulse(normalForce, absoluteEffectPoint, steps);
		//addMoment(normalForce, HelpMath.pointDirection(absoluteAxisPosition, 
		//		absoluteEffectPoint), r, steps);
		addCollisionFriction(other, normalForce, frictionModifier, 
				absoluteEffectPoint, steps);
		addCollisionRotationFriction(other, normalForce, frictionModifier, absoluteEffectPoint, steps);
		// Also adds compensation movement
		//addCompensationMovement(normalForce);
	}
	
	/**
	 * Collides with another advanced physic object interactively so that 
	 * both of the objects are affected by the collision.
	 * 
	 * @param p The collided object
	 * @param collisionPoints The points at which the collision happens
	 * @param frictionModifier How much the collision affects speed that isn't 
	 * directional to the opposing force (0+).
	 * @param energyLossModifier How much energy is lost in the collision. [0, 1]
	 * @param steps How many steps does the collision take to happen
	 */
	public void collideWith(AdvancedPhysicDrawnObject2 p, 
			ArrayList<Point2D.Double> collisionPoints, 
			double frictionModifier, double energyLossModifier, double steps)
	{
		// Collects the necessary data about the collision
		CollisionData data = new CollisionData(collisionPoints, p);
		
		// Applies collisions to each affected point
		for (Point2D.Double effectPoint : data.getData().keySet())
		{
			collideWith(p, effectPoint, data.getData().get(effectPoint), 
					frictionModifier, energyLossModifier, steps);
		}
	}
	
	private void collideWith(AdvancedPhysicDrawnObject2 p, 
			Point2D.Double collisionPoint, double forceDirection, 
			double frictionModifier, double energyLossModifier, double steps)
	{
		// TODO: Test if this breaks stuff
		// Changes rotation axis (maybe)
		
		if (this.rotationAllowed)
			addCollisionPointAsRotationAxisCandidate(collisionPoint);
		
		
		// Calculates the new momentums for the objects
		double newMomentumThis = getDirectionalEndMomentumOnCollisionWith(p, 
				forceDirection);
		double newMomentumOther = p.getDirectionalEndMomentumOnCollisionWith(
				this, forceDirection);
		
		/*
		System.out.println("*****************");
		System.out.println("Momentum " + getX() + ": " + 
				getDirectionalMomentum(forceDirection) + " -> " + newMomentumThis);
		System.out.println("Momentum " + p.getX() + ": " + 
				p.getDirectionalMomentum(forceDirection) + " -> " + newMomentumOther);
		*/
		
		// Changes the momentums
		setDirectionalMomentumTo(p, forceDirection, newMomentumThis, 
				energyLossModifier, frictionModifier, steps, collisionPoint);
		p.setDirectionalMomentumTo(this, forceDirection, newMomentumOther, 
				energyLossModifier, frictionModifier, steps, collisionPoint);
		
		// Calculates the new rotation momentums
		
		
		if (this.rotationAllowed)
		{
			double newRotationMomentumThis = getEndRotationMomentumOnCollisionWith(p);
			double newRotationMomentumOther = p.getEndRotationMomentumOnCollisionWith(this);
			
			// Changes the rotation momentums
			// TODO: Return and debug
			setRotationMomentumTo(p, forceDirection, newRotationMomentumThis, energyLossModifier, 
					frictionModifier, steps, collisionPoint);
			p.setRotationMomentumTo(this, forceDirection, newRotationMomentumOther, energyLossModifier, 
					frictionModifier, steps, collisionPoint);
		}
		
			
		// Forces the object out of the other
		getRelativePointAwayFromObject(p, negateTransformations(collisionPoint), 
				forceDirection, steps);
	}
	
	private void getRelativePointAwayFromObject(PhysicalCollidable p, 
			Point2D.Double relativeCollisionPoint, double escapeDirection, double steps)
	{
		int moves = 0;
		// TODO: This doesn't work too well
		while (p.pointCollides(transform(relativeCollisionPoint)) && moves < 100)
		{
			moves ++;
			addPosition(Movement.createMovement(escapeDirection, 0.5 * steps));
		}
	}
	
	// Note: force != acceleration
	/*
	private void addCompensationMovement(Movement force)
	{
		// Calculates the acceleration (F = ma -> a = F / m)
		addPosition(Movement.createMovement(force.getDirection(), 
				force.getSpeed() / getMass()));
	}
	*/
	
	private double getDirectionalEndMomentumOnCollisionWith(
			AdvancedPhysicDrawnObject2 other, double direction)
	{
		// P = m1 * (V1 * (m1 - m2) + 2 * p2) / (m1 + m2)
		/*
		return getMass() * (getMovement().getDirectionalSpeed(direction) * (getMass() - 
				other.getMass()) + 2 * other.getDirectionalMomentum(direction)) / 
				(getMass() + other.getMass());
		*/
		// -> P = (p1 * (m1 - m2) + 2 * p2 * m1) / (m1 + m2)
		return (getDirectionalMomentum(direction) * (getMass() - 
				other.getMass()) + 2 * getMass() * other.getDirectionalMomentum(direction)) / 
				(getMass() + other.getMass());
				
		
		//return (2 * getMass() * other.getDirectionalMomentum(direction) + 
		//		(getMass() - other.getMass() * 
		//		getDirectionalMomentum(direction))) / (getMass() + other.getMass());
	}
	
	private double getEndRotationMomentumOnCollisionWith(AdvancedPhysicDrawnObject2 other)
	{
		// TODO: Remove this
		/*
		return (2 * getMass() * other.getRotationMomentum() + 
				(getMass() - other.getMass() * getRotationMomentum())) / 
				(getMass() + other.getMass());
		*/
		// -> P = (p1 * (m1 - m2) + 2 * p2 * m1) / (m1 + m2)
		return (getRotationMomentum() * (this.currentMomentMass - 
				other.currentMomentMass) + 2 * this.currentMomentMass * other.getRotationMomentum()) / 
				(this.currentMomentMass + other.currentMomentMass);
	}
	
	// Calculates the object's momentum on the given axis
	private double getDirectionalMomentum(double direction)
	{
		// P = m * v
		return getMass() * getMovement().getDirectionalSpeed(direction);
	}
	
	private double getRotationMomentum()
	{
		// the momentum caused by rotation: P = w * J
		//System.out.println("J: " + this.currentMomentMass);
		//System.out.println("Rotation: " + getRotation());
		return getRotation() * this.currentMomentMass;
	}
	
	// Adds the actual rotation
	private void addMoment(Movement force, double directionToEffectPoint, 
			double r, double steps)
	{
		// Calculates the tangentual force ft that actually causes rotation
		Movement tangentualForce = force.getDirectionalMovement(
				directionToEffectPoint + 90);
		
		//System.out.println(tangentualForce.getSpeed());
		
		// Calculates how much the rotation speed is affected
		// dw = (TanForce * r * dt) / MomentMass
		double deltaRotSpeed = (
				tangentualForce.getDirectionalSpeed(directionToEffectPoint + 90) 
				* r * steps) / this.currentMomentMass;
		
		// Applies the rotation speed change
		addRotation(deltaRotSpeed);
		
		if (deltaRotSpeed < 0.01)
		{
			// Skips very small forces
			return;
		}
		
		// Adds compensation angle
		rotateAroundRelativePoint(deltaRotSpeed, this.currentRotationAxis);
	}
	
	private void changeRotationAxisTo(Point2D.Double newRelativeAxis)
	{	
		double oldRotation = getRotation();
		// If the axis is that already, does nothing
		//if (newRelativeAxis.equals(this.currentRotationAxis))
		//	return;
		//if (Math.abs(newRelativeAxis.getX() - this.currentRotationAxis.getX()) 
		//		< 7 && Math.abs(newRelativeAxis.getY() - this.currentRotationAxis.getY()) < 7)
		//	return;
		
		//System.out.println(Math.abs(newRelativeAxis.getY() - this.currentRotationAxis.getY()));
		
		// If the rotation axis is not the origin, changes it back first
		if (!currentAxisIsOrigin())
			resetRotationAxisToOrigin();
		
		// Calculates the distance to the new axis
		//double r2 = Math.pow(HelpMath.pointDistance(getOriginX(), getOriginY(), 
		//		newRelativeAxis.getX(), newRelativeAxis.getY()), 2);
		// With scaling included
		double r2 = Math.pow(HelpMath.pointDistance(0, 0, (getOriginX() - 
				newRelativeAxis.getX()) * getXScale(), (getOriginY() - 
				newRelativeAxis.getY()) * getYScale()), 2);
		
		// Changes the moment mass (Ja = J1 + m * r^2)
		this.currentMomentMass = this.defaultMomentMass + getMass() * r2;
		
		// Remembers the current / old rotation speed
		//double oldRotSpeed2 = Math.pow(getRotation(), 2);
		
		// Calculates the object's new rotation speed around the new axis
		// (wa = sqrt(J1 * w1^2 / Ja)) - OLD VERSION, NOT WORKING (with energy)
		// (wa = J1 * w1 / Ja)
		double newSpeed = this.defaultMomentMass * getRotation() / 
				this.currentMomentMass;
		
		// Remembers the new axis
		this.currentRotationAxis = newRelativeAxis;
		
		// Changes the rotation to the new amount
		setRotation(newSpeed);
		
		System.out.println("Changed rotation axis");
		
		if (getRotation() < 0 && oldRotation > 0 || (getRotation() > 0 && oldRotation < 0))
			System.out.println("Rotation swapped signs!!!!!!!!!!!!!!!!");
	}
	
	private void resetRotationAxisToOrigin()
	{
		// wo = Ja * wa / J1
		
		// Calculates the new speed
		double newSpeed = this.currentMomentMass * getRotation() /
				this.defaultMomentMass;
		
		// Changes back to normal rotation
		this.currentRotationAxis = new Point2D.Double(getOriginX(), getOriginY());
		setRotation(newSpeed);
	}
	
	private double getW2PlusH2()
	{
		return Math.pow(getWidth() * getXScale(), 2) + 
				Math.pow(getHeight() * getYScale(), 2);
	}
	
	private boolean currentAxisIsOrigin()
	{
		return (this.currentRotationAxis.getX() == getOriginX() && 
				this.currentRotationAxis.getY() == getOriginY());
	}
	
	/**
	 * Slows the objects directional movement (tangentual to the force) 
	 * with the given modifier. Should be used upon collision.
	 *
	 * @param other the other advancedPhysicDrawnObject that took part of the 
	 * collision process (null if there is no such object or if the object 
	 * can't be moved / rotated)
	 * @param normalForce The force (N) that causes the friction
	 * @param frictionModifier The friction modifier with which the friction is 
	 * calculated
	 * @param absoluteFrictionPosition The position to which the normal force applies
	 * @param steps How long the friction is added
	 */
	private void addCollisionFriction(AdvancedPhysicDrawnObject2 other, 
			Movement normalForce, double frictionModifier, 
			Point2D.Double absoluteFrictionPosition, double steps)
	{
		// Calculates the friction force
		Movement frictionForce = Movement.createMovement(normalForce.getDirection() + 90, 
				normalForce.getSpeed() * frictionModifier);
		
		// Calculates the collision point's movement
		
		//System.out.println(frictionModifier);
		//System.out.println(frictionForce.getSpeed());
		
		// Calculates the maximum effect the friction can have
		Movement maximumEffect = null;
		if (other == null)
			maximumEffect = getMovement().getDirectionalMovement(
					frictionForce.getDirection()).getOpposingMovement();
		// If the collided object is moving, uses that as the target
		else
			maximumEffect = new Movement(other.getMovement().getHSpeed() - getMovement().getHSpeed(), 
					other.getMovement().getVSpeed() - 
					getMovement().getVSpeed()).getDirectionalMovement(frictionForce.getDirection());
		
		if (other != null)
		{
			System.out.println("Before friction: ------");
			System.out.println(getY() + ": " + getMovement().getHSpeed());
			System.out.println(other.getY() + ": " + other.getMovement().getHSpeed());
			//System.out.println("-> " + getY() + "; " + maximumEffect.getSpeed() + "; " + maximumEffect.getDirection());
		}
		
		// Checks if the friction should affect the other direction
		if (HelpMath.getAngleDifference180(frictionForce.getDirection(), 
				maximumEffect.getDirection()) > 90)
			frictionForce = frictionForce.getOpposingMovement();
		
		// If the acceleration caused by friction would be larger than the 
		// maximum effect's speed, modifies the 
		// friction smaller (a = F / m), F = m * a
		if (frictionForce.getSpeed() / getMass() > maximumEffect.getSpeed())
		{
			//System.out.println("friction before: " + frictionForce.getSpeed());
			frictionForce.setSpeed(maximumEffect.getSpeed() * getMass());
			//System.out.println("Adjusts friction smaller");
			//System.out.println("friction after: " + frictionForce.getSpeed());
		}
		
		//System.out.println(getY() + ": Friction: " + frictionForce.getSpeed() + ", dir: " + frictionForce.getDirection());
		
		//if (frictionForce.getSpeed() > 1)
		//System.out.println("Causes friction " + frictionForce.getSpeed() + " to direction " + frictionForce.getDirection());
		
		// Applies force to the other object (if possible / necessary)
		if (other != null)
		{
			other.addImpulse(frictionForce.getOpposingMovement(), 
					absoluteFrictionPosition, steps);
		}
		
		// Applies the force to the object
		addImpulse(frictionForce, absoluteFrictionPosition, steps);
		// TODO: Check
		//addMotion(fircti.getDirection(), force.getSpeed() * steps / getMass());
		
		if (other != null)
		{
			System.out.println("After friction: ------");
			System.out.println(getY() + ": " + getMovement().getHSpeed());
			System.out.println(other.getY() + ": " + other.getMovement().getHSpeed());
			//System.out.println("-> " + getY() + "; " + maximumEffect.getSpeed() + "; " + maximumEffect.getDirection());
		}
	}
	
	private void addCollisionRotationFriction(AdvancedPhysicDrawnObject2 other, 
			Movement normalForce, double frictionModifier, 
			Point2D.Double absoluteFrictionPosition, double steps)
	{
		// Calculates the friction force
		Movement frictionForce = Movement.createMovement(normalForce.getDirection() + 90, 
				normalForce.getSpeed() * frictionModifier);
		
		// Calculates the collision point's movement
		Point2D.Double absoluteRotationAxis = transform(this.currentRotationAxis);
		double pointDir = HelpMath.pointDirection(absoluteRotationAxis, absoluteFrictionPosition);
		double r = HelpMath.pointDistance(absoluteRotationAxis, absoluteFrictionPosition);
		Movement pointMovement = Movement.createMovement(pointDir + 90, r * getRotation());
		//if (pointMovement.getSpeed() > 0.1)
		//	System.out.println(pointMovement.getSpeed());
		//pointMovement = getMovement();
		//System.out.println("R is " + r);
		
		// TODO: Change the pointMovement back to getMovement() if this doesn't start working
		
		//System.out.println(frictionModifier);
		//System.out.println(frictionForce.getSpeed());
		
		// Calculates the maximum effect the friction can have
		Movement maximumEffect = null;
		if (other == null)
			maximumEffect = pointMovement.getDirectionalMovement(
					frictionForce.getDirection()).getOpposingMovement();
		// If the collided object is moving, uses that as the target
		else
			maximumEffect = new Movement(other.getMovement().getHSpeed() - pointMovement.getHSpeed(), 
					other.getMovement().getVSpeed() - 
					pointMovement.getVSpeed()).getDirectionalMovement(frictionForce.getDirection());
		
		// Checks if the friction should affect the other direction
		if (HelpMath.getAngleDifference180(frictionForce.getDirection(), 
				maximumEffect.getDirection()) > 90)
			frictionForce = frictionForce.getOpposingMovement();
		
		// If the acceleration caused by friction would be larger than the 
		// maximum effect's speed, modifies the 
		// friction smaller (a = F / m), F = m * a
		if (frictionForce.getSpeed() / getMass() > maximumEffect.getSpeed())
			frictionForce.setSpeed(maximumEffect.getSpeed() * getMass());
		
		//if (frictionForce.getSpeed() > 1)
		//System.out.println("Causes friction " + frictionForce.getSpeed() + " to direction " + frictionForce.getDirection());
		
		// Applies force to the other object (if possible / necessary)
		if (other != null)
		{
			other.addImpulse(frictionForce.getOpposingMovement(), 
					absoluteFrictionPosition, steps);
		}
		
		// Applies the force to the object
		addMoment(frictionForce, pointDir, r, steps);
	}
	
	/**
	 * This method should be called when the width, height or the density of 
	 * the object changes
	 */
	protected void updateDefaultMomentMass()
	{
		double oldMomentMass = this.defaultMomentMass;
		this.defaultMomentMass = (1.0 / 12.0) * getW2PlusH2() * getMass();
		
		// If the moment mass changed while the object is rotating, updates the 
		// rotation speed
		if (oldMomentMass != this.defaultMomentMass && getRotation() != 0)
			resetRotationAxisToOrigin();
	}
	
	
	// SUBCLASSES	-----------------------------------------------------
	
	/**
	 * CollisionData calculates the necessary data for collision handling: 
	 * the collision effect points as well as the directions towards which 
	 * the force is applied at each point.
	 * 
	 * @author Mikko Hilpinen
	 * @since 20.3.2014
	 */
	protected class CollisionData
	{
		// ATTRIBUTES	------------------------------------------------
		
		private HashMap<Point2D.Double, Double> effectPointsAndDirections;
		
		
		// CONSTRUCTOR	------------------------------------------------
		
		/**
		 * Creates a new CollisionData using the given collision points and 
		 * collided object
		 * 
		 * @param collisionPoints The points at which the collision happens
		 * @param p The collided object
		 */
		protected CollisionData(ArrayList<Point2D.Double> collisionPoints, PhysicalCollidable p)
		{
			this.effectPointsAndDirections = new HashMap<Point2D.Double, Double>();
			
			// Calculates the stuff
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
				
				// The effect's point is formed with an average value
				this.effectPointsAndDirections.put(HelpMath.getAveragePoint(collisionPoints), forceDirection);
			}
			// Otherwise uses separate collision directions for all the points
			else
			{
				for (Point2D.Double colPoint : collisionPoints)
				{
					this.effectPointsAndDirections.put(colPoint, 
							p.getCollisionForceDirection(colPoint));
				}
			}
		}
		
		
		// GETTERS & SETTERS	------------------------------------------
		
		/**
		 * @return The data the onject has collected. The keys of the map are 
		 * affected collision points and the values are directions towards 
		 * which the force is applied at that point.
		 */
		protected HashMap<Point2D.Double, Double> getData()
		{
			return this.effectPointsAndDirections;
		}
	}
	/*
	private class TitaniumCollisionReminder
	{
		// ATTRIBUTES	--------------------------------------------------
		
		private int timeLeft;
		private double directionToTitanium;
	}
	*/
}
