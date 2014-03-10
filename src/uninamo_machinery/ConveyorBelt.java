package uninamo_machinery;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import uninamo_components.ConnectorRelay;
import uninamo_gameplaysupport.Wall;
import uninamo_obstacles.Obstacle;
import uninamo_worlds.Area;
import utopia_handleds.Collidable;
import utopia_handlers.ActorHandler;
import utopia_handlers.CollisionHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_listeners.CollisionListener;

/**
 * ConveyorBelt is a machine that pushes actors either left or right or 
 * doesn't push them at all, depending on the input.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class ConveyorBelt extends Machine implements Wall, CollisionListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private boolean running, active;
	private int speedSign;
	private Point2D.Double[] relativeColPoints;
	
	
	// CONSTRUCOR	-----------------------------------------------------
	
	/**
	 * Creates a new conveyorBelt to the given position
	 * 
	 * @param x The x-coordinate of the belt
	 * @param y The y-coordinate of the belt
	 * @param drawer The DrawableHandler that will draw the belt
	 * @param actorhandler The ActorHandler that will inform the belt about 
	 * step events
	 * @param collisionHandler the CollisionHandler that will handle the belt's 
	 * collision checking and collision event informing
	 * @param codingArea The coding area of the game where the components are 
	 * created
	 * @param connectorRelay The connectorRelay that will handle the belts 
	 * connectors
	 */
	public ConveyorBelt(int x, int y, DrawableHandler drawer,
			ActorHandler actorhandler, CollisionHandler collisionHandler,
			Area codingArea, ConnectorRelay connectorRelay)
	{
		super(x, y, true, CollisionType.BOX, drawer, actorhandler,
				collisionHandler.getCollidableHandler(), codingArea, 
				connectorRelay,"belt", "beltreal", "machinecomponent", null, 
				2, 0);
		
		// Initializes attributes
		this.running = false;
		this.speedSign = 1;
		this.active = true;
		
		// Calculates the collision points
		this.relativeColPoints = new Point2D.Double[5];
		for (int i = 0; i < this.relativeColPoints.length; i++)
		{
			this.relativeColPoints[i] = new Point2D.Double(
					i * getWidth() / (this.relativeColPoints.length - 1), -15);
		}
		
		updateAnimation();
		
		// Adds the object to the handler(s)
		collisionHandler.addCollisionListener(this);
	}
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onSignalEvent(boolean signalType, int inputIndex)
	{
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
		return this.relativeColPoints;
	}

	@Override
	public void onCollision(ArrayList<Double> colpoints, Collidable collided,
			double steps)
	{
		System.out.println("Collides with something");
		
		// When collides with obstacles, moves them either left or right
		if (this.running && collided instanceof Obstacle)
		{
			Obstacle o = (Obstacle) collided;
			
			o.addPosition(4 * this.speedSign, 0);
		}
	}
	
	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		super.drawSelfBasic(g2d);
		
		drawRelativePoints(g2d, this.relativeColPoints);
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	private void updateAnimation()
	{
		if (this.running)
			getSpriteDrawer().setImageSpeed(this.speedSign * 0.1);
		else
			getSpriteDrawer().setImageSpeed(0);
	}
}
