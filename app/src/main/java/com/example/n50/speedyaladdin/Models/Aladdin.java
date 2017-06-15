package com.example.n50.speedyaladdin.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.example.n50.speedyaladdin.Constant;
import com.example.n50.speedyaladdin.MyApplication;

/**
 * Created by 12124 on 4/30/2017.
 */

public class Aladdin extends GameObjectBase {
    private long mLastDrawNanoTime = -1;
    private int mColUsing;
    private Bitmap[] mBottomToTops;
    private Bitmap[] mTopToBottoms;
    private Bitmap[] mStandStills;

    private GameSurface mGameSurface;

    private static final int ROW_BOTTOM_TO_TOP = 0; // sprite khi bay lên
    private static final int ROW_TOP_TO_BOTTOM = 1; // sprite khi rớt xuống
    private static final int ROW_STAND_STILL = 2;  // sprite khi đứng yên
    private int mRowUsing = ROW_STAND_STILL; // dòng ảnh đang được sử dụng.

    private int mYPostionWhenTap; // vị trí của Aladdin ngay khi người chơi tap
    private int mFlyingDistanceEchTap = 200; // mỗi lần người chơi tap, Aladdin bay lên 200px

    //khi bắt đầu thì Aladdin đứng yên
    private int mMovingVectorX = 0;
    private int mMovingVectorY = 0;

    // padding của Aladdin (phần padding có chạm obstacle thì cũng không thua)
    public static int mPaddingHor;
    public static int mPaddingVer;

    public Aladdin(Context context, GameSurface mGameSurface, Bitmap image, int x, int y) {
        super(context, image, 3, 16, x, y); // ảnh sprite sử dụng có 3 dòng, 16 cột
        this.mGameSurface = mGameSurface;

        // xác định padding
        mPaddingVer = (int) (0.1 * getHeight());
        mPaddingHor = (int) (0.1 * getWidth());

        // cắt lấy các dòng ảnh từ ảnh lớn
        this.mTopToBottoms = new Bitmap[mColCount];
        this.mBottomToTops = new Bitmap[mColCount];
        this.mStandStills = new Bitmap[mColCount];
        for (int col = 0; col < this.mColCount; col++) {
            this.mTopToBottoms[col] = this.createSubImageAt(ROW_TOP_TO_BOTTOM, col);
            this.mBottomToTops[col] = this.createSubImageAt(ROW_BOTTOM_TO_TOP, col);
            this.mStandStills[col] = this.createSubImageAt(ROW_STAND_STILL, col);
        }
    }

    // lấy dòng ảnh đang sử dụng
    public Bitmap[] getMoveBitmaps() {
        switch (mRowUsing) {
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

    // lấy chính xác ảnh đang sử dụng trong dòng ảnh
    public Bitmap getCurrentMoveBitmap() {
        Bitmap[] bitmaps = this.getMoveBitmaps();
        return bitmaps[this.mColUsing];
    }

    // update đồ hoạ
    public void update() {
        // chuyển hình
        this.mColUsing++;
        if (mColUsing >= this.mColCount) {
            this.mColUsing = 0;
        }

        //>>> tính toán các giá trị vị trí của Aladdin
        // thời điểm hiện tại theo nano giây.
        long now = System.nanoTime();
        // chưa vẽ lần nào.
        if (mLastDrawNanoTime == -1) {
            mLastDrawNanoTime = now;
        }
        // đổi nano giây ra mili giây (1 nanosecond = 1000000 millisecond).
        int deltaTime = (int) ((now - mLastDrawNanoTime) / 1000000);
        // quãng đường mà nhân vật đi được (fixel).
        float distance = Constant.ALADDIN_VELOCITY * deltaTime;
        double movingVectorLength = Math.sqrt(mMovingVectorX * mMovingVectorX + mMovingVectorY * mMovingVectorY);
        // tính toán vị trí mới của nhân vật.
        this.mCoor.mX = this.mCoor.mX + (int) (distance * mMovingVectorX / movingVectorLength);
        this.mCoor.mY = mCoor.mY + (int) (distance * mMovingVectorY / movingVectorLength);
        //<<< tính toán các giá trị vị trí của Aladdin

        // nếu Aladdin bay đụng nóc thì rớt xuống
        if (this.mCoor.mY < 0) {
            this.mCoor.mY = 0;
            setMovingVectorForFlying(false);
        }

        // nếu Aladdin bay quá distance của mỗi tap thì rơi xuống
        if (this.mCoor.mY < this.mYPostionWhenTap - this.mFlyingDistanceEchTap) {
            setMovingVectorForFlying(false);
        }

        // nếu Aladdin rớt xuống đất là thua
        if (this.mCoor.mY > this.mGameSurface.getHeight() - height) {
            endGame();
        }

        //>>> nếu Aladdin đụng obstacle thì thua
        Obstacle obst1, obst2;
        obst1 = ((MyApplication) mContext.getApplicationContext()).mObstacle1Current;
        obst2 = ((MyApplication) mContext.getApplicationContext()).mObstacle2Current;
        Boolean isTouching;
        if (obst1.mCoor.mX < 0)
            isTouching = isAladdinTouching(obst2);      // trên màn hình chỉ có obst2: ||||  obst2   ||||
        else if (obst2.mCoor.mX < 0)
            isTouching = isAladdinTouching(obst1); // trên màn hình chỉ có obst1: ||||  obst1   ||||
        else {
            if (obst1.mCoor.mX < obst2.mCoor.mX) { // obst1 nằm trước obst2: |||| obst1    obst2 ||||
                isTouching = isAladdinTouching(obst1);
            } else { // obst2 nằm trước obst1:  |||| obst2    obst1 ||||
                isTouching = isAladdinTouching(obst2);
            }
        }
        if (isTouching){
            endGame();
        }
        //<<< nếu Aladdin đụng obstacle thì thua
    }

    public void draw(Canvas canvas) {
        Bitmap bitmap = this.getCurrentMoveBitmap();
        canvas.drawBitmap(bitmap, this.mCoor.mX, this.mCoor.mY, null);
        this.mLastDrawNanoTime = System.nanoTime();
    }

    public void setMovingVectorForFlying(boolean isUp) {
        if (isUp) { // Aladdin đang bay lên
            this.mYPostionWhenTap = this.mCoor.mY;
            this.mRowUsing = ROW_BOTTOM_TO_TOP;
            this.mMovingVectorX = 0;
            this.mMovingVectorY = -2;
        } else { // Aladdin đang rơi xuống
            this.mRowUsing = ROW_TOP_TO_BOTTOM;
            this.mMovingVectorX = 0;
            this.mMovingVectorY = 2;
        }
    }

    public boolean isAladdinTouching(Obstacle obst) { // check Aladdin chạm obstacle
        Coordinate aladdinNose = new Coordinate(this.mCoor.mX + this.width, this.mCoor.mY);
        if (aladdinNose.mX - mPaddingHor >= obst.mCoor.mX + Obstacle.mPaddingHor && this.mCoor.mX + mPaddingHor <= obst.mCoor.mX + obst.getWidth() - Obstacle.mPaddingHor // Aladdin bay tới obstacle
                && (aladdinNose.mY + mPaddingVer < obst.mCoor.mY + obst.getHeight() - Obstacle.mPaddingVer // đầu Aladdin chạm wand
                || aladdinNose.mY + this.height - mPaddingVer > obst.mCoor.mY + obst.getHeight() + Obstacle.mPaddingVer + Constant.DISTANCE_BOTTOM_TOP_OBSTACLE)) // chân Aladdin chạm vào tower
        {
            Log.d("TOUCH", "=====================================");

            Log.d("TOUCH", "Aaladdin: x= "+ this.mCoor.mX + ", y= "+ this.mCoor.mY);
            Log.d("TOUCH", "AmPaddingHor: " + mPaddingHor + "; mPaddingVer" + mPaddingVer);
            Log.d("TOUCH", "Awidth: " + this.width + "; height: " + this.height);

            Log.d("TOUCH", "obst.mCoor: x= "+ obst.mCoor.mX + ", y= "+obst.mCoor.mY);
            Log.d("TOUCH", "obst.mPaddingHor: " + Obstacle.mPaddingHor + "; obst.mPaddingVer" + Obstacle.mPaddingVer);
            Log.d("TOUCH", "Oobst.getWidth(): " + obst.getWidth() + "; obst.getHeight(): " + obst.getHeight());





            return true;
        }
        return false;
    }

    // game over
    public void endGame() {
        // save best score
        MyApplication myApplication = ((MyApplication) mContext.getApplicationContext());
        if (myApplication.mScore > myApplication.mBestScore) {
            SharedPreferences.Editor editor = mContext.getSharedPreferences(Constant.MY_PREFS, mContext.MODE_PRIVATE).edit();
            editor.putInt(Constant.PREF_BEST_SCORE, myApplication.mScore);
            editor.commit();
        }
        myApplication.mIsEndGame = true;
    }
}