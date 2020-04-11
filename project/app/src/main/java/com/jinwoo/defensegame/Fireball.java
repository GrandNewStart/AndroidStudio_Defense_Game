package com.jinwoo.defensegame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import java.util.Random;
import static com.jinwoo.defensegame.GameView.screenRatioX;
import static com.jinwoo.defensegame.GameView.screenRatioY;
import static com.jinwoo.defensegame.GameView.screenX;
import static com.jinwoo.defensegame.GameView.screenY;

public class Fireball {

    int x, y,count;
    int width, height;
    int damage, speed;
    Bitmap fireball_1, fireball_2, fireball_3, fireball_4, fireball_5;
    Random random = new Random();

    Fireball(Resources res) {
        fireball_1 = BitmapFactory.decodeResource(res, R.drawable.img_ultimate_1);
        fireball_2 = BitmapFactory.decodeResource(res, R.drawable.img_ultimate_2);
        fireball_3 = BitmapFactory.decodeResource(res, R.drawable.img_ultimate_3);
        fireball_4 = BitmapFactory.decodeResource(res, R.drawable.img_ultimate_4);
        fireball_5 = BitmapFactory.decodeResource(res, R.drawable.img_ultimate_5);

        width = fireball_1.getWidth();
        height = fireball_1.getHeight();

        width /= 7;
        height /= 7;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        fireball_1 = Bitmap.createScaledBitmap(fireball_1, width, height, false);
        fireball_2 = Bitmap.createScaledBitmap(fireball_2, width, height, false);
        fireball_3 = Bitmap.createScaledBitmap(fireball_3, width, height, false);
        fireball_4 = Bitmap.createScaledBitmap(fireball_4, width, height, false);
        fireball_5 = Bitmap.createScaledBitmap(fireball_5, width, height, false);

        reset();

        damage = 10;
        int bound = (int) (5 * screenRatioY);
        speed = random.nextInt(bound) + 25;
        count = 1;
    }

    void reset() {
        x = screenX - random.nextInt(screenX);
        y = screenY/4 + random.nextInt(screenY/2);
    }

    Bitmap getUltimate() {
        if (count == 1) {
            count++;
            return fireball_1;
        }
        if (count == 2) {
            count++;
            return fireball_2;
        }
        if (count == 3) {
            count++;
            return fireball_3;
        }
        if (count == 4) {
            count++;
            return fireball_4;
        }
        count = 1;
        return fireball_5;
    }

    public Rect getFireballCollider() { return new Rect(x, y, x + width, y + height); }
}
