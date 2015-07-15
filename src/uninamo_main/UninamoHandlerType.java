package uninamo_main;

import uninamo_components.CableConnector;
import uninamo_components.NormalComponent;
import uninamo_gameplaysupport.TestListener;
import uninamo_gameplaysupport.TurnBased;
import uninamo_gameplaysupport.VictoryCondition;
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
	TURN,
	/**
	 * The connector relay keeps track of all the connectors
	 */
	CONNECTOR,
	/**
	 * NormalComponentRelay keeps track of all the created components
	 */
	NORMALCOMPONENT;
	
	
	// IMPLEMENTED METHODS	------------------------

	@Override
	public Class<?> getSupportedHandledClass()
	{
		switch (this)
		{
			case VICTORY: return VictoryCondition.class;
			case TEST: return TestListener.class;
			case TURN: return TurnBased.class;
			case CONNECTOR: return CableConnector.class;
			case NORMALCOMPONENT: return NormalComponent.class;
		}
		
		return null;
	}
}
