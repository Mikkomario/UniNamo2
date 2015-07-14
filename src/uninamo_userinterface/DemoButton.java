package uninamo_userinterface;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.VictoryCondition;

/**
 * DemoButton allows testing at the beginning of the stage but disappears as 
 * soon as coding area is initiated.
 * 
 * @author Mikko Hilpinen
 * @since 16.3.2014
 */
public class DemoButton extends TestButton implements VictoryCondition
{	
	// CONSTRUCTOR	-------------------------------------------------------
	
	/**
	 * Creates a new demoButton to the given position
	 * @param handlers The handlers that will handle the button
	 * @param position The button's new position
	 * @param testHandler The testHandler that will be informed about test 
	 * (= demo) events
	 */
	public DemoButton(HandlerRelay handlers, Vector3D position, TestHandler testHandler)
	{
		super(handlers, position, testHandler, "demo");
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public boolean isClear()
	{
		// The existence of demoButton makes winning the game impossible
		return false;
	}
}
