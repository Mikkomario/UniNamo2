package uninamo_components;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;
import uninamo_userinterface.CurrentCostDrawer;

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
	// TODO: The sprite name, number of inputs & outputs, etc. should be listed here
	
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
	 * @param handlers The handlers that will handle the component
	 * @param position The component's new position
	 * @param connectorRelay The connectorRelay that will handle the connectors 
	 * of the component
	 * @param costDrawer The costDrawer that will be affected by the component 
	 * (optional)
	 * @param isTestComponent Will the component be used only for testing 
	 * purposes. If so, it doesn't react to mouse but will use test cables
	 * @return A component of this componentType
	 */
	public NormalComponent getNewComponent(HandlerRelay handlers, Vector3D position, 
			ConnectorRelay connectorRelay, CurrentCostDrawer costDrawer, 
			boolean isTestComponent)
	{
		switch (this)
		{
			case OR: return new OrComponent(handlers, position, connectorRelay, 
					costDrawer, isTestComponent);
			case PULSE: return new PulseGeneratorComponent(handlers, position, connectorRelay, 
					costDrawer, isTestComponent);
			case POWER: return new PowerSourceComponent(handlers, position, connectorRelay, 
					costDrawer, isTestComponent);
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
	
	/**
	 * @return How much does a component of this type cost?
	 */
	public double getPrice()
	{
		switch (this)
		{
			case OR: return 0.3;
			case PULSE: return 0.8;
			case POWER: return 0.2;
		}
		
		return 0;
	}
}
