package uninamo_components;

import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TurnHandler;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_worlds.Room;

/**
 * There's a componentType for each different type of component out there. 
 * Each of the types also has its own class (at least usually).
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 * @see Component
 */
public enum ComponentType
{
	/**
	 * Or component sends TRUE when either of its inputs receive TRUE
	 */
	OR,
	/**
	 * Pulse sends TRUE and FALSE in succession
	 */
	PULSE,
	/**
	 * Power sends TRUE
	 */
	POWER;
	
	
	// METHODS	---------------------------------------------------------
	
	/**
	 * Creates a new component of this type
	 * 
	 * @param x The component's new x-coordinate
	 * @param y The component's new y-coordinate
	 * @param drawer The drawableHandler that will draw the component
	 * @param actorHandler The actorHandler that will inform the object about 
	 * step events and animate it (optional)
	 * @param mouseHandler The mouseListenerHandler that will inform the 
	 * component about mouse events
	 * @param room The room where the object will be placed to
	 * @param testHandler The testHandler that will inform the object about 
	 * test events (optional)
	 * @param connectorRelay The connectorRelay that will handle the connectors 
	 * of the component
	 * @param componentRelay The componentRelay that will keep track of the 
	 * component
	 * @param turnHandler The turnHandler that will inform the component about 
	 * turn events (if applicable)
	 * @param isTestComponent Will the component be used only for testing 
	 * purposes. If so, it doesn't react to mouse but will use test cables
	 * @return A component of this componentType
	 */
	public NormalComponent getNewComponent(int x, int y, DrawableHandler drawer, 
			ActorHandler actorHandler, MouseListenerHandler mouseHandler, 
			Room room, TestHandler testHandler, ConnectorRelay connectorRelay, 
			NormalComponentRelay componentRelay, 
			TurnHandler turnHandler, boolean isTestComponent)
	{
		switch (this)
		{
			case OR: return new OrComponent(x, y, drawer, actorHandler, 
					mouseHandler, room, testHandler, connectorRelay, 
					componentRelay, isTestComponent);
			case PULSE: return new PulseGeneratorComponent(x, y, drawer, 
					actorHandler, mouseHandler, room, testHandler, 
					connectorRelay, componentRelay, turnHandler, isTestComponent);
			case POWER: return new PowerSourceComponent(x, y, drawer, 
					actorHandler, mouseHandler, room, testHandler, 
					connectorRelay, componentRelay, isTestComponent);
			default: System.err.println("Couldn't create the component. "
					+ "Please update ComponentType.getNewComponent method"); 
				break;
		}
		
		return null;
	};
	
	/**
	 * @return The name used of the componentType
	 */
	public String getName()
	{
		switch (this)
		{
			case OR: return "Logical OR";
			case PULSE: return "Pulse Generator";
			case POWER: return "Power Source";
			default: return "Failed to recognize";
		}
	}
}
