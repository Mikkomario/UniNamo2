package uninamo_components;

import omega_world.Area;
import uninamo_gameplaysupport.TestHandler;
import uninamo_userinterface.CurrentCostDrawer;

/**
 * PowerSourceComponent is a very simple component that simply sends true 
 * signal all the time.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class PowerSourceComponent extends NormalComponent
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new powerSource that immediately starts to send true signal
	 * 
	 * @param area The area where the object will reside at
	 * @param x The x-coordinate of the component
	 * @param y The y-coordinate of the component
	 * @param testHandler The testHandler that will inform the object about 
	 * test events
	 * @param connectorRelay A connectorRelay that will keep track of the 
	 * connectors
	 * @param componentRelay The componentRelay that will keep track of the 
	 * component
	 * @param costDrawer The costDrawer that will be affected by the component 
	 * (optional)
	 * @param isForTesting If this is true, the component will go to test mode 
	 * where it won't react to mouse but will create test cables to its connectors
	 */
	public PowerSourceComponent(Area area, int x, int y, 
			TestHandler testHandler, ConnectorRelay connectorRelay, 
			NormalComponentRelay componentRelay, CurrentCostDrawer costDrawer, 
			boolean isForTesting)
	{
		super(area, x, y, testHandler, 
				connectorRelay, componentRelay, costDrawer, "test", 0, 1, true, 
				isForTesting);
		
		// Informs the output(s) about the component's status
		sendSignalToOutput(0, true);
	}

	@Override
	public void onSignalChange(boolean newSignalStatus, SignalSender source)
	{
		// Inputs don't react to signal changes
	}

	@Override
	public boolean getSignalStatus()
	{
		return true;
	}

	@Override
	public ComponentType getType()
	{
		return ComponentType.POWER;
	}
}
