package uninamo_components;

import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_worlds.Room;

/**
 * OrComponent is a logical unit that sends true signal if either of its inputs 
 * receive true signal
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class OrComponent extends Component
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private boolean signalStatus;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new OrComponent to the given position.
	 * 
	 * @param x The x-coordinate of the component (pixels)
	 * @param y The y-coordinate of the component (pixels)
	 * @param drawer The drawableHandler that will draw the component
	 * @param mousehandler The mouseListenerHandler that will inform the 
	 * object about mouse events
	 * @param room The room where the component resides at
	 * @param connectorRelay A connectorRelay that will keep track of the 
	 * connectors
	 */
	public OrComponent(int x, int y, DrawableHandler drawer, 
			MouseListenerHandler mousehandler, Room room, 
			ConnectorRelay connectorRelay)
	{
		super(x, y, drawer, null, mousehandler, room, connectorRelay, "test", 2, 1);
		
		// Initializes attributes
		this.signalStatus = false;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onSignalChange(boolean newSignalStatus, SignalSender source)
	{
		System.out.println("Or signal changes");
		
		// Informs the new signal to the output connector
		this.signalStatus = (getInputStatus(0) || getInputStatus(1));
		sendSignalToOutput(0, this.signalStatus);
		System.out.println("Now sends signal: " + this.signalStatus);
	}

	@Override
	public boolean getSignalStatus()
	{
		return this.signalStatus;
	}
}
