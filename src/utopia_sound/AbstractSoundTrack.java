package utopia_sound;

import utopia_listeners.SoundListener;

/**
 * Abstractsoundtrack provides the necessary methods for the soundtracks 
 * so they can easily play custom tracks.
 *
 * @author Mikko Hilpinen.
 *         Created 25.8.2013.
 */
public abstract class AbstractSoundTrack extends Sound implements SoundListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private int currentindex, currentloopcount;
	private Sound currentsound;
	private boolean paused, delayed, loops;
	private int releasespending;
	 // The index to which the next jump will lead. -1 if the track is 
	// supposed to traverse at default
	private int nextjumpindex;
	
	
	// CONSTRUCTROR	------------------------------------------------------
	
	/**
	 * Creates a new soundtrack with the given information
	 *
	 * @param name The name of the track
	 */
	public AbstractSoundTrack(String name)
	{
		super(name);
		
		// Initializes attributes
		this.currentindex = 0;
		this.currentloopcount = 0;
		this.currentsound = null;
		this.paused = false;
		this.delayed = false;
		this.loops = false;
		this.releasespending = 0;
		this.nextjumpindex = -1;
	}
	
	
	// ABSTRACT METHODS	-------------------------------------------------
	
	/**
	 * Plays a certain phase of the track. This means playing a certain sound 
	 * once and adding the track as the listener. A subclass may wish to 
	 * affect the playing of the sound as well and it should be done in this 
	 * method.
	 *
	 * @param index The index of the phase played
	 * @return The sound that was just played
	 * @see utopia_sound.Sound#play(SoundListener)
	 */
	protected abstract Sound playPhase(int index);
	
	/**
	 * Returns how many times the given phase should be repeated
	 *
	 * @param index The index of the phase that will be played
	 * @return How many times the phase should be repeated (a negative number 
	 * means that the phase will be repeated until the track is released, 
	 * though the sound will be played at least once)
	 */
	protected abstract int getLoopCount(int index);
	
	/**
	 * @return How many phases there are in the track
	 */
	protected abstract int getMaxPhase();
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public boolean isActive()
	{
		// The track is always active so it doesn't break (inactivating is done 
		// by pausing)
		return true;
	}
	
	@Override
	public void activate()
	{
		// The track is always active
	}
	
	@Override
	public void inactivate()
	{
		// The track is always active
	}
	
	@Override
	public void onSoundStart(Sound source)
	{
		// Does nothing
	}
	
	@Override
	public void onSoundEnd(Sound source)
	{
		// If the sound was stopped, doesn't do anything
		if (!isPlaying())
			return;
		// Plays the next sound (if not paused, in which case delays the sound)
		if (this.paused)
		{
			this.delayed = true;
			//System.out.println("Delays");
		}
		else
			playnextsound();
	}
	
	/**
	 * Stops the track from playing
	 */
	@Override
	protected void stopSound()
	{
		// Stops the current sound and the track
		this.delayed = false;
		this.paused = false;
		this.releasespending = 0;
		this.currentsound.stop();
	}
	
	/**
	 * Pauses the track. The track can be continued from the same spot with 
	 * unpause method
	 */
	@Override
	public void pause()
	{
		this.paused = true;
		this.currentsound.pause();
	}
	
	/**
	 * Unpauses the track from the last state
	 */
	@Override
	public void unpause()
	{
		// TODO: Unpausing doesn't always seem to work (when not delayed)
		this.paused = false;
		// Continues the track if it was delayed
		if (this.delayed)
			playnextsound();
		// Otherwise just continues the former sound
		else
			this.currentsound.unpause();
	}
	
	/**
	 * Plays through the track once
	 */
	@Override
	protected void playSound()
	{
		// Doesn't work if the track was killed
		if (isDead())
			return;
		
		// Updates information
		this.currentindex = 0;
		this.currentloopcount = getLoopCount(this.currentindex);
		this.paused = false;
		this.delayed = false;
		this.loops = false;
		this.releasespending = 0;
		
		// Plays the first sound
		this.currentsound = playPhase(this.currentindex);
	}
	
	/**
	 * Plays through the track repeatedly until stopped
	 */
	@Override
	protected void loopSound()
	{
		playSound();
		this.loops = true;
	}	
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Releases the track from the next infinite loop when it starts. 
	 * The releases stack so if you call the release multiple times it affects 
	 * multiple following loops.
	 */
	public void release()
	{
		this.releasespending++;
	}
	
	/**
	 * Negates the last release so that the track will not break out of the 
	 * next infinite loop until it is released again. If multiple releases 
	 * have been issued, only the latest is negated.
	 * 
	 * @see #release()
	 */
	public void unrelease()
	{
		this.releasespending--;
		
		if (this.releasespending < 0)
			this.releasespending = 0;
	}
	
	/**
	 * Orders the track to jump to the given index after the current sound 
	 * has been played. If another jump is set before the sound ends, the 
	 * previously issued jump will be skipped.
	 * 
	 * @param soundindex The index of the sound to which the track jumps to 
	 * (indexing starts from 0)
	 */
	public void setJumpToIndex(int soundindex)
	{
		this.nextjumpindex = soundindex % getMaxPhase();
	}
	
	private void playnextsound()
	{
		// Only plays the next sound if the track is still playing
		if (!isPlaying())
			return;
		
		// The sound is no longer delayed
		this.delayed = false;
		
		// Checks if the track should jump to a specific index
		if (this.nextjumpindex >= 0)
		{
			//System.out.println("Forced track jump");
			
			// Gathers information
			this.currentindex = this.nextjumpindex;
			this.currentloopcount = getLoopCount(this.currentindex);
					
			this.nextjumpindex = -1;
			
			// And plays the new sound
			this.currentsound = playPhase(this.currentindex);
		}
		// otherwise checks whether more loops are needed
		// Loops the current sound if needed
		else if (this.currentloopcount > 0 || 
				(this.currentloopcount < 0 && this.releasespending == 0))
		{
			this.currentloopcount --;
			this.currentsound = playPhase(this.currentindex);
		}
		// otherwise plays the next sound
		else
		{
			// If the track was released from an infinite loop, remembers it
			if (this.currentloopcount < 0)
				this.releasespending--;
			
			// Gathers the information
			this.currentindex ++;
			
			// If the end of the track was reached, either repeats or stops
			if (this.currentindex >= getMaxPhase())
			{
				if (this.loops)
					this.currentindex = 0;
				else
				{
					this.delayed = false;
					this.paused = false;
					this.releasespending = 0;
					informSoundEnd();
					return;
				}
			}
			// Updates loop count
			this.currentloopcount = getLoopCount(this.currentindex);
			
			// And plays the new sound
			this.currentsound = playPhase(this.currentindex);
		}
	}
}
