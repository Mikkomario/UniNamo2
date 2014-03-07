package utopia_interfaceElements;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import utopia_gameobjects.DrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_graphic.Sprite;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;

// TODO: Consider adding a width / height system that allows rescaling 
// of the background sprite without scaling the text size

/**
 * Messageboxes are used to present information to the user in text format. 
 * Messageboxes can be drawn on screen and manipulated like any DrawnObject
 * 
 * @author Mikko Hilpinen
 * created 8.1.2014
 */
public class MessageBox extends DrawnObject
{
	// ATTRIBUTES	-----------------------------------------------------
	
	/**
	 * Margin is the empty amount of pixels left to the each side of the box
	 */
	protected static final int MARGIN = 15;
	
	private SingleSpriteDrawer spritedrawer;
	private String message;
	// The iterator used in drawing the message on the box
	private AttributedCharacterIterator styledtextiterator;
	private Font font;
	private Color color;
	
	
	// CONSTRUCTOR	-----------------------------------------------------

	/**
	 * Creates a new abstract message to the given position with the given 
	 * message and background.
	 * 
	 * @param x The x-coordinate of the box's center
	 * @param y The y-coordinate of the box's center
	 * @param depth The drawing depth of the box
	 * @param message The message shown in the box
	 * @param textfont The font used in drawing the message
	 * @param textcolor What color is used when drawing the text
	 * @param backgroundsprite The sprite used to draw the background of the 
	 * messageBox
	 * @param drawer The drawablehandler that will draw the messagebox (optional)
	 * @param actorhandler The actorhandler that will animate the message 
	 * background (optional)
	 */
	public MessageBox(int x, int y, int depth, String message, 
			Font textfont, Color textcolor, Sprite backgroundsprite, 
			DrawableHandler drawer, ActorHandler actorhandler)
	{
		super(x, y, depth, drawer);

		// Initializes the attributes
		this.spritedrawer = new SingleSpriteDrawer(backgroundsprite, 
				actorhandler, this);
		this.message = message + "";
		this.font = textfont;
		this.color = textcolor;
		
		AttributedString attstring = new AttributedString(this.message);
		attstring.addAttribute(TextAttribute.FONT, this.font);
		
		this.styledtextiterator = attstring.getIterator();
		
		// Adds the object to the handler(s)
		if (drawer != null)
			drawer.addDrawable(this);
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	public int getOriginX()
	{
		if (this.spritedrawer == null)
			return 0;
		
		return this.spritedrawer.getSprite().getWidth() / 2;
	}

	@Override
	public int getOriginY()
	{
		if (this.spritedrawer == null)
			return 0;
		
		return this.spritedrawer.getSprite().getHeight() / 2;
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// TODO: Test the positioning that it's right
		
		// Draws the background
		if (this.spritedrawer != null)
			this.spritedrawer.drawSprite(g2d, 0, 0);
		
		// And then the text
		g2d.setFont(this.font);
		g2d.setColor(this.color);
		
		// From: http://docs.oracle.com/javase/7/docs/api/java/awt/font/LineBreakMeasurer.html
		Point pen = new Point(MARGIN, MARGIN);
		FontRenderContext frc = g2d.getFontRenderContext();

		// "let styledText be an AttributedCharacterIterator containing at least
		// one character"
		
		LineBreakMeasurer measurer = new LineBreakMeasurer(this.styledtextiterator, frc);
		float wrappingWidth = this.spritedrawer.getSprite().getWidth() - MARGIN;
		
		while (measurer.getPosition() < this.message.length()/*fStyledText.length()*/)
		{
			TextLayout layout = measurer.nextLayout(wrappingWidth);
		
		    pen.y += (layout.getAscent());
		    float dx = layout.isLeftToRight() ?
		    		 0 : (wrappingWidth - layout.getAdvance());
		
		    layout.draw(g2d, pen.x + dx, pen.y);
		    pen.y += layout.getDescent() + layout.getLeading();
		}
		
		// From oracle docs: 
		// http://docs.oracle.com/javase/7/docs/api/java/awt/font/TextLayout.html
		/*
		FontRenderContext frc = g2d.getFontRenderContext();
		TextLayout layout = new TextLayout(this.message, this.font, frc);
		layout.draw(g2d, (float) topLeft.getX(), (float) topLeft.getY());

		Rectangle2D bounds = layout.getBounds();
		bounds.setRect(
				bounds.getX() + topLeft.getX(), 
				bounds.getY() + topLeft.getY(), 
				bounds.getWidth(), bounds.getHeight());
		g2d.draw(bounds);
		*/
		//g2d.setFont(this.font);
		//g2d.drawString(this.message, - getOriginX(), - getOriginY());
	}
}
