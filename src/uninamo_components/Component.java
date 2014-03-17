package uninamo_components;

import java.awt.Graphics2D;
import java.util.Random;

import uninamo_gameplaysupport.TestHandler;
import utopia_gameobjects.DimensionalDrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.DepthConstants;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Room;

/**
 * Components are key elements in the game. Components can be placed on the 
 * workbench and together they form logical systems. Components are connected 
 * with cables and send, process and / or receive signal(s) using 
 * signalprocessors.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public abstract class Component extends DimensionalDrawnObject implements
		SignalReceiver, SignalSender, RoomListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private String id;
	private SingleSpriteDrawer spritedrawer;
	private InputCableConnector[] inputs;
	private OutputCableConnector[] outputs;
	
	private static int componentsCreated = 0;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
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
	 * @param spritename The name of the component sprite used to draw the 
	 * component
	 * @param inputs How many input connectors the component has
	 * @param outputs How many output connectors the component has
	 * @param fromBox Was the component created by pulling it from a componentBox
	 * @param isForTesting If this is true, the component will go to test mode 
	 * where it won't react to mouse but will create test cables to its connectors
	 */
	public Component(int x, int y, DrawableHandler drawer, 
			ActorHandler actorhandler, MouseListenerHandler mousehandler, 
			Room room, TestHandler testHandler, ConnectorRelay connectorRelay, 
			String spritename, int inputs, int outputs, boolean fromBox, 
			boolean isForTesting)
	{
		super(x, y, DepthConstants.NORMAL, false, CollisionType.BOX, drawer, 
				null);
		
		// Initializes attributes
		this.spritedrawer = new SingleSpriteDrawer(
				MultiMediaHolder.getSpriteBank("components").getSprite(
				spritename), actorhandler, this);
		this.inputs = new InputCableConnector[inputs];
		this.outputs = new OutputCableConnector[outputs];
		
		// Generates the id
		// TODO: There's still a small chance that two components will have the 
		// same ID after loading
		this.id = componentsCreated + "." + new Random().nextInt(1000);
		componentsCreated ++;
		
		// Creates the connectors
		for (int i = 0; i < inputs; i++)
		{
			int relativey = (int) ((i + 1) * (getHeight() / (inputs + 1.0)));
			this.inputs[i] = new InputCableConnector(0, relativey, drawer, 
					mousehandler, room, testHandler, connectorRelay, this, i, 
					isForTesting);
		}
		for (int i = 0; i < outputs; i++)
		{
			int relativey = (int) ((i + 1) * (getHeight() / (outputs + 1.0)));
			this.outputs[i] = new OutputCableConnector(getWidth() - 0, 
					relativey, drawer, mousehandler, room, testHandler, 
					connectorRelay, this, i, isForTesting);
		}
		
		// Adds the object to the handler(s)
		if (room != null)
			room.addObject(this);
		
		// Resets the transform status
		forceTransformationUpdate();
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		// Doesn't limit collisionListeners
		return null;
	}

	@Override
	public int getWidth()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getWidth();
	}

	@Override
	public int getHeight()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getHeight();
	}

	@Override
	public int getOriginX()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getOriginX();
	}

	@Override
	public int getOriginY()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getOriginY();
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// Draws the sprite
		if (this.spritedrawer == null)
			return;
		this.spritedrawer.drawSprite(g2d, 0, 0);
	}
	
	@Override
	public void onRoomStart(Room room)
	{
		// Does nothing
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Dies
		kill();
	}
	
	@Override
	public void kill()
	{
		// Also kills the connectors
		for (CableConnector connector : this.inputs)
		{
			//System.out.println("Kills an input");
			connector.kill();
		}
		for (CableConnector connector : this.outputs)
		{
			//System.out.println("Kills an output");
			connector.kill();
		}
		
		super.kill();
	}
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * @return The identifier of this component. No other component has the 
	 * same identifier
	 */
	public String getID()
	{
		return this.id;
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Informs a specific output about a signal change
	 * 
	 * @param index The index of the output informed
	 * @param signal The signal given to the output
	 */
	protected void sendSignalToOutput(int index, boolean signal)
	{
		if (index < 0 || index >= this.outputs.length)
		{
			System.err.println("The component doesn't have output with index " 
					+ index);
			return;
		}
		
		this.outputs[index].onSignalChange(signal, this);
	}
	
	/**
	 * Informs all outputs about a signal change
	 * @param signal The new signal status
	 */
	protected void sendSignalToAllOutputs(boolean signal)
	{
		for (int i = 0; i < this.outputs.length; i++)
		{
			this.outputs[i].onSignalChange(signal, this);
		}
	}
	
	/**
	 * Returns the index of the given inputCableConnector. This can be used in 
	 * recognizing the role of the input.
	 * 
	 * @param c The InputCableConnector who's index is needed
	 * @return The index of the connector or -1 if the connector isn't 
	 * connected to the component
	 */
	protected int getInputIndex(InputCableConnector c)
	{
		for (int i = 0; i < this.inputs.length; i++)
		{
			if (this.inputs[i].equals(c))
				return i;
		}
		
		return -1;
	}
	
	/**
	 * Returns a signal status from the input with the given index
	 * 
	 * @param index The index of the questioned input
	 * @return The signal the input is receiving. returns false if no such input 
	 * exists.
	 */
	protected boolean getInputStatus(int index)
	{
		if (index < 0 || index >= this.inputs.length)
		{
			System.err.println("The component doesn't have input with index " 
					+ index);
			return false;
		}
		
		return this.inputs[index].getSignalStatus();
	}
}
