package uninamo_gameplaysupport;

import java.util.HashMap;

import uninamo_components.ComponentType;
import uninamo_machinery.MachineType;

/**
 * TotalCostAnalyzer can calculate the final costs of the creation, analyze 
 * their sources and form a grade.
 * 
 * @author Mikko Hilpinen
 * @since 17.3.2014
 */
public class TotalCostAnalyzer
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private double demoComponentCost;
	private HashMap<ComponentType, Integer> componentAmounts;
	private HashMap<MachineType, Integer> machineAmounts;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new totalCostAnalyzer. The data must be added separately.
	 */
	public TotalCostAnalyzer()
	{
		// Initializes attributes
		this.demoComponentCost = 0;
		this.componentAmounts = new HashMap<ComponentType, Integer>();
		this.machineAmounts = new HashMap<MachineType, Integer>();
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Records the demo component costs to the given value
	 * 
	 * @param demoCosts How much did the demo components cost
	 */
	public void setDemoComponentCosts(double demoCosts)
	{
		this.demoComponentCost = demoCosts;
	}
	
	/**
	 * Adds a new component to the cost calculations
	 * 
	 * @param type The type of the new component to be analyzed
	 */
	public void addComponentCost(ComponentType type)
	{
		int lastAmount = 0;
		
		if (this.componentAmounts.containsKey(type))
			lastAmount = this.componentAmounts.get(type);
		
		this.componentAmounts.put(type, lastAmount + 1);
	}
	
	/**
	 * Adds a new machine to the cost calculations
	 * 
	 * @param type The type of the new machine to be analyzed
	 */
	public void addComponentCost(MachineType type)
	{
		int lastAmount = 0;
		
		if (this.machineAmounts.containsKey(type))
			lastAmount = this.machineAmounts.get(type);
		
		this.machineAmounts.put(type, lastAmount + 1);
	}
	
	// TODO: Add data visualization
}
