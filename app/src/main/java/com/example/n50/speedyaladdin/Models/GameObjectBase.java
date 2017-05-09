package com.example.n50.speedyaladdin.Models;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by 12124 on 4/30/2017.
 */

public abstract class GameObjectBase {
    protected Context mContext;

    protected Bitmap mImage;

    protected final int mRowCount;
    protected final int mColCount;

    protected final int WIDTH;
    protected final int HEIGHT;

    protected final int width;
    protected final int height;

    protected Coordinate mCoor;

    public GameObjectBase(Context context, Bitmap mImage, int mRowCount, int mColCount, int x, int y)  {

        this.mContext = context;
        this.mImage = mImage;
        this.mRowCount = mRowCount;
        this.mColCount = mColCount;

        this.mCoor = new Coordinate(x, y);

        this.WIDTH = mImage.getWidth();
        this.HEIGHT = mImage.getHeight();

        this.width = this.WIDTH/ mColCount;
        this.height = this.HEIGHT/ mRowCount;
    }


    protected Bitmap createSubImageAt(int row, int col)  {
        // createBitmap(bitmap, x, y, width, height).
        Bitmap subImage = Bitmap.createBitmap(mImage, col* width, row* height, width, height);
        return subImage;
    }

    public int getX()  {
        return this.mCoor.mX;
    }

    public int getY()  {
        return this.mCoor.mY;
    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
