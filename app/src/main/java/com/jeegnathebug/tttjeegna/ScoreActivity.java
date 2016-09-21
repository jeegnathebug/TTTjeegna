package com.jeegnathebug.tttjeegna;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Intent intent = getIntent();
        int player1 = intent.getIntExtra(MainActivity.COUNTER_PLAYER1_WINS, 0);
        int player2 = intent.getIntExtra(MainActivity.COUNTER_PLAYER2_WINS, 0);
        int ties = intent.getIntExtra(MainActivity.COUNTER_TIES, 0);

        // Display data
        String text = String.format(getString(R.string.scorePlayer1), player1);
        ((TextView) findViewById(R.id.textViewScorePlayer1)).setText(text);

        text = String.format(getString(R.string.scorePlayer2), player2);
        ((TextView) findViewById(R.id.textViewScorePlayer2)).setText(text);

        text = String.format(getString(R.string.scoreTies), ties);
        ((TextView) findViewById(R.id.textViewScoreTies)).setText(text);
    }
}
