package utopia_resourcebanks;

/**
 * OpenSoundBankHolder is an abstract OpenBank that contains only sounds
 * 
 * @author Mikko Hilpinen. 
 * Created 14.2.2014
 */
public abstract class OpenSoundBankHolder extends OpenBankHolder
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates and initializes new OpenSoundBankHolder. The content is loaded 
	 * using the given file.
	 *
	 * @param filename A file that shows information about what banks to create 
	 * (data/ automatically included). 
	 * The file should be written as follows:<p>
	 * 
	 * &bankname<br>
	 * ...<br>
	 * ...<br>
	 * &anotherbankname<br>
	 * ...<br>
	 * * this is a comment
	 * @param autoinitialize Should the holder be initialized right in the 
	 * constructor (true) or manually later (false)
	 */
	public OpenSoundBankHolder(String filename, boolean autoinitialize)
	{
		super(filename, autoinitialize);
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Returns an soundbank from the held banks.
	 *
	 * @param bankname The name of the soundbank
	 * @return a soundbank with the given name or null if no such 
	 * soundbank was found
	 */
	public SoundBank getSoundBank(String bankname)
	{
		OpenBank maybesoundbank = getBank(bankname);
		
		// Tries to cast the bank to WavSoundBank
		if (maybesoundbank instanceof SoundBank)
			return (SoundBank) maybesoundbank;
		else
			return null;
	}
}
