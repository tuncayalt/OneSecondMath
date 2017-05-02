package com.tuncay.onesecondmath.entity;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.tuncay.onesecondmath.MainGamePanel;
import com.tuncay.onesecondmath.R;
import com.tuncay.onesecondmath.component.SoundManager;
import com.tuncay.onesecondmath.parameters.Globals;

public class FalseSign extends TrueOrFalseSign{
	
	//private static final String TAG = TrueSign.class.getSimpleName();
	
	public FalseSign(Context context, MainGamePanel gamePanel) {
		this.context = context;
		this.gamePanel = gamePanel;
		
		this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.falsesign);
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
		
		this.x = (gamePanel.width / 2);
		this.y = (int)(700 * Globals.scaleY);
	}

	@Override
	public void handleActionDown(int eventX, int eventY) {
		// TODO Auto-generated method stub
		if 	((eventX >= x) && 
			 (eventX <= (x + width * Globals.scaleX + 20)) && 
			 (eventY >= y - 10) && 
			 (eventY <= (y + height * Globals.scaleY + 10))) {
			
				Globals.touched = true;
				if (Globals.trueOrFalse){
					SoundManager.playPoolSound(3, false);
					Globals.gameOverType = Globals.GameOverType.wrongAnswer;
					gamePanel.gameOver();
				}else{
					super.trueAnswer();
				}
		} 		
	}

}
