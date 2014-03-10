package uninamo_worlds;

import uninamo_components.ConnectorRelay;
import uninamo_components.OrComponent;
import uninamo_components.PowerSourceComponent;
import uninamo_components.PulseGeneratorComponent;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TurnTimer;
import uninamo_main.GameSettings;
import uninamo_userinterface.CodeTransitionButton;
import uninamo_userinterface.TestingButton;

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
	private TestHandler testHandler;
	private TurnTimer timer;
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new CodignObjectCreator. The creator will create the objects 
	 * when the area starts.
	 * 
	 * @param areaChanger The areaChanger that handles different areas in the 
	 * game
	 * @param connectorRelay The connectorRelay that contains the connectors 
	 * used in the area
	 * @param testHandler The testHandler that will inform the objects about 
	 * test events
	 * @param timer The turnTimer that informs the objects about turn events
	 */
	public CodingObjectCreator(AreaChanger areaChanger, 
			ConnectorRelay connectorRelay, TestHandler testHandler, 
			TurnTimer timer)
	{
		super(areaChanger.getArea("coding"), null, null);
		
		// Initializes attributes
		this.areaChanger = areaChanger;
		this.connectorRelay = connectorRelay;
		this.testHandler = testHandler;
		this.timer = timer;
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	protected void createObjects(Area area)
	{
		// Creates a set of (test) objects
		new CodeTransitionButton(area.getDrawer(), area.getMouseHandler(), 
				area, CodeTransitionButton.TODESING, this.areaChanger);
		new TestingButton(GameSettings.screenWidth - 110, 45, area.getDrawer(), 
				area.getMouseHandler(), area, this.testHandler);
		
		new PowerSourceComponent(300, 300, area.getDrawer(), 
				area.getActorHandler(), area.getMouseHandler(), area, 
				this.testHandler, this.connectorRelay);
		new OrComponent(600, 300, area.getDrawer(), area.getMouseHandler(), 
				area, this.testHandler, this.connectorRelay);
		new PulseGeneratorComponent(600, 600, area.getDrawer(), 
				area.getActorHandler(), area.getMouseHandler(), area, 
				this.testHandler, this.connectorRelay, this.timer);
	}
}
