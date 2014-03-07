package utopia_camera;

import utopia_gameobjects.BasicPhysicDrawnObject;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;

/**
 * A camera, which follows the given object around. The camera's position is 
 * fixed to the followed object's position
 * 
 * @author Unto Solala & Mikko Hilpinen 
 * Created 18.6.2013
 */
public class FollowerCamera extends BasicCamera
{
	// ATTRIBUTES ----------------------------------------------------

	private BasicPhysicDrawnObject followed;

	
	// CONSTRUCTOR ---------------------------------------------------

	/**
	 * Creates a new follower camera added to the given handlers and starting 
	 * from the <b>followed</b> object's coordinates
	 * 
	 * @param drawer The drawablehandler that will draw the camera and 
	 * objects it shows
	 * @param actorhandler The actorhandler that will inform the camera of the 
	 * act-event
	 * @param screenWidth The width of the screen
	 * @param screenHeight The height of the screen
	 * @param followed The followed PhysicDrawnObject
	 */
	public FollowerCamera(DrawableHandler drawer, ActorHandler actorhandler,
			int screenWidth, int screenHeight, BasicPhysicDrawnObject followed)
	{
		super((int) (followed.getX()), (int) (followed.getY()), 
				screenWidth, screenHeight, drawer, actorhandler);

		// Initializes attributes
		this.followed = followed;
	}

	
	// IMPLEMENTED METHODS --------------------------------------------

	@Override
	public void act(double steps)
	{
		// In addition to normal acting, the camera follows the object
		super.act(steps);

		if (this.followed == null)
			return;

		// Follows the object
		setPosition(this.followed.getX(), this.followed.getY());
	}
	
	
	// GETTERS & SETTERS	-------------------------------------------
	
	/**
	 * @return The object that the camera follows
	 */
	public BasicPhysicDrawnObject getFollowedObject()
	{
		return this.followed;
	}
	
	/**
	 * Changes the object the camera follows
	 *
	 * @param d The new object the camera will follow
	 */
	public void setFollowedObject(BasicPhysicDrawnObject d)
	{
		this.followed = d;
	}
}
