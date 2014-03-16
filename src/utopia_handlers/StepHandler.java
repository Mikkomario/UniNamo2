package utopia_handlers;

import utopia_handleds.Actor;
import utopia_video.GameWindow;


/**
 * This class calculates millisconds and calls all actors when a certain number 
 * of milliseconds has passed. All of the actors should be under the command of 
 * this object. This object doesn't stop functioning by itself if it runs out 
 * of actors.<p>
 *
 * @author Mikko Hilpinen.
 *         Created 29.11.2012.
 */
public class StepHandler extends ActorHandler implements Runnable
{
	// ATTRIBUTES	-------------------------------------------------------
	
	/**
	 * How long does a single step take in milliseconds.
	 */
	public static final int STEPLENGTH = 15;
	
	private int callinterval, maxstepspercall;
	private long nextupdatemillis, lastactmillis;
	private boolean running;
	private GameWindow window;
	
	
	// CONSTRUCTOR	-------------------------------------------------------
	
	/**
	 * This creates a new stephandler. Actors are informed 
	 * when a certain number of milliseconds has passed. Actors can be 
	 * added using addActor method.
	 * 
	 * @param callInterval How many milliseconds will there at least be between 
	 * update calls? This also defines the maximum frame rate / action rate 
	 * for the program. No more than 20 milliseconds is adviced. All computers 
	 * may be unable to update the program in less than 10 milliseconds though. 
	 * (>0)
	 * @param maxStepsPerCall How many steps can be "skipped" or simulated 
	 * during a single call. Normally there is only one step or less for each 
	 * call, but if the program can't run fast enough more steps are simulated 
	 * for each call. The larger the value, the more unstable the program can 
	 * become under heavy CPU-usage, but the better the game keeps from slowing 
	 * down. The adviced value is from 2 to 3 but it can be 
	 * different depending on the nature of the software. (> 0)
	 * @param window The which which created the stepHandler
	 * @param optimizesteplength Should Aps optimization be activated. The 
	 * optimization tries to increase / decrease the Aps to the optimal value. 
	 * Usually this is unnecessary but may counter the computer's tries to 
	 * limit the Aps
	 * @see #addActor(utopia_handleds.Actor)
	 */
	public StepHandler(int callInterval, int maxStepsPerCall, 
			GameWindow window, boolean optimizesteplength)
	{
		super(false, null); // Stephandler doesn't have a superhandler
		
		// Initializes attributes
		this.callinterval = callInterval;
		this.maxstepspercall = maxStepsPerCall;
		this.nextupdatemillis = 0;
		this.lastactmillis = System.currentTimeMillis();
		this.running = false;
		this.window = window;
		
		// Creates an ApsOptimizer and adds it to the actors
		if (optimizesteplength)
			addActor(new ApsOptimizer(this.callinterval, 8, 4000, 20000, 6));
	}
	
	
	// IMPLEMENTED METHODS	-----------------------------------------------

	@Override
	public void run()
	{
		this.running = true;
		
		// Starts counting steps and does it until the object is killed
		while (this.running)
			update();
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Stops the stephandler from functioning anymore
	 */
	public void stop()
	{
		this.running = false;
	}
	
	// This method updates the actors and the window when needed
	private void update()
	{
		// Remembers the time
		this.nextupdatemillis = System.currentTimeMillis() + this.callinterval;
		
		// Calls all actors
		if (!isDead())
		{
			// Calculates the step length that is informed for the objects
			double steps = (System.currentTimeMillis() - this.lastactmillis) / 
					(double) STEPLENGTH;
			
			// Sometimes the true amount of steps can't be informed and a 
			// different number is given instead (physics don't like there 
			// being too many steps at once)
			if (steps > this.maxstepspercall)
				steps = this.maxstepspercall;
			
			act(steps);
			
			// Updates the game according to the changes
			this.window.callScreenUpdate();
			this.window.callMousePositionUpdate();
			
			// Updates the stepmillis
			this.lastactmillis = System.currentTimeMillis();
		}
		// Stops running if dies
		else
			this.running = false;
		
		// If there is time, the thread will wait until another step is needed
		if (System.currentTimeMillis() < this.nextupdatemillis)
		{
			synchronized (this)
			{
				try
				{
					// TODO: Apparently this can become negative under very 
					// rare circumstances (added the second check, hope it helps)
					if (System.currentTimeMillis() < this.nextupdatemillis)
						wait(this.nextupdatemillis - System.currentTimeMillis());
				}
				catch (InterruptedException exception)
				{
					System.err.println("StepHandler's stepdelay was " +
							"interupted unexpectedly");
					exception.printStackTrace();
				}
			}
		}
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	// ApsOptimizer optimizes the aps to get virtually as close to the 
	// optimal aps as possible
	private class ApsOptimizer implements Actor
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private int optimalsteptime;
		private int optimalaps;
		private int maxstepsizeadjustment;
		private int currentstepsizeadjustments;
		private int checkphase;
		private int checkdelay;
		private int breaklength;
		private long lastcheck;
		private boolean onbreak;
		private boolean dead;
		private int aps;
		private int actions;
		private long lastmillis;
		private int lastapsdifference;
		private int stepsizeadjuster;
		private int maxoptimizations;
		private int optimizationsdone;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		/**
		 * Creates a new apsOptimizer with the given information. The optimizer 
		 * will start to optimize the aps after a small delay
		 *
		 * @param startsteptime How long is the steptime in the beginning 
		 * of the program (how long is the default step time, also defines the 
		 * optimal actions per second value)
		 * @param maxstepsizeadjustment How much the stepsize can be adjusted at 
		 * once (the larger value makes optimization more precise but also 
		 * makes it last longer)
		 * @param checkdelay How many milliseconds the optimizer calculates the 
		 * values before they are adjusted again (>= 1000)
		 * @param breaklength How many milliseconds the optimizer sleeps after 
		 * it has done its optimizing (> checkdelay)
		 * @param maximumoptimizations how many optimizations will be done 
		 * before the optimizer dies
		 */
		public ApsOptimizer(int startsteptime, int maxstepsizeadjustment, 
				int checkdelay, int breaklength, int maximumoptimizations)
		{
			// Initializes attributes
			this.optimalsteptime = startsteptime;
			this.optimalaps = 1000 / this.optimalsteptime;
			this.aps = this.optimalaps;
			this.maxstepsizeadjustment = maxstepsizeadjustment;
			this.checkdelay = checkdelay;
			this.breaklength = breaklength;
			this.checkphase = 0;
			this.lastcheck = System.currentTimeMillis();
			this.onbreak = false;
			this.dead = false;
			this.actions = 0;
			this.lastmillis = System.currentTimeMillis();
			this.lastapsdifference = 0;
			this.stepsizeadjuster = 0;
			this.currentstepsizeadjustments = 0;
			this.maxoptimizations = maximumoptimizations;
			this.optimizationsdone = 0;
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------
		
		@Override
		public boolean isActive()
		{
			// The optimizer is always active
			return true;
		}

		@Override
		public void activate()
		{
			// The optimizer is always active
		}

		@Override
		public void inactivate()
		{
			// The optimizer is always active
		}

		@Override
		public boolean isDead()
		{
			// The optimizer can only die while on break
			return this.dead && this.onbreak;
		}

		@Override
		public void kill()
		{
			this.dead = true;
		}

		@Override
		public void act(double steps)
		{
			// Calculates the aps
			this.actions ++;
			
			if (System.currentTimeMillis() - this.lastmillis >= 1000)
			{
				this.aps = this.actions;
				this.actions = 0;
				this.lastmillis = System.currentTimeMillis();
			}
			
			// Optimizes (if needed)
			int delay = this.checkdelay;
			if (this.onbreak)
				delay = this.breaklength;
			// Checks if a new check is needed
			if (System.currentTimeMillis() - this.lastcheck >= delay)
				optimize();
		}
		
		private void optimize()
		{
			// Stops the break
			this.onbreak = false;
			
			// Calculates the current aps difference
			int currentapsdifference = Math.abs(this.aps - this.optimalaps);
			
			// Phase 0: Checks if the aps is optimal
			if (this.checkphase == 0)
			{
				// Of the aps is optimal goes to break
				if (currentapsdifference == 0)
					goToBreak();
				// Otherwise advances to the next phase (and updates the steptime)
				else
				{
					this.optimalsteptime = StepHandler.this.callinterval;
					if (this.aps < this.optimalaps)
						this.stepsizeadjuster = -1;
					else
						this.stepsizeadjuster = 1;
					this.checkphase ++;
					this.currentstepsizeadjustments = 0;
				}
			}
			// Phase 1: Slowly adjusts the steptime in hopes of adjusting 
			// the aps
			if (this.checkphase == 1)
			{
				// If the current apsdifference is larger than the last aps or 
				// If there have already been too many adjustments
				// difference, goes to the next phase
				if (currentapsdifference > this.lastapsdifference || 
						this.currentstepsizeadjustments >= 
						this.maxstepsizeadjustment)
				{
					goToBreak();
					StepHandler.this.callinterval = this.optimalsteptime;
				}
				// Otherwise further adjusts the stepduration
				else
				{
					// Checks if the current status is closer to the goal than 
					// the last and remembers it
					if (currentapsdifference < this.lastapsdifference)
						this.optimalsteptime = StepHandler.this.callinterval;
					
					// Increases / decreases the stepduration
					StepHandler.this.callinterval += this.stepsizeadjuster;
					this.currentstepsizeadjustments ++;
				}
			}
			
			// Remembers the last aps difference
			this.lastapsdifference = Math.abs(this.aps - this.optimalaps);
			// Updates the timer
			this.lastcheck = System.currentTimeMillis();
		}
		
		private void goToBreak()
		{
			// Updates the stephandler's action modifier
			//StepHandler.this.actionmodifier = this.optimalaps / 
			//		(double) this.aps;
			
			this.onbreak = true;
			this.checkphase = 0;
			this.optimizationsdone ++;
			
			// If the optimizer has done enough optimizations, it kills itself
			if (this.optimizationsdone >= this.maxoptimizations)
				kill();
		}
	}
}
