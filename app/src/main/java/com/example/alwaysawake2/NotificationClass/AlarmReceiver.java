package com.example.alwaysawake2.NotificationClass;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import com.example.alwaysawake2.ActivityE1Habit;
import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    final int MOVE_HABIT = 3600;
    boolean mon, tue, wed, thu, fri, sat, sun;
    String title;
    @Override
    public void onReceive(Context context, Intent intent) {
       String id = intent.getStringExtra("id");
       title = intent.getStringExtra("title");
       mon = intent.getBooleanExtra("mon", false);
        tue = intent.getBooleanExtra("tue", false);
        wed = intent.getBooleanExtra("wed", false);
        thu = intent.getBooleanExtra("thu", false);
        fri = intent.getBooleanExtra("fri", false);
        sat = intent.getBooleanExtra("sat", false);
        sun = intent.getBooleanExtra("sun", false);

        BaseActivity.keyId = id;
       int alarmId = intent.getIntExtra("alarmId", 0);



        Calendar calendar = Calendar.getInstance();

        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case 1:
                if(!sun){ return; }
                break;
            case 2:
                if(!mon){ return; }
                break;
            case 3:
                if(!tue){ return; }
                break;
            case 4:
                if(!wed){ return; }
                break;
            case 5:
                if(!thu){ return; }
                break;
            case 6:
                if(!fri){ return; }
                break;
            case 7:
                if(!sat){ return; }
                break;


        }
        CharSequence charSequence = new StringBuffer(title);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.intro_awaken)
                .setContentTitle(charSequence)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(PendingIntent.getActivity(context,MOVE_HABIT,new Intent(context, ActivityE1Habit.class),PendingIntent.FLAG_CANCEL_CURRENT));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) SystemClock.elapsedRealtime() / 1000,builder.build());
    }
}
