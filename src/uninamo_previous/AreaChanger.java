package uninamo_previous;

import arc_bank.GamePhaseBank;
import arc_bank.OpenGamePhaseBank;
import genesis_graphic.GamePanel;
import genesis_graphic.GameWindow;
import omega_world.AreaRelay;
import uninamo_components.ConnectorRelay;
import uninamo_components.NormalComponentRelay;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TotalCostAnalyzer;
import uninamo_gameplaysupport.TurnTimer;
import uninamo_worlds.CodingObjectCreator;
import uninamo_worlds.DesignObjectCreator;
import uninamo_worlds.MissionObjectCreator;

/**
 * AreaChanger makes the game transition between different states like coding 
 * and design views.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 * @deprecated This will be replaced with a static method
 */
public class AreaChanger extends AreaRelay
{
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates the areaChanger as well as all the areas in the game
	 * @param window The window that hosts the game
	 * @param panel The panel in which the areas will be drawn
	 */
	public AreaChanger(GameWindow window, GamePanel panel)
	{
		super(window, panel);
		
		// Creates areas
		GamePhaseBank phaseBank = OpenGamePhaseBank.getGamePhaseBank("default");
		
		// Results contains analysis of the costs of the previous mission
		addArea("results", phaseBank.getPhase("results"));
		// Coding area contains the user interface for "coding"
		addArea("coding", phaseBank.getPhase("gameplay"));
		
		// Creates shared resources
		TotalCostAnalyzer costAnalyzer = new TotalCostAnalyzer(getArea("results"));
		ConnectorRelay connectorRelay = new ConnectorRelay();
		NormalComponentRelay componentRelay = new NormalComponentRelay(costAnalyzer, 
				getArea("coding"));
		TestHandler testHandler = new TestHandler(null);
		
		TurnTimer turnTimer = new TurnTimer(testHandler, 
				getArea("coding"), getArea("coding").getActorHandler());
		new CodingObjectCreator(this, connectorRelay, componentRelay, 
				testHandler, turnTimer);
		
		// The design area contains the context and the mission stuff
		addArea("design", phaseBank.getPhase("gameplay"));
		new DesignObjectCreator(this, testHandler, componentRelay, 
				connectorRelay, costAnalyzer);
		
		// Manual contains useful information
		addArea("manual", phaseBank.getPhase("gameplay"));
		
		// Mission contains the short mission briefing shown at the start of a stage
		addArea("mission", phaseBank.getPhase("gameplay"));
		new MissionObjectCreator(getArea("mission"));
	}
}
