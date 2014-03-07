package utopia_resourcebanks;

import java.util.ArrayList;

/**
 * OpenSoundTrackBank is an open bank that contains soundtracks. The contents 
 * of the bank are initialized from a set of commands.
 * 
 * @author Mikko Hilpinen. 
 * Created 14.2.2014
 */
public class OpenSoundTrackBank extends SoundTrackBank implements OpenBank
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private ArrayList<String> creationcommands;
	private OpenSoundBankHolder soundbankholder;
		
		
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new SoundTackBank that will be initialized using the 
	 * given commands. Each command instance creates a single soundtrack to the 
	 * bank
	 *
	 * @param creationcommands The creationcommands contain the necessary 
	 * information for creating the tracks. The commands should have the 
	 * following syntax:<br>
	 * <i>soundname1,soundname2,soundname3,...#loopcount1,loopcount2,
	 * loopcount3,...#soundbankname#trackname</i><br>
	 * Command can be for example such line as "smooth,jazz#2,-1s#smoothjazz"
	 * @param soundbankholder The soundBankHolder that holds the soundBanks 
	 * that contain the sounds used in the tracks
	 */
	public OpenSoundTrackBank(ArrayList<String> creationcommands, 
			OpenSoundBankHolder soundbankholder)
	{
		// Initializes attributes
		this.creationcommands = creationcommands;
		this.soundbankholder = soundbankholder;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected void initialize()
	{
		// Creates the wavsounds using the commands
		// Goes through all the commands
		for (int i = 0; i < this.creationcommands.size(); i++)
		{
			String commandline = this.creationcommands.get(i);
			String[] commands = commandline.split("#");
			
			// Check that there's enough information
			if (commands.length < 4)
			{
				System.err.println("Command " + commandline + " doesn't " +
						"have enough arguments for creating a soundtrack!");
				continue;
			}
			
			String[] soundnames = commands[0].split(",");
			String[] loopsubcommands = commands[1].split(",");
			int[] loopcounts = new int[loopsubcommands.length];
			
			// Tries to convert the loopounts from strings to integers
			try
			{
				for (int a = 0; a < loopsubcommands.length; a++)
				{
					loopcounts[a] = Integer.parseInt(loopsubcommands[a]);
					//System.out.println("Loopcount " + a + ": " + loopcounts[a]);
				}
			}
			catch (NumberFormatException nfe)
			{
				System.err.println("Command " + commandline + " contains " +
						"invalid information! loopcounts need to be in " +
						"integer format.");
				nfe.printStackTrace();
			}
			
			// Creates the new soundtack and adds it to the bank
			createTrack(soundnames, loopcounts, 
					this.soundbankholder.getSoundBank(commands[2]), commands[3]);
		}
	}
}
