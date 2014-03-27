package uninamo_components;

import uninamo_gameplaysupport.TestHandler;
import uninamo_userinterface.CurrentCostDrawer;
import utopia_worlds.Area;

/**
 * OrComponent is a logical unit that sends true signal if either of its inputs 
 * receive true signal
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class OrComponent extends NormalComponent
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private boolean signalStatus;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new OrComponent to the given position.
	 * 
	 * @param area The area where the object will reside at
	 * @param x The x-coordinate of the component (pixels)
	 * @param y The y-coordinate of the component (pixels)
	 * @param testHandler The testHandler that will inform the object about test events
	 * @param connectorRelay A connectorRelay that will keep track of the 
	 * connectors
	 * @param componentRelay The componentRelay that will keep track of the 
	 * component (optional)
	 * @param costDrawer The currentCostDrawer that will be affected by the 
	 * component (optional)
	 * @param isForTesting If this is true, the component will go to test mode 
	 * where it won't react to mouse but will create test cables to its connectors
	 */
	public OrComponent(Area area, int x, int y, TestHandler testHandler, 
			ConnectorRelay connectorRelay, NormalComponentRelay componentRelay, 
			CurrentCostDrawer costDrawer, boolean isForTesting)
	{
		super(area, x, y, testHandler, 
				connectorRelay, componentRelay, costDrawer, "test", 2, 1, true, 
				isForTesting);
		
		// Initializes attributes
		this.signalStatus = false;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onSignalChange(boolean newSignalStatus, SignalSender source)
	{
		//System.out.println("Or signal changes");
		
		// Informs the new signal to the output connector
		this.signalStatus = (getInputStatus(0) || getInputStatus(1));
		sendSignalToOutput(0, this.signalStatus);
		//System.out.println("Now sends signal: " + this.signalStatus);
	}

	@Override
	public boolean getSignalStatus()
	{
		return this.signalStatus;
	}

	@Override
	public ComponentType getType()
	{
		return ComponentType.OR;
	}
}
