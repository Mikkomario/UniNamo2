package uninamo_missionHandling;

import uninamo_components.Cable;
import uninamo_components.ComponentType;
import uninamo_components.ConnectorRelay;
import uninamo_components.InputCableConnector;
import uninamo_components.NormalComponent;
import uninamo_components.NormalComponentRelay;
import uninamo_components.OutputCableConnector;
import uninamo_gameplaysupport.ObstacleCollector;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TurnHandler;
import uninamo_gameplaysupport.VictoryHandler;
import uninamo_machinery.MachineCounter;
import uninamo_machinery.MachineType;
import uninamo_main.GameSettings;
import uninamo_obstacles.ObstacleType;
import uninamo_userinterface.Note;
import uninamo_worlds.Area;
import uninamo_worlds.AreaChanger;
import utopia_fileio.FileReader;

/**
 * MissionInitializer creates the necessary stuff to start a mission
 * 
 * @author Mikko Hilpinen
 * @since 13.3.2014
 */
public class MissionInitializer extends FileReader
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Mode mode;
	//private String filename;
	private AreaChanger areaChanger;
	private TestHandler testHandler;
	private ConnectorRelay connectorRelay;
	private VictoryHandler victoryHandler;
	private MachineCounter machineCounter;
	private TurnHandler turnHandler;
	private NormalComponentRelay componentRelay;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new missionInitializer that also initializes the necessary 
	 * objects
	 * 
	 * @param instructionFileName The file from which the necessary data is 
	 * read (data/ automatically included)
	 * @param areaChanger The areaChanger that handles different areas
	 * @param testHandler The testHandler that will inform the created objects 
	 * about test events (if applicable)
	 * @param connectorRelay The connectorHandler that will keep track of the 
	 * created connectors (if any)
	 * @param componentRelay The ComponenRelay that will keep track of the 
	 * created components (if any)
	 * @param turnHandler The turnHandler that will inform the objects about turn 
	 * events (if applicable)
	 */
	public MissionInitializer(String instructionFileName, AreaChanger areaChanger, 
			TestHandler testHandler, ConnectorRelay connectorRelay, 
			NormalComponentRelay componentRelay, TurnHandler turnHandler)
	{
		// Initializes atributes
		this.mode = null;
		this.areaChanger = areaChanger;
		this.testHandler = testHandler;
		this.connectorRelay = connectorRelay;
		this.victoryHandler = new VictoryHandler(null);
		this.machineCounter = new MachineCounter();
		this.turnHandler = turnHandler;
		this.componentRelay = componentRelay;
		
		// Reads the data / initializes the mission
		readFile(instructionFileName, "*");
	}

	@Override
	protected void onLine(String line)
	{
		// Checks if a mode should be changed
		if (line.startsWith("&"))
		{
			String modename = line.substring(1).toLowerCase();
			
			for (Mode mode : Mode.values())
			{
				if (mode.toString().toLowerCase().equals(modename))
				{
					this.mode = mode;
					break;
				}
			}
		}
		// Otherwise creates new objects according to the mode
		else
			this.mode.createNewInstance(line, this.areaChanger, this.testHandler, 
					this.connectorRelay, this.componentRelay, 
					this.victoryHandler, this.machineCounter, this.turnHandler);
	}

	
	// ENUMERATIONS	------------------------------------------------------
	
	private enum Mode
	{
		NOTE1, NOTE2, OBSTACLES, MACHINES, COLLECTORS, COMPONENTS, CABLES;
		
		
		// METHODS	-----------------------------------------------------
		
		private void createNewInstance(String commandLine, AreaChanger areaChanger, 
				TestHandler testHandler, ConnectorRelay connectorRelay, 
				NormalComponentRelay componentRelay, 
				VictoryHandler victoryHandler, MachineCounter machineCounter, 
				TurnHandler turnHandler)
		{		
			// TODO: Refactor this awful method
			
			// Creates a new instance of this mode's object type
			switch (this)
			{
				case NOTE1: 
					new Note(GameSettings.screenWidth / 2 + 64, GameSettings.screenHeight - 200, 
							75, 32, "docket", commandLine, areaChanger.getArea("mission"));
					break;
				case NOTE2:
					new Note(2 * GameSettings.screenWidth / 3, GameSettings.screenHeight / 2, 
							75, 96, "description", commandLine, areaChanger.getArea("mission"));
					break;
				case OBSTACLES:
				{
					String[] commands = commandLine.split("#");
					
					for (ObstacleType type : ObstacleType.values())
					{
						if (type.toString().equalsIgnoreCase(commands[0]))
						{
							int x = 0;
							int y = 0;
							
							try
							{
							x = Integer.parseInt(commands[1]);
							y = Integer.parseInt(commands[2]);
							}
							catch (NumberFormatException nfe)
							{
								System.err.println("Cannot create an obstacle from line " + commandLine);
								nfe.printStackTrace();
							}
							
							type.getNewObstacle(x, y, 
									areaChanger.getArea("design"), testHandler);
							break;
						}
					}
					break;
				}
				case MACHINES:
				{
					String[] commands = commandLine.split("#");
					
					for (MachineType type : MachineType.values())
					{
						if (type.toString().equalsIgnoreCase(commands[0]))
						{
							int x = 0;
							int y = 0;
							
							try
							{
							x = Integer.parseInt(commands[1]);
							y = Integer.parseInt(commands[2]);
							}
							catch (NumberFormatException nfe)
							{
								System.err.println("Cannot create a machine from line " + commandLine);
								nfe.printStackTrace();
							}
							
							type.getNewMachine(x, y, 
									areaChanger.getArea("design"), 
									areaChanger.getArea("coding"), testHandler, 
									connectorRelay, machineCounter);
							break;
						}
					}
					break;
				}
				case COLLECTORS:
				{
					String[] commands = commandLine.split("#");
					ObstacleType collectedType = null;
					
					// Reads the collected obstacle type from the first argument
					for (ObstacleType type : ObstacleType.values())
					{
						if (type.toString().equalsIgnoreCase(commands[0]))
						{
							collectedType = type;
							break;
						}
					}
					
					// Reads the collected amount from the second argument, 
					// as well as the position from third and fourth
					int amount = 0;
					int x = 0;
					int y = 0;
					
					try
					{
						amount = Integer.parseInt(commands[1]);
						x = Integer.parseInt(commands[2]);
						y = Integer.parseInt(commands[3]);
					}
					catch (NumberFormatException nfe)
					{
						System.err.println("Couldn't create an obstacle collector from line " + commandLine);
						nfe.printStackTrace();
					}
					
					Area area = areaChanger.getArea("design");
					new ObstacleCollector(x, y, area.getDrawer(), 
							area.getCollisionHandler(), testHandler, 
							victoryHandler, collectedType, amount, commands[4], 
							commands[5]);
					break;
				}
				case COMPONENTS:
				{
					String[] commands = commandLine.split("#");
					ComponentType componentType = null;
					
					// Finds the componentType given in the second argument
					for (ComponentType type : ComponentType.values())
					{
						if (type.toString().equalsIgnoreCase(commands[1]))
						{
							componentType = type;
							break;
						}
					}
					
					// Changes the x and y from arguments 3 and 4 to integers
					int x = 0;
					int y = 0;
					
					try
					{
						x = Integer.parseInt(commands[2]);
						y = Integer.parseInt(commands[3]);
					}
					catch (NumberFormatException nfe)
					{
						System.err.println("Component's given location on line " 
								+ commandLine + " is not made of integers");
						nfe.printStackTrace();
					}
					
					// Creates the component and gives it a new ID
					Area codingArea = areaChanger.getArea("coding");
					NormalComponent component = componentType.getNewComponent(x, y, 
							codingArea.getDrawer(), 
							codingArea.getActorHandler(), 
							codingArea.getMouseHandler(), codingArea, 
							testHandler, connectorRelay, componentRelay, 
							turnHandler, false);
					component.stopDrag();
					component.setID(commands[0]);
					
					break;
				}
				case CABLES:
				{
					String[] commands = commandLine.split("#");
					
					OutputCableConnector start = null;
					InputCableConnector end = null;
					
					// Finds the start and end connectors based on the IDs
					try
					{
						start = (OutputCableConnector) connectorRelay.getConnectorWithID(commands[0]);
						end = (InputCableConnector) connectorRelay.getConnectorWithID(commands[1]);
						
						if (start == null)
							System.err.println("Couldn't find a connector with ID: " + commands[0]);
						if (end == null)
							System.err.println("Couldn't find a connector with ID: " + commands[1]);
					}
					catch (ClassCastException cce)
					{
						System.err.println("Failed to read connectors in cable "
								+ "creation from line " + commandLine);
						break;
					}
					
					// Creates a new cable between the connectors
					Area codingArea = areaChanger.getArea("coding");
					new Cable(codingArea.getDrawer(), 
							codingArea.getMouseHandler(), codingArea, 
							testHandler, connectorRelay, start, end, false);
					break;
				}
			}
		}
	}
}
