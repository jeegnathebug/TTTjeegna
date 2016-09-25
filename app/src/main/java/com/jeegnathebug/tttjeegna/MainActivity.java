package com.jeegnathebug.tttjeegna;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.jeegnathebug.tttjeegna.business.GameMode;
import com.jeegnathebug.tttjeegna.business.TicTacToe;

import java.util.Random;

/**
 * The main class
 */
public class MainActivity extends Activity {

    private TicTacToe tictactoe = new TicTacToe(GameMode.PvE);

    static final String PREFS_NAME = "Preferences";

    private static final String PLAYER1_TURN = "player1Turn";
    private static final String GAME_MODE = "gameMode";
    private static final String GAME_BOARD = "gameBoard";

    public static final String TIC_TAC_TOE = "tictactoe";
    public static final String COUNTER_PLAYER1_WINS = "counterPlayer1Wins";
    public static final String COUNTER_PLAYER2_WINS = "counterPlayer2Wins";
    public static final String COUNTER_TIES = "counterTies";

    private int moveCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);

        // Set button events
        setButtons();

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            onRestoreInstanceState(savedInstanceState);
        } else {
            // Get scores from shared preferences
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
        int id = item.getItemId();

        if (id == R.id.action_about) {
            about(null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Set turn
        tictactoe.setPlayer1Turn(savedInstanceState.getBoolean(PLAYER1_TURN));
        // Set gamemode
        tictactoe.setGameMode(GameMode.fromInt(savedInstanceState.getInt(GAME_MODE)));

        // Get board
        int[] board = savedInstanceState.getIntArray(GAME_BOARD);
        // Set board
        tictactoe.setBoard(board);

        // Set markers on board
        ImageButton[] buttons = getButtons();
        for (int i = 0; i < board.length; i++) {
            Drawable marker = null;
            if (board[i] == 1) {
                marker = getDrawable(R.drawable.x);
                moveCounter++;
            } else if (board[i] == 2) {
                marker = getDrawable(R.drawable.o);
                moveCounter++;
            }
            buttons[i].setImageDrawable(marker);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save state information
        savedInstanceState.putBoolean(PLAYER1_TURN, tictactoe.getPlayer1Turn());
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
     * @param v The {@code View}
     */
    public void about(View v) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    /**
     * Changes the game mode
     *
     * @param v The {@code View}
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
     * Plays the given button, if possible
     *
     * @param button The {@code Button} that was clicked
     */
    public void click(ImageButton button) {

        // FIXME on rotate
        // Just an aesthetics thing. The buttons change height if this isn't done
        setHeights();

        // Get position of button in array
        int position = getPosition(button);

        // Play position if it has not yet been set
        if (tictactoe.isPlayable(position)) {

            // Add move
            moveCounter++;

            // Play position
            tictactoe.play(position);

            // Get player marker
            Drawable marker = tictactoe.getPlayer1Turn() ? getDrawable(R.drawable.x) : getDrawable(R.drawable.o);
            // Set marker
            button.setImageDrawable(marker);

            // Check win or change turn
            if (tictactoe.checkWin()) {
                int winner = tictactoe.getPlayer1Turn() ? 1 : 2;
                displayWin(winner);
                endGame(winner);
            } else {
                tictactoe.changeTurn();
            }

            // Tie game
            if (moveCounter == 9) {
                displayWin(0);
                endGame(0);
            }

            // Computer turn
            if (!tictactoe.getPlayer1Turn() && tictactoe.getGameMode().equals(GameMode.PvE)) {
                click(computerChoice());
            }
        }
    }

    /**
     * FIXME
     */
    private void setHeights() {
        ImageButton[] buttons = getButtons();
        int height = (((TableLayout) findViewById(R.id.tableLayout)).getHeight()) / 3;

        for (ImageButton button : buttons) {
            ViewGroup.LayoutParams params = button.getLayoutParams();
            params.height = height;
            params.width = 0;
            button.setLayoutParams(params);
        }
    }

    /**
     * Restarts the game
     *
     * @param v The {@code View}
     */
    public void restartGame(View v) {
        ImageButton[] buttons = getButtons();
        for (int i = 0; i < buttons.length; i++) {
            // Enable buttons
            buttons[i].setEnabled(true);
            // Reset button images
            buttons[i].setImageDrawable(null);
        }

        moveCounter = 0;
        tictactoe.restartGame();
    }

    /**
     * Launches the score activity
     *
     * @param v The {@code View}
     */
    public void scores(View v) {
        Intent intent = new Intent(this, ScoreActivity.class);

        // Add TicTacToe class to Scores
        intent.putExtra(TIC_TAC_TOE, tictactoe);

        startActivity(intent);
    }

    /**
     * Chooses a move for the computer and plays it
     */
    private ImageButton computerChoice() {
        int[] board = tictactoe.getBoard();
        Random random = new Random();
        int choice;

        do {
            choice = random.nextInt(9);
        } while (!tictactoe.isPlayable(choice));

        Log.i("Comp choice", choice + "");
        // Pretend computer clicked the button
        // Kind of counter-productive so FIXME
        return getButtons()[choice];
    }

    /**
     * Displays a {@code Toast} with an appropriate message
     *
     * @param winner The winner of the game, where 0 = tie game, 1 = player 1, and 2 = player 2.
     */
    private void displayWin(int winner) {
        String message = "";
        switch (winner) {
            case 0:
                message = "Player 1 winner!";
                break;
            case 1:
                message = "Player 2 winner!";
                break;
            case 2:
                message = "Tie game!";
                break;
        }

        // Display toast
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Ends the game, and updates the score
     */
    private void endGame(int winner) {
        ImageButton[] buttons = getButtons();
        for (int i = 0; i < buttons.length; i++) {
            // Disable buttons
            buttons[i].setEnabled(false);
        }

        tictactoe.updateScore(winner);
    }

    /**
     * Gets all the {@code ImageButton} objects from the {@code GridLayout}
     *
     * @return An array of the {@code ImageButton}s
     */
    private ImageButton[] getButtons() {
        ImageButton buttons[] = new ImageButton[9];

        // Get layout
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);

        for (int i = 0; i < table.getChildCount(); i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                buttons[j + (3 * i)] = (ImageButton) row.getChildAt(j);
            }
        }

        return buttons;
    }

    /**
     * Gets the position of the given Button
     *
     * @param button The button whose position is to be found
     * @return The position of the given button
     */
    private int getPosition(ImageButton button) {
        ImageButton[] buttons = getButtons();
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].equals(button)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Adds event listeners to the button grid
     */
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

        // Add events
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
