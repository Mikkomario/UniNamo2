package uninamo_componentBoxes;

import uninamo_components.ConnectorRelay;
import uninamo_components.PulseGeneratorComponent;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TurnHandler;
import uninamo_worlds.Area;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_worlds.Room;

/**
 * PulseGeneratorComponentBox creates pulse generator components
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public class PulseGeneratorComponentBox extends ComponentBox
{
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new component box with the given data
	 * 
	 * @param x The x-coordinate of the box
	 * @param y The y-coordinate of the box
	 * about mouse events
	 * @param area The area where the box is located
	 * @param testHandler The testHandler that will inform objects about test 
	 * events
	 * @param connectorRelay The connectorRelay that will handle the created 
	 * connectors 
	 * @param turnHandler The turnHandler that will inform the objects about 
	 * turn events 
	 */
	public PulseGeneratorComponentBox(int x, int y, Area area,
			TestHandler testHandler, ConnectorRelay connectorRelay,
			TurnHandler turnHandler)
	{
		super(x, y, area, testHandler, connectorRelay, turnHandler);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected String getText()
	{
		return "PulseG";
	}

	@Override
	protected void createComponent(int x, int y, DrawableHandler drawer,
			ActorHandler actorHandler, MouseListenerHandler mouseHandler,
			Room room, TestHandler testHandler, ConnectorRelay connectorRelay,
			TurnHandler turnHandler)
	{
		new PulseGeneratorComponent(x, y, drawer, actorHandler, mouseHandler, 
				room, testHandler, connectorRelay, turnHandler);
	}

}
