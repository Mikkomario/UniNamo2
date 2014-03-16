package uninamo_components;

import uninamo_gameplaysupport.TestHandler;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_worlds.Room;

/**
 * NormalComponents are components that aren't machineComponents. 
 * NormalComponents can be saved as a string from which they can later be 
 * recreated.
 * 
 * @author Mikko Hilpinen
 * @since 15.3.2014
 */
public abstract class NormalComponent extends Component
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private String id;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new component to the given position.
	 * 
	 * @param x The new x-coordinate of the component's origin (pixels)
	 * @param y The new y-coordinate of the component's origin (pixels)
	 * @param drawer The drawableHandler that will draw the component
	 * @param actorhandler The actorHandler that will animate the component
	 * @param mousehandler The mouseListenerHandler that will inform the 
	 * object about mouse events
	 * @param room The room where the component resides at
	 * @param testHandler The testHandler that will inform the object about test 
	 * events
	 * @param connectorRelay A connectorRelay that will keep track of the 
	 * connectors
	 * @param componentRelay The NormalComponentRelay that will keep track of 
	 * the component (optional)
	 * @param spritename The name of the component sprite used to draw the 
	 * component
	 * @param inputs How many input connectors the component has
	 * @param outputs How many output connectors the component has
	 * @param fromBox Was the component created by pulling it from a componentBox
	 * @param isForTesting If this is true, the component will go to test mode 
	 * where it won't react to mouse but will create test cables to its connectors
	 */
	public NormalComponent(int x, int y, DrawableHandler drawer,
			ActorHandler actorhandler, MouseListenerHandler mousehandler,
			Room room, TestHandler testHandler, ConnectorRelay connectorRelay, 
			NormalComponentRelay componentRelay, String spritename, int inputs, 
			int outputs, boolean fromBox, boolean isForTesting)
	{
		super(x, y, drawer, actorhandler, mousehandler, room, testHandler,
				connectorRelay, spritename, inputs, outputs, fromBox,
				isForTesting);
		
		// Initializes attributes
		String typeName = getType().toString();
		if (typeName.length() > 4)
			typeName = typeName.substring(0, 4);
		this.id = typeName + super.getID();
		
		
		// Adds the object to the handler(s)
		if (componentRelay != null)
			componentRelay.addComponent(this);
	}
	
	
	// ABSTRACT METHODS	--------------------------------------------------
	
	/**
	 * @return What componentType this component represents
	 */
	public abstract ComponentType getType();

	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public String getID()
	{
		return this.id;
	}

	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * @return Returns the data needed for recreating the component in a 
	 * string format. The string will have the following structure:<br>
	 * ID#ComponentType#x#y
	 */
	public String getSaveData()
	{
		return getID() + "#" + getType().toString().toLowerCase() + "#" + 
				(int) getX() + "#" + (int) getY();
	}
	
	/**
	 * Changes the components unique ID.
	 * 
	 * @param ID The component's new unique ID.
	 */
	public void setID(String ID)
	{
		this.id = ID;
	}
}
