package utopia_resourcebanks;

import java.util.ArrayList;

/**
 * OpenWavSoundBankHolder holds a number of wavsoundbanks and provides them 
 * for other objects when necessary.
 *
 * @author Mikko Hilpinen.
 *         Created 7.9.2013.
 */
public class OpenWavSoundBankHolder extends OpenSoundBankHolder
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates and initializes new OpenWavSoundBankHolder. The content is loaded 
	 * using the given file.
	 *
	 * @param filename A file that shows information about what banks to create 
	 * (data/ automatically included). 
	 * The file should be written as follows:<p>
	 * 
	 * &bankname<br>
	 * soundname#filename(data/ automatically included)#volumeadjustment (float)#
	 * pan (float [-1, 1])<br>
	 * anothersoundname#...<br>
	 * ...<br>
	 * &anotherbankname<br>
	 * ...<br>
	 * * this is a comment
	 */
	public OpenWavSoundBankHolder(String filename)
	{
		super(filename, true);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected OpenBank createBank(ArrayList<String> commands)
	{
		// Creates and retuns a new openwavsoundbank
		return new OpenWavSoundBank(commands);
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Returns an wavsoundbank from the held banks.
	 *
	 * @param bankname The name of the soundbank
	 * @return The held soundbank or null if no such soundbank was found
	 */
	public OpenWavSoundBank getWavSoundBank(String bankname)
	{
		OpenBank maybewavsoundbank = getBank(bankname);
		
		// Tries to cast the bank to WavSoundBank
		if (maybewavsoundbank instanceof OpenWavSoundBank)
			return (OpenWavSoundBank) maybewavsoundbank;
		else
			return null;
	}
}
