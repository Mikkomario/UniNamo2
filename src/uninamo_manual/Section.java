package uninamo_manual;

import genesis_event.HandlerRelay;
import genesis_util.Vector3D;

import java.util.ArrayList;
import java.util.List;

import arc_bank.Bank;
import uninamo_components.ComponentType;
import uninamo_machinery.MachineType;
import uninamo_obstacles.ObstacleType;
import vision_sprite.Sprite;
import vision_sprite.SpriteBank;

/**
 * Section is a set of pages that are shown in pairs (openings)
 * @author Mikko Hilpinen
 * @since 16.7.2015
 */
public class Section
{
	// ATTRIBUTES	-------------------------
	
	private List<Opening> openings;
	private int openIndex;
	
	
	// CONSTRUCTOR	-------------------------
	
	/**
	 * Creates a new section that will contain the given set of pages.
	 * @param pages The pages that form this section (excluding the first empty page)
	 */
	public Section(List<? extends Page> pages)
	{
		this.openings = new ArrayList<>();
		this.openIndex = -1;
		
		for (int i = 0; i < pages.size(); i += 2)
		{
			this.openings.add(new Opening(pages, i));
		}
	}
	
	
	// OTHER METHODS	---------------------
	
	/**
	 * @return Is the section currently opened
	 */
	public boolean isOpen()
	{
		return this.openIndex >= 0 && this.openIndex < this.openings.size();
	}
	
	/**
	 * Closes the section for now
	 */
	public void close()
	{
		if (isOpen())
			getCurrentOpening().setOpenState(false);
		this.openIndex = -1;
	}
	
	/**
	 * Opens the next opening
	 * @return Is there still pages to show (true) or did the section end (false)
	 */
	public boolean flipForward()
	{
		return move(this.openIndex + 1);
	}
	
	/**
	 * Opens the previous opening
	 * @return Is there still pages to show (true) or did the section end (false)
	 */
	public boolean flipBackwards()
	{
		return move(this.openIndex - 1);
	}
	
	/**
	 * Opens the first page of this section
	 */
	public void openAtTheBeginning()
	{
		move(0);
	}
	
	/**
	 * Opens the last page of this section
	 */
	public void openAtTheEnd()
	{
		move(this.openings.size() - 1);
	}
	
	/**
	 * @return The section is opened on the last page
	 */
	public boolean onLastPage()
	{
		return this.openIndex == this.openings.size() - 1;
	}
	
	/**
	 * @return The section is opened on the first page
	 */
	public boolean onFirstPage()
	{
		return this.openIndex == 0;
	}
	
	/**
	 * Destroys all the pages in this section
	 */
	public void kill()
	{
		for (Opening opening : this.openings)
		{
			opening.kill();
		}
		
		this.openings.clear();
		this.openIndex = -1;
	}
	
	/**
	 * Calculates a position relative to the manual's position for a page
	 * @param pageIndex The index of the page in the section
	 * @return A relative position the page should have
	 */
	public static Vector3D getRelativePositionForPage(int pageIndex)
	{
		if (pageIndex % 2 == 0)
			return Vector3D.zeroVector();
		else
			return new Vector3D(ManualMaster.MANUALDIMENSIONS.getFirst() / 2, 0);
	}
	
	/**
	 * Creates the tutorial section of the manual
	 * @param position The manual's position
	 * @param handlers The handlers that will handle the pages
	 * @return The section that was created
	 */
	public static Section createTutorialSection(Vector3D position, HandlerRelay handlers)
	{
		// Creates the pages
		List<Page> pages = createEmptyPages(4, handlers, position);
		// Page 0 = empty, 1 = table of contents (image), 2 = cable tutorial (image),
		// 3 = signal tutorial
		pages.get(1).addImage(getSpriteBank().get("contentstext"));
		pages.get(2).addImage(getSpriteBank().get("cables"));
		pages.get(3).addImage(getSpriteBank().get("signals"));
		
		return new Section(pages);
	}
	
	/**
	 * Creates the component section of the manual
	 * @param position The manual's position
	 * @param handlers The handlers that will handle the pages
	 * @return The section that was created
	 */
	public static Section createComponentSection(Vector3D position, HandlerRelay handlers)
	{
		ComponentType[] content = ComponentType.values();
		// Creates the pages (with an empty page at the beginning)
		List<Page> pages = createEmptyPages(content.length + 1, handlers, position);
		// Fills the pages with content
		for (int i = 0; i < content.length; i++)
		{
			pages.get(i + 1).addComponentContent(content[i]);
		}
		
		return new Section(pages);
	}
	
	// TODO: WET WET (again)
	
	/**
	 * Creates the machine section of the manual
	 * @param position The manual's position
	 * @param handlers The handlers that will handle the pages
	 * @return The section that was created
	 */
	public static Section createMachineSection(Vector3D position, HandlerRelay handlers)
	{
		MachineType[] content = MachineType.values();
		// Creates the pages (with an empty page at the beginning)
		List<Page> pages = createEmptyPages(content.length + 1, handlers, position);
		// Fills the pages with content
		for (int i = 0; i < content.length; i++)
		{
			pages.get(i + 1).addMachineContent(content[i]);
		}
		
		return new Section(pages);
	}
	
	// TODO: WET WET WET
	
	/**
	 * Creates the obstacle section of the manual
	 * @param position The manual's position
	 * @param handlers The handlers that will handle the pages
	 * @return The section that was created
	 */
	public static Section createObstacleSection(Vector3D position, HandlerRelay handlers)
	{
		ObstacleType[] content = ObstacleType.values();
		// Creates the pages (with an empty page at the beginning)
		List<Page> pages = createEmptyPages(content.length + 1, handlers, position);
		// Fills the pages with content
		for (int i = 0; i < content.length; i++)
		{
			pages.get(i + 1).addObstacleContent(content[i]);
		}
		
		return new Section(pages);
	}
	
	private boolean move(int newIndex)
	{
		if (isOpen())
			getCurrentOpening().setOpenState(false);
		this.openIndex = newIndex;
		if (isOpen())
			getCurrentOpening().setOpenState(true);
		return isOpen();
	}
	
	private Opening getCurrentOpening()
	{
		return this.openings.get(this.openIndex);
	}
	
	private static List<Page> createEmptyPages(int amount, HandlerRelay handlers, 
			Vector3D position)
	{
		List<Page> pages = new ArrayList<>();
		for (int i = 0; i < amount; i++)
		{
			pages.add(new Page(handlers, position.plus(getRelativePositionForPage(i))));
		}
		
		return pages;
	}
	
	private static Bank<Sprite> getSpriteBank()
	{
		return SpriteBank.getSpriteBank("manual");
	}
	
	
	// SUBCLASSES	-------------------------
	
	private static class Opening
	{
		// ATTRIBUTES	---------------------
		
		private List<Page> pages;
		
		
		// CONSTRUCTOR	---------------------
		
		public Opening(List<? extends Page> pages, int index)
		{
			this.pages = new ArrayList<>();
			
			for (int i = index; i < pages.size() && i < index + 2; i++)
			{
				this.pages.add(pages.get(i));
			}
		}
		
		
		// OTHER METHODS	-----------------
		
		public void setOpenState(boolean newState)
		{
			for (Page page : this.pages)
			{
				page.getIsOpenOperator().setState(newState);
			}
		}
		
		public void kill()
		{
			for (Page page : this.pages)
			{
				page.getIsDeadStateOperator().setState(true);
			}
			
			this.pages.clear();
		}
	}
}
