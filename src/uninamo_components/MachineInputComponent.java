package uninamo_components;

import java.util.HashMap;

import uninamo_machinery.Machine;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_worlds.Room;

/**
 * MachineInputComponents relay signals to specific machines which then can 
 * react to them. Also, machine components cannot be moved on the workbench
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class MachineInputComponent extends Component
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Machine host;
	private HashMap<Integer, Boolean> signaldata;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new machineInputComponent to the given position with the 
	 * given sprite and host machine.
	 * 
	 * @param x The component's x-coordinate
	 * @param y The component's y-coordinate
	 * @param drawer The drawableHandler that will draw the component
	 * @param actorhandler The actorHandler that will animate the component
	 * @param mousehandler The mouseListenerHandler that will inform the 
	 * component about mouse events
	 * @param room The room where the component resides
	 * @param connectorRelay The connectorRelay that keeps track of all the 
	 * connectors
	 * @param spritename The name of the sprite the component uses
	 * @param inputs How many inputs does the component have
	 * @param host the componen't host machine
	 */
	public MachineInputComponent(int x, int y, DrawableHandler drawer,
			ActorHandler actorhandler, MouseListenerHandler mousehandler,
			Room room, ConnectorRelay connectorRelay, String spritename,
			int inputs, Machine host)
	{
		super(x, y, drawer, actorhandler, mousehandler, room, connectorRelay,
				spritename, inputs, 0);
		
		// Initializes attributes
		this.host = host;
		this.signaldata = new HashMap<Integer, Boolean>();
		
		for (int i = 0; i < inputs; i++)
		{
			this.signaldata.put(i, false);
		}
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onSignalChange(boolean newSignalStatus, SignalSender source)
	{
		if (!(source instanceof InputCableConnector))
			return;
		
		// If the signal changed, informs the machine
		int index = getInputIndex((InputCableConnector) source);
		
		if (index < 0)
			return;
		
		// Checks if the signal changed
		if (newSignalStatus != this.signaldata.get(index))
		{
			this.signaldata.put(index, newSignalStatus);
			this.host.onSignalEvent(newSignalStatus, index);
		}
	}

	@Override
	public boolean getSignalStatus()
	{
		if (this.signaldata == null || this.signaldata.size() == 0)
			return false;
		
		return this.signaldata.get(0);
	}
	
	@Override
	public MouseButtonEventScale getCurrentButtonScaleOfInterest()
	{
		// MachineInputComponents don't react to mouse
		return MouseButtonEventScale.NONE;
	}
	
	@Override
	public boolean listensMouseEnterExit()
	{
		return false;
	}
}
