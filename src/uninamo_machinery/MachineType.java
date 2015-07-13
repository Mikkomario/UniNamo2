package uninamo_machinery;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;
import uninamo_components.ConnectorRelay;

/**
 * MachineTypes represent the different kinds of machines there are. Each 
 * type has a specific class (at least usually) and sprite
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public enum MachineType
{
	// TODO: As with componentType, connect the necessary info here
	
	/**
	 * Conveyor belts slide objects left or right
	 */
	CONVEYORBELT;
	
	
	// METHODS	---------------------------------------------------------
	
	
	/**
	 * Creates a new test machine of this type to the given position. 
	 * The test version doesn't function quite like the normal one
	 * @param position The position of the machine
	 * @param handlers The handlers that will handle the machine and the test component
	 * @return A test machine of this type
	 */
	public Machine getTestMachine(Vector3D position, HandlerRelay handlers)
	{	
		switch (this)
		{		
			case CONVEYORBELT: return new ConveyorBelt(position, handlers, handlers, null, 
					null, null, true);
		}
		
		System.err.println("Can't create a machine of type " + this + 
				", please update the method");
		return null;
	}
	
	/**
	 * Creates a new machine of this type
	 * @param position The machine's position
	 * @param machineHandlers The handlers that will handle the machine
	 * @param componentHandlers The handlers that will handle the components
	 * @param connectorRelay The connectorRelay that will handle the machine's 
	 * connectors
	 * @param machineCounter The machineCounter that will count the created 
	 * machine (optional)
	 * @param ID An unique ID the machine will have. Use null if you wan't it 
	 * generated automatically
	 * @return A new machine of this type
	 */
	public Machine getNewMachine(Vector3D position, HandlerRelay machineHandlers, 
			HandlerRelay componentHandlers, 
			ConnectorRelay connectorRelay, MachineCounter machineCounter, 
			String ID)
	{	
		switch (this)
		{
			case CONVEYORBELT: return new ConveyorBelt(position, 
					componentHandlers, machineHandlers, connectorRelay, 
					machineCounter, ID, false);
		}
		
		System.err.println("Couldn't create a machine of type " + this + 
				", please update the method");
		return null;
	}
	
	/**
	 * @return The name of the machine type
	 */
	public String getName()
	{
		switch (this)
		{
			case CONVEYORBELT: return "Converyor Belt of Power";
		}
		
		System.err.println("Can't get a name for machine type " + this + 
				", please update the method");
		return null;
	}
	
	/**
	 * @return How many euros producing a single machine of this type costs.
	 */
	public double getPrice()
	{
		switch (this)
		{
			case CONVEYORBELT: return 10;
		}
		
		System.err.println("Can't get a price for machine type " + this + 
				", please update the method");
		return 0;
	}
}
