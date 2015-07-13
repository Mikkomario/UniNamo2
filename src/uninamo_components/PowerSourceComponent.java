package uninamo_components;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;
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
	 * @param handlers The handlers that will handle the component
	 * @param position The component's position
	 * @param connectorRelay A connectorRelay that will keep track of the 
	 * connectors
	 * @param costDrawer The costDrawer that will be affected by the component 
	 * (optional)
	 * @param isForTesting If this is true, the component will go to test mode 
	 * where it won't react to mouse but will create test cables to its connectors
	 */
	public PowerSourceComponent(HandlerRelay handlers, Vector3D position, 
			ConnectorRelay connectorRelay, CurrentCostDrawer costDrawer, 
			boolean isForTesting)
	{
		super(handlers, position, connectorRelay, costDrawer, "test", 0, 1, true, 
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
