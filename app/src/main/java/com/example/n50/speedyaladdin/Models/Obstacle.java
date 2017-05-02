package com.example.n50.speedyaladdin.Models;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by 12124 on 5/02/2017.
 */

public class Obstacle extends GameObjectBase {

    Random rand = new Random();

    private int visibleHeight = 4;
    private int visibleHeightMax = 10;
    private int visibleHeightMin = 4;

    private int colUsing;

    private Bitmap[] obstacleImage;

    public void setVisibleHeight() {
        this.visibleHeight = (rand.nextInt(visibleHeightMax) + visibleHeightMin);
        this.mCoor.mY = this.gameSurface.getHeight() - 100*this.visibleHeight;
    }

    // Vận tốc di chuyển của nhân vật (pixel/milisecond).
    public static final float VELOCITY = 0.3f;

    //Aladdin stands still at the beginning
    private int movingVectorX = 0;//10;
    private int movingVectorY = 0;//5;

    private long lastDrawNanoTime =-1;

    private GameSurface gameSurface;



    public Obstacle(GameSurface gameSurface, Bitmap image, int x, int y) {

        super(image, 1, 1, x, y);
        this.gameSurface= gameSurface;

        setVisibleHeight();
        this.mCoor.mX = x;




        this.obstacleImage = new Bitmap[colCount]; // 3

        for(int col = 0; col< this.colCount; col++ ) {
            this.obstacleImage[col]  = image;
        }

        setMovingVectorForObstacle();
    }

    public Bitmap[] getMoveBitmaps()  {
        return this.obstacleImage;
    }

    public Bitmap getCurrentMoveBitmap()  {
        Bitmap[] bitmaps = this.getMoveBitmaps();
        return bitmaps[this.colUsing];
    }


    public void update()  {
        this.colUsing++;
        if(colUsing >= this.colCount)  {
            this.colUsing =0;
        }

        // Thời điểm hiện tại theo nano giây.
        long now = System.nanoTime();


        // Chưa vẽ lần nào.
        if(lastDrawNanoTime==-1) {
            lastDrawNanoTime= now;
        }

        // Đổi nano giây ra mili giây (1 nanosecond = 1000000 millisecond).
        int deltaTime = (int) ((now - lastDrawNanoTime)/ 1000000 );


//        // Quãng đường mà nhân vật đi được (fixel).
        float distance = VELOCITY * deltaTime;
//
        double movingVectorLength = Math.sqrt(movingVectorX* movingVectorX + movingVectorY*movingVectorY);
//
//
//        // Tính toán vị trí mới của nhân vật.
        this.mCoor.mX = this.mCoor.mX +  (int)(distance* movingVectorX / movingVectorLength);
        this.mCoor.mY = this.mCoor.mY +  (int)(distance* movingVectorY / movingVectorLength);


        //đụng vách trái thì trờ lại vách phải
        if(this.mCoor.mX + this.width < 0){


            setVisibleHeight();
            this.mCoor.mX = this.gameSurface.getWidth();

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
//        Log.d("NOGIAS","Image.Y:" + this.height + "; Aladdin Y: " + this.y);
//        if(this.y > this.gameSurface.getHeight()- height){
//            this.movingVectorX= 0;
//            this.movingVectorY = 0;
//        }

//


//        // Tính toán rowUsing.
//        if( movingVectorX > 0 )  {
////            if(movingVectorY > 0 && Math.abs(movingVectorX) < Math.abs(movingVectorY)) {
////                this.rowUsing = ROW_TOP_TO_BOTTOM;
////            }else if(movingVectorY < 0 && Math.abs(movingVectorX) < Math.abs(movingVectorY)) {
////                this.rowUsing = ROW_BOTTOM_TO_TOP;
////            }else  {
//                this.rowUsing = ROW_BOTTOM_TO_TOP;
////            }
//        } else {
////            if(movingVectorY > 0 && Math.abs(movingVectorX) < Math.abs(movingVectorY)) {
////                this.rowUsing = ROW_TOP_TO_BOTTOM;
////            }else if(movingVectorY < 0 && Math.abs(movingVectorX) < Math.abs(movingVectorY)) {
////                this.rowUsing = ROW_BOTTOM_TO_TOP;
////            }else  {
////                this.rowUsing = ROW_RIGHT_TO_LEFT;
////            }
//        }
    }

    public void draw(Canvas canvas)  {
        Bitmap bitmap = this.getCurrentMoveBitmap();
        canvas.drawBitmap(bitmap, this.mCoor.mX, this.mCoor.mY, null);

        // Thời điểm vẽ cuối cùng (Nano giây).
        this.lastDrawNanoTime= System.nanoTime();
    }

    public void setMovingVectorForObstacle()  {
        this.movingVectorX= -1;
        this.movingVectorY = 0;
    }

    public void setMovingVectorForFlying(boolean isUp)  {
//        if(isUp){
//            this.yPostionWhenTap = this.y;
//            this.rowUsing = ROW_BOTTOM_TO_TOP;
//            this.movingVectorX= 0;
//            this.movingVectorY = -1;
//        } else {
//            this.rowUsing = ROW_TOP_TO_BOTTOM;
//            this.movingVectorX= 0;
//            this.movingVectorY = 1;
//        }

    }
}