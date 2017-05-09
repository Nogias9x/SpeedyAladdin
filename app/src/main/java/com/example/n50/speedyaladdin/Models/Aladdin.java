package com.example.n50.speedyaladdin.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.example.n50.speedyaladdin.Constant;
import com.example.n50.speedyaladdin.MyApplication;

/**
 * Created by 12124 on 4/30/2017.
 */

public class Aladdin extends GameObjectBase {
    private static final int ROW_BOTTOM_TO_TOP = 0;
    private static final int ROW_TOP_TO_BOTTOM = 1;
    private static final int ROW_STAND_STILL = 2;

    public static int mPaddingHor;
    public static int mPaddingVer;

    // Dòng ảnh đang được sử dụng.
    private int mRowUsing = ROW_STAND_STILL;

    private int mColUsing;

    private Bitmap[] mBottomToTops;
    private Bitmap[] mTopToBottoms;
    private Bitmap[] mStandStills;

    private int mYPostionWhenTap;
    private int mFlyingDistanceEchTap = 200;

    //Aladdin stands still at the beginning
    private int mMovingVectorX = 0;//10;
    private int mMovingVectorY = 0;//5;

    private long mLastDrawNanoTime =-1;

    private GameSurface mGameSurface;



    public Aladdin(Context context, GameSurface mGameSurface, Bitmap image, int x, int y) {

        super(context, image, 3, 16, x, y);

        mPaddingVer = (int)(0.1* getHeight());
        mPaddingHor = (int)(0.1* getWidth());


        this.mGameSurface = mGameSurface;

        this.mTopToBottoms = new Bitmap[mColCount]; // 3
        this.mBottomToTops = new Bitmap[mColCount]; // 3
        this.mStandStills = new Bitmap[mColCount]; // 3

        for(int col = 0; col< this.mColCount; col++ ) {
            this.mTopToBottoms[col] = this.createSubImageAt(ROW_TOP_TO_BOTTOM, col);
//            this.rightToLefts[col]  = this.createSubImageAt(ROW_RIGHT_TO_LEFT, col);
            this.mBottomToTops[col] = this.createSubImageAt(ROW_BOTTOM_TO_TOP, col);
            this.mStandStills[col]  = this.createSubImageAt(ROW_STAND_STILL, col);
        }
    }

    public Bitmap[] getMoveBitmaps()  {
        switch (mRowUsing)  {
//            case ROW_BOTTOM_TO_TOP:
//                return  this.mBottomToTops;
            case ROW_BOTTOM_TO_TOP:
                return this.mBottomToTops;
            case ROW_TOP_TO_BOTTOM:
                return this.mTopToBottoms;
            case ROW_STAND_STILL:
                return this.mStandStills;
            default:
                return null;
        }
    }

    public Bitmap getCurrentMoveBitmap()  {
        Bitmap[] bitmaps = this.getMoveBitmaps();
        return bitmaps[this.mColUsing];
    }


    public void update()  {
        if(((MyApplication)mContext.getApplicationContext()).isEndGame == true) {
            this.mCoor.mX = mGameSurface.getWidth();
        } else{
            this.mColUsing++;
            if(mColUsing >= this.mColCount)  {
                this.mColUsing =0;
            }

            // Thời điểm hiện tại theo nano giây.
            long now = System.nanoTime();


            // Chưa vẽ lần nào.
            if(mLastDrawNanoTime ==-1) {
                mLastDrawNanoTime = now;
            }

            // Đổi nano giây ra mili giây (1 nanosecond = 1000000 millisecond).
            int deltaTime = (int) ((now - mLastDrawNanoTime)/ 1000000 );


//        // Quãng đường mà nhân vật đi được (fixel).
            float distance = Constant.ALADDIN_VELOCITY * deltaTime;
//
            double movingVectorLength = Math.sqrt(mMovingVectorX * mMovingVectorX + mMovingVectorY * mMovingVectorY);
//
//
//        // Tính toán vị trí mới của nhân vật.
            this.mCoor.mX = this.mCoor.mX +  (int)(distance* mMovingVectorX / movingVectorLength);
            this.mCoor.mY = mCoor.mY +  (int)(distance* mMovingVectorY / movingVectorLength);

//
            //bay đụng nóc thì rớt xuống
            if(this.mCoor.mY < 0){
                this.mCoor.mY = 0;
                setMovingVectorForFlying(false);
            }

            //khi bay quá distance thì rơi xuống
            if(this.mCoor.mY < this.mYPostionWhenTap - this.mFlyingDistanceEchTap){
                setMovingVectorForFlying(false);
            }

            //rớt xuống đất là thua
            if(this.mCoor.mY > this.mGameSurface.getHeight()- height){
                this.mMovingVectorX = 0;
                this.mMovingVectorY = 0;
                endGame();
            }

            //todo: đụng obstacle thì thua
            Obstacle obst1, obst2;
            obst1 = ((MyApplication)mContext.getApplicationContext()).mObstacle1Current;
            obst2 = ((MyApplication)mContext.getApplicationContext()).mObstacle2Current;
            Boolean isTouching;

            if(obst1.mCoor.mX < 0) isTouching= isAladdinTouching(obst2);      // ||||  ob2   ||||
            else if(obst2.mCoor.mX < 0) isTouching= isAladdinTouching(obst1); // ||||  ob1   ||||
            else {
                if(obst1.mCoor.mX < obst2.mCoor.mX){ // |||| ob1    ob2 ||||
                    isTouching= isAladdinTouching(obst1);
                } else{ //  |||| ob2    ob1 ||||
                    isTouching= isAladdinTouching(obst2);
                }
            }

            if(isTouching) endGame();
        }


    }

    public void draw(Canvas canvas)  {
        Bitmap bitmap = this.getCurrentMoveBitmap();
        canvas.drawBitmap(bitmap, this.mCoor.mX, this.mCoor.mY, null);

        // Thời điểm vẽ cuối cùng (Nano giây).
        this.mLastDrawNanoTime = System.nanoTime();
    }

    public void setMovingVector(int movingVectorX, int movingVectorY)  {
        this.mMovingVectorX = movingVectorX;
        this.mMovingVectorY = movingVectorY;
    }

    public void setMovingVectorForFlying(boolean isUp)  {
        if(isUp){
            this.mYPostionWhenTap = this.mCoor.mY;
            this.mRowUsing = ROW_BOTTOM_TO_TOP;
            this.mMovingVectorX = 0;
            this.mMovingVectorY = -2;
        } else {
            this.mRowUsing = ROW_TOP_TO_BOTTOM;
            this.mMovingVectorX = 0;
            this.mMovingVectorY = 2;
        }

    }


    public boolean isAladdinTouching(Obstacle obst){
        Coordinate aladdinNose =  new Coordinate(this.mCoor.mX + this.width, this.mCoor.mY);
        if( aladdinNose.mX - mPaddingHor >= obst.mCoor.mX && this.mCoor.mX + mPaddingHor <= obst.mCoor.mX + obst.getWidth() //Aladdin bay tới obstacle
            && (aladdinNose.mY + mPaddingVer < obst.mCoor.mY + obst.getHeight() // đầu Aladdin chạm wand
                || aladdinNose.mY + this.height - mPaddingVer > obst.mCoor.mY + obst.getHeight() + Constant.DISTANCE_BOTTOM_TOP_OBSTACLE) // chân Aladdin chạm vào tower
           ) {
            Log.d("TOUCH", "");

            return true;
        }
        return false;
    }
    public void endGame(){
        ((MyApplication)mContext.getApplicationContext()).isEndGame = true;
    }
}