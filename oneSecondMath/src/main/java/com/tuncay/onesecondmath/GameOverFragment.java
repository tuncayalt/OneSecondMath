package com.tuncay.onesecondmath;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.tuncay.onesecondmath.AnalyticsSampleApp.TrackerName;
import com.tuncay.onesecondmath.parameters.Globals;

public class GameOverFragment extends Fragment implements OnClickListener {
	
    String mExplanation = "";
    //int mScore = 0;
    boolean mShowSignIn = false;
    //AdView adView;
    
	public interface Listener {
        public void onWinScreenDismissed();
        public void onWinScreenSignInClicked();
        public void onWinStartOverClicked();
    }
	
	Listener mListener = null;
    
	public void setListener(Listener l) {
        mListener = l;
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
    		Bundle savedInstanceState) {  
        View v = inflater.inflate(R.layout.game_over, container, false);
        
        TextView tv = (TextView)v.findViewById(R.id.tvGameOverType);
        if (Globals.gameOverType == Globals.GameOverType.timeOut){
        	tv.setText(v.getContext().getResources().getString(R.string.game_over_type_time));
        }else{
        	tv.setText(v.getContext().getResources().getString(R.string.game_over_type_wrong));
        }
        	
        
        //sendScreenView();
        v.findViewById(R.id.dialog_button_start_over).setOnClickListener(this);
        v.findViewById(R.id.dialog_button_ok).setOnClickListener(this);
        v.findViewById(R.id.win_screen_sign_in_button).setOnClickListener(this);
        
     // Look up the AdView as a resource and load a request.
        //putAdv(v);
        
        putGoogleAnalytics();
        
        return v;
	}
	

	
	private void putGoogleAnalytics() {
		// Get tracker.
		    Tracker t = ((AnalyticsSampleApp) getActivity().getApplication()).getTracker(
		        TrackerName.APP_TRACKER);

		    // Set screen name.
		    // Where path is a String representing the screen name.
		    t.setScreenName("com.tuncay.onesecondmath.GameOverFragment");

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
    public void onStart() {
        super.onStart();
        updateUi();
    }

    void updateUi() {
        if (getActivity() == null) return;
        
        TextView scoreText = (TextView)getActivity().findViewById(R.id.txtScore);
        
        scoreText.setText("Your score is " + Globals.points);

        getActivity().findViewById(R.id.win_screen_sign_in_bar).setVisibility(
                mShowSignIn ? View.VISIBLE : View.GONE);
        getActivity().findViewById(R.id.win_screen_signed_in_bar).setVisibility(
                mShowSignIn ? View.GONE : View.VISIBLE);
    }
	
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.win_screen_sign_in_button) {
            mListener.onWinScreenSignInClicked();
            mListener.onWinScreenDismissed();
        }
        else if (view.getId() == R.id.dialog_button_start_over){
        	mListener.onWinStartOverClicked();
        }
        else{
        	mListener.onWinScreenDismissed();
        }
    }

    public void setShowSignInButton(boolean showSignIn) {
        mShowSignIn = showSignIn;
        updateUi();
    }

    /*public void setFinalScore(int i) {
        mScore = i;
    }*/
}
