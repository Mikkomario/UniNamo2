package uninamo_userinterface;

import gateway_interface.AbstractButton;
import genesis_graphic.DepthConstants;

import java.awt.geom.Point2D.Double;

import omega_graphic.OpenSpriteBank;
import uninamo_gameplaysupport.TestListener;
import uninamo_gameplaysupport.TurnHandler;
import uninamo_main.GameSettings;
import uninamo_manual.ManualMaster;
import uninamo_previous.AreaChanger;

/**
 * ManualButton takes the user to coding manual when clicked.
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 * @deprecated Replaced with a simple button
 */
public class ManualButton extends AbstractButton implements TestListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private AreaChanger areaChanger;
	private TurnHandler turnHandler;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new manual button to the given position
	 * 
	 * @param x The x-coordinate of the manual button
	 * @param y The y-coordinate of the manual button
	 * @param areaChanger The areaChanger that handles the transitions between areas
	 * @param turnHandler The turnHandler that will inform the manual's objects 
	 * about turn events
	 */
	public ManualButton(int x, int y, 
			AreaChanger areaChanger, TurnHandler turnHandler)
	{
		super(x, y, DepthConstants.FOREGROUND, 
				OpenSpriteBank.getSpriteBank("gameplayinterface").getSprite(
				"manual"), areaChanger.getArea("coding"));
		
		// Initializes attributes
		this.areaChanger = areaChanger;
		this.turnHandler = turnHandler;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Double mousePosition,
			double eventStepTime)
	{
		// On left click goes to the manual
		if (button == MouseButton.LEFT && eventType == MouseButtonEventType.PRESSED)
			new ManualMaster(this.areaChanger, this.turnHandler, this);
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
		// On enter, largens, on exit, rescales
		if (eventType == MousePositionEventType.ENTER)
			setScale(GameSettings.interfaceScaleFactor, GameSettings.interfaceScaleFactor);
		else if (eventType == MousePositionEventType.EXIT)
			setScale(1, 1);
	}

	@Override
	public void onTestStart()
	{
		// Hides
		setInvisible();
		inactivate();
	}

	@Override
	public void onTestEnd()
	{
		// Reappears
		setVisible();
		activate();
	}
	
	/*
	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		super.drawSelfBasic(g2d);
		System.out.println("Draws the manualbutton, which is " + isVisible() + " visible");
	}
	*/
	
	/*
	@Override
	public void setVisible()
	{
		System.out.println("Sets the manualButton back to visible");
		super.setVisible();
	}
	*/
}
