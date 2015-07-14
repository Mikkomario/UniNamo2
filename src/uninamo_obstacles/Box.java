package uninamo_obstacles;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;

/**
 * Box is a simple obstacle that does pretty much nothing but collides with walls
 * 
 * @author Mikko Hilpinen
 */
public class Box extends Obstacle
{
	// TODO: All collision systems are removed at the time
	
	/**
	 * Creates a new box to the given position
	 * @param handlers The handlers that will handle the box
	 * @param position The box's position
	 */
	public Box(HandlerRelay handlers, Vector3D position)
	{
		super(handlers, position, "boxdesign", "boxreal");
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	protected void resetStatus()
	{
		// Does nothing
		//getMovement().setHSpeed(-5);
	}
}
