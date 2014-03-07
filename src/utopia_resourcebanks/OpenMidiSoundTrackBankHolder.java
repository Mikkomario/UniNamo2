package utopia_resourcebanks;

/**
 * OpenMidiSoundTrackBankHolder is an OpenSoundTrackBankHolder that holds 
 * only tracks created by fusing MidiMusics together
 * 
 * @author Mikko Hilpinen 
 * Created 14.2.2014
 */
public class OpenMidiSoundTrackBankHolder extends OpenSoundTrackBankHolder
{
	// CONSTRUCTOR	------------------------------------------------------
	
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
	public OpenMidiSoundTrackBankHolder(String filename,
			OpenMidiMusicBankHolder soundbankholder)
	{
		super(filename, soundbankholder);
	}
}
