package uninamo_userinterface;

import gateway_interface.AbstractButton;
import genesis_graphic.DepthConstants;

import java.awt.geom.Point2D.Double;

import omega_graphic.OpenSpriteBank;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TestListener;
import uninamo_main.GameSettings;
import uninamo_previous.AreaChanger;

/**
 * FinishButton is used for finishing the current stage and moving to the results 
 * screen
 * 
 * @author Mikko Hilpinen
 * @since 17.3.2014
 * @deprecated Replace with a simple button
 */
public class FinishButton extends AbstractButton implements TestListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	// TODO: Add cost calculations
	private AreaChanger areaChanger;
	private boolean testing;
	
	
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
				OpenSpriteBank.getSpriteBank("gameplayinterface").getSprite("finish"), 
				areaChanger.getArea("design"));
		
		// Initializes attributes
		this.areaChanger = areaChanger;
		this.testing = false;
		
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
		this.areaChanger.getArea("coding").end();
		this.areaChanger.getArea("design").end();
		this.areaChanger.getArea("results").start();
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
		else if (eventType == MousePositionEventType.EXIT)
			setScale(1, 1);
	}

	@Override
	public void onTestStart()
	{
		this.testing = true;
	}

	@Override
	public void onTestEnd()
	{
		// (re)hides the button
		this.testing = false;
		setInvisible();
		inactivate();
	}
	
	@Override
	public void activate()
	{
		// Can only be activated while testing
		if (this.testing)
			super.activate();
	}
	
	@Override
	public void setVisible()
	{
		// Can only be set visible while testing
		if (this.testing)
			super.setVisible();
	}
}
