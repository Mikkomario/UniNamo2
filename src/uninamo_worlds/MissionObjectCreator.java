package uninamo_worlds;

import uninamo_components.ConnectorRelay;
import uninamo_gameplaysupport.TestHandler;
import uninamo_missionHandling.MissionInitializer;

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
	//private VictoryHandler victoryHandler;
	
	
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
	 */
	public MissionObjectCreator(AreaChanger areaChanger, 
			TestHandler testHandler, ConnectorRelay connectorRelay/*, 
			VictoryHandler victoryHandler*/)
	{
		super(areaChanger.getArea("mission"), null, null);
		
		// Initializes attributes
		this.areaChanger = areaChanger;
		this.testHandler = testHandler;
		this.connectorRelay = connectorRelay;
		//this.victoryHandler = victoryHandler;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected void createObjects(Area area)
	{
		// Creates the MissionInitializer
		new MissionInitializer("missions/teststage.txt", this.areaChanger, 
				this.testHandler, this.connectorRelay/*, this.victoryHandler*/);
	}
}
