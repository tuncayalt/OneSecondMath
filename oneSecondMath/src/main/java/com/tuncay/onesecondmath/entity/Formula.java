package com.tuncay.onesecondmath.entity;

import java.util.Random;

import com.tuncay.onesecondmath.MainGamePanel;
import com.tuncay.onesecondmath.parameters.Globals;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Formula implements IItem{

	//private static final String TAG = Formula.class.getSimpleName();
	
	int x;
	int y;
	
	int width; 
	
	int leftSide;
	int rightSide;
	char operator;
	MainGamePanel gamePanel;
	
	Random random;
	
	int age;
	
	public Formula(Context context, MainGamePanel gamePanel) {
		// TODO Auto-generated constructor stub
		
		this.gamePanel = gamePanel;
		
		random = new Random();
		boolean trueOrFalse = random.nextBoolean();
		
		int operatorHelper = random.nextInt(3);
		switch (operatorHelper){
		case 0:
			operator = '=';
			break;
		case 1:
			operator = '<';
			break;
		case 2:
			operator = '>';
			break;
		}
		
		leftSide = random.nextInt(99) + 1;
		rightSide = decideRightSide(trueOrFalse, operator, leftSide);
		Globals.trueOrFalse = trueOrFalse;
	}

	private int decideRightSide(boolean _trueOrFalse, char _operator,
			int _leftSide) {
		int _rightSide = -1;
		
		if (_trueOrFalse && _operator == '='){
			_rightSide = _leftSide;
		}
		else if (_trueOrFalse && _operator == '<'){
			if (_leftSide == 99){
				_rightSide = 100;
			}else{
				_rightSide = _leftSide + 1 + random.nextInt(99 - _leftSide);
			}
		}
		else if (_trueOrFalse && _operator == '>'){
			_rightSide = _leftSide - 1 - random.nextInt(_leftSide);
		}
		else if (!_trueOrFalse && _operator == '='){
			_rightSide = random.nextInt(101);
			while (_rightSide == _leftSide){
				_rightSide = random.nextInt(101);
			}
		}
		else if (!_trueOrFalse && _operator == '<'){
			if (_leftSide == 1){
				_rightSide = 1;
			}else{
				_rightSide = _leftSide - random.nextInt(_leftSide - 1);
			}
		}
		else if (!_trueOrFalse && _operator == '>'){
			_rightSide = _leftSide + random.nextInt(100 - _leftSide);
		}
		
		return _rightSide;
	}


	
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append(leftSide);
		str.append(" ");
		str.append(operator);
		str.append(" ");
		str.append(rightSide);
		
		return str.toString();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		age++;
	}

	@Override
	public void render(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.parseColor("#ECD078"));
		paint.setTextSize(150 * Globals.scaleY);
		width = (int)paint.measureText(this.toString(), 0, this.toString().length());
		
		this.x = (int) ((gamePanel.width - width) / 2.0f);
		this.y = (int)(500 * Globals.scaleY);
		
		canvas.drawText(this.toString(), x, y, paint);
	}

	@Override
	public void handleActionDown(int eventX, int eventY) {
		// TODO Auto-generated method stub
		
	}

}
