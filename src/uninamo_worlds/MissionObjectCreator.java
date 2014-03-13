package uninamo_worlds;

import uninamo_missionHandling.MissionInitializer;

/**
 * MissionObjectCreator creates the necessary objects at the start of the 
 * mission brief
 * 
 * @author Mikko Hilpinen
 * @since 13.3.2014
 */
public class MissionObjectCreator extends AreaObjectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private AreaChanger areaChanger;
	
	
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
		
		// Initializes attributes
		this.areaChanger = areaChanger;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected void createObjects(Area area)
	{
		// Creates the MissionInitializer
		new MissionInitializer("missions/teststage.txt", this.areaChanger);
	}
}
