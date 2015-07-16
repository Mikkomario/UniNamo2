package uninamo_previous;

import uninamo_obstacles.ObstacleType;

/**
 * ObstacleInfoHolder holds information about different obstacles
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 * @deprecated page does this already
 */
public class ObstacleInfoHolder extends DescriptionHolder
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new holder. the data is read automatically
	 */
	public ObstacleInfoHolder()
	{
		super("configure/obstacleinstructions.txt");
	}

	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Reads information about the given obstacle type
	 * @param type The type who's information is searched for
	 * @return Information about the given obstacle type
	 */
	public String getObstacleDescription(ObstacleType type)
	{
		return getData(type.toString().toLowerCase());
	}
}
