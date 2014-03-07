package utopia_worlds;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import utopia_backgrounds.Background;
import utopia_backgrounds.TileMap;
import utopia_handleds.Collidable;
import utopia_handlers.ActorHandler;
import utopia_handlers.CollidableHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.HelpMath;
import utopia_resourcebanks.SpriteBank;

/**
 * Dimensional room is a room that has a position and size
 *
 * @author Mikko Hilpinen.
 *         Created 12.7.2013.
 * @warning Changing the position of the room doesn't work well if the room 
 * is active. Please end the room before moving it. Also remember to move 
 * the map, background and the objects in the room as well
 */
public class DimensionalRoom extends TiledRoom implements Collidable
{
	// ATTRIBUTES	------------------------------------------------------
	
	private int width, height;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new dimensionalroom with the given size to the given position. 
	 * A new tilemap is also created into the room using the given information. 
	 * The room has the given background(s). The room is inctive until started
	 * 
	 * @param x The room's x-coordinate (in pixels)
	 * @param y The room's y-coordinate (in pixels)
	 * @param drawer The drawableHandler that will draw the contents of the room
	 * @param animator The actorhandler that will animate the background(s) and tiles 
	 * in the room (optional)
	 * @param collidablehandler The collidableHandler that will handle the room's 
	 * collision checking (optional)
	 * @param width The width of the room (in pixels)
	 * @param height The height of the room (in pixels)
	 * @param xtiles How many tiles the room has horizontally (>= 0)
	 * @param ytiles How many tiles the room has vertically (>= 0)
	 * @param bankindexes A table telling which index for a spritebank is 
	 * used in which tile
	 * @param rotations A table telling how much each tile is rotated
	 * @param xscales A table telling how the tiles are flipped around the x-axis
	 * @param yscales A table telling how the tiles are flipped around the y-axis
	 * @param nameindexes A table telling which index is used for each tile to 
	 * find their spritename in a spritebank
	 * @param backgrounds The background(s) used in the room
	 * @param tiletexturebanks A list of spritebanks containing the textures used in tiles
	 * @param tiletexturenames A list of names of the tiletextures
	 * @see utopia_worlds.Room#start()
	 * @see utopia_worlds.Room#end()
	 */
	public DimensionalRoom(int x, int y, DrawableHandler drawer, 
			ActorHandler animator, CollidableHandler collidablehandler, 
			int width, int height, int xtiles, int ytiles, 
			short[] bankindexes, short[] rotations, short[] xscales, 
			short[] yscales, short[] nameindexes, 
			ArrayList<Background> backgrounds, 
			ArrayList<SpriteBank> tiletexturebanks,
			ArrayList<String> tiletexturenames)
	{
		super(backgrounds, new TileMap(x, y, drawer, animator, 
				collidablehandler, xtiles, ytiles, width / xtiles, 
				height / ytiles, bankindexes, rotations, xscales, yscales, 
				nameindexes), tiletexturebanks, tiletexturenames);
		
		// Initializes attributes
		this.width = width;
		this.height = height;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public boolean pointCollides(Point2D testPoint)
	{
		// Room uses tilemap for collision checking
		if (getTiles() != null)
			return getTiles().pointCollides(testPoint);
		else
			return false;
	}

	@Override
	public boolean isSolid()
	{
		// Uses tilemap for collision checking
		if (getTiles() != null)
			return getTiles().isSolid();
		else
			return false;
	}

	@Override
	public void makeSolid()
	{
		// Uses tilemap for collision checking
		if (getTiles() != null)
			getTiles().makeSolid();
	}

	@Override
	public void makeUnsolid()
	{
		// Uses tilemap for collision checking
		if (getTiles() != null)
			getTiles().makeUnsolid();
	}
	
	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		if (getTiles() != null)
			return getTiles().getSupportedListenerClasses();
		else
			return null;
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return The width of the room (in pixels)
	 */
	public int getWidth()
	{
		return this.width;
	}
	
	/**
	 * @return The height of the room (in pixels)
	 */
	public int getHeight()
	{
		return this.height;
	}
	
	/**
	 * @return The room's top-left x-coordinate
	 */
	public double getX()
	{
		return getTiles().getX();
	}
	
	/**
	 * @return The room's top-left y-coordinate
	 */
	public double getY()
	{
		return getTiles().getY();
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Transforms coordinates relative to room's position into absolute coordinates.
	 *
	 * @param x The x-coordinate from the top left corner of the room (in pixels)
	 * @param y The y-coordinate from the top left corner of the room (in pixels)
	 * @return The absolute position
	 */
	public Point2D.Double getTransformedPosition(int x, int y)
	{
		return new Point2D.Double(getX() + x, getY() + y);
	}
	
	/**
	 * Transforms the absolute coordinates into coordinates relative to the room's position
	 *
	 * @param x The absolute x-coordinate to be transformed
	 * @param y The absolute y-coordinate to be transformed
	 * @return A point relative to the room's coordinates
	 */
	public Point2D.Double getNegatedPosition(int x, int y)
	{
		return new Point2D.Double(x - getX(), y - getY());
	}
	
	/**
	 * Checks whether an absolute position is within room borders
	 *
	 * @param absp The absolute point to be tested
	 * @return Is the point within the room borders
	 */
	public boolean absolutePointIsInRoom(Point2D.Double absp)
	{
		return HelpMath.pointIsInRange(absp, (int) getX(), 
				(int) getX() + getWidth(), (int) getY(), 
				(int) getY() + getHeight());
	}
	
	/**
	 * Checks whether an relative position is within room borders
	 *
	 * @param relp The relative point to be tested
	 * @return Is the point within the room borders
	 */
	public boolean relativePointIsInRoom(Point2D.Double relp)
	{
		return HelpMath.pointIsInRange(relp, 0, getWidth(), 0, getHeight());
	}
}
