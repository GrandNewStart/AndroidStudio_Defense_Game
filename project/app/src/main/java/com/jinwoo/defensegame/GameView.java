package com.jinwoo.defensegame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    private GameActivity activity;
    private Thread thread;
    private Paint paint;
    private SoundPool sp1, sp2, sp3;
    private SharedPreferences prefs;

    // Game Objects
    private ReadyGo readyGo;
    private Menu menu;
    private Background background;
    private Enemy enemies[];
    private Spray spray;
    private Trap trap;
    private Ultimate ultimate;
    private Fireball fireballs[];

    private boolean isPlaying, spraySoundPlaying, gameStarted, GO;
    public static boolean sprayUsing, trapUsing, ultimateUsing;
    public static int screenX, screenY;
    private int score, defense, touchX, touchY, hitSound, spraySound, trapSound, ultSound, textSize, currentSpraySound, currentUltSound, rank;
    public static float screenRatioX, screenRatioY;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);
        this.screenX = screenX;
        this.screenY = screenY;
        this.activity = activity;

        isPlaying = true;
        spraySoundPlaying = false;
        gameStarted = false;
        GO = false;
        sprayUsing = false;
        trapUsing = false;
        ultimateUsing = false;
        score = 0;
        defense = 100;
        textSize = 80;
        currentSpraySound = 0;
        currentUltSound = 0;
        rank = 0;

        // Setting screen ratio values
        screenRatioX = 1080f / screenX;
        screenRatioY = 2198f / screenY;

        // Initializing sounds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            sp1 = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();
            sp2 = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();
            sp3 = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        else {
            sp1 = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            sp2 = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            sp3 = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        hitSound = sp1.load(activity, R.raw.click, 2);
        spraySound = sp1.load(activity, R.raw.spray, 1);
        trapSound = sp2.load(activity, R.raw.trap, 1);
        ultSound = sp3.load(activity, R.raw.ultimate, 1);

        // Initializing preferences
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);
        //prefs.edit().clear().apply();

        // Initializing ready-go
        readyGo = new ReadyGo(getResources());

        // Initializing menu
        menu = new Menu(getResources());

        // Initializing background
        background = new Background(screenX, screenY, getResources());

        // Initializing enemies
        enemies = new Enemy[4];
        for (int i = 0; i < 4; i++) {
            Enemy enemy = new Enemy(getResources());
            enemies[i] = enemy;
        }

        // Initializing skills
        spray = new Spray(getResources());
        trap = new Trap(getResources());
        ultimate = new Ultimate(getResources());
        fireballs = new Fireball[20];
        for (int i = 0; i < 20; i++) {
            Fireball fireball = new Fireball(getResources());
            fireballs[i] = fireball;
        }

        // Initializing paint
        paint = new Paint();

        // Initializing thread
        thread = new Thread(this);
    }

    @Override
    public void run() {
        startReadyGo();
        while(isPlaying) {
            update();
            draw();
            sleep();
            gameOver();
        }
    }

    public void update() {
        if (gameStarted) enemyUpdate();
        sprayUpdate();
        trapUpdate();
        ultimateUpdate();
    }

    public void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas =getHolder().lockCanvas();

            // background
            canvas.drawBitmap(background.background, background.x, background.y, paint);
            // menu
            canvas.drawBitmap(menu.menu, menu.x, menu.y, paint);
            // score
            paint.setTextSize(textSize);
            paint.setColor(Color.BLACK);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("" + score, screenX/2, 150, paint);
            // defense
            if(gameStarted) {
                paint.setTextSize(textSize);
                paint.setColor(Color.GRAY);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("DEFENSE: " + defense, screenX/2, screenY/2, paint);
            }
            if (gameStarted)
                // enemies
                for (Enemy enemy : enemies)
                    canvas.drawBitmap(enemy.getEnemy(), enemy.x, enemy.y, paint);
            else
                // ready-go
                canvas.drawBitmap(readyGo.getText(GO), readyGo.x, readyGo.y, paint);
            // spray
            canvas.drawBitmap(spray.getIcon(), spray.iconX, spray.iconY, paint);
            if (sprayUsing)
                canvas.drawBitmap(spray.spray, spray.touchX, spray.touchY, paint);
            if (spray.state != Spray.status.ready) {
                paint.setTextSize(textSize);
                paint.setColor(Color.WHITE);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("" + spray.timer,
                        spray.iconX + spray.icon_width/2,
                        spray.iconY + spray.icon_height/2 + textSize/3,
                        paint);
            }
            // trap
            canvas.drawBitmap(trap.getIcon(), trap.iconX, trap.iconY, paint);
            if (trapUsing)
                canvas.drawBitmap(trap.trap, trap.x, trap.y, paint);
            if (trap.state != Trap.status.ready) {
                paint.setTextSize(textSize);
                paint.setColor(Color.WHITE);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("" + trap.timer,
                        trap.iconX + trap.icon_width/2,
                        trap.iconY + trap.icon_height/2 + textSize/3,
                        paint);
            }
            // ultimate
            canvas.drawBitmap(ultimate.getIcon(), ultimate.iconX, ultimate.iconY, paint);
            if (ultimate.state != Ultimate.status.ready) {
                int newTextSize = textSize - 10;
                paint.setTextSize(newTextSize);
                paint.setColor(Color.WHITE);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(ultimate.gauge + " %",
                        ultimate.iconX + ultimate.icon_width/2,
                        ultimate.iconY + ultimate.icon_height/2 + newTextSize/3,
                        paint);
            }
            if (ultimateUsing) {
                for (Fireball fireball : fireballs)
                    canvas.drawBitmap(fireball.getUltimate(), fireball.x, fireball.y, paint);
            }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
        spray.resume();
        trap.resume();
        ultimate.resume();
        if (!prefs.getBoolean("isMute", false)) {
            sp1.resume(currentSpraySound);
            sp3.resume(currentUltSound);
        }
        else {
            sp1.pause(currentSpraySound);
            sp3.pause(currentUltSound);
        }
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
            spray.pause();
            trap.pause();
            ultimate.pause();
            sp1.pause(currentSpraySound);
            sp3.pause(currentUltSound);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startReadyGo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    GO = true;
                    Thread.sleep(1000);
                    gameStarted = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void enemyUpdate() {
        for (Enemy enemy : enemies) {
            // Move enemy
            enemy.y += enemy.speed;

            // Enemy health is under zero
            if (enemy.health < 0) {
                score += 10;
                if (!ultimateUsing) ultimate.gauge += 2;
                enemy.reset();
            }

            // Enemy goes out of bound
            if (enemy.y + enemy.height > screenY - 100) {
                defense -= 2;
                enemy.reset();
            }
        }
    }

    private void sprayUpdate() {
        if (sprayUsing){
            if (!spraySoundPlaying) {
                spraySoundPlaying = true;
                currentSpraySound = sp1.play(spraySound, 1, 1, 0, -1, 2.0f);
                if (prefs.getBoolean("isMute", false))
                    sp1.pause(currentSpraySound);
            }
        }
        else {
            sp1.stop(currentSpraySound);
            spraySoundPlaying = false;
        }
    }

    private void trapUpdate() {
        if (trapUsing) {
            for (Enemy enemy : enemies) {
                if (Rect.intersects(enemy.getCollider(), trap.getTrapCollider())){
                    enemy.speed = 5;
                    enemy.health--;
                }
            }
        }
    }

    private void ultimateUpdate() {
        if (ultimateUsing) {
            for (Fireball fireball : fireballs) {
                // Fireball move
                fireball.x -= fireball.speed;

                // Enemy damage
                for (Enemy enemy : enemies) {
                    if (Rect.intersects(fireball.getFireballCollider(), enemy.getCollider()))
                        enemy.health -= fireball.damage;
                }

                // If fireball goes out of bound
                if (fireball.x + fireball.width < 0)
                    fireball.reset();
            }
        }
    }

    private boolean saveIfHighScore() {
        SharedPreferences.Editor editor = prefs.edit();
        int scores[] = new int[10];

        for (int i = 0; i < 10; i++)
            scores[i] = prefs.getInt("score." + (i + 1), 0);

        for (int i = 0; i < 10; i++) {
            if (scores[i] < score) {
                rank = i + 1;
                return true;
            }
        }

        return false;
    }

    private void gameOver() {
        if (defense <= 0) {
            if (saveIfHighScore()) {
                Intent intent = new Intent(activity, NewRecordActivity.class);
                intent.putExtra("score", score);
                intent.putExtra("rank", rank);
                activity.startActivity(intent);
                activity.finish();
            }
            else {
                activity.startActivityForResult(new Intent(activity, GameOverActivity.class), 2);
                activity.finish();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (!gameStarted) return false;

        if (!sprayUsing) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                touchX = (int) event.getX();
                touchY = (int) event.getY();
                Rect touch = new Rect(touchX - 25, touchY - 25, touchX + 25, touchY + 25);

                // enemy attack
                for (Enemy enemy : enemies) {
                    if (Rect.intersects(touch, enemy.getCollider())) {
                        if (!prefs.getBoolean("isMute", false))
                            sp1.play(hitSound, 1, 1, 0, 0, 2.0f);
                        enemy.health -= 20;
                    }
                }

                // spray icon click
                if (Rect.intersects(touch, spray.getIconCollider())) {
                    if (spray.state == Spray.status.ready)
                        spray.state = Spray.status.using;
                    return true;
                }

                // trap icon click
                if (!trapUsing) {
                    if (Rect.intersects(touch, trap.getIconCollider())) {
                        if (trap.state == Trap.status.ready) {
                            trap.state = Trap.status.using;
                            if (!prefs.getBoolean("isMute", false))
                                sp2.play(trapSound, 1, 1, 0, 0, 1);
                        }
                        return true;
                    }
                }

                // ultimate icon click
                if (!ultimateUsing) {
                    if (Rect.intersects(touch, ultimate.getIconCollider())) {
                        if (ultimate.state == Ultimate.status.ready) {
                            ultimate.state = Ultimate.status.using;
                            if (!prefs.getBoolean("isMute", false))
                                currentUltSound = sp3.play(ultSound, 1, 1, 0, 0, 1);
                        }

                        return true;
                    }
                }

                // menu click
                if (Rect.intersects(touch, menu.getCollider())) {
                    activity.startActivityForResult(new Intent(activity, MenuActivity.class), 1);
                    return true;
                }

                touchX = -5000;
                touchY = -5000;
            }
        }
        else {
            // spray attack
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchX = (int) event.getX();
                    touchY = (int) event.getY();
                    Rect touch = new Rect(touchX - 25, touchY - 25, touchX + 25, touchY + 25);

                    // menu click
                    if (Rect.intersects(touch, menu.getCollider())) {
                        activity.startActivityForResult(new Intent(activity, MenuActivity.class), 1);
                        return true;
                    }

                    // trap icon click
                    if (!trapUsing) {
                        if (Rect.intersects(touch, trap.getIconCollider())) {
                            if (trap.state == Trap.status.ready) {
                                trap.state = Trap.status.using;
                                if (!prefs.getBoolean("isMute", false))
                                    sp2.play(trapSound, 1, 1, 0, 0, 1);
                            }
                            return true;
                        }
                    }

                    // ultimate icon click
                    if (!ultimateUsing) {
                        if (Rect.intersects(touch, ultimate.getIconCollider())) {
                            if (ultimate.state == Ultimate.status.ready) {
                                ultimate.state = Ultimate.status.using;
                                if (!prefs.getBoolean("isMute", false))
                                    currentUltSound = sp3.play(ultSound, 1, 1, 0, 0, 1);
                            }
                            return true;
                        }
                    }

                    // use spray
                    spray.touchX = touchX - spray.width / 2;
                    spray.touchY = touchY - spray.height / 2;
                    for (Enemy enemy : enemies) {
                        if (Rect.intersects(spray.getSprayCollider(), enemy.getCollider())) {
                            enemy.health -= spray.damage;
                        }
                    }
                    return true;
                case MotionEvent.ACTION_MOVE:
                    // use spray
                    touchX = (int) event.getX();
                    touchY = (int) event.getY();
                    spray.touchX = touchX - spray.width / 2;
                    spray.touchY = touchY - spray.height / 2;
                    for (Enemy enemy : enemies) {
                        if (Rect.intersects(spray.getSprayCollider(), enemy.getCollider())) {
                            enemy.health -= spray.damage;
                        }
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    spray.touchX = -5000;
                    spray.touchY = -5000;
                    return false;
            }
        }

        return false;
    }
}
