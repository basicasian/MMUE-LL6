package at.ac.tuwien.mmue_ll6;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import at.ac.tuwien.mmue_ll6.objects.DynamicObject;
import at.ac.tuwien.mmue_ll6.objects.StaticObject;

/**
 * Help class for GameSurfaceView
 * @author Michelle Lau
 */
public class GameHelper {

    /**
     * gets the size of the display from the window
     * @param context to get the window
     * @return size of the display
     */
    private static Point getDisplaySize(Context context) {
        // get the size of the screen
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        return size;
    }

    /**
     * gets the display width
     * @param context to get the window
     * @param actionBarHeight reduce the display size by the action bar height
     * @return display width
     */
    public static int getDisplayWidth(Context context, int actionBarHeight) {
        Point size = getDisplaySize(context);
        return (size.x - actionBarHeight);
    }

    /**
     * gets the display height
     * @param context to get the window
     * @return display height
     */
    public static int getDisplayHeight(Context context) {
        Point size = getDisplaySize(context);
        return size.y;
    }

    /**
     * generate platforms based on level
     */
    public static ArrayList<DynamicObject> createPlatforms(Context context, int displayHeight, int level) {
        ArrayList<DynamicObject> platformObjects = new ArrayList<>();

        if (level == 1) {
            DynamicObject platform1 = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.platform2), 100, displayHeight - 150);
            DynamicObject platform2 = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.platform2), 1100, displayHeight - 150);
            DynamicObject platform3 = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.platform2), 2000, displayHeight - 300);
            DynamicObject platform4 = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.platform2), 3200, displayHeight - 150);
            DynamicObject platform5 = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.platform2), 3900, displayHeight - 400);
            DynamicObject platform6 = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.platform2), 5000, displayHeight - 300);

            // the order of array is the order of draw calls!
            platformObjects = new ArrayList<>(Arrays.asList(platform1, platform2, platform3, platform4, platform5, platform6));
        }

        if (level == 2) {
            DynamicObject platform1 = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.platform2), 200, displayHeight - 150);
            DynamicObject platform2 = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.platform2), 1100, displayHeight - 100);
            DynamicObject platform3 = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.platform2), 2000, displayHeight - 200);
            DynamicObject platform4 = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.platform2), 3000, displayHeight - 280);
            DynamicObject platform5 = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.platform2), 3800, displayHeight - 400);
            DynamicObject platform6 = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.platform2), 5000, displayHeight - 350);
            DynamicObject platform7 = new DynamicObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.platform2), 5900, displayHeight - 300);

            // the order of array is the order of draw calls!
            platformObjects = new ArrayList<>(Arrays.asList(platform1, platform2, platform3, platform4, platform5, platform6, platform7));
        }
        return platformObjects;
    }

    public static HashMap<String, StaticObject> createStaticObjectsFixed(Context context, int displayHeight, int displayWidth, int padding) {
        StaticObject buttonLeft = new StaticObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrowleft), displayWidth - 600,displayHeight - (int) padding);
        StaticObject buttonRight = new StaticObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrowright), displayWidth - 300,displayHeight - (int) padding);
        StaticObject buttonUp = new StaticObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrowup),  100, displayHeight - (int) padding);
        StaticObject heart1 = new StaticObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.heart), 100, (int) (padding + BitmapFactory.decodeResource(context.getResources(), R.drawable.heart).getHeight()));
        StaticObject heart2 = new StaticObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.heart), 300, (int) (padding + BitmapFactory.decodeResource(context.getResources(), R.drawable.heart).getHeight()));
        StaticObject heart3 = new StaticObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.heart), 500, (int) (padding + BitmapFactory.decodeResource(context.getResources(), R.drawable.heart).getHeight()));

        HashMap<String, StaticObject> staticObjects = new HashMap<>();
        staticObjects.put("buttonLeft", buttonLeft);
        staticObjects.put("buttonRight", buttonRight);
        staticObjects.put("buttonUp", buttonUp);
        staticObjects.put("heart1", heart1);
        staticObjects.put("heart2", heart2);
        staticObjects.put("heart3", heart3);

        return staticObjects;
    }

    public static HashMap<String, StaticObject> createStaticObjectsVariable(Context context, int displayHeight, int displayWidth, int padding) {
        StaticObject pauseButton = new StaticObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.pause), displayWidth - 300, (int) (padding + BitmapFactory.decodeResource(context.getResources(), R.drawable.pause).getHeight()));
        StaticObject playButton = new StaticObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.play), displayWidth - 300, (int) (padding + BitmapFactory.decodeResource(context.getResources(), R.drawable.play).getHeight()));
        StaticObject gameOverImage = new StaticObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.gameover), displayWidth/2 - BitmapFactory.decodeResource(context.getResources(), R.drawable.gameover).getWidth()/2, displayHeight/2 + BitmapFactory.decodeResource(context.getResources(), R.drawable.gameover).getHeight()/2);
        StaticObject gameWinImage = new StaticObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.youwin), displayWidth/2 - BitmapFactory.decodeResource(context.getResources(), R.drawable.youwin).getWidth()/2, displayHeight/2 + BitmapFactory.decodeResource(context.getResources(), R.drawable.youwin).getHeight()/2);
        StaticObject gamePauseImage = new StaticObject(BitmapFactory.decodeResource(context.getResources(), R.drawable.paused), displayWidth/2 - BitmapFactory.decodeResource(context.getResources(), R.drawable.paused).getWidth()/2, displayHeight/2 + BitmapFactory.decodeResource(context.getResources(), R.drawable.paused).getHeight()/2);

        HashMap<String, StaticObject> staticObjects = new HashMap<>();
        staticObjects.put("pauseButton", pauseButton);
        staticObjects.put("playButton", playButton);
        staticObjects.put("gameOverImage", gameOverImage);
        staticObjects.put("gameWinImage", gameWinImage);
        staticObjects.put("gamePauseImage", gamePauseImage);

        return staticObjects;
    }


    /**
     * set text paint
     */
    public static Paint setTextPaint() {
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(60);
        textPaint.setTypeface(Typeface.create("Monospace",Typeface.NORMAL));

        return textPaint;
    }



}