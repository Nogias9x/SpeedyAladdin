package com.example.n50.speedyaladdin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.n50.speedyaladdin.Models.GameSurface;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LOGGGGG", "onCreate");

        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Sét đặt giao diện của Activity.
        this.setContentView(new GameSurface(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LOGGGGG", "onResume");

//        // Set fullscreen
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        // Sét đặt giao diện của Activity.
//        this.setContentView(new GameSurface(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LOGGGGG", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LOGGGGG", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LOGGGGG", "onDestroy");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LOGGGGG", "onPause");
        finish();
        System.exit(0);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("LOGGGGG", "onBackPressed");

    }
}
