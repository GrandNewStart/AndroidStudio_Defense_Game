package com.jinwoo.defensegame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import static com.jinwoo.defensegame.GameView.screenRatioX;
import static com.jinwoo.defensegame.GameView.screenRatioY;
import static com.jinwoo.defensegame.GameView.screenY;
import static com.jinwoo.defensegame.GameView.sprayUsing;

public class Spray implements Runnable{

    Thread thread;
    int touchX, touchY, iconX, iconY;
    int width, height, icon_width, icon_height;
    int damage;
    int timer;
    enum status{ready, cooling, using};
    status state;
    Bitmap spray, icon_ready, icon_cooling, icon_using;

    Spray(Resources res) {
        spray = BitmapFactory.decodeResource(res, R.drawable.img_circle);
        icon_ready = BitmapFactory.decodeResource(res, R.drawable.icon_spray);
        icon_using = BitmapFactory.decodeResource(res, R.drawable.icon_using_spray);
        icon_cooling = BitmapFactory.decodeResource(res, R.drawable.icon_cooling_spray);

        width = spray.getWidth();
        height = spray.getHeight();
        icon_width = icon_ready.getWidth();
        icon_height = icon_ready.getHeight();

        icon_width /= 6;
        icon_height /= 6;

        width = (int) (width * screenRatioY);
        height = (int) (height * screenRatioX);
        icon_width = (int) (icon_width * screenRatioY);
        icon_height = (int) (icon_height * screenRatioX);

        spray = Bitmap.createScaledBitmap(spray, width, height, false);
        icon_ready = Bitmap.createScaledBitmap(icon_ready, icon_width, icon_height, false);
        icon_using = Bitmap.createScaledBitmap(icon_using, icon_width, icon_height, false);
        icon_cooling = Bitmap.createScaledBitmap(icon_cooling, icon_width, icon_height, false);

        touchX = -500;
        touchY = -500;
        iconX = 300 - icon_width;
        iconY = screenY - 300;
        damage = 5;
        timer = 5;
        state = status.ready;

        thread = new Thread(this);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()){
                switch (state){
                    case ready:
                        break;
                    case using:
                        sprayUsing = true;
                        while (timer > 0) {
                            Thread.sleep(1000);
                            timer--;
                        }
                        timer = 5;
                        state = status.cooling;
                        break;
                    case cooling:
                        sprayUsing = false;
                        while (timer > 0) {
                            Thread.sleep(1000);
                            timer--;
                        }
                        timer = 5;
                        state = status.ready;
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{ Log.d("SPRAY ", "THREAD STOPPED"); }
    }

    void resume() {
        thread = new Thread(this);
        thread.start();
    }

    void pause() {
        thread.interrupt();
    }

    Bitmap getIcon() {
        switch (state){
            case ready:
                return icon_ready;
            case using:
                return icon_using;
            case cooling:
                return icon_cooling;
        }
        return null;
    }

    public Rect getSprayCollider() { return new Rect(touchX, touchY, touchX + width, touchY + height); }

    public Rect getIconCollider() { return new Rect(iconX, iconY, iconX + icon_width, iconY + icon_height); }
}
