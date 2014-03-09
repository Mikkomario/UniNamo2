package uninamo_worlds;

import uninamo_userinterface.CodeTransitionButton;

/**
 * DesignObjectCreator creates the objects needed in the design area. It also 
 * handles the background of the said area.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class DesignObjectCreator extends AreaObjectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private AreaChanger areaChanger;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new objectCreator. The creator will create the objects when 
	 * the area starts.
	 * 
	 * @param areaChanger The areaChanger that handles different areas
	 */
	public DesignObjectCreator(AreaChanger areaChanger)
	{
		super(areaChanger.getArea("design"), "paper", "gameplaybackgrounds");
		
		// Initializes attributes
		this.areaChanger = areaChanger;
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	protected void createObjects(Area area)
	{
		// Creates objects
		
		new CodeTransitionButton(area.getDrawer(), area.getMouseHandler(), 
				area, CodeTransitionButton.TOCODE, this.areaChanger);
	}
}
