package com.example.n50.speedyaladdin.Models;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by 12124 on 4/30/2017.
 */

public abstract class GameObjectBase {
    protected final int mRowCount;
    protected final int mColCount;
    protected final int WIDTH;
    protected final int HEIGHT;
    protected final int width;
    protected final int height;
    protected Context mContext;
    protected Bitmap mImage;
    protected Coordinate mCoor;

    public GameObjectBase(Context context, Bitmap mImage, int mRowCount, int mColCount, int x, int y) {
        this.mContext = context;
        this.mImage = mImage;
        this.mRowCount = mRowCount;
        this.mColCount = mColCount;

        this.mCoor = new Coordinate(x, y);

        this.WIDTH = mImage.getWidth();
        this.HEIGHT = mImage.getHeight();

        // kích thước hiển thị của nhân vật trên màn hình
        this.width = this.WIDTH / mColCount;
        this.height = this.HEIGHT / mRowCount;
    }


    // lấy 1 dòng ảnh từ ảnh lớn
    protected Bitmap createSubImageAt(int row, int col) {
        Bitmap subImage = Bitmap.createBitmap(mImage, col * width, row * height, width, height);
        return subImage;
    }

    // lấy kích thước hiển thị của nhân vật trên màn hình
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
}
