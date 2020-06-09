package com.jinwoo.defensegame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NewRecordActivity extends AppCompatActivity {

    int score, rank;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        rank = intent.getIntExtra("rank", 0);

        TextView txt_score = (TextView) findViewById(R.id.txt_score);
        txt_score.setText(""+score);

        Button enter = (Button) findViewById(R.id.btn_enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edt_id = (EditText) findViewById(R.id.edt_id);
                String id = edt_id.getText().toString();

                // ID validation
                if (id.equals("")) {
                    Toast.makeText(NewRecordActivity.this, "아이디를 입력하십시오.", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences prefs = NewRecordActivity.this.getSharedPreferences("game", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                // Get top 10
                int scores[] = new int[10];
                String ids[] = new String[10];
                for (int i = 0; i < 10; i++) {
                    scores[i] = prefs.getInt("score." + (i + 1), 0);
                    ids[i] = prefs.getString("id." + (i + 1), "no ID");
                }

                // Update scores and ids
                int newScores[] = new int[10];
                String newIds[] = new String[10];
                for (int i = 0, j = 0; i < 10; i++, j++) {
                    if (i == rank - 1) {
                        newScores[i] = score;
                        newIds[i] = id;
                        j--;
                        continue;
                    }

                    newScores[i] = scores[j];
                    newIds[i] = ids[j];
                }

                for (int i = 0; i < 10; i++) {
                    editor.putInt("score." + (i + 1), newScores[i]);
                    editor.putString("id." + (i + 1), newIds[i]);
                }

                editor.apply();

                startActivity(new Intent(NewRecordActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() { }
}
