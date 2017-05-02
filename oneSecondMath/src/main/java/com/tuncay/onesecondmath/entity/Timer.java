package com.tuncay.onesecondmath.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.tuncay.onesecondmath.MainGamePanel;
import com.tuncay.onesecondmath.component.SoundManager;
import com.tuncay.onesecondmath.parameters.Globals;

public class Timer implements IItem{

	private static final String TAG = Timer.class.getSimpleName();

	protected int x;
	protected int y;
	protected int width;	
	protected int height;	
	protected int fullTime; 
	protected int remainingTime;
	int age;
	boolean timePassing;
	long lastTime;
	MainGamePanel gamePanel;
	
	public Timer(MainGamePanel gamePanel, int x, int y, int width, int height){
		//this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.timer);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.gamePanel = gamePanel;
		
		fullTime = 1000;
		remainingTime = fullTime;
		
		lastTime = System.currentTimeMillis();
		
		Log.d(TAG, "Timer is being created");	
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub	
		age++;
		
		long now = System.currentTimeMillis();

		if (Globals.gamePlayContinue){
			remainingTime -= now - lastTime;
		}
		Log.d(TAG, "Now: " + now + " lastTime: " + lastTime);	
		lastTime = now;
		
		if (Globals.touched == true){
			Globals.touched = false;
			remainingTime = fullTime;
			Globals.gamePlayContinue = true;
		}
		if (remainingTime <= 0){
			remainingTime = 0;
			SoundManager.playPoolSound(2, false);
			Globals.gameOverType = Globals.GameOverType.timeOut;
			gamePanel.gameOver();
		}
		
	}

	@Override
	public void render(Canvas canvas) {
		// TODO Auto-generated method stub
		float fRemainingTime = remainingTime;
		float fFullTime = fullTime;
		float rate = fRemainingTime / fFullTime;
		float barX = x;
		float barY = y;
		RectF rect;
		Paint paint;
		if (rate > 0){
		    rect = new RectF (barX, barY, barX + width * rate, barY + height);
			paint = new Paint();
			paint.setColor(Color.GREEN);
			canvas.drawRect(rect, paint);
		}
		if (rate < 1){
				
			barX += rate * width;
			rect = new RectF(barX, barY, barX + (1 - rate) * width,
						barY + height);
			paint = new Paint();
			paint.setColor(Color.RED);
			canvas.drawRect(rect, paint);
		}
		
	}

	@Override
	public void handleActionDown(int eventX, int eventY) {
		// TODO Auto-generated method stub
		
	}

}
