package com.jinwoo.defensegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    Handler handler;
    boolean isMute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);

        Button toMain = (Button) findViewById(R.id.btn_enter);
        Button resume = (Button) findViewById(R.id.btn_resume);
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

        toMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra("quit", true);
                setResult(1, result);
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
                finish();
            }
        });

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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
    }
}
