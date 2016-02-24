package com.mahjongmanager.riichi;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public Hand currentHand = new Hand(new ArrayList<Tile>());

    public List<Hand> scoredHands = new ArrayList<>();
    public List<int[]> hanFuGuesses = new ArrayList<>();
    public int currentHanGuess = 0;
    public int currentFuGuess  = 0;

    public CountDownTimer speedQuizTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create fragment and give it an argument specifying the article it should show
            MainMenuFragment newFragment = new MainMenuFragment();
            Bundle args = new Bundle();
            //args.putInt(MainMenuFragment.ARG_POSITION, position);
            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.add(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }

    public void goToHanFuCalculator(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, new HanFuCalculatorFragment());
        //transaction.addToBackStack("MainMenu");

        transaction.commit();
    }

    public void gotoHandCalculator(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, new HandCalculatorFragment_1Keyboard());
        //transaction.addToBackStack("MainMenu");

        transaction.commit();
    }
    public void gotoHandCalculatorWinningTile(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, new HandCalculatorFragment_2WinningTile());
        //transaction.addToBackStack("MainMenu");

        transaction.commit();
    }
    public void goToHandCalculatorOtherInfo(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, new HandCalculatorFragment_3OtherInfo());
        //transaction.addToBackStack("MainMenu");

        transaction.commit();
    }
    public void goToHandCalculatorScoreScreen(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, new HandCalculatorFragment_4ScoreScreen());
        //transaction.addToBackStack("MainMenu");

        transaction.commit();
    }

    public void goToSpeedQuizStart(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, new SpeedQuizFragment_1Start());
        //transaction.addToBackStack("MainMenu");

        transaction.commit();
    }
    public void speedQuizHanFu(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, new SpeedQuizFragment_2ScoreHand());
        //transaction.addToBackStack("MainMenu");

        transaction.commit();

        speedQuizTimer = new CountDownTimer(90000, 100){
            @Override
            public void onTick(long millisUntilFinished){
                SpeedQuizFragment_2ScoreHand sq = (SpeedQuizFragment_2ScoreHand) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                //Log.v("countdownTimer", "onTick - sq: " + sq );
                if( sq!=null ){
                    sq.updateSecondCounter( String.valueOf( (int)millisUntilFinished/1000 ));
                }
            }

            @Override
            public void onFinish(){
                speedQuizScoreScreen(getCurrentFocus());
            }
        }.start();
    }
    public void speedQuizHanFuNext(View view){
        scoredHands.add(currentHand);
        int[] guess = new int[2];
        guess[0] = currentHanGuess;
        guess[1] = currentFuGuess;
        hanFuGuesses.add(guess);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, new SpeedQuizFragment_2ScoreHand());
        //transaction.addToBackStack("MainMenu");

        transaction.commit();
    }
    public void speedQuizScoreScreen(View view){
        speedQuizTimer.cancel();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, new SpeedQuizFragment_3ScoreScreen());
        //transaction.addToBackStack("MainMenu");

        transaction.commit();
    }
    public void speedQuizReviewHand(View view){
         FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, new SpeedQuizFragment_4ReviewHandHanFu());
        //transaction.addToBackStack("MainMenu");

        transaction.commit();
    }

    public void goToScoreTable(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, new ScoreTableFragment());
        //transaction.addToBackStack("MainMenu");

        transaction.commit();
    }

    public void goToYakuList(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, new YakuListFragment());
        //transaction.addToBackStack("MainMenu");

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

    public Hand getCurrentHand(){ return currentHand; }
    public void setCurrentHand(Hand h){ currentHand = h; }

    public int getCurrentHanGuess(){ return currentHanGuess; }
    public void setCurrentHanGuess(int i){ currentHanGuess = i; }
    public int getCurrentFuGuess(){ return currentFuGuess; }
    public void setCurrentFuGuess(int i){ currentFuGuess = i; }

    public List<Hand> getScoredHands(){ return scoredHands; }
    public List<int[]> getHanFuGuesses(){ return hanFuGuesses; }

    public CountDownTimer getSpeedQuizTimer(){ return speedQuizTimer; }
}
