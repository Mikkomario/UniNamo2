package uninamo_userinterface;

import java.awt.geom.Point2D.Double;

import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TestListener;
import uninamo_main.GameSettings;
import uninamo_worlds.AreaChanger;
import utopia_helpAndEnums.DepthConstants;
import utopia_interfaceElements.AbstractButton;
import utopia_resourcebanks.MultiMediaHolder;

/**
 * FinishButton is used for finishing the current stage and moving to the results 
 * screen
 * 
 * @author Mikko Hilpinen
 * @since 17.3.2014
 */
public class FinishButton extends AbstractButton implements TestListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	// TODO: Add cost calculations
	private AreaChanger areaChanger;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new finishButton. The button won't be usable until activated & 
	 * set visible
	 * 
	 * @param areaChanger The areaChanger that handles the different areas
	 * @param testHandler The testHandler that will inform the button about test events
	 */
	public FinishButton(AreaChanger areaChanger, TestHandler testHandler)
	{
		super(GameSettings.screenWidth / 2, GameSettings.screenHeight / 2, 
				DepthConstants.FOREGROUND, 
				MultiMediaHolder.getSpriteBank("gameplayinterface").getSprite("finish"), 
				areaChanger.getArea("design").getDrawer(), 
				areaChanger.getArea("design").getMouseHandler(), 
				areaChanger.getArea("design"));
		
		// Initializes attributes
		this.areaChanger = areaChanger;
		
		inactivate();
		setInvisible();
		
		// Adds the object to the handler(s)
		if (testHandler != null)
			testHandler.addTestable(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Double mousePosition,
			double eventStepTime)
	{
		// Changes to the results area
		// TODO: Add stuff
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
		// Scaling
		if (eventType == MousePositionEventType.ENTER)
			setScale(GameSettings.interfaceScaleFactor, GameSettings.interfaceScaleFactor);
		else
			setScale(1, 1);
	}

	@Override
	public void onTestStart()
	{
		// Does nothing
	}

	@Override
	public void onTestEnd()
	{
		// (re)hides the button
		setInvisible();
		inactivate();
	}
}
