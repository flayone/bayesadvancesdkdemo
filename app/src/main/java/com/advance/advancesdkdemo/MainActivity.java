package com.advance.advancesdkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.advance.advancesdkdemo.admore.MYSplashActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }



    public void amSplash(View view) {
        startActivity(new Intent(this, MYSplashActivity.class));
    }

}
