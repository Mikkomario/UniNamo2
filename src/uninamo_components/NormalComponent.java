package uninamo_components;

import java.awt.geom.Point2D;

import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TestListener;
import uninamo_main.GameSettings;
import uninamo_userinterface.CurrentCostDrawer;
import utopia_helpAndEnums.HelpMath;
import utopia_listeners.AdvancedMouseListener;
import utopia_worlds.Area;

/**
 * NormalComponents are components that aren't machineComponents. 
 * NormalComponents can be saved as a string from which they can later be 
 * recreated. under normal circumstances, NormalComponents can be moved around 
 * by dragging and dropping.
 * 
 * @author Mikko Hilpinen
 * @since 15.3.2014
 */
public abstract class NormalComponent extends Component implements 
		AdvancedMouseListener, TestListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private String id;
	private CurrentCostDrawer costDrawer;
	private boolean dragged, diesWhenDropped, active, testing, testVersion;
	// Notice that this is relative to the origin, not top-left corner 
	// (in other words is a relative absolute point)
	private Point2D.Double lastRelativeMouseGrabPosition;
	private ConnectorRelay relay;
	
	private static boolean componentDragged = false;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new component to the given position.
	 * 
	 * @param area The area where the object will reside at
	 * @param x The new x-coordinate of the component's origin (pixels)
	 * @param y The new y-coordinate of the component's origin (pixels)
	 * @param testHandler The testHandler that will inform the object about test 
	 * events
	 * @param connectorRelay A connectorRelay that will keep track of the 
	 * connectors
	 * @param componentRelay The NormalComponentRelay that will keep track of 
	 * the component (optional)
	 * @param costDrawer The costDrawer that will be affected by the component 
	 * (optional)
	 * @param spritename The name of the component sprite used to draw the 
	 * component
	 * @param inputs How many input connectors the component has
	 * @param outputs How many output connectors the component has
	 * @param fromBox Was the component created by pulling it from a componentBox
	 * @param isForTesting If this is true, the component will go to test mode 
	 * where it won't react to mouse but will create test cables to its connectors
	 */
	public NormalComponent(Area area, int x, int y, TestHandler testHandler, 
			ConnectorRelay connectorRelay, 
			NormalComponentRelay componentRelay, CurrentCostDrawer costDrawer, 
			String spritename, int inputs, int outputs, boolean fromBox, 
			boolean isForTesting)
	{
		super(area, x, y, testHandler,
				connectorRelay, spritename, inputs, outputs, fromBox,
				isForTesting);
		
		// Initializes attributes
		String typeName = getType().toString();
		if (typeName.length() > 4)
			typeName = typeName.substring(0, 4);
		this.id = typeName + super.getID();
		this.costDrawer = costDrawer;
		
		this.dragged = fromBox && !isForTesting;
		this.diesWhenDropped = fromBox && !isForTesting;
		this.lastRelativeMouseGrabPosition = new Point2D.Double();
		if (fromBox && !isForTesting)
			componentDragged = true;
		this.active = true;
		this.testing = false;
		this.relay = connectorRelay;
		this.testVersion = isForTesting;
		
		// Informs the costDrawer about increased component costs
		if (this.costDrawer != null)
			costDrawer.addCosts(getType().getPrice());
		
		// Adds the object to the handler(s)
		if (componentRelay != null)
			componentRelay.addComponent(this);
		if (area.getMouseHandler() != null)
			area.getMouseHandler().addMouseListener(this);
		if (testHandler != null)
			testHandler.addTestable(this);
	}
	
	
	// ABSTRACT METHODS	--------------------------------------------------
	
	/**
	 * @return What componentType this component represents
	 */
	public abstract ComponentType getType();

	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public String getID()
	{
		return this.id;
	}
	
	@Override
	public boolean isActive()
	{
		return this.active && !this.testing && !this.testVersion;
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
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D.Double mousePosition,
			double eventStepTime)
	{
		// If the component (and not one of its connectors) is clicked, it 
		// is considered a grab
		if (button == MouseButton.LEFT && 
				eventType == MouseButtonEventType.PRESSED && !componentDragged &&
				this.relay.getConnectorAtPoint(mousePosition, null) == null)
		{
			this.dragged = true;
			componentDragged = true;
			this.lastRelativeMouseGrabPosition = new Point2D.Double(getX() - 
					mousePosition.getX(), getY() - mousePosition.getY());
		}
		// If the button is released, also releases the grab
		else if (button == MouseButton.LEFT && 
				eventType == MouseButtonEventType.RELEASED && this.dragged)
		{
			this.dragged = false;
			componentDragged = false;
			
			// If the component was supposed to die upon drop, dies
			if (this.diesWhenDropped)
				kill();
		}
	}
	
	@Override
	public boolean listensPosition(Point2D.Double testedPosition)
	{
		return pointCollides(testedPosition);
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		// Doesn't listen to mouse if is just a test version
		return true;
	}
	
	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Point2D.Double mousePosition, double eventStepTime)
	{
		if (this.dragged)
			return;
		
		if (eventType == MousePositionEventType.ENTER)
			setScale(GameSettings.interfaceScaleFactor, 
					GameSettings.interfaceScaleFactor);
		else if (eventType == MousePositionEventType.EXIT)
			setScale(1, 1);
	}
	
	@Override
	public void onMouseMove(Point2D.Double newMousePosition)
	{
		// If being dragged, jumps into the mouse's position
		if (this.dragged)
		{
			setPosition(newMousePosition.getX() + 
					this.lastRelativeMouseGrabPosition.getX(), 
					newMousePosition.getY() + 
					this.lastRelativeMouseGrabPosition.getY());
			// If the position is near an edge of the screen, prepares to die, 
			// may also return from this state
			if (HelpMath.pointIsInRange(getPosition(), 64, 
					GameSettings.screenWidth - 64, 64, 
					GameSettings.screenHeight - 64))
			{
				if (this.diesWhenDropped)
				{
					this.diesWhenDropped = false;
					setScale(GameSettings.interfaceScaleFactor, 
							GameSettings.interfaceScaleFactor);
				}
			}
			else
			{
				if (!this.diesWhenDropped)
				{
					this.diesWhenDropped = true;
					setScale(0.5, 0.5);
				}
			}
		}
	}
	
	@Override
	public MouseButtonEventScale getCurrentButtonScaleOfInterest()
	{
		if (this.dragged)
			return MouseButtonEventScale.GLOBAL;
		
		return MouseButtonEventScale.LOCAL;
	}
	
	@Override
	public void onTestStart()
	{
		this.testing = true;
	}

	@Override
	public void onTestEnd()
	{
		this.testing = false;
	}
	
	@Override
	public void kill()
	{
		// Also informs the costDrawer about decreased component costs
		if (this.costDrawer != null)
			this.costDrawer.addCosts(-getType().getPrice());
		
		super.kill();
	}

	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * @return Returns the data needed for recreating the component in a 
	 * string format. The string will have the following structure:<br>
	 * ID#ComponentType#x#y
	 */
	public String getSaveData()
	{
		return getID() + "#" + getType().toString().toLowerCase() + "#" + 
				(int) getX() + "#" + (int) getY();
	}
	
	/**
	 * Changes the components unique ID.
	 * 
	 * @param ID The component's new unique ID.
	 */
	public void setID(String ID)
	{
		this.id = ID;
	}
	
	/**
	 * Stops the component from being dragged
	 */
	public void stopDrag()
	{
		this.dragged = false;
		componentDragged = false;
	}
}
