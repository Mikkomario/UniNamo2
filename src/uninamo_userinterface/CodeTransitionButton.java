package uninamo_userinterface;

import java.awt.geom.Point2D;

import uninamo_main.GameSettings;
import utopia_helpAndEnums.DepthConstants;
import utopia_interfaceElements.AbstractButton;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;

/**
 * CodeTransitionButton lets the user switch between the code phase and the 
 * design phase
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class CodeTransitionButton extends AbstractButton
{
	// ATTRIBUTES	------------------------------------------------------

	private Area oldArea, newArea;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new CodeTransitionButton with the given type.
	 * 
	 * @param y The new y-coordinate of the button
	 * @param oldArea The area where the button resides
	 * @param newArea The area where the button will take the user
	 */
	public CodeTransitionButton(int y, Area oldArea, Area newArea)
	{
		super(GameSettings.screenWidth / 2, y, DepthConstants.FOREGROUND, 
				MultiMediaHolder.getSpriteBank("gameplayinterface").getSprite(
				"transition"), oldArea.getDrawer(), oldArea.getMouseHandler(), 
				oldArea);
		
		// Initializes attributes
		this.oldArea = oldArea;
		this.newArea = newArea;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D.Double mousePosition,
			double eventStepTime)
	{	
		if (button != MouseButton.LEFT || eventType != MouseButtonEventType.PRESSED)
			return;
		
		// Enables drawing and mouse of the new area but disables them from 
		// the old one
		this.oldArea.getMouseHandler().inactivate();
		this.oldArea.getDrawer().setInvisible();
		
		this.newArea.getMouseHandler().activate();
		this.newArea.getDrawer().setVisible();
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
		// Scales the object on mouse over
		if (eventType == MousePositionEventType.ENTER)
			setScale(GameSettings.interfaceScaleFactor, 
			GameSettings.interfaceScaleFactor);
		else if (eventType == MousePositionEventType.EXIT)
			setScale(1, 1);
	}
}
