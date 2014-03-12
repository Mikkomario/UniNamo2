package uninamo_userinterface;

import java.awt.geom.Point2D.Double;

import uninamo_gameplaysupport.TestListener;
import uninamo_main.GameSettings;
import uninamo_manual.ManualMaster;
import uninamo_worlds.AreaChanger;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.DepthConstants;
import utopia_interfaceElements.AbstractButton;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Room;

/**
 * ManualButton takes the user to coding manual when clicked.
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public class ManualButton extends AbstractButton implements TestListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private AreaChanger areaChanger;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new manual button to the given position
	 * 
	 * @param x The x-coordinate of the manual button
	 * @param y The y-coordinate of the manual button
	 * @param drawer The drawableHandler that will draw the button
	 * @param mousehandler The mouseListenerHandler that will inform the button 
	 * about mouse events
	 * @param room The room where the button will be staying
	 * @param areaChanger The areaChanger that handles the transitions between areas
	 */
	public ManualButton(int x, int y, DrawableHandler drawer, 
			MouseListenerHandler mousehandler, Room room, AreaChanger areaChanger)
	{
		super(x, y, DepthConstants.FOREGROUND, 
				MultiMediaHolder.getSpriteBank("gameplayinterface").getSprite(
				"manual"), drawer, mousehandler, room);
		
		// Initializes attributes
		this.areaChanger = areaChanger;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Double mousePosition,
			double eventStepTime)
	{
		// On left click goes to the manual
		if (button == MouseButton.LEFT && eventType == MouseButtonEventType.PRESSED)
		{
			System.out.println("Goes to manual");
			// TODO: Go to manual
			new ManualMaster(this.areaChanger);
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
}
