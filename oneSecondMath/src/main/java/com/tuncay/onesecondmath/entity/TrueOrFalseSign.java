package com.tuncay.onesecondmath.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.tuncay.onesecondmath.MainGamePanel;
import com.tuncay.onesecondmath.component.SoundManager;
import com.tuncay.onesecondmath.parameters.Globals;

public abstract class TrueOrFalseSign implements IItem{

	Bitmap bitmap;
	protected int x;
	protected int y;
	int width;	
	int height;	
	Context context;
	MainGamePanel gamePanel;
	int age;

	@Override
	public void update() {
		// TODO Auto-generated method stub
		age++;
	}

	@Override
	public void render(Canvas canvas) {
		// TODO Auto-generated method stub
		Rect src = new Rect(0, 0, width, height);
		Rect dst = new Rect(x, y, (int) (x + width * Globals.scaleX), (int) (y + height * Globals.scaleY));
		canvas.drawBitmap(bitmap, src, dst, null);
	}
	
	protected void trueAnswer() {
		SoundManager.playPoolSound(1,false);
		Globals.points++;
		Formula formula = null;
		for (Object o : Items.ItemList) {
			if (o.getClass().equals(Formula.class)) {
				formula = (Formula)o;
				break;
			}
		}
		if (formula != null)
			Items.ItemList.remove(formula);
		formula = new Formula(context, gamePanel);
		Items.ItemList.add(formula);
	}
}
