package uninamo_userinterface;

import java.awt.geom.Point2D;

import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.Testable;
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
public class TestingButton extends AbstractButton implements Testable
{
	// ATTRIBUTES	------------------------------------------------------
	
	private boolean testing;
	private TestHandler testHandler;
	
	
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
	 * @param testHandler The testHandler that is used to inform objects about 
	 * produced test events
	 */
	public TestingButton(int x, int y, DrawableHandler drawer, 
			MouseListenerHandler mousehandler, Room room, 
			TestHandler testHandler)
	{
		super(x, y, DepthConstants.FOREGROUND, 
				MultiMediaHolder.getSpriteBank(
				"gameplayinterface").getSprite("testing"), drawer, 
				mousehandler, room);
		
		// Initializes attributes
		this.testing = false;
		this.testHandler = testHandler;
		
		getSpriteDrawer().setImageSpeed(0);
		getSpriteDrawer().setImageIndex(0);
		
		// Adds the object to the handler(s)
		if (testHandler != null)
			testHandler.addTestable(this);
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
				this.testHandler.endTesting();
			else
				this.testHandler.startTesting();
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

	@Override
	public void startTesting()
	{
		this.testing = true;
		getSpriteDrawer().setImageIndex(1);
	}

	@Override
	public void endTesting()
	{
		this.testing = false;
		getSpriteDrawer().setImageIndex(0);
	}
}
