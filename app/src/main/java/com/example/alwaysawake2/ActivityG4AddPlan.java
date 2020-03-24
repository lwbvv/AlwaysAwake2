package com.example.alwaysawake2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alwaysawake2.BaseActivity.BaseActivity;

import java.util.Calendar;

public class ActivityG4AddPlan extends BaseActivity {

    Button backButton, successButton;

    EditText planTitle, bodyText;

    TextView deadlineDate, preNotice;

    RatingBar ratingBar;

    int planCount;
    int deadlineDay, deadlineMonth, deadlineYear;
    int noticeTimeHour, noticeTimeMinute;
    long noticeMilliTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_7_4_addplan);

        /**뷰 매칭**/
        backButton = findViewById(R.id.addPlan_backButton);
        successButton = findViewById(R.id.addPlan_successButton);
        planTitle = findViewById(R.id.addPlan_planTitle);
        bodyText = findViewById(R.id.addPlan_bodyText);
        deadlineDate = findViewById(R.id.addPlan_deadlineDate);
        preNotice = findViewById(R.id.addPlan_preNotice);
        ratingBar = findViewById(R.id.addPlan_ratingBar);

/**어뎁터에서 받아온 데이터들 세팅 및 처리**/
        if (planAdapterDataFlag) {
            Intent getPlanDataIntent = getIntent();
            planCount = getPlanDataIntent.getIntExtra("planGetCount", 0);
            deadlineYear = getPlanDataIntent.getIntExtra("deadlineYear", 0);
            deadlineMonth = getPlanDataIntent.getIntExtra("deadlineMonth", 0);
            deadlineDay = getPlanDataIntent.getIntExtra("deadlineDay", 0);
            planTitle.setText(getPlanDataIntent.getExtras().getString("title"));
            bodyText.setText(getPlanDataIntent.getExtras().getString("bodyText"));
            ratingBar.setRating(getPlanDataIntent.getFloatExtra("importance", 0));
            preNotice.setText(getPlanDataIntent.getStringExtra("noticeTime"));
            String dateFormat = String.format("%d년 %d월 %d일", deadlineYear, deadlineMonth + 1, deadlineDay);
            deadlineDate.setText(dateFormat);
        }
/**어뎁터에서 받아온 데이터들 세팅 및 처리**/


        /**버튼 클릭 이벤트**/
        Button.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.addPlan_backButton:
                        onBackPressed();
                        break;
                    case R.id.addPlan_successButton:
                        successConditions();
                        break;


                    case R.id.addPlan_deadlineDate:
                        deadlineDialog(ActivityG4AddPlan.this, deadlineDate);
                        break;


                    case R.id.addPlan_preNotice:
                        planTimeSetting(preNotice, ActivityG4AddPlan.this);
                        break;
                }
            }
        };
        backButton.setOnClickListener(onClickListener);
        successButton.setOnClickListener(onClickListener);
        deadlineDate.setOnClickListener(onClickListener);
        preNotice.setOnClickListener(onClickListener);
        /**버튼 클릭 이벤트**/

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        planAdapterDataFlag = false;
    }

    /**
     * 데드라인 설정 데이트피커 다이얼로그 메소드
     **/
    TextView deadlineDialog(Context context, final TextView datePrirnt) {
        Calendar calendar = Calendar.getInstance();
        int toYear = calendar.get(Calendar.YEAR);
        int toMonth = calendar.get(Calendar.MONTH);
        int toDay = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                deadlineDay = dayOfMonth;
                deadlineMonth = month;
                deadlineYear = year;
                String dateFormat = String.format("%d년 %d월 %d일", year, month + 1, dayOfMonth);
                datePrirnt.setText(dateFormat);
            }
        }, toYear, toMonth, toDay);
        calendar.set(toYear, toMonth, toDay);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
        return datePrirnt;
    }
    /**데드라인 설정 데이트피커 다이얼로그 메소드**/


    /**
     *
     *확인버튼 조건
     *
     */
    void successConditions(){
        if ("".equals(planTitle.getText().toString())) {
            Toast.makeText(ActivityG4AddPlan.this, "제목을 입력 해주세요", Toast.LENGTH_SHORT).show();
        } else if (deadlineDate.getText().equals("마감일")) {
            Toast.makeText(ActivityG4AddPlan.this, "마감일을 설정 해주세요", Toast.LENGTH_SHORT).show();
        } else if (ratingBar.getRating() == 0) {
            Toast.makeText(ActivityG4AddPlan.this, "중요도를 선택 해주세요", Toast.LENGTH_SHORT).show();
        } else if ("".equals(bodyText.getText().toString())) {
            Toast.makeText(ActivityG4AddPlan.this, "본문 내용을 입력 해주세요", Toast.LENGTH_SHORT).show();
        } else {
            Intent toPlanIntent = new Intent();
            toPlanIntent.putExtra("title", planTitle.getText().toString());
            toPlanIntent.putExtra("bodyText", bodyText.getText().toString());
            toPlanIntent.putExtra("importance", ratingBar.getRating());
            toPlanIntent.putExtra("deadlineYear", deadlineYear);
            toPlanIntent.putExtra("deadlineMonth", deadlineMonth);
            toPlanIntent.putExtra("deadlineDay", deadlineDay);
            toPlanIntent.putExtra("noticeTime", preNotice.getText().toString());
            toPlanIntent.putExtra("noticeMilliTime", noticeMilliTime);
            toPlanIntent.putExtra("noticeTimeHour", noticeTimeHour);
            toPlanIntent.putExtra("noticeTimeMinute", noticeTimeMinute);
            if (planAdapterDataFlag) {
                toPlanIntent.putExtra("planGetCount", planCount);
            }
            setResult(RESULT_OK, toPlanIntent);
            finish();
        }
    }




    public TextView planTimeSetting(final TextView textView, Context context){

        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(context,android.R.style.Theme_Holo_Dialog_NoActionBar,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String am_pm = (hourOfDay < 12) ? "AM" : "PM";
                int hour = (hourOfDay <= 12) ? hourOfDay : hourOfDay%12;
                String msg = String.format("%d시 %d분 %s",hour, minute, am_pm);
               noticeTimeHour = hourOfDay;
               noticeTimeMinute = minute;
                textView.setText(msg);
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePickerDialog.show();
        calendar.set(Calendar.HOUR_OF_DAY, noticeTimeHour);
        calendar.set(Calendar.MINUTE,noticeTimeMinute);
        Calendar calendar1 = Calendar.getInstance();
        noticeMilliTime = calendar.getTimeInMillis();
        calendar1.getTimeInMillis();
        return textView;
    }
}
