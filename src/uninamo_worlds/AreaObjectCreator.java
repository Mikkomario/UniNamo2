package uninamo_worlds;

import utopia_gameobjects.GameObject;
import utopia_listeners.RoomListener;
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
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new areaObjectCreator to the given area. The creator will 
	 * create objects when the area starts.
	 * 
	 * @param area The area the creator will reside at
	 */
	public AreaObjectCreator(Area area)
	{
		// Initializes attributes
		this.area = area;
		
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
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Does nothing
	}
}
