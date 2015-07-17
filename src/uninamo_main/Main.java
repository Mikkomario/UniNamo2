package uninamo_main;

import exodus_util.ConstructableGameObject;
import exodus_world.Area;
import exodus_world.AreaBank;
import exodus_world.AreaHandlerConstructor;
import exodus_world.AreaObjectConstructorProvider;
import flow_recording.AbstractConstructor;
import genesis_event.ActorHandler;
import genesis_event.DrawableHandler;
import genesis_event.HandlerRelay;
import genesis_event.MouseListenerHandler;
import genesis_util.DepthConstants;
import genesis_video.GamePanel;
import genesis_video.GameWindow;
import arc_bank.GamePhaseBank;
import tests.MouseTester;
import uninamo_components.ConnectorRelay;
import uninamo_components.NormalComponentRelay;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TotalCostAnalyzer;
import uninamo_gameplaysupport.TurnHandler;
import uninamo_gameplaysupport.TurnTimer;
import uninamo_worlds.CodingObjectCreator;
import uninamo_worlds.DesignObjectCreator;
import vision_sprite.SpriteBank;

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
		// Initializes resources
		SpriteBank.initializeSpriteResources("configure/spriteload.txt");
		GamePhaseBank.initializeGamePhaseResources("configure/gamephaseload.txt", "default");
		
		// Creates the main window
		GameWindow window = new GameWindow(GameSettings.resolution, "UniNamo2", 
				!GameSettings.fullScreen, 120, 20);
		GamePanel panel = window.getMainPanel().addGamePanel();
		if (GameSettings.fullScreen)
			window.setFullScreen(true);
		
		// Starts the gameplay area(s)
		HandlerRelay superHandlers = new HandlerRelay();
		superHandlers.addHandler(new DrawableHandler(true, panel.getDrawer()));
		superHandlers.addHandler(new ActorHandler(true, window.getHandlerRelay()));
		superHandlers.addHandler(new MouseListenerHandler(true, window.getHandlerRelay()));
		// TODO: Add keyboard support if necessary
		TotalCostAnalyzer costAnalyzer = new TotalCostAnalyzer();
		
		AreaBank.initializeAreaResources("configure/areas.txt", new HandlerConstructor(
				superHandlers, costAnalyzer), new ConstructorProvider());
		AreaBank.activateAreaBank("gameplay");
		costAnalyzer.connectToArea(AreaBank.getArea("gameplay", "results"));
		
		Area codingArea = AreaBank.getArea("gameplay", "coding");
		Area designArea = AreaBank.getArea("gameplay", "design");
		
		// Adds the objects to the areas
		new DesignObjectCreator(designArea.getHandlers(), costAnalyzer);
		new CodingObjectCreator(codingArea.getHandlers());
		//new MouseTester(codingArea.getHandlers());
		
		designArea.start(true);
		codingArea.start(false);
		
		Utility.setMouseState(designArea.getHandlers(), false);
		Utility.setVisibleState(designArea.getHandlers(), false);
		
		codingArea.getIsActiveStateOperator().setState(true);
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
	
	
	// SUBCLASSES	-------------------
	
	private static class HandlerConstructor implements AreaHandlerConstructor
	{
		// ATTRIBUTES	---------------
		
		private HandlerRelay superHandlers;
		private HandlerRelay sharedHandlers;
		private TurnHandler turnHandler;
		
		
		// CONSTRUCTOR	---------------
		
		public HandlerConstructor(HandlerRelay superHandlers, TotalCostAnalyzer costAnalyzer)
		{
			this.superHandlers = superHandlers;
			
			// Uses the same turnTimer for all objects
			this.turnHandler = new TurnTimer(superHandlers).getListenerHandler();
			
			// Coding and design share test, connector and component handlers
			this.sharedHandlers = new HandlerRelay();
			this.sharedHandlers.addHandler(new TestHandler(null));
			this.sharedHandlers.addHandler(new ConnectorRelay());
			this.sharedHandlers.addHandler(new NormalComponentRelay(costAnalyzer));
		}
		
		
		// IMPLEMENTED METHODS	-------
		
		@Override
		public HandlerRelay constructRelay(String areaName)
		{
			HandlerRelay relay;
			// Coding and design share some special handlers
			if (areaName.equals("coding") || areaName.equals("design"))
				relay = new HandlerRelay(this.sharedHandlers);
			else
				relay = new HandlerRelay();
			
			// Manual is drawn a top of the other areas
			int depth = DepthConstants.NORMAL;
			if (areaName.equals("manual"))
				depth --;
			
			// All areas use the mouse, drawable and actor handlers
			relay.addHandler(new ActorHandler(false, this.superHandlers));
			relay.addHandler(new MouseListenerHandler(false, this.superHandlers));
			relay.addHandler(new DrawableHandler(false, true, depth, 5, this.superHandlers));
			
			// Gameplay areas also use the turnHandler
			if (areaName.equals("coding") || areaName.equals("design") || 
					areaName.equals("manual"))
				relay.addHandler(this.turnHandler);
			
			// TODO: Also add collision and collidable handlers
			
			return relay;
		}
	}
	
	private static class ConstructorProvider implements 
			AreaObjectConstructorProvider<ConstructableGameObject>
	{
		@Override
		public AbstractConstructor<ConstructableGameObject> getConstructor(
				Area targetArea)
		{
			// TODO: Add the correct constructors once they are done
			return null;
		}	
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
