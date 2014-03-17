package uninamo_components;

import utopia_handleds.Handled;
import utopia_handlers.Handler;
import utopia_listeners.RoomListener;
import utopia_worlds.Room;

/**
 * ComponentRelay keeps track of all the created components 
 * (not in the manual though) and provides data about them. The relay doesn't 
 * support machine components, only normal components.
 * 
 * @author Mikko Hilpinen
 * @since 16.3.2014
 */
public class NormalComponentRelay extends Handler implements RoomListener
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new empty componentRelay
	 */
	public NormalComponentRelay()
	{
		super(false, null);
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return NormalComponent.class;
	}

	@Override
	protected boolean handleObject(Handled h)
	{
		// Kills the component (part of the killComponents functionality)
		h.kill();
		return true;
	}
	
	@Override
	public void onRoomStart(Room room)
	{
		// Creates a new componentCostDrawer
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Kills the componentCostDrawer
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Kills all the currently living normalComponents
	 */
	public void killAllComponents()
	{
		handleObjects();
	}
	
	/**
	 * Adds a new component to the relay
	 * @param c The component to be added to the relay
	 */
	public void addComponent(NormalComponent c)
	{
		addHandled(c);
	}
	
	// TODO: Add handlingOperator for component cost calculations
}
