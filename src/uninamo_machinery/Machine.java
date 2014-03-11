package uninamo_machinery;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import uninamo_components.ConnectorRelay;
import uninamo_components.MachineInputComponent;
import uninamo_components.MachineOutputComponent;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.Testable;
import uninamo_main.GameSettings;
import uninamo_obstacles.Obstacle;
import uninamo_worlds.Area;
import utopia_gameobjects.DimensionalDrawnObject;
import utopia_graphic.MultiSpriteDrawer;
import utopia_graphic.Sprite;
import utopia_handlers.ActorHandler;
import utopia_handlers.CollidableHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.DepthConstants;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Room;

/**
 * Machines interact with actors as well as components. Machines have different 
 * functions that are usually represented in the component level.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public abstract class Machine extends DimensionalDrawnObject implements 
		RoomListener, Testable
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private MachineOutputComponent output;
	private MachineInputComponent input;
	private MultiSpriteDrawer spritedrawer;
	
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
	 * @param drawer The drawer that will draw the machine
	 * @param actorhandler The actorHandler that will animate the object
	 * @param collidablehandler The collidableHandler that will handle the 
	 * machine's collision checking
	 * @param codingArea The coding area where the machine components will be created
	 * @param designArea The area where the machine is located at
	 * @param testHandler The testHandler that will inform the object about test events
	 * @param connectorRelay The connectorRelay that will handle the machine's 
	 * components' connectors
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
	 */
	public Machine(int x, int y, boolean isSolid,
			CollisionType collisiontype, DrawableHandler drawer, 
			ActorHandler actorhandler, CollidableHandler collidablehandler, 
			Area codingArea, Area designArea, TestHandler testHandler, 
			ConnectorRelay connectorRelay, 
			String designSpriteName, String realSpriteName, 
			String inputComponentSpriteName, String outputComponentSpriteName, 
			int inputs, int outputs)
	{
		super(x, y, DepthConstants.NORMAL + 5, isSolid, collisiontype, drawer, 
				collidablehandler);
		
		// Initializes attributes
		Sprite[] sprites = new Sprite[2];
		sprites[0] = MultiMediaHolder.getSpriteBank("machines").getSprite(designSpriteName);
		sprites[1] = MultiMediaHolder.getSpriteBank("machines").getSprite(realSpriteName);
		this.spritedrawer = new MultiSpriteDrawer(sprites, actorhandler, this);
		this.output = null;
		this.input = null;
		
		// Creates components
		if (inputComponentSpriteName != null && inputs > 0)
		{
			Point2D.Double position = getNewComponentPosition(true);
			
			new MachineInputComponent((int) position.getX(), 
					(int) position.getY(), codingArea.getDrawer(), 
					codingArea.getActorHandler(), codingArea.getMouseHandler(), 
					codingArea, testHandler, connectorRelay, 
					inputComponentSpriteName, inputs, this);
			inputComponentsCreated ++;
		}
		
		if (outputComponentSpriteName != null && outputs > 0)
		{
			Point2D.Double position = getNewComponentPosition(false);
			this.output = new MachineOutputComponent((int) position.getX(), 
					(int) position.getY(), codingArea.getDrawer(), 
					codingArea.getActorHandler(), codingArea.getMouseHandler(), 
					codingArea, testHandler, connectorRelay, 
					outputComponentSpriteName, outputs);
			outputComponentsCreated ++;
		}
		
		// Adds the object to the handler(s)
		if (designArea != null)
			designArea.addObject(this);
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
		
		drawCollisionArea(g2d);
	}
	
	@Override
	public void kill()
	{
		// Also kills the components (which also affects the counters)
		if (this.input != null)
		{
			this.input.kill();
			this.input = null;
			inputComponentsCreated --;
		}
		if (this.output != null)
		{
			this.output.kill();
			this.output = null;
			outputComponentsCreated --;
		}
		
		super.kill();
	}
	
	@Override
	public void startTesting()
	{
		// Changes the sprite
		getSpriteDrawer().setSpriteIndex(1, false);
	}

	@Override
	public void endTesting()
	{
		// Changes the sprite
		getSpriteDrawer().setSpriteIndex(0, false);
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
			y += COMPONENTDISTANCE;
			
			if (y > GameSettings.screenHeight - COMPONENTDISTANCE / 2 + 160)
			{
				if (isInput)
					x -= COMPONENTDISTANCE;
				else
					x += COMPONENTDISTANCE;
				y = COMPONENTDISTANCE / 2;
			}
			
			i++;
		}
		
		return new Point2D.Double(x, y);
	}
}
