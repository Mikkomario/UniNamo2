package uninamo_worlds;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;
import uninamo_main.GameSettings;
import uninamo_userinterface.Note;
import vision_sprite.SpriteBank;

/**
 * MissionInitializer creates the necessary stuff to start a mission
 * 
 * @author Mikko Hilpinen
 * @since 13.3.2014
 */
public class MissionInitializer extends ObjectInitializer
{
	// TODO: Replace with objectconstructor
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new missionInitializer that also initializes the notes
	 * @param handlers The handlers that will handle the objects
	 */
	public MissionInitializer(HandlerRelay handlers)
	{
		super(handlers);
		
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
		createNote(getHandlers(), currentMode, arguments[0]);
	}
	
	private static void createNote(HandlerRelay handlers, CreationMode mode, String content)
	{
		Vector3D position, margins;
		String spriteName;
		
		if (mode == CreationMode.NOTE1)
		{
			position = GameSettings.resolution.dividedBy(2).plus(new Vector3D(64, 0));
			margins = new Vector3D(75, 32);
			spriteName = "docket";
		}
		else
		{
			position = GameSettings.resolution.dividedBy(new Vector3D(3, 2));
			margins = new Vector3D(75, 96);
			spriteName = "description";
		}
		
		new Note(handlers, position, margins, SpriteBank.getSprite("mission", spriteName), 
				content);
	}
}
