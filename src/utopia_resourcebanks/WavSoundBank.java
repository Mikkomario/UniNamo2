package utopia_resourcebanks;

import utopia_sound.WavSound;

/**
 * A wavsoundbank holds numerous wavsounds and gives them for the other objects 
 * to use
 *
 * @author Mikko Hilpinen.
 *         Created 17.8.2013.
 */
public abstract class WavSoundBank extends SoundBank
{
	// IMPLEMENTED METHODS	--------------------------------------------
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return WavSound.class;
	}
	
	
	// OTHER METHODS ---------------------------------------------------

	/**
	 * Creates and puts a sound to the bank
	 * 
	 * @param filename The name of the wav-file (data/ included automatically)
	 * @param soundname The name of the sound in the bank
	 * @param defvolume How many desibels the volume is adjusted by default
	 * @param defpan How much the sound is panned by default [-1 (left speaker 
	 * only, 1 (right speaker only)]
	 */
	protected void createSound(String filename, String soundname, float defvolume, 
			float defpan)
	{
		WavSound newsound = new WavSound(filename, soundname, defvolume, defpan);
		addObject(newsound, soundname);
	}

	/**
	 * Returns a sound from the bank.
	 * 
	 * @param soundname The name of the sound in the bank
	 * @return The sound with the given name or null if no such sound was found
	 */
	@Override
	public WavSound getSound(String soundname)
	{
		return (WavSound) getObject(soundname);
	}
}
