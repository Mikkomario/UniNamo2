package utopia_handlers;


import java.awt.geom.Point2D;

import utopia_camera.BasicCamera;

/**
 * CameraMouseListenerHandler is a mouseListenerHandler that transforms 
 * mouse's coordinates on screen to camera's space. This handler should be 
 * used instead of a normal mouseListenerHandler when using cameras.
 * 
 * @author Mikko Hilpinen
 * Created 21.2.2014
 */
public class CameraMouseListenerHandler extends MouseListenerHandler
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private BasicCamera camera;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new CameraListenerHandler. Use addListener to add handleds 
	 * to the handler.
	 * 
	 * @param autodeath Will the handler die when it runs out of handleds
	 * @param actorhandler The actorhandler that will inform the handler 
	 * about step-events
	 * @param superhandler the MouseListenerHandler that informs the handler 
	 * about mouseEvents
	 * @param camera The camera whose transformations affect the informed 
	 * mouse positions
	 */
	public CameraMouseListenerHandler(boolean autodeath,
			ActorHandler actorhandler, MouseListenerHandler superhandler, 
			BasicCamera camera)
	{
		super(autodeath, actorhandler, superhandler);
		
		// Initializes attributes
		this.camera = camera;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	// Overrides the mousemove to transform the coordinates before informing 
	// them further
	@Override
	public void onMouseMove(Point2D mousePosition)
	{
		super.onMouseMove(this.camera.transform(mousePosition));
	}
	
	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D mousePosition,
			double eventStepTime)
	{
		// Transforms the position before updating
		super.onMouseButtonEvent(button, eventType, 
				this.camera.transform(mousePosition), eventStepTime);
	}
}
