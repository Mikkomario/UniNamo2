package utopia_video;

/**
 * Screendrawer redraws the screen when needed and it works in its own thread.
 *
 * @author Mikko Hilpinen.
 *         Created 30.8.2013.
 */
public class ScreenDrawer implements Runnable
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private GameWindow window;
	private boolean running;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new screendrawer that will draw the given window after 
	 * added to a thread.
	 *
	 * @param window The window where the stuff is drawn
	 * @see Thread
	 */
	public ScreenDrawer(GameWindow window)
	{
		// Initializes attributes
		this.window = window;
		this.running = false;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public void run()
	{
		this.running = true;
		
		// Draws the screen until stopped
		while (isRunning())
			draw();
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Makes the screendrawer update the screen
	 */
	public void callUpdate()
	{
		//this.needsupdating = true;
		synchronized (this)
		{
			notify();
		}
	}
	
	/**
	 * Stops the drawer from no longer drawing the screen
	 */
	public void stop()
	{
		this.running = false;
	}
	
	/**
	 * @return Is the drawer currently trying to draw stuff
	 */
	public boolean isRunning()
	{
		return this.running;
	}
	
	private void draw()
	{
		// Draws the window and starts waiting for the next order
		this.window.repaint();
		try
		{
			synchronized (this)
			{
				wait();
			}
		}
		catch (InterruptedException exception)
		{
			System.out.println("The drawing thread's wait was interrupted");
			exception.printStackTrace();
		}
	}
}
