package com.mahjongmanager.riichi;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mahjongmanager.riichi.handcalculator.InputHand;
import com.mahjongmanager.riichi.handcalculator.WinningTile;
import com.mahjongmanager.riichi.handcalculator.OtherInfo;
import com.mahjongmanager.riichi.handcalculator.FinalScore;
import com.mahjongmanager.riichi.simplefragments.HanFuCalculatorFragment;
import com.mahjongmanager.riichi.simplefragments.MainMenuFragment;
import com.mahjongmanager.riichi.simplefragments.OptionsFragment;
import com.mahjongmanager.riichi.simplefragments.OptionsRulesetFragment;
import com.mahjongmanager.riichi.simplefragments.ScoreTableFragment;
import com.mahjongmanager.riichi.simplefragments.ScoringFlowchartFragment;
import com.mahjongmanager.riichi.simplefragments.ScoringOverviewFragment;
import com.mahjongmanager.riichi.simplefragments.YakuListFragment;
import com.mahjongmanager.riichi.speedquiz.Start;
import com.mahjongmanager.riichi.speedquiz.ScoreHand;
import com.mahjongmanager.riichi.speedquiz.ResultsScreen;
import com.mahjongmanager.riichi.speedquiz.ReviewHand;
import com.mahjongmanager.riichi.utils.ExampleHands;
import com.mahjongmanager.riichi.utils.ImageCache;
import com.mahjongmanager.riichi.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // When a hand needs to be passed along between fragments (such as the HandCalculator)
    public Hand currentHand = new Hand(new ArrayList<Tile>());

    // Speed Quiz - Record the hands/guesses so they can be reviewed at the end
    private List<Hand> scoredHands = new ArrayList<>();
    private List<int[]> hanFuGuesses = new ArrayList<>();
    private int currentHanGuess = 0;
    private int currentFuGuess  = 0;
    private CountDownTimer speedQuizTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            MainMenuFragment newFragment = new MainMenuFragment();
            //Bundle args = new Bundle();
            //args.putInt(MainMenuFragment.ARG_POSITION, position);
            //newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, newFragment);
            //transaction.addToBackStack(null);

            transaction.commit();
        }

        getExampleHands();
        getImageCache();
    }

    ///////////////////////////////////////////////////
    /////////////    Navigating UIs     ///////////////
    ///////////////////////////////////////////////////
    public void goToHanFuCalculator(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new HanFuCalculatorFragment());
        //transaction.addToBackStack("MainMenu");
        transaction.commit();
    }

    public void gotoHandCalculatorStart(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new InputHand());
        transaction.commit();
    }
    public void gotoHandCalculatorWinningTile(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new WinningTile());
        transaction.commit();
    }
    public void goToHandCalculatorOtherInfo(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new OtherInfo());
        transaction.commit();
    }
    public void goToHandCalculatorFinalScore(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new FinalScore());
        transaction.commit();
    }

    public void goToSpeedQuizStart(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new Start());
        transaction.commit();
    }
    public void goToSpeedQuizScoreHand(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ScoreHand());
        transaction.commit();

        speedQuizTimer = new CountDownTimer(90000, 100){
            @Override
            public void onTick(long millisUntilFinished){
                ScoreHand sq = (ScoreHand) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if( sq!=null ){
                    sq.updateSecondCounter( String.valueOf( (int)millisUntilFinished/1000 ));
                }
            }

            @Override
            public void onFinish(){
                goToSpeedQuizResultsScreen(getCurrentFocus());
            }
        }.start();
    }
    public void goToSpeedQuizScoreHandNext(View view){
        scoredHands.add(currentHand);
        int[] guess = new int[2];
        guess[0] = currentHanGuess;
        guess[1] = currentFuGuess;
        hanFuGuesses.add(guess);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ScoreHand());
        transaction.commit();
    }
    public void goToSpeedQuizResultsScreen(View view){
        if( speedQuizTimer!=null ){
            speedQuizTimer.cancel();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ResultsScreen());
        transaction.commit();
    }
    public void goToSpeedQuizReviewHand(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ReviewHand());
        transaction.commit();
    }

    public void goToScoreTable(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ScoreTableFragment());
        transaction.commit();
    }

    public void goToYakuList(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new YakuListFragment());
        transaction.commit();
    }

    public void goToScoringOverview(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ScoringOverviewFragment());
        transaction.commit();
    }

    public void goToScoringFlowchart(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ScoringFlowchartFragment());
        transaction.commit();
    }

    public void goToOptions(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new OptionsFragment());
        transaction.commit();
    }
    public void goToOptionsRuleset(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new OptionsRulesetFragment());
        transaction.commit();
    }

    public void backToMainMenu(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new MainMenuFragment());

        currentHanGuess = 0;
        currentFuGuess = 0;
        currentHand = new Hand(new ArrayList<Tile>());
        scoredHands = new ArrayList<>();
        hanFuGuesses = new ArrayList<>();
        transaction.commit();
    }

    /////////////////////////////////////////////////////////
    /////////    Cross-Fragment Communication     ///////////
    /////////////////////////////////////////////////////////
    public Hand getCurrentHand(){ return currentHand; }
    public void setCurrentHand(Hand h){ currentHand = h; }
    public int getCurrentHanGuess(){ return currentHanGuess; }
    public void setCurrentHanGuess(int i){ currentHanGuess = i; }
    public int getCurrentFuGuess(){ return currentFuGuess; }
    public void setCurrentFuGuess(int i){ currentFuGuess = i; }

    public List<Hand> getScoredHands(){ return scoredHands; }
    public List<int[]> getHanFuGuesses(){ return hanFuGuesses; }

    public CountDownTimer getSpeedQuizTimer(){ return speedQuizTimer; }

    //////////////////////////////////////////
    /////////////    Utils     ///////////////
    //////////////////////////////////////////
    private Utils _utils;
    public Utils getUtils(){
        if(_utils==null){
            _utils = new Utils(this);
        }
        return _utils;
    }
    private ExampleHands _exampleHands;
    public ExampleHands getExampleHands(){
        if(_exampleHands==null){
            _exampleHands = new ExampleHands(this);
        }
        return _exampleHands;
    }
    private ImageCache _imageCache;
    public ImageCache getImageCache(){
        if(_imageCache==null){
            _imageCache = new ImageCache(this);
            _imageCache.clearCache();
            getUtils().populateImageCacheForHandDisplay();
        }
        return _imageCache;
    }
}
