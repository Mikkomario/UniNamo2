package utopia_graphic;

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
import utopia_listeners.TransformationListener;

/**
 * TextDrawer draws text over a certain area. The text is broken into lines in 
 * a working fashion. The textDrawer is not independent and should be used by 
 * a drawnObject to draw text.
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public class TextDrawer implements TransformationListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private String text;
	private AttributedCharacterIterator styledtextiterator;
	private Font font;
	private Color color;
	private int areaWidth;
	
	private LineBreakMeasurer measurer;
	private boolean measurerNeedsUpdating, dead;
	private DrawnObject user;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new textDrawer that can be used to draw text with the given 
	 * parameters
	 * 
	 * @param text The text that will be drawn (can be changed later)
	 * @param font The font with which the text will be drawn
	 * @param textColor The color with which the text will be drawn
	 * @param textAreaWidth The width of the area the text will cover 
	 * (No transformations are included here)
	 * @param user The user to which this textDrawer is tied to. If null, the 
	 * drawer won't update its status and won't die unless specifically called
	 */
	public TextDrawer(String text, Font font, Color textColor, int textAreaWidth, 
			DrawnObject user)
	{
		// Initializes attributes
		this.text = text;
		this.font = font;
		this.color = textColor;
		this.areaWidth = textAreaWidth;
		setText(text);
		this.measurer = null;
		this.measurerNeedsUpdating = true;
		this.dead = false;
		this.user = user;
		
		if (user != null)
			user.getTransformationListenerHandler().addListener(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public boolean isActive()
	{
		return true;
	}

	@Override
	public void activate()
	{
		// Can't be deactivated
	}

	@Override
	public void inactivate()
	{
		// Can't be deactivated
	}

	@Override
	public boolean isDead()
	{
		return this.dead || this.user.isDead();
	}

	@Override
	public void kill()
	{
		this.dead = true;
	}

	@Override
	public void onTransformationEvent(TransformationEvent e)
	{
		this.measurerNeedsUpdating = true;
	}

	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * Changes the text that will be drawn
	 * 
	 * @param newText The new text that will be drawn
	 */
	public void setText(String newText)
	{
		if (newText == null)
			return;
		
		this.text = newText;
		
		// Updates the attributedString
		AttributedString attstring = new AttributedString(this.text);
		attstring.addAttribute(TextAttribute.FONT, this.font);
		
		this.styledtextiterator = attstring.getIterator();
		
		this.measurerNeedsUpdating = true;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Draws the text to the given position. This method should be called in 
	 * another drawing method like Drawable's drawSelf method.
	 * 
	 * @param g2d The graphics object that does the actual drawing
	 * @param horizontalTranslation How much the top-left corner of the text is 
	 * translated horizontally
	 * @param verticalTranslation How much the top-left corner of the text is 
	 * translated vertically
	 * @return How high the written text was
	 */
	public int drawText(Graphics2D g2d, int horizontalTranslation, int verticalTranslation)
	{
		// And then the text
		//g2d.setFont(this.font);
		g2d.setColor(this.color);
		
		// From: http://docs.oracle.com/javase/7/docs/api/java/awt/font/LineBreakMeasurer.html
		Point pen = new Point(horizontalTranslation, verticalTranslation);
		
		if (this.measurerNeedsUpdating)
			updateLineBreakMeasurer(g2d);
		
		float wrappingWidth = this.areaWidth;
		
		while (this.measurer.getPosition() < this.text.length()/*fStyledText.length()*/)
		{
			TextLayout layout = this.measurer.nextLayout(wrappingWidth);
		
		    pen.y += (layout.getAscent());
		    float dx = layout.isLeftToRight() ?
		    		 0 : (wrappingWidth - layout.getAdvance());
		
		    layout.draw(g2d, pen.x + dx, pen.y);
		    pen.y += layout.getDescent() + layout.getLeading();
		}
		
		return pen.y - verticalTranslation;
		
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
	
	private void updateLineBreakMeasurer(Graphics2D g2d)
	{
		FontRenderContext frc = g2d.getFontRenderContext();

		// "let styledText be an AttributedCharacterIterator containing at least
		// one character"
		
		this.measurer = 
				new LineBreakMeasurer(this.styledtextiterator, frc);
	}
}
