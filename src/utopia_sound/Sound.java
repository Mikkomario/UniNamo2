package utopia_sound;

import utopia_handlers.SoundListenerHandler;
import utopia_listeners.SoundListener;
import utopia_resourcebanks.BankObject;

/**
 * Sound is a sound or a music that can be played during the game. Each playable 
 * piece should extend this class. This class handles the listener informing and 
 * sets a standard for the subclasses.
 *
 * @author Mikko Hilpinen.
 *         Created 19.8.2013.
 */
public abstract class Sound implements BankObject
{
	// ATTRIBUTES	------------------------------------------------------
	
	private SoundListener specificlistener;
	private SoundListenerHandler listenerhandler;
	private String name;
	private boolean dead, playing;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new sound with the given name
	 *
	 * @param name The name of the sound (in the bank)
	 */
	public Sound(String name)
	{
		// Initializes attributes
		this.name = name;
		this.listenerhandler = new SoundListenerHandler(false, null);
		this.specificlistener = null;
		this.dead = false;
		this.playing = false;
	}
	
	
	// ABSTRACT METHODS	--------------------------------------------------
	
	/**
	 * Plays the sound. This is meant for class-subclass interaction only and 
	 * the user should use the play() method instead.
	 * @see Sound#play(SoundListener)
	 */
	protected abstract void playSound();
	
	/**
	 * Loops the sound until it is stopped. This is meant for class-subclass 
	 * interaction only and the user should use the loop() method instead.
	 * @see Sound#loop(SoundListener)
	 */
	protected abstract void loopSound();
	
	/**
	 * Stops the sound from playing. This is meant for class-subclass 
	 * interaction only and the user should use the stop() method instead.
	 * @see Sound#stop()
	 */
	protected abstract void stopSound();
	
	/**
	 * Pauses the sound from playing
	 */
	public abstract void pause();
	
	/**
	 * Continues the sound from the spot it was paused at
	 */
	public abstract void unpause();
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void kill()
	{
		// Stops the sound and empties the handler
		stop();
		this.specificlistener = null;
		this.listenerhandler.killWithoutKillingHandleds();
		
		this.dead = true;
	}
	
	@Override
	public boolean isDead()
	{
		return this.dead;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Plays through the sound once. Informs the listener about the start 
	 * and end of the sound. Informs the given specificlistener just about this 
	 * playthrough of the sound.
	 *
	 * @param specificlistener A specific listener that will be informed about 
	 * the events caused by this play of the sound only (null if not needed)
	 */
	public void play(SoundListener specificlistener)
	{	
		// Only plays sounds if alive
		if (this.dead)
			return;
		
		// If the sound was already playing, stops the former one
		if (this.playing)
			stop();
		
		// Informs the listeners about the event
		this.specificlistener = specificlistener;
		if (this.specificlistener != null)
			this.specificlistener.onSoundStart(this);
		this.listenerhandler.onSoundStart(this);
		
		this.playing = true;
		// Plays the sound
		playSound();
	}
	
	/**
	 * Loops the sound continuously until stopped
	 *
	 * @param specificlistener a listener that will be informed specifically 
	 * about the events caused by this play of the sound (null if not needed)
	 */
	public void loop(SoundListener specificlistener)
	{
		// Only plays sounds if alive
		if (this.dead)
			return;
		
		// If the sound was already playing, stops the former one
		if (this.playing)
			stop();
		
		// Informs the listeners about the event
		this.specificlistener = specificlistener;
		if (this.specificlistener != null)
			this.specificlistener.onSoundStart(this);
		this.listenerhandler.onSoundStart(this);
		
		this.playing = true;
		// Plays the sound
		loopSound();
	}
	
	/**
	 * This method stops the sound from playing and informs the listeners about 
	 * the end of the sound
	 */
	public void stop()
	{
		// Only stops sounds if alive and playing
		if (this.dead || !this.playing)
			return;
		
		this.playing = false;
		// Stops the sound
		stopSound();
		// Informs the listeners about the event
		if (this.specificlistener != null)
			this.specificlistener.onSoundEnd(this);
		this.listenerhandler.onSoundEnd(this);
	}
	
	/**
	 * @return Is the sound currently playing or paused (true) or stopped (false)
	 */
	public boolean isPlaying()
	{
		return this.playing;
	}
	
	/**
	 * Adds a soundlistener to the listeners that are informed when the sound 
	 * starts or ends
	 *
	 * @param s The soundlistener to be informed
	 */
	public void addListener(SoundListener s)
	{
		this.listenerhandler.addListener(s);
	}
	
	/**
	 * Removes a soundlistener from the informed listeners
	 *
	 * @param s The soundlistener to be removed
	 */
	public void removeListener(SoundListener s)
	{
		this.listenerhandler.removeHandled(s);
	}
	
	/**
	 * @return The name of the sound to differentiate it from other sounds
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Subclasses should call this method when a sound ends naturally but not 
	 * when stopSound method is called.
	 */
	protected void informSoundEnd()
	{
		// Updates the status
		this.playing = false;
		
		// Informs the listeners
		if (this.specificlistener != null)
			this.specificlistener.onSoundEnd(this);
		this.listenerhandler.onSoundEnd(this);
	}
	
	/**
	 * Subclasses should call this method when a sound starts outside the 
	 * playsound method
	 * @param specificlistener A listener that will be informed about events 
	 * during this one sound
	 */
	protected void informSoundStart(SoundListener specificlistener)
	{
		// Updates the status
		this.playing = true;
		this.specificlistener = specificlistener;
		
		// Informs the listeners
		if (this.specificlistener != null)
			this.specificlistener.onSoundStart(this);
		this.listenerhandler.onSoundStart(this);
	}
}

