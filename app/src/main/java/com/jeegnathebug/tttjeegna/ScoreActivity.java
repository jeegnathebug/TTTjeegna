package com.jeegnathebug.tttjeegna;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jeegnathebug.tttjeegna.business.TicTacToe;

/**
 * The score screen
 */
public class ScoreActivity extends Activity {

    private TicTacToe tictactoe;
    public static final String SCORE = "com.jeegnathebug.scoreActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Intent intent = getIntent();
        tictactoe = (TicTacToe) intent.getSerializableExtra(MainActivity.TIC_TAC_TOE);

        int player1 = tictactoe.getPlayer1Score(), player2 = tictactoe.getPlayer2Score(), computer = tictactoe.getComputerScore(), ties = tictactoe.getTies();

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            player1 = savedInstanceState.getInt(MainActivity.COUNTER_PLAYER1_WINS);
            player2 = savedInstanceState.getInt(MainActivity.COUNTER_PLAYER2_WINS);
            computer = savedInstanceState.getInt(MainActivity.COUNTER_COMPUTER_WINS);
            ties = savedInstanceState.getInt(MainActivity.COUNTER_TIES);
        }

        // Update view
        String text = String.format(getString(R.string.score_player1), player1);
        ((TextView) findViewById(R.id.textViewScorePlayer1)).setText(text);

        text = String.format(getString(R.string.score_player2), player2);
        ((TextView) findViewById(R.id.textViewScorePlayer2)).setText(text);

        text = String.format(getString(R.string.score_computer), computer);
        ((TextView) findViewById(R.id.textViewScoreComputer)).setText(text);

        text = String.format(getString(R.string.score_ties), ties);
        ((TextView) findViewById(R.id.textViewScoreTies)).setText(text);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save state information
        savedInstanceState.putInt(MainActivity.COUNTER_PLAYER1_WINS, tictactoe.getPlayer1Score());
        savedInstanceState.putInt(MainActivity.COUNTER_PLAYER2_WINS, tictactoe.getPlayer2Score());
        savedInstanceState.putInt(MainActivity.COUNTER_COMPUTER_WINS, tictactoe.getComputerScore());
        savedInstanceState.putInt(MainActivity.COUNTER_TIES, tictactoe.getTies());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void finish() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(SCORE, tictactoe);
        setResult(RESULT_OK, intent);

        super.finish();
    }

    /**
     * Resets the scores
     *
     * @param v
     *            The {@code View}
     */
    public void resetScores(View v) {
        tictactoe.resetScores();

        // Update view
        String text = String.format(getString(R.string.score_player1), 0);
        ((TextView) findViewById(R.id.textViewScorePlayer1)).setText(text);

        text = String.format(getString(R.string.score_player2), 0);
        ((TextView) findViewById(R.id.textViewScorePlayer2)).setText(text);

        text = String.format(getString(R.string.score_computer), 0);
        ((TextView) findViewById(R.id.textViewScoreComputer)).setText(text);

        text = String.format(getString(R.string.score_ties), 0);
        ((TextView) findViewById(R.id.textViewScoreTies)).setText(text);
    }
}
