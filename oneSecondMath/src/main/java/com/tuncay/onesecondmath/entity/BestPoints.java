package com.tuncay.onesecondmath.entity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.tuncay.onesecondmath.parameters.Globals;

public class BestPoints implements IItem{

	private static final String TAG = BestPoints.class.getSimpleName();
	
	protected int x;
	protected int y;
	
	int age;
	int points;
	Context context;
	int bestPoint;
	
	public BestPoints(Context context, int bestPoint, int x, int y) {
		this.x = x;
		this.y = y;
		this.context = context;
		this.bestPoint = bestPoint;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		age++;
	}

	@Override
	public void render(Canvas canvas) {
		// TODO Auto-generated method stub
		Paint paint = new Paint();
		paint.setColor(Color.parseColor("#D95B43"));
		paint.setTextSize(44 * Globals.scaleX);
		canvas.drawText(Globals.best + " " + String.valueOf(bestPoint), x, y, paint);
		Log.d(TAG, TAG + "drawn");  
	}

	@Override
	public void handleActionDown(int eventX, int eventY) {
		// TODO Auto-generated method stub
		
	}
}
