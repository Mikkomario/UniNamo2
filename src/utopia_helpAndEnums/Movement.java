package utopia_helpAndEnums;

/**
 * Movement Represents movement the object can have. Movement itself resembles a 
 * two dimensional vector.
 *
 * @author Mikko Hilpinen.
 *         Created 4.7.2013.
 */
public class Movement
{
	// ATTRIBUTES	------------------------------------------------------
	
	private double hspeed;
	private double vspeed;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new movement with the given proportions
	 *
	 * @param hspeed The horizontal speed in the movement (pixels / step)
	 * @param vspeed The vertical speed in the movement (pixels / step)
	 */
	public Movement(double hspeed, double vspeed)
	{
		// Initializes attributes
		this.hspeed = hspeed;
		this.vspeed = vspeed;
	}
	
	/**
	 * A copy constructor for the class movement
	 *
	 * @param other The movement which will be copied
	 */
	public Movement(Movement other)
	{
		this.hspeed = other.getHSpeed();
		this.vspeed = other.getVSpeed();
	}
	
	/**
	 * Alternate method for creating a movement. Uses direction and speed.
	 *
	 * @param direction The direction of the movement (degrees) [0, 360[
	 * @param speed The movement's speed (pixels / step)
	 * @return The movement with the given information
	 */
	public static Movement createMovement(double direction, double speed)
	{
		Movement newmovement = new Movement(0, 0);
		newmovement.setDirSpeed(direction, speed);
		return newmovement;
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return The horizontal component of the movement (pixels / step)
	 */
	public double getHSpeed()
	{
		return this.hspeed;
	}
	
	/**
	 * @return The vertical component of the movement (pixels / step)
	 */
	public double getVSpeed()
	{
		return this.vspeed;
	}
	
	/**
	 * Changes the horizontal speed of the movement
	 *
	 * @param hspeed The new horizontal speed of the movement (pixels / step)
	 */
	public void setHSpeed(double hspeed)
	{
		this.hspeed = hspeed;
	}
	
	/**
	 * Changes the vertical speed of the movement
	 *
	 * @param vspeed The new vertical speed of the movement (pixels / step)
	 */
	public void setVSpeed(double vspeed)
	{
		this.vspeed = vspeed;
	}
	
	/**
	 * @return The movement's direction (degrees) [0, 360[
	 */
	public double getDirection()
	{
		return HelpMath.getVectorDirection(getHSpeed(), getVSpeed());
	}
	
	/**
	 * @return The movement's total speed (pixels / step)
	 */
	public double getSpeed()
	{
		return HelpMath.pointDistance(0, 0, getHSpeed(), getVSpeed());
	}
	
	/**
	 * @return The opposing movement to the calling movement. The sum of these 
	 * two movements is always 0.
	 */
	public Movement getOpposingMovement()
	{
		return new Movement(-getHSpeed(), -getVSpeed());
	}
	
	/**
	 * Changes the movement's direction
	 *
	 * @param direction The new direction of the movement (degrees) [0, 360[
	 */
	public void setDirection(double direction)
	{
		setDirSpeed(direction, getSpeed());
	}
	
	/**
	 * Changes the speed of the movement
	 *
	 * @param speed The movement's new speed (pixels / step)
	 */
	public void setSpeed(double speed)
	{
		// Note: Doesn't work so well when the speed's sign changes
		setDirSpeed(getDirection(), speed);
	}
	
	/**
	 * Increases the movement's speed by the given amount
	 *
	 * @param accelration How much the speed increases (pixels / step) (0+)
	 */
	public void addSpeed(double accelration)
	{
		if (accelration > 0)
			setSpeed(getSpeed() + accelration);
	}
	
	/**
	 * Lessens the movement's speed by the given amount
	 * 
	 * @param speedloss How much speed is lost (pixels / step) (0+)
	 */
	public void diminishSpeed(double speedloss)
	{
		// Doesn't work with negative values
		if (speedloss <= 0)
			return;
		
		// If speed was already low, sets it to 0
		if (getSpeed() <= speedloss)
			setSpeed(0);
		else if (getSpeed() > 0)
			setSpeed(getSpeed() - speedloss);
	}
	
	/**
	 * Changes the movement's direction
	 * 
	 * @param rotation How much the direction turns (degrees)
	 */
	public void addDirection(double rotation)
	{
		setDirection(getDirection() + rotation);
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Returns a movement projected to the given direction
	 *
	 * @param direction The direction to which the movement is projected 
	 * (degrees) [0, 360[
	 * @return The projected movement
	 */
	public Movement getDirectionalMovement(double direction)
	{
		double newspeed = HelpMath.getDirectionalForce(getDirection(), 
				getSpeed(), direction);
		return createMovement(direction, newspeed);
	}
	
	/**
	 * returns a movement created by multiplying the current movement with a 
	 * certain multiplier
	 *
	 * @param multiplier The multiplier used in creating the new movement
	 * @return The movement multiplied using the multiplier
	 */
	public Movement getMultiplication(double multiplier)
	{
		return new Movement(getHSpeed() * multiplier, getVSpeed() * multiplier);
	}
	
	/**
	 * Multiplies the movement's speed with the given multiplier
	 *
	 * @param multiplier What will the movement's speed be multiplied with (
	 * for example, 2 means double the former speed)
	 */
	public void multiplySPeed(double multiplier)
	{
		this.hspeed *= multiplier;
		this.vspeed *= multiplier;
	}
	
	/**
	 * Diminishes the the movement's one component while keeping other the same
	 *
	 * @param direction The direction to which the speedloss affects 
	 * (direction) [0, 360[
	 * @param speedloss How much (directional) speed is lost (pixels / step)
	 * @return The movement where the speedloss has been added
	 */
	public Movement getDirectionalllyDiminishedMovement(double direction, 
			double speedloss)
	{
		// Divides the movement into two ("x" and "y") movements
		Movement dirmovement = getDirectionalMovement(direction);
		Movement othermovement = getDirectionalMovement(direction + 90);
		
		// Diminishes the directional speed while keepin the other speed the same
		dirmovement.diminishSpeed(speedloss);
		
		// Adds the movements back together
		return Movement.movementSum(dirmovement, othermovement);
	}
	
	/**
	 * Puts the two movements together and returns their sum
	 * 
	 * @param movement1 The first movement
	 * @param movement2 The second movement
	 * @return The sum of the two movements
	 */
	public static Movement movementSum(Movement movement1, Movement movement2)
	{
		// Adds the movements together
		double hspeed = movement1.getHSpeed() + movement2.getHSpeed();
		double vspeed = movement1.getVSpeed() + movement2.getVSpeed();
		// Returns the sum of the movements
		return new Movement(hspeed, vspeed);
	}
	
	/**
	 * Multiplies the two movements and returns their multiplication
	 *
	 * @param movement1 The first movement
	 * @param movement2 The second movement
	 * @return The multiplication of the two movements
	 */
	public static Movement movementMultiplication(Movement movement1, 
			Movement movement2)
	{
		// Multiplies the movements
		double hspeed = movement1.getHSpeed() * movement2.getHSpeed();
		double vspeed = movement1.getVSpeed() * movement2.getVSpeed();
		// Returns the multiplication
		return new Movement(hspeed, vspeed);
	}
	
	/**
	 * Returns a multiplication of the movement as a new movement
	 *
	 * @param movement The movement which is used in the multiplication, the 
	 * movement isn't affected by this method
	 * @param multiplier The multiplier which is used in the multiplication
	 * @return A new movement which is created by multiplying the given movement
	 */
	public static Movement getMultipliedMovement(Movement movement, 
			double multiplier)
	{
		Movement returnvalue = new Movement(movement);
		returnvalue.multiplySPeed(multiplier);
		return returnvalue;
	}
	
	private void setDirSpeed(double direction, double speed)
	{
		// If speed is 0, simply creates a new movement
		if (speed == 0)
		{
			this.hspeed = 0;
			this.vspeed = 0;
			return;
		}
		
		this.hspeed = HelpMath.lendirX(speed, direction);
		this.vspeed = HelpMath.lendirY(speed, direction);
	}
}
