package utopia_handlers;

import java.awt.Graphics2D;
import java.util.Comparator;

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
	
	private int depth;
	private boolean usesDepth;
	private Graphics2D lastg2d;
	private boolean needsSorting;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new drawablehandler. Drawables must be added later manually.
	 *
	 * @param autodeath Will the handler die if it has no living drawables to handle
	 * @param usesDepth Will the handler draw the objects in a depth-specific order
	 * @param depth How 'deep' the objects in this handler are drawn
	 * @param superhandler The drawablehandler that will draw this handler (optional)
	 * @see DepthConstants
	 */
	public DrawableHandler(boolean autodeath, boolean usesDepth, int depth, 
			DrawableHandler superhandler)
	{
		super(autodeath, superhandler);
		
		// Initializes attributes
		this.depth = depth;
		this.usesDepth = usesDepth;
		this.lastg2d = null;
		this.needsSorting = false;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void drawSelf(Graphics2D g2d)
	{
		// Handleobjects draws the handleds at default
		this.lastg2d = g2d;
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
		
		// If the depth sorting is on, finds the spot for the object
		if (this.usesDepth)
		{
			// If the handler uses depth sorting, the handling list needs to 
			// be sorted after this addition
			this.needsSorting = true;
		}
		
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
}
