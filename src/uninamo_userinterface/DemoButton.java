package uninamo_userinterface;

import java.awt.geom.Point2D.Double;

import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.VictoryCondition;
import uninamo_gameplaysupport.VictoryHandler;
import uninamo_main.GameSettings;
import utopia_helpAndEnums.DepthConstants;
import utopia_interfaceElements.AbstractButton;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;

/**
 * DemoButton allows testing at the beginning of the stage but disappears as 
 * soon as coding area is initiated.
 * 
 * @author Mikko Hilpinen
 * @since 16.3.2014
 */
public class DemoButton extends AbstractButton implements VictoryCondition
{
	// ATTRIBUTES	-------------------------------------------------------
	
	private boolean testing;
	private TestHandler testHandler;
	
	
	// CONSTRUCTOR	-------------------------------------------------------
	
	/**
	 * Creates a new demoButton to the given position
	 * 
	 * @param area The area where the object will reside at
	 * @param x The x-coordinate of the button
	 * @param y The y-coordinate of the button
	 * @param testHandler The testHandler that will be informed about test 
	 * (= demo) events
	 * @param victoryHandler The victoryHandler that is affected by the condition
	 */
	public DemoButton(Area area, int x, int y, 
			TestHandler testHandler, VictoryHandler victoryHandler)
	{
		super(x, y, DepthConstants.HUD, 
				MultiMediaHolder.getSpriteBank("gameplayinterface").getSprite("demo"), 
				area);
		
		// Initializes attributes
		this.testHandler = testHandler;
		this.testing = false;
		
		// Adds the object to the handler
		if (victoryHandler != null)
			victoryHandler.addVictoryCondition(this);
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

	@Override
	public boolean isClear()
	{
		// The existence of demoButton makes winning the game impossible
		return false;
	}
}
