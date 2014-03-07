package utopia_resourcebanks;

import java.util.ArrayList;

/**
 * OpenWavSoundBank initializes the sounds using a list of commands in string 
 * format. The bank then provides these sounds for the other classes to use
 *
 * @author Mikko Hilpinen.
 *         Created 7.9.2013.
 */
public class OpenWavSoundBank extends WavSoundBank implements OpenBank
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private ArrayList<String> creationcommands;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new OpenWavSoundBank that will be initialized using the 
	 * given commands. Each command instance creates a single wavsound to the 
	 * bank
	 *
	 * @param creationcommands The creationcommands contain the necessary 
	 * information for creating a wavsound. The commands should have the 
	 * following syntax:<br>
	 * <i>soundname#filename (data/ automatically included)
	 * #volumeadjustment#panning</i><br>
	 * Command can be for example such line as "car#sounds/car.wav#3.0#-0.4"
	 */
	public OpenWavSoundBank(ArrayList<String> creationcommands)
	{
		// Initializes attributes
		this.creationcommands = creationcommands;
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
						"have enough arguments for creating a wavsound!");
				continue;
			}
			
			// Tries to change the volume and pan from strings to floats
			float volume = 0;
			float pan = 0;
			
			try
			{
				volume = Float.parseFloat(commands[2]);
				pan = Float.parseFloat(commands[3]);
			}
			catch (NumberFormatException nfe)
			{
				System.err.println("Command " + commandline + " contains " +
						"invalid information! Volume and pan need to be in " +
						"float format.");
				nfe.printStackTrace();
			}
			
			// Creates the new wavsound and adds it to the bank
			createSound(commands[1], commands[0], volume, pan);
		}
	}
}
