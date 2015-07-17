package uninamo_worlds;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;
import uninamo_components.ConnectorRelay;
import uninamo_gameplaysupport.ObstacleCollector;
import uninamo_gameplaysupport.VictoryHandler;
import uninamo_machinery.MachineCounter;
import uninamo_machinery.MachineType;
import uninamo_obstacles.ObstacleType;

/**
 * DesignInitializer creates the machines, obstacles and obstacleCollectors 
 * by reading their data from a file
 * 
 * @author Mikko Hilpinen
 * @since 17.3.2014
 */
public class DesignInitializer extends ObjectInitializer
{
	// TODO: Replace with object construction system
	
	// ATTRIBUTES	-----------------------------------------------------
	
	private HandlerRelay codingHandlers;
	private MachineCounter machineCounter;
	private ConnectorRelay connectorRelay;
	private VictoryHandler victoryHandler;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new designInitializer that also creates the objects
	 * @param designHandlers The handlers that will handle the objects
	 * @param codingHandlers The handlers that handle the coding area
	 * @param machineCounter The machineCounter that will count the created 
	 * machines
	 * @param connectorRelay The connectorRelay that will keep track of the 
	 * created connectors
	 * @param victoryHandler The victory handler
	 */
	public DesignInitializer(HandlerRelay designHandlers, HandlerRelay codingHandlers, 
			MachineCounter machineCounter, ConnectorRelay connectorRelay, 
			VictoryHandler victoryHandler)
	{
		super(designHandlers);
		
		// Initializes attributes
		this.codingHandlers = codingHandlers;
		this.machineCounter = machineCounter;
		this.connectorRelay = connectorRelay;
		this.victoryHandler = victoryHandler;
		
		createObjects();
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected boolean supportsCreationMode(CreationMode mode)
	{
		// Supports obstacle, machine and collector modes
		return (mode == CreationMode.MACHINES || mode == CreationMode.OBSTACLES || 
				mode == CreationMode.COLLECTORS);
	}

	@Override
	protected void createObjectFromLine(String[] arguments,
			CreationMode currentMode)
	{
		if (currentMode == CreationMode.OBSTACLES)
		{
			// Finds the obstacle type described in the first argument
			for (ObstacleType type : ObstacleType.values())
			{
				if (type.toString().equalsIgnoreCase(arguments[0]))
				{
					int x = getArgumentAsInt(arguments[1]);
					int y = getArgumentAsInt(arguments[2]);
					
					type.getNewObstacle(new Vector3D(x, y), getHandlers());
					break;
				}
			}
		}
		else if (currentMode == CreationMode.MACHINES)
		{
			// Finds the machineType maching the argument 0
			for (MachineType type : MachineType.values())
			{
				if (type.toString().equalsIgnoreCase(arguments[0]))
				{
					// Reads the position from arguments 1 and 2
					int x = getArgumentAsInt(arguments[1]);
					int y = getArgumentAsInt(arguments[2]);
					
					// Argument 3 is the ID of the machine
					type.getNewMachine(new Vector3D(x, y), getHandlers(), 
							this.codingHandlers, this.connectorRelay, this.machineCounter, 
							arguments[3]);
					break;
				}
			}
		}
		else
		{
			ObstacleType collectedType = null;
			
			// Reads the collected obstacle type from the first argument
			for (ObstacleType type : ObstacleType.values())
			{
				if (type.toString().equalsIgnoreCase(arguments[0]))
				{
					collectedType = type;
					break;
				}
			}
			
			// Reads the collected amount from the second argument, 
			// as well as the position from third and fourth
			int amount = getArgumentAsInt(arguments[1]);
			int x = getArgumentAsInt(arguments[2]);
			int y = getArgumentAsInt(arguments[3]);
			
			new ObstacleCollector(getHandlers(), new Vector3D(x, y), this.victoryHandler, 
					collectedType, amount, arguments[4], arguments[5]);
		}
	}
}
