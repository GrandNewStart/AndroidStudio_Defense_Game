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
import static com.jinwoo.defensegame.GameView.ultimateUsing;

public class Ultimate implements Runnable {

    Thread thread;
    int iconX, iconY, count;
    int icon_width, icon_height;
    int gauge;
    enum status{ready, charging, using};
    status state;
    Bitmap ready, charging, using;

    Ultimate(Resources res) {
        ready = BitmapFactory.decodeResource(res, R.drawable.icon_ultimate);
        using = BitmapFactory.decodeResource(res, R.drawable.icon_using_ultimate);
        charging = BitmapFactory.decodeResource(res, R.drawable.icon_cooling_ultimate);

        icon_width = ready.getWidth();
        icon_height = ready.getHeight();

        icon_width /= 5;
        icon_height /= 5;

        icon_width = (int) (icon_width * screenRatioY);
        icon_height = (int) (icon_height * screenRatioX);

        ready = Bitmap.createScaledBitmap(ready, icon_width, icon_height, false);
        using = Bitmap.createScaledBitmap(using, icon_width, icon_height, false);
        charging = Bitmap.createScaledBitmap(charging, icon_width, icon_height, false);

        iconX = screenX / 2 - icon_width / 2;
        iconY = screenY - 350;
        gauge = 0;
        count = 1;
        state = status.charging;

        thread = new Thread(this);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {

                switch (state) {
                    case ready:
                        break;
                    case charging:
                        ultimateUsing = false;
                        if (gauge >= 100)
                            state = status.ready;
                        break;
                    case using:
                        gauge = 0;
                        ultimateUsing = true;
                        for (int i = 5; i > 0; i--) {
                            Thread.sleep(1000);
                        }
                        state = status.charging;
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Log.d("ULTIMATE ", "THREAD STOPPED");
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
        switch (state) {
            case ready:
                return ready;
            case charging:
                return charging;
            case using:
                return using;
        }
        return null;
    }

    public Rect getIconCollider() { return new Rect(iconX, iconY, iconX + icon_width, iconY + icon_height); }
}
