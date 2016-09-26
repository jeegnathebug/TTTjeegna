package com.jeegnathebug.tttjeegna;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private static final String IS_END = "isEnd";

    public static final String TIC_TAC_TOE = "tictactoe";
    public static final String COUNTER_PLAYER1_WINS = "counterPlayer1Wins";
    public static final String COUNTER_PLAYER2_WINS = "counterPlayer2Wins";
    public static final String COUNTER_COMPUTER_WINS = "counterComputerWins";
    public static final String COUNTER_TIES = "counterTies";

    private int moveCounter = 0;
    private boolean isEnd = false;

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
            tictactoe.setComputerScore(settings.getInt(COUNTER_COMPUTER_WINS, 0));
            tictactoe.setTies(settings.getInt(COUNTER_TIES, 0));
        }

        // Display data
        String gameMode = getString(R.string.PvP);
        if (tictactoe.getGameMode().equals(GameMode.PvE)) {
            gameMode = getString(R.string.PvE);
        }
        ((TextView) findViewById(R.id.textViewVersus)).setText(gameMode);
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
        // Set end boolean
        isEnd = savedInstanceState.getBoolean(IS_END);

        // Get board
        int[] board = savedInstanceState.getIntArray(GAME_BOARD);
        // Set board
        tictactoe.setBoard(board);

        // Set markers on board
        ImageButton[] buttons = getButtons();
        for (int i = 0; i < board.length; i++) {
            Drawable marker = null;
            switch (board[i]) {
                case 1:
                    marker = getDrawable(R.drawable.x);
                    moveCounter++;
                    break;
                case 2:
                    marker = getDrawable(R.drawable.o);
                    moveCounter++;
                    break;
            }

            // Set image
            buttons[i].setImageDrawable(marker);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save state information
        savedInstanceState.putBoolean(PLAYER1_TURN, tictactoe.getPlayer1Turn());
        savedInstanceState.putInt(GAME_MODE, tictactoe.getGameMode().getValue());
        savedInstanceState.putIntArray(GAME_BOARD, tictactoe.getBoard());
        savedInstanceState.putBoolean(IS_END, isEnd);

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
        editor.putInt(COUNTER_COMPUTER_WINS, tictactoe.getComputerScore());
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
        tictactoe.changeGameMode();

        String gameMode = getString(R.string.PvP);
        if (tictactoe.getGameMode().equals(GameMode.PvE)) {
            gameMode = getString(R.string.PvE);
        }
        // Update text view
        ((TextView) findViewById(R.id.textViewVersus)).setText(gameMode);

        // Restart the game
        restartGame(v);
    }

    /**
     * Plays the given button, if possible
     *
     * @param button The {@code Button} that was clicked
     */
    public void click(ImageButton button) {

        // Just an aesthetics thing. The buttons change height otherwise
        setHeights();

        // Get position of button in array
        int position = getPosition(button);

        // Play the position
        play(position);
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
        isEnd = false;
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
    private int computerChoice() {
        int[] board = tictactoe.getBoard();
        Random random = new Random();
        int choice;

        do {
            choice = random.nextInt(9);
        } while (!tictactoe.isPlayable(choice));

        Log.i("Computer choice", choice + "");
        return choice;
    }

    /**
     * Displays a {@code Toast} or alert box with an appropriate message, and updates the score
     *
     * @param winner The winner of the game, where 0 = tie game, 1 = player 1, 2 = player 2, and
     *               3 = computer.
     */
    private void displayWin(int winner) {
        switch (winner) {
            case 0:
                Toast.makeText(this, getString(R.string.winTie), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.winPlayer1))
                        .setNegativeButton(R.string.button_OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                break;
            case 2:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.winPlayer2))
                        .setNegativeButton(R.string.button_OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                break;
            case 3:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.winComputer))
                        .setNegativeButton(R.string.button_OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                break;
        }

        Log.i("Winner", winner + "");
        Log.i("Computer wins", tictactoe.getComputerScore() + "");
        tictactoe.updateScore(winner);
    }

    /**
     * Ends the game
     */
    private void endGame() {
        ImageButton[] buttons = getButtons();
        for (int i = 0; i < buttons.length; i++) {
            // Disable buttons
            buttons[i].setEnabled(false);
        }

        isEnd = true;
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
     * Plays the given position
     *
     * @param position The position to be played
     */
    private void play(int position) {
        // Play position if it has not yet been set
        if (tictactoe.isPlayable(position)) {

            // Add move
            moveCounter++;

            // Play position
            tictactoe.play(position);

            // Get player markers
            Drawable marker;
            int player;
            if (tictactoe.getPlayer1Turn()) {
                player = 1;
                marker = getDrawable(R.drawable.x);
            } else {
                marker = getDrawable(R.drawable.o);
                if (tictactoe.getGameMode().equals(GameMode.PvP)) {
                    player = 2;
                } else {
                    player = 3;
                }
            }

            // Set marker
            getButtons()[position].setImageDrawable(marker);

            // Check win/tie
            if (tictactoe.checkWin()) {
                displayWin(player);
                endGame();
            } else if (moveCounter == 9) { // Tie game
                displayWin(0);
                endGame();
            }

            // Change turn
            tictactoe.changeTurn();

            // Computer turn
            if (!isEnd && !tictactoe.getPlayer1Turn() && tictactoe.getGameMode().equals(GameMode.PvE)) {
                play(computerChoice());
            }
        }
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
}
