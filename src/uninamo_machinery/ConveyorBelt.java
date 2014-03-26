package uninamo_machinery;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import uninamo_components.ConnectorRelay;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.Wall;
import uninamo_main.GameSettings;
import uninamo_obstacles.Obstacle;
import utopia_handleds.Collidable;
import utopia_handlers.ActorHandler;
import utopia_handlers.CollidableHandler;
import utopia_handlers.CollisionHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.HelpMath;
import utopia_helpAndEnums.Movement;
import utopia_listeners.CollisionListener;
import utopia_listeners.TransformationListener;
import utopia_worlds.Area;

/**
 * ConveyorBelt is a machine that pushes actors either left or right or 
 * doesn't push them at all, depending on the input.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class ConveyorBelt extends Machine implements Wall, CollisionListener, 
	TransformationListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private boolean running, active, absolutePointsNeedUpdating;
	private int speedSign;
	private Point2D.Double[] relativeColPoints;
	private Point2D.Double[] absoluteColPoints;
	
	
	// CONSTRUCOR	-----------------------------------------------------
	
	/**
	 * Creates a new conveyorBelt to the given position
	 * 
	 * @param x The x-coordinate of the belt
	 * @param y The y-coordinate of the belt
	 * @param drawer The DrawableHandler that will draw the belt
	 * @param actorhandler The ActorHandler that will inform the belt about 
	 * step events
	 * @param collidableHandler The collidableHandler that will handle the 
	 * belt's collision checking
	 * @param collisionHandler the CollisionHandler that will handle the belt's 
	 * collision event informing
	 * @param componentArea The area of the game where the components are 
	 * created
	 * @param designArea The area where the machine is located at
	 * @param testHandler The testHandler that will inform the object about 
	 * test events
	 * @param connectorRelay The connectorRelay that will handle the belts 
	 * connectors
	 * @param machineCounter The machineCounter that will count the created 
	 * machine(s) (optional)
	 * @param ID The unique ID of the machine. Use null if you wan't it generated automatically
	 * @param isForTesting Is the machine created for simple demonstration purposes
	 */
	public ConveyorBelt(int x, int y, DrawableHandler drawer,
			ActorHandler actorhandler, CollidableHandler collidableHandler, 
			CollisionHandler collisionHandler, Area componentArea, Area designArea, 
			TestHandler testHandler, ConnectorRelay connectorRelay, 
			MachineCounter machineCounter, String ID, boolean isForTesting)
	{
		super(x, y, true, CollisionType.BOX, drawer, actorhandler,
				collidableHandler, componentArea, 
				designArea, testHandler, connectorRelay, machineCounter, "belt", 
				"beltreal", "machinecomponent", null, 2, 0, ID, isForTesting);
		
		// Initializes attributes
		this.absolutePointsNeedUpdating = true;
		this.running = false;
		this.speedSign = 1;
		this.active = true;
		//this.colhandler = collisionHandler;
		
		// Calculates the collision points
		this.relativeColPoints = new Point2D.Double[5];
		for (int i = 0; i < this.relativeColPoints.length; i++)
		{
			this.relativeColPoints[i] = new Point2D.Double(
					i * getWidth() / (this.relativeColPoints.length - 1), -15);
		}
		
		updateAbsoluteCollisionPoints();
		
		updateAnimation();
		
		//collisionHandler.printHandledNumber();
		
		// Adds the object to the handler(s)
		if (collisionHandler != null)
			collisionHandler.addCollisionListener(this);
		
		getTransformationListenerHandler().addListener(this);
	}
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onSignalEvent(boolean signalType, int inputIndex)
	{
		//this.colhandler.printHandledNumber();
		
		// May start, stop or change direction
		if (inputIndex == 0)
			this.running = signalType;
		else if (signalType)
		{
			this.speedSign = -1;
		}
		else
			this.speedSign = 1;
		
		updateAnimation();
	}
	
	@Override
	public boolean isActive()
	{
		return this.active;
	}

	@Override
	public void activate()
	{
		this.active = true;
	}

	@Override
	public void inactivate()
	{
		this.active = false;
	}

	@Override
	public Double[] getCollisionPoints()
	{
		//System.out.println("Relative points: " + this.relativeColPoints);
		//System.out.println("Absolute points: " + this.absoluteColPoints);
		
		// Updates the points if necessary
		if (this.absolutePointsNeedUpdating)
			updateAbsoluteCollisionPoints();
		
		return this.absoluteColPoints;
	}

	@Override
	public void onCollision(ArrayList<Double> colpoints, Collidable collided,
			double steps)
	{		
		// When collides with obstacles, moves them either left or right
		if (this.running && collided instanceof Obstacle)
		{
			//System.out.println("Collides with a box");
			
			Obstacle o = (Obstacle) collided;
			
			double optimalSpeed = GameSettings.normalTurnSpeed * this.speedSign;
			
			if ((this.speedSign == 1 && o.getMovement().getHSpeed() < optimalSpeed) 
					|| (this.speedSign == -1 && o.getMovement().getHSpeed() > optimalSpeed))
				o.addImpulse(new Movement(500 * this.speedSign, 0), 
						HelpMath.getAveragePoint(colpoints), steps);
		}
	}
	
	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		super.drawSelfBasic(g2d);
		
		drawRelativePoints(g2d, this.relativeColPoints);
	}
	
	@Override
	public void onTransformationEvent(TransformationEvent e)
	{
		// Remembers that the collision points need updating
		this.absolutePointsNeedUpdating = true;
	}
	
	@Override
	public MachineType getMachineType()
	{
		return MachineType.CONVEYORBELT;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	private void updateAnimation()
	{
		if (this.running)
			getSpriteDrawer().setImageSpeed(this.speedSign * 0.1);
		else
			getSpriteDrawer().setImageSpeed(0);
	}
	
	private void updateAbsoluteCollisionPoints()
	{
		// if relativepoints don't exist, sets up an empty table
		if (this.relativeColPoints == null)
		{
			this.absoluteColPoints = new Point2D.Double[0];
			return;
		}
		
		this.absoluteColPoints = new Point2D.Double[this.relativeColPoints.length];
		
		// Transforms each of the points and adds them to the new table
		for (int i = 0; i < this.relativeColPoints.length; i++)
		{
			this.absoluteColPoints[i] = transform(this.relativeColPoints[i]);
		}
		
		this.absolutePointsNeedUpdating = false;
	}
}
