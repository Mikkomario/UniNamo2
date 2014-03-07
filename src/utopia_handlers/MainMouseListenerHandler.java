package utopia_handlers;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import utopia_video.GameWindow;

/**
 * This class takes input straight from the gamewindow and informs all 
 * mouselisteners 'below' it. There should be only one 
 * mainmouselistenerhandler created and / or used at a time
 *
 * @author Mikko Hilpinen.
 *         Created 29.12.2012.
 * @see GameWindow
 */
public class MainMouseListenerHandler extends AbstractMouseListenerHandler
{	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new empty mouselistenerhandler. The handler won't die 
	 * automatically
	 * 
	 * @param actorhandler The handler that will handle this handler (optional)
	 */
	public MainMouseListenerHandler(ActorHandler actorhandler)
	{
		super(false, actorhandler);
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Informs the handler about the mouse's current position and button status
	 * 
	 * @param mousePosition The mouse's current position
	 * @param mousePressed Is a mouse button pressed
	 * @param mouseButton Which mouse button is pressed
	 */
	public void setMouseStatus(Point2D mousePosition, boolean mousePressed, 
			int mouseButton)
	{
		setMousePosition(mousePosition);
		
		if (mousePressed)
		{
			if (mouseButton == MouseEvent.BUTTON1)
				setLeftMouseDown(true);
			else if (mouseButton == MouseEvent.BUTTON3)
				setRightMouseDown(true);
		}
		else
		{
			setLeftMouseDown(false);
			setRightMouseDown(false);
		}
	}
}
