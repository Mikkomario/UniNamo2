package uninamo_worlds;

import exodus_world.Area;
import exodus_world.AreaBank;
import exodus_world.AreaListener;
import genesis_event.HandlerRelay;
import genesis_util.Vector3D;
import omega_util.SimpleGameObject;
import uninamo_components.ComponentBox;
import uninamo_components.ComponentType;
import uninamo_components.ConnectorRelay;
import uninamo_main.UninamoHandlerType;
import uninamo_userinterface.CodingInterface;
import uninamo_userinterface.CurrentCostDrawer;

/**
 * This objectCreator creates the necessary elements used in the coding 
 * environment when the room starts.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class CodingObjectCreator extends SimpleGameObject implements AreaListener
{
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new CodignObjectCreator. The creator will create the objects 
	 * when the area starts.
	 * @param handlers The handlers that will handle the object
	 */
	public CodingObjectCreator(HandlerRelay handlers)
	{
		super(handlers);
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	public void onAreaStateChange(Area area)
	{
		HandlerRelay handlers = area.getHandlers();
		ConnectorRelay connectorRelay = (ConnectorRelay) 
				handlers.getHandler(UninamoHandlerType.CONNECTOR);
		
		// Creates the necessary utilities
		CurrentCostDrawer costDrawer = new CurrentCostDrawer(handlers);
		
		// Creates the interface
		new CodingInterface(handlers, AreaBank.getArea("gameplay", "design").getHandlers());
		
		// Creates the component boxes
		Vector3D boxPosition = new Vector3D(64, 30);
		for (ComponentType type : ComponentType.values())
		{
			new ComponentBox(boxPosition, handlers, connectorRelay, costDrawer, type);
			boxPosition = boxPosition.plus(new Vector3D(0, 60));
		}
		
		// Creates other objects
		new CodingInitializer(handlers, connectorRelay, costDrawer);
	}
}
