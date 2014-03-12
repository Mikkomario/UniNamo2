package uninamo_manual;

import java.util.ArrayList;

import uninamo_components.ComponentType;
import uninamo_gameplaysupport.TurnHandler;
import uninamo_main.GameSettings;
import uninamo_userinterface.ManualButton;
import uninamo_worlds.Area;
import uninamo_worlds.AreaChanger;
import utopia_backgrounds.Tile;
import utopia_gameobjects.GameObject;
import utopia_resourcebanks.MultiMediaHolder;

/**
 * ManualMaster handles the objects shown in the manual and takes care of 
 * opening and closing the manual.
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public class ManualMaster extends GameObject
{
	// ATTRIBUTES	------------------------------------------------------
	
	private Tile manualBack;
	private AreaChanger areaChanger;
	private ArrayList<DoublePage> pages;
	private int currentPageIndex;
	private ManualPageButton leftPageButton, rightPageButton;
	private ManualButton manualButton;
	
	/**
	 * ManualWidth tells the width of the manual's area
	 */
	protected static final int MANUALWIDTH = 600;
	/**
	 * ManualHeight tells the height of the manual's area
	 */
	protected static final int MANUALHEIGHT = 500;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new manual to the center of the screen. Mouse handling will 
	 * be stopped for the coding area while the manual is shown.
	 * 
	 * @param areaChanger The areaChanger that will handle the transitions 
	 * between areas and give access to different areas.
	 * @param turnHandler The turnHandler that will inform the created test 
	 * components about turn events
	 * @param manualButton The manualButton in the coding area
	 */
	public ManualMaster(AreaChanger areaChanger, TurnHandler turnHandler, 
			ManualButton manualButton)
	{
		super();
		
		// Starts the manual area
		Area manualArea = areaChanger.getArea("manual");
		manualArea.start();
		
		// Initializes attributes
		this.manualButton = manualButton;
		this.areaChanger = areaChanger;
		this.currentPageIndex = 0;
		this.manualBack = new Tile(GameSettings.screenWidth / 2, 
				GameSettings.screenHeight / 2, manualArea.getDrawer(), 
				manualArea.getActorHandler(), 
				MultiMediaHolder.getSpriteBank("manual").getSprite("manualback"), 
				MANUALWIDTH, MANUALHEIGHT);
		this.pages = new ArrayList<DoublePage>();
		
		// Creates buttons
		new ManualCloseButton(GameSettings.screenWidth / 2 + MANUALWIDTH / 2 - 20, 
				GameSettings.screenHeight / 2 - MANUALHEIGHT / 2 + 20, 
				manualArea.getDrawer(), manualArea.getMouseHandler(), 
				manualArea, this);
		this.leftPageButton = new ManualPageButton(ManualPageButton.BACWARD, 
				manualArea.getDrawer(), manualArea.getMouseHandler(), 
				manualArea, this);
		this.rightPageButton = new ManualPageButton(ManualPageButton.FORWARD, 
				manualArea.getDrawer(), manualArea.getMouseHandler(), 
				manualArea, this);
		
		// Creates the page content
		int leftPageX = GameSettings.screenWidth / 2 - MANUALWIDTH / 4;
		int rightPageX = GameSettings.screenWidth / 2 + MANUALWIDTH / 4;
		int pageY = GameSettings.screenHeight / 2;
		
		// Creates the turorial pages
		this.pages.add(new DoublePage(new EmptyPage(), 
				new SimplePage(rightPageX, pageY, 
				MultiMediaHolder.getSpriteBank("manual").getSprite(
				"contentstext"), manualArea.getDrawer())));
		this.pages.add(new DoublePage(new SimplePage(leftPageX, pageY, 
				MultiMediaHolder.getSpriteBank("manual").getSprite("cables"), 
				manualArea.getDrawer()), new SimplePage(rightPageX, pageY, 
				MultiMediaHolder.getSpriteBank("manual").getSprite("signals"), 
				manualArea.getDrawer())));
		
		// Creates a component page for testing
		ComponentInfoHolder componentData = new ComponentInfoHolder();
		this.pages.add(new DoublePage(new EmptyPage(), 
				new ComponentPage(rightPageX, pageY, manualArea.getDrawer(), 
				manualArea.getActorHandler(), manualArea.getMouseHandler(), 
				manualArea, turnHandler, ComponentType.OR, componentData)));
		
		// Opens the first doublePage
		this.pages.get(this.currentPageIndex).open();
		
		// Hides the manual button while the manual is open
		this.manualButton.setInvisible();
		//System.out.println(this.manualButton.isVisible());
		
		// Deactivates the coding area
		areaChanger.getArea("coding").disableOnlyMouse();
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void kill()
	{
		// Kills the background and other stuff and returns to the coding area
		this.manualBack.kill();
		
		for (DoublePage doublePage : this.pages)
		{
			doublePage.kill();
		}
		this.pages.clear();
		
		// Shows the manualButton since the manual is closed
		this.manualButton.setVisible();
		
		//System.out.println("Ends manual, starts coding");
		this.areaChanger.getArea("manual").end();
		this.areaChanger.getArea("coding").returnNormal();
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Closes the current pages and opens the next ones
	 */
	protected void openNextPages()
	{
		// Only opens next pages if possible
		if (!nextPagesLeft())
			return;
		
		// Closes the current pages and opens the next ones
		this.pages.get(this.currentPageIndex).close();
		this.currentPageIndex ++;
		this.pages.get(this.currentPageIndex).open();
	}
	
	/**
	 * Closes the current pages and opens the previous ones
	 */
	protected void openPreviousPages()
	{
		// Only opens previous pages if possible
		if (!previousPagesLeft())
			return;
		
		// Closes the current pages and opens the previous ones
		this.pages.get(this.currentPageIndex).close();
		this.currentPageIndex --;
		this.pages.get(this.currentPageIndex).open();
	}
	
	/**
	 * @return Are there any pages left to open on the left
	 */
	protected boolean previousPagesLeft()
	{
		return this.currentPageIndex > 0;
	}
	
	/**
	 * @return Are there any pages left to open on the right
	 */
	protected boolean nextPagesLeft()
	{
		return this.currentPageIndex < this.pages.size() - 1;
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	private class DoublePage
	{
		// ATTRIBUTES	--------------------------------------------------
		
		private Page leftPage;
		private Page rightPage;
		
		
		// CONSTRUCTOR	--------------------------------------------------
		
		private DoublePage(Page leftPage, Page rightPage)
		{
			// Initializes attributes
			this.leftPage = leftPage;
			this.rightPage = rightPage;
		}
		
		
		// OTHER METHODS	----------------------------------------------
		
		private void open()
		{
			this.leftPage.open();
			this.rightPage.open();
			
			// Informs the buttons about a new page opening
			ManualMaster.this.leftPageButton.onPageChange();
			ManualMaster.this.rightPageButton.onPageChange();
		}
		
		private void close()
		{
			this.leftPage.close();
			this.rightPage.close();
		}
		
		private void kill()
		{
			this.leftPage.kill();
			this.rightPage.kill();
		}
	}
}
