package uninamo_components;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;

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
	 * @param handlers The handlers that will handle the component
	 * @param position The component's position
	 * @param connectorRelay The connectorRelay that keeps track of the 
	 * connectors
	 * @param spritename The name of the sprite the component uses
	 * @param outputs THe number of output connectors the component has
	 * @param isForTesting Is the machine component created for simple 
	 * demonstration purposes
	 * @param hostName The name of the machine that created this component
	 */
	public MachineOutputComponent(HandlerRelay handlers,Vector3D position,  
			ConnectorRelay connectorRelay, 
			String spritename, int outputs, boolean isForTesting, 
			String hostName)
	{
		super(handlers, position, connectorRelay, spritename, 0, outputs, isForTesting, 
				hostName + "O");
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
