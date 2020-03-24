package com.example.alwaysawake2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.RecyclerProject.ProjectAdapter;
import com.example.alwaysawake2.RecyclerProject.ProjectItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class ActivityG1Project extends BaseActivity {

    RecyclerView recyclerView;
    ProjectAdapter projectAdapter = new ProjectAdapter(ActivityG1Project.this);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityG1Project.this);

    Button addProjectButton, toHomeButton, toHabitButton, toGoodWritingButton, toLikeButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_7_1_project);

        /**뷰 매칭**/
        addProjectButton = findViewById(R.id.project_addProjectButton);
        toHomeButton = findViewById(R.id.project_toHomeButton);
        toHabitButton = findViewById(R.id.project_toHabitButton);
        toGoodWritingButton = findViewById(R.id.project_toGoodWritingButton);
        toLikeButton = findViewById(R.id.project_toLikeButton);
        recyclerView = findViewById(R.id.project_recyclerView);

        /**세팅**/
        recyclerView.setAdapter(projectAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);


        /**버튼 클릭 이벤트**/
        Button.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.project_addProjectButton:
                        startActivity(new Intent(ActivityG1Project.this, ActivityG2Title.class));
                        break;
                    case R.id.project_toHomeButton:
//                        startActivity(new Intent(ActivityG1Project.this, ActivityDMain.class));
                        onBackPressed();
                        break;
                    case R.id.project_toHabitButton:
                        startActivity(new Intent(ActivityG1Project.this, ActivityE1Habit.class));
                        finish();
                        break;
                    case R.id.project_toGoodWritingButton:
                        startActivity(new Intent(ActivityG1Project.this, ActivityF1GoodWriting.class));
                        finish();
                        break;
                    case R.id.project_toLikeButton:
                        startActivity(new Intent(ActivityG1Project.this, ActivityH1Like.class));
                        finish();
                        break;
                }
            }
        };
        addProjectButton.setOnClickListener(onClickListener);
        toHomeButton.setOnClickListener(onClickListener);
        toHabitButton.setOnClickListener(onClickListener);
        toGoodWritingButton.setOnClickListener(onClickListener);
        toLikeButton.setOnClickListener(onClickListener);
        /**버튼 클릭 이벤트**/
    }

    @Override
    protected void onResume() {
        super.onResume();
        projectAdapterDataFlag = false;
        projectAdapter.notifyDataSetChanged();
    }

}
