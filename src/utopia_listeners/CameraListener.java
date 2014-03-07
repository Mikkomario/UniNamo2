package utopia_listeners;

import utopia_handleds.LogicalHandled;

/**
 * Cameralisteners are interested in camera's position and movement and should 
 * be informed when changes in the former happen
 *
 * @author Mikko Hilpinen.
 *         Created 7.12.2012.
 * @see utopia_camera.BasicCamera
 * @warning This class may become deprecated in the near future if a 
 * transformationListener class is added
 * @deprecated TransformationListener should be used instead of this class
 */
public interface CameraListener extends LogicalHandled
{	
	/**
	 * This method should be called when the camera's status changes and is used 
	 * to keep the object in time with the camera's movement and/or position.
	 *
	 * @param posx The camera's current x-coordinate (pixels)
	 * @param posy The camera's current y-coordinate (pixels)
	 * @param w The width of the camera's area (pixels, includes scaling)
	 * @param h The height of the camera's area (pixels, includes scaling)
	 * @param angle The angle of the camera (degrees)
	 */
	public void informCameraPosition(int posx, int posy, int w, int h, int angle);
}
