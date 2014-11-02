package uninamo_gameplaysupport;

import genesis_logic.Handled;

/**
 * VictoryCondition objects must be satisfied before a stage can be cleared
 * 
 * @author Mikko Hilpinen
 * @since 11.3.2014
 */
public interface VictoryCondition extends Handled
{
	/**
	 * @return Is the victoryCondition satisfied.
	 */
	public boolean isClear();
}
