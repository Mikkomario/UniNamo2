package uninamo_main;

import omega_util.Transformable;
import omega_util.Transformation;
import conflict_util.Polygon;
import vision_sprite.Sprite;
import genesis_event.DrawableHandler;
import genesis_event.GenesisHandlerType;
import genesis_event.HandlerRelay;
import genesis_event.MouseListenerHandler;
import genesis_util.Vector3D;

/**
 * This is a collection of useful static methods
 * @author Mikko Hilpinen
 * @since 12.7.2015
 */
public class Utility
{
	// CONSTRUCTOR	--------------
	
	private Utility()
	{
		// Static interface
	}

	
	// OTHER METHODS	----------
	
	/**
	 * Calculates the vertices of the sprite's corners
	 * @param sprite The sprite
	 * @return The sprite's corners
	 */
	public static Vector3D[] getSpriteVertices(Sprite sprite)
	{
		Vector3D topLeft = sprite.getOrigin().reverse();
		return Polygon.getRectangleVertices(topLeft, topLeft.plus(sprite.getDimensions()));
	}
	
	/**
	 * Transforms a single transformable somehow, adding to the previous transformation(s)
	 * @param transformable The object that is being transformed
	 * @param t How the object is transformed
	 */
	public static void Transform(Transformable transformable, Transformation t)
	{
		// TODO: Have to do it like this since, for some reason, Transformable.transform(...) 
		// doesn't work in this project
		transformable.setTrasformation(transformable.getTransformation().plus(t));
	}
	
	/**
	 * Changes the mouse listening state of the given handler(s)
	 * @param relay The relay that will be affected by this change
	 * @param newState The new mouse listening state of the objects in the handler
	 */
	public static void setMouseState(HandlerRelay relay, boolean newState)
	{
		((MouseListenerHandler) relay.getHandler(GenesisHandlerType.MOUSEHANDLER)
				).getListensToMouseEventsOperator().setState(newState);
	}
	
	/**
	 * Changes the visibility of the given handler(s)
	 * @param relay The relay that will be affected by this change
	 * @param newState The new visibility state of the objects in the handler
	 */
	public static void setVisibleState(HandlerRelay relay, boolean newState)
	{
		((DrawableHandler) relay.getHandler(GenesisHandlerType.DRAWABLEHANDLER)
				).getIsVisibleStateOperator().setState(newState);
	}
}
