package utopia_video;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.DepthConstants;

/**
 * Gamepanel is a single panel in the gamescreen that draws numerous drawables. 
 * Gamepanels are used in GameWindows
 * 
 * @author Unto Solala & Mikko Hilpinen. Created 8.8.2013
 * @see utopia_video.GameWindow
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel
{
	// ATTRIBUTES ---------------------------------------------------------
	
	private int width;
	private int height;
	private DrawableHandler drawer;
	private double xscale, yscale;
	
	
	// CONSTRUCTOR ---------------------------------------------------------
	
	/**
	 * Creates a new panel with default color being white
	 * 
	 * @param width	Panel's width (in pixels)
	 * @param height Panel's height (in pixels)
	 */
	public GamePanel(int width, int height)
	{
		// Initializes attributes
		this.xscale = 1;
		this.yscale = 1;
		this.width = width;
		this.height = height;
		// TODO: Check that 5 is good
		this.drawer = new DrawableHandler(false, true, DepthConstants.NORMAL, 5,
				null);
		
		//Let's format our panel
		this.formatPanel();
		
		//And make it visible
		this.setVisible(true);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void paintComponent(Graphics g)
	{
		// The panel draws all stuff inside it
		Graphics2D g2d = (Graphics2D) g;
		
		// Scales the area of drawing
		if (this.xscale != 1 || this.yscale != 1)
			g2d.scale(this.xscale, this.yscale);
		
		// Clears the former drawings
		g2d.clearRect(0, 0, this.width, this.height);
		
		this.drawer.drawSelf(g2d);
	}
	
	
	// PRIVATE METHODS ---------------------------------------------------
	
	private void formatPanel()
	{
		//Let's set the panel's size...
		this.setSizes(this.width, this.height);
		//...And color
		//this.setBackground(new Color(0,50,150));
		//setBackgroundColor(255, 255, 255);
	}
	
	
	// OTHER METHODS ---------------------------------------------------
	
	/**
	 * Changes the size of the game panel.
	 * 
	 * @param width	Panel's new width (in pixels)
	 * @param height Panel's new height (in pixels)
	 */
	public void setSizes(int width, int height)
	{
		this.setSize(width, height);
		Dimension preferred = new Dimension(width, height);
		this.setPreferredSize(preferred);
		this.setMinimumSize(preferred);
		this.setMaximumSize(preferred);
	}
	
	/**
	 * Changes the panel's background color.
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setBackgroundColor(int red, int green, int blue)
	{
		this.setBackground(new Color(red, green, blue));
	}
	
	/**
	 * Makes the panel visible.
	 */
	public void makeVisible()
	{
		this.setVisible(true);
	}
	
	/**
	 * Makes the panel invisible.
	 */
	public void makeInvisible()
	{
		this.setVisible(false);
	}
	
	/**
	 * @return The drawablehandler that draws the content of this panel
	 */
	public DrawableHandler getDrawer()
	{
		return this.drawer;
	}
	
	/**
	 * Scales the panel, keeping the same resolution but changing the size 
	 * of the area. The scaling is relative to the former scaling of the panel
	 *
	 * @param xscale How much the panel is scaled horizontally (1 = no scaling) 
	 * @param yscale How much the panel is scaled vertically (1 = no scaling)
	 */
	protected void scale(double xscale, double yscale)
	{
		// The scaling is relative to the former scaling
		setScale(xscale * this.xscale, yscale * this.yscale);
	}
	
	/**
	 * Scales the panel, keeping the same resolution but changing the size 
	 * of the area
	 *
	 * @param xscale How much the panel is scaled horizontally (1 = no scaling) 
	 * @param yscale How much the panel is scaled vertically (1 = no scaling)
	 */
	protected void setScale(double xscale, double yscale)
	{
		//System.out.println("Sets scaling to " + xscale + ", " + yscale);
		
		// Remembers the scaling
		this.xscale = xscale;
		this.yscale = yscale;
		
		// Resizes the panel
		setSizes((int) (this.width * this.xscale), 
				(int) (this.height * this.yscale));
	}
}
