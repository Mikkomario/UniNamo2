package uninamo_manual;

import omega_world.Area;
import omega_world.GameObject;

/**
 * An empty page is just filler. It doesn't contain anything.
 * 
 * @author Mikko Hilpinen
 * @since 12.3.2014
 */
public class EmptyPage extends GameObject implements Page
{
	/**
	 * Creates an empty page to the given area
	 * @param area The area where the page will be created to
	 */
	public EmptyPage(Area area)
	{
		super(area);
	}

	@Override
	public void open()
	{
		// Does nothing
	}

	@Override
	public void close()
	{
		// Does nothing
	}
}
