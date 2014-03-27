package uninamo_components;

import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TurnBased;
import uninamo_gameplaysupport.TurnHandler;
import uninamo_userinterface.CurrentCostDrawer;
import utopia_worlds.Area;

/**
 * Pulse generator sends true and false signal in pulses, starting from false.
 * 
 * @author Mikko Hilpinen
 */
public class PulseGeneratorComponent extends NormalComponent implements TurnBased
{
	// ATTRIBUTES	------------------------------------------------------
	
	private boolean lastSignalType;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new pulseGenerator to the given position
	 * 
	 * @param area The area where the object will reside at
	 * @param x The generator's x-coordinate (pixels)
	 * @param y The generator's y-coordinate (pixels)
	 * @param testHandler The testHandler that informs the object about 
	 * test events
	 * @param connectorRelay The connectorRelay that keeps track of all the 
	 * connectors
	 * @param componentRelay The componentRelay that will keep track of the 
	 * component
	 * @param costDrawer The costDrawer that will be affected by the component 
	 * (optional)
	 * @param turnHandler The turnHandler that informs the object about 
	 * turn events
	 * @param isForTesting If this is true, the component will go to test mode 
	 * where it won't react to mouse but will create test cables to its connectors
	 */
	public PulseGeneratorComponent(Area area, int x, int y, 
			TestHandler testHandler, ConnectorRelay connectorRelay, 
			NormalComponentRelay componentRelay, CurrentCostDrawer costDrawer, 
			TurnHandler turnHandler, boolean isForTesting)
	{
		super(area, x, y, testHandler,
				connectorRelay, componentRelay, costDrawer, "test", 0, 1, true, 
				isForTesting);
		
		// Initializes attributes
		this.lastSignalType = false;
		
		// Adds the object to the handler(s)
		if (turnHandler != null)
			turnHandler.addTurnListener(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onSignalChange(boolean newSignalStatus, SignalSender source)
	{
		// Doesnt react to signal input
	}

	@Override
	public boolean getSignalStatus()
	{
		return this.lastSignalType;
	}

	@Override
	public void onTurnEvent()
	{
		// Changes the signal type and informs the connector
		this.lastSignalType = !this.lastSignalType;
		sendSignalToOutput(0, this.lastSignalType);
	}
	
	@Override
	public void onTestStart()
	{
		super.onTestStart();
		
		// Resets the signal on test start
		this.lastSignalType = false;
		sendSignalToOutput(0, this.lastSignalType);
	}

	@Override
	public ComponentType getType()
	{
		return ComponentType.PULSE;
	}
}
