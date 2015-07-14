package uninamo_userinterface;

import java.awt.geom.Point2D;

import uninamo_components.NormalComponentRelay;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TestListener;
import uninamo_main.GameSettings;
import uninamo_previous.AreaChanger;

/**
 * ToCodeButton is a codeTransitionButton which also handles some additional 
 * functions when first clicked (removes the demo button and adds the test 
 * button for example)
 * 
 * @author Mikko Hilpinen
 * @since 16.3.2014
 * @deprecated Replace with a simple button
 */
public class ToCodeButton extends CodeTransitionButton implements TestListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private TestingButton testButton;
	private TestHandler testHandler;
	private DemoButton demoButton;
	private NormalComponentRelay demoComponentRelay;
	private boolean hasBeenClicked;
	private AreaChanger areaChanger;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new toCodeButton
	 * 
	 * @param areaChanger The areaChanger that handles the different areas
	 * @param testHandler The testHandler that will inform the button about 
	 * demo events
	 * @param testingButton The testinfButton that will be shown after the first 
	 * click 
	 * @param demoButton The demoButton which will be removed after the first 
	 * transition
	 * @param demoComponentRelay The componentRelay that holds the components 
	 * used to demo the stage
	 */
	public ToCodeButton(AreaChanger areaChanger, TestHandler testHandler,
			TestingButton testingButton, DemoButton demoButton, 
			NormalComponentRelay demoComponentRelay)
	{
		super(GameSettings.screenHeight, areaChanger.getArea("design"), 
				areaChanger.getArea("coding"));
		
		// Initializes attributes
		this.testButton = testingButton;
		this.testHandler = testHandler;
		this.demoButton = demoButton;
		this.demoComponentRelay = demoComponentRelay;
		this.hasBeenClicked = false;
		this.areaChanger = areaChanger;
		
		// Adds the object to the handler(s)
		if (testHandler != null)
			testHandler.addTestable(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D.Double mousePosition,
			double eventStepTime)
	{
		// If this is the first click, removes the demoButton, adds the testButton and 
		// stops listening to the test events
		if (!this.hasBeenClicked && button == MouseButton.LEFT && 
				eventType == MouseButtonEventType.PRESSED)
		{
			this.hasBeenClicked = true;
			
			// Ends the mission area as well
			this.areaChanger.getArea("mission").end();
			
			if (this.demoButton != null)
			{
				this.demoButton.kill();
				this.demoButton = null;
			}
			if (this.testHandler != null)
			{
				this.testHandler.removeHandled(this);
				this.testHandler = null;
			}
			if (this.testButton != null)
			{
				this.testButton.setVisible();
				this.testButton.activate();
				this.testButton = null;
			}
			if (this.demoComponentRelay != null)
			{
				this.demoComponentRelay.killAllComponents();
				this.demoComponentRelay = null;
			}
		}
		
		super.onMouseButtonEvent(button, eventType, mousePosition, eventStepTime);
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
