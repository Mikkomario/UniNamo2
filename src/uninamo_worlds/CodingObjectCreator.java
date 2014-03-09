package uninamo_worlds;

import uninamo_components.ConnectorRelay;
import uninamo_components.OrComponent;
import uninamo_components.PowerSourceComponent;

/**
 * This objectCreator creates the necessary elements used in the coding 
 * environment when the room starts.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class CodingObjectCreator extends AreaObjectCreator
{
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new CodignObjectCreator. The creator will create the objects 
	 * when the area starts.
	 * 
	 * @param areaChanger The areaChanger that handles different areas in the 
	 * game
	 */
	public CodingObjectCreator(AreaChanger areaChanger)
	{
		super(areaChanger.getArea("coding"), null, null);
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	protected void createObjects(Area area)
	{
		// Creates a set of (test) objects
		ConnectorRelay relay = new ConnectorRelay();
		new PowerSourceComponent(300, 300, area.getDrawer(), 
				area.getActorHandler(), area.getMouseHandler(), area, relay);
		new OrComponent(600, 300, area.getDrawer(), area.getMouseHandler(), area, relay);
	}

}
