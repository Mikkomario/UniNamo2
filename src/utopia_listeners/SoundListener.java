package utopia_listeners;

import utopia_handleds.LogicalHandled;
import utopia_sound.Sound;

/**
 * Soundlistener reacts to a start and/or end of a sound playing
 *
 * @author Mikko Hilpinen.
 *         Created 19.8.2013.
 * @see utopia_sound.Sound
 */
public interface SoundListener extends LogicalHandled
{
	/**
	 * This method is called when a sound the listener listens to is played
	 * @param source the sound that just started
	 */
	public void onSoundStart(Sound source);
	
	/**
	 * This method is called when a sound the listener listens to ends
	 * @param source The sound that just ended
	 */
	public void onSoundEnd(Sound source);
}
