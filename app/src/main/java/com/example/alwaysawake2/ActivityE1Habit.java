package com.example.alwaysawake2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.RecyclerHabit.HabitAdapter;
import com.example.alwaysawake2.RecyclerHabit.HabitItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivityE1Habit extends BaseActivity {

    Button addHabitButton, allGet;
    Button toHomeButton, toProjectButton, toGoodWritingButton, toLikeButton;
    RecyclerView recyclerView;
    ArrayList<HabitItem> arrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityE1Habit.this);
    HabitAdapter habitAdapter = new HabitAdapter(arrayList, this);
    TextView weekText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5_habit);

        exampledd();
        setArrayList();
        setWeekText();

        /**버튼 클릭 이벤트**/
        Button.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.habit_addHabitButton:
                        startActivityForResult(new Intent(ActivityE1Habit.this, ActivityE2HabitCreating.class), REQUEST_CREATEHABIT);
                        break;
                    case R.id.habit_toHomeButton:
//                        startActivity(new Intent(ActivityE1Habit.this, ActivityDMain.class));
                        finish();
                        break;
                    case R.id.habit_toProjectButton:
                        startActivity(new Intent(ActivityE1Habit.this, ActivityG1Project.class));
                        finish();
                        break;
                    case R.id.habit_toGoodWritingButton:
                        startActivity(new Intent(ActivityE1Habit.this, ActivityF1GoodWriting.class));
                        finish();
                        break;
                    case R.id.habit_toLikeButton:
                        startActivity(new Intent(ActivityE1Habit.this, ActivityH1Like.class));
                        finish();
                        break;
                }
            }
        };
        addHabitButton.setOnClickListener(onClickListener);
        toHomeButton.setOnClickListener(onClickListener);
        toProjectButton.setOnClickListener(onClickListener);
        toGoodWritingButton.setOnClickListener(onClickListener);
        toLikeButton.setOnClickListener(onClickListener);
        /**버튼 클릭 이벤트**/



        allGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allPlan == false) {
                    allPlan = true;
                    habitAdapter.notifyDataSetChanged();
                    allGet.setText("가리기");
                }else{
                    allPlan = false;
                    habitAdapter.notifyDataSetChanged();
                    allGet.setText("모두보기");
                }
            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        savedShared();
        arrayList.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setArrayList();
        habitAdapter.notifyDataSetChanged();
    }

    void exampledd() {
        /**뷰 매칭**/
        allGet = findViewById(R.id.all_get);
        addHabitButton = findViewById(R.id.habit_addHabitButton);
        toHomeButton = findViewById(R.id.habit_toHomeButton);
        toProjectButton = findViewById(R.id.habit_toProjectButton);
        toGoodWritingButton = findViewById(R.id.habit_toGoodWritingButton);
        toLikeButton = findViewById(R.id.habit_toLikeButton);
        recyclerView = findViewById(R.id.habit_recyclerView);
        weekText = findViewById(R.id.habit_week);
        /**리사이클러뷰에 어뎁터 리니어레이아웃매니저 세팅**/
        recyclerView.setAdapter(habitAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    void setArrayList() {
        SharedPreferences sharedPreferences = getSharedPreferences("habitFile", MODE_PRIVATE);

        try {
            JSONArray jsonArray = new JSONArray(sharedPreferences.getString(keyId, ""));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                HabitItem habitItem = new HabitItem(jsonObject.getString("title"), jsonObject.getBoolean("check"), jsonObject.getBoolean("mon"), jsonObject.getBoolean("tue"), jsonObject.getBoolean("wed")
                        , jsonObject.getBoolean("thu"), jsonObject.getBoolean("fri"), jsonObject.getBoolean("sat"), jsonObject.getBoolean("sun"), jsonObject.getInt("checkTimeHour"),
                        jsonObject.getInt("checkTimeMinute"), jsonObject.getInt("hourLimit"), jsonObject.getInt("minuteLimit"),
                        jsonObject.getDouble("latitude"),jsonObject.getDouble("longitude"), jsonObject.getDouble("radius"),
                        jsonObject.getString("preNoticeTime"), jsonObject.getString("placeNameOrAddress"), jsonObject.getString("timerStart")
                , jsonObject.getInt("noticeHour"), jsonObject.getInt("noticeMinute"), jsonObject.getString("timerFinish"), jsonObject.getInt("alarmId"));
                arrayList.add(habitItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void savedShared() {
        SharedPreferences sharedPreferences = getSharedPreferences("habitFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(sharedPreferences.getString(keyId, ""));
            for (int i = 0; i < arrayList.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                boolean check = arrayList.get(i).isCheck();
                jsonObject.put("check", check);
                jsonArray.put(i, jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.putString(keyId, jsonArray.toString());
        editor.apply();
    }


    void setWeekText(){
        Calendar calendar = Calendar.getInstance();

        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case 1:
                weekText.setText("Sun");
                break;
            case 2:
                weekText.setText("Mon");
                break;
            case 3:
                weekText.setText("Tue");
                break;
            case 4:
                weekText.setText("Wed");
                break;
            case 5:
                weekText.setText("Thu");
                break;
            case 6:
                weekText.setText("Fri");
                break;
            case 7:
                weekText.setText("Sat");
                break;


        }
    }
}
