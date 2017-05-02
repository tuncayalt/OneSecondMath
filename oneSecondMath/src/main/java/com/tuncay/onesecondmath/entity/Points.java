package com.tuncay.onesecondmath.entity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.tuncay.onesecondmath.MainGamePanel;
import com.tuncay.onesecondmath.parameters.Globals;

public class Points implements IItem{

	//private static final String TAG = Points.class.getSimpleName();
	
	protected int x;
	protected int y;
	MainGamePanel gamePanel;
	
	int age;
	int points;
	
	public Points(Context context, MainGamePanel gamePanel, int y) {
		this.y = y;
		this.gamePanel = gamePanel;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		age++;
		points = Globals.points;
	}
	
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Globals.currentScore + " " + String.valueOf(points);
	}

	@Override
	public void render(Canvas canvas) {
		// TODO Auto-generated method stub
		Paint paint = new Paint();
		paint.setColor(Color.parseColor("#D95B43"));
		paint.setTextSize(50 * Globals.scaleY);
		int width = (int)paint.measureText(this.toString(), 0, this.toString().length());
		
		this.x = (int) ((gamePanel.width - width) / 2.0f);
		
		canvas.drawText(this.toString(), x, y, paint);
	}

	@Override
	public void handleActionDown(int eventX, int eventY) {
		// TODO Auto-generated method stub
		
	}
}
