package utopia_resourcebanks;

import java.util.ArrayList;

/**
 * The holder holds multiple soundtrackBanks and offers them for other objects. 
 * The banks are initialized using a specific file.
 * 
 * @author Mikko Hilpinen. 
 * Created 14.2.2014
 */
public abstract class OpenSoundTrackBankHolder extends OpenSoundBankHolder
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private OpenSoundBankHolder soundbankholder;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates and initializes new OpenSoundTrackBankHolder. The content is loaded 
	 * using the given file.
	 *
	 * @param filename A file that shows information about what banks to create 
	 * (data/ automatically included). 
	 * The file should be written as follows:<p>
	 * 
	 * &bankname<br>
	 * soundname1,soundname2,soundname3,...#loopcount1,loopcoun2,loopcount3,...
	 * #soundbankname#trackname<br>
	 * anothersoundnames#anotherloopcounts#soundbankname#anothertrackname<br>
	 * ...<br>
	 * &anotherbankname<br>
	 * ...<br>
	 * * this is a comment
	 * @param soundbankholder The soundBankHolder that holds the sounds used 
	 * in the tracks
	 */
	public OpenSoundTrackBankHolder(String filename, 
			OpenSoundBankHolder soundbankholder)
	{
		super(filename, false);
		
		// Initializes attributes
		this.soundbankholder = soundbankholder;
		
		// Initializes the bank
		initialize();
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected OpenBank createBank(ArrayList<String> commands)
	{
		return new OpenSoundTrackBank(commands, this.soundbankholder);
	}

	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Returns a soundTrackBank with the given name if it's held in the holder.
	 * 
	 * @param bankname The name of the bank to be returned
	 * @return a SoundTrackBank with the given name or null if no such bank 
	 * could be found
	 */
	public OpenSoundTrackBank getSoundTrackBank(String bankname)
	{
		OpenBank maybeOpenTrackBank = getBank(bankname);
		
		if(maybeOpenTrackBank instanceof OpenSoundTrackBank)
			return (OpenSoundTrackBank) maybeOpenTrackBank;
		else
			return null;
	}
}
