package com.example.alwaysawake2.NotificationClass;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import com.example.alwaysawake2.ActivityE1Habit;
import com.example.alwaysawake2.ActivityG1Project;
import com.example.alwaysawake2.R;

public class ProjectService extends JobService {
    final int MOVE_PROJECT = 9980;
PollTask pollTask;
    @Override
    public boolean onStartJob(JobParameters params) {
        pollTask = new PollTask();
        pollTask.execute(params);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        pollTask.cancel(true);
        return false;
    }

    private class PollTask extends AsyncTask<JobParameters, Void, Void> {
        @Override
        public Void doInBackground(JobParameters... params) {

            JobParameters jobParams = params[0];
//            CharSequence charSequence = new StringBuffer(title);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(ProjectService.this)
                    .setSmallIcon(R.drawable.intro_awaken)
                    .setContentTitle("타이틀")
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentIntent(PendingIntent.getActivity(ProjectService.this, MOVE_PROJECT, new Intent(ProjectService.this, ActivityG1Project.class), PendingIntent.FLAG_CANCEL_CURRENT));
            NotificationManager notificationManager = (NotificationManager) ProjectService.this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify((int) SystemClock.elapsedRealtime() / 1000, builder.build());
            jobFinished(jobParams, false);

            return null;
        }

    }
}
