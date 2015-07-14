package uninamo_worlds;

import omega_world.Area;
import uninamo_main.GameSettings;
import uninamo_previous.Note;

/**
 * MissionInitializer creates the necessary stuff to start a mission
 * 
 * @author Mikko Hilpinen
 * @since 13.3.2014
 */
public class MissionInitializer extends ObjectInitializer
{
	// TODO: Replace with objectconstructor
	
	// ATTRIBUTES	-----------------------------------------------------
	
	private Area missionArea;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new missionInitializer that also initializes the notes
	 * @param missionArea The mission area to which the notes will be added
	 */
	public MissionInitializer(Area missionArea)
	{
		// Initializes atributes
		this.missionArea = missionArea;
		
		createObjects();
	}

	@Override
	protected boolean supportsCreationMode(CreationMode mode)
	{
		// MissionInitializer creates notes only
		return (mode == CreationMode.NOTE1 || mode == CreationMode.NOTE2);
	}

	@Override
	protected void createObjectFromLine(String[] arguments,
			CreationMode currentMode)
	{
		if (currentMode == CreationMode.NOTE1) 
			new Note(GameSettings.screenWidth / 2 + 64, GameSettings.screenHeight / 2, 
					75, 32, "docket", arguments[0], this.missionArea);
		else
			new Note(2 * GameSettings.screenWidth / 3, GameSettings.screenHeight / 2, 
					75, 96, "description", arguments[0], this.missionArea);
	}
}
