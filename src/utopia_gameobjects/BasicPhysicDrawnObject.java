package utopia_gameobjects;

import utopia_handleds.Actor;
import utopia_handlers.ActorHandler;
import utopia_handlers.CollidableHandler;
import utopia_handlers.CollisionHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.Movement;

/**
 * In addition to CollidingDrawnObject's abilities Physicobject handles 
 * basic physical methods like moving and rotating
 *
 * @author Mikko Hilpinen.
 *         Created 28.11.2012.
 */
public abstract class BasicPhysicDrawnObject extends CollidingDrawnObject 
		implements Actor
{	
	// ATTRIBUTES	------------------------------------------------------
	
	private double rotation, friction, rotFriction, maxspeed, 
			maxrotation;
	private Movement movement;
	
	
	// CONSTRUCTOR	------------------------------------------------------

	/**
	 * Creates a new physicobject with the given information. The object will 
	 * be static until motion is applied. There's no friction or rotation friction 
	 * until those are added. The object is active by default.
	 *
	 * @param x The ingame x-coordinate of the new object
	 * @param y The ingame y-coordinate of the new object
	 * @param depth How 'deep' the object is drawn
	 * @param isSolid Can the object be collided with
	 * @param collisiontype What is the shape of the object collisionwise
	 * @param drawer The drawablehandler that draws the object (optional)
	 * @param collidablehandler The collidablehandler that handles the object's 
	 * collision checking (optional)
	 * @param collisionhandler Collisionhandler that informs the object about 
	 * collisions (optional)
	 * @param actorhandler The actorhandler that calls the object's act 
	 * event (optional)
	 */
	public BasicPhysicDrawnObject(int x, int y, int depth, boolean isSolid, 
			CollisionType collisiontype, DrawableHandler drawer, 
			CollidableHandler collidablehandler, CollisionHandler collisionhandler, 
			ActorHandler actorhandler)
	{
		super(x, y, depth, isSolid, collisiontype, drawer, collidablehandler, 
				collisionhandler);
		
		// Initializes attributes
		this.movement = new Movement(0, 0);
		this.rotation = 0;
		this.friction = 0;
		this.rotFriction = 0;
		this.maxspeed = -1;
		this.maxrotation = -1;
		
		// Adds the object to the actorhandler if possible
		if (actorhandler != null)
			actorhandler.addActor(this);
	}
	
	
	// IMPLEMENTED METHODS	-----------------------------------------------
	
	@Override
	public void act(double steps)
	{
		// Handles the movement of the object
		move(steps);
		rotate(steps);
		implyRotationFriction(steps);
	}
	
	
	// GETTERS & SETTERS	-----------------------------------------------
	
	/**
	 * @return The current movement of the object
	 */
	public Movement getMovement()
	{
		return this.movement;
	}
	
	/**
	 * Changes the object's movement speed
	 *
	 * @param hspeed The new horizontal speed (pxl / step)
	 * @param vspeed The new vertical speed (pxl / step)
	 */
	public void setVelocity(double hspeed, double vspeed)
	{
		this.movement = new Movement(hspeed, vspeed);
	}
	
	/**
	 * Changes the object's movement
	 *
	 * @param movement The object's new movement
	 */
	public void setMovement(Movement movement)
	{
		this.movement = movement;
	}
	
	/**
	 * Adds some horizontal and vertical motion to the object. The movement stacks 
	 * with the previous movement speed.
	 *
	 * @param haccelration How much speed is increased horizontally (pxl / step)
	 * @param vacceltarion How much speed is increased vertically (pxl / step)
	 */
	public void addVelocity(double haccelration, double vacceltarion)
	{
		this.movement = Movement.movementSum(getMovement(), 
				new Movement(haccelration, vacceltarion));
	}
	
	/**
	 * @return How much the object is rotated at each step (degrees / step)
	 */
	public double getRotation()
	{
		return this.rotation;
	}
	
	/**
	 * Changes how fast the object rotates around its origin
	 *
	 * @param rotation The speed with which the object rotates (degrees / step)
	 */
	public void setRotation(double rotation)
	{
		this.rotation = rotation;
	}
	
	/**
	 * @return How much the object's speed is reduced at each step (pxl / step)
	 */
	public double getFriction()
	{
		return this.friction;
	}
	
	/**
	 * Changes how much the object's speed is reduced at each step
	 *
	 * @param friction the new friction of the object (pxl / step)
	 */
	public void setFriction(double friction)
	{
		this.friction = friction;
	}
	
	/**
	 * @return How much the rotation of the object is reduced at each step
	 */
	public double getRotationFriction()
	{
		return this.rotFriction;
	}
	
	/**
	 * Changes how much the rotation of the object is reduced at each step
	 * 
	 * @param rotationFriction How much the rotation is reduced at each step 
	 * (degrees / step)
	 */
	public void setRotationFriction(double rotationFriction)
	{
		this.rotFriction = rotationFriction;
	}
	
	/**
	 *Changes how much the object rotates at each step. The rotation accelration 
	 *stacks with the previous rotation speed.
	 *
	 * @param raccelration How much faster will the object be rotated 
	 * (degrees / step)
	 */
	public void addRotation(double raccelration)
	{
		this.rotation += raccelration;
	}
	
	/**
	 * Adds the objects movement towards the given direction
	 *
	 * @param direction Direction towards which the force is applied (degrees)
	 * @param force The amount of force applied to the object (pxl / step)
	 */
	public void addMotion(double direction, double force)
	{
		this.movement = Movement.movementSum(getMovement(), 
				Movement.createMovement(direction, force));
	}
	
	/**
	 * Makes the object move towards given direction with given speed
	 *
	 * @param direction Towards which direction will the object move (degrees)
	 * @param speed How fast the object will be moving (pxl / step)
	 */
	public void setMotion(double direction, double speed)
	{	
		this.movement = Movement.createMovement(direction, speed);
	}
	
	/**
	 * Changes the object's maximum speed
	 *
	 * @param maxspeed The new maximum speed of the object (negative if you 
	 * don't want to limit the speed (default))
	 */
	public void setMaxSpeed(double maxspeed)
	{
		this.maxspeed = maxspeed;
	}
	
	/**
	 * @return The maximum speed of the object (negative if not limited)
	 */
	public double getMaxSpeed()
	{
		return this.maxspeed;
	}
	
	/**
	 * @return How fast the object can rotate at maximum (negative if not limited)
	 */
	public double getMaxRotation()
	{
		return this.maxrotation;
	}
	
	/**
	 * Changes how fast the object can rotate
	 *
	 * @param maxrotation How fast the object can rotate (negative if no limit)
	 */
	public void setMaxRotation(double maxrotation)
	{
		this.maxrotation = maxrotation;
	}
	
	
	// OTHER METHODS	----------------------------------------------------
	
	// Moves the object and handles the friction
	private void move(double steps)
	{
		addPosition(Movement.getMultipliedMovement(getMovement(), steps));
		
		// Checks the friction
		if (getFriction() != 0)
			implyFriction(steps);
		
		// Also checks the maximum speed and rotation
		checkMaxSpeed();
		checkMaxRotation();
	}
	
	/**
	 * This method rotates the object according to it's rotation. This method 
	 * is called automatically in the physicObject's act(double) method and 
	 * should really only concern subclasses that would override it.
	 * 
	 * @param steps How many steps the rotation takes place
	 */
	protected void rotate(double steps)
	{
		addAngle(getRotation() * steps);
	}
	
	// Slows the speed the amount of given friction
	private void implyFriction(double steps)
	{
		getMovement().diminishSpeed(getFriction() * steps);
	}
	
	// Slows the rotation speed the amount of given friction
	private void implyRotationFriction(double steps)
	{	
		// Only implies friction if there is any
		if (getRotationFriction() == 0)
			return;
		
		// Slows down the object's rotation
		if (Math.abs(getRotation()) <= getRotationFriction() * steps)
			this.rotation = 0;
		else if (getRotation() > 0)
			this.rotation -= getRotationFriction() * steps;
		else
			this.rotation += getRotationFriction() * steps;
	}
	
	private void checkMaxSpeed()
	{
		if (this.maxspeed >= 0 && getMovement().getSpeed() > this.maxspeed)
			getMovement().setSpeed(this.maxspeed);
	}
	
	private void checkMaxRotation()
	{
		// If maxrotation is negative, skips the whole thing
		if (this.maxrotation < 0)
			return;
		
		// Limits the rotation speed (if needed)
		if (Math.abs(getRotation()) > this.maxrotation)
		{
			if (getRotation() < 0)
				setRotation(-this.maxrotation);
			else
				setRotation(this.maxrotation);
		}
	}
}
