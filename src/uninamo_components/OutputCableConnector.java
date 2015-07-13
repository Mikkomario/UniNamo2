package uninamo_components;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;

/**
 * OutputCableConnectors take signal events from components and relays them to 
 * multiple cables.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public class OutputCableConnector extends CableConnector
{
	// ATTRIBUTES	------------------------------------------------------
	
	private boolean lastSignalStatus;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new outputCableConnector connected to the given component
	 * 
	 * @param handlers The handlers that will handle the connector
	 * @param relativePosition The connector's position relative to the component
	 * @param host The component this connector is tied to
	 * @param outputIndex Which of the host's output connectors this one is
	 * @param isForTesting If this is true, the connector will go to test mode 
	 * and not react to mouse. It will, however create a test cable connected 
	 * to it
	 * @param relay The connector relay that keeps track of the connectors
	 */
	public OutputCableConnector(HandlerRelay handlers, Vector3D relativePosition,
			Component host, int outputIndex, boolean isForTesting, ConnectorRelay relay)
	{
		super(handlers, relativePosition, host, isForTesting, host.getID() +  "O" + 
				outputIndex, relay);
		
		// Initializes attributes
		this.lastSignalStatus = false;
		
		// Changes the look of the connector
		getSpriteDrawer().setSpriteIndex(1, false);
		
		// If is on test mode, creates a test cable
		if (isForTesting)
			connectCable(new Cable(handlers, relay, this, null, true, Vector3D.zeroVector()));
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public boolean getSignalStatus()
	{
		return this.lastSignalStatus;
	}

	@Override
	public void onSignalChange(boolean newSignalStatus, SignalSender source)
	{
		// Updates the last signal status and informs the cables if the status 
		// changed
		if (newSignalStatus != this.lastSignalStatus)
		{
			this.lastSignalStatus = newSignalStatus;
			
			for (int i = 0; i < getCableAmount(); i++)
			{
				getCable(i).onSignalChange(getSignalStatus(), this);
			}
			
			if (this.lastSignalStatus)
				getSpriteDrawer().setImageIndex(1);
			else
				getSpriteDrawer().setImageIndex(0);
		}
	}
	
	@Override
	public void connectCable(Cable c)
	{
		super.connectCable(c);
		
		// In addition to normal connecting, informs the signal to the cable
		c.onSignalChange(getSignalStatus(), this);
	}
	
	@Override
	public void removeCable(Cable c)
	{
		super.removeCable(c);
		
		// In addition to removal, resets signal status to false
		c.onSignalChange(false, this);
	}

	@Override
	protected void createCable(HandlerRelay handlers, ConnectorRelay relay,
			Vector3D mousePosition)
	{
		new Cable(handlers, relay, this, null, false, mousePosition);
	}
}
