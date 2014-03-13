package uninamo_userinterface;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D.Double;

import uninamo_main.GameSettings;
import uninamo_worlds.Area;
import utopia_graphic.MultiParagraphTextDrawer;
import utopia_helpAndEnums.DepthConstants;
import utopia_interfaceElements.AbstractButton;
import utopia_resourcebanks.MultiMediaHolder;

/**
 * Notes present information about the current mission and can be clicked to get rid of
 * 
 * @author Mikko Hilpinen
 * @since 13.3.2014
 */
public class Note extends AbstractButton
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private MultiParagraphTextDrawer textDrawer;
	private int verticalMargin;
	private int horizontalMargin;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new note to the given position with the given layout
	 * 
	 * @param x The x-coordinate of the note's origin
	 * @param y The y-coordinate of the note's origin
	 * @param horizontalMargin How many empty pixels there are to the left & 
	 * right of the text
	 * @param verticalMargin How many empty pixels there are above the text
	 * @param spriteName The name of the sprite used on the background
	 * @param content The contents of the note. # marks a paragraph change
	 * @param area The area where the notes are shown
	 */
	public Note(int x, int y, int horizontalMargin, int verticalMargin, 
			String spriteName, String content, Area area)
	{
		super(x, y, DepthConstants.FOREGROUND, 
				MultiMediaHolder.getSpriteBank("mission").getSprite(spriteName), 
				area.getDrawer(), area.getMouseHandler(), area);
		
		// Initializes attributes
		this.textDrawer = new MultiParagraphTextDrawer(GameSettings.basicFont, 
				Color.BLACK, getWidth() - horizontalMargin * 2, 10, this);
		this.textDrawer.addText(content, "#");
		this.verticalMargin = verticalMargin;
		this.horizontalMargin = horizontalMargin;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Double mousePosition,
			double eventStepTime)
	{
		// On left click, dies
		if (button == MouseButton.LEFT && eventType == MouseButtonEventType.PRESSED)
			kill();
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		return true;
	}

	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Double mousePosition, double eventStepTime)
	{
		// On enter, scales and changes image, on exit returns
		if (eventType == MousePositionEventType.ENTER)
		{
			setScale(GameSettings.interfaceScaleFactor, 
					GameSettings.interfaceScaleFactor);
			getSpriteDrawer().setImageIndex(1);
		}
		else if (eventType == MousePositionEventType.EXIT)
		{
			setScale(1, 1);
			getSpriteDrawer().setImageIndex(0);
		}
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		super.drawSelfBasic(g2d);
		
		// Also draws the text
		if (this.textDrawer != null)
			this.textDrawer.drawTexts(g2d, this.horizontalMargin, 
					this.verticalMargin);
	}
}
