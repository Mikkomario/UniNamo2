package uninamo_userinterface;

import java.util.ArrayList;
import java.util.List;

import omega_util.SimpleGameObject;
import exodus_world.Area;
import exodus_world.AreaBank;
import exodus_world.AreaListener;
import gateway_event.ButtonEvent;
import gateway_event.ButtonEventListener;
import gateway_event.ButtonEvent.ButtonEventType;
import gateway_ui.AbstractButton;
import genesis_event.DrawableHandler;
import genesis_event.EventSelector;
import genesis_event.GenesisHandlerType;
import genesis_event.HandlerRelay;
import genesis_event.MouseListenerHandler;
import genesis_util.StateOperator;
import genesis_util.StateOperatorListener;

/**
 * This interface controls all interface elements used in the default design view
 * @author Mikko Hilpinen
 * @since 14.7.2015
 */
public class DesignInterface extends SimpleGameObject implements AreaListener,
		ButtonEventListener, StateOperatorListener
{
	// ATTRIBUTES	---------------------
	
	private List<AbstractButton> buttons;
	private List<Function> buttonFunctions;
	private EventSelector<ButtonEvent> selector;
	// TODO: Could create a map for these handlers
	private HandlerRelay codingHandlers, designHandlers;
	
	
	// CONSTRUCTOR	---------------------
	
	/**
	 * Creates a new interface for the design view
	 * @param designHandlers The handlers that handle the design area's objects
	 * @param codingHandlers The handlers that handle the coding area.
	 */
	public DesignInterface(HandlerRelay designHandlers, HandlerRelay codingHandlers)
	{
		super(designHandlers);
		
		this.designHandlers = designHandlers;
		this.codingHandlers = codingHandlers;
		this.buttons = new ArrayList<>();
		this.buttonFunctions = new ArrayList<>();
		this.selector = ButtonEvent.createButtonEventSelector(ButtonEventType.RELEASED);
		
		// TODO: Create the actual buttons
	}
	
	
	// IMPLEMENTED METHODS	--------------

	@Override
	public void onStateChange(StateOperator source, boolean newState)
	{
		// Kills the buttons when dies
		if (source.equals(getIsDeadStateOperator()) && newState)
		{
			for (AbstractButton button : this.buttons)
			{
				button.getIsDeadStateOperator().setState(newState);
			}
		}
		
		this.buttons.clear();
		this.buttonFunctions.clear();
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
		Function function = getFunctionForButton(e.getSource());
		
		if (function == null)
		{
			System.err.println("How can button function be null?");
			return;
		}
		
		switch (function)
		{
			case TOCODING: 
				// Switches to coding area (keeping some of the processes active)
				// Also kills the demo button
				AbstractButton demoButton = getButtonForFunction(Function.DEMO);
				if (demoButton != null)
				{
					demoButton.getIsDeadStateOperator().setState(true);
					removeButton(Function.DEMO);
				}
				setMouseState(this.codingHandlers, true);
				setVisibleState(this.codingHandlers, true);
				setMouseState(this.designHandlers, false);
				setVisibleState(this.designHandlers, false);
				break;
			case FINISH:
				// TODO: These are probably wrong
				AreaBank.getArea("gameplay", "results").start(true);
				break;
			case NOTE:
				// Kills the note
				e.getSource().getIsDeadStateOperator().setState(true);
				break;
			default: break; // Demo and test buttons handle themselves
		}
	}

	@Override
	public void onAreaStateChange(Area area)
	{
		// Kills the interface, along with any buttons
		if (!area.getIsActiveStateOperator().getState())
			getIsDeadStateOperator().setState(true);
	}
	
	
	// OTHER METHODS	-------------------
	
	private AbstractButton getButtonForFunction(Function f)
	{
		int i = this.buttonFunctions.indexOf(f);
		if (i >= 0)
			return this.buttons.get(i);
		else
			return null;
	}
	
	private Function getFunctionForButton(AbstractButton button)
	{
		int i = this.buttons.indexOf(button);
		if (i >= 0)
			return this.buttonFunctions.get(i);
		else
			return null;
	}
	
	private void removeButton(Function f)
	{
		int i = this.buttonFunctions.indexOf(f);
		this.buttonFunctions.remove(i);
		this.buttons.remove(i);
	}
	
	private static void setMouseState(HandlerRelay relay, boolean newState)
	{
		((MouseListenerHandler) relay.getHandler(GenesisHandlerType.MOUSEHANDLER)
				).getListensToMouseEventsOperator().setState(newState);
	}
	
	private static void setVisibleState(HandlerRelay relay, boolean newState)
	{
		((DrawableHandler) relay.getHandler(GenesisHandlerType.DRAWABLEHANDLER)
				).getIsVisibleStateOperator().setState(newState);
	}
	
	/*
	private static void setTestState(HandlerRelay relay, boolean testing)
	{
		TestEventType type;
		if (testing)
			type = TestEventType.START;
		else
			type = TestEventType.END;
		
		((TestHandler) relay.getHandler(UninamoHandlerType.TEST)).onTestEvent(
				new TestEvent(type));
	}
	*/
	
	// ENUMS	---------------------------
	
	private static enum Function
	{
		TOCODING,
		DEMO,
		FINISH,
		TEST,
		NOTE;
	}
}
