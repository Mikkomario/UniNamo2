package utopia_sound;

import java.util.LinkedList;

/**
 * WavSoundQueue is a soundqueue that plays wavsounds. The volume and pan of 
 * the individual sounds can be adjusted.
 *
 * @author Mikko Hilpinen.
 *         Created 7.9.2013.
 */
public class WavSoundQueue extends SoundQueue
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private LinkedList<WavSoundInformation> soundinformations;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new empty WavSoundQueue ready to play wavsounds
	 *
	 * @param autodeath Will the queue die after it has played all the sounds 
	 * in it
	 */
	public WavSoundQueue(boolean autodeath)
	{
		super(autodeath);
		
		// Initializes attributes
		this.soundinformations = new LinkedList<WavSoundInformation>();
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected void playSound(Sound sound)
	{
		// Only plays wavsounds
		if (!(sound instanceof WavSound))
		{
			System.err.println("Incorrect sound type (" + sound.getClass() + 
					") at WavSoundQueue");
			return;
		}
		// Plays a sound with the right volume and pan
		WavSoundInformation information = this.soundinformations.getFirst();
		((WavSound) sound).play(information.getVolume(), information.getPan(), 
				this);
		// Updates the sound information list
		this.soundinformations.removeFirst();
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Adds a new wavsound to the queue waiting for its turn to be played.
	 *
	 * @param sound The WavSound added to the queue
	 * @param volumeadjustment How much the volume of the sound is adjusted 
	 * from the default (in desibels)
	 * @param pan How much the sound is panned [-1, 1]
	 * @param playiffree Will the queue start if it isn't playing a sound 
	 * already
	 */
	public void addWavSound(WavSound sound, int volumeadjustment, float pan, 
			boolean playiffree)
	{
		// Adds the new information
		this.soundinformations.add(new WavSoundInformation(
				volumeadjustment, pan));
		// Adds the sound to the queue
		addSound(sound, playiffree);
	}

	
	// SUBCLASSES	-----------------------------------------------------
	
	private class WavSoundInformation
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private int volumechange;
		private float pan;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		public WavSoundInformation(int volume, float pan)
		{
			// Initializes attributes
			this.volumechange = volume;
			this.pan = pan;
		}
		
		
		// GETTERS & SETTERS	-----------------------------------------
		
		private int getVolume()
		{
			return this.volumechange;
		}
		
		private float getPan()
		{
			return this.pan;
		}
	}
}
