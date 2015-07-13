package uninamo_components;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;

/**
 * InputCableConnector is a cableConnector that handles signals coming into 
 * a component. The connector serves as a relay and informs the components if 
 * the signal changes.
 * 
 * @author Mikko Hilpinen
 */
public class InputCableConnector extends CableConnector
{
	// ATTRIBUTES	------------------------------------------------------
	
	private boolean lastSignalStatus;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new InputCableConnector connected to the given component
	 * 
	 * @param handlers The handlers that will handle the connector
	 * @param relativePosition The connector's position relative to the component
	 * @param host The host component the connector is tied to
	 * @param inputIndex Which if the host's inputs this connector is
	 * @param isForTesting If this is true, the connector will go to test mode 
	 * and not react to mouse. It will, however, create a test cable connected 
	 * to it
	 * @param relay The connector relay that keeps track of the connectors
	 */
	public InputCableConnector(HandlerRelay handlers, Vector3D relativePosition,
			Component host, int inputIndex, boolean isForTesting, ConnectorRelay relay)
	{
		super(handlers, relativePosition, host, isForTesting, host.getID() + "I" + inputIndex, 
				relay);
		
		// Initializes attributes
		this.lastSignalStatus = false;
		
		// If is on test mode, creates a test cable
		if (isForTesting)
			connectCable(new Cable(handlers, relay, null, this, true, Vector3D.zeroVector()));
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public boolean getSignalStatus()
	{
		// Returns the last known status of the signal
		return this.lastSignalStatus;
	}

	@Override
	public void onSignalChange(boolean newSignalStatus, SignalSender source)
	{
		//System.out.println("Input receives new signal");
		
		// Checks if the signal changed and informs the component if that 
		// is the case
		boolean newStatus = calculateNewSignalStatus();
		
		if (newStatus != getSignalStatus())
		{
			this.lastSignalStatus = newStatus;
			// Informs the component as well
			getMaster().onSignalChange(getSignalStatus(), this);
			
			if (this.lastSignalStatus)
				getSpriteDrawer().setImageIndex(1);
			else
				getSpriteDrawer().setImageIndex(0);
		}	
	}
	
	@Override
	protected void createCable(HandlerRelay handlers, ConnectorRelay relay,
			Vector3D mousePosition)
	{
		new Cable(handlers, relay, null, this, false, mousePosition);
	}
	
	@Override
	public void connectCable(Cable c)
	{
		super.connectCable(c);
		
		// In addition to normal connecting, recalculates the signal status
		onSignalChange(c.getSignalStatus(), c);
	}
	
	@Override
	public void removeCable(Cable c)
	{
		super.removeCable(c);
		
		// In addition to removal, recalculates the signal status
		onSignalChange(false, c);
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	private boolean calculateNewSignalStatus()
	{
		// Checks all the cables connected to the connector. The signal is 
		// true if any of those cables' signal is true
		boolean signal = false;
		
		for (int i = 0; i < getCableAmount(); i++)
		{
			if (getCable(i).getSignalStatus())
			{
				signal = true;
				break;
			}
		}
		
		return signal;
	}
}
