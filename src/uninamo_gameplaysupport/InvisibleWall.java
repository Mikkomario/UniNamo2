package uninamo_gameplaysupport;

import genesis_event.HandlerRelay;
import genesis_util.StateOperator;
import genesis_util.Vector3D;
import conflict_collision.CollisionInformation;
import conflict_util.Polygon;
import exodus_world.Area;
import exodus_world.AreaListener;
import omega_util.SimpleGameObject;
import omega_util.Transformation;

/**
 * Invisible walls are walls that are not drawn but can be collided with. They are simple 
 * rectangles.
 * 
 * @author Mikko Hilpinen
 * @since 10.3.2014
 */
public class InvisibleWall extends SimpleGameObject implements Wall, AreaListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Transformation transformation;
	private CollisionInformation collisionInfo;
	private StateOperator collisionOperator;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new invisibleWall to the given position with the given form.
	 * 
	 * @param topLeft The position of the wall's top left corner
	 * @param dimensions The size of the wall
	 * @param handlers The handlers that will handle the wall
	 */
	public InvisibleWall(Vector3D topLeft, Vector3D dimensions, HandlerRelay handlers)
	{
		super(handlers);
		
		this.transformation = new Transformation(topLeft);
		this.collisionInfo = new CollisionInformation(Polygon.getRectangleVertices(
				Vector3D.zeroVector(), dimensions));
		this.collisionOperator = new StateOperator(true, true);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public StateOperator getCanBeCollidedWithStateOperator()
	{
		return this.collisionOperator;
	}

	@Override
	public CollisionInformation getCollisionInformation()
	{
		return this.collisionInfo;
	}

	@Override
	public Transformation getTransformation()
	{
		return this.transformation;
	}

	@Override
	public void setTrasformation(Transformation t)
	{
		this.transformation = t;
	}

	@Override
	public void onAreaStateChange(Area area, boolean newState)
	{
		// Dies when the area ends
		if (!newState)
			getIsDeadStateOperator().setState(true);
	}
}
