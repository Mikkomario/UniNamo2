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
		
		// Creates shared resources
		TotalCostAnalyzer costAnalyzer = new TotalCostAnalyzer();
		ConnectorRelay connectorRelay = new ConnectorRelay();
		NormalComponentRelay componentRelay = new NormalComponentRelay(costAnalyzer);
		TestHandler testHandler = new TestHandler(null);
		
		// Creates areas
		GamePhaseBank phaseBank = MultiMediaHolder.getGamePhaseBank("default");
		
		this.areas.put("coding", new Area(phaseBank.getPhase("gameplay"), 
				mousehandler, actorhandler, drawer));
		TurnTimer turnTimer = new TurnTimer(testHandler, 
				this.areas.get("coding"), actorhandler);
		new CodingObjectCreator(this, connectorRelay, componentRelay, 
				testHandler, turnTimer);
		
		this.areas.put("design", new Area(phaseBank.getPhase("gameplay"), 
				mousehandler, actorhandler, drawer));
		new DesignObjectCreator(this, testHandler, componentRelay, 
				connectorRelay, costAnalyzer);
		
		this.areas.put("manual", new Area(phaseBank.getPhase("gameplay"), 
				mousehandler, actorhandler, drawer));
		
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
