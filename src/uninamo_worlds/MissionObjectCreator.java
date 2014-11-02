package uninamo_worlds;

import omega_world.Area;
import omega_world.AreaObjectCreator;

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
	 * Creates a new object creator that will start listening to its specific 
	 * area and create the objects when need be.
	 * @param area The area where the objects will be created to
	 */
	public MissionObjectCreator(Area area)
	{
		super(area, null, null, 0, 0);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected void createObjects(Area area)
	{
		// Creates the MissionInitializer
		new MissionInitializer(area);
	}
}
