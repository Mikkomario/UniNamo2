package uninamo_worlds;

import uninamo_components.ConnectorRelay;
import uninamo_components.OrComponent;
import uninamo_components.PowerSourceComponent;
import uninamo_userinterface.CodeTransitionButton;

/**
 * This objectCreator creates the necessary elements used in the coding 
 * environment when the room starts.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class CodingObjectCreator extends AreaObjectCreator
{
	// ATTRIBUTES	----------------------------------------------------
	
	private AreaChanger areaChanger;
	private ConnectorRelay connectorRelay;
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new CodignObjectCreator. The creator will create the objects 
	 * when the area starts.
	 * 
	 * @param areaChanger The areaChanger that handles different areas in the 
	 * game
	 * @param connectorRelay The connectorRelay that contains the connectors 
	 * used in the area
	 */
	public CodingObjectCreator(AreaChanger areaChanger, 
			ConnectorRelay connectorRelay)
	{
		super(areaChanger.getArea("coding"), null, null);
		
		// Initializes attributes
		this.areaChanger = areaChanger;
		this.connectorRelay = connectorRelay;
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	protected void createObjects(Area area)
	{
		// Creates a set of (test) objects
		new CodeTransitionButton(area.getDrawer(), area.getMouseHandler(), 
				area, CodeTransitionButton.TODESING, this.areaChanger);
		
		new PowerSourceComponent(300, 300, area.getDrawer(), 
				area.getActorHandler(), area.getMouseHandler(), area, 
				this.connectorRelay);
		new OrComponent(600, 300, area.getDrawer(), area.getMouseHandler(), 
				area, this.connectorRelay);
	}

}
