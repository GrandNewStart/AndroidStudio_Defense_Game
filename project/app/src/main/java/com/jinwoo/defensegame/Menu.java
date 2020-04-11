package com.jinwoo.defensegame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import static com.jinwoo.defensegame.GameView.screenRatioX;
import static com.jinwoo.defensegame.GameView.screenRatioY;
import static com.jinwoo.defensegame.GameView.screenX;

public class Menu {

    int x, y, width, height;
    Bitmap menu;

    Menu(Resources res) {
        menu = BitmapFactory.decodeResource(res, R.drawable.icon_menu);

        width = menu.getWidth();
        height = menu.getHeight();

        width /= 9;
        height /= 9;

        width = (int) (width * screenRatioY);
        height = (int) (height * screenRatioX);

        menu = Bitmap.createScaledBitmap(menu, width, height, false);

        x = screenX - 200;
        y = 100;
    }

    public Rect getCollider() { return new Rect(x, y, x + width, y + height); }
}
