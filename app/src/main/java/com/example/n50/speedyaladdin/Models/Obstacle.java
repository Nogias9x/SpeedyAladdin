package com.example.n50.speedyaladdin.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.example.n50.speedyaladdin.Constant;
import com.example.n50.speedyaladdin.MyApplication;

import java.util.Random;

/**
 * Created by 12124 on 5/02/2017.
 */

public class Obstacle extends GameObjectBase {

    Random mRandSeed = new Random();

    private int mID;
    private Constant.ObstacleType mObstacleType;

    private int mVisibleHeight = 4;


    private int mColUsing;

    private Bitmap[] mObstacleImage;

    //Aladdin stands still at the beginning
    private int mMovingVectorX = 0;
    private int mMovingVectorY = 0;

    private long mLastDrawNanoTime =-1;

    private GameSurface mGameSurface;

    public void setVisibleHeight() {
        if(this.mObstacleType == Constant.ObstacleType.WAND){
            this.mVisibleHeight = (mRandSeed.nextInt(Constant.VISIBLE_HEIGHT_MAX) + Constant.VISIBLE_HEIGHT_MIN);
            this.mCoor.mY = 100*this.mVisibleHeight - this.height;
        } else { //TOWER
            this.mCoor.mY = 0; //toto: sua theo tower
        }

    }

    public Obstacle(Context context, GameSurface mGameSurface, Constant.ObstacleType obstacleType, int id, Bitmap image, int x, int y) {

        super(context, image, 1, 1, x, y);
        this.mGameSurface = mGameSurface;

        this.mObstacleType = obstacleType;
        this.mID = id;

        setVisibleHeight();
        this.mCoor.mX = x;




        this.mObstacleImage = new Bitmap[mColCount]; // 3

        for(int col = 0; col< this.mColCount; col++ ) {
            this.mObstacleImage[col]  = image;
        }

        setMovingVectorForObstacle();
    }

    public Bitmap[] getMoveBitmaps()  {
        return this.mObstacleImage;
    }

    public Bitmap getCurrentMoveBitmap()  {
        Bitmap[] bitmaps = this.getMoveBitmaps();
        return bitmaps[this.mColUsing];
    }


    public void update()  {
        if(((MyApplication)mContext.getApplicationContext()).isEndGame == true) {
        } else {
            if (((MyApplication) mContext.getApplicationContext()).isPlaying == true) {
                this.mColUsing++;
                if (mColUsing >= this.mColCount) {
                    this.mColUsing = 0;
                }

                // Thời điểm hiện tại theo nano giây.
                long now = System.nanoTime();


                // Chưa vẽ lần nào.
                if (mLastDrawNanoTime == -1) {
                    mLastDrawNanoTime = now;
                }

                // Đổi nano giây ra mili giây (1 nanosecond = 1000000 millisecond).
                int deltaTime = (int) ((now - mLastDrawNanoTime) / 1000000);

                if (this.mObstacleType == Constant.ObstacleType.WAND) {
                    //        // Quãng đường mà nhân vật đi được (fixel).
                    float distance = Constant.OBSTACLE_VELOCITY * deltaTime;
//
                    double movingVectorLength = Math.sqrt(mMovingVectorX * mMovingVectorX + mMovingVectorY * mMovingVectorY);
//
//
//        // Tính toán vị trí mới của nhân vật.
                    this.mCoor.mX = this.mCoor.mX + (int) (distance * mMovingVectorX / movingVectorLength);
                    this.mCoor.mY = this.mCoor.mY + (int) (distance * mMovingVectorY / movingVectorLength);


                    //đụng vách trái thì trờ lại vách phải
                    if (this.mCoor.mX + this.width < 0) {
                        Coordinate otherObstacleCoor;
                        if (this.mID == 1) { // obstacle 1
                            otherObstacleCoor = ((MyApplication) this.mContext.getApplicationContext()).mObstacle2Current.mCoor;
                        } else { // obstacle 2
                            otherObstacleCoor = ((MyApplication) this.mContext.getApplicationContext()).mObstacle1Current.mCoor;
                        }

                        if (otherObstacleCoor.mX <= mGameSurface.getWidth() / 2) {
                            setVisibleHeight();
                            this.mCoor.mX = this.mGameSurface.getWidth();
                        }
                    }

                } else { // TOWER
                    Obstacle wandObstacle;
                    if (this.mID == 1) { // obstacle 1
                        wandObstacle = ((MyApplication) this.mContext.getApplicationContext()).mObstacle1Current;
                    } else { // obstacle 2
                        wandObstacle = ((MyApplication) this.mContext.getApplicationContext()).mObstacle2Current;
                    }
                    // Tính toán vị trí mới của nhân vật.
                    this.mCoor.mX = wandObstacle.mCoor.mX;
                    this.mCoor.mY = wandObstacle.mCoor.mY + wandObstacle.getHeight() + Constant.DISTANCE_BOTTOM_TOP_OBSTACLE;// - Constant.DISTANCE_BOTTOM_TOP_OBSTACLE - this.height;

                }

//        //di chuyển từ phải qua trái
//        setMovingVectorForObstacle();
//
//
//        //bay đụng nóc thì rớt xuống
//        if(this.y < 0){
//            setMovingVectorForFlying(false);
//        }
//
//        //khi bay quá distance thì rơi xuống
//        if(this.y < this.yPostionWhenTap - this.flyingDistanceEchTap){
//            setMovingVectorForFlying(false);
//        }
//
//        //rớt xuống đất là thua
//        if(this.y > this.mGameSurface.getHeight()- height){
//            this.mMovingVectorX= 0;
//            this.mMovingVectorY = 0;
//        }
            }
        }
    }

    public void draw(Canvas canvas)  {
        Bitmap bitmap = this.getCurrentMoveBitmap();
        canvas.drawBitmap(bitmap, this.mCoor.mX, this.mCoor.mY, null);

        // Thời điểm vẽ cuối cùng (Nano giây).
        this.mLastDrawNanoTime = System.nanoTime();
    }

    public void setMovingVectorForObstacle()  {
        this.mMovingVectorX = -1;
        this.mMovingVectorY = 0;
    }

    public void setMovingVectorForFlying(boolean isUp)  {
//        if(isUp){
//            this.yPostionWhenTap = this.y;
//            this.rowUsing = ROW_BOTTOM_TO_TOP;
//            this.mMovingVectorX= 0;
//            this.mMovingVectorY = -1;
//        } else {
//            this.rowUsing = ROW_TOP_TO_BOTTOM;
//            this.mMovingVectorX= 0;
//            this.mMovingVectorY = 1;
//        }

    }
}