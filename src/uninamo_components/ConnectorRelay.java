package uninamo_components;

import uninamo_main.UninamoHandlerType;
import genesis_event.Handler;
import genesis_event.HandlerType;
import genesis_util.Vector3D;

/**
 * ConnectorRelay keeps track of all the connectors used in the game and 
 * can tell if there's a connector at a given position.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class ConnectorRelay extends Handler<CableConnector>
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private CableConnector lastFoundConnector;
	private Class<?> lastConnectorFilter; // TODO: Replace with enum or something 
	// (plus, make the connectors a single class or something)
	private Vector3D lastCheckPosition;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new empty connectorRelay
	 */
	public ConnectorRelay()
	{
		super(false);
		
		// Initializes attributes
		this.lastConnectorFilter = CableConnector.class;
		this.lastFoundConnector = null;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public HandlerType getHandlerType()
	{
		return UninamoHandlerType.CONNECTOR;
	}

	@Override
	protected boolean handleObject(CableConnector h)
	{
		// Checks if the connector collides with the checkposition and if so, 
		// remembers it and quits
		if (!this.lastConnectorFilter.isInstance(h))
			return true;
		
		if (h.isInAreaOfInterest(this.lastCheckPosition))
		{
			this.lastFoundConnector = h;
			return false;
		}
		
		return true;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Goes through the connectors in the relay and tries to find one that 
	 * resides in the given position.
	 * 
	 * @param testPoint The point where connectors are looked for
	 * @param connectorClassLimit Should the results be limited to a certain 
	 * class. The class should naturally be a subclass of CableConnector class. 
	 * Use null if you don't want to limit the search
	 * @return A connector at the given position or null if no suitable 
	 * connector was found.
	 */
	public CableConnector getConnectorAtPoint(Vector3D testPoint, 
			Class<?> connectorClassLimit)
	{
		// Sets up the attributes
		this.lastFoundConnector = null;
		this.lastCheckPosition = testPoint;
		
		if (connectorClassLimit == null)
			this.lastConnectorFilter = getHandlerType().getSupportedHandledClass();
		else
			this.lastConnectorFilter = connectorClassLimit;
		
		// Tries to find the connector
		handleObjects();
		
		return this.lastFoundConnector;
	}
	
	/**
	 * Searches trough the relay for the given ID.
	 * 
	 * @param ID The ID that is searched for
	 * @return A connector with the given ID or null if no such connector 
	 * exists in this relay
	 */
	public CableConnector getConnectorWithID(String ID)
	{
		// Creates a handling operator and starts the search
		IDFindOperator operator = new IDFindOperator(ID);
		handleObjects(operator);
		
		// Returns the connector that was found (or null)
		return operator.getConnector();
	}
	
	
	// SUBCLASSES	-----------------------------------------------------
	
	private class IDFindOperator extends HandlingOperator
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private String idToBeFound;
		private CableConnector foundConnector;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		public IDFindOperator(String idToBeFound)
		{
			// Initializes attributes
			this.idToBeFound = idToBeFound;
			this.foundConnector = null;
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------
		
		@Override
		protected boolean handleObject(CableConnector h)
		{
			if (h.getID().equals(this.idToBeFound))
			{
				this.foundConnector = h;
				return false;
			}
			
			return true;
		}
		
		
		// OTHER METHODS	---------------------------------------------
		
		public CableConnector getConnector()
		{
			return this.foundConnector;
		}
	}
}
