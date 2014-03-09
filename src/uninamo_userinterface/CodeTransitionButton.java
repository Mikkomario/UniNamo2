package uninamo_userinterface;

import java.awt.geom.Point2D;

import uninamo_main.GameSettings;
import uninamo_worlds.Area;
import uninamo_worlds.AreaChanger;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.DepthConstants;
import utopia_interfaceElements.AbstractButton;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Room;

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
	
	/**
	 * ToDesign takes the user to design area when the button is clicked
	 */
	public static final int TODESING = 1;
	/**
	 * ToCode takes the user to code area when the button is clicked
	 */
	public static final int TOCODE = 2;
	
	private int type;
	private AreaChanger areaChanger;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new CodeTransitionButton with the given type.
	 * 
	 * @param drawer The drawer that will draw the button
	 * @param mousehandler The mouseHandler that will inform the button 
	 * about mouse events
	 * @param room The room where the button resides
	 * @param type The type of action the button should take when it is clicked
	 * @param areaChanger AreaChanger that handles the transition between areas
	 * @see #TOCODE
	 * @see #TODESING
	 */
	public CodeTransitionButton(DrawableHandler drawer, 
			MouseListenerHandler mousehandler, Room room, int type, 
			AreaChanger areaChanger)
	{
		super(GameSettings.screenWidth / 2, 0, DepthConstants.FOREGROUND, 
				MultiMediaHolder.getSpriteBank("gameplayinterface").getSprite(
				"transition"), drawer, mousehandler, room);
		
		// Initializes attributes
		this.type = type;
		this.areaChanger = areaChanger;
		
		// Changes position if needed
		if (this.type == TOCODE)
			setY(GameSettings.screenHeight);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D mousePosition,
			double eventStepTime)
	{	
		if (button != MouseButton.LEFT || eventType != MouseButtonEventType.PRESSED)
			return;
		
		// Enables drawing and mouse of the new area but disables them from 
		// the old one
		Area newArea, oldArea;
		
		//System.out.println(this);
		
		if (this.type == TODESING)
		{
			System.out.println("Transition to desing");
			newArea = this.areaChanger.getArea("design");
			oldArea = this.areaChanger.getArea("coding");
		}
		else
		{
			System.out.println("Transition to coding");
			newArea = this.areaChanger.getArea("coding");
			oldArea = this.areaChanger.getArea("design");
		}
		
		oldArea.disableMouseAndDrawing();
		newArea.returnNormal();
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
		// Scales the object on mouse over
		if (eventType == MousePositionEventType.ENTER)
			setScale(GameSettings.interfaceScaleFactor, 
			GameSettings.interfaceScaleFactor);
		else if (eventType == MousePositionEventType.EXIT)
			setScale(1, 1);
	}
}
