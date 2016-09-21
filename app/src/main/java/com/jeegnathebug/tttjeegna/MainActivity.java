package com.jeegnathebug.tttjeegna;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import com.jeegnathebug.tttjeegna.business.GameMode;
import com.jeegnathebug.tttjeegna.business.TicTacToe;

public class MainActivity extends AppCompatActivity {

    private TicTacToe tictactoe = new TicTacToe(GameMode.PvE);

    private static final String PREFS_NAME = "Preferences";

    private static final String GAME_MODE = "gameMode";
    private static final String GAME_BOARD = "gameBoard";

    public static final String COUNTER_PLAYER1_WINS = "counterPlayer1Wins";
    public static final String COUNTER_PLAYER2_WINS = "counterPlayer2Wins";
    public static final String COUNTER_TIES = "counterTies";

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set button events
        setButtons();

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            onRestoreInstanceState(savedInstanceState);
        } else {
            // Get shared preferences
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            tictactoe.setPlayer1Score(settings.getInt(COUNTER_PLAYER1_WINS, 0));
            tictactoe.setPlayer2Score(settings.getInt(COUNTER_PLAYER2_WINS, 0));
            tictactoe.setTies(settings.getInt(COUNTER_TIES, 0));
        }

        // Display data
        ((TextView) findViewById(R.id.textView1)).setText(tictactoe.getGameMode().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        tictactoe.setPlayer1Score(savedInstanceState.getInt(COUNTER_PLAYER1_WINS));
        tictactoe.setPlayer2Score(savedInstanceState.getInt(COUNTER_PLAYER2_WINS));
        tictactoe.setTies(savedInstanceState.getInt(COUNTER_TIES));
        tictactoe.setGameMode(GameMode.fromInt(savedInstanceState.getInt(GAME_MODE)));
        tictactoe.setBoard(savedInstanceState.getIntArray(GAME_BOARD));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // save state information with a collection of key-value pairs
        savedInstanceState.putInt(COUNTER_PLAYER1_WINS, tictactoe.getPlayer1Score());
        savedInstanceState.putInt(COUNTER_PLAYER2_WINS, tictactoe.getPlayer2Score());
        savedInstanceState.putInt(COUNTER_TIES, tictactoe.getTies());
        savedInstanceState.putInt(GAME_MODE, tictactoe.getGameMode().getValue());
        savedInstanceState.putIntArray(GAME_BOARD, tictactoe.getBoard());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Set shared preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt(COUNTER_PLAYER1_WINS, tictactoe.getPlayer1Score());
        editor.putInt(COUNTER_PLAYER2_WINS, tictactoe.getPlayer2Score());
        editor.putInt(COUNTER_TIES, tictactoe.getTies());

        editor.apply();
    }

    /**
     * Launches the about activity
     *
     * @param v
     */
    public void about(View v) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    /**
     * Changes the game mode
     *
     * @param v
     */
    public void changeGameMode(View v) {
        // Get current gamemode
        GameMode mode = tictactoe.getGameMode();
        // Change it
        if (mode.equals(GameMode.PvE)) {
            tictactoe.setGameMode(GameMode.PvP);
            // Update text view
            ((TextView) findViewById(R.id.textView1)).setText(getString(R.string.PvP));
        } else {
            tictactoe.setGameMode(GameMode.PvE);
            // Update text view
            ((TextView) findViewById(R.id.textView1)).setText(getString(R.string.PvE));
        }

        // Restart the game
        restartGame(v);
    }

    /**
     * @param v
     */
    public void click(ImageButton button) {
        int height = (((TableLayout) findViewById(R.id.tableLayout)).getHeight()) / 3;

        if (tictactoe.getPlayer1Turn()) {
            tictactoe.play(0);
            button.setImageDrawable(getDrawable(R.drawable.x));

            ViewGroup.LayoutParams params = button.getLayoutParams();
            params.height = height;
            params.width = 0;
            button.setLayoutParams(params);
        } else {
            tictactoe.play(0);
            button.setImageDrawable(getDrawable(R.drawable.o));

            ViewGroup.LayoutParams params = button.getLayoutParams();
            params.height = height;
            params.width = 0;
            button.setLayoutParams(params);
        }
    }

    /**
     * Resets the scores
     *
     * @param v
     */
    public void resetScores(View v) {
        tictactoe.resetScores();
    }

    /**
     * Restarts the game
     *
     * @param v
     */
    public void restartGame(View v) {
        // TODO reset buttons
        tictactoe.restartGame();
    }

    /**
     * Launches the score activity
     *
     * @param v
     */
    public void scores(View v) {
        Intent intent = new Intent(this, ScoreActivity.class);

        // Add TicTacToe class to Scores
        intent.putExtra(COUNTER_PLAYER1_WINS, tictactoe.getPlayer1Score());
        intent.putExtra(COUNTER_PLAYER2_WINS, tictactoe.getPlayer2Score());
        intent.putExtra(COUNTER_TIES, tictactoe.getTies());

        startActivity(intent);
    }

    /**
     *
     */
    private void computerChoice() {

    }

    private void setButtons() {
        final ImageButton button1 = (ImageButton) findViewById(R.id.imageButton1);
        final ImageButton button2 = (ImageButton) findViewById(R.id.imageButton2);
        final ImageButton button3 = (ImageButton) findViewById(R.id.imageButton3);
        final ImageButton button4 = (ImageButton) findViewById(R.id.imageButton4);
        final ImageButton button5 = (ImageButton) findViewById(R.id.imageButton5);
        final ImageButton button6 = (ImageButton) findViewById(R.id.imageButton6);
        final ImageButton button7 = (ImageButton) findViewById(R.id.imageButton7);
        final ImageButton button8 = (ImageButton) findViewById(R.id.imageButton8);
        final ImageButton button9 = (ImageButton) findViewById(R.id.imageButton9);

        button1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                click(button1);
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                click(button2);
            }
        });
        button3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                click(button3);
            }
        });
        button4.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                click(button4);
            }
        });
        button5.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                click(button5);
            }
        });
        button6.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                click(button6);
            }
        });
        button7.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                click(button7);
            }
        });
        button8.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                click(button8);
            }
        });
        button9.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                click(button9);
            }
        });
    }
}
