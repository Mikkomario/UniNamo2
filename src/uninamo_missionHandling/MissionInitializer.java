package uninamo_missionHandling;

import uninamo_main.GameSettings;
import uninamo_userinterface.Note;
import uninamo_worlds.AreaChanger;
import utopia_fileio.FileReader;

/**
 * MissionInitializer creates the necessary stuff to start a mission
 * 
 * @author Mikko Hilpinen
 * @since 13.3.2014
 */
public class MissionInitializer extends FileReader
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Mode mode;
	//private String filename;
	private AreaChanger areaChanger;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new missionInitializer that also initializes the necessary 
	 * objects
	 * 
	 * @param instructionFileName The file from which the necessary data is 
	 * read (data/ automatically included)
	 * @param areaChanger The areaChanger that handles different areas
	 */
	public MissionInitializer(String instructionFileName, AreaChanger areaChanger)
	{
		// Initializes atributes
		this.mode = null;
		this.areaChanger = areaChanger;
		
		// Reads the data / initializes the mission
		readFile(instructionFileName, "*");
	}

	@Override
	protected void onLine(String line)
	{
		// Checks if a mode should be changed
		if (line.startsWith("&"))
		{
			String modename = line.substring(1).toLowerCase();
			
			for (Mode mode : Mode.values())
			{
				if (mode.toString().toLowerCase().equals(modename))
				{
					this.mode = mode;
					break;
				}
			}
		}
		// Otherwise reacts depending on the mode
		else if (this.mode == Mode.NOTE1)
			new Note(GameSettings.screenWidth / 2 + 64, GameSettings.screenHeight - 200, 
					75, 32, "docket", line, this.areaChanger.getArea("mission"));
		else if (this.mode == Mode.NOTE2)
			new Note(2 * GameSettings.screenWidth / 3, GameSettings.screenHeight / 2, 
					75, 96, "description", line, this.areaChanger.getArea("mission"));
	}

	
	// ENUMERATIONS	------------------------------------------------------
	
	private enum Mode
	{
		NOTE1, NOTE2;
	}
}
