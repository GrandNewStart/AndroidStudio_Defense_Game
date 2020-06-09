package com.jinwoo.defensegame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    ListView scoreList;
    ArrayList<Score> scores;
    ScoreAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Button back = (Button) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        scores = new ArrayList<Score>();


        for (int i = 1; i < 11; i++) {
            int score = prefs.getInt("score." + i, 0);
            String id = prefs.getString("id." + i, "no ID");
            Score s = new Score(i, id, score);
            scores.add(s);
        }

        scoreList = (ListView) findViewById(R.id.score_list);
        adapter = new ScoreAdapter(scores, getApplicationContext());
        scoreList.setAdapter(adapter);
    }
}
