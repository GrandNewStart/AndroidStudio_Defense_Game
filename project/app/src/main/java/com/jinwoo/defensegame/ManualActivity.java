package com.jinwoo.defensegame;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ManualActivity extends AppCompatActivity {

    int counter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_1);
        counter = 1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch(counter) {
                case 1:
                    setContentView(R.layout.activity_manual_2);
                    counter++;
                    return true;
                case 2:
                    setContentView(R.layout.activity_manual_3);
                    counter++;
                    return true;
                case 3:
                    setContentView(R.layout.activity_manual_4);
                    counter++;
                    return true;
                case 4:
                    finish();
                    return true;
            }
        }

        return false;
    }
}
