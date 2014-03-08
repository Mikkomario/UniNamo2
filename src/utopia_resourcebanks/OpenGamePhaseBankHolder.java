package utopia_resourcebanks;

import java.util.ArrayList;

/**
 * OpenGamePhaseBankHolder is an OpenBankHolder that creates and holds 
 * GamePhaseBanks and offers them for other objects
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public class OpenGamePhaseBankHolder extends OpenBankHolder
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new OpenGamePhaseBankHolder and creates the GamePhases from 
	 * a file.
	 * 
	 * @param filename The file used to create the GamePhases (data/ is 
	 * automatically included). The file should have the following syntax:<p>
	 * 
	 * &bankname1<br>
	 * phasename1#resourcetypename1:bankname1,bankname2,bankname3, ... #resourcetypename2: ... # ...<br>
	 * phasename2# ...<br>
	 * ...<p>
	 * 
	 * &bankname2<br>
	 * ...<p>
	 * 
	 * ...<br>
	 * * This is a comment
	 */
	public OpenGamePhaseBankHolder(String filename)
	{
		super(filename, true);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	protected OpenBank createBank(ArrayList<String> commands)
	{
		return new OpenGamePhaseBank(commands);
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Returns a GamePhasebank with the given name or null if no such bank 
	 * exists.
	 * 
	 * @param bankName	The name of the bank to be returned.
	 * @return A GamePhaseBank with the given name or null if no such bank 
	 * exists in the holder.
	 */
	public OpenGamePhaseBank getGamePhaseBank(String bankName)
	{
		OpenBank maybeGamePhaseBank = getBank(bankName);
		
		if(maybeGamePhaseBank instanceof OpenGamePhaseBank)
			return (OpenGamePhaseBank) maybeGamePhaseBank;
		else
			return null;
	}
}
