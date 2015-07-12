package uninamo_gameplaysupport;

import genesis_event.HandlerType;

/**
 * These are the handler types introduced in this project
 * @author Mikko Hilpinen
 * @since 12.7.2015
 */
public enum UninamoHandlerType implements HandlerType
{
	/**
	 * The victory handler keeps track of the different victory conditions
	 */
	VICTORY,
	/**
	 * The test handler informs objects about test events
	 */
	TEST,
	/**
	 * The turn handler informs objects about turn events
	 */
	TURN;
	
	
	// IMPLEMENTED METHODS	------------------------

	@Override
	public Class<?> getSupportedHandledClass()
	{
		switch (this)
		{
			case VICTORY: return VictoryCondition.class;
			case TEST: return TestListener.class;
			case TURN: return TurnBased.class;
		}
		
		return null;
	}
}
