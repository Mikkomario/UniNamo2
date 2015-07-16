package uninamo_machinery;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import arc_bank.Bank;
import conflict_collision.Collidable;
import conflict_collision.CollisionInformation;
import exodus_world.Area;
import exodus_world.AreaListener;
import genesis_event.Drawable;
import genesis_event.HandlerRelay;
import genesis_util.DepthConstants;
import genesis_util.StateOperator;
import genesis_util.StateOperatorListener;
import genesis_util.Vector3D;
import omega_util.SimpleGameObject;
import omega_util.Transformation;
import uninamo_components.ConnectorRelay;
import uninamo_components.MachineInputComponent;
import uninamo_components.MachineOutputComponent;
import uninamo_gameplaysupport.TestEvent;
import uninamo_gameplaysupport.TestListener;
import uninamo_main.GameSettings;
import uninamo_main.Utility;
import uninamo_obstacles.Obstacle;
import vision_sprite.MultiSpriteDrawer;
import vision_sprite.Sprite;
import vision_sprite.SpriteBank;

/**
 * Machines interact with actors as well as components. Machines have different 
 * functions that are usually represented in the component level.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public abstract class Machine extends SimpleGameObject implements 
		AreaListener, TestListener, Drawable, Collidable, StateOperatorListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private MachineOutputComponent output;
	private MachineInputComponent input;
	private MultiSpriteDrawer spritedrawer;
	private String name;
	private boolean testing;
	private Transformation transformation;
	private StateOperator isSolidOperator, isVisibleOperator;
	private CollisionInformation collisionInfo;
	
	private static int inputComponentsCreated = 0;
	private static int outputComponentsCreated = 0;
	private static final int COMPONENTDISTANCE = 85;
	private static final Class<?>[] COLLIDEDCLASSES = {Obstacle.class};
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new machine to the given position
	 * @param position The machine's position
	 * @param componentHandlers The handlers that will handle the components
	 * @param machineHandlers The handlers that will handle the machine
	 * @param connectorRelay The connectorRelay that will handle the machine's 
	 * components' connectors
	 * @param machineCounter The machineCounter that counts the created machines 
	 * (optional)
	 * @param designSpriteName The name of the sprite the machine uses in the 
	 * design view
	 * @param realSpriteName The name of the sprite the machine uses in the 
	 * execution view 
	 * @param inputComponentSpriteName The name of the sprite used to draw the 
	 * machine's input component (null if no input component is used)
	 * @param outputComponentSpriteName The name of the sprite used to draw the 
	 * machine's output component (null if no output component is used)
	 * @param inputs How many input connectors the machine's input component 
	 * has (0 if no input component is used)
	 * @param outputs How many output connectors the machine's output component 
	 * has (0 if no output component is used)
	 * @param ID The unique ID of the machine. Use null if you wan't it 
	 * generated automatically
	 * @param isForTesting Is the machine meant for testing / demonstration 
	 * purposes (into the manual). The machine works a bit differently if so
	 */
	public Machine(Vector3D position, HandlerRelay componentHandlers, 
			HandlerRelay machineHandlers, ConnectorRelay connectorRelay, 
			MachineCounter machineCounter, String designSpriteName, String realSpriteName, 
			String inputComponentSpriteName, String outputComponentSpriteName, 
			int inputs, int outputs, String ID, boolean isForTesting)
	{
		super(machineHandlers);
		
		// Initializes attributes
		Bank<Sprite> spriteBank = SpriteBank.getSpriteBank("machines");
		Sprite[] sprites = {spriteBank.get(designSpriteName), spriteBank.get(realSpriteName)};
		this.spritedrawer = new MultiSpriteDrawer(sprites, this, machineHandlers);
		this.output = null;
		this.input = null;
		this.testing = false;
		this.isSolidOperator = new StateOperator(true, true);
		this.isVisibleOperator = new StateOperator(true, true);
		this.collisionInfo = new CollisionInformation(Utility.getSpriteVertices(sprites[0]));
		this.collisionInfo.limitSupportedListenersTo(COLLIDEDCLASSES);
		
		// Increases the counter (if possible)
		if (machineCounter != null)
			machineCounter.countNewMachine(getMachineType());
		
		// Creates the name
		if (ID == null)
		{
			String typeName = getMachineType().toString();
			// Shortens the name if it's too long
			if (typeName.length() > 4)
				typeName = typeName.substring(0, 4);
			// Adds the number
			if (machineCounter != null)
				this.name = typeName + machineCounter.getMachineTypeAmount(getMachineType());
			else
				this.name = typeName;
		}
		// Or simply uses the given ID
		else
			this.name = ID;
		
		// Creates components
		// TODO: WET WET
		if (inputComponentSpriteName != null && inputs > 0)
		{
			Vector3D componentPosition = getNewComponentPosition(true);
			
			// If is in testing mode, simply puts the component near the machine
			if (isForTesting)
				componentPosition = getTransformation().getPosition().plus(new Vector3D(-50, 
						75));
			
			this.input = new MachineInputComponent(componentHandlers, componentPosition, 
					connectorRelay, inputComponentSpriteName, inputs, this, isForTesting, 
					toString());
			inputComponentsCreated ++;
		}
		
		if (outputComponentSpriteName != null && outputs > 0)
		{
			Vector3D componentPosition = getNewComponentPosition(false);
			
			// If is in testing mode, simply puts the component near the machine
			if (isForTesting)
				componentPosition = getTransformation().getPosition().plus(new Vector3D(-50, 
						75));
			
			this.output = new MachineOutputComponent(componentHandlers, componentPosition, 
					connectorRelay, outputComponentSpriteName, outputs, isForTesting, 
					toString());
			outputComponentsCreated ++;
		}
		
		// If is a test version, goes straight to "real" sprite
		if (isForTesting)
			getSpriteDrawer().setSpriteIndex(1, false);
	}

	
	// ABSTRACT METHODS	-------------------------------------------------
	
	/**
	 * This method is called when an inputComponent receives signal.
	 * @param signalType The type of the signal (true or false)
	 * @param inputIndex From which of the component's input connectors the 
	 * input signal came from
	 */
	public abstract void onSignalEvent(boolean signalType, int inputIndex);
	
	/**
	 * @return What kind of machineType the class represents
	 */
	public abstract MachineType getMachineType();
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public String toString()
	{
		return this.name;
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
	public StateOperator getCanBeCollidedWithStateOperator()
	{
		return this.isSolidOperator;
	}

	@Override
	public CollisionInformation getCollisionInformation()
	{
		return this.collisionInfo;
	}

	@Override
	public void drawSelf(Graphics2D g2d)
	{
		// Draws the sprite
		if (this.spritedrawer == null)
			return;
		
		AffineTransform lastTransform = getTransformation().transform(g2d);
		getSpriteDrawer().drawSprite(g2d);
		
		// And the name of the machine (if on design mode)
		if (!this.testing)
		{
			g2d.setFont(GameSettings.basicFont);
			g2d.setColor(Color.WHITE);
			g2d.drawString(toString(), 0, 0);
		}
		
		g2d.setTransform(lastTransform);
	}

	@Override
	public int getDepth()
	{
		return DepthConstants.BACK - 20;
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return this.isVisibleOperator;
	}

	@Override
	public void onTestEvent(TestEvent event)
	{
		this.testing = event.testRunning();
		if (this.testing)
			getSpriteDrawer().setSpriteIndex(1, false);
		else
			getSpriteDrawer().setSpriteIndex(0, false);
	}

	@Override
	public void onAreaStateChange(Area area)
	{
		if (!area.getIsActiveStateOperator().getState())
			getIsDeadStateOperator().setState(false);
	}

	@Override
	public void onStateChange(StateOperator source, boolean newState)
	{
		if (source.equals(getIsDeadStateOperator()) && newState)
		{
			// Also kills the components (which also affects the counters)
			if (this.input != null)
			{
				this.input.getIsDeadStateOperator().setState(newState);
				this.input = null;
				inputComponentsCreated --;
			}
			if (this.output != null)
			{
				this.output.getIsDeadStateOperator().setState(newState);
				this.output = null;
				outputComponentsCreated --;
			}
		}
	}
	
	
	// GETTERS & SETTERS	-----------------------
	
	/**
	 * @return The size of the machine (without scaling)
	 */
	public Vector3D getDimensions()
	{
		return getSpriteDrawer().getSprite().getDimensions();
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * @return The spritedrawer that draws the machine's sprites
	 */
	protected MultiSpriteDrawer getSpriteDrawer()
	{
		return this.spritedrawer;
	}
	
	/**
	 * Sends signal through the output component to other connected components
	 * 
	 * @param signal The type of signal
	 * @param outputIndex The index of the cableConnector used
	 */
	protected void sendSignal(boolean signal, int outputIndex)
	{
		if (this.output == null)
			System.err.println("Machine " + getClass().getName() + 
					" doesn't have an output component to send signals through!");
		else
			this.output.sendSignal(signal, outputIndex);
	}
	
	private static Vector3D getNewComponentPosition(boolean isInput)
	{	
		int x = GameSettings.resolution.getFirstInt() - 200;
		int y = COMPONENTDISTANCE / 2 + 160;
		int i = 0;
		int components = inputComponentsCreated;
		
		if (!isInput)
		{
			x = 200;
			components = outputComponentsCreated;
		}
		
		while (i < components)
		{
			y += COMPONENTDISTANCE + 15;
			
			if (y > GameSettings.resolution.getSecond() - COMPONENTDISTANCE / 2 + 160)
			{
				if (isInput)
					x -= COMPONENTDISTANCE;
				else
					x += COMPONENTDISTANCE;
				y = COMPONENTDISTANCE / 2 + 160;
			}
			
			i++;
		}
		
		return new Vector3D(x, y);
	}
}
