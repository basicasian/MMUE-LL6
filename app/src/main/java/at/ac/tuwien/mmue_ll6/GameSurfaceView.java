package at.ac.tuwien.mmue_ll6;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;

import at.ac.tuwien.mmue_ll6.assets.Background;
import at.ac.tuwien.mmue_ll6.assets.DynamicObject;
import at.ac.tuwien.mmue_ll6.assets.SpriteObject;
import at.ac.tuwien.mmue_ll6.assets.StaticObject;

/**
 * The game view for loading assets and starting and ending the game
 * @author Renate Zhang
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = GameSurfaceView.class.getSimpleName();

    private GameLoop gameLoop;
    private Thread gameMainThread;
    private final Context context;
    private double deltaTime;

    // check variables
    private boolean isJumping = false;
    private boolean isGameOver = false;
    private boolean isGameWin = false;
    private int jumpCounter;

    // objects
    private DynamicObject flummi;
    private DynamicObject enemy;
    private DynamicObject goal;
    private SpriteObject fire;
    private ArrayList<DynamicObject> platformObjects = new ArrayList<>();
    private ArrayList<DynamicObject> dynamicObjects = new ArrayList<>();

    // assets
    private Background bg1;
    private StaticObject buttonLeft;
    private StaticObject buttonRight;
    private StaticObject buttonUp;
    private StaticObject gameOverImage;
    private StaticObject gameWinImage;
    private ArrayList<StaticObject> staticObjects = new ArrayList<>();

    // information about display
    int displayHeight;
    int displayWidth;
    int barHeight;
    int offset = 270;

    // coordinates of touch
    int touchX;
    int touchY;

    /**
     * constructor for the class GameSurfaceView
     * @param attrs attribute set
     * @param context needed to get access to resource (bitmaps)
     */
    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        // callback to add events
        getHolder().addCallback(this);
        // so events can be handled
        setFocusable(true);

        // initialize resources
        loadAssets(context);
    }

    /**
     * create a new game loop and game thread, and starts it
     * @param holder surface holder needed for the game loop
     */
    private void startGame(SurfaceHolder holder) {
        gameLoop = new GameLoop(holder, this);
        gameMainThread = new Thread(gameLoop);

        Log.d(TAG, "Starting Game Thread");
        gameMainThread.start();
    }

    /**
     * ends the game and joins the game thread
     */
    private void endGame() {
        gameLoop.setRunning(false);
        try {
            Log.d(TAG, "Joining Game Thread");
            gameMainThread.join();
        } catch (InterruptedException e) {
            Log.e("Error", e.getMessage());
        }
    }

    /**
     * loading the assets (character, background, etc) and initializing them with x and y coordinates
     * also getting the display sizes for the background
     * @param context to get the bitmap
     */
    private void loadAssets(Context context) {

        // get the size of the screen
        displayWidth = context.getResources().getDisplayMetrics().widthPixels;
        displayHeight = context.getResources().getDisplayMetrics().heightPixels;

        // because we removed the notification bar, we have to add the height manually
        barHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            barHeight = getResources().getDimensionPixelSize(resourceId);
            displayWidth += barHeight;
        }
        Log.d(TAG, "loadAssets: "+ displayWidth);

        // Initialize the assets
        // coordinate system starts from top left! (in landscape mode)
        // but elements are initialized from bottom left

        // dynamic objects
        flummi = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.flummi), 700, displayHeight - 300);
        enemy = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy), 300, displayHeight - 300);
        goal = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.goal), 3000, displayHeight/2);
        DynamicObject platform1 = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.platform2), 100, displayHeight - 150);
        DynamicObject platform2 = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.platform2), 1100, displayHeight - 150);
        DynamicObject platform3 = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.platform2), 2000, displayHeight - 300);
        platformObjects = new ArrayList<>(Arrays.asList(platform1, platform2, platform3));
        dynamicObjects = new ArrayList<>(Arrays.asList(platform1, platform2, platform3, flummi, enemy, goal));

        // sprites
        fire = new SpriteObject(BitmapFactory.decodeResource(getResources(), R.drawable.fire), 4, 100, displayHeight - 300);

        // static objects
        buttonLeft = new StaticObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrowleft), displayWidth - 500, displayHeight - 50);
        buttonRight= new StaticObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrowright), displayWidth - 250, displayHeight - 50);
        buttonUp = new StaticObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrowup), barHeight + 50, displayHeight - 50);
        gameOverImage = new StaticObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.gameover), displayWidth/5, 2*displayHeight/3);
        gameWinImage = new StaticObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.youwin), displayWidth/5, displayHeight/2);
        staticObjects = new ArrayList<>(Arrays.asList(buttonLeft, buttonRight, buttonUp));

        bg1 = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background), barHeight, displayWidth, displayHeight);
    }

    /**
     * surfaceView has been created, create game loop and start
     * @param surfaceHolder needed for the game loop
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        startGame(surfaceHolder);
    }

    /**
     * SurfaceView has been changed (size, format,…)
     */
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder,  int format, int width, int height) {
    }

    /**
     * SurfaceView has been hidden, end the game loop
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        endGame();
    }

    /**
     * a touch-event has been triggered, set pressed state to true or false
     * can be pressed only once, unlike longTouchEvent() method
     * @param e the input motion event
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        Log.d(TAG, "onTouchEvent: " + e);

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            setPressed(true); // needed for LongTouchEvent()

            touchX = (int) e.getX();
            touchY = (int) e.getY();

            // up botton
            if (touchX >= 150 && touchX < (150 + buttonLeft.getBitmap().getWidth())
                    && touchY >= displayHeight - offset && touchY < (displayHeight - offset + buttonLeft.getBitmap().getHeight())) {
                // jump motion is not handled here, but in longTouchEvent() for smoother movement
                isJumping = true;
                jumpCounter = 0;
            }
        }

        if (e.getAction() == MotionEvent.ACTION_UP) {
            setPressed(false);
            isJumping = false;
        }

        // if the game's over, touching on the screen sends you to MainActivity
        if (isGameOver || isGameWin){
            if (e.getAction()==MotionEvent.ACTION_DOWN){
                context.startActivity(new Intent(context,AfterGameActivity.class));
            }
        }
        return true;
    }

    /**
     * check if the buttons are pressed, if yes move character to the left or right
     * can be pressed permanently, unlike onTouchEvent() method
     */
    private void longTouchEvent() {

        // TODO: should here dt also be used? -> platforms end up moving in a different speed
        // move scene to the right
        if (flummi.getX() >= (displayWidth/2)
                && !isJumping && !(checkButton("left"))) {
            for (DynamicObject d: dynamicObjects) {
               d.move(-200 * 0.03, 0);
            }
            fire.move(-200 * 0.03, 0);
        }

        // move scene to the left
        if (flummi.getX() <= barHeight * 2) {
            for (DynamicObject d: dynamicObjects) {
                d.move(+200 * 0.03, 0);
            }
            fire.move(+200 * 0.03, 0);
        }

        // right button
        if (checkButton("right") && flummi.getX() < (displayWidth/2)) {
            flummi.move(+200 * this.deltaTime, 0); // velocity * dt
        }
        // left button
        if (checkButton("left") && flummi.getX() > barHeight) {
            flummi.move(-200 * this.deltaTime, 0); // velocity * dt
        }
        // up button
        if (isJumping && jumpCounter < 5) {
            // jumpCounter controls the max time of jumping, so the character cant jump indefinitely
            jumpCounter++;
            flummi.move(0,-1700 * this.deltaTime); // velocity * dt
        }
    }

    /**
     * help method to check which button is pressed
     * @param button the specified direction
     */
    private boolean checkButton(String button) {
        boolean result = false;
        switch (button) {
            case "right":
                result = (touchX >= displayWidth - offset && touchX < (displayWidth - offset + buttonLeft.getBitmap().getWidth())
                        && touchY >= displayHeight - offset && touchY < (displayHeight - offset + buttonLeft.getBitmap().getHeight()));
                break;
            case "left":
                result = (touchX >= displayWidth - offset * 2 && touchX < (displayWidth - offset * 2 + buttonLeft.getBitmap().getWidth())
                        && touchY >= displayHeight - offset && touchY < (displayHeight - offset + buttonLeft.getBitmap().getHeight()));
                break;
        }
        return result;
    }

    /**
     * help method to check if the character is colliding against platforms
     * @return true if character is colliding against platform
     */
    private boolean checkCollision() {
        boolean result = false;
        for (DynamicObject p: platformObjects) {
            if (Rect.intersects(flummi.getRectTarget(), p.getRectTarget())) {
                result = true;
            }
        }
        return result;
    }


    /**
     * updates the sprite animation and checks if the screen is still pressed
     * or if lose condition is fulfilled
     * @param deltaTime the delta time needed for frame independence
     */
    public void update(double deltaTime) {
        this.deltaTime = deltaTime;

        // lose condition
        // if flummi touches the enemy or flummi falls from platforms
        if ((Rect.intersects(flummi.getRectTarget(), enemy.getRectTarget())) || (flummi.getRectTarget().top > displayHeight)) {
            Log.d(TAG, "update: game over");
            isGameOver = true;
            // don't call endGame() here! only if the you go back to the main screen
        }

        // win condition
        // if flummi touches the goal
        if (Rect.intersects(flummi.getRectTarget(), goal.getRectTarget())) {
            Log.d(TAG, "update: game won");
            isGameWin = true;
        }

        fire.update(System.currentTimeMillis());

        // gravity simulation
        if (!isJumping && !checkCollision()) {
            flummi.move(0,+200 * deltaTime);
        }
        if (isPressed()) {
            longTouchEvent();
        }

    }

    /**
     * draw the objects created in loadAssets() on the canvas
     * @param canvas which is drawn on
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (canvas != null) {

            bg1.draw(canvas);
            fire.draw(canvas);

            for (DynamicObject d: dynamicObjects) {
                d.draw(canvas);
            }
            for (StaticObject s: staticObjects) {
                s.draw(canvas);
            }

            // draw game won when the game is won
            if (isGameWin){
                gameWinImage.draw(canvas);
            }
            // draw game over when the game is over
            if (isGameOver){
                gameOverImage.draw(canvas);
                gameLoop.setRunning(false);
            }
        }
    }
}
