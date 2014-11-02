package uninamo_components;

import java.awt.geom.Point2D;

import omega_world.Area;
import uninamo_gameplaysupport.TestHandler;

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
	
	private Area area;
	private boolean lastSignalStatus;
	private ConnectorRelay relay;
	private TestHandler testHandler;
	private String hostConnectInfo;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new outputCableConnector connected to the given component
	 * 
	 * @param area The area where the object will reside at
	 * @param relativex The connector's x-coordinate in relation to the 
	 * component's top-left corner (pixels)
	 * @param relativey The connector's x-coordinate in relation to the 
	 * component's top-left corner (pixels)
	 * @param testHandler The testHandler that will inform the connector 
	 * about test events
	 * @param relay The connectorRelay that will keep track of the connectors
	 * @param host The component the connector is tied to
	 * @param outputIndex Which of the host's output connectors this one is
	 * @param isForTesting If this is true, the connector will go to test mode 
	 * and not react to mouse. It will, however create a test cable connected 
	 * to it
	 */
	public OutputCableConnector(Area area, int relativex, int relativey,
			TestHandler testHandler, ConnectorRelay relay, 
			Component host, int outputIndex, boolean isForTesting)
	{
		super(area, relativex, relativey, relay, host, isForTesting);
		
		// Initializes attributes
		this.lastSignalStatus = false;
		this.area = area;
		this.relay = relay;
		this.testHandler = testHandler;
		this.hostConnectInfo = "O" + outputIndex;
		
		// Changes the look of the connector
		getSpriteDrawer().setSpriteIndex(1, false);
		
		// If is on test mode, creates a test cable
		if (isForTesting)
		{
			connectCable(new Cable(area, testHandler, relay, this, null, true));
		}
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
			new Cable(this.area, this.testHandler, this.relay, this, null, false);
	}

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
	public String getID()
	{
		return getHost().getID() + this.hostConnectInfo;
	}
}
