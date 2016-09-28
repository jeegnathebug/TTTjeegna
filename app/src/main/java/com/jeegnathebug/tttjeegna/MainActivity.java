package com.jeegnathebug.tttjeegna;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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
 * The controller class for the main screen
 */
public class MainActivity extends Activity {

    private TicTacToe tictactoe;

    public static final String PREFS_NAME = "com.jeegnathebug.tttjeegna.Preferences";

    private static final String PLAYER1_TURN = "com.jeegnathebug.tttjeegna.player1Turn";
    private static final String GAME_MODE = "com.jeegnathebug.tttjeegna.gameMode";
    private static final String GAME_BOARD = "com.jeegnathebug.tttjeegna.gameBoard";
    private static final String PLAYER1_START = "com.jeegnathebug.tttjeegna.playerTurn";
    private static final String MOVE_COUNTER = "com.jeegnathebug.ttjeegna.moveCounter";
    private static final String IS_END = "com.jeegnathebug.tttjeegna.isEnd";

    public static final String TIC_TAC_TOE = "com.jeegnathebug.tttjeegna.tictactoe";
    public static final String COUNTER_PLAYER1_WINS = "com.jeegnathebug.tttjeegna.counterPlayer1Wins";
    public static final String COUNTER_PLAYER2_WINS = "com.jeegnathebug.tttjeegna.counterPlayer2Wins";
    public static final String COUNTER_COMPUTER_WINS = "com.jeegnathebug.tttjeegna.counterComputerWins";
    public static final String COUNTER_TIES = "com.jeegnathebug.tttjeegna.counterTies";

    private int moveCounter = 0;
    private boolean isPlayer1Start = true;
    private boolean isEnd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);

        // Default game
        tictactoe = new TicTacToe(GameMode.PvE);

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


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Set turn
        tictactoe.setPlayer1Turn(savedInstanceState.getBoolean(PLAYER1_TURN));
        // Set gamemode
        tictactoe.setGameMode(GameMode.fromInt(savedInstanceState.getInt(GAME_MODE)));
        // Set move counter
        moveCounter = savedInstanceState.getInt(MOVE_COUNTER);
        // Set round robin counter
        isPlayer1Start = savedInstanceState.getBoolean(PLAYER1_START);
        // Set end boolean
        isEnd = savedInstanceState.getBoolean(IS_END);
        // Set scores
        tictactoe.setPlayer1Score(savedInstanceState.getInt(COUNTER_PLAYER1_WINS));
        tictactoe.setPlayer2Score(savedInstanceState.getInt(COUNTER_PLAYER2_WINS));
        tictactoe.setComputerScore(savedInstanceState.getInt(COUNTER_COMPUTER_WINS));
        tictactoe.setTies(savedInstanceState.getInt(COUNTER_TIES));

        // Get board
        int[] board = savedInstanceState.getIntArray(GAME_BOARD);
        // Set board
        tictactoe.setBoard(board);

        // Set markers on board
        ImageButton[] buttons = getButtons();
        for (int i = 0; i < board.length; i++) {
            Drawable marker;
            switch (board[i]) {
                case 1:
                    marker = getDrawable(R.drawable.x);
                    // Set image
                    buttons[i].setImageDrawable(marker);
                    break;
                case 2:
                    marker = getDrawable(R.drawable.o);
                    // Set image
                    buttons[i].setImageDrawable(marker);
                    break;
            }

            // If game hsa ended, disable buttons
            if (isEnd) {
                buttons[i].setEnabled(false);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Player 1 turn
        savedInstanceState.putBoolean(PLAYER1_TURN, tictactoe.getPlayer1Turn());
        // Game mode
        savedInstanceState.putInt(GAME_MODE, tictactoe.getGameMode().getValue());
        // Game board
        savedInstanceState.putIntArray(GAME_BOARD, tictactoe.getBoard());
        // Moves played
        savedInstanceState.putInt(MOVE_COUNTER, moveCounter);
        // Round robin counter
        savedInstanceState.putBoolean(PLAYER1_START, isPlayer1Start);
        // End of game
        savedInstanceState.putBoolean(IS_END, isEnd);
        // Scores
        savedInstanceState.putInt(COUNTER_PLAYER1_WINS, tictactoe.getPlayer1Score());
        savedInstanceState.putInt(COUNTER_PLAYER2_WINS, tictactoe.getPlayer2Score());
        savedInstanceState.putInt(COUNTER_COMPUTER_WINS, tictactoe.getComputerScore());
        savedInstanceState.putInt(COUNTER_TIES, tictactoe.getTies());

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
        for (ImageButton button : buttons) {
            // Enable buttons
            button.setEnabled(true);
            // Reset button images
            button.setImageDrawable(getDrawable(R.drawable.blank));
        }

        moveCounter = 0;
        isEnd = false;
        tictactoe.restartGame();

        // If player 1 is not starting, change who starts
        if (tictactoe.getGameMode().equals(GameMode.PvP)) {
            // Change start turn
            if (isPlayer1Start) {
                ((TextView) findViewById(R.id.textViewPlayerTurn)).setText(getString(R.string.player1Start));
            } else {
                tictactoe.changeTurn();
                ((TextView) findViewById(R.id.textViewPlayerTurn)).setText(getString(R.string.player2Start));
            }

            isPlayer1Start = !isPlayer1Start;
        }
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

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                tictactoe = (TicTacToe) data.getExtras().getSerializable(ScoreActivity.SCORE);
                break;
        }
    }

    /**
     * Chooses a move for the computer and plays it
     */
    private int computerChoice() {
        Random random = new Random();
        int choice;

        do {
            choice = random.nextInt(9);
        } while (!tictactoe.isPlayable(choice));

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
                Toast.makeText(this, getString(R.string.message_tie), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.message_player1))
                        .setNegativeButton(R.string.button_OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                break;
            case 2:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.message_player2))
                        .setNegativeButton(R.string.button_OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                break;
            case 3:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.message_computer))
                        .setNegativeButton(R.string.button_OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                break;
        }

        tictactoe.updateScore(winner);
    }

    /**
     * Ends the game
     */
    private void endGame() {
        ImageButton[] buttons = getButtons();
        for (ImageButton button : buttons) {
            // Disable buttons
            button.setEnabled(false);
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
     * @return The position of the given button, -1 if the button is not found
     */
    private int getPosition(ImageButton button) {
        ImageButton[] buttons = getButtons();
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].equals(button)) {
                return i;
            }
        }
        // Shouldn't happen unless you've created a new Button and search for that
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

            // Get button
            ImageButton button = getButtons()[position];
            // Set marker
            button.setImageDrawable(marker);
            // Disable button
            button.setEnabled(false);

            // Check win/tie
            if (tictactoe.checkWin()) {
                displayWin(player);
                endGame();
            } else if (moveCounter == 9) { // Tie game
                displayWin(0);
                endGame();
            }

            // Change turn
            changeTurn();

            // Computer turn
            if (!isEnd && !tictactoe.getPlayer1Turn() && tictactoe.getGameMode().equals(GameMode.PvE)) {
                play(computerChoice());
            }
        }
    }

    /**
     * Changes the turn and sets the player turn text if necessary
     */
    private void changeTurn() {
        // Change turn
        tictactoe.changeTurn();
        // Update text
        if (tictactoe.getGameMode().equals(GameMode.PvP)) {
            if (tictactoe.getPlayer1Turn()) {
                ((TextView) findViewById(R.id.textViewPlayerTurn)).setText(getString(R.string.player1Start));
            } else {
                ((TextView) findViewById(R.id.textViewPlayerTurn)).setText(getString(R.string.player2Start));
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
}
