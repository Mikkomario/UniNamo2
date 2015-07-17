package uninamo_userinterface;

import uninamo_gameplaysupport.TestHandler;
import uninamo_main.GameSettings;
import uninamo_main.UninamoHandlerType;
import uninamo_main.Utility;
import uninamo_manual.ManualMaster;
import gateway_ui.AbstractButton;
import genesis_event.HandlerRelay;
import genesis_util.Vector3D;

/**
 * This element contains all the interface elements used in the coding area (except for the 
 * component boxes)
 * @author Mikko Hilpinen
 * @since 15.7.2015
 */
public class CodingInterface extends AreaInterface
{
	// ATTRIBUTES	-----------------------
	
	private HandlerRelay designHandlers;
	
	
	// CONSTRUCTOR	-----------------------
	
	/**
	 * Creates a new interface
	 * @param codingHandlers The handlers that handle the coding area
	 * @param designHandlers The handlers that handle the design area
	 */
	public CodingInterface(HandlerRelay codingHandlers, HandlerRelay designHandlers)
	{
		super(codingHandlers, CodingFunction.values());
		
		this.designHandlers = designHandlers;
		
		createButtons();
		
		// Also creates a testing button
		TestButton.createButton(codingHandlers, 
				new Vector3D(GameSettings.resolution.getFirst() - 110, 45), 
				(TestHandler) codingHandlers.getHandler(UninamoHandlerType.TEST));
	}
	
	
	// IMPLEMENTED METHODS	---------------
	
	@Override
	protected AbstractButton createButtonForFunction(Function f)
	{
		if (f == CodingFunction.MANUAL)
			return ScalingSpriteButton.createButton(GameSettings.resolution.dividedBy(
					new Vector3D(3, 1)), getHandlers(), "manual");
		else if (f == CodingFunction.TODESIGN)
			return ScalingSpriteButton.createButton(GameSettings.resolution.dividedBy(
					new Vector3D(2, 1)), getHandlers(), "transition");
		
		return null;
	}

	@Override
	protected void executeFunction(Function f)
	{
		if (f == CodingFunction.TODESIGN)
		{
			Utility.setMouseState(this.designHandlers, true);
			Utility.setVisibleState(this.designHandlers, true);
			Utility.setMouseState(getHandlers(), false);
			Utility.setVisibleState(getHandlers(), false);
		}
		// TODO: If you want to make manual unavailable during testing, create a new class 
		// for the button
		else if (f == CodingFunction.MANUAL)
			ManualMaster.openManual(getButtonForFunction(f));
	}
	
	
	// ENUMS	----------------------------

	private static enum CodingFunction implements Function
	{
		MANUAL,
		TODESIGN;
	}
}
