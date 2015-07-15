package uninamo_userinterface;

import java.awt.Graphics2D;

import exodus_world.Area;
import exodus_world.AreaListener;
import genesis_event.Drawable;
import genesis_event.HandlerRelay;
import genesis_util.DepthConstants;
import genesis_util.StateOperator;
import omega_util.SimpleGameObject;
import uninamo_main.GameSettings;

/**
 * CurrentCostDrawer draws the current total costs of used components as well 
 * as machinery
 * 
 * @author Mikko Hilpinen
 * @since 17.3.2014
 */
public class CurrentCostDrawer extends SimpleGameObject implements AreaListener, Drawable
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private double currentCosts;
	private String currentCostString;
	private StateOperator isVisibleOperator;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new CurrentCostDrawer
	 * @param handlers The handlers that will handle the drawer
	 */
	public CurrentCostDrawer(HandlerRelay handlers)
	{
		super(handlers);
		
		// Initializes attributes
		this.isVisibleOperator = new StateOperator(true, true);
		this.currentCosts = 0;
		this.currentCostString = null;
		
		updateText();
	}

	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void drawSelf(Graphics2D g2d)
	{
		// Draws the current total
		g2d.drawString(this.currentCostString , GameSettings.screenWidth - 250, 
				GameSettings.screenHeight - 16);
	}

	@Override
	public int getDepth()
	{
		return DepthConstants.HUD;
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return this.isVisibleOperator;
	}

	@Override
	public void onAreaStateChange(Area area)
	{
		if (!area.getIsActiveStateOperator().getState())
			getIsDeadStateOperator().setState(true);
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * This method is used for increasing and decreasing the current drawn total
	 * 
	 * @param amount How much the current costs should increase (or decrease)
	 */
	public void addCosts(double amount)
	{
		this.currentCosts += amount;
		updateText();
	}
	
	private void updateText()
	{
		this.currentCostString = "Component Costs: " + 
				String.format("%.2f", this.currentCosts) + " M €";
	}
}
