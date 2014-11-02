package uninamo_manual;

import gateway_interface.AbstractButton;
import genesis_graphic.DepthConstants;

import java.awt.geom.Point2D.Double;

import omega_graphic.OpenSpriteBank;
import omega_world.Area;
import uninamo_main.GameSettings;

/**
 * ManualCloseButton closes the manual when clicked
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public class ManualCloseButton extends AbstractButton
{
	// ATTRIBUTES	------------------------------------------------------
	
	private ManualMaster master;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new closebutton to the given position
	 * 
	 * @param x The button's x-coordinate
	 * @param y The button's y-coordinate
	 * @param master The ManualMaster that will be closed when the button is pressed
	 * @param area The area where the object will reside at
	 */
	public ManualCloseButton(int x, int y, ManualMaster master, Area area)
	{
		super(x, y, DepthConstants.FOREGROUND, 
				OpenSpriteBank.getSpriteBank("manual").getSprite("close"), 
				area);
		
		// Initializes attributes
		this.master = master;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Double mousePosition,
			double eventStepTime)
	{
		// Closes the manual when clicked
		if (button == MouseButton.LEFT && eventType == MouseButtonEventType.PRESSED)
			this.master.kill();
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		return true;
	}

	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Double mousePosition, double eventStepTime)
	{
		// On enter, scales, on exit, rescales
		if (eventType == MousePositionEventType.ENTER)
			setScale(GameSettings.interfaceScaleFactor, 
					GameSettings.interfaceScaleFactor);
		else if (eventType == MousePositionEventType.EXIT)
			setScale(1, 1);
	}
}
