package uninamo_manual;

import java.util.HashMap;

import uninamo_components.ComponentType;
import utopia_fileio.FileReader;

/**
 * ComponentInfoHolder reads component information from a file and offers that 
 * data for other objects
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public class ComponentInfoHolder extends FileReader
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private final static String infoFileName = "configure/manualcontent.txt";
	
	private HashMap<ComponentType, String> data;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new ComponentInfoHolder that automatically reads the necessary 
	 * information.
	 */
	public ComponentInfoHolder()
	{
		// Initializes attributes
		this.data = new HashMap<ComponentType, String>();
		
		// Reads the data
		readFile(infoFileName);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected void onLine(String line)
	{
		// Ignores lines that start with "*"
		if (line.startsWith("*"))
			return;
		
		// The format of a line should be ComponentType#Description
		String[] arguments = line.split("#");
		
		if (arguments.length != 2)
		{
			System.err.println("The line: " + line + " doesn't hold a correct "
					+ "amount of arguments. ComponentInfoHolder requires 2 "
					+ "arguments per line to work");
			return;
		}
		
		// Finds the componentType the first argument represents
		for (ComponentType type : ComponentType.values())
		{
			if (type.toString().equalsIgnoreCase(arguments[0]))
			{
				this.data.put(type, arguments[1]);
				return;
			}
		}
		
		System.err.println(arguments[0] + 
				" doesn't resemble any component type");
	}

	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * Finds and returns a description for the given component type from the 
	 * collected data.
	 * 
	 * @param type The componentType who's data is searched for
	 * @return The data describing the given componentType
	 */
	public String getComponentData(ComponentType type)
	{
		String results = this.data.get(type);
		
		if (type == null)
			return "No data available";
		else
			return results;
	}
}
