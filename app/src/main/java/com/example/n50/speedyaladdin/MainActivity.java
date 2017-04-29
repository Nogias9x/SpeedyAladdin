package com.example.n50.speedyaladdin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.n50.speedyaladdin.Models.GameSurface;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        // Sét đặt giao diện của Activity.
        this.setContentView(new GameSurface(this));
    }
}
