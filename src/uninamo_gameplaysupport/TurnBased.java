package uninamo_gameplaysupport;

import genesis_event.Handled;
import genesis_util.StateOperator;

/**
 * Turn based instances react to time events that are produced at 
 * certain intervals.
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public interface TurnBased extends Handled
{
	/**
	 * This method is called each time a new turn starts
	 */
	public void onTurnEvent();
	
	/**
	 * @return The stateOperator that defines whether the object should be informed about the 
	 * turn events
	 */
	public StateOperator getListensToTurnEventsOperator();
}
