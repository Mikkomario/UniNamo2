package utopia_graphic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import utopia_resourcebanks.BankObject;


/**
 * This object represents a drawn image that can be animated. Sprites are
 * meant to be used in multiple objects and those objects should handle the
 * animation (this class merely loads and provides all the neccessary images)
 *
 * @author Mikko Hilpinen.
 *         Created 27.11.2012.
 */
public class Sprite implements BankObject
{	
	// ATTRIBUTES	-------------------------------------------------------
	
	private BufferedImage[] images;
	
	private int origX, origY;
	private boolean dead;
	
	
	// CONSTRUCTOR	-------------------------------------------------------
	
	/**
	 * This method creates a new sprite based on the information provided by 
	 * the caller. The images are loaded from a strip that contains one or more 
	 * images.
	 *
	 * @param filename The location of the loaded image (data/ is added 
	 * automatically to the beginning)
	 * @param numberOfImages How many separate images does the strip contain?
	 * @param originX the x-coordinate of the sprite's origin (Pxl)
	 * @param originY the y-coordinate of the sprite's origin (Pxl)
	 */
	public Sprite(String filename, int numberOfImages, int originX, int originY)
	{
		// Checks the variables
		if (filename == null || numberOfImages <= 0)
			throw new IllegalArgumentException();
		
		//System.out.println("loads sprite " + filename);
		
		// Initializes attributes
		this.origX = originX;
		this.origY = originY;
		this.dead = false;
		
		// Loads the image
		File img = new File("data/" + filename);
		BufferedImage strip = null;
		
		try
		{
			strip = ImageIO.read(img);
		}
		catch (IOException ioe)
		{
			System.err.println(this + " failed to load the image data/" + 
					filename);
			ioe.printStackTrace();
			return;
		}
		
		// Creates the subimages
		this.images = new BufferedImage[numberOfImages];
		
		// Calculates the subimage width
		int sw = strip.getWidth() / numberOfImages;
		
		for (int i = 0; i < numberOfImages; i++)
		{
			// Calculates the needed variables
			int sx;
			sx = i*sw;
			
			this.images[i] = strip.getSubimage(sx, 0, sw, strip.getHeight());
		}
	}
	
	
	// IMPLEMENTED METHODS	-----------------------------------------------
	
	@Override
	public void kill()
	{
		// Doesn't do much since the image information will be released 
		// automatically once the sprite is not held anywhere anymore
		this.dead = true;
	}
	
	@Override
	public boolean isDead()
	{
		return this.dead;
	}
	
	
	// GETTERS & SETTERS	------------------------------------------------
	
	/**
	 * @return returns how many subimages exist within this sprite
	 */
	public int getImageNumber()
	{
		return this.images.length;
	}
	
	/**
	 * @return The x-coordinate of the origin of the sprite (relative pixel)
	 */
	public int getOriginX()
	{
		return this.origX;
	}
	
	/**
	 * @return The y-coordinate of the origin of the sprite (relative pixel)
	 */
	public int getOriginY()
	{
		return this.origY;
	}
	
	/**
	 * @return How wide a single subimage is (pixels)
	 */
	public int getWidth()
	{
		return getSubImage(0).getWidth();
	}
	
	/**
	 * @return How tall a single subimage is (pixels)
	 */
	public int getHeight()
	{
		//System.out.println(this.strip.height);
		return getSubImage(0).getHeight();
	}
	
	
	// METHODS	------------------------------------------------------------
	
	/**
	 * This method returns a single subimage from the sprite.
	 *
	 * @param imageIndex The index of the image to be drawn [0, numberOfImages[
	 * @return The subimage from the given index
	 * @see #getImageNumber()
	 */
	public BufferedImage getSubImage(int imageIndex)
	{
		// Checks the given index and adjusts it if needed
		if (imageIndex < 0 || imageIndex >= this.images.length)
			imageIndex = Math.abs(imageIndex % this.images.length);
		
		return this.images[imageIndex];
	}
	
	// TODO: If you get bored, try to implement filters into the project
	// check: http://docs.oracle.com/javase/tutorial/2d/images/drawimage.html
}
