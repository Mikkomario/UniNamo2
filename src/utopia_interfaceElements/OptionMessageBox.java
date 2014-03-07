package utopia_interfaceElements;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import utopia_gameobjects.DimensionalDrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_graphic.Sprite;
import utopia_handleds.LogicalHandled;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_listeners.AdvancedMouseListener;
import utopia_listeners.OptionMessageBoxListener;
import utopia_listeners.TransformationListener;

/**
 * OptionMessageBoxes are interactive messageBoxes that show a number of 
 * buttons that represent different options the user can take. A number of 
 * users can listen to the events caused by the user-box-interaction
 * 
 * @author Mikko Hilpinen
 * created 8.1.2014
 */
public class OptionMessageBox extends MessageBox implements LogicalHandled
{
	// ATTRIBUTES	------------------------------------------------------
	
	private boolean active, autodeath, deactivatesOthers;
	private OptionMessageBoxListener user;
	private MouseListenerHandler mousehandler;
	private ActorHandler actorhandler;
	
	/**
	 * This template setting is used for boxes that only need to present 
	 * information
	 */
	public static final String[] OKOPTIONS = {"OK"};
	/**
	 * This template setting is used for boxes that require the user to make 
	 * a simple yes no decision
	 */
	public static final String[] YESNOPTIONS = {"Yes", "No"};
	/**
	 * This template setting is used for boxes that allow the user to make 
	 * a yes no decision or cancel the operation
	 */
	public static final String[] YESNOCANCELOPTIONS = {"Yes", "No", "Cancel"};
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * @param x The x-coordinate of the box's center
	 * @param y The y-coordinate of the box's center
	 * @param depth The drawing depth of the box
	 * @param message The message shown on the box
	 * @param textfont The font with which the message is drawn
	 * @param textcolor The color with which the text is drawn
	 * @param backgroundsprite The sprite used to draw the messageBox
	 * @param options A table containing all the names of the options shown 
	 * as buttons
	 * @param buttonsprite The sprite used to draw the buttons in the box. 
	 * Should contain (at least) 2 subimages so the buttons can react to 
	 * mouse hover
	 * @param diesafteruse Will the optionmessageBox die and disappear after 
	 * one of the options is chosen
	 * @param deactivateOtherComponents Should other objects in the mouse and 
	 * actorhandled be deactivated?
	 * @param user The object that is interested in which of the options 
	 * was pressed (optional)
	 * @param drawer The drawableHandler that will draw the box (optional)
	 * @param actorhandler The actorHandler that will animate the background 
	 * sprite (optional)
	 * @param mousehandler MouseListenerHandler that informs the option buttons 
	 * about mouse events
	 */
	public OptionMessageBox(int x, int y, int depth, String message,
			Font textfont, Color textcolor, Sprite backgroundsprite, 
			String[] options, Sprite buttonsprite, boolean diesafteruse, 
			boolean deactivateOtherComponents, 
			OptionMessageBoxListener user, DrawableHandler drawer,
			ActorHandler actorhandler, MouseListenerHandler mousehandler)
	{
		super(x, y, depth, message, textfont, textcolor, backgroundsprite, drawer,
				actorhandler);
		
		// Checks if the message should deactivate other objects
		if (deactivateOtherComponents)
		{
			actorhandler.inactivate();
			mousehandler.inactivate();
		}
		
		// Initializes attributes
		this.active = true;
		this.autodeath = diesafteruse;
		this.user = user;
		this.deactivatesOthers = true;
		this.mousehandler = mousehandler;
		this.actorhandler = actorhandler;
		
		int buttony = backgroundsprite.getHeight() - MARGIN - 
				buttonsprite.getHeight() + buttonsprite.getOriginY();
		int minbuttonx = /*MARGIN +*/ buttonsprite.getOriginX();
		int maxbuttonx = backgroundsprite.getWidth() - //MARGIN - 
				buttonsprite.getWidth() + buttonsprite.getOriginX();
		
		// Creates the options
		for (int i = 0; i < options.length; i++)
		{
			int buttonx = (int) (minbuttonx + ((i + 1.0) / (options.length + 1.0)) * (maxbuttonx - minbuttonx));
			
			new OptionButton(buttonx, buttony, buttonsprite, options[i], i, 
					textfont, textcolor, drawer, mousehandler, this);
		}
	}
	
	@Override
	public boolean isActive()
	{
		return this.active;
	}

	@Override
	public void activate()
	{
		this.active = true;
	}

	@Override
	public void inactivate()
	{
		this.active = false;
	}
	
	@Override
	public void kill()
	{
		// Checks if the other objects should be reactivated
		if (this.deactivatesOthers)
		{
			this.mousehandler.activate();
			this.actorhandler.activate();
		}
			
		super.kill();
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	private void onOptionClick(OptionButton option)
	{
		// Informs the listener about the event
		if (this.user != null)
			this.user.onOptionMessageEvent(option.getText(), option.getIndex());
		// Dies if autodeath is on
		if (this.autodeath)
			kill();
	}
	
	
	// SUBCLASSES	-----------------------------------------------------
	
	/**
	 * OptionButton is a simple button that listens to mouse clicks and 
	 * and informs the messagebox if it is clicked.
	 * 
	 * @author Mikko Hilpinen
	 * created 8.1.2014
	 */
	private class OptionButton extends DimensionalDrawnObject implements 
			TransformationListener, AdvancedMouseListener
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private Point2D relativeposition;
		private SingleSpriteDrawer spritedrawer;
		private OptionMessageBox box;
		private String text;
		private int index;
		private Font textfont;
		private Color textcolor;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		// Note: Relativex and relativey are relative to the top left corner 
		// of the object
		public OptionButton(int relativex, int relativey, Sprite buttonsprite, 
				String text, int index, Font textfont, Color textcolor, 
				DrawableHandler drawer, MouseListenerHandler mousehandler, 
				OptionMessageBox containerbox)
		{
			super(0, 0, containerbox.getDepth(), false, 
					CollisionType.BOX, drawer, null);
			
			// Initializes attributes
			this.box = containerbox;
			this.text = text;
			this.index = index;
			this.relativeposition = new Point(relativex, relativey);
			this.spritedrawer = new SingleSpriteDrawer(buttonsprite, null, this);
			this.textfont = textfont;
			this.textcolor = textcolor;
			
			updatePosition();
			
			// Adds the object to the handler(s)
			this.box.getTransformationListenerHandler().addListener(this);
			mousehandler.addMouseListener(this);
		}
		
		
		// IMPLEMENTED METHODS	------------------------------------------

		@Override
		public int getWidth()
		{
			if (this.spritedrawer == null)
				return 0;
			
			return this.spritedrawer.getSprite().getWidth();
		}

		@Override
		public int getHeight()
		{
			if (this.spritedrawer == null)
				return 0;
			
			return this.spritedrawer.getSprite().getHeight();
		}

		@Override
		public int getOriginX()
		{
			if (this.spritedrawer == null)
				return 0;
			
			return this.spritedrawer.getSprite().getOriginX();
		}

		@Override
		public int getOriginY()
		{
			if (this.spritedrawer == null)
				return 0;
			
			return this.spritedrawer.getSprite().getOriginY();
		}

		@Override
		public void drawSelfBasic(Graphics2D g2d)
		{
			// Draws the sprite
			if (this.spritedrawer == null)
				return;
			
			this.spritedrawer.drawSprite(g2d, 0, 0);
			
			g2d.setFont(this.textfont);
			g2d.setColor(this.textcolor);
			g2d.drawString(this.text, 0, 0);
		}

		@Override
		public boolean isActive()
		{
			return this.box.isActive();
		}

		@Override
		public void activate()
		{
			this.box.activate();
		}

		@Override
		public void inactivate()
		{
			this.box.inactivate();
		}

		@Override
		public void onTransformationEvent(TransformationEvent e)
		{
			// Resets the position
			updatePosition();
		}
		
		@Override
		public double getXScale()
		{
			return this.box.getXScale();
		}
		
		@Override
		public double getYScale()
		{
			return this.box.getYScale();
		}
		
		@Override
		public double getXShear()
		{
			return this.box.getXShear();
		}
		
		@Override
		public double getYShear()
		{
			return this.box.getYShear();
		}
		
		@Override
		public double getAngle()
		{
			return this.box.getAngle();
		}

		@Override
		public boolean listensPosition(Point2D testPosition)
		{
			return pointCollides(testPosition);
		}

		@Override
		public boolean listensMouseEnterExit()
		{
			return true;
		}

		@Override
		public void onMouseMove(Point2D newMousePosition)
		{
			// Does nothing
		}

		@Override
		public MouseButtonEventScale getCurrentButtonScaleOfInterest()
		{
			return MouseButtonEventScale.LOCAL;
		}
		
		@Override
		public boolean isVisible()
		{
			return super.isVisible() && this.box.isVisible();
		}
		
		@Override
		public boolean isDead()
		{
			return super.isDead() || this.box.isDead();
		}
		
		@Override
		public void onMouseButtonEvent(MouseButton button,
				MouseButtonEventType eventType, Point2D mousePosition,
				double eventStepTime)
		{
			// If the button was clicked, Informs the box
			if (button == MouseButton.LEFT && 
					eventType == MouseButtonEventType.PRESSED)
				this.box.onOptionClick(this);
		}

		@Override
		public void onMousePositionEvent(MousePositionEventType eventType,
				Point2D mousePosition, double eventStepTime)
		{
			// Button reacts to mouse over by changing sprite index
			if (eventType == MousePositionEventType.ENTER)
				this.spritedrawer.setImageIndex(1);
			else if (eventType == MousePositionEventType.EXIT)
				this.spritedrawer.setImageIndex(0);
		}
		
		@Override
		public Class<?>[] getSupportedListenerClasses()
		{
			// Doesn't limit supported classes
			return null;
		}
		
		
		// GETTERS & SETTERS	-----------------------------------------
		
		public int getIndex()
		{
			return this.index;
		}
		
		public String getText()
		{
			return this.text;
		}
		
		
		// OTHER METHODS	---------------------------------------------
		
		private void updatePosition()
		{
			// Keeps the object in the same relative point relative to the 
			// containing box
			Point2D newposition = this.box.transform(this.relativeposition);
			
			setPosition(newposition.getX(), newposition.getY());
		}
	}
}
