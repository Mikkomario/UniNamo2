package utopia_sound;

/**
 * Midimusictrack plays a single midi using certain sets of start- and endpoints
 *
 * @author Mikko Hilpinen.
 *         Created 23.8.2013.
 */
public class MidiMusicTrack extends AbstractSoundTrack
{
	// ATTRIBUTES	------------------------------------------------------
	
	private MidiMusic midi;
	private LoopPointInformation[] loopinformations;
	
	
	// CONSTRUCTOR	------------------------------------------------------

	/**
	 * Creates a new midimusictrack with the given information
	 *
	 * @param name The name of the track
	 * @param midi The midimusic used in the track
	 * @param loopinformations The set of looppoints
	 */
	public MidiMusicTrack(String name, MidiMusic midi, 
			LoopPointInformation[] loopinformations)
	{
		super(name);
		
		// Initializes attributes
		this.midi = midi;
		this.loopinformations = loopinformations;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	protected Sound playPhase(int index)
	{
		// Plays the midi with the settings of the given phase
		playMidi(index);
		return this.midi;
	}

	@Override
	protected int getLoopCount(int index)
	{
		return this.loopinformations[index].getLoopCount();
	}

	@Override
	protected int getMaxPhase()
	{
		return this.loopinformations.length;
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	// Plays the midi using the start- and endpoints indicated by the current 
	// index
	private void playMidi(int index)
	{
		// Starts the first midi seqment
		this.midi.startMusic(
				this.loopinformations[index].getStartPoint(), this);
		// Sets the endpoint
		this.midi.setLoopEnd(this.loopinformations[index].getEndPoint());
	}
	
	
	// SUBCLASSES	-----------------------------------------------------
	
	/**
	 * LoopPointInformation holds the information about midimusic's loop's 
	 * start- and endpoints.
	 *
	 * @author Mikko Hilpinen.
	 *         Created 23.8.2013.
	 * @see utopia_sound.MidiMusicTrack
	 */
	public class LoopPointInformation
	{
		// ATTRIBUTES	--------------------------------------------------
		
		private long start;
		private long end;
		private int loops;
		
		
		// CONSTRUCTOR	--------------------------------------------------
		
		/**
		 * Creates a new looppointinformation object holding the given information
		 *
		 * @param startpoint The loop's startpoint (tick)
		 * @param endpoint The loop's endpoint (tick)
		 * @param loopcount How many times the music is repeated between the 
		 * given points (0 means that the music is played once, a negative 
		 * number means that the music is played until released but at least once)
		 */
		public LoopPointInformation(long startpoint, long endpoint, int loopcount)
		{
			// Checks the arguments
			if (startpoint < 0 || endpoint < startpoint)
			{
				System.err.println("Invalid arguments in loopPointInformation");
				throw new IllegalArgumentException();
			}
			
			// Initializes attributes
			this.start = startpoint;
			this.end = endpoint;
			this.loops = loopcount;
		}
		
		
		// GETTERS & SETTERS	-------------------------------------------
		
		/**
		 * Changes the startpoint information
		 *
		 * @param startpoint The new held startpoint (tick)
		 */
		public void setStartPoint(long startpoint)
		{
			this.start = startpoint;
		}
		
		/**
		 * Changes the endpoint information
		 *
		 * @param endpoint The new held endpoint (tick)
		 */
		public void setEndPoint(long endpoint)
		{
			this.end = endpoint;
		}
		
		/**
		 * Changes how many times the interval is repeated
		 *
		 * @param loopcount How many times the interval is repeated 
		 * (0 means that the music is played once, -1 means that 
		 * the music is played until released but at least once, -2 or smaller 
		 * means that the music is played until released but not neccessarily 
		 * once)
		 */
		public void setLoopCount(int loopcount)
		{
			this.loops = loopcount;
		}
		
		private long getStartPoint()
		{
			return this.start;
		}
		
		private long getEndPoint()
		{
			return this.end;
		}
		
		private int getLoopCount()
		{
			return this.loops;
		}
	}
}
