package utopia_sound;

import java.util.LinkedList;



import utopia_listeners.SoundListener;
import utopia_resourcebanks.BankObject;

/**
 * SoundQueues play a number of sounds in a succession, starting the 
 * next sound when the last one stops playing. the Sounds can be added to the 
 * queue easily even during playing.
 *
 * @author Mikko Hilpinen.
 *         Created 6.9.2013.
 */
public abstract class SoundQueue implements SoundListener, BankObject
{
	// ATTRIBUTES	------------------------------------------------------
	
	private LinkedList<Sound> sounds;
	private boolean dead, diesatend, playing;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new empty soundqueue ready to play sounds
	 * 
	 * @param autodeath Should the queue die when it has played all the 
	 * sounds in it
	 */
	public SoundQueue(boolean autodeath)
	{
		// Initializes attributes
		this.sounds = new LinkedList<Sound>();
		this.dead = false;
		this.diesatend = autodeath;
		this.playing = false;
	}
	
	
	// ABSTRACT METHODS	-------------------------------------------------
	
	/**
	 * here the subclass is supposed to play te given sound using the settings 
	 * it seems neccessary. The SoundQueue should be added as the specific 
	 * listener to the sound
	 *
	 * @param sound The sound that needs to be played
	 */
	protected abstract void playSound(Sound sound);
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public boolean isActive()
	{
		// The queue is active as long as it lives (it needs to react to the 
		// sound events even when inactivated)
		return isDead();
	}

	@Override
	public void activate()
	{
		// The queue is always active
	}

	@Override
	public void inactivate()
	{
		// The queue is always active
	}

	@Override
	public boolean isDead()
	{
		return this.dead;
	}

	@Override
	public void kill()
	{
		this.dead = true;
		
		// Also clears the sound list
		//this.sounds = null;
		this.sounds.clear();
	}

	@Override
	public void onSoundStart(Sound source)
	{
		// Does nothing
	}

	@Override
	public void onSoundEnd(Sound source)
	{
		// Removes the old sound from the queue
		if (this.sounds.size() > 0)
			this.sounds.removeFirst();
		
		// Checks if there are any more sounds to play
		if (this.sounds.size() == 0)
		{
			// Dies if autodeath is on
			if (this.diesatend)
				kill();
			
			this.playing = false;
			return;
		}
		
		// Plays the next sound (if still playing)
		if (this.playing)
			playSound(this.sounds.getFirst());
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Plays through the sounds once, removing them after playing them
	 */
	public void play()
	{
		// Plays through the sounds (if there are any and if not already playing)
		if (isDead() || this.playing || this.sounds.size() == 0)
			return;
		this.playing = true;
		playSound(this.sounds.getFirst());
	}
	
	/**
	 * Stops the current sound from playing. The queue can be restarted with 
	 * play method. If a permanent stop is needed, it is adviced to use the 
	 * kill method insted.
	 * 
	 * @see #play()
	 * @see #kill()
	 */
	public void stop()
	{
		this.playing = false;
		this.sounds.getFirst().stop();
	}
	
	/**
	 * Empties the queue without stopping any sounds.
	 */
	public void empty()
	{
		this.sounds.clear();
	}
	
	/**
	 * Adds a sound to the queue. May also start playing the sound if needed.
	 *
	 * @param sound The sound added to the queue of played sounds
	 * @param playiffree Should the sound be played if there isn't a sound 
	 * playing yet.
	 */
	protected void addSound(Sound sound, boolean playiffree)
	{
		// Checks the argument
		if (sound == null)
			return;
		
		// Adds the sound to the list
		this.sounds.add(sound);
		
		// Plays the sound if needed & not playing another sound
		if (playiffree && !this.playing)
		{
			playSound(this.sounds.getFirst());
			this.playing = true;
		}
	}
}
