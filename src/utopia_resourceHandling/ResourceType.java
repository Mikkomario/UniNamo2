package utopia_resourceHandling;

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
	
	
	// METHODS	----------------------------------------------------------
	
	/**
	 * Returns ResourceType based on the given string. The method ignores 
	 * casing.
	 * 
	 * @param typename The name of the resourceType
	 * @return The resourceType that corresponds with the given string
	 */
	public static ResourceType fromString(String typename)
	{
		for (ResourceType type : ResourceType.values())
		{
			if (typename.equalsIgnoreCase(type.toString()))
				return type;
		}
		
		System.err.println(typename + " isn't a resourceType");
		return null;
	}
}
