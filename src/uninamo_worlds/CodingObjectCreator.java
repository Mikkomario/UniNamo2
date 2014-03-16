package uninamo_worlds;

import uninamo_components.ComponentBox;
import uninamo_components.ComponentType;
import uninamo_components.ConnectorRelay;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TurnTimer;
import uninamo_main.GameSettings;
import uninamo_userinterface.CodeTransitionButton;
import uninamo_userinterface.ManualButton;
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
		new CodeTransitionButton(0, area, this.areaChanger.getArea("design"));
		new TestingButton(GameSettings.screenWidth - 110, 45, area.getDrawer(), 
				area.getMouseHandler(), area, this.testHandler);
		
		new ComponentBox(64, 30, area, this.testHandler, 
				this.connectorRelay, this.timer, ComponentType.PULSE);
		new ComponentBox(64, 90, area, this.testHandler, 
				this.connectorRelay, this.timer, ComponentType.POWER);
		
		new ManualButton(GameSettings.screenWidth / 3, GameSettings.screenHeight, 
				area.getDrawer(), area.getMouseHandler(), area, 
				this.areaChanger, this.timer);
	}
}
