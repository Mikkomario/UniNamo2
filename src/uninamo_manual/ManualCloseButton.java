package uninamo_manual;

import java.awt.geom.Point2D.Double;

import uninamo_main.GameSettings;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.DepthConstants;
import utopia_interfaceElements.AbstractButton;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Room;

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
	 * @param drawer The drawableHandler that will draw the button
	 * @param mousehandler The mouseListenerHandler that will inform the button 
	 * about mouse events
	 * @param room The room where the button is located at
	 * @param master The ManualMaster that will be closed when the button is pressed
	 */
	public ManualCloseButton(int x, int y, DrawableHandler drawer, 
			MouseListenerHandler mousehandler, Room room, ManualMaster master)
	{
		super(x, y, DepthConstants.FOREGROUND, 
				MultiMediaHolder.getSpriteBank("manual").getSprite("close"), 
				drawer, mousehandler, room);
		
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
