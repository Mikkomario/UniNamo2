package uninamo_userinterface;

import java.awt.geom.Point2D.Double;

import uninamo_gameplaysupport.TestHandler;
import uninamo_main.GameSettings;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.DepthConstants;
import utopia_interfaceElements.AbstractButton;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Room;

/**
 * DemoButton allows testing at the beginning of the stage but disappears as 
 * soon as coding area is initiated.
 * 
 * @author Mikko Hilpinen
 * @since 16.3.2014
 */
public class DemoButton extends AbstractButton
{
	// ATTRIBUTES	-------------------------------------------------------
	
	private boolean testing;
	private TestHandler testHandler;
	
	
	// CONSTRUCTOR	-------------------------------------------------------
	
	/**
	 * Creates a new demoButton to the given position
	 * 
	 * @param x The x-coordinate of the button
	 * @param y The y-coordinate of the button
	 * @param drawer The drawableHandler that will draw the button
	 * @param mousehandler The mouseHandler that will inform the object about 
	 * mouse events
	 * @param room The room where the button is located at
	 * @param testHandler The testHandler that will be informed about test 
	 * (= demo) events
	 */
	public DemoButton(int x, int y, DrawableHandler drawer, 
			MouseListenerHandler mousehandler, Room room, 
			TestHandler testHandler)
	{
		super(x, y, DepthConstants.HUD, 
				MultiMediaHolder.getSpriteBank("gameplayinterface").getSprite("demo"), 
				drawer, mousehandler, room);
		
		// Initializes attributes
		this.testHandler = testHandler;
		this.testing = false;
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Double mousePosition,
			double eventStepTime)
	{
		// Starts demoing / testing on left click (or stops if already testing)
		if (button == MouseButton.LEFT && eventType == MouseButtonEventType.PRESSED)
		{
			if (this.testing)
			{
				this.testHandler.onTestEnd();
				this.testing = false;
				getSpriteDrawer().setImageIndex(0);
			}
			else
			{
				this.testHandler.onTestStart();
				this.testing = true;
				getSpriteDrawer().setImageIndex(1);
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
			Double mousePosition, double eventStepTime)
	{
		// On enter, scales, on exit rescales
		if (eventType == MousePositionEventType.ENTER)
			setScale(GameSettings.interfaceScaleFactor, GameSettings.interfaceScaleFactor);
		else if (eventType == MousePositionEventType.EXIT)
			setScale(1, 1);
	}
}
