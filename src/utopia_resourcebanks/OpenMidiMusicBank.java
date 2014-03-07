package utopia_resourcebanks;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * OpenMidiMusicBank holds a number of midis that are initialized using a 
 * set of commands.
 * 
 * @author Mikko Hilpinen. 
 * Created 10.2.2014
 */
public class OpenMidiMusicBank extends MidiMusicBank implements OpenBank
{
	// ATTRIBUTES	------------------------------------------------------
	
	private ArrayList<String> commands;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new midiMusicBank by providing it a set of commands used to 
	 * initialize the bank.
	 * 
	 * @param creationcommands The creation commands that show which midis 
	 * should be loaded and from where.<br>
	 * Each of the commands should have the following syntax:<br>
	 * MusicName#filename (data/ automatically included)
	 */
	public OpenMidiMusicBank(ArrayList<String> creationcommands)
	{
		// Initializes attributes
		this.commands = creationcommands;
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public void createMidis() throws FileNotFoundException
	{
		// TODO Consider finding a way to avoid copy-paste between the different 
		// openBanks

		// Goes through all the commands
		for (int i = 0; i < this.commands.size(); i++)
		{
			String commandline = this.commands.get(i);
			String[] commands = commandline.split("#");
			
			// Check that there's enough information
			if (commands.length < 2)
			{
				System.err.println("Command " + commandline + " doesn't " +
						"have enough arguments for creating a MidiMusic!");
				continue;
			}
			
			// Creates the new wavsound and adds it to the bank
			createMidiMusic(commands[1], commands[0]);
		}
	}

}
