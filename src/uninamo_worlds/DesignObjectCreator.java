package uninamo_worlds;

import omega_world.Area;
import omega_world.AreaObjectCreator;
import uninamo_components.ConnectorRelay;
import uninamo_components.NormalComponentRelay;
import uninamo_gameplaysupport.InvisibleWall;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TotalCostAnalyzer;
import uninamo_gameplaysupport.VictoryHandler;
import uninamo_machinery.MachineCounter;
import uninamo_main.GameSettings;
import uninamo_previous.AreaChanger;
import uninamo_previous.FinishButton;
import uninamo_previous.TestingButton;
import uninamo_previous.ToCodeButton;
import uninamo_userinterface.DemoButton;

/**
 * DesignObjectCreator creates the objects needed in the design area. It also 
 * handles the background of the said area.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class DesignObjectCreator extends AreaObjectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private AreaChanger areaChanger;
	private TestHandler testHandler;
	private NormalComponentRelay componentRelay;
	private ConnectorRelay connectorRelay;
	private TotalCostAnalyzer costAnalyzer;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new objectCreator. The creator will create the objects when 
	 * the area starts.
	 * 
	 * @param areaChanger The areaChanger that handles different areas
	 * @param testHandler The testHandler that will inform the objects about 
	 * test events
	 * @param componentRelay The componentRelay that holds the information 
	 * about created components
	 * @param connectorRelay The connectorRelay that will keep track of the 
	 * created connectors
	 * @param costAnalyzer The costAnalyzer that will keep track of the machine 
	 * costs
	 */
	public DesignObjectCreator(AreaChanger areaChanger, 
			TestHandler testHandler, NormalComponentRelay componentRelay, 
			ConnectorRelay connectorRelay, TotalCostAnalyzer costAnalyzer)
	{
		super(areaChanger.getArea("design"), "paper", "gameplaybackgrounds", 
				GameSettings.screenWidth, GameSettings.screenHeight);
		
		// Initializes attributes
		this.areaChanger = areaChanger;
		this.componentRelay = componentRelay;
		this.testHandler = testHandler;
		this.connectorRelay = connectorRelay;
		this.costAnalyzer = costAnalyzer;
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	protected void createObjects(Area area)
	{
		// Creates the invisible walls
		new InvisibleWall(0, 1, 0, area);
		new InvisibleWall(0, -1, GameSettings.screenWidth, area);
		new InvisibleWall(-1, 0, GameSettings.screenHeight - 80, area);
		
		
		FinishButton finishButton = new FinishButton(this.areaChanger, 
				this.testHandler);
		
		// Creates a new victoryHandler
		VictoryHandler victoryHandler = new VictoryHandler(finishButton);
		
		// Creates interface elements
		DemoButton demoButton = new DemoButton(area, 600, 80, this.testHandler, 
				victoryHandler);
		TestingButton testButton = new TestingButton(area, GameSettings.screenWidth - 110, 
				GameSettings.screenHeight - 45, this.testHandler);
		new ToCodeButton(this.areaChanger, this.testHandler, testButton, 
				demoButton, this.componentRelay);
		
		// Creates a new machine counter
		MachineCounter machineCounter = new MachineCounter(this.costAnalyzer);
		
		// Creates stage objects
		new DesignInitializer(this.areaChanger, this.testHandler, 
				machineCounter, this.connectorRelay, victoryHandler);
	}
}
