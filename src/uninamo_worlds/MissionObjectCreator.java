package uninamo_worlds;

import uninamo_main.GameSettings;
import uninamo_userinterface.Note;

/**
 * MissionObjectCreator creates the necessary objects at the start of the 
 * mission brief
 * 
 * @author Mikko Hilpinen
 * @since 13.3.2014
 */
public class MissionObjectCreator extends AreaObjectCreator
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new object creator that will start listening to its specifi 
	 * area and create the objects when need be.
	 * 
	 * @param areaChanger The areaChanger that handles the different areas 
	 */
	public MissionObjectCreator(AreaChanger areaChanger)
	{
		super(areaChanger.getArea("mission"), null, null);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected void createObjects(Area area)
	{
		// Creates the notes
		new Note(2 * GameSettings.screenWidth / 3, GameSettings.screenHeight / 2, 
				75, 75, "description", area);
		new Note(GameSettings.screenWidth / 2 + 64, GameSettings.screenHeight - 200, 
				75, 32, "docket", area);
	}
}
