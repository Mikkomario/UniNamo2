package uninamo_components;

import java.awt.geom.Point2D;

import uninamo_gameplaysupport.TestHandler;
import utopia_worlds.Area;

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
	
	private Area area;
	private boolean lastSignalStatus;
	private ConnectorRelay relay;
	private TestHandler testHandler;
	private String hostConnectInfo;
	
	// TODO: These should be moved to the superclass
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new InputCableConnector connected to the given component
	 * 
	 * @param area The area where the object will reside at
	 * @param relativex The connector's x-coordinate relative to the component's 
	 * top-left corner (pixels)
	 * @param relativey The connector's y-coordinate relative to the component's 
	 * top-left corner (pixels)
	 * @param testHandler The testHandler that will inform the cables about 
	 * test events
	 * @param relay The connectorRelay that will keep track of the connectors
	 * @param host The host component the connector is tied to
	 * @param inputIndex Which if the host's inputs this connector is
	 * @param isForTesting If this is true, the connector will go to test mode 
	 * and not react to mouse. It will, however, create a test cable connected 
	 * to it
	 */
	public InputCableConnector(Area area, int relativex, int relativey,
			TestHandler testHandler, ConnectorRelay relay, 
			Component host, int inputIndex, boolean isForTesting)
	{
		super(area, relativex, relativey, relay, host, isForTesting);
		
		// Initializes attributes
		this.area = area;
		this.relay = relay;
		this.testHandler = testHandler;
		this.lastSignalStatus = false;
		this.hostConnectInfo = "I" + inputIndex;
		
		// If is on test mode, creates a test cable
		if (isForTesting)
			connectCable(new Cable(area, testHandler, relay, null, this, true));
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D.Double mousePosition,
			double eventStepTime)
	{
		// If the connector is clicked with a left mouse button it will create 
		// a new cable
		if (button == MouseButton.LEFT && eventType == 
				MouseButtonEventType.PRESSED && !Cable.cableIsBeingDragged)
			new Cable(this.area, 
					this.testHandler, this.relay, null, this, false);
	}

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
			getHost().onSignalChange(getSignalStatus(), this);
			
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
	
	@Override
	public String getID()
	{
		return getHost().getID() + this.hostConnectInfo;
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
