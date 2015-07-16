package uninamo_previous;

import uninamo_components.ComponentType;

/**
 * ComponentInfoHolder reads component information from a file and offers that 
 * data for other objects
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 * @deprecated Just use a single description holder class
 */
public class ComponentInfoHolder extends DescriptionHolder
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private final static String infoFileName = "configure/manualcontent.txt";
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new ComponentInfoHolder that automatically reads the necessary 
	 * information.
	 */
	public ComponentInfoHolder()
	{
		super(infoFileName);
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
		return getData(type.toString().toLowerCase());
	}
}
