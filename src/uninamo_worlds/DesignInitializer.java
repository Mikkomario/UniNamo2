package uninamo_worlds;

import uninamo_components.ConnectorRelay;
import uninamo_gameplaysupport.ObstacleCollector;
import uninamo_gameplaysupport.TestHandler;
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
	// ATTRIBUTES	-----------------------------------------------------
	
	private Area designArea, codingArea;
	private TestHandler testHandler;
	private MachineCounter machineCounter;
	private ConnectorRelay connectorRelay;
	private VictoryHandler victoryHandler;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new designInitializer that also creates the objects
	 * 
	 * @param areaChanger The areaChanger that handles the different areas
	 * @param testHandler The testHandler that will inform the created objects 
	 * about test events
	 * @param machineCounter The machineCounter that will count the created 
	 * machines
	 * @param connectorRelay The connectorRelay that will keep track of the 
	 * created connectors
	 * @param victoryHandler The victoryHandler that will keep track of the 
	 * created victory conditions (collectors)
	 */
	public DesignInitializer(AreaChanger areaChanger, TestHandler testHandler, 
			MachineCounter machineCounter, ConnectorRelay connectorRelay, 
			VictoryHandler victoryHandler)
	{
		// Initializes attributes
		this.designArea = areaChanger.getArea("design");
		this.codingArea = areaChanger.getArea("coding");
		this.testHandler = testHandler;
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
					
					type.getNewObstacle(x, y, this.designArea, 
							this.testHandler);
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
					int x = getArgumentAsInt(arguments[1]);
					int y = getArgumentAsInt(arguments[2]);
					
					type.getNewMachine(x, y, this.designArea, 
							this.codingArea, this.testHandler, 
							this.connectorRelay, this.machineCounter);
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
			
			new ObstacleCollector(x, y, this.designArea.getDrawer(), 
					this.designArea.getCollisionHandler(), this.testHandler, 
					this.victoryHandler, collectedType, amount, arguments[4], 
					arguments[5]);
		}
	}
}
