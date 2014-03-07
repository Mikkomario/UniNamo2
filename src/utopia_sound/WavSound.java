package utopia_sound;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import utopia_listeners.SoundListener;

/**
 * WavSound represents a single sound that can be played during the program. 
 * Each sound has a name so it can be differentiated from other wavsounds. 
 * Wavsounds often serve as shorter sound effects since the filesize is rather 
 * large
 *
 * @author Mikko Hilpinen.
 *         Created 17.8.2013.
 */
public class WavSound extends Sound
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private LinkedList<WavPlayer> players;
	//private String filename;
	private File soundfile;
	private float defaultvolume, defaultpan;
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new wawsound with the given information
	 *
	 * @param filename The location of the wav sound file (data/ is 
	 * automatically included)
	 * @param name The name of the sound that differentiates it from other 
	 * sounds
	 * @param defaultvolume How many desibels the volume is adjusted by default
	 * @param defaultpan How much pan is added by default [-1, 1]
	 */
	public WavSound(String filename, String name, float defaultvolume, 
			float defaultpan)
	{
		super(name);
		
		// Initializes attributes
		//this.filename = "/data/" + filename;
		this.soundfile = new File("data/" + filename);
		this.defaultvolume = defaultvolume;
		this.defaultpan = defaultpan;
		this.players = new LinkedList<WavPlayer>();
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------
	
	@Override
	public void playSound()
	{
		startsound(this.defaultvolume, this.defaultpan, false);
	}
	
	@Override
	public void loopSound()
	{
		startsound(this.defaultvolume, this.defaultpan, true);
	}
	
	/**
	 * Stops all instances of the sound from playing
	 */
	@Override
	public void stopSound()
	{
		// Stops all of the sounds playing
		Iterator<WavPlayer> i = this.players.iterator();
		
		while (i.hasNext())
			i.next().stopSound();
	}

	/**
	 * Pauses all instances of the sound playing
	 */
	@Override
	public void pause()
	{
		// Stops all of the sounds playing
		Iterator<WavPlayer> i = this.players.iterator();
		
		while (i.hasNext())
			i.next().pause();
	}
	
	/**
	 * Unpauses all the paused instances of the sound
	 */
	@Override
	public void unpause()
	{
		// Stops all of the sounds playing
		Iterator<WavPlayer> i = this.players.iterator();
		
		while (i.hasNext())
			i.next().unpause();
	}
	
	@Override
	public boolean isPlaying()
	{
		// Paused sounds are also counted as playing
		return this.players.isEmpty();
	}
	
	
	// OTHER METHODS	------------------------------------------------
	
	private void startsound(float volume, float pan, boolean loops)
	{
		//System.out.println("Playing a sound using pan: " + pan);
		
		WavPlayer newplayer = new WavPlayer(pan, volume, loops);
		newplayer.start();
		this.players.add(newplayer);
	}
	
	/**
	 * Plays the sound using the given settings
	 *
	 * @param volume How many desibels the sound's volume is increased / decreased
	 * @param pan How much the sound is panned [-1, 1]
	 * @param specificlistener A listener that listens to only this instance of 
	 * the sound (null if no listener is needed)
	 */
	public void play(float volume, float pan, SoundListener specificlistener)
	{
		startsound(this.defaultvolume + volume, pan, false);
		informSoundStart(specificlistener);
	}
	
	/**
	 * Loops the sound using the given settings
	 *
	 * @param volume How many desibels the sound's volume is increased / decreased
	 * @param pan How much the sound is panned [-1, 1]
	 * 
	 * @param specificlistener A listener that listens to only this instance of 
	 * the sound (null if no listener is needed)
	 */
	public void loop(float volume, float pan, SoundListener specificlistener)
	{
		startsound(volume, pan, true);
		informSoundStart(specificlistener);
	}
	
	/**
	 * Stops the oldest playing instance of the sound
	 */
	public void stopOldest()
	{
		// Stops the oldest wavplayer
		this.players.getFirst().stopSound();
		informSoundEnd();
	}
	
	/**
	 * Pauses the oldest unpaused instance of the sound
	 */
	public void pauseOldest()
	{
		Iterator<WavPlayer> i = this.players.iterator();
		
		while(i.hasNext())
		{
			WavPlayer p = i.next();
			if (!p.paused)
			{
				p.pause();
				break;
			}
		}
	}
	
	/**
	 * Unpauses the oldest paused instance of the sound
	 */
	public void unpauseOldest()
	{
		Iterator<WavPlayer> i = this.players.iterator();
		
		while(i.hasNext())
		{
			WavPlayer p = i.next();
			if (p.paused)
			{
				p.unpause();
				break;
			}
		}
	}
	
	private void onSoundEnd(WavPlayer source)
	{
		// Removes the old player from the list of players
		this.players.remove(source);
		
		// If the sound should loop, plays it again
		if (source.looping)
			loop(source.volume, source.pan, source.listener);
		// Otherwise, informs the listeners (if the sound stopped naturally)
		else if (!source.stopped)
			informSoundEnd();
	}
	
	
	// SUBCLASSES	-----------------------------------------------------
	
	/**
	 * WavPlayer loads and plays wav files with the given settings
	 * 
	 * @author http://www.anyexample.com/programming/java/java_play_wav_sound_file.xml
	 * Modified by: Mikko Hilpinen
	 */
	private class WavPlayer extends Thread
	{  
		// ATTRIBUTES	------------------------------------------------------
		
	    private float pan, volume;
	    private final int EXTERNAL_BUFFER_SIZE = 262144;//64kb //524288; // 128Kb
	    private boolean paused, looping, stopped;
	    private SoundListener listener;
	 
	    
	    // CONSTRUCTOR	-----------------------------------------------------
		
		/**
		 * Creates a new wavplayer with custom settings
		 *
		 * @param pan How much the sound is panned [-1 (left speaker only), 
		 * 1 (right speaker only)] (0 default)
		 * @param volume How much the volume is adjusted in desibels (default 0)
		 * @param loops Should the sound be looped after it ends?
		 */
		public WavPlayer(float pan, float volume, boolean loops)
		{
	        this.pan = pan;
	        this.volume = volume;
	        this.paused = false;
	        this.looping = loops;
	        this.stopped = false;
	        
	        // Checks that the pan is within limits
	        if (this.pan < -1)
	        	this.pan = -1;
	        else if (this.pan > 1)
	        	this.pan = 1;
	    }
		
		
		// IMPLEMENTED METHODS	----------------------------------------------
	 
	    @Override
		public void run()
	    {
	        // Reads the file as an audioinputstream
	        AudioInputStream audioInputStream = null;
	        try
	        {
	        	// TODO: WHY U NO READ IN STEREO FORMAT?!?
	        	audioInputStream = AudioSystem.getAudioInputStream(
	        			WavSound.this.soundfile);
	        			//this.getClass().getResourceAsStream(WavSound.this.filename));
	        }
	        catch (UnsupportedAudioFileException e1)
	        {
	        	System.err.println("Audiofile " + //WavSound.this.filename + 
	        			" not supported!");
	            e1.printStackTrace();
	            return;
	        }
	        catch (IOException e1)
	        {
	        	System.err.println("Failed to load the audio file!");
	            e1.printStackTrace();
	            return;
	        } 
	        catch (NullPointerException npe)
	        {
	        	System.err.println("Could not find the file " /*+ WavSound.this.filename*/);
	        	npe.printStackTrace();
	        	return;
	        }
	 
	        // Opens the audioline
	        AudioFormat format = audioInputStream.getFormat();
	        SourceDataLine auline = null;
	        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
	        
	        //System.out.println(format);
	 
	        try
	        { 
	            auline = (SourceDataLine) AudioSystem.getLine(info);
	            auline.open(format);
	        }
	        catch (LineUnavailableException e)
	        {
	        	System.err.println("Audioline unavailable");
	            e.printStackTrace();
	            return;
	        }
	       
	        // Prints the possible controls
	        /*
	        Control[] controls = auline.getControls();
	        for (int i = 0; i < controls.length; i++)
	        {
	        	System.out.println(controls[i]);
		    }
	        */
	        
	        // Tries to set the correct pan (if needed)
	        if (this.pan != 0 && auline.isControlSupported(FloatControl.Type.PAN))
	        { 
	        	// TODO: Pan is nor supported?
	        	
	            FloatControl pancontrol = (FloatControl) auline.getControl(
	            		FloatControl.Type.PAN);
	            pancontrol.setValue(this.pan);
	        }
	     	// Tries to set the correct pan #2 (if needed)
	        if (this.pan != 0 && auline.isControlSupported(FloatControl.Type.BALANCE))
	        { 
	        	// TODO: Pan is nor supported?
	        	
	            FloatControl pancontrol = (FloatControl) auline.getControl(
	            		FloatControl.Type.BALANCE);
	            pancontrol.setValue(this.pan);
	        }
	        // Tries to set the correct volume (if needed)
	        if (this.volume != 0 && auline.isControlSupported(FloatControl.Type.VOLUME))
	        { 
	            FloatControl volumecontrol = (FloatControl) auline.getControl(
	            		FloatControl.Type.VOLUME);
	            volumecontrol.setValue(this.volume);
	        } 
	 
	        // Plays the track
	        auline.start();
	        int nBytesRead = 0;
	        byte[] abData = new byte[this.EXTERNAL_BUFFER_SIZE];
	 
	        try
	        { 
	        	// Reads the stream until it ends or until the sound is forced 
	        	// to stop
	            while (nBytesRead != -1 && !this.stopped)
	            {
	            	if (!this.paused)
	            	{
		                nBytesRead = audioInputStream.read(abData, 0, abData.length);
		                if (nBytesRead >= 0) 
		                    auline.write(abData, 0, nBytesRead);
	            	}
	            }
	        }
	        catch (IOException e)
	        {
	        	System.err.println("Error in playing the soundfile "/* + 
	        			WavSound.this.filename*/);
	            e.printStackTrace();
	            return;
	        }
	        // Closes the file after the sound has stopped playing
	        finally
	        { 
	            auline.drain();
	            auline.close();
	            // Also informs the WavSound that the current file ended
	            onSoundEnd(this);
	        }
	    }
	    
	    
	    // OTHER METHODS	---------------------------------------------------
	    
	    /**
	     * Temporarily stops the sound from playing (if it was playing)
	     */
	    public void pause()
	    {
	    	this.paused = true;
	    }
	    
	    /**
	     * Continues a paused sound
	     */
	    public void unpause()
	    {
	    	this.paused = false;
	    }
	    
	    /**
	     * Stops the sound from playing and looping
	     */
	    public void stopSound()
	    {
	    	this.stopped = true;
	    	this.looping = false;
	    }
	}
}
