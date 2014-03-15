package utopia_camera;

import utopia_gameobjects.BasicPhysicDrawnObject;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;

/**
 * This camera follows a given object and zooms out when the speed of the object 
 * increases
 *
 * @author Mikko Hilpinen & Unto Solala.
 *         Created 24.8.2013.
 */
public class ZoomingFollowerCamera extends FollowerCamera
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private double minimumspeed;
	private double zoommodifier;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new zoomingfollowercamera with the given information. The camera 
	 * starts at the position of the followed object
	 *
	 * @param drawer The drawablehandler that will draw the camera and 
	 * objects it shows
	 * @param actorhandler The actorhandler that will inform the camera of the 
	 * act-event
	 * @param screenWidth The width of the screen
	 * @param screenHeight The height of the screen
	 * @param depthLayers How many layers of depth handling there should be. 
	 * The less the content's depth changes, the more there should be. [1, 6]
	 * @param followed The followed PhysicDrawnObject
	 * @param minimumzoomspeed How much speed the followed object has to have 
	 * before the camera starts to zoom out (0+)
	 * @param zoommodifier How fast the camera's zoom increases and decreases 
	 * (0+, default 1.0)
	 */
	public ZoomingFollowerCamera(DrawableHandler drawer,
			ActorHandler actorhandler, int screenWidth, int screenHeight, 
			int depthLayers, BasicPhysicDrawnObject followed, 
			double minimumzoomspeed, double zoommodifier)
	{
		super(drawer, actorhandler, screenWidth, screenHeight, depthLayers, 
				followed);
		// Initializes attributes
		this.minimumspeed = minimumzoomspeed;
		this.zoommodifier = zoommodifier;
		
		// Checks the attributes
		if (this.minimumspeed < 0)
			this.minimumspeed = 0;
		if (this.zoommodifier < 0)
			this.zoommodifier = 0;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void act(double steps)
	{
		// In addition to normal acting, zooms the camera in and out
		super.act(steps);
		
		// Zooms out
		double scale = 1;

		if (getFollowedObject().getMovement().getSpeed() > this.minimumspeed + 1)
				scale = (1 + Math.log(getFollowedObject().getMovement().getSpeed()
			- this.minimumspeed)) * this.zoommodifier;
		setScale(scale, scale);
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return How much the followed object needs to have before the camera 
	 * starts zooming out
	 */
	public double getMinimumZoomSpeed()
	{
		return this.minimumspeed;
	}
	
	/**
	 * @return How fast the camera zooms out
	 */
	public double getZoomModifier()
	{
		return this.zoommodifier;
	}
	
	/**
	 * Changes how fast the followed object needs to move before the camera 
	 * zooms out
	 *
	 * @param minimumspeed how fast the followed object needs to move before 
	 * the camera starts to zoom out (0+)
	 */
	public void setMinimumZoomSpeed(double minimumspeed)
	{
		this.minimumspeed = minimumspeed;
		if (this.minimumspeed < 0)
			this.minimumspeed = 0;
	}
	
	/**
	 * Changes how fast the camera zooms in and out
	 *
	 * @param zoommodifier How fast the camera zooms in and out (0+, default 1.0)
	 */
	public void setZoomModifier(double zoommodifier)
	{
		this.zoommodifier = zoommodifier;
		if (this.zoommodifier < 0)
			this.zoommodifier = 0;
	}
}
