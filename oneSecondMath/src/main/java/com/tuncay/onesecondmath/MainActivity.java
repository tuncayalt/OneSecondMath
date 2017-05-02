package com.tuncay.onesecondmath;

/*
 * Copyright (C) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;*/

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.tuncay.onesecondmath.component.AppRater;
import com.tuncay.onesecondmath.parameters.Globals;

/**
 * Our main activity for the game.
 *
 * IMPORTANT: Before attempting to run this sample, please change
 * the package name to your own package name (not com.android.*) and
 * replace the IDs on res/values/ids.xml by your own IDs (you must
 * create a game in the developer console to get those IDs).
 *
 * This is a very simple game where the user selects "easy mode" or
 * "hard mode" and then the "gameplay" consists of inputting the
 * desired score (0 to 9999). In easy mode, you get the score you
 * request; in hard mode, you get half.
 *
 * @author Bruno Oliveira
 */
public class MainActivity extends BaseGameActivity
        implements 
        GameMenuFragment.Listener,
        MainGameFragment.Listener, 
        GameOverFragment.Listener{
	
	private static final String TAG = MainActivity.class.getSimpleName();
	final int RC_RESOLVE = 5000, RC_UNUSED = 5001;
	
    // Fragments
    GameMenuFragment mGameMenuFragment;
    MainGameFragment mMainGameFragment;
    GameOverFragment mGameOverFragment;

    // achievements and scores we're pending to push to the cloud
    // (waiting for the user to sign in, for instance)
    AccomplishmentsOutbox mOutbox = new AccomplishmentsOutbox();
    
    SharedPreferences prefs;
	String pointKey = "com.tuncay.onesecondmath.currentpoints";
	String interAdKey = "com.tuncay.onesecondmath.interadkey";
	/*String accBeginner = "com.tuncay.onesecondmath.accBeginner";
	String accIntermediate = "com.tuncay.onesecondmath.accIntermediate";
	String accExpert = "com.tuncay.onesecondmath.accExpert";
	String accHeroic = "com.tuncay.onesecondmath.accHeroic";
	String accLegendary = "com.tuncay.onesecondmath.accLegendary";*/
	
	int bestPoint = 0;
	
	private InterstitialAd interstitial;
	private AdRequest adRequest;
	private AdView adView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
     // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);     
        //lights on
    	//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   

    	prefs = this.getSharedPreferences(
			      Globals.packageName, Context.MODE_PRIVATE);
    	
    	// create fragments
        mGameMenuFragment = new GameMenuFragment();
        mMainGameFragment = new MainGameFragment();
        mGameOverFragment = new GameOverFragment();

        // listen to fragment events
        mGameMenuFragment.setListener(this);
        mMainGameFragment.setListener(this);
        mGameOverFragment.setListener(this);

        // add initial fragment (welcome fragment)
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                mGameMenuFragment, "mGameMenuFragment").commit();

        // IMPORTANT: if this Activity supported rotation, we'd have to be
        // more careful about adding the fragment, since the fragment would
        // already be there after rotation and trying to add it again would
        // result in overlapping fragments. But since we don't support rotation,
        // we don't deal with that for code simplicity.

        // load outbox from file
        mOutbox.loadLocal(this);
        
        putAdv();
        
        loadInterstitialAd();
        AppRater.app_launched(this);
        //AppRater.showRateDialog(this, null);
        
        getGlobalStrings();
        
        //ilk basta hemen logon olmamasi icin
        getGameHelper().setMaxAutoSignInAttempts(0);
    }

    private void getGlobalStrings() {
		Globals.best = this.getResources().getString(R.string.best);
		Globals.currentScore = this.getResources().getString(R.string.current_score);
	}
    
    private void putAdv() {
		adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		.addTestDevice("D40614808A0D116B440AFCD00348185F")
        .build();
        
        // Start loading the ad in the background.
        adView.loadAd(adRequest);
	}

	private void loadInterstitialAd() {
    	// Create the interstitial.
    	interstitial = new InterstitialAd(this);
    	interstitial.setAdUnitId("ca-app-pub-5819132225601729/2290136296");

    	// Create ad request.
    	adRequest = new AdRequest.Builder().build();

    	// Set an AdListener.
        interstitial.setAdListener(new AdListener() {
        	@Override
        	public void onAdLoaded(){
        		super.onAdLoaded();
        		Log.d(TAG, "interstitial loaded");
        	}
        	
        	@Override
        	public void onAdClosed() {
        		// TODO Auto-generated method stub
        		super.onAdClosed();
        		interstitial.loadAd(adRequest);
        	}
		});
    	
    	// Begin loading your interstitial.
    	interstitial.loadAd(adRequest);	 
    }
    
    public void displayInterstitial() {
    	if (interstitial.isLoaded()) {
    		interstitial.show();
    	}
    }

    // Switch UI to the given fragment
    void switchToFragment(Fragment newFrag, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newFrag, tag)
                .commit();
    }

    @Override
    public void onShowAchievementsRequested() {
        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),
                    RC_UNUSED);
        } else {
            showAlert(getString(R.string.achievements_not_available));
        }
    }

    @Override
    public void onShowLeaderboardsRequested() {
        if (isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()),
                    RC_UNUSED);
        } else {
            showAlert(getString(R.string.leaderboards_not_available));
        }
    }

    /**
     * Start gameplay. This means updating some status variables and switching
     * to the "gameplay" screen (the screen where the user types the score they want).
     *
     * @param hardMode whether to start gameplay in "hard mode".
     */
    void startGame() {
        switchToFragment(mMainGameFragment, "mMainGameFragment");
    }

    /**
     * Check for achievements and unlock the appropriate ones.
     *
     * @param requestedScore the score the user requested.
     */
    void checkForAchievements(int finalScore) {
        // Check if each condition is met; if so, unlock the corresponding
        // achievement.
        if (finalScore >= 15) {
            mOutbox.mBeginnerAchievement = true;
            achievementToast(getString(R.string.achievement_beginner_toast_text));
        }
        if (finalScore >= 30) {
            mOutbox.mIntermediateAchievement = true;
            achievementToast(getString(R.string.achievement_intermediate_toast_text));
        }
        if (finalScore >= 60) {
            mOutbox.mExpertAchievement = true;
            achievementToast(getString(R.string.achievement_expert_toast_text));
        }
        if (finalScore >= 120) {
            mOutbox.mHeroicAchievement = true;
            achievementToast(getString(R.string.achievement_heroic_toast_text));
        }
        if (finalScore >= 240) {
            mOutbox.mLegendaryAchievement = true;
            achievementToast(getString(R.string.achievement_legendary_toast_text));
        }

    }

    void unlockAchievement(int achievementId, String fallbackString) {
        if (isSignedIn()) {
            Games.Achievements.unlock(getApiClient(), getString(achievementId));
        } else {
            Toast.makeText(this, getString(R.string.achievement) + ": " + fallbackString,
                    Toast.LENGTH_LONG).show();
        }
    }

    void achievementToast(String achievement) {
        // Only show toast if not signed in. If signed in, the standard Google Play
        // toasts will appear, so we don't need to show our own.
        if (!isSignedIn()) {
            Toast.makeText(this, getString(R.string.achievement) + ": " + achievement,
                    Toast.LENGTH_LONG).show();
        }
    }

    void pushAccomplishments() {
    	if (!isSignedIn()) {
    		// can't push to the cloud, so save locally
    		mOutbox.saveLocal(this);
    		return;
    	}

    	if (mOutbox.mBeginnerAchievement) {
    		Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_beginner));
    		mOutbox.mBeginnerAchievement = false;
    	}
    	if (mOutbox.mIntermediateAchievement) {
    		Games.Achievements.increment(getApiClient(), getString(R.string.achievement_intermediate), 3);
    		mOutbox.mIntermediateAchievement = false;
    	}
    	if (mOutbox.mExpertAchievement) {
    		Games.Achievements.increment(getApiClient(), getString(R.string.achievement_expert), 6);
    		mOutbox.mExpertAchievement = false;
    	}
    	if (mOutbox.mHeroicAchievement) {
    		Games.Achievements.increment(getApiClient(), getString(R.string.achievement_heroic), 12);
    		mOutbox.mHeroicAchievement = false;
    	}
    	if (mOutbox.mLegendaryAchievement) {
    		Games.Achievements.increment(getApiClient(), getString(R.string.achievement_legendary), 24);
    		mOutbox.mLegendaryAchievement = false;
    	}
    	if (mOutbox.mFinalScore >= 0) {
    		Games.Leaderboards.submitScore(getApiClient(), getString(R.string.leaderboard_leaderboard),
    				mOutbox.mFinalScore);
    		mOutbox.mFinalScore = -1;
    	}

    	mOutbox.saveLocal(this);
    }

    /**
     * Update leaderboards with the user's score.
     *
     * @param finalScore The score the user got.
     */
    void updateLeaderboards(int finalScore) {
        if (mOutbox.mFinalScore < finalScore) {
            mOutbox.mFinalScore = finalScore;
        }
    }

    @Override
    public void onWinScreenDismissed() {
        switchToFragment(mGameMenuFragment, "mGameMenuFragment");
    }

    @Override
    public void onSignInFailed() {
        // Sign-in failed, so show sign-in button on main menu
        //mGameMenuFragment.setGreeting(getString(R.string.signed_out_greeting));
        mGameMenuFragment.setShowSignInButton(true);
        mGameOverFragment.setShowSignInButton(true);
        mGameMenuFragment.updateUi();
    }

    @Override
    public void onSignInSucceeded() {
        // Show sign-out button on main menu
        mGameMenuFragment.setShowSignInButton(false);

        // Show "you are signed in" message on win screen, with no sign in button.
        mGameOverFragment.setShowSignInButton(false);

        mGameMenuFragment.updateUi();
        
        // if we have accomplishments to push, push them
        if (!mOutbox.isEmpty()) {
            pushAccomplishments();
            Toast.makeText(this, getString(R.string.your_progress_will_be_uploaded),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSignInButtonClicked() {
        // start the sign-in flow
    	mGameMenuFragment.setShowSignInButton(true);
        mGameOverFragment.setShowSignInButton(true);
        mGameMenuFragment.updateUi();
        beginUserInitiatedSignIn();
    }

    @Override
    public void onSignOutButtonClicked() {
        signOut();
        mGameMenuFragment.setShowSignInButton(true);
        mGameOverFragment.setShowSignInButton(true);
        mGameMenuFragment.updateUi();
    }

    class AccomplishmentsOutbox {
        boolean mBeginnerAchievement = false;
        boolean mIntermediateAchievement = false;
        boolean mExpertAchievement = false;
        boolean mHeroicAchievement = false;
        boolean mLegendaryAchievement = false;

        int mFinalScore = -1;

        boolean isEmpty() {
            return !mBeginnerAchievement && !mIntermediateAchievement && !mExpertAchievement &&
                    !mHeroicAchievement && !mLegendaryAchievement && mFinalScore < 0;
        }

        public void saveLocal(Context ctx) {
            /* TODO: This is left as an exercise. To make it more difficult to cheat,
             * this data should be stored in an encrypted file! And remember not to
             * expose your encryption key (obfuscate it by building it from bits and
             * pieces and/or XORing with another string, for instance). */
    		//set the best point 
/*    		bestPoint = prefs.getInt(pointKey, 0);
    		if (mFinalScore > bestPoint)*/
    		prefs.edit().putInt(pointKey, mFinalScore).commit();
    		
    		/*prefs.edit().putBoolean(accBeginner, mBeginnerAchievement);
    		prefs.edit().putBoolean(accIntermediate, mIntermediateAchievement);
    		prefs.edit().putBoolean(accExpert, mExpertAchievement);
    		prefs.edit().putBoolean(accHeroic, mHeroicAchievement);
    		prefs.edit().putBoolean(accLegendary, mLegendaryAchievement);*/
        }

        public void loadLocal(Context ctx) {
            /* TODO: This is left as an exercise. Write code here that loads data
             * from the file you wrote in saveLocal(). */
        	
        	mFinalScore = prefs.getInt(pointKey, 0);
        	
        	mBeginnerAchievement = false;
        	mIntermediateAchievement = false;
        	mExpertAchievement = false;
        	mHeroicAchievement = false;
        	mLegendaryAchievement = false;
        	
        	if (mFinalScore >= 15)
        		mBeginnerAchievement = true;
        	if (mFinalScore >= 30)
        		mIntermediateAchievement = true;
        	if (mFinalScore >= 60)
        		mExpertAchievement = true;
        	if (mFinalScore >= 120)
        		mHeroicAchievement = true;
        	if (mFinalScore >= 240)
        		mLegendaryAchievement = true;
        	
        	/*mBeginnerAchievement = prefs.getBoolean(accBeginner, false);
        	mIntermediateAchievement = prefs.getBoolean(accIntermediate, false);
        	mExpertAchievement = prefs.getBoolean(accExpert, false);
        	mHeroicAchievement = prefs.getBoolean(accHeroic, false);
        	mLegendaryAchievement = prefs.getBoolean(accLegendary, false);*/
        }
    }

    @Override
    public void onWinScreenSignInClicked() {
    	mGameMenuFragment.setShowSignInButton(true);
        mGameOverFragment.setShowSignInButton(true);
        mGameMenuFragment.updateUi();
        beginUserInitiatedSignIn();
    }

	@Override
	public void onWinStartOverClicked() {
		switchToFragment(mMainGameFragment, "mMainGameFragment");
		interAdCounter();
	}

	private void interAdCounter() {
		int startNumberForAds = prefs.getInt(interAdKey, 0);
		Log.d(TAG, "startNumberForAds: " + String.valueOf(startNumberForAds));
		
		if (startNumberForAds >= Globals.maxStartNumberForAds){
			prefs.edit().putInt(interAdKey, 0).commit();
			displayInterstitial();
		}
		else{
			prefs.edit().putInt(interAdKey, startNumberForAds + 1).commit();
		}
	}

	@Override
	public void onStartGameRequested() {
		switchToFragment(mMainGameFragment, "mMainGameFragment");
		interAdCounter();
	}

	@Override
	public void onGameOver() {
		
        // check for achievements
        checkForAchievements(Globals.points);

        // update leaderboards
        updateLeaderboards(Globals.points);

        // push those accomplishments to the cloud, if signed in
        pushAccomplishments();

        // switch to the exciting "you won" screen
        switchToFragment(mGameOverFragment, "mGameOverFragment");
	}

	@Override
	public void onShowRateAppRequested() {
		this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Globals.packageName)));
	}
	
	@Override
	public void onBackPressed(){
		Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
		if (f instanceof GameMenuFragment) {
		    super.onBackPressed();
		    finish();
		    System.exit(0);
		}else{
			switchToFragment(mGameMenuFragment, "mGameMenuFragment");
		}
	}
	
	@Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass   
        if (adView != null){
        	adView.destroy();
        }
        android.os.Debug.stopMethodTracing();
        Log.d(TAG, "onDestroy");
    }
	
    @Override
	public void onPause() {
    	Log.d(TAG, "onPause");
        if (adView != null){
        	adView.pause();
        	//pauseWebView(adView);
        }
        WebView wv = new WebView(this);
        wv.pauseTimers();
        wv = null;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (adView != null){
        	adView.resume();
        	//resumeWebView(adView);
        }
        WebView wv = new WebView(this);
        wv.resumeTimers();
        wv = null;
        Log.d(TAG, "onResume");
    }

    public void pauseWebView(ViewGroup v) {
		for (int i = 0; i < v.getChildCount(); i++) {
			View child = v.getChildAt(i);
			if (child instanceof WebView) {
				((WebView)child).pauseTimers();
			}
			else if (child instanceof ViewGroup) {
				pauseWebView((ViewGroup) child);
			}
		}
	}

	public void resumeWebView(ViewGroup v) {
		for (int i = 0; i < v.getChildCount(); i++) {
			View child = v.getChildAt(i);
			if (child instanceof WebView) {
				((WebView)child).resumeTimers();
			}
			else if (child instanceof ViewGroup) {
				resumeWebView((ViewGroup)child);
			}
		}
	}
}
