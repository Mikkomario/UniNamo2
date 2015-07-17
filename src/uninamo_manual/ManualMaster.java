package uninamo_manual;

import exodus_world.Area;
import exodus_world.AreaBank;
import gateway_ui.AbstractButton;
import genesis_event.HandlerRelay;
import genesis_util.DepthConstants;
import genesis_util.Vector3D;

import java.util.ArrayList;
import java.util.List;

import arc_bank.Bank;
import omega_util.Transformation;
import uninamo_main.GameSettings;
import uninamo_main.Utility;
import uninamo_userinterface.AreaInterface;
import uninamo_userinterface.ScalingSpriteButton;
import vision_drawing.SimpleSingleSpriteDrawerObject;
import vision_sprite.Sprite;
import vision_sprite.SpriteBank;

/**
 * ManualMaster handles the objects shown in the manual and takes care of 
 * opening and closing the manual.
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public class ManualMaster extends AreaInterface
{
	// ATTRIBUTES	------------------------------------------------------
	
	private SimpleSingleSpriteDrawerObject background;
	private List<Section> sections;
	private int currentSectionIndex;
	private AbstractButton manualButton;
	private Vector3D position;
	private List<BookMark> bookMarks;
	
	static final Vector3D MANUALDIMENSIONS = new Vector3D(600, 500);
	static final String SPRITEBANKNAME = "manual";
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new manual to the center of the screen. Mouse handling will 
	 * be stopped for the coding area while the manual is shown.
	 * 
	 * @param manualButton The manualButton in the coding area
	 */
	private ManualMaster(AbstractButton manualButton, HandlerRelay handlers, Vector3D position)
	{
		super(handlers, ButtonFunction.values());
		
		// Initializes attributes
		this.manualButton = manualButton;
		this.currentSectionIndex = 0;
		this.position = position;
		this.sections = new ArrayList<>();
		this.bookMarks = new ArrayList<>();
		
		this.background = new SimpleSingleSpriteDrawerObject(DepthConstants.BACK, 
				getSpriteBank().get("manualback"), handlers);
		this.background.addTransformation(Transformation.transitionTransformation(
				this.position));
		this.background.getDrawer().getSpriteDrawer().setOrigin(Vector3D.zeroVector());
		this.background.getDrawer().scaleToSize(MANUALDIMENSIONS);
		
		// Creates the manual content
		this.sections.add(Section.createTutorialSection(position, handlers));
		this.sections.add(Section.createComponentSection(position, handlers));
		this.sections.add(Section.createMachineSection(position, handlers));
		this.sections.add(Section.createObstacleSection(position, handlers));
		
		// Opens the first section
		this.sections.get(this.currentSectionIndex).openAtTheBeginning();
		
		// Hides the manual button while the manual is open
		this.manualButton.getIsVisibleStateOperator().setState(false);
		
		// Creates the bookmarks
		for (int i = 0; i < this.sections.size(); i++)
		{
			this.bookMarks.add(new BookMark((MANUALDIMENSIONS.getFirstInt() - 32) / 
					this.sections.size() * i, i, this, handlers));
		}
		
		// Creates other buttons
		createButtons();
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	protected AbstractButton createButtonForFunction(Function f)
	{
		if (f == ButtonFunction.CLOSE)
			return ScalingSpriteButton.createButton(this.position.plus(new Vector3D(
					MANUALDIMENSIONS.getFirst() - 32, 32)), getHandlers(), SPRITEBANKNAME, 
					"close");
		else
		{
			Vector3D buttonPosition;
			String spriteName;
			if (f == ButtonFunction.NEXT)
			{
				buttonPosition = this.position.plus(MANUALDIMENSIONS).minus(new Vector3D(
						32, 32));
				spriteName = "next";
			}
			else
			{
				buttonPosition = this.position.plus(new Vector3D(32, 
						MANUALDIMENSIONS.getSecond() - 32));
				spriteName = "previous";
			}
			
			return ScalingSpriteButton.createButton(buttonPosition, getHandlers(), 
					SPRITEBANKNAME, spriteName);
		}
	}


	@Override
	protected void executeFunction(Function f)
	{
		if (f == ButtonFunction.CLOSE)
		{
			// Kills the related components
			this.background.getIsDeadStateOperator().setState(true);
			for (Section section : this.sections)
			{
				section.kill();
			}
			this.sections.clear();
			for (BookMark mark : this.bookMarks)
			{
				mark.getIsDeadStateOperator().setState(true);
			}
			this.bookMarks.clear();
			
			// And itself
			getIsDeadStateOperator().setState(true);
			
			// Manual button becomes visible again
			this.manualButton.getIsVisibleStateOperator().setState(true);
			
			// Changes the area back to coding
			getAreaBank().get("manual").end();
			Utility.setMouseState(getAreaBank().get("coding").getHandlers(), true);
		}
		else if (f == ButtonFunction.NEXT)
		{
			// If an end of a section was reached, moves to the next one
			if (!this.sections.get(this.currentSectionIndex).flipForward())
			{
				this.currentSectionIndex ++;
				this.sections.get(this.currentSectionIndex).openAtTheBeginning();
			}
			
			if (this.sections.get(this.currentSectionIndex).onLastPage())
			{
				AbstractButton button = getButtonForFunction(f);
				button.getListensToMouseEventsOperator().setState(false);
				button.getIsVisibleStateOperator().setState(false);
			}
			
			informBookMarks();
		}
		// TODO: WET WET
		else if (f == ButtonFunction.PREVIOUS)
		{
			if (!this.sections.get(this.currentSectionIndex).flipBackwards())
			{
				this.currentSectionIndex --;
				this.sections.get(this.currentSectionIndex).openAtTheEnd();
			}
			
			if (this.sections.get(this.currentSectionIndex).onFirstPage())
			{
				AbstractButton button = getButtonForFunction(f);
				button.getListensToMouseEventsOperator().setState(false);
				button.getIsVisibleStateOperator().setState(false);
			}
			
			informBookMarks();
		}
	}
	
	
	// ACCESSORS	-------------------
	
	/**
	 * @return The position of the manual's top left corner
	 */
	public Vector3D getPosition()
	{
		return this.position;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	void openSection(int sectionIndex)
	{
		if (sectionIndex >= 0 && sectionIndex < this.sections.size())
		{
			this.sections.get(this.currentSectionIndex).close();
			this.currentSectionIndex = sectionIndex;
			this.sections.get(this.currentSectionIndex).openAtTheBeginning();
			informBookMarks();
		}
	}
	
	private void informBookMarks()
	{
		for (BookMark mark : this.bookMarks)
		{
			mark.onSectionChange(this.currentSectionIndex);
		}
	}
	
	/**
	 * Opens the manual, disabling mouse functionality from the coding area for a moment
	 * @param manualButton The manual button that lead to opening this manual
	 */
	public static void openManual(AbstractButton manualButton)
	{
		// Disables mouse from the coding area
		Utility.setMouseState(getAreaBank().get("coding").getHandlers(), false);
		
		// Starts the area
		Area manualArea = getAreaBank().get("manual");
		manualArea.start(false);
		
		// Creates the manual
		Vector3D manualPosition = 
				GameSettings.resolution.dividedBy(2).minus(MANUALDIMENSIONS.dividedBy(2));
		new ManualMaster(manualButton, manualArea.getHandlers(), manualPosition);
	}
	
	private static Bank<Sprite> getSpriteBank()
	{
		return SpriteBank.getSpriteBank(SPRITEBANKNAME);
	}
	
	private static Bank<Area> getAreaBank()
	{
		return AreaBank.getAreaBank("gameplay");
	}
	
	
	// ENUMS	-----------------------
	
	private static enum ButtonFunction implements Function
	{
		CLOSE, NEXT, PREVIOUS;
	}
}
