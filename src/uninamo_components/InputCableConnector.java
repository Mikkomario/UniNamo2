package uninamo_components;

import java.awt.geom.Point2D;

import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;

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
	 * @param relativex The connector's x-coordinate relative to the component's 
	 * top-left corner (pixels)
	 * @param relativey The connector's y-coordinate relative to the component's 
	 * top-left corner (pixels)
	 * @param drawer The drawableHandler that will draw the connector
	 * @param mousehandler The mouseListenerHandler that will inform the 
	 * connector about mouse events
	 * @param relay The connectorRelay that will keep track of the connectors
	 * @param host The host component the connector is tied to
	 */
	public InputCableConnector(int relativex, int relativey,
			DrawableHandler drawer, MouseListenerHandler mousehandler, 
			ConnectorRelay relay, Component host)
	{
		super(relativex, relativey, drawer, mousehandler, relay, host);
		// TODO: Change the parameter to inputComponent and add an attribute
		
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
		// Doesn't react to mouse buttons (cables do that)
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		// Only listens to enter / exit if a new cable is being dragged and 
		// can be placed
		return Cable.cableIsBeingDragged;
	}

	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Point2D mousePosition, double eventStepTime)
	{
		// Scales the object on enter, rescales on exit
		if (eventType == MousePositionEventType.ENTER)
			largen();
		else if (eventType == MousePositionEventType.EXIT)
			rescale();
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
		// Checks if the signal changed and informs the component if that 
		// is the case
		boolean newStatus = calculateNewSignalStatus();
		
		if (newStatus != getSignalStatus())
		{
			this.lastSignalStatus = newStatus;
			// TODO: Inform the component (once you get input and output components done)
		}	
	}
	
	@Override
	public MouseButtonEventScale getCurrentButtonScaleOfInterest()
	{
		// Isn't interested in mouse button presses
		return MouseButtonEventScale.NONE;
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
