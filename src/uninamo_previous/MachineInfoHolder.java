package uninamo_previous;

import uninamo_machinery.MachineType;

/**
 * MachineInfoHolder holds information about different machine types and 
 * offers that information for other objects
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 * @deprecated page does this already
 */
public class MachineInfoHolder extends DescriptionHolder
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new MachineInfoHolder that reads the data automatically
	 */
	public MachineInfoHolder()
	{
		super("configure/machineinstructions.txt");
	}

	
	// OTHER METHODS	--------------------------------------------------

	/**
	 * Fetches information about a certain machine type
	 * 
	 * @param type The machine type searched for
	 * @return The description of the given type
	 */
	public String getMachineDescription(MachineType type)
	{
		return getData(type.toString().toLowerCase());
	}
}
