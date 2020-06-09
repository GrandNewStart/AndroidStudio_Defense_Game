package com.jinwoo.defensegame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.jinwoo.defensegame.GameView.screenRatioX;
import static com.jinwoo.defensegame.GameView.screenRatioY;
import static com.jinwoo.defensegame.GameView.screenX;
import static com.jinwoo.defensegame.GameView.screenY;

public class ReadyGo {

    Bitmap ready, go;
    int x, y;
    int width, height;

    ReadyGo(Resources res) {
        ready = BitmapFactory.decodeResource(res, R.drawable.txt_ready);
        go = BitmapFactory.decodeResource(res, R.drawable.txt_go);

        width = ready.getWidth();
        height = ready.getHeight();

        width /= 2;
        height /= 2;

        width = (int) (screenRatioY * width);
        height = (int) (screenRatioX * height);

        x = screenX / 2 - width;
        y = screenY / 2 - height;
    }

    Bitmap getText(boolean GO) {
        if (GO) return go;
        else return ready;
    }
}
