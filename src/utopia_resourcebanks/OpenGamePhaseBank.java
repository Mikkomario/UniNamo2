package utopia_resourcebanks;

import java.util.ArrayList;
import java.util.HashMap;

import utopia_resourceHandling.GamePhase;
import utopia_resourceHandling.ResourceType;

/**
 * OpenGamePhaseBank is a gamePhaseBank that creates its contents using a list 
 * of commands.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public class OpenGamePhaseBank extends GamePhaseBank implements OpenBank
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private ArrayList<String> creationCommands;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new GamePhaseBank.
	 * @param creationCommands A list of commands the bank will use to 
	 * initialize itself. The commands should follow the following 
	 * style:<br>
	 * phasename#resourcetypename1:bankname1,bankname2,bankname3, ... #resourcetypename2: ... # ...
	 */
	public OpenGamePhaseBank(ArrayList<String> creationCommands)
	{
		// Initializes attributes
		this.creationCommands = creationCommands;
	}

	@Override
	protected void initialize()
	{
		// Creates the gamephases by going through the commands
		for (int i = 0; i < this.creationCommands.size(); i++)
		{
			String command = this.creationCommands.get(i);
			String[] parts = command.split("#");
			
			// Checks that there are enough arguments
			if (parts.length < 2)
			{
				System.err.println("Couldn't load a GamePhase. Line " + command + 
						"doensn't have enough arguments");
				continue;
			}
			
			HashMap<ResourceType, String[]> banknames = 
					new HashMap<ResourceType, String[]>();
			
			// Reads the banknames and resourcetypes from arguments 2 forwards
			for (int partindex = 1; partindex < parts.length; partindex++)
			{
				// The banknames are listed in the second part of the argument
				String[] resourceparts = parts[partindex].split(":");
				String[] nameslist = resourceparts[1].split(",");
				
				// Adds the names to the map
				banknames.put(ResourceType.fromString(resourceparts[0]), nameslist);
			}
			
			// Creates a new phase, adds it to the bank and updates it according 
			// to the findings
			GamePhase newPhase = new GamePhase(parts[0]);
			
			for (ResourceType type : banknames.keySet())
			{
				newPhase.connectResourceBankNames(type,  banknames.get(type));
			}
			
			addObject(newPhase, parts[0]);
		}
	}
}
