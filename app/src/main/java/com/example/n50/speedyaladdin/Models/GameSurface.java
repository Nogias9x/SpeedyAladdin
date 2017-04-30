package com.example.n50.speedyaladdin.Models;

/**
 * Created by 12124 on 4/30/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.n50.speedyaladdin.R;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {


    private GameThread gameThread;

    private Bitmap mScaledBackground;

    private Aladdin chibi1;

    public GameSurface(Context context)  {
        super(context);


        // Đảm bảo Game Surface có thể focus để điều khiển các sự kiện.
        this.setFocusable(true);


        // Sét đặt các sự kiện liên quan tới Game.
        this.getHolder().addCallback(this);
    }

    public void update()  {
        this.chibi1.update();
    }



    @Override
    public void draw(Canvas canvas)  {
        super.draw(canvas);

        canvas.drawBitmap(mScaledBackground, 0, 0, null); // draw the background
        this.chibi1.draw(canvas);
    }


    // Thi hành phương thức của interface SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //background
        Bitmap backgroundRaw = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        float scale = (float)backgroundRaw.getHeight()/(float)getHeight();
        int newWidth = Math.round(backgroundRaw.getWidth()/scale);
        int newHeight = Math.round(backgroundRaw.getHeight()/scale);
        mScaledBackground = Bitmap.createScaledBitmap(backgroundRaw, newWidth, newHeight, true);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int x = displayMetrics.widthPixels/6;
        int y = displayMetrics.heightPixels/2;



        Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.sprite_aladin_flying);
        this.chibi1 = new Aladdin(this, chibiBitmap1, x, y, displayMetrics.widthPixels, displayMetrics.heightPixels);

        this.gameThread = new GameThread(this,holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    // Thi hành phương thức của interface SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }


    // Thi hành phương thức của interface SurfaceHolder.Callback
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry= true;
        while(retry) {
            try {
                this.gameThread.setRunning(false);


                // Luồng cha, cần phải tạm dừng chờ GameThread kết thúc.
                this.gameThread.join();
            }catch(InterruptedException e)  {
                e.printStackTrace();
            }
            retry= true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x=  (int)event.getX();
            int y = (int)event.getY();

//            int movingVectorX =x-  this.chibi1.getX() ;
//            int movingVectorY =y-  this.chibi1.getY() ;

//            this.chibi1.setMovingVector(movingVectorX,movingVectorY);
            this.chibi1.setMovingVectorForFlying(true);//fly up
            return true;
        }
        return false;
    }
}
