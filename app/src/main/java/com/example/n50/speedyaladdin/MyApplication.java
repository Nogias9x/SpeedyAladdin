package com.example.n50.speedyaladdin;

import android.app.Application;

import com.example.n50.speedyaladdin.Models.Coordinate;
import com.example.n50.speedyaladdin.Models.Obstacle;

/**
 * Created by 12124 on 5/3/2017.
 */

public class MyApplication extends Application{
    public Coordinate mAladdinCurrentCoor;
    public Obstacle mObstacle1Current;
    public Obstacle mObstacle2Current;
    public boolean isPlaying;

    @Override
    public void onCreate() {
        super.onCreate();
        isPlaying = false;
        mAladdinCurrentCoor = new Coordinate();
//        mObstacle1Current = new Obstacle();
//        mObstacle2Current = new Obstacle();
    }
}
