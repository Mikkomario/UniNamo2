package uninamo_userinterface;

import exodus_world.AreaBank;
import gateway_ui.AbstractButton;
import genesis_event.HandlerRelay;

/**
 * This interface controls all interface elements used in the default design view
 * @author Mikko Hilpinen
 * @since 14.7.2015
 */
public class DesignInterface extends AreaInterface
{
	// ATTRIBUTES	---------------------
	
	private HandlerRelay codingHandlers;
	
	
	// CONSTRUCTOR	---------------------
	
	/**
	 * Creates a new interface for the design view
	 * @param designHandlers The handlers that handle the design area's objects
	 * @param codingHandlers The handlers that handle the coding area.
	 */
	public DesignInterface(HandlerRelay designHandlers, HandlerRelay codingHandlers)
	{
		super(designHandlers, DesignFunction.values());
		
		this.codingHandlers = codingHandlers;
		createButtons();
	}
	
	
	// IMPLEMENTED METHODS	--------------

	@Override
	protected AbstractButton createButtonForFunction(
			uninamo_userinterface.AreaInterface.Function f)
	{
		// TODO Complete this method
		return null;
	}

	@Override
	protected void executeFunction(
			uninamo_userinterface.AreaInterface.Function f)
	{
		if (f == DesignFunction.TOCODING)
		{
			// Switches to coding area (keeping some of the processes active)
			// Also kills the demo button
			removeFunction(DesignFunction.DEMO);
			setMouseState(this.codingHandlers, true);
			setVisibleState(this.codingHandlers, true);
			setMouseState(getHandlers(), false);
			setVisibleState(getHandlers(), false);
		}
		else if (f == DesignFunction.FINISH)
			AreaBank.getArea("gameplay", "results").start(true);
		else if (f == DesignFunction.NOTE)
			removeFunction(DesignFunction.NOTE);
	}
	
	
	// OTHER METHODS	-------------------
	
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
	
	private static enum DesignFunction implements AreaInterface.Function
	{
		TOCODING,
		DEMO,
		FINISH,
		TEST,
		NOTE;
	}
}
