package uninamo_components;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;

import exodus_world.Area;
import exodus_world.AreaListener;
import genesis_event.Drawable;
import genesis_event.HandlerRelay;
import genesis_util.DepthConstants;
import genesis_util.StateOperator;
import genesis_util.Vector3D;
import omega_util.SimpleGameObject;
import omega_util.Transformable;
import omega_util.Transformation;
import vision_sprite.SingleSpriteDrawer;
import vision_sprite.SpriteBank;

/**
 * Components are key elements in the game. Components can be placed on the 
 * workbench and together they form logical systems. Components are connected 
 * with cables and send, process and / or receive signal(s) using 
 * signalprocessors.
 * 
 * @author Mikko Hilpinen
 * @since 8.3.2014
 */
public abstract class Component extends SimpleGameObject implements
		SignalReceiver, SignalSender, AreaListener, Transformable, Drawable
{
	// TODO: Make this implement constructable & writable
	
	// ATTRIBUTES	------------------------------------------------------
	
	private String id;
	private SingleSpriteDrawer spriteDrawer;
	private InputCableConnector[] inputs;
	private OutputCableConnector[] outputs;
	private Transformation transformation;
	private StateOperator isVisibleOperator;
	
	private static int componentsCreated = 0;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new component to the given position.
	 * @param handlers The handlers that will handle the component
	 * @param position The component's position
	 * @param connectorRelay A connectorRelay that will keep track of the 
	 * connectors
	 * @param spriteName The name of the component sprite used to draw the 
	 * component
	 * @param inputs How many input connectors the component has
	 * @param outputs How many output connectors the component has
	 * @param fromBox Was the component created by pulling it from a componentBox
	 * @param isForTesting If this is true, the component will go to test mode 
	 * where it won't react to mouse but will create test cables to its connectors
	 */
	public Component(HandlerRelay handlers, Vector3D position, 
			ConnectorRelay connectorRelay, String spriteName, int inputs, int outputs, 
			boolean fromBox, boolean isForTesting)
	{
		super(handlers);
		
		// Initializes attributes
		this.transformation = new Transformation(position);
		this.isVisibleOperator = new StateOperator(true, true);
		this.spriteDrawer = new SingleSpriteDrawer(SpriteBank.getSprite("components", 
				spriteName), this, handlers);
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
			// TODO: WET WET
			Vector3D relativePosition = new Vector3D(0, (i + 1) * 
					(this.spriteDrawer.getSprite().getDimensions().getSecond() / 
					(inputs + 1.0)));
			this.inputs[i] = new InputCableConnector(handlers, relativePosition, this, i, 
					isForTesting, connectorRelay);
		}
		for (int i = 0; i < outputs; i++)
		{
			Vector3D relativePosition = new Vector3D(
					this.spriteDrawer.getSprite().getDimensions().getFirst(), (i + 1) * 
					(this.spriteDrawer.getSprite().getDimensions().getSecond() / 
					(outputs + 1.0)));
			this.outputs[i] = new OutputCableConnector(handlers, relativePosition, this, i, 
					isForTesting, connectorRelay);
		}
		
		updateConnectorTransformations();
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void drawSelf(Graphics2D g2d)
	{
		AffineTransform lastTransform = getTransformation().transform(g2d);
		
		this.spriteDrawer.drawSprite(g2d);
		
		g2d.setTransform(lastTransform);
	}

	@Override
	public int getDepth()
	{
		return DepthConstants.NORMAL;
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return this.isVisibleOperator;
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
		updateConnectorTransformations();
	}

	@Override
	public void onAreaStateChange(Area area)
	{
		// At the end of the area, dies
		if (!area.getIsActiveStateOperator().getState())
			getIsDeadStateOperator().setState(true);
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
	
	/**
	 * @return The spriteDrawer the component uses
	 */
	protected SingleSpriteDrawer getSpriteDrawer()
	{
		return this.spriteDrawer;
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
	
	private void updateConnectorTransformations()
	{
		// TODO: WET WET
		for (int i = 0; i < this.inputs.length; i++)
		{
			this.inputs[i].updateTransformations();
		}
		for (int i = 0; i < this.outputs.length; i++)
		{
			this.outputs[i].updateTransformations();
		}
	}
}
