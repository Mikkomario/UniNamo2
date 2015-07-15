package uninamo_components;

import exodus_world.Area;
import exodus_world.AreaListener;
import genesis_event.Handler;
import genesis_event.HandlerType;
import uninamo_gameplaysupport.TotalCostAnalyzer;
import uninamo_main.UninamoHandlerType;

/**
 * ComponentRelay keeps track of all the created components 
 * (not in the manual though) and provides data about them. The relay doesn't 
 * support machine components, only normal components.
 * 
 * @author Mikko Hilpinen
 * @since 16.3.2014
 */
public class NormalComponentRelay extends Handler<NormalComponent> implements AreaListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private TotalCostAnalyzer costAnalyzer;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new empty componentRelay
	 * @param costAnalyzer The costAnalyzer that analyzes the data from the 
	 * component relay when necessary
	 */
	public NormalComponentRelay(TotalCostAnalyzer costAnalyzer)
	{
		super(false);
		
		// Initializes attributes
		this.costAnalyzer = costAnalyzer;
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected boolean handleObject(NormalComponent h)
	{
		// Kills the component (part of the killComponents functionality)
		h.getIsDeadStateOperator().setState(true);
		return true;
	}

	@Override
	public void onAreaStateChange(Area area)
	{
		if (area.getIsActiveStateOperator().getState())
			this.costAnalyzer.resetComponentStatus();
		else
			handleObjects(new CostAnalyzerInformerOperator());
	}

	@Override
	public HandlerType getHandlerType()
	{
		return UninamoHandlerType.NORMALCOMPONENT;
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Kills all the currently living normalComponents
	 */
	public void killAllComponents()
	{
		handleObjects();
	}
	
	/**
	 * Calculates the current componentCosts and informs them as the demo costs
	 */
	public void calculateDemoCosts()
	{
		// Creates a new handlingOperator to calculate the costs
		CostOperator operator = new CostOperator();
		
		handleObjects(operator);
		
		// Informs the costAnalyzer about the findings
		this.costAnalyzer.setDemoComponentCosts(operator.getTotalCosts());
	}
	
	
	// SUBCLASSES	-----------------------------------------------------
	
	private class CostOperator extends HandlingOperator
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private double currentCosts;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		public CostOperator()
		{
			// Initializes attributes
			this.currentCosts = 0;
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------
		
		@Override
		protected boolean handleObject(NormalComponent h)
		{
			// Adds the component's costs to the total costs
			this.currentCosts += h.getType().getPrice();
			
			return true;
		}
		
		
		// OTHER METHODS	---------------------------------------------
		
		public double getTotalCosts()
		{
			return this.currentCosts;
		}
	}
	
	private class CostAnalyzerInformerOperator extends HandlingOperator
	{
		// IMPLEMENTED METHODS	----------------------------------------
		
		@Override
		protected boolean handleObject(NormalComponent h)
		{
			// Informs the costs analyzer about the handled type
			NormalComponentRelay.this.costAnalyzer.addComponentCost(h.getType());
			
			return true;
		}
	}
}
