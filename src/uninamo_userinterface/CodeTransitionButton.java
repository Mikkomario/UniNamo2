package uninamo_userinterface;

import java.awt.geom.Point2D;

import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TestListener;
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
public class CodeTransitionButton extends AbstractButton implements TestListener
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
	private TestingButton testButton;
	private TestHandler testHandler;
	private DemoButton demoButton;
	
	// TODO: Consider dividing these into two separate classes since they have 
	// so little in common at this point (though they would be really small classes)
	
	
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
	 * @param testHandler The testHandler that will inform the button about 
	 * demo events (can be freely left null in the coding area)
	 * @param testingButton The testingButton that will be first shown only after 
	 * the first click (can be left null in the coding area)
	 * @param demoButton The demoButton that will be killed after the button 
	 * has been pressed for the first time (null if there is no demoButton)
	 * @see #TOCODE
	 * @see #TODESING
	 */
	public CodeTransitionButton(DrawableHandler drawer, 
			MouseListenerHandler mousehandler, Room room, int type, 
			AreaChanger areaChanger, TestHandler testHandler, 
			TestingButton testingButton, DemoButton demoButton)
	{
		super(GameSettings.screenWidth / 2, 0, DepthConstants.FOREGROUND, 
				MultiMediaHolder.getSpriteBank("gameplayinterface").getSprite(
				"transition"), drawer, mousehandler, room);
		
		// Initializes attributes
		this.type = type;
		this.areaChanger = areaChanger;
		this.testButton = testingButton;
		this.testHandler = testHandler;
		this.demoButton = demoButton;
		
		// Changes position if needed
		if (this.type == TOCODE)
			setY(GameSettings.screenHeight);
		
		// TODO: Change the sprite index to 2 once there is one
		
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
		if (button != MouseButton.LEFT || eventType != MouseButtonEventType.PRESSED)
			return;
		
		// Enables drawing and mouse of the new area but disables them from 
		// the old one
		Area newArea, oldArea;
		
		//System.out.println(this);
		
		if (this.type == TODESING)
		{
			//System.out.println("Transition to desing");
			newArea = this.areaChanger.getArea("design");
			oldArea = this.areaChanger.getArea("coding");
		}
		else
		{
			//System.out.println("Transition to coding");
			newArea = this.areaChanger.getArea("coding");
			oldArea = this.areaChanger.getArea("design");
			
			// Also removes the mission area
			this.areaChanger.getArea("mission").end();
		}
		
		oldArea.disableMouseAndDrawing();
		newArea.returnNormal();
		
		// Removes the button from the testHandler if it even was there
		if (this.testHandler != null)
		{
			this.testHandler.removeHandled(this);
			this.testHandler = null;
		}
		
		// If this was the first click, activates the testButton
		if (this.testButton != null)
		{
			this.testButton.activate();
			this.testButton.setVisible();
			this.testButton = null;
		}
		
		// Also kills the demoButton if not dead already
		if (this.demoButton != null)
		{
			this.demoButton.kill();
			this.demoButton = null;
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
		// Scales the object on mouse over
		if (eventType == MousePositionEventType.ENTER)
			setScale(GameSettings.interfaceScaleFactor, 
			GameSettings.interfaceScaleFactor);
		else if (eventType == MousePositionEventType.EXIT)
			setScale(1, 1);
	}

	@Override
	public void onTestStart()
	{
		// As the coding hasn't been started yet (= demo is running), 
		// goes invisible and unusable (to not spoil the answer)
		setInvisible();
		inactivate();
	}

	@Override
	public void onTestEnd()
	{
		// As this is called only in the beginning, it means that the demo 
		// has ended and the button can come back
		activate();
		setVisible();
	}
}
