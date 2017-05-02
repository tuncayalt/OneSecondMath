package com.tuncay.onesecondmath;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.tuncay.onesecondmath.component.SoundManager;
import com.tuncay.onesecondmath.entity.BestPoints;
import com.tuncay.onesecondmath.entity.FalseSign;
import com.tuncay.onesecondmath.entity.Formula;
import com.tuncay.onesecondmath.entity.Items;
import com.tuncay.onesecondmath.entity.Points;
import com.tuncay.onesecondmath.entity.SoundMenu;
import com.tuncay.onesecondmath.entity.Timer;
import com.tuncay.onesecondmath.entity.TrueSign;
import com.tuncay.onesecondmath.parameters.Globals;

public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback{
	
	private static final String TAG = MainGamePanel.class.getSimpleName();
	
	public MainThread thread;
	public Display display;
	public int width;
	int height;

	MainGamePanelHelper gamePanelHelper = new MainGamePanelHelper();

	SharedPreferences prefs;
	String bestPointKey = "com.tuncay.onesecondmath.points";
	
	public interface Listener {
		public void onGameOver();
	}

	Listener mListener;
	
    public void setListener(Listener l) {
        mListener = l;
    }

	@SuppressWarnings("deprecation")
	public MainGamePanel(Context context, AttributeSet attrs) {
	    super(context, attrs);
	 // adding the callback (this) to the surface holder to intercept events
	 		getHolder().addCallback(this);

	 		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	 		display = wm.getDefaultDisplay();
	 		
	 		width = display.getWidth();
	 		height = display.getHeight() - 48;
	 		
	 		Globals.scaleX = ((float)width) / 720.0f;
	 		Globals.scaleY = ((float)height) / 1232.0f;
	 		
	 		destroy();
	 		
	 		prefs = this.getContext().getSharedPreferences(
	 			      "com.tuncay.onesecondmath", Context.MODE_PRIVATE);
	 		
	 		//addSounds();
	 		populateInitialScreen();
	}
	
	private void addSounds() {		
		SoundManager.getInstance();
		SoundManager.initSounds(this.getContext());
		SoundManager.addPoolSound(1, R.raw.trueanswer);
		SoundManager.addPoolSound(2, R.raw.timeout);
		SoundManager.addPoolSound(3, R.raw.gameover);
	}
	
	@SuppressLint("ClickableViewAccessibility") 
	public boolean onTouchEvent(MotionEvent event) {
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {			
			// delegating event handling to the Bubble
				
			if (Items.ItemList.size() > 0){
				for (int i = 0; i < Items.ItemList.size(); i++){			
					Items.ItemList.get(i).handleActionDown((int)event.getX(), (int)event.getY());
					Log.d(TAG, "end of item update");
				}
			}
		} 
		return true;
	}
	
	private void populateInitialScreen() {
		//From top to bottom
		int bestPoint = prefs.getInt(bestPointKey, 0);
		
		
		gamePanelHelper.setSoundMenu(new SoundMenu(this.getContext(), (int)(50 * Globals.scaleX), (int)(30 * Globals.scaleY)));
		BestPoints bestPoints = new BestPoints(this.getContext(), bestPoint, width - ((int)(250 * Globals.scaleX)), (int)(100 * Globals.scaleY));
		Timer timer = new Timer(this, 0, (int)(150 * Globals.scaleY), width, (int)(10 * Globals.scaleY));
		Points points = new Points(this.getContext(), this, (int)(250 * Globals.scaleY));
		Formula formula = new Formula(this.getContext(), this);
		TrueSign trueSign = new TrueSign(this.getContext(), this);
		FalseSign falseSign = new FalseSign(this.getContext(), this);
		
		Items.ItemList.add(gamePanelHelper.getSoundMenu());	
		Items.ItemList.add(bestPoints);
		Items.ItemList.add(timer);	
		Items.ItemList.add(points);
		Items.ItemList.add(formula);
		Items.ItemList.add(trueSign);
		Items.ItemList.add(falseSign);
		
		Globals.touched = false;
		Globals.gamePlayContinue = false;
		Globals.points = 0;
	}

	public void update() {
		if (!thread.isRunning())
			return;
		
		if (Items.ItemList.size() > 0){
			for (int i = 0; i < Items.ItemList.size(); i++){			
				Items.ItemList.get(i).update();									
				Log.d(TAG, "end of item update");
			}
		}
	}

	public void render(Canvas canvas) {
		if (!thread.isRunning())
			return;
		
		canvas.drawColor(Color.parseColor("#542437"));
		
		if (Items.ItemList.size() > 0){
			for (int i = 0; i < Items.ItemList.size(); i++){			
				Items.ItemList.get(i).render(canvas);									
				Log.d(TAG, "end of item render");
			}
		}
		
	}

	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {			
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being created");
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");	
	}

	public void pause() {
		thread.setRunning(false);
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
				Log.d(TAG, "Lifecy thread joined");
			}
			catch (InterruptedException e) {
				// try again shutting down the thread
				Log.d(TAG, "Lifecy trying to join thread");
			}
		}
		SoundManager.cleanup(true);
		//mediaPausePosition  = soundManager.pauseMediaSound(1);
	}

	public void resume() {
		addSounds();
		thread = new MainThread(getHolder(), this);
		thread.setRunning(true);
		thread.start();		
		//soundManager.playMediaSound(1, true, mediaPausePosition);
	}

	public void gameOver() {
		Globals.gamePlayContinue = false;
		int bestPoint = prefs.getInt(bestPointKey, 0);
		
		if (Globals.points > bestPoint)
			prefs.edit().putInt(bestPointKey, Globals.points).commit();
		
		destroy();
		
		/*Intent intent = new Intent(getContext(), GameOverFragment.class);		
		getContext().startActivity(intent);	*/	
		
		mListener.onGameOver();
		
		thread.setRunning(false);
//		((Activity)getContext()).finish();
		
	}
	

	public void destroy() {
		// TODO Auto-generated method stub
		Items.ItemList.clear();
		// Stop method tracing that the activity started during onCreate()
	}
}
