package com.tuncay.onesecondmath.entity;

import com.tuncay.onesecondmath.R;
import com.tuncay.onesecondmath.parameters.Globals;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class SoundMenu implements IItem{
	//private static final String TAG = SoundMenu.class.getSimpleName();

	Bitmap bitmap;
	int x;
	int y;
	int width;	
	int height;	
	int age;
	Context context;
	
	public SoundMenu(Context context, int x, int y) {
		this.context = context;
		this.x = x;
		this.y = y;
		
		if (Globals.soundOn)
			this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sound);
		else
			this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.soundoff);
		
		this.width = bitmap.getWidth();
		this.height = bitmap.getWidth();
	}

	@Override
	public void update() {
		age++;
	}

	@Override
	public void render(Canvas canvas) {
		Rect src = new Rect(0, 0, width, height);
		Rect dst = new Rect(x, y, x + width, y + height);
		canvas.drawBitmap(bitmap, src, dst, null);
	}

	public void handleActionDown(int eventX, int eventY) {
		if 	((eventX >= x) && 
			 (eventX <= (x + width)) && 
			 (eventY >= y) && 
			 (eventY <= (y + height))) {
		
			Globals.soundOn = !Globals.soundOn;
			if (Globals.soundOn)
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sound);
			else
				this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.soundoff);
		} 		
	}

}
