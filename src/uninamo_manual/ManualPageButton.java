package uninamo_manual;

import java.awt.geom.Point2D.Double;

import uninamo_main.GameSettings;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.DepthConstants;
import utopia_interfaceElements.AbstractButton;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Room;

/**
 * ManualPageButton lets the user move forwards and backwards in the manual
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public class ManualPageButton extends AbstractButton
{
	// ATTRIBUTES	------------------------------------------------------
	
	private ManualMaster master;
	private int direction;
	
	/**
	 * Forwards is towards the end of the manual
	 */
	public static final int FORWARD = 1;
	/**
	 * Backwards is towards the beginning of the manual
	 */
	public static final int BACWARD = -1;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	/**
	 * Creates a new button to the given position
	 * 
	 * @param direction The direction towards which the button will take the 
	 * user. (either forwards or backwards)
	 * @param drawer The drawableHandler that will draw the button
	 * @param mousehandler The mouseListenerHandler that will inform the button 
	 * about mouse events
	 * @param room The room where the button is located at
	 * @param master The manualMaster that handles the manual's pages
	 * 
	 * @see #FORWARD
	 * @see #BACWARD
	 */
	public ManualPageButton(int direction, DrawableHandler drawer, 
			MouseListenerHandler mousehandler, Room room, ManualMaster master)
	{
		super(0, 0, DepthConstants.FOREGROUND, 
				MultiMediaHolder.getSpriteBank("manual").getSprite("arrow"), 
				drawer, mousehandler, room);
		
		// Initializes attributes
		this.master = master;
		this.direction = direction;
		
		// Sets the position and scaling
		setPosition(GameSettings.screenWidth / 2 + 
				ManualMaster.MANUALWIDTH / 2 * direction + 20 * direction, 
				GameSettings.screenHeight / 2);
		setScale(this.direction, 1);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Double mousePosition,
			double eventStepTime)
	{
		// On mouse press, moves either forward or backwards on the manual
		if (button == MouseButton.LEFT && eventType == MouseButtonEventType.PRESSED)
		{
			if (this.direction == FORWARD)
				this.master.openNextPages();
			else
				this.master.openPreviousPages();
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
		// Scales or rescales
		if (eventType == MousePositionEventType.ENTER)
			setScale(GameSettings.interfaceScaleFactor * this.direction, 
					GameSettings.interfaceScaleFactor);
		else if (eventType == MousePositionEventType.EXIT)
			setScale(this.direction, 1);
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * This method should be called when the manual's page status changes
	 */
	public void onPageChange()
	{
		if (this.direction == FORWARD)
		{
			if (this.master.nextPagesLeft())
			{
				setVisible();
				activate();
			}
			else
			{
				setInvisible();
				inactivate();
			}
		}
		else
		{
			if (this.master.previousPagesLeft())
			{
				setVisible();
				activate();
			}
			else
			{
				setInvisible();
				inactivate();
			}
		}
	}
}
