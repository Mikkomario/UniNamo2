package uninamo_components;

import uninamo_gameplaysupport.TestHandler;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_worlds.Room;

/**
 * PowerSourceComponent is a very simple component that simply sends true 
 * signal all the time.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class PowerSourceComponent extends Component
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new powerSource that immediately starts to send true signal
	 * 
	 * @param x The x-coordinate of the component
	 * @param y The y-coordinate of the component
	 * @param drawer The drawableHandler that will draw the component
	 * @param actorhandler The actorHandler that will animate the component
	 * @param mousehandler The mouseListenerHandler that will inform the 
	 * component about mouse events
	 * @param room The room where the component resides at
	 * @param testHandler The testHandler that will inform the object about 
	 * test events
	 * @param connectorRelay A connectorRelay that will keep track of the 
	 * connectors
	 */
	public PowerSourceComponent(int x, int y, DrawableHandler drawer,
			ActorHandler actorhandler, MouseListenerHandler mousehandler, 
			Room room, TestHandler testHandler, ConnectorRelay connectorRelay)
	{
		super(x, y, drawer, actorhandler, mousehandler, room, testHandler, 
				connectorRelay, "test", 0, 1);
		
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
}
