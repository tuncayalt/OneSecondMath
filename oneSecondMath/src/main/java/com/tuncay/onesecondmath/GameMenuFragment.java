package com.tuncay.onesecondmath;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.tuncay.onesecondmath.AnalyticsSampleApp.TrackerName;


public class GameMenuFragment extends Fragment implements OnClickListener  {
	
	private static final String TAG = GameMenuFragment.class.getSimpleName();
	private static final String PACKAGE = GameMenuFragment.class.getPackage().getName();

	public interface Listener {
		public void onStartGameRequested();
		public void onShowAchievementsRequested();
		public void onShowLeaderboardsRequested();
		public void onShowRateAppRequested();
		public void onSignInButtonClicked();
		public void onSignOutButtonClicked();
	}

	private Listener mListener;
	boolean mShowSignIn = true;
	//private String mGreeting;

    public void setListener(Listener l) {
        mListener = l;
    }
    
    //AdView adView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
    		Bundle savedInstanceState) {   
        View v = inflater.inflate(R.layout.main_menu, container, false);
        
        v.findViewById(R.id.play_game).setOnClickListener(this);
        v.findViewById(R.id.show_achievements_button).setOnClickListener(this);
        v.findViewById(R.id.show_leaderboards_button).setOnClickListener(this);
        v.findViewById(R.id.show_rate_app).setOnClickListener(this);
        v.findViewById(R.id.sign_in_button).setOnClickListener(this);
        v.findViewById(R.id.sign_out_button).setOnClickListener(this);
        
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
		    t.setScreenName(PACKAGE + "." + TAG);

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
    
    /*@Override
    public void onStart() {
        super.onStart();
        updateUi();
    }*/

    void updateUi() {
        if (getActivity() == null) return;

        getActivity().findViewById(R.id.sign_in_bar).setVisibility(mShowSignIn ?
                View.VISIBLE : View.GONE);
        getActivity().findViewById(R.id.sign_out_bar).setVisibility(mShowSignIn ?
                View.GONE : View.VISIBLE);
    }


    public void setShowSignInButton(boolean showSignIn) {
        mShowSignIn = showSignIn;
        updateUi();
    }

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.play_game) {
            mListener.onStartGameRequested();
        }
		else if(view.getId() == R.id.show_achievements_button){
			mListener.onShowAchievementsRequested();
		}
		else if(view.getId() == R.id.show_leaderboards_button){
			mListener.onShowLeaderboardsRequested();
		}
		else if(view.getId() == R.id.show_rate_app){
			mListener.onShowRateAppRequested();
		}
		else if(view.getId() == R.id.sign_in_button){
			mListener.onSignInButtonClicked();
		}
		else if(view.getId() == R.id.sign_out_button){
			mListener.onSignOutButtonClicked();
		}
	}

    /*public void setGreeting(String greeting) {
        mGreeting = greeting;
        updateUi();
    }*/

    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }
}
