package uninamo_worlds;

import java.util.HashMap;

import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
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
		
		// TODO Create areas
		
		// Normal coding area (with test objects)
		this.areas.put("coding", new Area(MultiMediaHolder.getGamePhaseBank(
				"default").getPhase("gameplay"), mousehandler, actorhandler, drawer));
		new CodingObjectCreator(this);
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
