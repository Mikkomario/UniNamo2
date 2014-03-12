package uninamo_manual;

import uninamo_main.GameSettings;
import uninamo_worlds.Area;
import uninamo_worlds.AreaChanger;
import utopia_backgrounds.Background;
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
	
	private Background manualBack;
	private AreaChanger areaChanger;
	// TODO: Add page list
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new manual to the center of the screen. Mouse handling will 
	 * be stopped for the coding area while the manual is shown.
	 * 
	 * @param areaChanger The areaChanger that will handle the transitions 
	 * between areas and give access to different areas.
	 */
	public ManualMaster(AreaChanger areaChanger)
	{
		super();
		
		Area manualArea = areaChanger.getArea("manual");
		
		// Initializes attributes
		this.areaChanger = areaChanger;
		this.manualBack = new Background(GameSettings.screenWidth / 2, 
				GameSettings.screenHeight / 2, manualArea.getDrawer(), 
				manualArea.getActorHandler(), 
				MultiMediaHolder.getSpriteBank("manual"), 
				"manualback");
		
		// Deactivates the coding area
		areaChanger.getArea("coding").disableOnlyMouse();
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void kill()
	{
		// Kills the background and other stuff and returns to the coding area
		this.manualBack.kill();
		this.areaChanger.getArea("manual").end();
		this.areaChanger.getArea("coding").returnNormal();
	}
}
