package com.jinwoo.defensegame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    boolean isMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button play = (Button) findViewById(R.id.btn_play);
        Button manual = (Button) findViewById(R.id.btn_manual);
        Button quit = (Button) findViewById(R.id.btn_quit);
        Button scores = (Button) findViewById(R.id.btn_scores);
        final ImageView sound = (ImageView) findViewById(R.id.img_sound);

        handler = new Handler();
        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        isMute = prefs.getBoolean("isMute", false);

        if (isMute) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    sound.setImageResource(R.drawable.icon_mute_on);
                }
            });
        }

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!isMute) {
                            sound.setImageResource(R.drawable.icon_mute_on);
                            isMute = true;
                        }
                        else {
                            sound.setImageResource(R.drawable.icon_mute_off);
                            isMute = false;
                        }

                        editor.putBoolean("isMute", isMute);
                        editor.apply();
                    }
                });
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
                finish();
            }
        });

        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ManualActivity.class));
            }
        });

        scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
