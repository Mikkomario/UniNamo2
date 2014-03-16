package uninamo_components;

import utopia_handleds.Handled;
import utopia_handlers.Handler;

/**
 * ComponentRelay keeps track of all the created components 
 * (not in the manual though) and provides data about them. The relay doesn't 
 * support machine components, only normal components.
 * 
 * @author Mikko Hilpinen
 * @since 16.3.2014
 */
public class NormalComponentRelay extends Handler
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
