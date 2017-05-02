package com.tuncay.onesecondmath.component;

import java.util.HashMap;

import com.tuncay.onesecondmath.parameters.Globals;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.Log;

@SuppressLint("UseSparseArrays")
public class SoundManager {
	
	protected static final String TAG = SoundManager.class.getSimpleName();
	
	private static SoundManager _instance;
	private static SoundPool soundPool;
	private static HashMap<Integer, Integer> soundPoolMap;
	private static HashMap<Integer, MediaPlayer> soundMediaMap;
	private static AudioManager  audioManager;	 
	private static Context context;	
	private static MediaPlayer mediaPlayer;
	
	private static boolean loaded;
	
	private SoundManager(){
		
	}	
	
	/**
     * Requests the instance of the Sound Manager and creates it
     * if it does not exist.
     *
     * @return Returns the single instance of the SoundManager
     */
    static synchronized public SoundManager getInstance()
    {
        if (_instance == null)
          _instance = new SoundManager();
        return _instance;
    	}
	
	public static void initSounds(Context _context) {
		loaded = false;
		context = _context;
		soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                    int status) {
                loaded = true;
            }
        });
		soundPoolMap = new HashMap<Integer, Integer>();
		soundMediaMap = new HashMap<Integer, MediaPlayer>();
		audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
	}
	
	public static void addPoolSound(int index, int soundID)
	{
	    soundPoolMap.put(index, soundPool.load(context, soundID, 1));
	}
	
	public void addMediaSound(int index, int soundID){
		soundMediaMap.put(index, MediaPlayer.create(context, soundID));
	}
	
	public static void playPoolSound(int index, boolean loop)
	{
		if (loaded){
			float volume;
			if (Globals.soundOn){
				float streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
				streamVolume = streamVolume / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				volume = streamVolume;
			}
			else{
				volume = 0;
			}
			if (loop){
				soundPool.play(soundPoolMap.get(index), volume, volume, 1, -1, 1f);
			}else{
				soundPool.play(soundPoolMap.get(index), volume, volume, 1, 0, 1f);
			}
			Log.d(TAG, "sound played");			
		}		
	}	
	
	/**
	 * Stop a Sound
	 * @param index - index of the sound to be stopped
	 */
	public static void stopPoolSound(int index)
	{
		soundPool.stop(soundPoolMap.get(index));
	}
	
	public static void playMediaSound(int index, boolean loop, int mediaPausePosition){
		float volume;
		if (Globals.soundOn){
			float streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			streamVolume = streamVolume / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			volume = streamVolume;
		}else{
			volume = 0;
		}
	    mediaPlayer = soundMediaMap.get(index);
	    
	    if (mediaPlayer != null){
	    	mediaPlayer.setVolume(volume, volume);

	    	if (mediaPausePosition != 0){
	    		mediaPlayer.seekTo(mediaPausePosition);
	    	}
	    	if (loop){
	    		mediaPlayer.setLooping(true);
	    	}
	    	mediaPlayer.start();
	    }
		Log.d(TAG, "media sound started");		
	}
	
	public static void stopMediaSound(int index){
		mediaPlayer = soundMediaMap.get(index);
		if (mediaPlayer != null){
			mediaPlayer.stop();
		}
		Log.d(TAG, "media sound stopped");		
	}

	public static int pauseMediaSound(int index) {
		int length = 0;
		mediaPlayer = soundMediaMap.get(index);
		if (mediaPlayer != null){
			length=mediaPlayer.getCurrentPosition();
			mediaPlayer.pause();
		}
		Log.d(TAG, "media sound paused");	
		return length;				
	}

	public static void cleanup(boolean waitUntilPoolSoundEnds)
	{
		while (waitUntilPoolSoundEnds && audioManager.isMusicActive()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Log.d(TAG, e.getMessage());	
			}
		}
		if (soundPool != null)
			soundPool.release();
		soundPool = null;
		if (mediaPlayer != null) 
			mediaPlayer.release();
		mediaPlayer = null; 
		audioManager.unloadSoundEffects();
		soundPoolMap.clear();
		soundMediaMap.clear();
		_instance = null;
		Log.d(TAG, "Lifecy sounds cleaned up");
	}
}
