package com.tuncay.onesecondmath.parameters;


public class Globals{

	public static final int maxStartNumberForAds = 14;
	
	// max number of frames per second
	public static int MAX_FPS = 35;
	// max number of frames to be skipped
	public static int MAX_FRAME_SKIPS = 5;
	
	public static boolean soundOn = true;
	public static boolean gamePlayContinue = false;
	public static boolean touched = true;
	public static int points = 0;
	public static boolean trueOrFalse;
	public static float scaleX;
	public static float scaleY;
	public static String packageName = "com.tuncay.onesecondmath";
	
	//from strings.xml, set in MainActivity.onCreate
	public static String best = "";
	public static String currentScore = "";

	public static GameOverType gameOverType;
	public enum GameOverType {timeOut, wrongAnswer};
	
	//Google Analytics
	public static final String propertyId = "UA-47999902-3";
	
}
