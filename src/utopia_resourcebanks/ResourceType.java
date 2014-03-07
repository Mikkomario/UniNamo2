package utopia_resourcebanks;

import utopia_graphic.Sprite;
import utopia_sound.MidiMusic;
import utopia_sound.SoundTrack;
import utopia_sound.WavSound;

/**
 * ResourceType shows the different types of resources the program supports
 * @author Mikko Hilpinen. 
 * Created 14.2.2014
 */
public enum ResourceType
{
	/**
	 * Sprite is an 2d (animated) image. Any .png or .jpg can be made into 
	 * a sprite.
	 * @see Sprite
	 */
	SPRITE, 
	/**
	 * Wav is a sound. A file that is in wav-format.
	 * @see WavSound
	 */
	WAV, 
	/**
	 * Midi is a midi sound. A file that is in midi-format.
	 * @see MidiMusic
	 */
	MIDI, 
	/**
	 * MidiSoundTrack is a track created by combining multiple midis together.
	 * @see SoundTrack
	 */
	MIDISOUNDTRACK, 
	/**
	 * WavSoundTrack is a track created by combining multiple wavs together.
	 * @see SoundTrack
	 */
	WAVSOUNDTRACK;
}
