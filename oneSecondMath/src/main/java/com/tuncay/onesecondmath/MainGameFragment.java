package com.tuncay.onesecondmath;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.tuncay.onesecondmath.AnalyticsSampleApp.TrackerName;

public class MainGameFragment extends Fragment implements OnClickListener, MainGamePanel.Listener {

	private static final String TAG = MainGameFragment.class.getSimpleName();
	
	public interface Listener {
		//public void onEnteredScore(int requestedScore);
		public void onGameOver();
	}

	Listener mListener;
	
    public void setListener(Listener l) {
        mListener = l;
    }

	//private LinearLayout mainGameLayout;
	public MainGamePanel gamePanel;
	//AdView adView;
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  	  // requesting to turn the title OFF
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
        // set our MainGamePanel as the View
    	View v = inflater.inflate(R.layout.main_game_layout, container, false);
    	gamePanel = (MainGamePanel) v.findViewById(R.id.maingamepanel);
    	gamePanel.setListener(this);
    	
    	// Look up the AdView as a resource and load a request.
        //putAdv(v);
        
        putGoogleAnalytics();
        
        Log.d(TAG, "View added");
		return v;        	
    }

	private void putGoogleAnalytics() {
		// Get tracker.
		    Tracker t = ((AnalyticsSampleApp) getActivity().getApplication()).getTracker(
		        TrackerName.APP_TRACKER);

		    // Set screen name.
		    // Where path is a String representing the screen name.
		    t.setScreenName("com.tuncay.onesecondmath.MainGameFragment");

		    // Send a screen view.
		    t.send(new HitBuilders.AppViewBuilder().build());
	}

	/*private void putAdv(View v) {
		adView = (AdView)v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		.addTestDevice("D40614808A0D116B440AFCD00348185F")
        .build();
        // Start loading the ad in the background.
        adView.loadAd(adRequest);
	}*/

	@Override
	public void onPause() {
		Log.d(TAG, "Lifecy onPause start");
		//adView.pause();
		super.onPause();
		gamePanel.pause();
		Log.d(TAG, "Lifecy onPause end");
	}

	@Override
	public void onResume() {
		Log.d(TAG, "Lifecy onResume start");
		super.onResume();
		gamePanel.resume();
		//adView.resume();
		Log.d(TAG, "Lifecy onResume end");
	}
	
	@Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass
        gamePanel.destroy();        
        android.os.Debug.stopMethodTracing();
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void onGameOver() {
		mListener.onGameOver();
	}
}
