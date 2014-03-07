package utopia_sound;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import utopia_listeners.SoundListener;


/**
 * Midimusics are musical objects which can be played. Only one midimusic should 
 * be played at a time.
 * 
 * @author Unto Solala & Mikko Hilpinen. Created 10.7.2013
 */
public class MidiMusic extends Sound implements MetaEventListener
{
	// ATTRIBUTES ---------------------------------------------------------

	private String fileName;
	private Sequence midiSequence;
	private Sequencer midiSequencer;
	private long pauseposition;

	
	// CONSTRUCTOR ---------------------------------------------------------

	/**
	 * Creates MidiMusic-object.
	 * 
	 * @param fileName Which midi file is used to play the music (data/ 
	 * automatically included).
	 * @param name The name of the midimusic (in the midimusicbank)
	 */
	public MidiMusic(String fileName, String name)
	{
		super(name);
		
		// Initializes attributes
		this.fileName = "data/" + fileName;
		this.pauseposition = 0;
		
		// tries to create the midisequence
		try
		{
			this.midiSequence = MidiSystem.getSequence(new File(this.fileName));
		}
		catch (InvalidMidiDataException e)
		{
			System.err.println("Couldn't find create a midisequence!");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.err.println("IOException whilst creating midisequence!");
			e.printStackTrace();
		}
		// Now let's try and set-up our midiSequencer
		try
		{
			this.midiSequencer = MidiSystem.getSequencer();
		}
		catch (MidiUnavailableException e)
		{
			System.err.println("Problems whilst setting up sequencer!");
			e.printStackTrace();
		} 
	}
	
	
	// IMPLEMENTED METHODS	-------------------------------------------
	
	@Override
	protected void playSound()
	{
		// Plays the music once from the very beginning
		setLoopCount(0);
		setLoopStart(0);
		setLoopEnd(-1);
		startMusic(0);
	}

	@Override
	protected void loopSound()
	{
		// Loops the music continuously
		setLoopCount(-1);
		setLoopStart(0);
		setLoopEnd(-1);
		startMusic(0);
	}

	@Override
	protected void stopSound()
	{
		// Stops the music from playing and informs the listeners
		if (this.midiSequencer.isRunning())
		{
			// Doesn't listen to the sequencer anymore
			this.midiSequencer.removeMetaEventListener(this);
			this.midiSequencer.stop();
			this.midiSequencer.close();
		}
	}

	@Override
	public void pause()
	{
		if (this.midiSequencer.isRunning())
		{
			this.midiSequencer.stop();
			this.pauseposition = this.midiSequencer.getTickPosition();
		}
	}

	@Override
	public void unpause()
	{
		if (!this.midiSequencer.isRunning()&&isPlaying())
		{
			// Starts the music from the spot it was at
			startMusic(this.pauseposition);
		}
	}
	
	@Override
	public void meta(MetaMessage event)
	{
		// Checks if a midi ended and informs the listeners
		if (event.getType() == 47)
		{
			// Doesn't listen to the sequencer anymore
			this.midiSequencer.removeMetaEventListener(this);
			// Informs that the music stopped
			informSoundEnd();
		}
	}
	

	// METHODS ---------------------------------------------------
	
	/**
	 * Starts playing the music from the given position.
	 * 
	 * @param startPosition	 Playback's starting tick-position.
	 * @param specificlistener A listener that will be informed about the 
	 * events caused by this specific play (null if not needed)
	 */
	public void startMusic(long startPosition, SoundListener specificlistener)
	{
		// Stops old music if still playing
		if (isPlaying())
			stop();
		
		// Informs listeners and starts the music
		informSoundStart(specificlistener);
		startMusic(startPosition);
	}

	/**
	 * @return Returns the length of a Midi-sequence in ticks.
	 */
	public long getSequenceLength()
	{
		return this.midiSequence.getTickLength();
	}

	private void startMusic(long startPosition)
	{
		// Adds the music as a listener to the sequencer
		this.midiSequencer.addMetaEventListener(this);
		
		//Now let's try to set our sequence
		try
		{		
			this.midiSequencer.setSequence(this.midiSequence);
		}
		catch (InvalidMidiDataException e)
		{
			System.err.println("Midi was invalid!");
			e.printStackTrace();
		}
		try
		{
			this.midiSequencer.open();
		}
		catch (MidiUnavailableException mue)
		{
			System.err.println("Midi" + getName() +  "was unavailable!");
			mue.printStackTrace();
		}
		this.midiSequencer.setTickPosition(startPosition);
		this.midiSequencer.start();
	}

	/**
	 * Sets how many times the music loops.
	 * 
	 * @param loopCount	How many times the music loops. If loopCount is 
	 * negative, the music will loop continuously.
	 */
	public void setLoopCount(int loopCount)
	{
		if (loopCount < 0)
		{
			this.midiSequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
		}
		else
		{
			this.midiSequencer.setLoopCount(loopCount);
		}
	}

	/**
	 * Changes where the music's loop starts.
	 * 
	 * @param loopStartPoint The tick where music's loop starts.
	 */
	public void setLoopStart(long loopStartPoint)
	{
		this.midiSequencer.setLoopStartPoint(loopStartPoint);
	}

	/**
	 * Changes where the music's loop ends.
	 * 
	 * @param loopEndPoint The tick where music's loop ends. (0 means no end point, 
	 * -1 means the end of the midi)
	 */
	public void setLoopEnd(long loopEndPoint)
	{
		this.midiSequencer.setLoopEndPoint(loopEndPoint);
	}
	
	/**
	 * Resets loop's start-point to 0 and end-point to the end of the sequence.
	 */
	public void setDefaultLoopPoints()
	{
		this.midiSequencer.setLoopStartPoint(0);
		this.midiSequencer.setLoopEndPoint(this.getSequenceLength());
	}
	
	/**
	 * Sets a new tempo for the midi. 1.0 is the default TempoFactor.
	 * 
	 * @param newTempoFactor New tempoFactor for the midi (0+) (1.0 by default)
	 */
	public void setTempoFactor (float newTempoFactor)
	{
		this.midiSequencer.setTempoFactor(newTempoFactor);
	}
	
	/**
	 * Sets the TempoFactor to 1.0, which is the default.
	 */
	public void resetTempoFactor()
	{
		this.midiSequencer.setTempoFactor(1);
	}
	
	/**
	 * Returns the current TempoFactor.
	 * 
	 * @return	Returns the current TempoFactor as a float.
	 */
	public float getTempoFactor()
	{
		return this.midiSequencer.getTempoFactor();
	}
}
