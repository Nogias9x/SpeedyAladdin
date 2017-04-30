package com.example.n50.speedyaladdin.Models;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by 12124 on 4/30/2017.
 */

public abstract class GameObjectBase {
    protected Bitmap image;

    protected final int rowCount;
    protected final int colCount;

    protected final int WIDTH;
    protected final int HEIGHT;

    protected final int width;


    protected final int height;
    protected int x;
    protected int y;

    protected int SCREEN_WIDTH;
    protected int SCREEN_HEIGHT;


    public void setScreenSize(int w, int h){
        this.SCREEN_WIDTH =  w;
        this.SCREEN_HEIGHT =  h;
        Log.d("NOGIAS","SCREEN_WIDTH: " + w);
        Log.d("NOGIAS","SCREEN_HEIGHT: " + h);
    }

    public GameObjectBase(Bitmap image, int rowCount, int colCount, int x, int y)  {

        this.image = image;
        this.rowCount= rowCount;
        this.colCount= colCount;

        this.x= x;
        this.y= y;

        this.WIDTH = image.getWidth();
        this.HEIGHT = image.getHeight();

        this.width = this.WIDTH/ colCount;
        this.height = this.HEIGHT/ rowCount;
    }


    protected Bitmap createSubImageAt(int row, int col)  {
        // createBitmap(bitmap, x, y, width, height).
        Bitmap subImage = Bitmap.createBitmap(image, col* width, row* height, width, height);
        return subImage;
    }

    public int getX()  {
        return this.x;
    }

    public int getY()  {
        return this.y;
    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}