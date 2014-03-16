package uninamo_worlds;

import uninamo_components.ConnectorRelay;
import uninamo_components.NormalComponentRelay;
import uninamo_gameplaysupport.TestHandler;
import uninamo_gameplaysupport.TurnHandler;

/**
 * MissionObjectCreator creates the necessary objects at the start of the 
 * mission brief
 * 
 * @author Mikko Hilpinen
 * @since 13.3.2014
 */
public class MissionObjectCreator extends AreaObjectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private AreaChanger areaChanger;
	private TestHandler testHandler;
	private ConnectorRelay connectorRelay;
	private TurnHandler turnHandler;
	private NormalComponentRelay componentRelay;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new object creator that will start listening to its specifi 
	 * area and create the objects when need be.
	 * 
	 * @param areaChanger The areaChanger that handles the different areas 
	 * @param testHandler The testHandler that will inform the created objects 
	 * about test events
	 * @param connectorRelay The connectorRelay that will handle the created 
	 * connectors
	 * @param componentRelay The componentRelay that will keep track of the 
	 * created components
	 * @param turnHandler The turnHandler that will inform the objects about 
	 * turn events
	 */
	public MissionObjectCreator(AreaChanger areaChanger, 
			TestHandler testHandler, ConnectorRelay connectorRelay, 
			NormalComponentRelay componentRelay, TurnHandler turnHandler)
	{
		super(areaChanger.getArea("mission"), null, null);
		
		// Initializes attributes
		this.areaChanger = areaChanger;
		this.testHandler = testHandler;
		this.connectorRelay = connectorRelay;
		this.turnHandler = turnHandler;
		this.componentRelay = componentRelay;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected void createObjects(Area area)
	{
		// Creates the MissionInitializer
		new MissionInitializer("missions/teststage.txt", this.areaChanger, 
				this.testHandler, this.connectorRelay, this.componentRelay, 
				this.turnHandler);
	}
}
