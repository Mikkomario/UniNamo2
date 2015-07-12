package uninamo_main;

import genesis_graphic.GamePanel;
import genesis_graphic.GameWindow;

import java.awt.BorderLayout;

import omega_graphic.OpenSpriteBankHolder;
import arc_bank.GamePhaseBank;
import arc_bank.MultiMediaHolder;
import arc_bank.OpenGamePhaseBankHolder;
import arc_resource.MetaResource;
import tests.FpsApsTest;
import uninamo_components.ConnectorRelay;
import uninamo_components.NormalComponentRelay;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TotalCostAnalyzer;
import uninamo_gameplaysupport.TurnTimer;
import uninamo_previous.AreaChanger;
import uninamo_worlds.CodingObjectCreator;
import uninamo_worlds.DesignObjectCreator;
import uninamo_worlds.MissionObjectCreator;

/**
 * This is the main class of UniNamo2. The class initializes the necessary 
 * systems and starts the game. The class also features the main method.
 * 
 * @author Mikko Hilpinen
 * @since 7.3.2014
 */
public class Main
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new main and starts the game. Opens the game in a new window.
	 */
	public Main()
	{
		// TODO: Make using gameWindow less cryptic and easier
		
		// Initializes resources
		/*
		MultiMediaHolder.initializeResourceDatabase(ResourceType.SPRITE, 
				"configure/spriteload.txt");
		MultiMediaHolder.initializeResourceDatabase(ResourceType.GAMEPHASE, 
				"configure/gamephaseload.txt");
		*/
		MultiMediaHolder.initializeResourceDatabase(
				new OpenSpriteBankHolder("configure/spriteload.txt"));
		MultiMediaHolder.initializeResourceDatabase(
				new OpenGamePhaseBankHolder("configure/gamephaseload.txt"));
		
		// Creates the main window
		GamePanel gamepanel = new GamePanel(GameSettings.screenWidth, 
				GameSettings.screenHeight);
		GameWindow window = new GameWindow(GameSettings.screenWidth, 
				GameSettings.screenHeight, "UniNamo2", !GameSettings.fullScreen, 
				120, 10, false);
		window.addGamePanel(gamepanel, BorderLayout.CENTER);
		if (GameSettings.fullScreen)
			window.setFullScreen(true);
		
		// Starts the gameplay area(s)
		MultiMediaHolder.activateBank(MetaResource.GAMEPHASE, "default", true);
		
		// TODO: Add key handler when needed
		AreaChanger areachanger = new AreaChanger(window, gamepanel);
		areachanger.getArea("design").start();
		areachanger.getArea("coding").start();
		areachanger.getArea("mission").start();
		
		// TODO: This causes occasional errors
		areachanger.getArea("coding").getMouseHandler().inactivate();
		areachanger.getArea("coding").getDrawer().setInvisible();
		
		// Also activates FPS test
		new FpsApsTest(window.getStepHandler(), gamepanel.getDrawer());
	}
	
	
	// MAIN METHOD	-----------------------------------------------------
	
	/**
	 * Starts the game
	 * 
	 * @param args Not used by the software
	 */
	public static void main(String[] args)
	{
		new Main();
	}
	
	/* TODO: Make this a static method or something
	 * /**
	 * Creates the areaChanger as well as all the areas in the game
	 * @param window The window that hosts the game
	 * @param panel The panel in which the areas will be drawn
	 
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
	 */
}
