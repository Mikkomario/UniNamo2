package uninamo_manual;

import utopia_backgrounds.Tile;
import utopia_gameobjects.GameObject;
import utopia_graphic.Sprite;
import utopia_handlers.DrawableHandler;

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
	 * @param drawer The drawableHandler that will draw the page's contents
	 */
	public SimplePage(int x, int y, Sprite tutorialSprite, 
			DrawableHandler drawer)
	{
		// Initializes attributes
		this.image = new Tile(x, y, drawer, null, tutorialSprite, 
				ManualMaster.MANUALWIDTH / 2, ManualMaster.MANUALHEIGHT);
		
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
