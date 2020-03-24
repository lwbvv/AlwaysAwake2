package com.example.alwaysawake2.NotificationClass;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.alwaysawake2.ActivityG1Project;
import com.example.alwaysawake2.R;
import com.example.alwaysawake2.RecyclerPlan.PlanItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import static com.example.alwaysawake2.BaseActivity.BaseActivity.keyId;

public class AlarmService extends Service {

//    /**
//     * 싱글톤 패턴 구현
//     */
//    private AlarmService(){}
//    private static class InstanceHolder {
//        private static final AlarmService INSTANCE = new AlarmService();
//    }
//    public static AlarmService getInstance(){
//        return  InstanceHolder.INSTANCE;
//    }

    ArrayList<PlanItem> arrayList = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        thread.isDaemon();
//////        thread.start();
        return START_NOT_STICKY;
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            //제이슨 어레이에서 값 추출
            arraySort();
            for (int i = 0; i < arrayList.size(); i++) {
                Log.e("서비스스레드", "run");
                Calendar calendar = Calendar.getInstance();
                try {
                    long toMilliTime = calendar.getTimeInMillis();
                    calendar.set(Calendar.HOUR_OF_DAY,arrayList.get(i).getNoticeTimeHour());
                    calendar.set(Calendar.MINUTE,arrayList.get(i).getNoticeTimeMinute());
                    long deadMilliTime = calendar.getTimeInMillis();
                    Log.e("서비스스레드", "run" + toMilliTime);
                    Log.e("서비스스레드", "run" + deadMilliTime);
                    Log.e("서비스스레드", "run" + (deadMilliTime- toMilliTime));
                    Thread.sleep(deadMilliTime - toMilliTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent toActivity = new Intent(AlarmService.this, ActivityG1Project.class);
                Log.e("서비스", "1번");
                toActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                Log.e("서비스", "2번");
                PendingIntent pendingIntent = PendingIntent.getActivity(AlarmService.this, (int) SystemClock.elapsedRealtime() / 1000, toActivity, PendingIntent.FLAG_ONE_SHOT);
                Log.e("서비스", "3번");
                Log.d("시간1", "" + (int) SystemClock.elapsedRealtime() / 1000);
                Log.e("서비스", "4번");
                NotificationCompat.Builder builder = new NotificationCompat.Builder(AlarmService.this)
                        .setSmallIcon(R.drawable.intro_awaken)
                        .setContentTitle(arrayList.get(i).getTitle())
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_VIBRATE);
                NotificationManager notificationManager = (NotificationManager) AlarmService.this.getSystemService(Context.NOTIFICATION_SERVICE);
                Log.e("서비스", "5번");
                notificationManager.notify((int) SystemClock.elapsedRealtime() / 1000, builder.build());
                Log.d("시간2", "" + (int) SystemClock.elapsedRealtime() / 1000);

            }
        }
    });

    void arraySort() {
        SharedPreferences sharedPreferencesSort = getSharedPreferences("projectListFile", MODE_PRIVATE);
        String parentObjectString = sharedPreferencesSort.getString(keyId, "");
        JSONObject parentObject = null;
        JSONArray noticeArray = null;
        try {
            parentObject = new JSONObject(parentObjectString);
            noticeArray = parentObject.getJSONArray("planNoticeTime");
            for (int i = 0; i < noticeArray.length(); i++) {
                JSONObject jsonObject = noticeArray.getJSONObject(i);
                PlanItem planItem = new PlanItem();
                planItem.setTitle(jsonObject.getString("title"));
                planItem.setNoticeMilliTime(jsonObject.getLong("noticeMilliTime"));
                planItem.setNoticeTimeHour(jsonObject.getInt("noticeTimeHour"));
                planItem.setNoticeTimeMinute(jsonObject.getInt("noticeTimeMinute"));
                arrayList.add(planItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Collections.sort(arrayList, new Comparator<PlanItem>() {
            @Override
            public int compare(PlanItem o1, PlanItem o2) {
                if (o1.getNoticeMilliTime() > o2.getNoticeMilliTime()) {
                    return 1;
                } else if (o1.getNoticeMilliTime() < o2.getNoticeMilliTime()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
//        for(int i = 0; i < arrayList.size(); i++){
//            Log.e("array", arrayList.get(i).getTitle() + arrayList.get(i).getNoticeMilliTime());
    }
}

