package uninamo_components;

import uninamo_gameplaysupport.TestHandler;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_worlds.Room;

/**
 * MachineOutputComponents take signals from machines and inform other 
 * components of them. MachineOutputComponents cannot be moved.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class MachineOutputComponent extends Component
{
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new machineOutputComponent that will be ready to send new 
	 * signals.
	 * 
	 * @param x The x-coordinate of the component
	 * @param y The y-coordinate of the component
	 * @param drawer The drawableHandler that will draw the component
	 * @param actorhandler The actorHandler that will inform the component 
	 * about step events (= animate the component)
	 * @param mousehandler The mouseHandler that will inform the component 
	 * about mouse events 
	 * @param room The room where the component resides
	 * @param testHandler The testHandler that will inform the object about 
	 * test events
	 * @param connectorRelay The connectorRelay that keeps track of the 
	 * connectors
	 * @param spritename The name of the sprite the component uses
	 * @param outputs THe number of output connectors the component has
	 */
	public MachineOutputComponent(int x, int y, DrawableHandler drawer,
			ActorHandler actorhandler, MouseListenerHandler mousehandler,
			Room room, TestHandler testHandler, ConnectorRelay connectorRelay, 
			String spritename, int outputs)
	{
		super(x, y, drawer, actorhandler, mousehandler, room, testHandler, 
				connectorRelay, spritename, 0, outputs, false, false);
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
