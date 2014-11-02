package uninamo_manual;

import omega_graphic.Sprite;
import omega_graphic.Tile;
import omega_world.Area;
import omega_world.GameObject;

/**
 * Simple page is a page that contains a single image about the basics of the 
 * game.
 * 
 * @author Mikko Hilpinen
 */
public class SimplePage extends GameObject implements Page
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Tile image;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new page containing the given image
	 * 
	 * @param x The x-coordinate of the center of the page
	 * @param y The y-coordinate of the center of the page
	 * @param tutorialSprite The sprite that contains the turorial info
	 * @param area The area where the object will reside at
	 */
	public SimplePage(int x, int y, Sprite tutorialSprite, Area area)
	{
		super(area);
		
		// Initializes attributes
		this.image = new Tile(x, y, tutorialSprite, 
				ManualMaster.MANUALWIDTH / 2, ManualMaster.MANUALHEIGHT, area);
		
		// Hides the image
		close();
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void open()
	{
		// Shows the tile
		this.image.setVisible();
	}

	@Override
	public void close()
	{
		this.image.setInvisible();
	}
	
	@Override
	public void kill()
	{
		// Also kills the image
		this.image.kill();
		super.kill();
	}
}
