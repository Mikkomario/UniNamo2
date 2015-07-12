package uninamo_worlds;

import omega_world.Area;
import omega_world.AreaObjectCreator;
import uninamo_components.ComponentBox;
import uninamo_components.ComponentType;
import uninamo_components.ConnectorRelay;
import uninamo_components.NormalComponentRelay;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TurnTimer;
import uninamo_main.GameSettings;
import uninamo_previous.AreaChanger;
import uninamo_userinterface.CodeTransitionButton;
import uninamo_userinterface.CurrentCostDrawer;
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
	private NormalComponentRelay componentRelay;
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new CodignObjectCreator. The creator will create the objects 
	 * when the area starts.
	 * 
	 * @param areaChanger The areaChanger that handles different areas in the 
	 * game
	 * @param connectorRelay The connectorRelay that contains the connectors 
	 * used in the area
	 * @param componentRelay The componentRelay that will keep track of the 
	 * created components
	 * @param testHandler The testHandler that will inform the objects about 
	 * test events
	 * @param timer The turnTimer that informs the objects about turn events
	 */
	public CodingObjectCreator(AreaChanger areaChanger, 
			ConnectorRelay connectorRelay, NormalComponentRelay componentRelay, 
			TestHandler testHandler, TurnTimer timer)
	{
		super(areaChanger.getArea("coding"), null, null, 0, 0);
		
		// Initializes attributes
		this.areaChanger = areaChanger;
		this.connectorRelay = connectorRelay;
		this.testHandler = testHandler;
		this.timer = timer;
		this.componentRelay = componentRelay;
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	protected void createObjects(Area area)
	{
		// Creates the interface elements
		new CodeTransitionButton(0, area, this.areaChanger.getArea("design"));
		new TestingButton(area, GameSettings.screenWidth - 110, 45, this.testHandler);
		
		CurrentCostDrawer costDrawer = new CurrentCostDrawer(area);
		
		new ComponentBox(64, 30, area, this.testHandler, 
				this.connectorRelay, this.componentRelay, costDrawer, 
				this.timer, ComponentType.PULSE);
		new ComponentBox(64, 90, area, this.testHandler, 
				this.connectorRelay, this.componentRelay, costDrawer, 
				this.timer, ComponentType.POWER);
		
		new ManualButton(GameSettings.screenWidth / 3, GameSettings.screenHeight, 
				this.areaChanger, this.timer);
		
		// Creates the demo components
		new CodingInitializer(area, this.componentRelay, this.connectorRelay, 
				this.testHandler, this.timer);
	}
}
