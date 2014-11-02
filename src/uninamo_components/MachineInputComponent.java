package uninamo_components;

import java.util.HashMap;

import omega_world.Area;
import uninamo_gameplaysupport.TestHandler;
import uninamo_machinery.Machine;

/**
 * MachineInputComponents relay signals to specific machines which then can 
 * react to them. Also, machine components cannot be moved on the workbench
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class MachineInputComponent extends MachineComponent
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Machine host;
	private HashMap<Integer, Boolean> signaldata;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new machineInputComponent to the given position with the 
	 * given sprite and host machine.
	 * 
	 * @param area The area where the object will reside at
	 * @param x The component's x-coordinate
	 * @param y The component's y-coordinate
	 * @param testHandler The testHandler that will inform the object about 
	 * test events
	 * @param connectorRelay The connectorRelay that keeps track of all the 
	 * connectors
	 * @param spritename The name of the sprite the component uses
	 * @param inputs How many inputs does the component have
	 * @param host the componen't host machine
	 * @param isForTesting Is the machine component created for simple 
	 * demonstration purposes
	 * @param hostName The name of the machine that created this component
	 */
	public MachineInputComponent(Area area, int x, int y, TestHandler testHandler, 
			ConnectorRelay connectorRelay, 
			String spritename, int inputs, Machine host, boolean isForTesting, 
			String hostName)
	{
		super(area, x, y, testHandler, connectorRelay, spritename, inputs, 0, 
				isForTesting, hostName + "I");
		
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
}
