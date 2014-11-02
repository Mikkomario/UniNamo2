package uninamo_gameplaysupport;

import genesis_logic.Handled;

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
}
