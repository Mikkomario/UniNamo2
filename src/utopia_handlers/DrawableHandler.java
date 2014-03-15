package utopia_handlers;

import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.Stack;

import utopia_handleds.Drawable;
import utopia_handleds.Handled;
import utopia_helpAndEnums.DepthConstants;

/**
 * The object from this class will draw multiple drawables, calling their 
 * drawSelf-methods and removing them when necessary
 *
 * @author Mikko Hilpinen.
 *         Created 27.11.2012.
 */
public class DrawableHandler extends Handler implements Drawable
{	
	// ATTRIBUTES	------------------------------------------------------
	
	private int depth, lastDrawableDepth;
	private boolean usesDepth;
	private Graphics2D lastg2d;
	private boolean needsSorting, usesSubDrawers, subDrawersAreReady;
	private SubDrawer[] subDrawers;
	private Stack<Drawable> drawablesWaitingDepthSorting;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new drawablehandler. Drawables must be added later manually.
	 *
	 * @param autodeath Will the handler die if it has no living drawables to handle
	 * @param usesDepth Will the handler draw the objects in a depth-specific order
	 * @param depth How 'deep' the objects in this handler are drawn
	 * @param depthSortLayers In how many sections the depthSorting is done. 
	 * For handlers that contain objects that have small or no depth changes, 
	 * a larger number like 5-6 is excellent. For handlers that contain objects 
	 * that have large depth changes a smaller number 1-3 is better. If the 
	 * handler doesn't use depth this doesn't matter.
	 * @param superhandler The drawableHandler that will draw this handler (optional)
	 * @see DepthConstants
	 */
	public DrawableHandler(boolean autodeath, boolean usesDepth, int depth, 
			int depthSortLayers, DrawableHandler superhandler)
	{
		super(autodeath, superhandler);
		
		// Initializes attributes
		this.drawablesWaitingDepthSorting = new Stack<Drawable>();
		this.depth = depth;
		this.usesDepth = usesDepth;
		this.lastg2d = null;
		this.needsSorting = false;
		this.lastDrawableDepth = DepthConstants.BOTTOM;
		this.subDrawersAreReady = false;
		
		// Initializes the subdrawers (if needed)
		if (usesDepth && depthSortLayers > 1)
		{
			this.usesSubDrawers = true;
			
			this.subDrawers = new SubDrawer[depthSortLayers];
			int depthRange = ((DepthConstants.BOTTOM + 100) - 
					(DepthConstants.TOP - 100)) / this.subDrawers.length;
			int lastMaxDepth = DepthConstants.BOTTOM + 100;
			
			for (int i = 0; i < this.subDrawers.length; i++)
			{
				this.subDrawers[i] = new SubDrawer(this, 
						lastMaxDepth - depthRange, lastMaxDepth);
				lastMaxDepth -= depthRange;
			}
			
			this.subDrawersAreReady = true;
			/*
			System.out.println("SubDrawers created with " + 
					this.drawablesWaitingDepthSorting.size() + " drawables waiting");
			*/
			
			// Adds all 1000 drawables that wanted to be added before the 
			// subDrawers could be initialized
			while (this.drawablesWaitingDepthSorting.size() > 0)
				addDrawable(this.drawablesWaitingDepthSorting.pop());
		}
		else
		{
			this.subDrawers = null;
			this.usesSubDrawers = false;
		}
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void drawSelf(Graphics2D g2d)
	{
		// Handleobjects draws the handleds at default
		this.lastg2d = g2d;
		this.lastDrawableDepth = DepthConstants.BOTTOM + 1000;
		
		handleObjects();
	}

	@Override
	public boolean isVisible()
	{
		// Checks the visibility of all handleds (until finds an visible one)
		VisibilityCheckOperator checkoperator = new VisibilityCheckOperator();
		handleObjects(checkoperator);
		
		return checkoperator.isVisible();
	}

	@Override
	public void setVisible()
	{
		// Ends disabilities
		endDisable();
		
		// tries to set all the drawables visible
		handleObjects(new SetVisibleOperator());
	}

	@Override
	public void setInvisible()
	{
		// Ends disables since invisibility is stronger
		endDisable();
				
		// tries to set all the drawables invisible
		handleObjects(new SetInVisibleOperator());
	}
	
	@Override
	public int getDepth()
	{
		return this.depth;
	}
	
	@Override
	public boolean setDepth(int depth)
	{
		this.depth = depth;
		return true;
	}
	
	@Override
	protected void addHandled(Handled h)
	{
		// Can only add drawables
		if (!(h instanceof Drawable))
			return;
		
		Drawable d = (Drawable) h;
		
		// Checks if depth causes additional issues
		if (this.usesDepth && !(d instanceof SubDrawer))
		{
			// If there are subDrawers, checks to which this drawable should 
			// be added to
			if (this.usesSubDrawers)
			{
				// If the subDrawers aren't ready yet, simply adds the drawable 
				// to a stack of waiting objects
				if (!this.subDrawersAreReady)
				{
					this.drawablesWaitingDepthSorting.push(d);
					return;
				}
				
				int drawableDepth = d.getDepth();
				boolean spotFound = false;
					
				for (int i = 0; i < this.subDrawers.length; i++)
				{
					//System.out.println(this.subDrawers[i]);
					
					if (this.subDrawers[i].depthIsWithinRange(drawableDepth))
					{
						spotFound = true;
						this.subDrawers[i].addDrawable(d);
						break;
					}
				}
				
				// For error checking, checks that a spot was actually found
				if (!spotFound)
				{
					System.err.println("DrawableHandler couldn't find a spot "
							+ "for an object with depth " + drawableDepth + 
							", please use depth within depthConstants' range");
					this.needsSorting = true;
					super.addHandled(d);
				}
			}
			// If the handler uses depth sorting but not subDrawers, the 
			// handling list needs to be sorted after this addition
			else
			{
				this.needsSorting = true;
				super.addHandled(d);
			}
		}
		// Otherwise simply adds the handled and is done with it
		else
			super.addHandled(d);
	}
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return Drawable.class;
	}
	
	@Override
	protected boolean handleObject(Handled h)
	{
		Drawable d = (Drawable) h;
		
		// Draws the visible object
		if (d.isVisible())
			d.drawSelf(this.lastg2d);
		
		// Also checks if the depths are still ok
		if (d.getDepth() > this.lastDrawableDepth)
			this.needsSorting = true;
		
		return true;
	}
	
	@Override
	protected void updateStatus()
	{
		// In addition to normal update, sorts the handling list if needed
		super.updateStatus();
		
		if (this.needsSorting)
		{
			sortHandleds(new DepthSorter());
			this.needsSorting = false;
		}
	}
	
	
	// OTHER METHODS	---------------------------------------------------
	
	/**
	 *Adds the given drawable to the handled drawables
	 *
	 * @param d The drawable to be added
	 */
	public void addDrawable(Drawable d)
	{
		addHandled(d);
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	private class DepthSorter implements Comparator<Handled>
	{
		@Override
		public int compare(Handled h1, Handled h2)
		{
			// Actually only works with drawables but is used in "handled" list
			if (h1 instanceof Drawable && h2 instanceof Drawable)
			{
				// Drawables with more depth are put to the front of the list
				return ((Drawable) h2).getDepth() - ((Drawable) h1).getDepth();
			}
			
			return 0;
		}	
	}
	
	private class SetVisibleOperator extends HandlingOperator
	{
		@Override
		protected boolean handleObject(Handled h)
		{
			((Drawable) h).setVisible();
			return true;
		}	
	}
	
	private class SetInVisibleOperator extends HandlingOperator
	{
		@Override
		protected boolean handleObject(Handled h)
		{
			((Drawable) h).setInvisible();
			return true;
		}	
	}
	
	private class VisibilityCheckOperator extends HandlingOperator
	{
		// ATTRIBUTES	------------------------------------------------
		
		private boolean foundvisible;
		
		
		// CONSTRUCTOR	------------------------------------------------
		
		public VisibilityCheckOperator()
		{
			this.foundvisible = false;
		}
		
		
		// IMPLEMENTED METHODS	----------------------------------------
		
		@Override
		protected boolean handleObject(Handled h)
		{
			if (((Drawable) h).isVisible())
			{
				this.foundvisible = true;
				return false;
			}
			else
				return true;
		}
		
		
		// GETTERS & SETTERS	----------------------------------------
		
		public boolean isVisible()
		{
			return this.foundvisible;
		}
	}
	
	// Subdrawers handle drawables from certain depth ranges. The handleds 
	// are re-added to the superhandler if their depth changes too much
	private class SubDrawer extends DrawableHandler
	{
		// ATTRIBUTES	------------------------------------------------
		
		private int minDepth, maxDepth;
		private DrawableHandler superHandler;
		
		
		// CONSTRUCTOR	------------------------------------------------
		
		public SubDrawer(DrawableHandler superhandler, int minDepth, int maxDepth)
		{
			super(false, true, minDepth, 1, superhandler);
			
			// Initializes attributes
			this.minDepth = minDepth;
			this.maxDepth = maxDepth;
			this.superHandler = superhandler;
		}
		
		
		// IMPLEMENTED METHODS	----------------------------------------
		
		@Override
		protected boolean handleObject(Handled h)
		{
			// Also checks if the object is out of the depth range
			
			Drawable d = (Drawable) h;
			
			if (d.getDepth() < this.minDepth || d.getDepth() > this.maxDepth)
			{
				// Removes the drawable from this depth range and requests a 
				// repositioning
				removeHandled(h);
				this.superHandler.addDrawable(d);
			}	
			
			return super.handleObject(h);
		}
		
		// OTHER METHODS	------------------------------------------------
		
		public boolean depthIsWithinRange(int depth)
		{
			return (depth >= this.minDepth && depth <= this.maxDepth);
		}
	}
}
