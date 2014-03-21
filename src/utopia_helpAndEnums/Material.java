package utopia_helpAndEnums;

/**
 * Material tells which material the object is made from. Each material has 
 * its own density
 *
 * @author Mikko Hilpinen.
 *         Created 6.7.2013.
 * @see <a href="http://en.wikipedia.org/wiki/Density#Water">Densities wiki</a>
 * @see <a href="http://www.avlandesign.com/density_metal.htm">Densities avlandesing</a>
 */
public enum Material
{
	// See: http://en.wikipedia.org/wiki/Density#Water
	// And http://www.avlandesign.com/density_metal.htm
	@SuppressWarnings("javadoc")
	WATER, 
	@SuppressWarnings("javadoc")
	ICE, 
	@SuppressWarnings("javadoc")
	WOOD, 
	@SuppressWarnings("javadoc")
	AIR, 
	@SuppressWarnings("javadoc")
	CORK, 
	@SuppressWarnings("javadoc")
	LITHIUM, 
	@SuppressWarnings("javadoc")
	BERYLLIUMCOPPER, 
	@SuppressWarnings("javadoc")
	BRONZE, 
	@SuppressWarnings("javadoc")
	IRON, 
	@SuppressWarnings("javadoc")
	COPPER, 
	@SuppressWarnings("javadoc")
	COBALT, 
	@SuppressWarnings("javadoc")
	GOLD, 
	@SuppressWarnings("javadoc")
	PLATINUM, 
	@SuppressWarnings("javadoc")
	SILVER, 
	@SuppressWarnings("javadoc")
	STEEL, 
	@SuppressWarnings("javadoc")
	TIN, 
	@SuppressWarnings("javadoc")
	ALUMINIUM, 
	@SuppressWarnings("javadoc")
	ALUMINIUMBRONZE, 
	@SuppressWarnings("javadoc")
	DIAMOND, 
	@SuppressWarnings("javadoc")
	TITANIUM, 
	@SuppressWarnings("javadoc")
	LEAD, 
	@SuppressWarnings("javadoc")
	IRIDIUM, 
	@SuppressWarnings("javadoc")
	TUNGSTEN, 
	@SuppressWarnings("javadoc")
	ASPHALT, 
	@SuppressWarnings("javadoc")
	BALSAWOOD, 
	@SuppressWarnings("javadoc")
	BONE, 
	@SuppressWarnings("javadoc")
	BRICK, 
	@SuppressWarnings("javadoc")
	CEMENT, 
	@SuppressWarnings("javadoc")
	CLAY, 
	@SuppressWarnings("javadoc")
	EBONITE, 
	@SuppressWarnings("javadoc")
	GLASS, 
	@SuppressWarnings("javadoc")
	LEATHER, 
	@SuppressWarnings("javadoc")
	RUBBER, 
	@SuppressWarnings("javadoc")
	SAND, 
	@SuppressWarnings("javadoc")
	SANDSTONE, 
	@SuppressWarnings("javadoc")
	SNOW, 
	@SuppressWarnings("javadoc")
	STONE;
	
	/**
	 * @return The density of the material (kg/170pxl^2 or something)
	 */
	public double getDensity()
	{
		return getRealDensity() / Math.pow(170, 2);
	}
	
	/**
	 * @return The density of the material (kg/m3)
	 */
	private int getRealDensity()
	{
		switch (this)
		{
			case WATER: return 998;
			case ICE: return 917;
			case WOOD: return 700;
			case AIR: return 1;
			case CORK: return 240;
			case LITHIUM: return 535;
			case ALUMINIUM: return 2700;
			case BERYLLIUMCOPPER: return 8175;
			case BRONZE: return 8860;
			case IRON: return 7870;
			case COPPER: return 8940;
			case COBALT: return 8900;
			case GOLD: return 19320;
			case PLATINUM: return 21450;
			case SILVER: return 10500;
			case STEEL: return 7860;
			case TIN: return 7310;
			case ALUMINIUMBRONZE: return 8200;
			case DIAMOND: return 3500;
			case TITANIUM: return 4540;
			case LEAD: return 11340;
			case IRIDIUM: return 22420;
			case TUNGSTEN: return 19300;
			case ASPHALT: return 2360;
			case BALSAWOOD: return 130;
			case BONE: return 1800;
			case BRICK: return 1850;
			case CEMENT: return 2850;
			case CLAY: return 2250;
			case EBONITE: return 1150;
			case GLASS: return 2600;
			case LEATHER: return 1350;
			case RUBBER: return 1150;
			case SAND: return 1500;
			case SANDSTONE: return 2250;
			case SNOW: return 100;
			case STONE: return 2500;
			
			default: return 1000;
		}
	}
	// Also, check frictions at: http://www.taulukot.com/index.php?search_id=mekaniikka_termodynamiikka&lng=fi
}
