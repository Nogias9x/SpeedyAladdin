package com.example.n50.speedyaladdin;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.n50.speedyaladdin.Models.Coordinate;
import com.example.n50.speedyaladdin.Models.Obstacle;

/**
 * Created by 12124 on 5/3/2017.
 */

public class MyApplication extends Application{
    public Coordinate mAladdinCurrentCoor;
    public Obstacle mObstacle1Current;
    public Obstacle mObstacle2Current;
    public boolean mIsPlaying;
    public boolean mIsEndGame;
    public int mScore;
    public int mBestScore;

    @Override
    public void onCreate() {
        super.onCreate();
        mIsPlaying = false;
        mIsEndGame = false;
        mScore = 0;

        SharedPreferences prefs = getSharedPreferences(Constant.MY_PREFS, MODE_PRIVATE);
        mBestScore = prefs.getInt(Constant.PREF_BEST_SCORE, 0);


        mAladdinCurrentCoor = new Coordinate();
    }
}
