package uninamo_userinterface;

import uninamo_manual.ManualMaster;
import gateway_ui.AbstractButton;
import genesis_event.HandlerRelay;

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
	}
	
	
	// IMPLEMENTED METHODS	---------------
	
	@Override
	protected AbstractButton createButtonForFunction(Function f)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void executeFunction(Function f)
	{
		if (f == CodingFunction.TODESIGN)
		{
			setMouseState(this.designHandlers, true);
			setVisibleState(this.designHandlers, true);
			setMouseState(getHandlers(), false);
			setVisibleState(getHandlers(), false);
		}
		// TODO: If you want to make manual unavailable during testing, create a new class 
		// for it
		else if (f == CodingFunction.MANUAL)
			new ManualMaster(this.areaChanger, this.turnHandler, this);
	}
	
	
	// ENUMS	----------------------------

	private static enum CodingFunction implements Function
	{
		MANUAL,
		TODESIGN;
	}
}
