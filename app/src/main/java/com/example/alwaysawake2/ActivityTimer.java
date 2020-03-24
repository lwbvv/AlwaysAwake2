package com.example.alwaysawake2;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alwaysawake2.BaseActivity.BaseActivity;

import java.util.ArrayList;
import java.util.Locale;

public class ActivityTimer extends BaseActivity {

    //뒤로가기 버튼
    ImageButton backButton;
    Button start, stop;
    //카운트다운 표시시킬 텍스트
    TextView countDownText, textViewHour, textViewMinute, textViewSecond;

    NumberPicker hourSpinner, minuteSpinner, secondSpinner;

    ArrayList hourArray, minuteArray, secondsArray;

    ArrayAdapter hourAdapter, minuteAdapter, secondsAdapter;

    int startAndPauseStatus;
    int restart = 100;
    final int STARTSTATUS = 0;
    final int PAUSESTATUS = 1;
    final int RESTARTSTATUS = 2;
    final int RESTARTPAUSE = 3;


    // 시간 , 분 , 초 선탣
    int hour, minute, second;

    // 남은 시간 넣어줄 변수

    long timeRemaining;
    int totalTime;

    CountDownTimer countDownTimer;
    CountDownTimer reCountDownTimer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        /**매칭**/
        backButton = findViewById(R.id.timer_backButton);
        start = findViewById(R.id.timer_start);
        stop = findViewById(R.id.timer_stop);
        countDownText = findViewById(R.id.timer_countDownText);
        textViewHour = findViewById(R.id.timer_textViewHour);
        textViewMinute = findViewById(R.id.timer_textViewMinute);
        textViewSecond = findViewById(R.id.timer_textViewSecond);

        //numberPicker
        hourSpinner = findViewById(R.id.timer_hourSpinner);
        minuteSpinner = findViewById(R.id.timer_minuteSpinner);
        secondSpinner = findViewById(R.id.timer_secondSpinner);

        hourArray = new ArrayList();
        minuteArray = new ArrayList();
        secondsArray = new ArrayList();

        /**어뎁터 세팅**/
        hourAdapter = new ArrayAdapter(ActivityTimer.this, R.layout.support_simple_spinner_dropdown_item, hourArray);
        minuteAdapter = new ArrayAdapter(ActivityTimer.this, R.layout.support_simple_spinner_dropdown_item, minuteArray);
        secondsAdapter = new ArrayAdapter(ActivityTimer.this, R.layout.support_simple_spinner_dropdown_item, secondsArray);

        /**스피너 세팅**/
        hourSpinner.setMinValue(0);
        hourSpinner.setMaxValue(24);
        minuteSpinner.setMinValue(0);
        minuteSpinner.setMaxValue(60);
        secondSpinner.setMinValue(0);
        secondSpinner.setMaxValue(60);

        /**타이머 버튼 이벤트**/
        Button.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.timer_start) {
                    hour = hourSpinner.getValue();
                    minute = minuteSpinner.getValue();
                    second = secondSpinner.getValue();
                    switch (startAndPauseStatus) {

                        //시작버튼 클릭
                        case STARTSTATUS:
                            startAndPauseStatus++;

                            int totalTime = (hour*60*60*1000) + (minute*60*1000) + (second*1000);
                            int tt = totalTime;
                            countDownTimer = new CountDownTimer(tt,1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    long hourTick = millisUntilFinished/60/60/1000;
                                    long minuteTick = millisUntilFinished/60/1000 - (hourTick*60);
                                    long secondsTick = millisUntilFinished/1000 - ((hourTick*60*60)+(minuteTick*60));
                                    countDownText.setText(String.format("%d: %d: %d", hourTick, minuteTick, secondsTick));
                                    timeRemaining = millisUntilFinished;
                                }

                                @Override
                                public void onFinish() {
                                    countDownText.setText(String.format("%d: %d: %d",0,0,0));
                                }
                            }.start();
                            countDownText.setVisibility(View.VISIBLE);
                            textViewHour.setVisibility(View.GONE);
                            textViewMinute .setVisibility(View.GONE);
                            textViewSecond .setVisibility(View.GONE);
                            hourSpinner .setVisibility(View.GONE);
                            minuteSpinner .setVisibility(View.GONE);
                            secondSpinner .setVisibility(View.GONE);
                            start.setText("정지");
                            break;
                        case PAUSESTATUS:
                            countDownTimer.cancel();
                            startAndPauseStatus++;
                            start.setText("재시작");
                            break;

                        case RESTARTSTATUS:
                            startAndPauseStatus++;
                            restart = 101;
                            reCountDownTimer = new CountDownTimer(timeRemaining,1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    long hourTick = millisUntilFinished/60/60/1000;
                                    long minuteTick = millisUntilFinished/60/1000 - (hourTick*60);
                                    long secondsTick = millisUntilFinished/1000 - ((hourTick*60*60)+(minuteTick*60));
                                    countDownText.setText(String.format("%d: %d: %d", hourTick, minuteTick, secondsTick));
                                    timeRemaining = millisUntilFinished;
                                }

                                @Override
                                public void onFinish() {

                                }
                            }.start();
                            start.setText("정지");
                            break;
                        case RESTARTPAUSE:
                            restart = 102;
                            startAndPauseStatus--;
                            reCountDownTimer.cancel();
                            start.setText("재시작");
                            break;
                    }


                } else if (v.getId() == R.id.timer_stop) {
                            if(startAndPauseStatus == 1){
                                countDownTimer.cancel();
                                stopSetting();
                                startAndPauseStatus = 0;
                            }else if(startAndPauseStatus == 2){
                                stopSetting();
                                startAndPauseStatus = 0;
                            }else if(restart == 101){
                                reCountDownTimer.cancel();
                                stopSetting();
                                startAndPauseStatus = 0;
                                restart = 100;
                            }else if(restart == 102){
                                stopSetting();
                                startAndPauseStatus = 0;
                                restart = 100;
                            }
                }
            }
        };
        start.setOnClickListener(onClickListener);
        stop.setOnClickListener(onClickListener);
    }
    /**타이머 버튼 이벤트**/



    void stopSetting(){
        countDownText.setVisibility(View.GONE);
        textViewHour.setVisibility(View.VISIBLE);
        textViewMinute .setVisibility(View.VISIBLE);
        textViewSecond .setVisibility(View.VISIBLE);
        hourSpinner .setVisibility(View.VISIBLE);
        minuteSpinner .setVisibility(View.VISIBLE);
        secondSpinner .setVisibility(View.VISIBLE);
        start.setText("시작");
    }
}
