package uninamo_manual;

import java.util.ArrayList;

import omega_graphic.OpenSpriteBank;
import omega_graphic.Tile;
import omega_world.Area;
import omega_world.GameObject;
import uninamo_components.ComponentType;
import uninamo_gameplaysupport.TurnHandler;
import uninamo_machinery.MachineType;
import uninamo_main.GameSettings;
import uninamo_obstacles.ObstacleType;
import uninamo_previous.AreaChanger;
import uninamo_previous.ManualButton;

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
	private BookMark[] bookMarks;
	
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
		super(areaChanger.getArea("manual"));
		
		// Starts the manual area
		Area manualArea = areaChanger.getArea("manual");
		manualArea.start();
		
		// Initializes attributes
		this.manualButton = manualButton;
		this.areaChanger = areaChanger;
		this.currentPageIndex = 0;
		this.manualBack = new Tile(GameSettings.screenWidth / 2, 
				GameSettings.screenHeight / 2, 
				OpenSpriteBank.getSpriteBank("manual").getSprite("manualback"), 
				MANUALWIDTH, MANUALHEIGHT, manualArea);
		this.pages = new ArrayList<DoublePage>();
		
		// Creates buttons
		new ManualCloseButton(GameSettings.screenWidth / 2 + MANUALWIDTH / 2 - 20, 
				GameSettings.screenHeight / 2 - MANUALHEIGHT / 2 + 20, 
				this, manualArea);
		this.leftPageButton = new ManualPageButton(ManualPageButton.BACKWARDS, 
				this, manualArea);
		this.rightPageButton = new ManualPageButton(ManualPageButton.FORWARD, 
				this, manualArea);
		
		// Creates the page content
		int leftPageX = GameSettings.screenWidth / 2 - MANUALWIDTH / 4;
		int rightPageX = GameSettings.screenWidth / 2 + MANUALWIDTH / 4;
		int pageY = GameSettings.screenHeight / 2;
		
		// Creates the turorial pages
		this.pages.add(new DoublePage(new EmptyPage(manualArea), 
				new SimplePage(rightPageX, pageY, 
				OpenSpriteBank.getSpriteBank("manual").getSprite(
				"contentstext"), manualArea)));
		this.pages.add(new DoublePage(new SimplePage(leftPageX, pageY, 
				OpenSpriteBank.getSpriteBank("manual").getSprite("cables"), 
				manualArea), new SimplePage(rightPageX, pageY, 
				OpenSpriteBank.getSpriteBank("manual").getSprite("signals"), 
				manualArea)));
		
		// Creates a component page for testing
		ComponentInfoHolder componentData = new ComponentInfoHolder();
		this.pages.add(new DoublePage(new EmptyPage(manualArea), 
				new ComponentPage(rightPageX, pageY, manualArea, turnHandler, 
				ComponentType.OR, componentData)));
		
		// Creates a machine page for testing
		MachineInfoHolder machineData = new MachineInfoHolder();
		this.pages.add(new DoublePage(new EmptyPage(manualArea), 
				new MachinePage(rightPageX, pageY, 
				manualArea, MachineType.CONVEYORBELT, machineData)));
		
		// Creates an obstacle page for testing
		ObstacleInfoHolder obstacleData = new ObstacleInfoHolder();
		this.pages.add(new DoublePage(new EmptyPage(manualArea), new ObstaclePage(
				rightPageX, pageY, manualArea, ObstacleType.BOX, obstacleData)));
		
		// Creates the bookmarks
		int[] pagenumbers = new int[5];
		pagenumbers[0] = 0; // Contents
		pagenumbers[1] = 1; // Basics
		pagenumbers[2] = 2; // Components
		pagenumbers[3] = 3; // Machines
		pagenumbers[4] = 4; // Obstacles
		
		this.bookMarks = new BookMark[5];
		for (int i = 0; i < 5; i++)
		{
			this.bookMarks[i] = new BookMark(
					30 + (int) ((i / 5.0) * (MANUALWIDTH / 2 - 40)), pagenumbers[i], 
					i, manualArea, this);
		}
		
		// Opens the first doublePage
		this.pages.get(this.currentPageIndex).open();
		
		// Hides the manual button while the manual is open
		this.manualButton.setInvisible();
		//System.out.println(this.manualButton.isVisible());
		
		// Deactivates the coding area
		areaChanger.getArea("coding").getMouseHandler().inactivate();
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
		this.areaChanger.getArea("coding").getMouseHandler().activate();
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
	
	/**
	 * Changes the page to a certain index. The pages are indexed as double 
	 * pages
	 * 
	 * @param newPageIndex The index of the new page(s) opened
	 */
	protected void goToPage(int newPageIndex)
	{
		if (this.currentPageIndex == newPageIndex || newPageIndex < 0 || 
				newPageIndex >= this.pages.size())
			return;
		
		this.pages.get(this.currentPageIndex).close();
		this.currentPageIndex = newPageIndex;
		this.pages.get(this.currentPageIndex).open();
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
			
			// Informs the bookmarks
			for (int i = 0; i < ManualMaster.this.bookMarks.length; i++)
			{
				ManualMaster.this.bookMarks[i].onPageChange(
						ManualMaster.this.currentPageIndex);
			}
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
