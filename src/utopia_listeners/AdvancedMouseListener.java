package utopia_listeners;

import java.awt.geom.Point2D;

import utopia_handleds.LogicalHandled;
import utopia_handlers.MouseListenerHandler;

/**
 * Mouselisteners are interested in the mouse's movements and button presses.<br>
 * Remember to add the object to a MouseListenerHandler
 *
 * @author Mikko Hilpinen.
 *         Created 28.12.2012.
 * @see MouseListenerHandler
 */
public interface AdvancedMouseListener extends LogicalHandled
{
	/**
	 * This method is called each time a mouse button event is caused in the 
	 * object's scope of interest. The parameters specify the type and location 
	 * of the event.
	 * 
	 * @param button The mouse button that caused the event
	 * @param eventType The event caused by the mouse button
	 * @param mousePosition The position where the event occurred
	 * @param eventStepTime How long did the last step take. This tells how 
	 * long the mouse button was held down since the last event for 
	 * example.
	 * @see #getCurrentButtonScaleOfInterest()
	 */
	public void onMouseButtonEvent(MouseButton button, 
			MouseButtonEventType eventType, Point2D mousePosition, 
			double eventStepTime);
	
	/**
	 * Tell's whether the object is interested in clicks at the given position
	 * 
	 * @param testedPosition The position that is being tested for being important
	 * @return Is the object interested if a mouse event occurs in the given 
	 * position
	 */
	public boolean listensPosition(Point2D testedPosition);
	
	/**
	 * @return Should the listener be informed if the mouse enters or exits its 
	 * area of interrest
	 */
	public boolean listensMouseEnterExit();
	
	/**
	 * This method is called when a mouse position event occurs and the object 
	 * is interested in it. The events do not include mouse move event which 
	 * is informed separately
	 * 
	 * @param eventType The type of mouse position event that just occurred
	 * @param mousePosition The current position of the mouse
	 * @param eventStepTime How long did the last step take. This tells how 
	 * long the mouse has hovered over the object since the last event for 
	 * example.
	 * @see #listensMouseEnterExit()
	 */
	public void onMousePositionEvent(MousePositionEventType eventType, 
			Point2D mousePosition, double eventStepTime);
	
	/**
	 * This method is called at each time the mouse was moved and it tells 
	 * the mouse's current position
	 * 
	 * @param newMousePosition the mouse's new position
	 */
	public void onMouseMove(Point2D newMousePosition);
	
	/**
	 * @return On which scale the object is interested in mouse button events. 
	 * This affects what events are informed to the listener.
	 */
	public MouseButtonEventScale getCurrentButtonScaleOfInterest();
	
	
	// ENUMERATIONS	-------------------------------------------------------
	
	/**
	 * MouseButtonEventScale is used to define which mouse button events 
	 * should be informed to the listener and which shouldn't.
	 *
	 * @author Mikko Hilpinen.
	 *         Created 9.12.2013.
	 */
	public enum MouseButtonEventScale
	{
		/**
		 * Local mouse events are events that happen inside the object's area 
		 * of interest.<br>
		 * Should be used if the listener is interested only about a certain area
		 */
		LOCAL, 
		/**
		 * All mouse events are included in the global mouse events.<br>
		 * Should be used if the listener is interested in mouse events 
		 * regardles of mouse position
		 */
		GLOBAL,
		/**
		 * No event is included in this category.<br>
		 * Should be used if the listener is not interested in mouse button 
		 * events at all.
		 */
		NONE;
	}
	
	/**
	 * MouseButton represents the usual buttons in the mouse. Mouse button 
	 * events are always tied to one of those buttons.
	 * 
	 * @author Mikko Hilpinen.
	 * Created 10.1.2014
	 */
	public enum MouseButton
	{
		/**
		 * The left mouse button
		 */
		LEFT, 
		/**
		 * The right mouse button
		 */
		RIGHT;
	}
	
	/**
	 * MouseButtonEventType tells which kind of mouse button event is in 
	 * question.
	 * 
	 * @author Mikko Hilpinen.
	 * Created 10.1.2014
	 */
	public enum MouseButtonEventType
	{
		/**
		 * A mouse button is being held down. This kind of event is caused 
		 * continuously until the button is released.
		 */
		DOWN, 
		/**
		 * A mouse button was just pressed down. This kind of event is caused 
		 * only once per each button press
		 */
		PRESSED,
		/**
		 * A mouse button was just released and is no longer being held down. 
		 * This kind of event is caused only once per each button release
		 */
		RELEASED;
	}
	
	/**
	 * There are different types of mouse position events the object can 
	 * react to. An event is caused when the mouse enters the area of interest 
	 * of a certain object, when the mouse leaves that area and while the 
	 * mouse is over that area.
	 * 
	 * @author Gandalf
	 * @notice These kind of events are only informed if the object is 
	 * interested in them
	 * @see AdvancedMouseListener#listensMouseEnterExit()
	 */
	public enum MousePositionEventType
	{
		/**
		 * This event is created when the mouse enters the area of interest 
		 * of the listener
		 */
		ENTER, 
		/**
		 * This event is created when the mouse exits the area of interest of 
		 * the listener
		 */
		EXIT, 
		/**
		 * This event is continuously caused while the mouse hovers over the 
		 * area of interest of the object
		 */
		OVER;
	}
}
