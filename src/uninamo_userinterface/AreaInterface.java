package uninamo_userinterface;

import java.util.ArrayList;
import java.util.List;

import exodus_world.Area;
import exodus_world.AreaListener;
import gateway_event.ButtonEvent;
import gateway_event.ButtonEventListener;
import gateway_event.ButtonEvent.ButtonEventType;
import gateway_ui.AbstractButton;
import genesis_event.EventSelector;
import genesis_event.HandlerRelay;
import genesis_util.StateOperator;
import genesis_util.StateOperatorListener;
import omega_util.SimpleGameObject;

/**
 * This element creates a set of interface elements and handles some (or all) of their 
 * functionality
 * @author Mikko Hilpinen
 * @since 15.7.2015
 */
public abstract class AreaInterface extends SimpleGameObject implements
		ButtonEventListener, StateOperatorListener, AreaListener
{
	// ATTRIBUTES	---------------------
	
	private List<AbstractButton> buttons;
	private List<Function> buttonFunctions;
	private EventSelector<ButtonEvent> selector;
	private HandlerRelay handlers;
	
	
	// CONSTRUCTOR	---------------------
	
	/**
	 * Creates a new interface. The interface won't become fully initialized before the 
	 * createButtons() method is called
	 * @param handlers The handlers that will handle the interface
	 * @param functions The functions the interface will implement
	 * @see #createButtons()
	 */
	public AreaInterface(HandlerRelay handlers, Function[] functions)
	{
		super(handlers);
		
		this.handlers = handlers;
		this.buttonFunctions = new ArrayList<>();
		this.buttons = null;
		this.selector = ButtonEvent.createButtonEventSelector(ButtonEventType.RELEASED);
		
		for (int i = 0; i < functions.length; i++)
		{
			this.buttonFunctions.add(functions[i]);
		}
		
		getIsDeadStateOperator().getListenerHandler().add(this);
	}
	
	
	// ABSTRACT METHODS	-----------------
	
	/**
	 * This method should create and return a new button that will handle the given function
	 * @param f The function the button will implement
	 * @return A button created for for the function
	 */
	protected abstract AbstractButton createButtonForFunction(Function f);
	
	/**
	 * This method is called when a button representing the given function is pressed. 
	 * The interface should take care that the functionality will be executed properly
	 * @param f The function that should be executed
	 */
	protected abstract void executeFunction(Function f);
	
	
	// IMPLEMENTED METHODS	-------------

	@Override
	public void onAreaStateChange(Area area)
	{
		// Kills the interface, along with any buttons
		if (!area.getIsActiveStateOperator().getState())
			getIsDeadStateOperator().setState(true);
	}

	@Override
	public void onStateChange(StateOperator source, boolean newState)
	{
		// Kills the buttons when dies
		if (source.equals(getIsDeadStateOperator()) && newState)
		{
			if (isInitialized())
			{
				for (AbstractButton button : this.buttons)
				{
					button.getIsDeadStateOperator().setState(newState);
				}
				
				this.buttons.clear();
			}
			
			this.buttonFunctions.clear();
		}
	}

	@Override
	public EventSelector<ButtonEvent> getButtonEventSelector()
	{
		return this.selector;
	}

	@Override
	public StateOperator getListensToButtonEventsOperator()
	{
		return getIsActiveStateOperator();
	}

	@Override
	public void onButtonEvent(ButtonEvent e)
	{	
		if (!isInitialized())
			throw new InterfaceStateException("The button's haven't been created yet");
		
		Function function = getFunctionForButton(e.getSource());
		
		if (function == null)
			throw new InterfaceStateException("No function was found for a button");
		
		executeFunction(function);
	}
	
	
	// GETTERS & SETTERS	--------------
	
	/**
	 * @return The handlers that handle this interface
	 */
	protected HandlerRelay getHandlers()
	{
		return this.handlers;
	}
	
	
	// OTHER METHODS	------------------
	
	/**
	 * This will initialize the buttons in the interface and call 
	 * {@link #createButtonForFunction(Function)} consecutively. The subclass should call 
	 * this method once it has finished it's initialization process.
	 */
	protected void createButtons()
	{
		this.buttons = new ArrayList<>();
		for (Function function : this.buttonFunctions)
		{
			AbstractButton button = createButtonForFunction(function);
			button.getListenerHandler().add(this);
			this.buttons.add(button);
		}
	}
	
	/**
	 * Finds the button that represents the given function
	 * @param f The function the button is (in part) implementing
	 * @return The button that is connected to the function
	 */
	protected AbstractButton getButtonForFunction(Function f)
	{
		int i = this.buttonFunctions.indexOf(f);
		if (i >= 0)
			return this.buttons.get(i);
		else
			return null;
	}
	
	/**
	 * Finds the function the button is supposed to implement
	 * @param button The button that may implement a function
	 * @return The function implemented by the button or null if the button can't be connected 
	 * to a function
	 */
	protected Function getFunctionForButton(AbstractButton button)
	{
		int i = this.buttons.indexOf(button);
		if (i >= 0)
			return this.buttonFunctions.get(i);
		else
			return null;
	}
	
	/**
	 * This method removes a function and the associated button from the interface, 
	 * killing the latter in the process
	 * @param f The function that won't be executed anymore
	 */
	protected void removeFunction(Function f)
	{
		AbstractButton button = getButtonForFunction(f);
		if (button != null)
		{
			int i = this.buttonFunctions.indexOf(f);
			this.buttonFunctions.remove(i);
			this.buttons.remove(i);
			
			button.getIsDeadStateOperator().setState(true);
		}
	}
	
	private boolean isInitialized()
	{
		return this.buttons != null;
	}

	
	// INTERFACES	----------------------
	
	/**
	 * A function represents a function a button in the interface has. There should be 
	 * a function for each button in the interface. Only enumerations should extend this 
	 * interface.
	 * @author Mikko Hilpinen
	 * @since 15.7.2015
	 */
	protected interface Function
	{
		// This is used as a wrapper only
	}
	
	
	// SUBCLASSES	----------------------
	
	private static class InterfaceStateException extends RuntimeException
	{
		private static final long serialVersionUID = 6378665463966902357L;
		
		public InterfaceStateException(String message)
		{
			super(message);
		}
	}
}
