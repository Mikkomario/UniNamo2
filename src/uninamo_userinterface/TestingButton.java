package uninamo_userinterface;

import gateway_interface.AbstractButton;
import genesis_graphic.DepthConstants;

import java.awt.geom.Point2D;

import omega_graphic.OpenSpriteBank;
import omega_world.Area;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TestListener;
import uninamo_main.GameSettings;

/**
 * The user uses the testingButton to move between the testing phase and 
 * creation phase. Testing phase is used to see the results of one's creations 
 * while they be edited in the creation mode.
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 * @deprecated Replace with a simple button
 */
public class TestingButton extends AbstractButton implements TestListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private boolean testing;
	private TestHandler testHandler;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new testingButton to the given position
	 * 
	 * @param area The area where the object will reside at
	 * @param x The x-coordinate of the button
	 * @param y The y-coordinate of the button
	 * @param testHandler The testHandler that is used to inform objects about 
	 * produced test events
	 */
	public TestingButton(Area area, int x, int y, TestHandler testHandler)
	{
		super(x, y, DepthConstants.FOREGROUND, 
				OpenSpriteBank.getSpriteBank(
				"gameplayinterface").getSprite("testing"), area);
		
		// Initializes attributes
		this.testing = false;
		this.testHandler = testHandler;
		
		getSpriteDrawer().setImageSpeed(0);
		getSpriteDrawer().setImageIndex(0);
		
		// Goes to hiding until the button is needed
		setInvisible();
		inactivate();
		
		// Adds the object to the handler(s)
		if (testHandler != null)
			testHandler.addTestable(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D.Double mousePosition,
			double eventStepTime)
	{
		// On left mouse click, starts or ends the test mode
		if (button == MouseButton.LEFT && 
				eventType == MouseButtonEventType.PRESSED)
		{
			if (this.testing)
				this.testHandler.onTestEnd();
			else
				this.testHandler.onTestStart();
		}
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		return true;
	}

	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Point2D.Double mousePosition, double eventStepTime)
	{
		// On mouse enter largens, on mouse exit rescales back
		if (eventType == MousePositionEventType.ENTER)
			setScale(GameSettings.interfaceScaleFactor, 
					GameSettings.interfaceScaleFactor);
		else if (eventType == MousePositionEventType.EXIT)
			setScale(1, 1);
	}

	@Override
	public void onTestStart()
	{
		this.testing = true;
		getSpriteDrawer().setImageIndex(1);
	}

	@Override
	public void onTestEnd()
	{
		this.testing = false;
		getSpriteDrawer().setImageIndex(0);
	}
}
