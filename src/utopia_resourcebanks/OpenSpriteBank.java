package utopia_resourcebanks;

import java.io.FileNotFoundException;
import java.util.ArrayList;



/**
 * This SpriteBank, unlike other SpriteBanks can be put into a bank. Also, 
 * the content of the SpriteBank can be defined by providing a number of 
 * commands upon the creation of the bank.
 *
 * @author Mikko Hilpinen.
 *         Created 26.8.2013.
 */
public class OpenSpriteBank extends SpriteBank implements OpenBank
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private ArrayList<String> commands;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new OpenSpriteBank that will initialize itself using the 
	 * given information
	 *
	 * @param creationcommands Creation commands should follow the following 
	 * style:<br>
	 * spritename#filename <i>(data/ is automatically included)</i>#image 
	 * number#originx#originy
	 */
	public OpenSpriteBank(ArrayList<String> creationcommands)
	{
		// Initializes attributes
		this.commands = creationcommands;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public void createSprites() throws FileNotFoundException
	{
		// Creates the sprites by going through the commands
		for (int i = 0; i < this.commands.size(); i++)
		{
			String command = this.commands.get(i);
			String[] parts = command.split("#");
			
			// Checks that there are enough arguments
			if (parts.length < 5)
			{
				System.err.println("Couldn't load a sprite. Line " + command + 
						"doensn't have enough arguments");
				continue;
			}
			
			int imgnumber = 0;
			int originx = 0;
			int originy = 0;
			
			try
			{
				imgnumber = Integer.parseInt(parts[2]);
				originx = Integer.parseInt(parts[3]);
				originy = Integer.parseInt(parts[4]);
			}
			catch(NumberFormatException nfe)
			{
				System.err.println("Couldn't load a sprite. Line " + command 
						+ " contained invalid information.");
				continue;
			}
			
			createSprite(parts[1], imgnumber, originx, originy, parts[0]);
		}
	}
}
