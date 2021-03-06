package uninamo_components;

import omega_world.Room;
import omega_world.RoomListener;
import genesis_logic.Handled;
import genesis_logic.Handler;
import uninamo_gameplaysupport.TotalCostAnalyzer;

/**
 * ComponentRelay keeps track of all the created components 
 * (not in the manual though) and provides data about them. The relay doesn't 
 * support machine components, only normal components.
 * 
 * @author Mikko Hilpinen
 * @since 16.3.2014
 */
public class NormalComponentRelay extends Handler implements RoomListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private TotalCostAnalyzer costAnalyzer;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new empty componentRelay
	 * @param costAnalyzer The costAnalyzer that analyzes the data from the 
	 * component relay when necessary
	 * @param componentArea The area where the components are located at
	 */
	public NormalComponentRelay(TotalCostAnalyzer costAnalyzer, Room componentArea)
	{
		super(false, null);
		
		// Initializes attributes
		this.costAnalyzer = costAnalyzer;
		
		// Adds the object to the handler(s)
		if (componentArea != null)
			componentArea.addRoomListener(this);
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return NormalComponent.class;
	}

	@Override
	protected boolean handleObject(Handled h)
	{
		// Kills the component (part of the killComponents functionality)
		h.kill();
		return true;
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
	 * Adds a new component to the relay
	 * @param c The component to be added to the relay
	 */
	public void addComponent(NormalComponent c)
	{
		addHandled(c);
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
		protected boolean handleObject(Handled h)
		{
			// Adds the component's costs to the total costs
			NormalComponent c = (NormalComponent) h;
			this.currentCosts += c.getType().getPrice();
			
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
		protected boolean handleObject(Handled h)
		{
			// Informs the costs analyzer about the handled type
			NormalComponentRelay.this.costAnalyzer.addComponentCost(
					((NormalComponent) h).getType());
			
			return true;
		}
	}

	@Override
	public void onRoomStart(Room room)
	{
		// Resets the cost calculator
		this.costAnalyzer.resetComponentStatus();
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Informs the costAnalyzer about component status
		handleObjects(new CostAnalyzerInformerOperator());
	}
}
