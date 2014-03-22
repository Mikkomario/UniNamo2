package uninamo_machinery;

import uninamo_components.ConnectorRelay;
import uninamo_gameplaysupport.TestHandler;
import uninamo_worlds.Area;
import utopia_handlers.ActorHandler;
import utopia_handlers.CollidableHandler;
import utopia_handlers.CollisionHandler;
import utopia_handlers.DrawableHandler;

/**
 * MachineTypes represent the different kinds of machines there are. Each 
 * type has a specific class (at least usually) and sprite
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public enum MachineType
{
	/**
	 * Conveyor belts slide objects left or right
	 */
	CONVEYORBELT;
	
	
	// METHODS	---------------------------------------------------------
	
	
	/**
	 * Creates a new test machine of this type to the given position. 
	 * The test version doesn't function quite like the normal one
	 * 
	 * @param x The x-coordinate of the machine
	 * @param y The y-coordinate of the machine
	 * @param drawer The DrawableHandler that will draw the machine
	 * @param actorhandler The ActorHandler that will inform the machine about 
	 * step events
	 * @param area The area where the machine and its components are located at
	 * @return A test machine of this type
	 */
	public Machine getTestMachine(int x, int y, DrawableHandler drawer,
			ActorHandler actorhandler, Area area)
	{
		//System.out.println(area);
		
		switch (this)
		{		
			case CONVEYORBELT: return new ConveyorBelt(x, y, drawer, 
					actorhandler, null, null, area, area, null, null, null, 
					null, true);
		}
		
		System.err.println("Can't create a machine of type " + this + 
				", please update the method");
		return null;
	}
	
	/**
	 * Creates a new machine of this type
	 * 
	 * @param x The x-coordinate of the created machine
	 * @param y The y-coordinate of the created machine
	 * @param machineArea The Area where the machine will be
	 * @param componentArea The area where the machine's component(s) will be
	 * @param testHandler The testHandler that informs the machine about test events
	 * @param connectorRelay The connectorRelay that will handle the machine's 
	 * connectors
	 * @param machineCounter The machineCounter that will count the created 
	 * machine (optional)
	 * @param ID An unique ID the machine will have. Use null if you wan't it 
	 * generated automatically
	 * @return A new machine of this type
	 */
	public Machine getNewMachine(int x, int y, Area machineArea, 
			Area componentArea, TestHandler testHandler, 
			ConnectorRelay connectorRelay, MachineCounter machineCounter, 
			String ID)
	{
		DrawableHandler drawer = machineArea.getDrawer();
		ActorHandler actorHandler = machineArea.getActorHandler();
		CollisionHandler collisionHandler = machineArea.getCollisionHandler();
		CollidableHandler collidableHandler = collisionHandler.getCollidableHandler();
		
		switch (this)
		{
			case CONVEYORBELT: return new ConveyorBelt(x, y, drawer, 
					actorHandler, collidableHandler, collisionHandler, 
					componentArea, machineArea, testHandler, connectorRelay, 
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
}
