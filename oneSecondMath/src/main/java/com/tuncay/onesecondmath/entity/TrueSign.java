package com.tuncay.onesecondmath.entity;


import android.content.Context;
import android.graphics.BitmapFactory;

import com.tuncay.onesecondmath.MainGamePanel;
import com.tuncay.onesecondmath.R;
import com.tuncay.onesecondmath.component.SoundManager;
import com.tuncay.onesecondmath.parameters.Globals;

public class TrueSign extends TrueOrFalseSign{
	
	//private static final String TAG = TrueSign.class.getSimpleName();

	public TrueSign(Context context, MainGamePanel gamePanel) {
		this.context = context;
		this.gamePanel = gamePanel;
		
		this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.truesign);
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
		this.x = (int) ((gamePanel.width / 2) - width * Globals.scaleX);
		this.y = (int)(700 * Globals.scaleY);
	}



	@Override
	public void handleActionDown(int eventX, int eventY) {
		if 	((eventX >= x - 20) && 
				(eventX <= (x + width * Globals.scaleX)) && 
				(eventY >= y - 10) && 
				(eventY <= (y + height * Globals.scaleY + 10))) {

			Globals.touched = true;
			if (!Globals.trueOrFalse){
				SoundManager.playPoolSound(3, false);
				Globals.gameOverType = Globals.GameOverType.wrongAnswer;
				gamePanel.gameOver();
			}else{
				super.trueAnswer();
			}
		} 		
	}
}
