package utopia_handlers;

import utopia_gameobjects.DrawnObject;

/**
 * Drawnobjecthandler is a special drawablehandler that handles only drawn 
 * objects
 *
 * @author Mikko Hilpinen.
 *         Created 17.6.2013.
 */
public class DrawnObjectHandler extends DrawableHandler
{
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new empty drawnobjecthandler with the given information
	 *
	 * @param autodeath Will the handler automatically die when it runs out of handleds
	 * @param usesDepth Does the handler sort the drawn objects according to their depth
	 * @param depth How 'deep' the handler draws the objects
	 * @param superhandler Which drawablehandler will draw the handler and its content
	 */
	public DrawnObjectHandler(boolean autodeath, boolean usesDepth, int depth, 
			DrawableHandler superhandler)
	{
		super(autodeath, usesDepth, depth, superhandler);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return DrawnObject.class;
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 *Adds the given drawnobject to the drawn objects
	 *
	 * @param d The object to be added
	 */
	public void addDrawnObject(DrawnObject d)
	{
		addHandled(d);
	}
}
