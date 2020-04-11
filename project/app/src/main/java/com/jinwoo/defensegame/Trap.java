package com.jinwoo.defensegame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import static com.jinwoo.defensegame.GameView.screenRatioX;
import static com.jinwoo.defensegame.GameView.screenRatioY;
import static com.jinwoo.defensegame.GameView.screenX;
import static com.jinwoo.defensegame.GameView.screenY;
import static com.jinwoo.defensegame.GameView.trapUsing;

public class Trap implements Runnable {

    Thread thread;
    int x, y, iconX, iconY;
    int width, height, icon_width, icon_height;
    int damage;
    int timer;
    enum status{ready, using, cooling};
    status state;
    Bitmap trap, icon_ready, icon_using, icon_cooling;

    Trap(Resources res) {
        trap = BitmapFactory.decodeResource(res, R.drawable.img_trap);
        icon_ready = BitmapFactory.decodeResource(res, R.drawable.icon_trap);
        icon_using = BitmapFactory.decodeResource(res, R.drawable.icon_using_trap);
        icon_cooling = BitmapFactory.decodeResource(res, R.drawable.icon_cooling_trap);

        width = trap.getWidth();
        height = trap.getHeight();
        icon_width = icon_ready.getWidth();
        icon_height = icon_ready.getHeight();

        width /= 2;
        height /= 2;
        icon_width /= 6;
        icon_height /= 6;

        width = (int) (width * screenRatioX);
        icon_width = (int) (icon_width * screenRatioY);
        icon_height = (int) (icon_height * screenRatioX);

        trap = Bitmap.createScaledBitmap(trap, width, height, false);
        icon_ready = Bitmap.createScaledBitmap(icon_ready, icon_width, icon_height, false);
        icon_using = Bitmap.createScaledBitmap(icon_using, icon_width, icon_height, false);
        icon_cooling = Bitmap.createScaledBitmap(icon_cooling, icon_width, icon_height, false);

        x = 0;
        y = screenY / 2;
        iconX = screenX - 300;
        iconY = screenY - 300;
        damage = 10;
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
                        trapUsing = true;
                        while (timer > 0) {
                            Thread.sleep(1000);
                            timer--;
                        }
                        timer = 10;
                        state = status.cooling;
                        break;
                    case cooling:
                        trapUsing = false;
                        while (timer > 0) {
                            Thread.sleep(1000);
                            timer--;
                        }
                        timer = 5;
                        state = status.ready;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            Log.d("TRAP ", "THREAD STOPPED");
        }
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

    public Rect getTrapCollider() { return new Rect(x, y, x + width, y + height); }

    public Rect getIconCollider() { return new Rect(iconX, iconY, iconX + icon_width, iconY + icon_height); }
}
