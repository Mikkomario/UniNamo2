package uninamo_components;

import genesis_event.HandlerRelay;
import genesis_util.StateOperator;
import genesis_util.Vector3D;
import uninamo_gameplaysupport.TestEvent;
import uninamo_gameplaysupport.TurnBased;
import uninamo_gameplaysupport.TestEvent.TestEventType;
import uninamo_userinterface.CurrentCostDrawer;

/**
 * Pulse generator sends true and false signal in pulses, starting from false.
 * 
 * @author Mikko Hilpinen
 */
public class PulseGeneratorComponent extends NormalComponent implements TurnBased
{
	// ATTRIBUTES	------------------------------------------------------
	
	private boolean lastSignalType;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new pulseGenerator to the given position
	 * @param handlers The handlers that will handle the component
	 * @param position The component's position
	 * @param connectorRelay The connectorRelay that keeps track of all the 
	 * connectors
	 * @param costDrawer The costDrawer that will be affected by the component 
	 * (optional)
	 * @param isForTesting If this is true, the component will go to test mode 
	 * where it won't react to mouse but will create test cables to its connectors
	 */
	public PulseGeneratorComponent(HandlerRelay handlers, Vector3D position, 
			ConnectorRelay connectorRelay, CurrentCostDrawer costDrawer, boolean isForTesting)
	{
		super(handlers, position, connectorRelay, costDrawer, "test", 0, 1, true, 
				isForTesting);
		
		// Initializes attributes
		this.lastSignalType = false;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onSignalChange(boolean newSignalStatus, SignalSender source)
	{
		// Doesnt react to signal input
	}

	@Override
	public boolean getSignalStatus()
	{
		return this.lastSignalType;
	}

	@Override
	public void onTurnEvent()
	{
		// Changes the signal type and informs the connector
		this.lastSignalType = !this.lastSignalType;
		sendSignalToOutput(0, this.lastSignalType);
	}
	
	@Override
	public void onTestEvent(TestEvent event)
	{
		super.onTestEvent(event);;
		
		// Resets the signal on test start
		if (event.getType() == TestEventType.START)
		{
			this.lastSignalType = false;
			sendSignalToOutput(0, this.lastSignalType);
		}
	}

	@Override
	public ComponentType getType()
	{
		return ComponentType.PULSE;
	}

	@Override
	public StateOperator getListensToTurnEventsOperator()
	{
		return getIsActiveStateOperator();
	}
}
