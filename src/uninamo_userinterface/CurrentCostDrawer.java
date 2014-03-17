package uninamo_userinterface;

import java.awt.Graphics2D;

import uninamo_main.GameSettings;
import uninamo_worlds.Area;
import utopia_gameobjects.DrawnObject;
import utopia_helpAndEnums.DepthConstants;
import utopia_listeners.RoomListener;
import utopia_worlds.Room;

/**
 * CurrentCostDrawer draws the current total costs of used components as well 
 * as machinery
 * 
 * @author Mikko Hilpinen
 * @since 17.3.2014
 */
public class CurrentCostDrawer extends DrawnObject implements RoomListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private double currentCosts;
	
	// TODO: Add machine costs from future machineRelay class or something like 
	// that
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new CurrentCostDrawer
	 * 
	 * @param area The area where the costs will be drawn
	 */
	public CurrentCostDrawer(Area area)
	{
		super(GameSettings.screenWidth - 250, GameSettings.screenHeight - 16, 
				DepthConstants.HUD, area.getDrawer());
		
		// Initializes attributes
		this.currentCosts = 0;
		
		// Adds the object to the handler(s)
		if (area != null)
			area.addObject(this);
	}

	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void onRoomStart(Room room)
	{
		// Does nothing
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Dies
		kill();
	}

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
		// Draws the current total
		g2d.drawString("Current Costs: " + this.currentCosts + " M €", 0, 0);
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
	}
}
