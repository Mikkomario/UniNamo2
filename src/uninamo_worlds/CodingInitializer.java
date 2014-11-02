package uninamo_worlds;

import omega_world.Area;
import uninamo_components.Cable;
import uninamo_components.ComponentType;
import uninamo_components.ConnectorRelay;
import uninamo_components.InputCableConnector;
import uninamo_components.NormalComponent;
import uninamo_components.NormalComponentRelay;
import uninamo_components.OutputCableConnector;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TurnHandler;

/**
 * The codingInitializer reads the demo component solution from a file and 
 * creates it
 * 
 * @author Mikko Hilpinen
 * @since 17.3.2014
 */
public class CodingInitializer extends ObjectInitializer
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Area area;
	private NormalComponentRelay componentRelay;
	private ConnectorRelay connectorRelay;
	private TestHandler testHandler;
	private TurnHandler turnHandler;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new codingInitializer that also creates other objects
	 * 
	 * @param area The area where the objects will be created to
	 * @param componentRelay The componentRelay that will keep track of the 
	 * created demo components
	 * @param connectorRelay The connectorRelay that will keep track of the created connectors
	 * @param testHandler The testHandler that will inform the components about 
	 * test events
	 * @param turnHandler The turnHandler that will inform some of the components 
	 * about turn events
	 */
	public CodingInitializer(Area area, NormalComponentRelay componentRelay, 
			ConnectorRelay connectorRelay, TestHandler testHandler, 
			TurnHandler turnHandler)
	{
		// Initializes attributes
		this.area = area;
		this.componentRelay = componentRelay;
		this.connectorRelay = connectorRelay;
		this.testHandler = testHandler;
		this.turnHandler = turnHandler;
		
		createObjects();
		
		// After creating the demo components, calculates the 
		// demo component costs
		this.componentRelay.calculateDemoCosts();
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
			NormalComponent newComponent = currentType.getNewComponent(
					this.area, x, y, this.testHandler, this.connectorRelay, 
					this.componentRelay, null, this.turnHandler, false);
			
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
			new Cable(this.area, this.testHandler, this.connectorRelay, start, 
					end, false);
		}
	}
}
