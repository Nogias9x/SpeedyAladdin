package com.example.n50.speedyaladdin;

import android.app.Application;

import com.example.n50.speedyaladdin.Models.Coordinate;

/**
 * Created by 12124 on 5/3/2017.
 */

public class MyApplication extends Application{
    public Coordinate mAladdinCurrentCoor;
    public Coordinate mObstacle1CurrentCoor;
    public Coordinate mObstacle2CurrentCoor;
    public boolean isPlaying;

    @Override
    public void onCreate() {
        super.onCreate();
        isPlaying = false;
        mAladdinCurrentCoor = new Coordinate();
        mObstacle1CurrentCoor = new Coordinate();
        mObstacle2CurrentCoor = new Coordinate();
    }
}
