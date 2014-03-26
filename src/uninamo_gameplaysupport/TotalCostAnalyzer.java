package uninamo_gameplaysupport;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;

import uninamo_components.ComponentType;
import uninamo_machinery.MachineType;
import uninamo_main.GameSettings;
import utopia_gameobjects.DrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.DepthConstants;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import utopia_worlds.Room;

/**
 * TotalCostAnalyzer can calculate the final costs of the creation, analyze 
 * their sources and form a grade.
 * 
 * @author Mikko Hilpinen
 * @since 17.3.2014
 */
public class TotalCostAnalyzer implements RoomListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private double demoComponentCost;
	private HashMap<ComponentType, Integer> componentAmounts;
	private HashMap<MachineType, Integer> machineAmounts;
	private DrawableHandler textDrawer;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new totalCostAnalyzer. The data must be added separately.
	 * @param area The area where the visualization will be drawn
	 */
	public TotalCostAnalyzer(Area area)
	{
		// Initializes attributes
		this.demoComponentCost = 0;
		this.componentAmounts = new HashMap<ComponentType, Integer>();
		this.machineAmounts = new HashMap<MachineType, Integer>();
		this.textDrawer = new DrawableHandler(false, false, 
				DepthConstants.NORMAL, 0, area.getDrawer());
		
		// Adds the object to the handler(s)
		if (area != null)
			area.addRoomListener(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public boolean isDead()
	{
		// Can't be killed
		return false;
	}

	@Override
	public void kill()
	{
		// Can't be killed
	}

	@Override
	public void onRoomStart(Room room)
	{
		// Visualizes the data
		visualizeCosts();
	}
	
	@Override
	public void onRoomEnd(Room room)
	{
		// Ends the visualization
		// TODO: End visualization
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
		new TextLineDrawer(32, y, "MACHINE COSTS:", this.textDrawer);
		y += lineHeight;
		
		// Machine lines
		for (MachineType machineType : this.machineAmounts.keySet())
		{
			new TextLineDrawer(32, y, this.machineAmounts.get(machineType) + 
					" x " + machineType.getName(), this.textDrawer);
			double typeCosts = 
					this.machineAmounts.get(machineType) * machineType.getPrice();
			new TextLineDrawer(GameSettings.screenWidth - 96, y, 
					typeCosts + " M €", this.textDrawer);
			
			totalMachineCosts += typeCosts;
			y += lineHeight;
		}
		
		// Component intro line
		y += lineHeight;
		new TextLineDrawer(32, y, "COMPONENT COSTS:", this.textDrawer);
		y += lineHeight;
		
		// Component lines
		for (ComponentType componentType : this.componentAmounts.keySet())
		{
			new TextLineDrawer(32, y, this.componentAmounts.get(componentType) + 
					" x " + componentType.getName(), this.textDrawer);
			double typeCosts = 
					this.componentAmounts.get(componentType) * componentType.getPrice();
			new TextLineDrawer(GameSettings.screenWidth - 96, y, 
					typeCosts + " M €", this.textDrawer);
			
			totalComponentCosts += typeCosts;
			y += lineHeight;
		}
		
		// Total
		y += lineHeight * 2;
		new TextLineDrawer(32, y, "TOTAL: ", this.textDrawer);
		new TextLineDrawer(GameSettings.screenWidth - 96, y, 
				totalMachineCosts + totalComponentCosts + " M €", this.textDrawer);
		
		// TODO: Add estimated benefit as well as rank
		
		// Calculates the rank
		int costDifferencePercent = (int) (100 * (totalComponentCosts - 
				this.demoComponentCost) / this.demoComponentCost);
		Grade grade = Grade.getGradeFromCostDifference(costDifferencePercent);
		// Draws the rank
		new GradeDrawer(GameSettings.screenWidth - 96, 
				GameSettings.screenHeight - 96, this.textDrawer, grade);
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
	
	
	// SUBCLASSES	----------------------------------------------------
	
	private class TextLineDrawer extends DrawnObject
	{
		// ATTRIBUTES	-----------------------------------------------
		
		private String line;
		
		
		// CONSTRUCTOR	------------------------------------------------
		
		public TextLineDrawer(int x, int y, String line, DrawableHandler drawer)
		{
			super(x, y, DepthConstants.NORMAL, drawer);
			
			// Initializes attributes
			this.line = line;
		}
		
		
		// IMPLEMENTED METHODS	----------------------------------------

		@Override
		public int getOriginX()
		{
			return 0;
		}

		@Override
		public int getOriginY()
		{
			return 0;
		}

		@Override
		public void drawSelfBasic(Graphics2D g2d)
		{
			// Draws the text
			g2d.setFont(GameSettings.basicFont);
			g2d.setColor(Color.BLACK);
			g2d.drawString(this.line, 0, 0);
		}
	}
	
	private class GradeDrawer extends DrawnObject
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private SingleSpriteDrawer spriteDrawer;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		public GradeDrawer(int x, int y, DrawableHandler drawer, Grade grade)
		{
			super(x, y, DepthConstants.NORMAL, drawer);
			
			// Initializes attributes
			this.spriteDrawer = new SingleSpriteDrawer(
					MultiMediaHolder.getSpriteBank("results").getSprite("grade"), 
					null, this);
			this.spriteDrawer.setImageIndex(grade.getGradeSpriteIndex());
		}
		
		
		// IMPLEMENTED METHODS	----------------------------------------

		@Override
		public int getOriginX()
		{
			if (this.spriteDrawer == null)
				return 0;
			return this.spriteDrawer.getSprite().getOriginX();
		}

		@Override
		public int getOriginY()
		{
			if (this.spriteDrawer == null)
				return 0;
			return this.spriteDrawer.getSprite().getOriginY();
		}

		@Override
		public void drawSelfBasic(Graphics2D g2d)
		{
			if (this.spriteDrawer == null)
				return;
			
			this.spriteDrawer.drawSprite(g2d, 0, 0);
		}	
	}
}
