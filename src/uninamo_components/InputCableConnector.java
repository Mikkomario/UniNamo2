package uninamo_components;

import java.awt.geom.Point2D;

import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_worlds.Room;

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
	private DrawableHandler drawer;
	private MouseListenerHandler mousehandler;
	private ConnectorRelay relay;
	private Room room;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new InputCableConnector connected to the given component
	 * 
	 * @param relativex The connector's x-coordinate relative to the component's 
	 * top-left corner (pixels)
	 * @param relativey The connector's y-coordinate relative to the component's 
	 * top-left corner (pixels)
	 * @param drawer The drawableHandler that will draw the connector
	 * @param mousehandler The mouseListenerHandler that will inform the 
	 * connector about mouse events
	 * @param room The room where the connector resides at
	 * @param relay The connectorRelay that will keep track of the connectors
	 * @param host The host component the connector is tied to
	 */
	public InputCableConnector(int relativex, int relativey,
			DrawableHandler drawer, MouseListenerHandler mousehandler, 
			Room room, ConnectorRelay relay, Component host)
	{
		super(relativex, relativey, drawer, mousehandler, room, relay, host);
		
		// Initializes attributes
		this.drawer = drawer;
		this.mousehandler = mousehandler;
		this.relay = relay;
		this.room = room;
		
		// Changes the look of the connector
		getSpriteDrawer().setImageIndex(1);
		
		// Initializes attributes
		this.lastSignalStatus = false;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D mousePosition,
			double eventStepTime)
	{
		// If the connector is clicked with a left mouse button it will create 
		// a new cable
		if (button == MouseButton.LEFT && eventType == 
				MouseButtonEventType.PRESSED && !Cable.cableIsBeingDragged)
		{
			connectCable(new Cable(this.drawer, this.mousehandler, this.room, 
					this.relay, null, this));
		}
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
