package uninamo_gameplay;

import java.awt.geom.Point2D;

import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;

/**
 * OutputCableConnectors take signal events from components and relays them to 
 * multiple cables.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public class OutputCableConnector extends CableConnector
{
	// ATTRIBUTES	------------------------------------------------------
	
	private DrawableHandler drawer;
	private MouseListenerHandler mousehandler;
	private boolean lastSignalStatus;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new outputCableConnector connected to the given component
	 * 
	 * @param relativex The connector's x-coordinate in relation to the 
	 * component's top-left corner (pixels)
	 * @param relativey The connector's x-coordinate in relation to the 
	 * component's top-left corner (pixels)
	 * @param drawer The drawableHandler that will draw the connector
	 * @param mousehandler The MouseListenerHandler that will inform the 
	 * connector about mouse events
	 * @param host The component the connector is tied to
	 */
	public OutputCableConnector(int relativex, int relativey,
			DrawableHandler drawer, MouseListenerHandler mousehandler,
			Component host)
	{
		super(relativex, relativey, drawer, mousehandler, host);
		
		// Initializes attributes
		this.lastSignalStatus = false;
		this.drawer = drawer;
		this.mousehandler = mousehandler;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D mousePosition,
			double eventStepTime)
	{
		// If the connector is clicked with a left mouse button it will create 
		// a new cable
		if (button == MouseButton.LEFT && eventType == 
				MouseButtonEventType.PRESSED)
		{
			new Cable(this.drawer, this.mousehandler, this);
			// Also resets scaling
			rescale();
		}
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		// Only listens to enter / exit if a new cable can be created (one is 
		// not being dragged)
		return !Cable.cableIsBeingDragged;
	}

	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Point2D mousePosition, double eventStepTime)
	{
		// Scales the object on enter, rescales on exit
		if (eventType == MousePositionEventType.ENTER)
			largen();
		else if (eventType == MousePositionEventType.EXIT)
			rescale();
	}

	@Override
	public boolean getSignalStatus()
	{
		return this.lastSignalStatus;
	}

	@Override
	public void onSignalChange(boolean newSignalStatus, SignalRelay source)
	{
		// Updates the last signal status and informs the cables if the status 
		// changed
		if (newSignalStatus != this.lastSignalStatus)
		{
			this.lastSignalStatus = newSignalStatus;
			
			for (int i = 0; i < getCableAmount(); i++)
			{
				getCable(i).onSignalChange(getSignalStatus(), this);
			}
		}
	}

	@Override
	public MouseButtonEventScale getCurrentButtonScaleOfInterest()
	{
		return MouseButtonEventScale.LOCAL;
	}
}
