package uninamo_previous;

import gateway_interface.ParagraphDrawer;
import genesis_graphic.DepthConstants;

import java.awt.Color;
import java.awt.Graphics2D;

import omega_graphic.DrawnObject;
import omega_world.Area;
import omega_world.GameObject;
import uninamo_main.GameSettings;
import uninamo_manual.ManualMaster;

/**
 * A descriptionPage contains a description of some object of the game and 
 * perhaps something else as well (test object or picture)
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 * @deprecated replaced with page
 */
public abstract class DescriptionPage extends DrawnObject implements Page
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private GameObject testObject;
	private ParagraphDrawer textDrawer;
	private Area area;
	private String pagename;
	
	
	// CONSTRUCTOR	----------------------------------------------------

	/**
	 * Creates a new description page to the given position containing the 
	 * given text
	 * 
	 * @param x The x-coordinate of the center of the page
	 * @param y The y-coordinate of the center of the page
	 * @param area The area the page is on
	 * @param description the description on the page
	 * @param pagename The name shown at the top of the page 
	 */
	public DescriptionPage(int x, int y, Area area, String description, 
			String pagename)
	{
		super(x, y, DepthConstants.NORMAL, area);
		
		// Initializes attributes
		this.testObject = null;
		this.textDrawer = new ParagraphDrawer(description, GameSettings.basicFont, 
				Color.BLACK, ManualMaster.MANUALWIDTH / 2 - 50, this);
		this.area = area;
		this.pagename = pagename;
		
		// Hides the page
		setInvisible();
	}
	
	
	// ABSTRACT METHODS	--------------------------------------------------
	
	/**
	 * Creates a new testObject (or doesn't)
	 * 
	 * @param area The area where the object will be put to
	 * @return The object that was created or null if no test object is needed
	 */
	protected abstract GameObject createTestObject(Area area);
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void open()
	{
		this.testObject = createTestObject(this.area);
		setVisible();
	}

	@Override
	public void close()
	{
		if (this.testObject != null)
		{
			this.testObject.kill();
			this.testObject = null;
		}
		setInvisible();
	}

	@Override
	public int getOriginX()
	{
		return ManualMaster.MANUALWIDTH / 4;
	}

	@Override
	public int getOriginY()
	{
		return ManualMaster.MANUALHEIGHT / 2;
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// Draws the text
		if (this.textDrawer != null)
			this.textDrawer.drawText(g2d, 32, 256);
		
		// Draws the headline
		g2d.drawString(this.pagename, 32, 50);
	}

	@Override
	public void kill()
	{
		// Also kills the test object
		if (this.testObject != null)
			this.testObject.kill();
		this.textDrawer = null;
		
		super.kill();
	}
}
