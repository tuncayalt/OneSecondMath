package com.tuncay.onesecondmath;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import com.tuncay.onesecondmath.parameters.Globals;

public class MainThread extends Thread{

	private static final String TAG = MainThread.class.getSimpleName();
	// desired fps
	private int MAX_FPS = Globals.MAX_FPS;
	// maximum number of frames to be skipped
	private int	MAX_FRAME_SKIPS = Globals.MAX_FRAME_SKIPS;
	// the frame period
	private int framePeriod = 1000 / MAX_FPS;

	// Surface holder that can access the physical surface
	private SurfaceHolder surfaceHolder;
	// The actual view that handles inputs
	// and draws to the surface
	private MainGamePanel gamePanel;

	// flag to hold game state
	private boolean running;	
	
	long beginTime;		// the time when the cycle begun
	long timeDiff;		// the time it took for the cycle to execute
	int sleepTime;		// ms to sleep (<0 if we're behind)
	int framesSkipped;	// number of frames being skipped 

	public boolean isRunning() {
		return running;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}

	public MainThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;		
	}

	@Override
	public void run() {
		Canvas canvas;
		Log.d(TAG, "Starting game loop");

		sleepTime = 0;

		while (running) {
			
			if (!surfaceHolder.getSurface().isValid())
				continue;
			
			canvas = null;
			
			// try locking the canvas for exclusive pixel editing
			// in the surface							
			try {					
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					RunGame(canvas);			
				}
			}
			catch(Exception e){
				Log.d(TAG, "catching exception:" + e.getMessage());
				e.printStackTrace();
				setRunning(false);
			}
			finally {
				// in case of an exception the surface is not left in
				// an inconsistent state
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}	// end finally
		}
	}
	
	private void RunGame(Canvas canvas){
		beginTime = System.currentTimeMillis();
		framesSkipped = 0;	// resetting the frames skipped
				
		this.gamePanel.update();  // update game state  		
		
		this.gamePanel.render(canvas); //render state to the screen.. draws the canvas on the panel
		  		
		// calculate how long did the cycle take
		timeDiff = System.currentTimeMillis() - beginTime;
		
		// calculate sleep time
		sleepTime = (int)(framePeriod - timeDiff);

		if (sleepTime > 0) {
			// if sleepTime > 0 we're OK
			try {
				// send the thread to sleep for a short period
				// very useful for battery saving
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				Log.d(TAG, e.getMessage());	
			}
		}

		while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS ) {
			// we need to catch up
			this.gamePanel.update(); // update without rendering
			sleepTime += framePeriod;	// add frame period to check if in next frame
			framesSkipped++;
		}
	}
}

