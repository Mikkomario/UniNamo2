package utopia_resourcebanks;

import java.io.FileNotFoundException;

import utopia_sound.MidiMusic;

/**
 * Midimusicbank contains a group of midimusics and provides them for the 
 * objects that need them
 * 
 * @author Unto Solala & Mikko Hilpinen. Created 10.7.2013
 */
public abstract class MidiMusicBank extends SoundBank
{
	// ABSTRACT METHODS -----------------------------------------------------
	
	/**
	 * Creates Midis with the createMidi()-method.
	 * 
	 * @throws FileNotFoundException if all of the midis couldn't be loaded.
	 */
	public abstract void createMidis() throws FileNotFoundException;

	
	// IMPLEMENTED METHODS ---------------------------------------------------

	@Override
	protected void initialize()
	{
		// Creates the midis
		try
		{
			createMidis();
		}
		catch (FileNotFoundException fnfe)
		{
			// TODO: For some reason this doesn't seem to catch all the 
			// filenotfound exceptions
			
			System.err.println("Could not load all of the Midis!");
			fnfe.printStackTrace();
		}
	}
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return MidiMusic.class;
	}
	
	
	// OTHER METHODS	--------------------------------------------------

	/**
	 * Creates a midi and stores it in the bank
	 * 
	 * @param fileName	File's name and location (data/ is added by default)
	 * @param midiName	Name of the new midi in the bank.
	 */
	protected void createMidiMusic(String fileName, String midiName)
	{
		MidiMusic newMidi = new MidiMusic(fileName, midiName);
		addObject(newMidi, midiName);
	}

	/**
	 * Returns a midi from the MidiMusicBank.
	 * 
	 * @param midiName	Name of the wanted midi
	 * @return Returns the wanted midi if it is in the database, otherwise 
	 * returns null.
	 */
	@Override
	public MidiMusic getSound(String midiName)
	{
		return (MidiMusic) getObject(midiName);
	}
}
