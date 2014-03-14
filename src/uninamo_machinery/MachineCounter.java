package uninamo_machinery;

import java.util.HashMap;

/**
 * MachineCounter keeps track of how many machines of each type have been 
 * created
 * 
 * @author Mikko Hilpinen
 * @since 14.3.2014
 */
public class MachineCounter
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private HashMap<MachineType, Integer> machineCounts;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new machineCounter
	 */
	public MachineCounter()
	{
		this.machineCounts = new HashMap<MachineType, Integer>();
	}
	
	
	// OTHER METHODS	------------------------------------------------
	
	/**
	 * Adds a new machine to the machine count
	 * 
	 * @param type What kind of machine was created
	 */
	public void countNewMachine(MachineType type)
	{
		int previousAmount = 0;
		
		if (this.machineCounts.containsKey(type))
			previousAmount = this.machineCounts.get(type);
		
		this.machineCounts.put(type, previousAmount + 1);
	}
	
	/**
	 * Tells how many instances of a certain type of machine have been 
	 * counted
	 * 
	 * @param type The type of the machine that is counted
	 * @return How many machines there are of the given type
	 */
	public int getMachineTypeAmount(MachineType type)
	{
		if (this.machineCounts.containsKey(type))
			return this.machineCounts.get(type);
		else
			return 0;
	}
}
