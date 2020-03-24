package com.example.alwaysawake2.NotificationClass;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import com.example.alwaysawake2.BaseActivity.BaseActivity;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class AlarmSetting {
    Calendar calendar;
    public void setAlarmManager(Context context, Class ReceiverCls, int alarmId, int hour, int minute, String title, boolean mon,
                                boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, boolean sun) {
        long time; //알람시간 저장변수
        long interval = 1000*60*60*24; // 하루마다 반복
        calendar = Calendar.getInstance(); //캘린더에서 인스턴스 가져옴

        calendar.set(Calendar.HOUR_OF_DAY, hour);//매개값 받아 시간 세팅
        calendar.set(Calendar.MINUTE, minute);//매개값 받아 분 세팅
        calendar.set(Calendar.SECOND, 0); //고정 0초
        calendar.set(Calendar.MILLISECOND,0);// 고정 0밀리초
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        final int REQUEST_ALARM = 3000;//습관 목록 구별해 줄 상수값

        if(System.currentTimeMillis() > calendar.getTimeInMillis()){
            time = calendar.getTimeInMillis() + interval; //알람 시간이 현재 시간보자 작을 시에 하루 뒤에 알람 울리도록 설정
        }else {
            time = calendar.getTimeInMillis(); //알림시간이 현재시간보다 크면 그냥 알림시간을 넣어줌
        }

        Intent intent = new Intent(context, ReceiverCls);
        intent.putExtra("id", BaseActivity.keyId);
        intent.putExtra("title", title);
        intent.putExtra("mon", mon);
        intent.putExtra("tue", tue);
        intent.putExtra("wed", wed);
        intent.putExtra("thu", thu);
        intent.putExtra("fri", fri);
        intent.putExtra("sat", sat);
        intent.putExtra("sun", sun);
        intent.putExtra("alarmId", alarmId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), alarmId,
                                                                    intent, PendingIntent.FLAG_CANCEL_CURRENT);//어레이의 포지션 값으로 알람 식별

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,time,interval,pendingIntent);

    }
}
