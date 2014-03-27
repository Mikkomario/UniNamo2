package uninamo_components;

import uninamo_gameplaysupport.TestHandler;
import utopia_worlds.Area;

/**
 * MachineOutputComponents take signals from machines and inform other 
 * components of them. MachineOutputComponents cannot be moved.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class MachineOutputComponent extends MachineComponent
{
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new machineOutputComponent that will be ready to send new 
	 * signals.
	 * 
	 * @param area The area where the object will reside at
	 * @param x The x-coordinate of the component
	 * @param y The y-coordinate of the component
	 * @param testHandler The testHandler that will inform the object about 
	 * test events
	 * @param connectorRelay The connectorRelay that keeps track of the 
	 * connectors
	 * @param spritename The name of the sprite the component uses
	 * @param outputs THe number of output connectors the component has
	 * @param isForTesting Is the machine component created for simple 
	 * demonstration purposes
	 * @param hostName The name of the machine that created this component
	 */
	public MachineOutputComponent(Area area, int x, int y, TestHandler testHandler, 
			ConnectorRelay connectorRelay, 
			String spritename, int outputs, boolean isForTesting, 
			String hostName)
	{
		super(area, x, y, testHandler, connectorRelay, spritename, 0, outputs, 
				isForTesting, hostName + "O");
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onSignalChange(boolean newSignalStatus, SignalSender source)
	{
		// Doesn't react to input signal. Use sendSignal to relay signals through 
		// the component instead
	}

	@Override
	public boolean getSignalStatus()
	{
		// Cannot inform any specific signal since the component has (possibly) 
		// multiple outputs
		return false;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Other components (machines) can send signals through the component 
	 * using this method.
	 * 
	 * @param signal The type of signal
	 * @param outputIndex The output connector's index
	 */
	public void sendSignal(boolean signal, int outputIndex)
	{
		sendSignalToOutput(outputIndex, signal);
	}
}
