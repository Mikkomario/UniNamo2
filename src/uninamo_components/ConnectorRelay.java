package uninamo_components;

import java.awt.geom.Point2D;

import utopia_handleds.Handled;
import utopia_handlers.Handler;

/**
 * ConnectorRelay keeps track of all the connectors used in the game and 
 * can tell if there's a connector at a given position.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class ConnectorRelay extends Handler
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private CableConnector lastFoundConnector;
	private Class<?> lastConnectorFilter;
	private Point2D.Double lastCheckPosition;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new empty connectorRelay
	 */
	public ConnectorRelay()
	{
		super(false, null);
		
		// Initializes attributes
		this.lastConnectorFilter = CableConnector.class;
		this.lastFoundConnector = null;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	protected Class<?> getSupportedClass()
	{
		return CableConnector.class;
	}

	@Override
	protected boolean handleObject(Handled h)
	{
		// Checks if the connector collides with the checkposition and if so, 
		// remembers it and quits
		if (!this.lastConnectorFilter.isInstance(h))
			return true;
		
		CableConnector c = (CableConnector) h;
		
		if (c.pointCollides(this.lastCheckPosition))
		{
			this.lastFoundConnector = c;
			return false;
		}
		
		return true;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Adds a new connector to the relay
	 * @param c The new CableConnector to be added
	 */
	public void addConnector(CableConnector c)
	{
		addHandled(c);
	}
	
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
	public CableConnector getConnectorAtPoint(Point2D.Double testPoint, 
			Class<?> connectorClassLimit)
	{
		// Sets up the attributes
		this.lastFoundConnector = null;
		this.lastCheckPosition = testPoint;
		
		if (connectorClassLimit == null)
			this.lastConnectorFilter = getSupportedClass();
		else
			this.lastConnectorFilter = connectorClassLimit;
		
		// Tries to find the connector
		handleObjects();
		
		return this.lastFoundConnector;
	}
}