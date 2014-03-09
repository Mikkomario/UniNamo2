package uninamo_worlds;

import java.util.ArrayList;

import uninamo_main.GameSettings;
import utopia_backgrounds.Background;
import utopia_backgrounds.Tile;
import utopia_gameobjects.GameObject;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Room;

/**
 * AreaObject creators are set into areas. They create a set of objects when 
 * the area starts.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public abstract class AreaObjectCreator extends GameObject implements RoomListener
{
	// ATTRIBUTES	----------------------------------------------------
	
	private Area area;
	private String areaBackgroundName, areaBackgroundBankName;
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new areaObjectCreator to the given area. The creator will 
	 * create objects when the area starts.
	 * 
	 * @param area The area the creator will reside at
	 * @param backgroundName The name of the area's background image in a 
	 * spriteBank (null if no background)
	 * @param backgroundBankName The name of the spriteBank that holds the 
	 * background used in the area (null if no background)
	 */
	public AreaObjectCreator(Area area, String backgroundName, 
			String backgroundBankName)
	{
		// Initializes attributes
		this.area = area;
		this.areaBackgroundBankName = backgroundBankName;
		this.areaBackgroundName = backgroundName;
		
		// Adds the object to the handler(s)
		if (area != null)
			area.addObject(this);
	}
	
	
	// ABSTRACT METHODS	-------------------------------------------------
	
	/**
	 * Here the creator will create objects it's supposed to and adds them 
	 * to the area (if necessary)
	 * 
	 * @param area The area where the objects will be created to.
	 */
	protected abstract void createObjects(Area area);
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onRoomStart(Room room)
	{
		// Creates the objects
		createObjects(this.area);
		
		// Changes the room's background
		if (this.areaBackgroundBankName != null && 
				this.areaBackgroundName != null)
		{
			ArrayList<Background> backs = new ArrayList<Background>();
			backs.add(new Tile(GameSettings.screenWidth / 2, 
					GameSettings.screenHeight / 2, this.area.getDrawer(),
					this.area.getActorHandler(), 
					MultiMediaHolder.getSpriteBank(this.areaBackgroundBankName), 
					this.areaBackgroundName, GameSettings.screenWidth, 
					GameSettings.screenHeight));
			this.area.setBackgrounds(backs, false);
		}
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Removes the background (if any)
		if (this.areaBackgroundBankName != null && this.areaBackgroundName != null)
			this.area.setBackgrounds(null, true);
	}
}
