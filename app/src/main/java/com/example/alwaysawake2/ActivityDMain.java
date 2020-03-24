package com.example.alwaysawake2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.RecyclerProject.ProjectItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityDMain extends BaseActivity {

    Button timerButton, stopwatchButton;
    /**하단부 화면전환 버튼**/
    Button toProjectButton, toHabitButton, toGoodWritingButton, toLikeButton;


    TextView deadlineProjectTitle, deadlineProjectDate;
    ArrayList<ProjectItem> arrayList = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4_main);


        Calendar time = Calendar.getInstance();
        int toDay, toMonth, toYear;

        toYear = time.get(Calendar.YEAR);
        toMonth = time.get(Calendar.MONTH);
        toDay = time.get(Calendar.DATE);
        time.set(toYear, toMonth, toDay);
        /**뷰 매칭**/
        timerButton = findViewById(R.id.main_timerButton);
        stopwatchButton = findViewById(R.id.main_stopwatchButton);
        toProjectButton = findViewById(R.id.main_toProjectButton);
        toHabitButton = findViewById(R.id.main_toHabitButton);
        toGoodWritingButton = findViewById(R.id.main_toGoodWritingButton);
        toLikeButton = findViewById(R.id.main_toLikeButton);
        deadlineProjectTitle = findViewById(R.id.main_deadlineProjectTitle);
        deadlineProjectDate = findViewById(R.id.main_deadlineProjectDate);
        recyclerView = findViewById(R.id.main_recyclerView);
        try {
            getProject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(arrayList.size() > 0) {
            deadlineProjectTitle.setText(arrayList.get(0).getTitle());
            deadlineProjectDate.setText("D-DAY " + ((arrayList.get(0).getDeadMilliTimes() - time.getTimeInMillis()) / (1000 * 60 * 60 * 24)));
        }

        /**버튼 클릭 이벤트**/
        Button.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.main_timerButton:
                        startActivity(new Intent(ActivityDMain.this,ActivityTimer.class));
                        break;
                    case R.id.main_stopwatchButton:
                        startActivity(new Intent(ActivityDMain.this,ActivityStopWatch.class));
                        break;
                    case R.id.main_toProjectButton:
                        startActivity(new Intent(ActivityDMain.this,ActivityG1Project.class));
                        break;
                    case R.id.main_toHabitButton:
                        startActivity(new Intent(ActivityDMain.this,ActivityE1Habit.class));
                        break;
                    case R.id.main_toGoodWritingButton:
                        startActivity(new Intent(ActivityDMain.this,ActivityF1GoodWriting.class));
                        break;
                    case R.id.main_toLikeButton:
                        startActivity(new Intent(ActivityDMain.this,ActivityH1Like.class));
                        break;
                }
            }
        };
        timerButton.setOnClickListener(onClickListener);
        stopwatchButton.setOnClickListener(onClickListener);
        toProjectButton.setOnClickListener(onClickListener);
        toHabitButton.setOnClickListener(onClickListener);
        toGoodWritingButton.setOnClickListener(onClickListener);
        toLikeButton.setOnClickListener(onClickListener);
        /**버튼 클릭 이벤트**/
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    void getProject() throws JSONException {
        SharedPreferences sharedPreferences = getSharedPreferences("projectListFile", MODE_PRIVATE);
        JSONArray jsonArray = new JSONArray();
        String jObject = sharedPreferences.getString(keyId,"");
        JSONObject parentJsonObject = new JSONObject(jObject);
        jsonArray = parentJsonObject.getJSONArray("projectList");

        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Calendar calendar = Calendar.getInstance();
            calendar.set(jsonObject.getInt("year"),jsonObject.getInt("month"),jsonObject.getInt("day"));

            ProjectItem projectItem = new ProjectItem(jsonObject.getString("title"),calendar.getTimeInMillis());
            arrayList.add(projectItem);
        }

        Collections.sort(arrayList, new Comparator<ProjectItem>() {
            @Override
            public int compare(ProjectItem o1, ProjectItem o2) {
                if(o1.getDeadMilliTimes() > o2.getDeadMilliTimes()){
                    return 1;
                } else if(o1.getDeadMilliTimes() <o2.getDeadMilliTimes()){
                    return -1;
                }
                return 0;
            }
        });

    }
}
