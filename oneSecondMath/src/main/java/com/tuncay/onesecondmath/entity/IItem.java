package com.tuncay.onesecondmath.entity;

import android.graphics.Canvas;

public interface IItem {
	void update();
	void render(Canvas canvas);
	void handleActionDown(int eventX, int eventY);
}
