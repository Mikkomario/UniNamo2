package uninamo_worlds;

import java.util.HashMap;

import uninamo_components.ConnectorRelay;
import uninamo_components.NormalComponentRelay;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TotalCostAnalyzer;
import uninamo_gameplaysupport.TurnTimer;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_resourcebanks.GamePhaseBank;
import utopia_resourcebanks.MultiMediaHolder;

/**
 * AreaChanger makes the game transition between different states like coding 
 * and design views.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class AreaChanger
{
	// ATTRIBUTES	------------------------------------------------------
	
	private HashMap<String, Area> areas;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates the areaChanger as well as all the areas in the game
	 * 
	 * @param mousehandler MouseListenerHandler that will inform the areas 
	 * about mouse events
	 * @param actorhandler ActorHandler that will inform areas about step 
	 * events
	 * @param drawer DrawableHandler that will draw the areas
	 */
	public AreaChanger(MouseListenerHandler mousehandler, 
			ActorHandler actorhandler, DrawableHandler drawer)
	{
		// Initializes attributes
		this.areas = new HashMap<String, Area>();
		
		// Creates areas
		GamePhaseBank phaseBank = MultiMediaHolder.getGamePhaseBank("default");
		
		// Results contains analysis of the costs of the previous mission
		this.areas.put("results", new Area(phaseBank.getPhase("results"), 
				mousehandler, actorhandler, drawer));
		// Coding area contains the user interface for "coding"
		this.areas.put("coding", new Area(phaseBank.getPhase("gameplay"), 
				mousehandler, actorhandler, drawer));
		
		// Creates shared resources
		TotalCostAnalyzer costAnalyzer = new TotalCostAnalyzer(this.areas.get("results"));
		ConnectorRelay connectorRelay = new ConnectorRelay();
		NormalComponentRelay componentRelay = new NormalComponentRelay(costAnalyzer, this.areas.get("coding"));
		TestHandler testHandler = new TestHandler(null);
		
		TurnTimer turnTimer = new TurnTimer(testHandler, 
				this.areas.get("coding"), actorhandler);
		new CodingObjectCreator(this, connectorRelay, componentRelay, 
				testHandler, turnTimer);
		
		// The design area contains the context and the mission stuff
		this.areas.put("design", new Area(phaseBank.getPhase("gameplay"), 
				mousehandler, actorhandler, drawer));
		new DesignObjectCreator(this, testHandler, componentRelay, 
				connectorRelay, costAnalyzer);
		
		// Manual contains useful information
		this.areas.put("manual", new Area(phaseBank.getPhase("gameplay"), 
				mousehandler, actorhandler, drawer));
		
		// Mission contains the short mission briefing shown at the start of a stage
		this.areas.put("mission", new Area(phaseBank.getPhase("gameplay"), 
				mousehandler, actorhandler, drawer));
		new MissionObjectCreator(this.areas.get("mission"));
	}

	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * Returns an area with the given name
	 * @param areaName The name of the area to be returned
	 * @return An area with the given name or null if no such area exists.
	 */
	public Area getArea(String areaName)
	{
		if (!this.areas.containsKey(areaName))
		{
			System.err.println("There is no area named " + areaName);
			return null;
		}
		
		return this.areas.get(areaName);
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Ends functions in all the areas. This might be useful to do between 
	 * area transitions
	 */
	public void endAllAreas()
	{
		for (Area area : this.areas.values())
		{
			area.end();
		}
	}
}
