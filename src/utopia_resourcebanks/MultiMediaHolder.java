package utopia_resourcebanks;

import java.util.HashMap;

import utopia_resourceHandling.ResourceType;

/**
 * Multimediaholder keeps track all kinds of resources 
 * and provides them for other objects. The resources can be 
 * activated and deactivated at-will. The class is wholly static since no 
 * copies of the resources should be made and they should be accessed from 
 * anywhere.<br>
 * Please note that different resourceType databases have to be initialized 
 * before they can be used.
 * 
 * @author Mikko Hilpinen. 
 * Created 14.2.2014
 * @see #initializeResourceDatabase(ResourceType, String)
 */
public class MultiMediaHolder
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private static HashMap<ResourceType, HashMap<String, OpenBank>> activebanks 
			= new HashMap<ResourceType, HashMap<String, OpenBank>>();
	private static HashMap<ResourceType, OpenBankHolder> bankholders = 
			new HashMap<ResourceType, OpenBankHolder>();
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	private MultiMediaHolder()
	{
		// Constructor hidden from other classes since only static 
		// interfaces are allowed
	}

	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Initializes the database of the given type. The necessary information is 
	 * read from a file.
	 * 
	 * @param type The type of the resource the database holds
	 * @param filename The name of the file that holds the information necessary 
	 * for creating the database (data/ included automatically). 
	 * The file should be written as follows:
	 * <p>
	 * &bankname<br>
	 * ...<br>
	 * ...<br>
	 * &anotherbankname<br>
	 * ...<br>
	 * * this is a comment
	 * @see OpenBankHolder#OpenBankHolder(String, boolean)
	 */
	public static void initializeResourceDatabase(ResourceType type, String filename)
	{
		if (bankholders.containsKey(type))
		{
			System.err.println("The resource database with type " + type + 
					" has already been initialized");
			return;
		}
		
		// Creates the bankHolder
		switch (type)
		{
			case SPRITE: bankholders.put(type, 
					new OpenSpriteBankHolder(filename)); break;
			case WAV: bankholders.put(type, 
					new OpenWavSoundBankHolder(filename)); break;
			case MIDI: bankholders.put(type, 
					new OpenMidiMusicBankHolder(filename)); break;
			case MIDISOUNDTRACK:
			{
				if (!bankholders.containsKey(ResourceType.MIDI))
				{
					System.err.println("Couldn't initialize the MidiSoundTrack "
							+ "database because the MidiDatabase hasn't been "
							+ "initialized yet");
					return;
				}
				
				bankholders.put(type, 
						new OpenMidiSoundTrackBankHolder(filename, 
								(OpenMidiMusicBankHolder) bankholders.get(
								ResourceType.MIDI))); break;
			}
			case WAVSOUNDTRACK:
			{
				if (!bankholders.containsKey(ResourceType.WAV))
				{
					System.err.println("Couldn't initialize the WavSoundTrack "
							+ "database because the WavDatabase hasn't been "
							+ "initialized yet");
					return;
				}
				
				bankholders.put(type, 
						new OpenWavSoundTrackBankHolder(filename, 
								(OpenWavSoundBankHolder) bankholders.get(
								ResourceType.WAV))); break;
			}
			default:
			{
				System.err.println(type + " is not supported by "
						+ "MultiMediaHolder. Please update the latter.");
				return;
			}
		}
		
		// Initializes the map
		activebanks.put(type, new HashMap<String, OpenBank>());
	}
	
	/**
	 * Returns a spritebank from the activated spritebanks
	 *
	 * @param spritebankname The name of the spritebank
	 * @return The active spritebank with the given name
	 */
	public static SpriteBank getSpriteBank(String spritebankname)
	{
		OpenBank maybespritebank = getBank(ResourceType.SPRITE, spritebankname);
		
		if (maybespritebank instanceof SpriteBank)
			return (SpriteBank) maybespritebank;
		else
			return null;
	}
	
	/**
	 * Returns an wavsoundbank if it has been initialized yet
	 *
	 * @param wavbankname The name of the needed wavbank
	 * @return The wavbank with the given name or null if no such bank exists 
	 * or if the bank is not active
	 */
	public static WavSoundBank getWavBank(String wavbankname)
	{
		OpenBank maybewavbank = getBank(ResourceType.WAV, wavbankname);
		
		if (maybewavbank instanceof WavSoundBank)
			return (WavSoundBank) maybewavbank;
		else
			return null;
	}
	
	/**
	 * Returns an midiMusicBank if it has been initialized
	 *
	 * @param midibankname The name of the needed midiBank
	 * @return The midiBank with the given name or null if no such bank exists 
	 * or if the bank is not active
	 */
	public static MidiMusicBank getMidiBank(String midibankname)
	{
		OpenBank maybemidibank = getBank(ResourceType.MIDI, midibankname);
		
		if (maybemidibank instanceof MidiMusicBank)
			return (MidiMusicBank) maybemidibank;
		else
			return null;
	}
	
	/**
	 * Returns an SoundTrackBank if it has been initialized
	 *
	 * @param trackbankname The name of the needed bank
	 * @return The SoundTrackBank with the given name or null if no such bank exists 
	 * or if the bank is not active
	 */
	public static SoundTrackBank getMidiTrackBank(String trackbankname)
	{
		OpenBank maybetrackbank = getBank(ResourceType.MIDISOUNDTRACK, 
				trackbankname);
		
		if (maybetrackbank instanceof SoundTrackBank)
			return (SoundTrackBank) maybetrackbank;
		else
			return null;
	}
	
	/**
	 * Returns an SoundTrackBank if it has been initialized
	 *
	 * @param trackbankname The name of the needed bank
	 * @return The SoundTrackBank with the given name or null if no such bank exists 
	 * or if the bank is not active
	 */
	public static SoundTrackBank getWavTrackBank(String trackbankname)
	{
		OpenBank maybetrackbank = getBank(ResourceType.WAVSOUNDTRACK, 
				trackbankname);
		
		if (maybetrackbank instanceof SoundTrackBank)
			return (SoundTrackBank) maybetrackbank;
		else
			return null;
	}
	
	/**
	 * Activates the bank with the given resourceType and name. The bank 
	 * can be then found by calling the getBank() -methods. The bank remains 
	 * active until deactivateBank() is called for it
	 * 
	 * @param type The resource type of the bank
	 * @param bankname The name of the resource bank
	 * @param preinitialize Should all the resources in the bank be initialized 
	 * right away (true) or at the first time the bank is used (false)
	 * @see #deactivateBank(ResourceType, String)
	 */
	public static void activateBank(ResourceType type, String bankname, 
			boolean preinitialize)
	{
		if (!activebanks.containsKey(type))
		{
			System.err.println(type + 
					" database hasn't been initialized and can't be used yet");
			return;
		}
		
		// If the bank was already activated, does nothing
		if (activebanks.get(type).containsKey(bankname))
			return;
		
		OpenBank newbank = bankholders.get(type).getBank(bankname);
		if (preinitialize)
			newbank.initializeBank();
		activebanks.get(type).put(bankname, newbank);
		
		if (newbank == null)
			return;
	}
	
	/**
	 * Deactivates a bank with the given resource type and name. The bank can 
	 * be later reactivated using the activateBank method.
	 * 
	 * @param type The resource type of the bank
	 * @param bankname The name of the bank
	 * @see #activateBank(ResourceType, String, boolean)
	 */
	public static void deactivateBank(ResourceType type, String bankname)
	{
		// If the bank isn't active or if the resource type hasn't been 
		// initialized, does nothing
		if (!activebanks.containsKey(type) || 
				!activebanks.get(type).containsKey(bankname))
			return;
		
		// Uninitializes the bank and then removes it from the active banks
		activebanks.get(type).get(bankname).uninitialize();
		activebanks.get(type).remove(bankname);
	}
	
	private static OpenBank getBank(ResourceType type, String bankname)
	{
		if (!activebanks.containsKey(type))
		{
			System.err.println(type + " database has not been initialized yet!");
			return null;
		}
		
		if (!activebanks.get(type).containsKey(bankname))
		{
			System.err.println(type + "bank named " + bankname + 
					" is not active!");
			return null;
		}
		
		return activebanks.get(type).get(bankname);
	}
}
