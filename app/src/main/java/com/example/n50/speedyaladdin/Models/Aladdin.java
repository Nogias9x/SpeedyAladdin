package com.example.n50.speedyaladdin.Models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by 12124 on 4/30/2017.
 */

public class Aladdin extends GameObjectBase {
    private static final int ROW_BOTTOM_TO_TOP = 0;
    private static final int ROW_TOP_TO_BOTTOM = 1;
    private static final int ROW_STAND_STILL = 2;

    // Dòng ảnh đang được sử dụng.
    private int rowUsing = ROW_STAND_STILL;

    private int colUsing;

    private Bitmap[] bottomToTops;
    private Bitmap[] topToBottoms;
    private Bitmap[] standStills;

    private int yPostionWhenTap;
    private int flyingDistanceEchTap = 200;


    // Vận tốc di chuyển của nhân vật (pixel/milisecond).
    public static final float VELOCITY = 0.4f;

    //Aladdin stands still at the beginning
    private int movingVectorX = 0;//10;
    private int movingVectorY = 0;//5;

    private long lastDrawNanoTime =-1;

    private GameSurface gameSurface;



    public Aladdin(GameSurface gameSurface, Bitmap image, int x, int y) {

        super(image, 3, 16, x, y);



        this.gameSurface= gameSurface;

        this.topToBottoms = new Bitmap[colCount]; // 3
//        this.rightToLefts = new Bitmap[colCount]; // 3
        this.bottomToTops = new Bitmap[colCount]; // 3
        this.standStills = new Bitmap[colCount]; // 3

        for(int col = 0; col< this.colCount; col++ ) {
            this.topToBottoms[col] = this.createSubImageAt(ROW_TOP_TO_BOTTOM, col);
//            this.rightToLefts[col]  = this.createSubImageAt(ROW_RIGHT_TO_LEFT, col);
            this.bottomToTops[col] = this.createSubImageAt(ROW_BOTTOM_TO_TOP, col);
            this.standStills[col]  = this.createSubImageAt(ROW_STAND_STILL, col);
        }
    }

    public Bitmap[] getMoveBitmaps()  {
        switch (rowUsing)  {
//            case ROW_BOTTOM_TO_TOP:
//                return  this.bottomToTops;
            case ROW_BOTTOM_TO_TOP:
                return this.bottomToTops;
            case ROW_TOP_TO_BOTTOM:
                return this.topToBottoms;
            case ROW_STAND_STILL:
                return this.standStills;
            default:
                return null;
        }
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
        this.mCoor.mY = mCoor.mY +  (int)(distance* movingVectorY / movingVectorLength);
//
//
        //bay đụng nóc thì rớt xuống
        if(this.mCoor.mY < 0){
            this.mCoor.mY = 0;
            setMovingVectorForFlying(false);
        }

        //khi bay quá distance thì rơi xuống
        if(this.mCoor.mY < this.yPostionWhenTap - this.flyingDistanceEchTap){
            setMovingVectorForFlying(false);
        }

        //rớt xuống đất là thua
        if(this.mCoor.mY > this.gameSurface.getHeight()- height){
            this.movingVectorX= 0;
            this.movingVectorY = 0;
        }

//        // Khi nhân vật của game chạm vào cạnh của màn hình thì đổi hướng.
//
//        if(this.x < 0 )  {
//            this.x = 0;
//            this.movingVectorX = - this.movingVectorX;
//        } else if(this.x > this.gameSurface.getWidth() -width)  {
//            this.x= this.gameSurface.getWidth()-width;
//            this.movingVectorX = - this.movingVectorX;
//        }
//
//        if(this.y < 0 )  {
//            this.y = 0;
//            this.movingVectorY = - this.movingVectorY;
//        } else if(this.y > this.gameSurface.getHeight()- height)  {
//            this.y= this.gameSurface.getHeight()- height;
//            this.movingVectorY = - this.movingVectorY ;
//        }


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

    public void setMovingVector(int movingVectorX, int movingVectorY)  {
        this.movingVectorX= movingVectorX;
        this.movingVectorY = movingVectorY;
    }

    public void setMovingVectorForFlying(boolean isUp)  {
        if(isUp){
            this.yPostionWhenTap = this.mCoor.mY;
            this.rowUsing = ROW_BOTTOM_TO_TOP;
            this.movingVectorX= 0;
            this.movingVectorY = -1;
        } else {
            this.rowUsing = ROW_TOP_TO_BOTTOM;
            this.movingVectorX= 0;
            this.movingVectorY = 1;
        }

    }
}