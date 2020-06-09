package com.jinwoo.defensegame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

import static com.jinwoo.defensegame.GameView.screenRatioX;
import static com.jinwoo.defensegame.GameView.screenRatioY;
import static com.jinwoo.defensegame.GameView.screenX;

public class Enemy {
    int x, y;
    int width, height;
    int speed;
    int health;
    int count = 1;
    Random random = new Random();
    Bitmap enemy_1, enemy_2, enemy_3;

    Enemy(Resources res) {
        enemy_1 = BitmapFactory.decodeResource(res, R.drawable.img_enemy_1);
        enemy_2 = BitmapFactory.decodeResource(res, R.drawable.img_enemy_2);
        enemy_3 = BitmapFactory.decodeResource(res, R.drawable.img_enemy_3);

        width = enemy_1.getWidth();
        height = enemy_1.getHeight();

        width /= 3;
        height /= 3;

        width =(int) (width * screenRatioY);
        height =(int) (height * screenRatioX);

        enemy_1 = Bitmap.createScaledBitmap(enemy_1, width, height, false);
        enemy_2 = Bitmap.createScaledBitmap(enemy_2, width, height, false);
        enemy_3 = Bitmap.createScaledBitmap(enemy_3, width, height, false);

        reset();
    }

    void reset() {
        int bound = (int) (4 * screenRatioX);
        speed = random.nextInt(bound) + 12;
        y = 0;
        x = random.nextInt(screenX - width);
        health = 100;
    }


    Bitmap getEnemy() {
        if (count == 1) {
            count++;
            return enemy_1;
        }
        if (count == 2) {
            count++;
            return enemy_2;
        }
        if (count == 3) {
            count++;
            return enemy_1;
        }
        count = 1;
        return enemy_3;
    }

    Rect getCollider() {
        return new Rect(x, y, x + width, y + height);
    }
}
