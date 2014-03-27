package uninamo_machinery;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import uninamo_components.ConnectorRelay;
import uninamo_components.MachineInputComponent;
import uninamo_components.MachineOutputComponent;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TestListener;
import uninamo_main.GameSettings;
import uninamo_obstacles.Obstacle;
import utopia_gameobjects.DimensionalDrawnObject;
import utopia_graphic.MultiSpriteDrawer;
import utopia_graphic.Sprite;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.DepthConstants;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import utopia_worlds.Room;

/**
 * Machines interact with actors as well as components. Machines have different 
 * functions that are usually represented in the component level.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public abstract class Machine extends DimensionalDrawnObject implements 
		RoomListener, TestListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private MachineOutputComponent output;
	private MachineInputComponent input;
	private MultiSpriteDrawer spritedrawer;
	private String name;
	private boolean testing;
	
	private static int inputComponentsCreated = 0;
	private static int outputComponentsCreated = 0;
	private static final int COMPONENTDISTANCE = 85;
	private static final Class<?>[] COLLIDEDCLASSES = {Obstacle.class};
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new machine to the given position
	 * 
	 * @param x The new x-coordinate of the machine (pixels)
	 * @param y The new y-coordinate of the machine (pixels)
	 * @param isSolid Will the machine collide with objects
	 * @param collisiontype What kind of shape does the machine have
	 * machine's collision checking
	 * @param componentArea The coding area where the machine components will be created
	 * @param machineArea The area where the machine is located at
	 * @param testHandler The testHandler that will inform the object about test events
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
	public Machine(int x, int y, boolean isSolid,
			CollisionType collisiontype, 
			Area componentArea, Area machineArea, TestHandler testHandler, 
			ConnectorRelay connectorRelay, MachineCounter machineCounter, 
			String designSpriteName, String realSpriteName, 
			String inputComponentSpriteName, String outputComponentSpriteName, 
			int inputs, int outputs, String ID, boolean isForTesting)
	{
		super(x, y, DepthConstants.NORMAL + 5, isSolid, collisiontype, machineArea);
		
		//System.out.println("Machine connector handler " + connectorRelay);
		
		// Initializes attributes
		Sprite[] sprites = new Sprite[2];
		sprites[0] = MultiMediaHolder.getSpriteBank("machines").getSprite(designSpriteName);
		sprites[1] = MultiMediaHolder.getSpriteBank("machines").getSprite(realSpriteName);
		this.spritedrawer = new MultiSpriteDrawer(sprites, machineArea.getActorHandler(), this);
		this.output = null;
		this.input = null;
		this.testing = false;
		
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
		if (inputComponentSpriteName != null && inputs > 0)
		{
			Point2D.Double position = getNewComponentPosition(true);
			
			// If is in testing mode, simply puts the component near the machine
			if (isForTesting)
				position = new Point2D.Double(getX() - 50, getY() + 75);
			
			//System.out.println(position);
			//System.out.println(componentArea);
			
			this.input = new MachineInputComponent(componentArea, 
					(int) position.getX(), 
					(int) position.getY(), testHandler, connectorRelay, 
					inputComponentSpriteName, inputs, this, isForTesting, 
					toString());
			inputComponentsCreated ++;
		}
		
		if (outputComponentSpriteName != null && outputs > 0)
		{
			Point2D.Double position = getNewComponentPosition(false);
			
			// If is in testing mode, simply puts the component near the machine
			if (isForTesting)
				position = new Point2D.Double(getX() + 50, getY() + 75);
			
			this.output = new MachineOutputComponent(componentArea, (int) position.getX(), 
					(int) position.getY(), testHandler, connectorRelay, 
					outputComponentSpriteName, outputs, isForTesting, 
					toString());
			outputComponentsCreated ++;
		}
		
		// If is a test version, goes straight to "real" sprite
		if (isForTesting)
			getSpriteDrawer().setSpriteIndex(1, false);
		
		// Adds the object to the handler(s)
		if (testHandler != null)
			testHandler.addTestable(this);
		
		// Forces transformation update
		forceTransformationUpdate();
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
	public Class<?>[] getSupportedListenerClasses()
	{
		// Only obstacles interact with the machines in a physical way
		return COLLIDEDCLASSES;
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
		
		// And the name of the machine (if on design mode)
		if (!this.testing)
		{
			g2d.setFont(GameSettings.basicFont);
			g2d.setColor(Color.WHITE);
			g2d.drawString(toString(), 0, 0);
		}
		
		drawCollisionArea(g2d);
	}
	
	@Override
	public void kill()
	{
		//System.out.println("Machine killed");
		//System.out.println(this.input);
		//System.out.println(this.output);
		
		// Also kills the components (which also affects the counters)
		if (this.input != null)
		{
			//System.out.println("Machineinput killed");
			
			this.input.kill();
			this.input = null;
			inputComponentsCreated --;
		}
		if (this.output != null)
		{
			//System.out.println("Machineoutput killed");
			
			this.output.kill();
			this.output = null;
			outputComponentsCreated --;
		}
		
		super.kill();
	}
	
	@Override
	public void onTestStart()
	{
		// Changes the sprite
		getSpriteDrawer().setSpriteIndex(1, false);
		this.testing = true;
	}

	@Override
	public void onTestEnd()
	{
		// Changes the sprite
		getSpriteDrawer().setSpriteIndex(0, false);
		this.testing = false;
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	@Override
	public String toString()
	{
		return this.name;
	}
	
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
	
	private static Point2D.Double getNewComponentPosition(boolean isInput)
	{	
		int x = GameSettings.screenWidth - 200;
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
			
			if (y > GameSettings.screenHeight - COMPONENTDISTANCE / 2 + 160)
			{
				if (isInput)
					x -= COMPONENTDISTANCE;
				else
					x += COMPONENTDISTANCE;
				y = COMPONENTDISTANCE / 2 + 160;
			}
			
			i++;
		}
		
		return new Point2D.Double(x, y);
	}
}
