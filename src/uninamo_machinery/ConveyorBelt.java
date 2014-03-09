package uninamo_machinery;

import uninamo_components.ConnectorRelay;
import uninamo_worlds.Area;
import utopia_handlers.ActorHandler;
import utopia_handlers.CollidableHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.CollisionType;

/**
 * ConveyorBelt is a machine that pushes actors either left or right or 
 * doesn't push them at all, depending on the input.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class ConveyorBelt extends Machine
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private boolean running;
	private int speedSign;
	
	
	// CONSTRUCOR	-----------------------------------------------------
	
	/**
	 * Creates a new conveyorBelt to the given position
	 * 
	 * @param x The x-coordinate of the belt
	 * @param y The y-coordinate of the belt
	 * @param drawer The DrawableHandler that will draw the belt
	 * @param actorhandler The ActorHandler that will inform the belt about 
	 * step events
	 * @param collidablehandler CollidableHandler that will handle the belt's 
	 * collision checking
	 * @param codingArea The coding area of the game where the components are 
	 * created
	 * @param connectorRelay The connectorRelay that will handle the belts 
	 * connectors
	 */
	public ConveyorBelt(int x, int y, DrawableHandler drawer,
			ActorHandler actorhandler, CollidableHandler collidablehandler,
			Area codingArea, ConnectorRelay connectorRelay)
	{
		super(x, y, true, CollisionType.BOX, drawer, actorhandler,
				collidablehandler, codingArea, connectorRelay,
				"belt", "beltreal", "machinecomponent", null, 2, 0);
		
		// Initializes attributes
		this.running = false;
		this.speedSign = 1;
		
		updateAnimation();
	}
	
	// TODO: Add interaction with actors (by making the belt a collisionListener?)
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onSignalEvent(boolean signalType, int inputIndex)
	{
		// May start, stop or change direction
		if (inputIndex == 0)
			this.running = signalType;
		else if (signalType)
		{
			this.speedSign = -1;
		}
		else
			this.speedSign = 1;
		
		updateAnimation();
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	private void updateAnimation()
	{
		if (this.running)
			getSpriteDrawer().setImageSpeed(this.speedSign * 0.1);
		else
			getSpriteDrawer().setImageSpeed(0);
	}
}
