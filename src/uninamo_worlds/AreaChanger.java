package uninamo_worlds;

import uninamo_components.ConnectorRelay;
import uninamo_components.NormalComponentRelay;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TotalCostAnalyzer;
import uninamo_gameplaysupport.TurnTimer;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.KeyListenerHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_resourcebanks.GamePhaseBank;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import utopia_worlds.AreaRelay;

/**
 * AreaChanger makes the game transition between different states like coding 
 * and design views.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class AreaChanger extends AreaRelay
{
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates the areaChanger as well as all the areas in the game
	 * 
	 * @param mousehandler MouseListenerHandler that will inform the areas 
	 * about mouse events
	 * @param keyHandler The keyListenerHandler that will inform the areas 
	 * about key events
	 * @param actorhandler ActorHandler that will inform areas about step 
	 * events
	 * @param drawer DrawableHandler that will draw the areas
	 */
	public AreaChanger(MouseListenerHandler mousehandler, 
			KeyListenerHandler keyHandler, ActorHandler actorhandler, 
			DrawableHandler drawer)
	{
		// Creates areas
		GamePhaseBank phaseBank = MultiMediaHolder.getGamePhaseBank("default");
		
		// Results contains analysis of the costs of the previous mission
		addArea("results", new Area(phaseBank.getPhase("results"), 
				mousehandler, actorhandler, drawer, keyHandler));
		// Coding area contains the user interface for "coding"
		addArea("coding", new Area(phaseBank.getPhase("gameplay"), 
				mousehandler, actorhandler, drawer, keyHandler));
		
		// Creates shared resources
		TotalCostAnalyzer costAnalyzer = new TotalCostAnalyzer(getArea("results"));
		ConnectorRelay connectorRelay = new ConnectorRelay();
		NormalComponentRelay componentRelay = new NormalComponentRelay(costAnalyzer, 
				getArea("coding"));
		TestHandler testHandler = new TestHandler(null);
		
		TurnTimer turnTimer = new TurnTimer(testHandler, 
				getArea("coding"), actorhandler);
		new CodingObjectCreator(this, connectorRelay, componentRelay, 
				testHandler, turnTimer);
		
		// The design area contains the context and the mission stuff
		addArea("design", new Area(phaseBank.getPhase("gameplay"), 
				mousehandler, actorhandler, drawer, keyHandler));
		new DesignObjectCreator(this, testHandler, componentRelay, 
				connectorRelay, costAnalyzer);
		
		// Manual contains useful information
		addArea("manual", new Area(phaseBank.getPhase("gameplay"), 
				mousehandler, actorhandler, drawer, keyHandler));
		
		// Mission contains the short mission briefing shown at the start of a stage
		addArea("mission", new Area(phaseBank.getPhase("gameplay"), 
				mousehandler, actorhandler, drawer, keyHandler));
		new MissionObjectCreator(getArea("mission"));
	}
}
