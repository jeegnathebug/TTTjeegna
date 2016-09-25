package com.jeegnathebug.tttjeegna;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Log;

import com.jeegnathebug.tttjeegna.business.TicTacToe;

public class ScoreActivity extends AppCompatActivity {

    private TicTacToe tictactoe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Intent intent = getIntent();
        tictactoe = (TicTacToe)intent.getSerializableExtra(MainActivity.TIC_TAC_TOE);

        int player1 = tictactoe.getPlayer1Score(), player2 = tictactoe.getPlayer2Score(), ties = tictactoe.getTies();
        Log.i("intent player1", player1 + "");
        Log.i("player2", player2 + "");
        Log.i("ties", ties + "");

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            player1 = savedInstanceState.getInt(MainActivity.COUNTER_PLAYER1_WINS);
            player2 = savedInstanceState.getInt(MainActivity.COUNTER_PLAYER2_WINS);
            ties = savedInstanceState.getInt(MainActivity.COUNTER_TIES);
        }

        // Update view
        String text = String.format(getString(R.string.scorePlayer1), player1);
        ((TextView) findViewById(R.id.textViewScorePlayer1)).setText(text);

        text = String.format(getString(R.string.scorePlayer2), player2);
        ((TextView) findViewById(R.id.textViewScorePlayer2)).setText(text);

        text = String.format(getString(R.string.scoreTies), ties);
        ((TextView) findViewById(R.id.textViewScoreTies)).setText(text);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save state information
        savedInstanceState.putInt(MainActivity.COUNTER_PLAYER1_WINS, tictactoe.getPlayer1Score());
        savedInstanceState.putInt(MainActivity.COUNTER_PLAYER2_WINS, tictactoe.getPlayer2Score());
        savedInstanceState.putInt(MainActivity.COUNTER_TIES, tictactoe.getTies());

        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Resets the scores
     *
     * @param v The {@code View}
     */
    public void resetScores(View v) {
        tictactoe.resetScores();

        // Update view
        String text = String.format(getString(R.string.scorePlayer1), 0);
        ((TextView) findViewById(R.id.textViewScorePlayer1)).setText(text);

        text = String.format(getString(R.string.scorePlayer2), 0);
        ((TextView) findViewById(R.id.textViewScorePlayer2)).setText(text);

        text = String.format(getString(R.string.scoreTies), 0);
        ((TextView) findViewById(R.id.textViewScoreTies)).setText(text);
    }
}
