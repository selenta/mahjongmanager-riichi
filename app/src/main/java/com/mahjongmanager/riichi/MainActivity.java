package com.mahjongmanager.riichi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mahjongmanager.riichi.handcalculator.*;
import com.mahjongmanager.riichi.simplefragments.*;
import com.mahjongmanager.riichi.speedquiz.*;
import com.mahjongmanager.riichi.utils.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;

    // When a hand needs to be passed along between fragments (such as the HandCalculator)
    public Hand currentHand = new Hand(new ArrayList<Tile>());

    // Speed Quiz - Record the hands/guesses so they can be reviewed at the end
    private List<Hand> scoredHands = new ArrayList<>();
    private List<int[]> hanFuGuesses = new ArrayList<>();
    private int currentHanGuess = 0;
    private int currentFuGuess  = 0;
    private CountDownTimer speedQuizTimer;
    private long timerRemaining;

    private boolean ignoreBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load banner ad
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7864566452891143/4353535112");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addKeyword( "mahjong" )
                .addKeyword( "Mahjong" )
                .build();
        mAdView.loadAd(adRequest);

        // Load Main Menu
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            replaceFragment(new MainMenuFragment(), false);
        }

        getExampleHands();
        getImageCache();
    }

    @Override
    public void onBackPressed(){
        if( !ignoreBack ){
            super.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        if (speedQuizTimer!=null){
            speedQuizTimer.cancel();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
//        if( timerRemaining!=0){
//            createSpeedQuizTimer(timerRemaining);
//        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        if (speedQuizTimer!=null){
            speedQuizTimer.cancel();
            timerRemaining = 0;
        }
        super.onDestroy();
    }

    ///////////////////////////////////////////////////
    /////////////    Navigating UIs     ///////////////
    ///////////////////////////////////////////////////
    public void gotoHandCalculatorStart(View view){
        replaceFragment(new InputHand());
    }
    public void gotoHandCalculatorWinningTile(View view){
        replaceFragment(new WinningTile());
    }
    public void goToHandCalculatorOtherInfo(View view){
        replaceFragment(new OtherInfo());
    }
    public void goToHandCalculatorFinalScore(View view){
        replaceFragment(new FinalScore());
    }

    public void goToSpeedQuizStart(View view){
        replaceFragment(new Start());
    }
    public void goToSpeedQuizScoreHand(View view){
        ignoreBack = true;
        generateSpeedQuizHand();
        replaceFragment(new ScoreHand(), false);

        createSpeedQuizTimer(90000);
    }
    public void goToSpeedQuizScoreHandNext(View view){
        scoredHands.add(currentHand);
        int[] guess = new int[2];
        guess[0] = currentHanGuess;
        guess[1] = currentFuGuess;
        hanFuGuesses.add(guess);

        if( proceedToQuizResults() ){
            currentHand = null;
            goToSpeedQuizResultsScreen(view);
        } else {
            generateSpeedQuizHand();
            replaceFragment(new ScoreHand(), false);
        }
    }
    public void goToSpeedQuizResultsScreen(View view){
        if( speedQuizTimer!=null ){
            speedQuizTimer.cancel();
        }
        timerRemaining = 0;
        replaceFragment(new ResultsScreen(), false);
    }
    public void goToSpeedQuizReviewHand(View view){
        ignoreBack = false;
        replaceFragment(new ReviewHand());
    }

    public void goToScoreTable(View view){
        replaceFragment(new ScoreTableFragment());
    }
    public void goToHanFuCalculator(View view){
        replaceFragment(new HanFuCalculatorFragment());
    }
    public void goToYakuList(View view){
        replaceFragment(new YakuListFragment());
    }

    public void goToOverview(View view){
        replaceFragment(new OverviewFragment());
    }
    public void goToOverviewBasics(View view){
        replaceFragment(new OverviewBasicsFragment());
    }
    public void goToOverviewPlay(View view){
        replaceFragment(new OverviewPlayFragment());
    }
    public void goToOverviewScoring(View view){
        replaceFragment(new OverviewScoringFragment());
    }
    public void goToScoringFlowchart(View view){
        replaceFragment(new ScoringFlowchartFragment());
    }

    public void goToAbout(View view){
        replaceFragment(new AboutFragment());
    }
    public void goToOptions(View view){
        replaceFragment(new OptionsFragment());
    }
    public void goToOptionsRuleset(View view){
        replaceFragment(new OptionsRulesetFragment());
    }

    public void backToMainMenu(View view){
        ignoreBack = false;

        currentHanGuess = 0;
        currentFuGuess = 0;
        currentHand = new Hand(new ArrayList<Tile>());
        scoredHands = new ArrayList<>();
        hanFuGuesses = new ArrayList<>();

        replaceFragment(new MainMenuFragment(), false);
        clearBackStack();
    }

    private void replaceFragment(Fragment fragment){
        replaceFragment(fragment, true);
    }
    private void replaceFragment(Fragment fragment, boolean addToBackstack){
        String backStateName = fragment.getClass().getName();
        Log.d("replaceFragment", "backStateName:  "+ backStateName);

        FragmentManager manager = getSupportFragmentManager();
        Log.d("replaceFragment", "backStackCount: "+ manager.getBackStackEntryCount());
        for( int i=0; i<manager.getBackStackEntryCount(); i++ ){
            Log.d("replaceFragment", "backStack["+i+"]: "+ manager.getBackStackEntryAt(i).getName());
        }

        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);
        Log.d("replaceFragment", "fragmentPopped: "+ fragmentPopped);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            if(addToBackstack){
                ft.addToBackStack(backStateName);
            }
            ft.commit();
        }
    }
    private void clearBackStack(){
        FragmentManager manager = getSupportFragmentManager();
        if( manager.getBackStackEntryCount() > 0 ){
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    /////////////////////////////////////////////////////////
    /////////    Cross-Fragment Communication     ///////////
    /////////////////////////////////////////////////////////
    public Hand getCurrentHand(){ return currentHand; }
    public void setCurrentHand(Hand h){ currentHand = h; }
    public int  getCurrentHanGuess(){ return currentHanGuess; }
    public void setCurrentHanGuess(int i){ currentHanGuess = i; }
    public int  getCurrentFuGuess(){ return currentFuGuess; }
    public void setCurrentFuGuess(int i){ currentFuGuess = i; }

    public List<Hand> getScoredHands(){ return scoredHands; }
    public List<int[]> getHanFuGuesses(){ return hanFuGuesses; }

    public CountDownTimer getSpeedQuizTimer(){ return speedQuizTimer; }
    public void generateSpeedQuizHand() {
        String SQ_RANDOM_WINDS = "SQRandomWinds";
        String SQ_SITUATIONAL_YAKU = "SQSituationalYaku";
        String SQ_NUMBER_OF_SUITS = "SQNumberOfSuits";
        String SQ_ALLOW_HONORS = "SQAllowHonors";
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        Boolean randomWinds = sharedPref.getBoolean(SQ_RANDOM_WINDS, false);
        Boolean situationalYaku = sharedPref.getBoolean(SQ_SITUATIONAL_YAKU, false);
        Integer numberOfSuits = sharedPref.getInt(SQ_NUMBER_OF_SUITS, 3);
        Boolean allowHonors = sharedPref.getBoolean(SQ_ALLOW_HONORS, true);

        Hand h = new Hand(new ArrayList<Tile>());

        while (h.han == 0) {
            HandGenerator hg = new HandGenerator();
            hg.setNumberOfSuits(numberOfSuits);
            hg.setAllowHonors(allowHonors);

            Hand hTemp = hg.completelyRandomHand();

            if (situationalYaku) {
                hg.addOtherYaku(hTemp);
            }

            if (randomWinds) {
                hTemp.prevailingWind = Utils.getRandomWind();
                hTemp.playerWind = Utils.getRandomWind();
            }

            ScoreCalculator sc = new ScoreCalculator(hTemp);
            if (sc.validatedHand != null) {
                h = sc.validatedHand;
            }
        }
        currentHand = h;
    }

    public void setIgnoreBack(boolean newState){
        ignoreBack = newState;
    }

    //////////////////////////////////////////
    /////////////    Utils     ///////////////
    //////////////////////////////////////////
    private void createSpeedQuizTimer(long durationMillis){
        speedQuizTimer = new CountDownTimer(durationMillis, 100){
            @Override
            public void onTick(long millisUntilFinished){
                Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if( frag!=null && frag.getClass()==ScoreHand.class ){
                    ((ScoreHand)frag).updateSecondCounter( String.valueOf( (int)millisUntilFinished/1000 ));
                }
                timerRemaining = millisUntilFinished;
            }
            @Override
            public void onFinish(){
                goToSpeedQuizResultsScreen(getCurrentFocus());
            }
        }.start();
    }

    private boolean proceedToQuizResults(){
        String SQ_MAX_HANDS = "SQMaxHands";
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        Integer maxHands = sharedPref.getInt(SQ_MAX_HANDS, 10);
        return getScoredHands().size() >= maxHands;
    }

    private Utils _utils;
    public  Utils getUtils(){
        if(_utils==null){
            _utils = new Utils(this);
        }
        return _utils;
    }
    private ExampleHands _exampleHands;
    public  ExampleHands getExampleHands(){
        if(_exampleHands==null){
            _exampleHands = new ExampleHands(this);
        }
        return _exampleHands;
    }
    private ImageCache _imageCache;
    public  ImageCache getImageCache(){
        if(_imageCache==null){
            _imageCache = new ImageCache(this);
            _imageCache.clearCache();
            getUtils().populateImageCacheForHandDisplay();
        }
        return _imageCache;
    }
}
