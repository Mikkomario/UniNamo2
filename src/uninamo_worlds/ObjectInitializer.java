package uninamo_worlds;

import java.io.FileNotFoundException;

import flow_io.AbstractFileReader;
import genesis_event.HandlerRelay;
import uninamo_components.Cable;
import uninamo_components.NormalComponent;
import uninamo_gameplaysupport.ObstacleCollector;
import uninamo_machinery.Machine;
import uninamo_obstacles.Obstacle;
import uninamo_userinterface.Note;

/**
 * ObjectInitializers create different objects when created by reading their 
 * data from a file.
 * 
 * @author Mikko Hilpinen
 * @since 17.3.2014
 */
public abstract class ObjectInitializer extends AbstractFileReader
{
	// TODO: This will be replaced with object constructor from flow
	
	// ATTRIBUTES	-----------------------------------------------------
	
	private CreationMode currentMode;
	private HandlerRelay handlers;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new objectInitializer and creates the objects
	 * @param handlers The handlers that will handle the created objects
	 */
	public ObjectInitializer(HandlerRelay handlers)
	{
		// TODO Add stage handling
		this.handlers = handlers;
		this.currentMode = null;
	}
	
	
	// ABSTRACT METHODS	------------------------------------------------
	
	/**
	 * Returns if the initializer creates anything during the given mode
	 * 
	 * @param mode The mode that would start
	 * @return Does the initializer react during this mode
	 */
	protected abstract boolean supportsCreationMode(CreationMode mode);
	
	/**
	 * In this method the creator should use the arguments extracted from 
	 * a command line to create an object.
	 * 
	 * @param arguments The arguments that are needed for creating the object
	 * @param currentMode The current creationMode (the arguments have a 
	 * different meaning during different modes)
	 */
	protected abstract void createObjectFromLine(String[] arguments, CreationMode currentMode);
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	protected void onLine(String line)
	{
		// Checks if a mode should be changed
		if (line.startsWith("&"))
		{
			String modename = line.substring(1).toLowerCase();
			
			for (CreationMode mode : CreationMode.values())
			{
				if (mode.toString().toLowerCase().equals(modename))
				{
					this.currentMode = mode;
					break;
				}
			}
		}
		// Otherwise creates new objects according to the mode
		else if (supportsCreationMode(this.currentMode))
			createObjectFromLine(line.split("#"), this.currentMode);
	}
	
	
	// ACCESSORS	-------------------
	
	/**
	 * @return The handlers used for the created objects
	 */
	protected HandlerRelay getHandlers()
	{
		return this.handlers;
	}
	
	
	// OTHER METHODS	------------------------------------------------
	
	/**
	 * Simply casts a string argument into an integer
	 * 
	 * @param argument The argument that will be cast
	 * @return The argument as an integer
	 */
	protected static int getArgumentAsInt(String argument)
	{
		try
		{
			return Integer.parseInt(argument);
		}
		catch (NumberFormatException nfe)
		{
			System.err.println(
					"DesignInitializer failed to convert a string into an integer!");
			nfe.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Initiates the object creation. The subclass should call this after it has 
	 * bee set up
	 */
	protected void createObjects()
	{
		try
		{
			readFile("missions/teststage.txt", "*");
		}
		catch (FileNotFoundException e)
		{
			System.err.println("Can't find the test stage file");
			e.printStackTrace();
		}
	}
	
	
	// ENUMERATIONS

	// TODO: Make this a separate enum
	
	/**
	 * CreationModes represent different object categories that can be read 
	 * from a file
	 * 
	 * @author Mikko Hilpinen
	 * @since 17.3.2014
	 */
	protected static enum CreationMode
	{
		/**
		 * Note1 is the shorter note shown at the beginning of a stage
		 * @see Note
		 */
		NOTE1,
		/**
		 * Note2 is the longer note shown at the beginning of a stage
		 * @see Note
		 */
		NOTE2,
		/**
		 * Obstacles are instances of class Obstacle
		 * @see Obstacle
		 */
		OBSTACLES,
		/**
		 * Machines are instances of class Machine
		 * @see Machine
		 */
		MACHINES,
		/**
		 * Collectors are different obstacleCollectors
		 * @see ObstacleCollector
		 */
		COLLECTORS,
		/**
		 * Readable components are the components used for demoing a stage
		 * @see NormalComponent
		 */
		COMPONENTS,
		/**
		 * Cables are used to connect the components
		 * @see Cable
		 */
		CABLES;
	}
}
