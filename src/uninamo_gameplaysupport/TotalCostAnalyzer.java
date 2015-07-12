package uninamo_gameplaysupport;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import exodus_world.Area;
import exodus_world.AreaListener;
import gateway_ui.TextDrawer;
import genesis_util.DepthConstants;
import genesis_util.StateOperator;
import genesis_util.Vector3D;
import uninamo_components.ComponentType;
import uninamo_machinery.MachineType;
import uninamo_main.GameSettings;
import vision_drawing.SimpleSingleSpriteDrawerObject;
import vision_sprite.SpriteBank;

/**
 * TotalCostAnalyzer can calculate the final costs of the creation, analyze 
 * their sources and form a grade.
 * 
 * @author Mikko Hilpinen
 * @since 17.3.2014
 */
// TODO: Make this a gameObject?
public class TotalCostAnalyzer implements AreaListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private double demoComponentCost;
	private Map<ComponentType, Integer> componentAmounts;
	private Map<MachineType, Integer> machineAmounts;
	private Area area;
	private StateOperator isDeadOperator;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new totalCostAnalyzer. The data must be added separately.
	 * @param area The area where the visualization will be drawn
	 */
	public TotalCostAnalyzer(Area area)
	{
		// Initializes attributes
		this.isDeadOperator = new StateOperator(false, false);
		this.demoComponentCost = 0;
		this.componentAmounts = new HashMap<>();
		this.machineAmounts = new HashMap<>();
		this.area = area;
		//this.textDrawer = new DrawableHandler(false, false, 
		//		DepthConstants.NORMAL, 0, area.getDrawer());
		
		// Adds the object to the handler(s)
		if (area != null)
			area.getListenerHandler().add(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public StateOperator getIsDeadStateOperator()
	{
		return this.isDeadOperator;
	}

	@Override
	public void onAreaStateChange(Area area)
	{
		// Visualizes the data
		if (area.getIsActiveStateOperator().getState())
			visualizeCosts();
		
		// Ends the visualization
		// TODO: End visualization
		// TODO: Find a way to kill all the objects. make them roomListeners or 
		// use a separate handler?
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
	public void addMachineCost(MachineType type)
	{
		int lastAmount = 0;
		
		if (this.machineAmounts.containsKey(type))
			lastAmount = this.machineAmounts.get(type);
		
		this.machineAmounts.put(type, lastAmount + 1);
	}
	
	/**
	 * Creates an visualization of the costs.
	 */
	public void visualizeCosts()
	{
		double totalMachineCosts = 0;
		double totalComponentCosts = 0;
		int lineHeight = 32;
		int y = 64;
		// Creates the lines that visualize the costs
		
		// Machine intro line
		createLineDrawer(new Vector3D(32, y), "MACHINE COSTS:");
		y += lineHeight;
		
		// Machine lines
		for (MachineType machineType : this.machineAmounts.keySet())
		{
			createLineDrawer(new Vector3D(32, y), this.machineAmounts.get(machineType) + 
					" x " + machineType.getName());
			double typeCosts = 
					this.machineAmounts.get(machineType) * machineType.getPrice();
			createLineDrawer(new Vector3D(GameSettings.screenWidth - 96, y), typeCosts + " M €");
			
			totalMachineCosts += typeCosts;
			y += lineHeight;
		}
		
		// Component intro line
		y += lineHeight;
		createLineDrawer(new Vector3D(32, y), "COMPONENT COSTS:");
		y += lineHeight;
		
		// Component lines
		for (ComponentType componentType : this.componentAmounts.keySet())
		{
			createLineDrawer(new Vector3D(32, y), this.componentAmounts.get(componentType) + 
					" x " + componentType.getName());
			double typeCosts = 
					this.componentAmounts.get(componentType) * componentType.getPrice();
			createLineDrawer(new Vector3D(GameSettings.screenWidth - 96, y), typeCosts + " M €");
			
			totalComponentCosts += typeCosts;
			y += lineHeight;
		}
		
		// Total
		y += lineHeight * 2;
		createLineDrawer(new Vector3D(32, y), "TOTAL: ");
		createLineDrawer(new Vector3D(GameSettings.screenWidth - 96, y), totalMachineCosts + 
				totalComponentCosts + " M €");
		
		// TODO: Add estimated benefit as well as rank
		
		// Calculates the rank
		int costDifferencePercent = (int) (100 * (totalComponentCosts - 
				this.demoComponentCost) / this.demoComponentCost);
		Grade grade = Grade.getGradeFromCostDifference(costDifferencePercent);
		// Draws the rank
		createGradeDrawer(grade, new Vector3D(GameSettings.screenWidth - 96, 
				GameSettings.screenHeight - 96));
	}
	
	/**
	 * Resets the cost calculations for the components
	 */
	public void resetComponentStatus()
	{
		this.componentAmounts.clear();
	}
	
	/**
	 * Resets the cost calculations for the machines
	 */
	public void resetMachineStatus()
	{
		this.machineAmounts.clear();
	}
	
	private void createLineDrawer(Vector3D position, String line)
	{
		new TextDrawer(position, line, null, GameSettings.basicFont, Color.BLACK, 
				new Vector3D(800, 32), Vector3D.zeroVector(), DepthConstants.NORMAL, 
				this.area.getHandlers());
	}
	
	private void createGradeDrawer(Grade grade, Vector3D position)
	{
		SimpleSingleSpriteDrawerObject drawer = new SimpleSingleSpriteDrawerObject(
				DepthConstants.NORMAL, SpriteBank.getSprite("results", "grade"), 
				this.area.getHandlers());
		drawer.getDrawer().getSpriteDrawer().setImageSpeed(0);
		drawer.getDrawer().getSpriteDrawer().setImageIndex(grade.getGradeSpriteIndex());
		
		drawer.setTrasformation(drawer.getTransformation().withPosition(position));
	}
	
	/*
	 * this.spriteDrawer = new SingleSpriteDrawer(
					OpenSpriteBank.getSpriteBank("results").getSprite("grade"), 
					null, this);
			this.spriteDrawer.setImageIndex(grade.getGradeSpriteIndex());
	 */
}
