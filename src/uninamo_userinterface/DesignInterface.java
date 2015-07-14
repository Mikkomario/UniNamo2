package uninamo_userinterface;

import java.util.HashMap;
import java.util.Map;

import omega_util.SimpleGameObject;
import uninamo_gameplaysupport.TestEvent;
import uninamo_gameplaysupport.UninamoHandlerType;
import uninamo_gameplaysupport.TestEvent.TestEventType;
import uninamo_gameplaysupport.TestHandler;
import vision_sprite.SingleSpriteDrawer;
import exodus_world.Area;
import exodus_world.AreaBank;
import exodus_world.AreaListener;
import gateway_event.ButtonEvent;
import gateway_event.ButtonEventListener;
import gateway_event.ButtonEvent.ButtonEventType;
import gateway_ui.AbstractButton;
import gateway_ui.AbstractSpriteButton;
import genesis_event.DrawableHandler;
import genesis_event.EventSelector;
import genesis_event.GenesisHandlerType;
import genesis_event.HandlerRelay;
import genesis_event.MouseListenerHandler;
import genesis_util.StateOperator;

/**
 * This interface controls all interface elements used in the default design view
 * @author Mikko Hilpinen
 * @since 14.7.2015
 */
public class DesignInterface extends SimpleGameObject implements AreaListener,
		ButtonEventListener
{
	// ATTRIBUTES	---------------------
	
	private Map<AbstractButton, Function> buttons;
	private EventSelector<ButtonEvent> selector;
	// TODO: Could create a map for these handlers
	private HandlerRelay codingHandlers, designHandlers;
	private boolean testing;
	
	
	// CONSTRUCTOR	---------------------
	
	public DesignInterface(HandlerRelay designHandlers, HandlerRelay codingHandlers)
	{
		super(designHandlers);
		
		this.designHandlers = designHandlers;
		this.codingHandlers = codingHandlers;
		this.buttons = new HashMap<>();
		this.selector = ButtonEvent.createButtonEventSelector(ButtonEventType.RELEASED);
		this.testing = false;
	}
	
	
	// IMPLEMENTED METHODS	--------------

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
		Function function = this.buttons.get(e.getSource());
		
		if (function == null)
		{
			System.err.println("How can button function be null?");
			return;
		}
		
		switch (function)
		{
			case TOCODING: 
				// Switches to coding area (keeping some of the processes active)
				setMouseState(this.codingHandlers, true);
				setVisibleState(this.codingHandlers, true);
				setMouseState(this.designHandlers, false);
				setVisibleState(this.designHandlers, false);
				break;
			case DEMO:
				// TODO: Change this to a separate demo button class since it will have to 
				// implement victoryCondition anyway
				// Changes demo button look & starts & ends tests
				this.testing = !this.testing;
				if (e.getSource() instanceof AbstractSpriteButton<?>)
					((AbstractSpriteButton<?>) e.getSource()).getSpriteDrawer().setImageIndex(
							this.testing ? 1 : 0);
				setTestState(this.designHandlers, this.testing);
				setTestState(this.codingHandlers, this.testing);
				break;
			case FINISH:
				// TODO: These are probably wrong
				AreaBank.getArea("gameplay", "results").start(true);
				break;
		}
	}

	@Override
	public void onAreaStateChange(Area area)
	{
		// TODO Auto-generated method stub

	}
	
	
	// OTHER METHODS	-------------------
	
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
