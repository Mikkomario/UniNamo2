package utopia_camera;

import utopia_gameobjects.DrawnObject;
import utopia_handleds.Handled;
import utopia_handlers.DrawnObjectHandler;
import utopia_helpAndEnums.DepthConstants;

/**
 * This class follows the camera and draws objects. It only draws objects that 
 * will be shown on screen. The later only works with CollidingDrawnObjects and 
 * dimensionaldrawnobjects since they can be checked.
 *
 * @author Mikko Hilpinen.
 *         Created 16.6.2013.
 * @see BasicCamera
 */
public class CameraDrawer extends DrawnObjectHandler
{
	// ATTRIBUTES	----------------------------------------------------
	
	private BasicCamera camera;
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new cameradrawer. The drawer is not added to any handler 
	 * and must be drawn manually with the drawSelf() method.
	 *
	 * @param autodeath Will the drawer die when it doesn't have anything to 
	 * draw anymore
	 * @param camera The camera that draws the drawer and that is used to check 
	 * which objects should be drawn
	 */
	public CameraDrawer(boolean autodeath, BasicCamera camera)
	{
		super(autodeath, true, DepthConstants.BACK, null);
		
		// Initializes attributes
		this.camera = camera;
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------
	
	@Override
	public boolean handleObject(Handled h)
	{	
		// Only handles (draws) objects that camera says should be drawn
		if (!this.camera.objectShouldBeDrawn((DrawnObject) h))
			return true;
		
		super.handleObject(h);
		
		return true;
	}
}
