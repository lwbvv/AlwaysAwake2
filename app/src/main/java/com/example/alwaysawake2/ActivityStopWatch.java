package com.example.alwaysawake2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.alwaysawake2.BaseActivity.BaseActivity;

public class ActivityStopWatch extends BaseActivity {
    TextView myOutput;
    TextView myRec;
    Button myBtnStart;
    Button myBtnRec;

    final static int Init = 0;
    final static int Run = 1;
    final static int Pause = 2;

    int cur_Status = Init; //현재의 상태를 저장할변수를 초기화함.
    int myCount = 1;
    long myBaseTime;
    long myPauseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        myOutput = (TextView) findViewById(R.id.time_out);
        myRec = (TextView) findViewById(R.id.record);
        myBtnStart = (Button) findViewById(R.id.btn_start);
        myBtnRec = (Button) findViewById(R.id.btn_rec);

        /**
         * 디스플레이 끄는것 나중에 구현
         */
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
//        @SuppressLint("InvalidWakeLockTag") final PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"tag");
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] v = event.values;
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    switch (cur_Status) {
                        case Init:
                            if(v[2] < -9.6){
                                myBaseTime = SystemClock.elapsedRealtime();
                                System.out.println(myBaseTime);
                                //myTimer이라는 핸들러를 빈 메세지를 보내서 호출
                                myTimer.sendEmptyMessage(0);
                                myBtnStart.setText("멈춤"); //버튼의 문자"시작"을 "멈춤"으로 변경
                                Log.i("초초초초", "멈춤");
                                myBtnRec.setEnabled(true); //기록버튼 활성
                                cur_Status = Run; //현재상태를 런상태로 변경
                                //디스플레이
//                                wakeLock.acquire();
                            }
                            break;
                        case Run:
                          if(v[2] > 9.6){
                              myTimer.removeMessages(0); //핸들러 메세지 제거
                              String str = myRec.getText().toString();
                              str += String.format("%d. %s\n", myCount, myOutput.getText());
                              myRec.setText(str);
                              myCount++; //카운트 증가
                              myPauseTime = SystemClock.elapsedRealtime();
                              Log.i("초초초초", "정지");
                              myBtnStart.setText("시작");
                              myBtnRec.setText("리셋");
                              cur_Status = Pause;
                              //디스플레이
//                              wakeLock.release();
                              return;
                        }
                            break;
                        case Pause:
                            if(v[2] < -9.6) {
                                long now = SystemClock.elapsedRealtime();
                                myTimer.sendEmptyMessage(0);
                                Log.i("초초초초", "재시작");
                                myBaseTime += (now - myPauseTime);
                                myBtnStart.setText("멈춤");
                                myBtnRec.setText("기록");
                                cur_Status = Run;
                                //디스플레이
//                                wakeLock.acquire();
                            }
                            break;


                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorManager.registerListener(sensorEventListener,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_UI);

    }


    public void myOnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start: //시작버튼을 클릭했을때 현재 상태값에 따라 다른 동작을 할수있게끔 구현.
                switch (cur_Status) {
                    case Init:
                        myBaseTime = SystemClock.elapsedRealtime();
                        System.out.println(myBaseTime);
                        //myTimer이라는 핸들러를 빈 메세지를 보내서 호출
                        myTimer.sendEmptyMessage(0);
                        myBtnStart.setText("멈춤"); //버튼의 문자"시작"을 "멈춤"으로 변경
                        Log.i("초초초초", "멈춤");
                        myBtnRec.setEnabled(true); //기록버튼 활성
                        cur_Status = Run; //현재상태를 런상태로 변경
                        break;
                    case Run:
                        myTimer.removeMessages(0); //핸들러 메세지 제거
                        myPauseTime = SystemClock.elapsedRealtime();
                        Log.i("초초초초", "시작");
                        myBtnStart.setText("시작");
                        myBtnRec.setText("리셋");
                        cur_Status = Pause;
                        break;
                    case Pause:
                        long now = SystemClock.elapsedRealtime();
                        myTimer.sendEmptyMessage(0);
                        Log.i("초초초초", "일정");
                        myBaseTime += (now - myPauseTime);
                        myBtnStart.setText("멈춤");
                        myBtnRec.setText("기록");
                        cur_Status = Run;
                        break;


                }
                break;
            case R.id.btn_rec:
                switch (cur_Status) {
                    case Run:
                        Log.i("초초초초", "11111");
                        String str = myRec.getText().toString();
                        str += String.format("%d. %s\n", myCount, getTimeOut());
                        myRec.setText(str);
                        myCount++; //카운트 증가

                        break;
                    case Pause:
                        //핸들러를 멈춤
                        myTimer.removeMessages(0);
                        Log.i("초초초초", "멈춰");
                        myBtnStart.setText("시작");
                        myBtnRec.setText("기록");
                        myOutput.setText("00:00:00");

                        cur_Status = Init;
                        myCount = 1;
                        myRec.setText("");
                        myBtnRec.setEnabled(false);
                        break;


                }
                break;

        }
    }

    Handler myTimer = new Handler() {
        public void handleMessage(Message msg) {
            Log.i("초초초초", "핸들러");
            myOutput.setText(getTimeOut());
            //sendEmptyMessage 는 비어있는 메세지를 Handler 에게 전송하는겁니다.
            myTimer.sendEmptyMessage(0);

        }
    };

    //현재시간을 계속 구해서 출력하는 메소드
    String getTimeOut() {
        long now = SystemClock.elapsedRealtime(); //애플리케이션이 실행되고나서 실제로 경과된 시간;
        long outTime = now - myBaseTime;
        String easy_outTime = String.format("%02d:%02d:%02d", outTime / 1000 / 60, (outTime / 1000) % 60, (outTime % 1000) / 10);
        return easy_outTime;

    }


    //생명주기 오버라이드
    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i("LOG_스탑워치", "리스타트");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("LOG_스탑워치", "스타트");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("LOG_스탑워치", "리줌");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i("LOG_스탑워치", "퍼즈");


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("LOG_스탑워치", "스탑");


    }

    @Override

    protected void onDestroy() {
        super.onDestroy();
        Log.i("LOG_스탑워치", "죽어");
        myTimer.removeMessages(0);
    }
}
