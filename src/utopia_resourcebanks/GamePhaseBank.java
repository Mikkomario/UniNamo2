package utopia_resourcebanks;

import utopia_resourceHandling.GamePhase;

/**
 * GamePhaseBank is a bank that contains a number of gamePhases. The bank 
 * offers these phases for other objects to use.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public abstract class GamePhaseBank extends AbstractBank
{	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return GamePhase.class;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Finds and returns a gamephase from the bank
	 * 
	 * @param phasename The name of the phase that is looked for
	 * @return A GamePhase with the given name or null if no such phase 
	 * was found
	 */
	public GamePhase getPhase(String phasename)
	{
		return (GamePhase) getObject(phasename);
	}
}
