package utopia_sound;

import utopia_resourcebanks.SoundBank;

/**
 * SoundTrack is a class that plays multiple sounds in order, forming a 
 * track.
 *
 * @author Mikko Hilpinen.
 *         Created 19.8.2013.
 */
public class SoundTrack extends AbstractSoundTrack
{
	// ATTRIBUTES	------------------------------------------------------
	
	private String[] soundnames;
	private int[] loopcounts;
	private SoundBank soundbank;
	
	
	// CONSTRUCTROR	------------------------------------------------------
	
	/**
	 * Creates a new soundtrack with the given information
	 *
	 * @param soundnames A table containing the names of the sounds that create 
	 * the track (in a specific order)
	 * @param loopcounts A table containing the numbers about how many times 
	 * each sound is repeated in a row (should have as many indexes as the 
	 * soundnames table) 
	 * (0 means that the music is played once, a negative number means that 
	 * the music is played until released but at least once)
	 * @param soundbank The soundbank that contains each of the sounds used 
	 * in the track
	 * @param name The name of the track
	 */
	public SoundTrack(String[] soundnames, int[] loopcounts, 
			SoundBank soundbank, String name)
	{
		super(name);
		
		// Initializes attributes
		this.soundbank = soundbank;
		this.soundnames = soundnames;
		this.loopcounts = loopcounts;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	protected Sound playPhase(int index)
	{
		// Plays a sound from the bank
		Sound newphase = this.soundbank.getSound(this.soundnames[index]);
		newphase.play(this);
		return newphase;
	}

	@Override
	protected int getLoopCount(int index)
	{
		// Returns a loopcount from the loopcounts table
		return this.loopcounts[index];
	}

	@Override
	protected int getMaxPhase()
	{
		// The length of the track is limited by the length of the soundnames 
		// and loopcounts tables
		return Math.min(this.soundnames.length, this.loopcounts.length);
	}
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * Changes the soundbank used in the track. This can be done in the midle 
	 * of playing a track and the change will take place once the current sound 
	 * stops
	 *
	 * @param bank The new soundbank to be used
	 */
	public void setSoundBank(SoundBank bank)
	{
		this.soundbank = bank;
	}
	
	/**
	 * Changes the sounds used in the track. This can be done in the midle 
	 * of playing a track and the change will take place once the current sound 
	 * stops
	 *
	 * @param soundnames The new sound names to be used
	 */
	public void setSoundNames(String[] soundnames)
	{
		this.soundnames = soundnames;
	}
	
	/**
	 * Changes the loopcounts used in the track. This can be done in the midle 
	 * of playing a track and the change will take place once the current sound 
	 * stops.
	 * (0 means that the music is played once, a negative number means that 
	 * the music is played until released but at least once)
	 *
	 * @param loopcounts The set of loopcounts to be used
	 */
	public void setLoopCounts(int[] loopcounts)
	{
		this.loopcounts = loopcounts;
	}
}
