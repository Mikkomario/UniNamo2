package uninamo_userinterface;

import java.awt.geom.Point2D;

import uninamo_main.GameSettings;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.DepthConstants;
import utopia_interfaceElements.AbstractButton;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Room;

/**
 * The user uses the testingButton to move between the testing phase and 
 * creation phase. Testing phase is used to see the results of one's creations 
 * while they be edited in the creation mode.
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public class TestingButton extends AbstractButton
{
	// ATTRIBUTES	------------------------------------------------------
	
	private boolean testing;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new testingButton to the given position
	 * 
	 * @param x The x-coordinate of the button
	 * @param y The y-coordinate of the button
	 * @param drawer The drawableHandler that will draw the button
	 * @param mousehandler The mouseListenerHandler that will inform the 
	 * button about mouse events
	 * @param room The room where the button resides at
	 */
	public TestingButton(int x, int y, DrawableHandler drawer, 
			MouseListenerHandler mousehandler, Room room)
	{
		super(x, y, DepthConstants.FOREGROUND, 
				MultiMediaHolder.getSpriteBank(
				"gameplayinterface").getSprite("testing"), drawer, 
				mousehandler, room);
		
		// Initializes attributes
		this.testing = false;
		
		getSpriteDrawer().setImageSpeed(0);
		getSpriteDrawer().setImageIndex(0);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D mousePosition,
			double eventStepTime)
	{
		// On left mouse click, starts or ends the test mode
		if (button == MouseButton.LEFT && 
				eventType == MouseButtonEventType.PRESSED)
		{
			if (this.testing)
			{
				// Ends the test
				this.testing = false;
				getSpriteDrawer().setImageIndex(0);
				// TODO: End testing
			}
			else
			{
				// Starts the test
				this.testing = true;
				getSpriteDrawer().setImageIndex(1);
				// TODO: Start testing
			}
		}
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		return true;
	}

	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Point2D mousePosition, double eventStepTime)
	{
		// On mouse enter largens, on mouse exit rescales back
		if (eventType == MousePositionEventType.ENTER)
			setScale(GameSettings.interfaceScaleFactor, 
					GameSettings.interfaceScaleFactor);
		else if (eventType == MousePositionEventType.EXIT)
			setScale(1, 1);
	}
}
