package uninamo_machinery;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;

import uninamo_components.ConnectorRelay;
import uninamo_gameplaysupport.Wall;

/**
 * ConveyorBelt is a machine that pushes actors either left or right or 
 * doesn't push them at all, depending on the input.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class ConveyorBelt extends Machine implements Wall
{
	// TODO: Any collision checking was removed due to an update.
	// This class is not complete
	
	// ATTRIBUTES	-----------------------------------------------------
	
	private boolean running;
	private int speedSign;
	
	
	// CONSTRUCOR	-----------------------------------------------------
	
	/**
	 * Creates a new conveyorBelt to the given position
	 * @param position The belt's position
	 * @param componentHandlers The component handlers
	 * @param machineHandlers The machine handlers
	 * @param connectorRelay The connectorRelay that will handle the belts 
	 * connectors
	 * @param machineCounter The machineCounter that will count the created 
	 * machine(s) (optional)
	 * @param ID The unique ID of the machine. Use null if you wan't it generated automatically
	 * @param isForTesting Is the machine created for simple demonstration purposes
	 */
	public ConveyorBelt(Vector3D position, HandlerRelay componentHandlers, HandlerRelay 
			machineHandlers, ConnectorRelay connectorRelay, 
			MachineCounter machineCounter, String ID, boolean isForTesting)
	{
		super(position, componentHandlers, machineHandlers, connectorRelay, machineCounter, 
				"belt", "beltreal", "machinecomponent", null, 2, 0, ID, isForTesting);
		
		// Initializes attributes
		this.running = false;
		this.speedSign = 1;
		
		updateAnimation();
	}
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onSignalEvent(boolean signalType, int inputIndex)
	{
		//this.colhandler.printHandledNumber();
		
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
	
	@Override
	public MachineType getMachineType()
	{
		return MachineType.CONVEYORBELT;
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
