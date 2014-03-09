package uninamo_worlds;

import java.util.HashMap;

import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;

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
	}

	
	// 
}
