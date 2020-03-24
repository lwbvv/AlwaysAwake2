package com.example.alwaysawake2;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.alwaysawake2.NotificationClass.AlarmService;

import java.util.ArrayList;
import java.util.List;

public class ActivityAIntro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1_intro);


//        Intent serviceIntent = new Intent(ActivityAIntro.this, AlarmService.class);
//           stopService(serviceIntent);


        Handler introHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                startActivity(new Intent(ActivityAIntro.this, ActivityBLogin.class));
                finish();
            }
        };
                introHandler.sendEmptyMessageDelayed(0,3000);




    }


}
