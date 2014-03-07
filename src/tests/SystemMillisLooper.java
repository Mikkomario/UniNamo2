package tests;

/**
 * This class tries to provide the most simple looping application there is. 
 * It simply loops forever and prints data at certain intervals.
 *
 * @author Gandalf.
 *         Created 16.10.2013.
 */
public class SystemMillisLooper
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private long starttime, lastmillis, stepduration;
	private boolean stopped;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates and starts the new looper. The status will be updated every 
	 * stepduration milliseconds
	 *
	 * @param stepduration How often the status will be updated (in milliseconds)
	 */
	public SystemMillisLooper(long stepduration)
	{
		this.stepduration = stepduration;
		this.starttime = System.currentTimeMillis();
		this.lastmillis = this.starttime;
		this.stopped = false;
		
		run();
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Stops the test from running anymore
	 */
	public void stop()
	{
		this.stopped = true;
	}
	
	private void run()
	{
		while(!this.stopped)
			update();	
	}
	
	private void update()
	{
		if (Math.abs(System.currentTimeMillis() - this.lastmillis) > 
				this.stepduration)
		{
			this.lastmillis = System.currentTimeMillis();
			System.out.println((this.lastmillis - this.starttime) / 1000);
		}
	}
}
