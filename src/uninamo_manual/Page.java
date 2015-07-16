package uninamo_manual;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import flow_io.ListFileReader;
import gateway_ui.TextDrawer;
import gateway_ui.UIComponent;
import genesis_event.Drawable;
import genesis_event.HandlerRelay;
import genesis_util.DependentStateOperator;
import genesis_util.DepthConstants;
import genesis_util.StateOperator;
import genesis_util.StateOperatorListener;
import genesis_util.Vector3D;
import omega_util.SimpleGameObject;
import omega_util.Transformation;
import uninamo_components.Component;
import uninamo_components.ComponentType;
import uninamo_machinery.Machine;
import uninamo_machinery.MachineType;
import uninamo_main.GameSettings;
import uninamo_main.Utility;
import uninamo_obstacles.ObstacleType;
import vision_drawing.DependentSingleSpriteDrawer;
import vision_sprite.Sprite;

/**
 * A page is an object that presents certain content to the player when opened.
 * @author Mikko Hilpinen
 * @since 15.7.2015
 */
public class Page extends SimpleGameObject implements StateOperatorListener, UIComponent
{
	// ATTRIBUTES	---------------------
	
	private List<Drawable> contents;
	private StateOperator isOpenOperator;
	private Transformation transformation;
	private HandlerRelay handlers;
	
	private static final Vector3D DEFAULTMARGINS = new Vector3D(32, 32);
	
	
	// CONSTRUCTOR	---------------------
	
	/**
	 * Creates a new page. The page will be closed until opened.
	 * @param handlers The handlers that will handle the page
	 * @param position The page's new position
	 */
	public Page(HandlerRelay handlers, Vector3D position)
	{
		super(handlers);
		
		this.contents = new ArrayList<>();
		this.isOpenOperator = new StateOperator(false, true);
		this.transformation = new Transformation(position);
		this.handlers = handlers;
		
		getIsOpenOperator().getListenerHandler().add(this);
		getIsDeadStateOperator().getListenerHandler().add(this);
	}
	
	
	// IMPLEMENTED METHODS	-------------

	@Override
	public void onStateChange(StateOperator source, boolean newState)
	{
		// When the page dies, it kill all of its contents as well
		if (source.equals(getIsOpenOperator()))
		{
			for (Drawable d : this.contents)
			{
				d.getIsVisibleStateOperator().setState(newState);
			}
		}
		else if (source.equals(getIsDeadStateOperator()) && newState)
		{
			for (Drawable d : this.contents)
			{
				d.getIsDeadStateOperator().setState(true);
			}
		}
	}
	
	@Override
	public Transformation getTransformation()
	{
		return this.transformation;
	}

	@Override
	public void setTrasformation(Transformation t)
	{
		this.transformation = t;
	}

	@Override
	public int getDepth()
	{
		return DepthConstants.BACK;
	}

	@Override
	public Vector3D getDimensions()
	{
		return ManualMaster.MANUALDIMENSIONS.dividedBy(new Vector3D(2, 1));
	}

	@Override
	public StateOperator getIsVisibleStateOperator()
	{
		return getIsOpenOperator();
	}

	@Override
	public Vector3D getOrigin()
	{
		return Vector3D.zeroVector();
	}
	
	
	// OTHER METHODS	-------------------
	
	/**
	 * @return The operator that defines whether the page is opened or not
	 */
	public StateOperator getIsOpenOperator()
	{
		return this.isOpenOperator;
	}
	
	/**
	 * Adds some content to the page. The content's visibility will now be tied to the page's 
	 * state
	 * @param content
	 */
	public void addContent(Drawable content)
	{
		if (content != null)
		{
			this.contents.add(content);
			content.getIsVisibleStateOperator().setState(getIsOpenOperator().getState());
		}
	}
	
	/**
	 * Adds a new image to the page
	 * @param relativePosition The image's origin's position relative to the top left corner 
	 * of the page
	 * @param dimensions The size of the image
	 * @param image The image shown on the page
	 * @return The drawer that will handle the actual drawing of the image
	 */
	public DependentSingleSpriteDrawer<Page> addImage(Vector3D relativePosition, Vector3D dimensions, Sprite image)
	{
		DependentSingleSpriteDrawer<Page> drawer = new DependentSingleSpriteDrawer<Page>(this, 
				getDepth() - 1, image, this.handlers);
		drawer.setTrasformation(new Transformation(relativePosition));
		drawer.scaleToSize(dimensions);
		
		addContent(drawer);
		
		return drawer;
	}
	
	/**
	 * Adds a new image to the page. The image will be scaled to fill the page.
	 * @param margins The margins placed around the image
	 * @param image The image shown on the page
	 * @return The drawer that will handle the actual drawing of the image
	 */
	public DependentSingleSpriteDrawer<Page> addImage(Vector3D margins, Sprite image)
	{
		DependentSingleSpriteDrawer<Page> drawer = 
				addImage(margins, getDimensions().minus(margins.times(2)), image);
		drawer.getSpriteDrawer().setOrigin(Vector3D.zeroVector());
		return drawer;
	}
	
	/**
	 * Adds some text to the page
	 * @param text The text written on the page ('£' marks a paragraph change)
	 * @param relativePosition The relative position of the top left corner of the text
	 * @param textAreaDimensions The size of the text area
	 * @return The text drawer that will draw the text
	 */
	public TextDrawer addText(String text, Vector3D relativePosition, 
			Vector3D textAreaDimensions)
	{
		Vector3D textPosition = getTransformation().transform(relativePosition);
		TextDrawer drawer = new TextDrawer(textPosition, text, "£", GameSettings.basicFont, 
				Color.BLACK, textAreaDimensions, Vector3D.zeroVector(), getDepth() - 1, 
				this.handlers);
		addContent(drawer);
		return drawer;
	}
	
	/**
	 * Adds some text to the page. The text will fill the whole area, starting from a 
	 * specified point in the y-axis
	 * @param text text The text written on the page ('£' marks a paragraph change)
	 * @param margins The margins placed around the text
	 * @param topMargin How much empty space is left above the text
	 * @return The text drawer that will draw the text
	 */
	public TextDrawer addText(String text, Vector3D margins, double topMargin)
	{
		return addText(text, new Vector3D(margins.getFirst(), topMargin), 
				getDimensions().minus(new Vector3D(margins.getFirst() * 2, 
				margins.getSecond() + topMargin)));
	}
	
	/**
	 * Adds some text to the page. The text will fill the whole area
	 * @param text text The text written on the page ('£' marks a paragraph change)
	 * @param margins The margins placed around the text
	 * @return The text drawer that will draw the text
	 */
	public TextDrawer addText(String text, Vector3D margins)
	{
		return addText(text, margins, getDimensions().minus(margins.times(2)));
	}
	
	/**
	 * Adds page content describing the given machine type
	 * @param type The type of the machine described on this page
	 */
	public void addMachineContent(MachineType type)
	{
		// Creates the machine
		Machine machine = type.getTestMachine(getContentPosition(), this.handlers);
		// Modifies it
		double scaling = getDimensions().getFirst() / 3 / machine.getDimensions().getFirst();
		Utility.Transform(machine, Transformation.scalingTransformation(scaling));
		machine.setIsActiveStateOperator(new DependentStateOperator(getIsOpenOperator()));
		// And adds it
		addContent(machine);
		// Also adds description and name
		addObjectName(type.getName());
		addText(getMachineDescription(type), DEFAULTMARGINS, getDimensions().getSecond() / 2);
	}
	
	// TODO: WET WET
	
	/**
	 * Adds description of a component
	 * @param type The component described on this page
	 */
	public void addComponentContent(ComponentType type)
	{
		// Creates the component
		Component component = type.getNewComponent(this.handlers, getContentPosition(), 
				null, null, true);
		// Modifies it
		component.setIsActiveStateOperator(new DependentStateOperator(getIsOpenOperator()));
		// And adds it
		addContent(component);
		// Also adds description and name
		addObjectName(type.getName());
		addText("Click the cables to test", DEFAULTMARGINS.plus(new Vector3D(0, 64)), 
				getDimensions().minus(DEFAULTMARGINS.times(2)));
		addText(getComponentDescription(type), DEFAULTMARGINS, 
				getDimensions().getSecond() / 2);
	}
	
	/**
	 * Adds description of an obstacle
	 * @param type The obstacle described on this page
	 */
	public void addObstacleContent(ObstacleType type)
	{
		// Creates the visualization
		Sprite image = type.getSprite();
		double scaling = (getDimensions().getSecond() / 2 - DEFAULTMARGINS.getSecond() * 3) / 
				image.getDimensions().getSecond();
		addImage(getContentPosition(), image.getDimensions().times(scaling), image);
		// Also adds description and name
		addObjectName(type.getName());
		addText(getObstacleDescription(type), DEFAULTMARGINS, getDimensions().getSecond() / 2);
	}
	
	private void addObjectName(String name)
	{
		addText(name, new Vector3D(32, 50), getDimensions());
	}
	
	private Vector3D getContentPosition()
	{
		return getTransformation().getPosition().plus(getDimensions().dividedBy(new Vector3D(
				2, 3)));
	}
	
	private static String getDescription(String fileName, String elementName)
	{
		ListFileReader fileReader = new ListFileReader();
		try
		{
			fileReader.readFile(fileName, "*");
			for (String line : fileReader.getLines())
			{
				String[] parts = line.split("#");
				if (parts[0].equalsIgnoreCase(elementName))
				{
					if (parts.length > 1)
						return parts[1];
					else
						return null;
				}
			}
		}
		catch (FileNotFoundException e)
		{
			System.err.println("Can't find a description file");
			e.printStackTrace();
		}
		
		return null;
	}
	
	// TODO: WET WET (Make an overarching interface for these types)
	
	private static String getMachineDescription(MachineType type)
	{
		return getDescription("configure/machineinstructions.txt", type.toString());
	}
	
	private static String getComponentDescription(ComponentType type)
	{
		return getDescription("configure/manualcontent.txt", type.toString());
	}
	
	private static String getObstacleDescription(ObstacleType type)
	{
		return getDescription("configure/obstacleinstructions.txt", type.toString());
	}
}
