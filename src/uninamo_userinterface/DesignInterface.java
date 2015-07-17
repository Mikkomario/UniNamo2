package uninamo_userinterface;

import uninamo_gameplaysupport.TestHandler;
import uninamo_main.GameSettings;
import uninamo_main.UninamoHandlerType;
import uninamo_main.Utility;
import exodus_world.AreaBank;
import gateway_ui.AbstractButton;
import genesis_event.HandlerRelay;
import genesis_util.Vector3D;

/**
 * This interface controls all interface elements used in the default design view
 * @author Mikko Hilpinen
 * @since 14.7.2015
 */
public class DesignInterface extends AreaInterface
{
	// ATTRIBUTES	---------------------
	
	private HandlerRelay codingHandlers;
	private AbstractButton finishButton;
	
	
	// CONSTRUCTOR	---------------------
	
	/**
	 * Creates a new interface for the design view
	 * @param designHandlers The handlers that handle the design area's objects
	 * @param codingHandlers The handlers that handle the coding area.
	 * @param finishButton The finishButton since it should be created already. The ownership 
	 * will move to the interface.
	 */
	public DesignInterface(HandlerRelay designHandlers, HandlerRelay codingHandlers, 
			AbstractButton finishButton)
	{
		super(designHandlers, DesignFunction.values());
		
		this.codingHandlers = codingHandlers;
		this.finishButton = finishButton;
		
		createButtons();
		
		// Also creates a testing button
		TestButton.createButton(designHandlers, GameSettings.resolution.minus(
				new Vector3D(110, 45)), (TestHandler) designHandlers.getHandler(
				UninamoHandlerType.TEST));
	}
	
	
	// IMPLEMENTED METHODS	--------------

	@Override
	protected AbstractButton createButtonForFunction(
			uninamo_userinterface.AreaInterface.Function f)
	{
		if (f == DesignFunction.DEMO)
			return ScalingSpriteButton.createButton(new Vector3D(
					GameSettings.resolution.getFirst() / 2, 80), getHandlers(), "demo");
		else if (f == DesignFunction.TOCODING)
			return ScalingSpriteButton.createButton(GameSettings.resolution.dividedBy(
					new Vector3D(2, 1)), getHandlers(), "transition");
		else if (f == DesignFunction.FINISH)
			return this.finishButton;
		
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
			Utility.setMouseState(this.codingHandlers, true);
			Utility.setVisibleState(this.codingHandlers, true);
			Utility.setMouseState(getHandlers(), false);
			Utility.setVisibleState(getHandlers(), false);
		}
		else if (f == DesignFunction.FINISH)
			AreaBank.getArea("gameplay", "results").start(true);
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
		FINISH;
	}
}
