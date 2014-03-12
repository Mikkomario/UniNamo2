package uninamo_manual;

import java.awt.geom.Point2D.Double;

import uninamo_main.GameSettings;
import uninamo_worlds.Area;
import utopia_helpAndEnums.DepthConstants;
import utopia_interfaceElements.AbstractButton;
import utopia_resourcebanks.MultiMediaHolder;

/**
 * By clicking a bookMark, the user can go to a certain double page on the manual
 * 
 * @author Mikko Hilpinen
 * @since 13.3.2014
 */
public class BookMark extends AbstractButton
{
	// ATTRIBUTES	------------------------------------------------------
	
	private int pageIndex;
	private int relativeX;
	private ManualMaster master;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new bookMark with the given number and page
	 * 
	 * @param x How much to the right the bookMark is from the center of the 
	 * manual
	 * @param pageIndex What page clicking the bookMark will take the user to
	 * @param number Which number is shown on the bookMark
	 * @param area The area where the bookMark is located at
	 * @param master The manualMaster that handles the page changing
	 */
	public BookMark(int x, int pageIndex, int number, Area area, 
			ManualMaster master)
	{
		super(GameSettings.screenWidth / 2 + x, 
				GameSettings.screenHeight / 2 - ManualMaster.MANUALHEIGHT / 2, 
				DepthConstants.BOTTOM, 
				MultiMediaHolder.getSpriteBank("manual").getSprite("bookmark"), 
				area.getDrawer(), area.getMouseHandler(), area);
		
		// Initializes attributes
		this.pageIndex = pageIndex;
		this.relativeX = x;
		this.master = master;
		
		// Sets the corrent image
		getSpriteDrawer().setImageIndex(number);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Double mousePosition,
			double eventStepTime)
	{
		if (button == MouseButton.LEFT && eventType == MouseButtonEventType.PRESSED)
			this.master.goToPage(this.pageIndex);
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
		// Enter scales, exit rescales
		if (eventType == MousePositionEventType.ENTER)
			setScale(GameSettings.interfaceScaleFactor, 
					GameSettings.interfaceScaleFactor);
		else if (eventType == MousePositionEventType.EXIT)
			setScale(1, 1);
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * This method should be called when a new page is opened
	 * 
	 * @param newPageIndex The index of the new (double) page
	 */
	public void onPageChange(int newPageIndex)
	{
		// Changes position
		if (newPageIndex < this.pageIndex)
			setX(GameSettings.screenWidth / 2 + this.relativeX);
		else
			setX(GameSettings.screenWidth / 2 - this.relativeX);
		
		// If the page is the same as the pageIndex, changes depth, otherwise 
		// rechanges if (if necessary)
		/* TODO: This didn't work for some reason...
		if (newPageIndex == this.pageIndex)
			setDepth(DepthConstants.NORMAL);
		else if (getDepth() == DepthConstants.NORMAL)
			setDepth(DepthConstants.BOTTOM);
		*/
	}
}
