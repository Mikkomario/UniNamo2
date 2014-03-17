package uninamo_components;

import uninamo_gameplaysupport.TotalCostAnalyzer;
import utopia_handleds.Handled;
import utopia_handlers.Handler;
import utopia_listeners.RoomListener;
import utopia_worlds.Room;

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
	 */
	public NormalComponentRelay(TotalCostAnalyzer costAnalyzer)
	{
		super(false, null);
		
		// Initializes attributes
		this.costAnalyzer = costAnalyzer;
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
	
	@Override
	public void onRoomStart(Room room)
	{
		// Creates a new componentCostDrawer
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Kills the componentCostDrawer
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
}
