package utopia_graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import utopia_gameobjects.DrawnObject;

/**
 * MultiParagraphTextDrawer draws multiple pieces of text separated by a small 
 * gap
 * 
 * @author Mikko Hilpinen
 */
public class MultiParagraphTextDrawer
{
	// ATTRIBUTES	------------------------------------------------------
	
	private ArrayList<TextDrawer> texts;
	private DrawnObject user;
	private Font font;
	private Color textColor;
	private int width, gap;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new textDrawer with the given stats. New paragraphs must be 
	 * added separately
	 * 
	 * @param font The font used to draw the text
	 * @param textColor The color used to draw the text
	 * @param textAreaWidth The width of the area on which the text is drawn
	 * @param paragraphGap How many pixels will be left empty between each paragraph
	 * @param user The drawnObject that uses the textDrawers. The drawers' 
	 * stats will be updated when the user's transformations are updated (optional)
	 * 
	 * @see #addParagraph(String)
	 */
	public MultiParagraphTextDrawer(Font font, Color textColor, int textAreaWidth, 
			int paragraphGap, DrawnObject user)
	{
		// Initializes attributes
		this.texts = new ArrayList<TextDrawer>();
		this.user = user;
		this.font = font;
		this.textColor = textColor;
		this.width = textAreaWidth;
		this.gap = paragraphGap;
	}

	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Adds a new paragraph to the drawn texts
	 * 
	 * @param paragraph The paragraph to be drawn in the future
	 */
	public void addParagraph(String paragraph)
	{
		this.texts.add(new TextDrawer(paragraph, this.font, this.textColor, 
				this.width, this.user));
	}
	
	/**
	 * Draws the texts to the given position. This method should be called in 
	 * another drawing method like Drawable's drawSelf method.
	 * 
	 * @param g2d The graphics object that does the actual drawing
	 * @param horizontalTranslation How much the top-left corner of the text is 
	 * translated horizontally
	 * @param verticalTranslation How much the top-left corner of the text is 
	 * translated vertically
	 */
	public void drawTexts(Graphics2D g2d, int horizontalTranslation, 
			int verticalTranslation)
	{
		int y = verticalTranslation;
		
		for (int i = 0; i < this.texts.size(); i++)
		{
			y += this.texts.get(i).drawText(g2d, horizontalTranslation, y) + 
					this.gap;
		}
	}
}
