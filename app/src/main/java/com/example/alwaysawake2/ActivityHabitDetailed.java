package com.example.alwaysawake2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.RecyclerHabit.HabitItem;

import java.util.ArrayList;
import java.util.List;

public class ActivityHabitDetailed extends BaseActivity {

    Button backButton, successButton;

    TextView title;

    CheckBox monCheckBox, tueCheckBox, wedCheckBox, thuCheckBox, friCheckBox, satCheckBox, sunCheckBox;

    Button everyDayCheckButton;

    TextView preNoticeTime, location, timerStart, timerFinish, indexTitle;

ArrayList<HabitItem> arrayList;
double latitude, longitude, radius;
int position;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detailed);

        /**매칭**/
        matchingWidget();
        setWidget();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(radius == 0){
                    Toast.makeText(ActivityHabitDetailed.this,"위치를 설정하지 않음", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(ActivityHabitDetailed.this, ActivityMapCrrentLocation.class);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("radius", radius);
                    startActivity(intent);
                }
            }
        });
    }








    void matchingWidget() {
        backButton = findViewById(R.id.habitDetailed_backButton);
        title = findViewById(R.id.habitDetailed_title);
        monCheckBox = findViewById(R.id.habitDetailed_monCheckBox);
        tueCheckBox = findViewById(R.id.habitDetailed_tueCheckBox);
        wedCheckBox = findViewById(R.id.habitDetailed_wedCheckBox);
        thuCheckBox = findViewById(R.id.habitDetailed_thuCheckBox);
        friCheckBox = findViewById(R.id.habitDetailed_friCheckBox);
        satCheckBox = findViewById(R.id.habitDetailed_satCheckBox);
        sunCheckBox = findViewById(R.id.habitDetailed_sunCheckBox);
        everyDayCheckButton = findViewById(R.id.habitDetailed_everyDayCheckButton);
        preNoticeTime = findViewById(R.id.habitDetailed_preNoticeTime);
        location = findViewById(R.id.habitDetailed_location);
        timerStart = findViewById(R.id.habitDetailed_timerStart);

    }

    void setWidget(){
        Intent intent =getIntent();
        arrayList = (ArrayList<HabitItem>) intent.getSerializableExtra("arrayList");
        position = intent.getIntExtra("position", 0);

        title.setText(arrayList.get(position).getTitle());
        monCheckBox.setChecked(arrayList.get(position).isMon());
        tueCheckBox.setChecked(arrayList.get(position).isTue());
        wedCheckBox.setChecked(arrayList.get(position).isWed());
        thuCheckBox.setChecked(arrayList.get(position).isThu());
        friCheckBox.setChecked(arrayList.get(position).isFri());
        satCheckBox.setChecked(arrayList.get(position).isSat());
        sunCheckBox.setChecked(arrayList.get(position).isSun());
        monCheckBox.setClickable(false);
        tueCheckBox.setClickable(false);
        wedCheckBox.setClickable(false);
        thuCheckBox.setClickable(false);
        friCheckBox.setClickable(false);
        satCheckBox.setClickable(false);
        sunCheckBox.setClickable(false);
        everyDayCheckButton = checkBoxStatus(everyDayCheckButton);
        if(!arrayList.get(position).getPreNoticeTime().equals("시간추가")){ preNoticeTime.setText(arrayList.get(position).getPreNoticeTime());}
        if(arrayList.get(position).getRadius() != 0){ location.setText(arrayList.get(position).getPlaceNameOrAddress()); }
        if(!arrayList.get(position).getTimerStart().equals("시작시간")){timerStart.setText(arrayList.get(position).getCheckTimeHour() + "시 " + arrayList.get(position).getCheckTimeMinute() + "분 부터 " +arrayList.get(position).getHourLimit() + "시" + arrayList.get(position).getMinuteLimit() + "분 까지");}
        latitude = arrayList.get(position).getLatitude();
        longitude = arrayList.get(position).getLongitude();
        radius = arrayList.get(position).getRadius();
    }

    Button checkBoxStatus(Button button) {
        if (monCheckBox.isChecked() && tueCheckBox.isChecked() && wedCheckBox.isChecked() && thuCheckBox.isChecked() && friCheckBox.isChecked() && satCheckBox.isChecked() && sunCheckBox.isChecked() == true) {
            monCheckBox.toggle();
            tueCheckBox.toggle();
            wedCheckBox.toggle();
            thuCheckBox.toggle();
            friCheckBox.toggle();
            satCheckBox.toggle();
            sunCheckBox.toggle();
            button.setBackgroundColor(Color.parseColor("#F2EC16"));
            button.setTextColor(Color.parseColor("#ffffff"));
        } else {
            button.setBackgroundColor(Color.parseColor("#EEEEEE"));
            button.setTextColor(Color.parseColor("#4B4B4B"));
        }
        return button;
    }
}
