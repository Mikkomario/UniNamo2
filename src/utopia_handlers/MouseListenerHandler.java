package utopia_handlers;

import java.awt.geom.Point2D;

import utopia_listeners.AdvancedMouseListener;

/**
 * Informs multiple mouselisteners about the mouse's movements and button status
 *
 * @author Mikko Hilpinen.
 *         Created 28.12.2012.
 */
public class MouseListenerHandler extends AbstractMouseListenerHandler 
	implements AdvancedMouseListener
{
	// CONSTRUCTOR	-------------------------------------------------------
	
	/**
	 * Creates a new empty mouselistenerhandler
	 *
	 * @param autodeath Will the handler die when it runs out of listeners
	 * @param actorhandler The actorhandler that will handle this handler 
	 * (optional)
	 * @param superhandler The mouselistenerhandler that will inform this 
	 * handler (optional)
	 */
	public MouseListenerHandler(boolean autodeath, 
			ActorHandler actorhandler, MouseListenerHandler superhandler)
	{
		super(autodeath, actorhandler);
		
		// Tries to add the object to the second handler
		if (superhandler != null)
			superhandler.addMouseListener(this);
	}
	
	
	// IMPLEMENTED METHODS	-------------------------------------------------

	@Override
	public boolean listensMouseEnterExit()
	{
		// Does not have a special area of interest
		return false;
	}

	@Override
	public void onMouseMove(Point2D mousePosition)
	{
		// Updates mouse status
		setMousePosition(mousePosition);
	}

	@Override
	public MouseButtonEventScale getCurrentButtonScaleOfInterest()
	{
		// Handlers are interested in all button events
		return MouseButtonEventScale.GLOBAL;
	}

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D mousePosition,
			double eventStepTime)
	{
		// TODO: Change this old system into a more elegant one
		
		// Updates the mouse status
		if (eventType == MouseButtonEventType.PRESSED)
		{
			if (button == MouseButton.LEFT)
				setLeftMouseDown(true);
			else
				setRightMouseDown(true);
		}
		else if (eventType == MouseButtonEventType.RELEASED)
		{
			if (button == MouseButton.LEFT)
				setLeftMouseDown(false);
			else
				setRightMouseDown(false);
		}
	}

	@Override
	public boolean listensPosition(Point2D testedPosition)
	{
		// Handlers are interested in all areas
		return true;
	}


	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Point2D mousePosition, double eventStepTime)
	{
		// Doesn't react to mouse position events
	}
}
