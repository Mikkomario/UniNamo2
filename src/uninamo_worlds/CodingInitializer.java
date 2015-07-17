package uninamo_worlds;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;
import uninamo_components.Cable;
import uninamo_components.ComponentType;
import uninamo_components.ConnectorRelay;
import uninamo_components.InputCableConnector;
import uninamo_components.NormalComponent;
import uninamo_components.NormalComponentRelay;
import uninamo_components.OutputCableConnector;
import uninamo_main.UninamoHandlerType;
import uninamo_userinterface.CurrentCostDrawer;

/**
 * The codingInitializer reads the demo component solution from a file and 
 * creates it
 * 
 * @author Mikko Hilpinen
 * @since 17.3.2014
 */
public class CodingInitializer extends ObjectInitializer
{
	// TODO: Extend object constructor instead
	
	// ATTRIBUTES	-----------------------------------------------------
	
	private ConnectorRelay connectorRelay;
	private CurrentCostDrawer costDrawer;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new codingInitializer that also creates other objects
	 * @param handlers The handlers used for the objects
	 * @param connectorRelay The connectorRelay that will keep track of the created connectors
	 * @param costDrawer The drawer that will draw the costs
	 */
	public CodingInitializer(HandlerRelay handlers, ConnectorRelay connectorRelay, 
			CurrentCostDrawer costDrawer)
	{
		super (handlers);
		
		// Initializes attributes
		this.connectorRelay = connectorRelay;
		this.costDrawer = costDrawer;
		
		createObjects();
		
		// After creating the demo components, calculates the 
		// demo component costs
		((NormalComponentRelay) handlers.getHandler(UninamoHandlerType.NORMALCOMPONENT)).calculateDemoCosts();
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected boolean supportsCreationMode(CreationMode mode)
	{
		// Supports components and cables
		return (mode == CreationMode.COMPONENTS || mode == CreationMode.CABLES);
	}

	@Override
	protected void createObjectFromLine(String[] arguments,
			CreationMode currentMode)
	{
		// Components
		if (currentMode == CreationMode.COMPONENTS)
		{
			// Finds the componentType the second argument represents
			ComponentType currentType = null;
			
			for (ComponentType type : ComponentType.values())
			{
				if (type.toString().equalsIgnoreCase(arguments[1]))
				{
					currentType = type;
					break;
				}
			}
			
			// Finds the position
			int x = getArgumentAsInt(arguments[2]);
			int y = getArgumentAsInt(arguments[3]);
			
			// Creates the component
			NormalComponent newComponent = currentType.getNewComponent(getHandlers(), 
					new Vector3D(x, y), this.connectorRelay, this.costDrawer, false);
			
			// Modifies it a bit
			newComponent.setID(arguments[0]);
			newComponent.stopDrag();
		}
		// Cables
		else
		{
			//System.out.println("Cable starts from: " + arguments[0]);
			//System.out.println("Cable ends to: "+ arguments[1]);
			
			// Finds the start and end components
			OutputCableConnector start = 
					(OutputCableConnector) this.connectorRelay.getConnectorWithID(arguments[0]);
			InputCableConnector end = 
					(InputCableConnector) this.connectorRelay.getConnectorWithID(arguments[1]);
			
			// Creates the cable
			new Cable(getHandlers(), this.connectorRelay, start, end, false, 
					Vector3D.zeroVector());
		}
	}
}
