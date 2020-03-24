package com.example.alwaysawake2;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.Dialog.NumberPickerDialog;
import com.example.alwaysawake2.NotificationClass.AlarmReceiver;
import com.example.alwaysawake2.NotificationClass.AlarmSetting;
import com.example.alwaysawake2.RecyclerHabit.HabitItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivityE2HabitCreating extends BaseActivity {

    Button backButton, successButton;

    EditText title;

    CheckBox monCheckBox, tueCheckBox, wedCheckBox, thuCheckBox, friCheckBox, satCheckBox, sunCheckBox;

    Button everyDayCheckButton;

    TextView preNoticeTime, location, timerStart, timerFinish;

    /**
     * 어뎁터에서 포지션값 겟
     **/
    int getCount;

    String msg;
    int noticeHour, noticeMinute;//미리알림 시간 세팅
    int checkTimeHour, checkTimeMinute, hourLimit, minuteLimit;//체크박스 활성화 타임
    private NumberPicker numberPickerHour;
    private NumberPicker numberPickerMinute;
    int alarmId;
    Dialog numberPickerDialog;

    JSONArray jsonArray = null;
    int position;
    //지도에서 가져온 값
    JSONObject alarmIdObject = null;
    double latitude, longitude, radius;
    String placeNameOrAddress = "위치없음";


    ArrayList<HabitItem> arrayList;

    public void setTitle(EditText title) {
        this.title = title;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5_habitcreating);


        /**매칭**/
        backButton = findViewById(R.id.habitCreating_backButton);
        successButton = findViewById(R.id.habitCreating_successButton);
        title = findViewById(R.id.habitCreating_title);
        monCheckBox = findViewById(R.id.habitCreating_monCheckBox);
        tueCheckBox = findViewById(R.id.habitCreating_tueCheckBox);
        wedCheckBox = findViewById(R.id.habitCreating_wedCheckBox);
        thuCheckBox = findViewById(R.id.habitCreating_thuCheckBox);
        friCheckBox = findViewById(R.id.habitCreating_friCheckBox);
        satCheckBox = findViewById(R.id.habitCreating_satCheckBox);
        sunCheckBox = findViewById(R.id.habitCreating_sunCheckBox);
        everyDayCheckButton = findViewById(R.id.habitCreating_everyDayCheckButton);
        preNoticeTime = findViewById(R.id.habitCreating_preNoticeTime);
        location = findViewById(R.id.habitCreating_location);
        timerStart = findViewById(R.id.habitCreating_timerStart);
        timerFinish = findViewById(R.id.habitCreating_timerFinish);


        /**어댑터 인텐트 데이터 활성화**/
        if (habitAdapterDataFlag) {
            Intent getDataIntent = getIntent();
            arrayList = (ArrayList<HabitItem>) getDataIntent.getSerializableExtra("array");
            position = getDataIntent.getIntExtra("position", 0);
            title.setText(arrayList.get(position).getTitle());
            noticeHour = arrayList.get(position).getNoticeHour();
            noticeMinute = arrayList.get(position).getNoticeMinute();
            latitude = arrayList.get(position).getLatitude();
            longitude = arrayList.get(position).getLongitude();
            radius = arrayList.get(position).getRadius();
            placeNameOrAddress = arrayList.get(position).getPlaceNameOrAddress();
            location.setText(placeNameOrAddress);
            checkTimeHour = arrayList.get(position).getCheckTimeHour();
            checkTimeMinute = arrayList.get(position).getCheckTimeMinute();
            hourLimit = arrayList.get(position).getHourLimit();
            minuteLimit = arrayList.get(position).getMinuteLimit();
            preNoticeTime.setText(arrayList.get(position).getPreNoticeTime());
            timerStart.setText(arrayList.get(position).getTimerStart());
            timerFinish.setText(arrayList.get(position).getTimerFinish());
            monCheckBox.setChecked(arrayList.get(position).isMon());
            tueCheckBox.setChecked(arrayList.get(position).isTue());
            wedCheckBox.setChecked(arrayList.get(position).isWed());
            thuCheckBox.setChecked(arrayList.get(position).isThu());
            friCheckBox.setChecked(arrayList.get(position).isFri());
            satCheckBox.setChecked(arrayList.get(position).isSat());
            sunCheckBox.setChecked(arrayList.get(position).isSun());
            alarmId = arrayList.get(position).getAlarmId();
        }


        /**버튼 클릭 이벤트**/
        Button.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.habitCreating_backButton:
                        onBackPressed();
                        break;
                    case R.id.habitCreating_successButton:


                        if (title.getText().toString().equals("")) {
                            Toast.makeText(ActivityE2HabitCreating.this, "제목을 선택해 주세요", Toast.LENGTH_SHORT).show();
                        } else if (monCheckBox.isChecked() == false && tueCheckBox.isChecked() == false && wedCheckBox.isChecked() == false &&
                                thuCheckBox.isChecked() == false && friCheckBox.isChecked() == false && satCheckBox.isChecked() == false && sunCheckBox.isChecked() == false &&
                                everyDayCheckButton.getTextColors().getDefaultColor() != -1) {
                            Toast.makeText(ActivityE2HabitCreating.this, "수행할 요일을 선택하세요", Toast.LENGTH_SHORT).show();
                        } else if (preNoticeTime.getText().toString().equals("시간추가")) {
                            putShared();
                            finish();
                        } else {
                            putShared();
                            AlarmSetting alarmSetting = new AlarmSetting();
                            alarmSetting.setAlarmManager(ActivityE2HabitCreating.this, AlarmReceiver.class, alarmId, noticeHour, noticeMinute, title.getText().toString(),
                                    monCheckBox.isChecked(), tueCheckBox.isChecked(), wedCheckBox.isChecked(), thuCheckBox.isChecked(), friCheckBox.isChecked(), satCheckBox.isChecked(), sunCheckBox.isChecked());
                            finish();
                        }
                        break;
                    case R.id.habitCreating_everyDayCheckButton:
                        everyDayButtonStatus(everyDayCheckButton);
                        break;
                    case R.id.habitCreating_preNoticeTime:
                        timeSetting(preNoticeTime, ActivityE2HabitCreating.this);

                        break;
                    case R.id.habitCreating_location:
                        startActivityForResult(new Intent(ActivityE2HabitCreating.this, ActivityE3Map.class), REQUEST_LOCATION);
                        break;
                    case R.id.habitCreating_timerStart:
                        checkTime(timerStart);
                        break;
                    case R.id.habitCreating_timerFinish:

                        if (timerStart.getText().toString().equals("시작시간")) {
                            Toast.makeText(ActivityE2HabitCreating.this, "시작 시간을 선택하세요", Toast.LENGTH_SHORT).show();
                        } else {
                            settingNumber();
                        }
                        break;

                }
            }
        };
        backButton.setOnClickListener(onClickListener);
        successButton.setOnClickListener(onClickListener);
        everyDayCheckButton.setOnClickListener(onClickListener);
        preNoticeTime.setOnClickListener(onClickListener);
        location.setOnClickListener(onClickListener);
        timerStart.setOnClickListener(onClickListener);
        timerFinish.setOnClickListener(onClickListener);
        /**버튼 클릭 이벤트**/


        /**체크박스 클릭 이벤트**/
        CheckBox.OnClickListener checkListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.habitCreating_monCheckBox:
                        checkBoxStatus(everyDayCheckButton);
                        break;
                    case R.id.habitCreating_tueCheckBox:
                        checkBoxStatus(everyDayCheckButton);
                        break;
                    case R.id.habitCreating_wedCheckBox:
                        checkBoxStatus(everyDayCheckButton);
                        break;
                    case R.id.habitCreating_thuCheckBox:
                        checkBoxStatus(everyDayCheckButton);
                        break;
                    case R.id.habitCreating_friCheckBox:
                        checkBoxStatus(everyDayCheckButton);
                        break;
                    case R.id.habitCreating_satCheckBox:
                        checkBoxStatus(everyDayCheckButton);
                        break;
                    case R.id.habitCreating_sunCheckBox:
                        checkBoxStatus(everyDayCheckButton);
                        break;
                }
            }
        };
        monCheckBox.setOnClickListener(checkListener);
        tueCheckBox.setOnClickListener(checkListener);
        wedCheckBox.setOnClickListener(checkListener);
        thuCheckBox.setOnClickListener(checkListener);
        friCheckBox.setOnClickListener(checkListener);
        satCheckBox.setOnClickListener(checkListener);
        sunCheckBox.setOnClickListener(checkListener);
        /**체크박스 클릭 이벤트**/

    }


    Button checkBoxStatus(Button button) {
        if (monCheckBox.isChecked() && tueCheckBox.isChecked() && wedCheckBox.isChecked()  && thuCheckBox.isChecked() && friCheckBox.isChecked() && satCheckBox.isChecked() && sunCheckBox.isChecked() == true) {
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


    Button everyDayButtonStatus(Button button) {
        monCheckBox.setChecked(false);
        tueCheckBox.setChecked(false);
        wedCheckBox.setChecked(false);
        thuCheckBox.setChecked(false);
        friCheckBox.setChecked(false);
        satCheckBox.setChecked(false);
        sunCheckBox.setChecked(false);
        Log.e("컬러1", "" + button.getTextColors().getDefaultColor());
        button.setBackgroundColor(Color.parseColor("#F2EC16"));
        button.setTextColor(Color.parseColor("#ffffff"));
        Log.e("컬러2", "" + button.getTextColors().getDefaultColor());
        return button;
    }


    @Override
    protected void onPause() {
        super.onPause();
//        /**어댑터 인텐트데이터 비활성화**/
//        habitAdapterDataFlag = false;
    }

    @Override
    protected void onDestroy() {
        habitAdapterDataFlag = false;
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        //맵에서 좌표값, 반지름, 주소명 or 이름 받아오는 곳 받아오는 곳
        if (requestCode == REQUEST_LOCATION) {
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);
            radius = data.getDoubleExtra("radius", 0);
            placeNameOrAddress = data.getStringExtra("placeNameOrAddress");
            location.setText(data.getStringExtra("placeNameOrAddress"));

        }
    }


    public void timeSetting(final TextView textView, Context context) {

        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                noticeHour = hourOfDay;
                noticeMinute = minute;
                String am_pm = (hourOfDay < 12) ? "AM" : "PM";
                int hour = (hourOfDay <= 12) ? hourOfDay : hourOfDay % 12;
                String msg = String.format("%d시 %d분 %s", hour, minute, am_pm);
                textView.setText(msg);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();

    }


    public void checkTime(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityE2HabitCreating.this, android.R.style.Theme_Holo_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String am_pm = (hourOfDay < 12) ? "AM" : "PM";
                int mhour = (hourOfDay <= 12) ? hourOfDay : hourOfDay % 12;
                msg = String.format("%d시 %d분 %s", mhour, minute, am_pm);
                checkTimeHour = hourOfDay;
                checkTimeMinute = minute;
                timerStart.setText(checkTimeHour + "시 " + checkTimeMinute + "분");
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }


    public void settingNumber() {
        numberPickerDialog = new Dialog(this);
        numberPickerDialog.setContentView(R.layout.dialog_numberpicker);
        numberPickerDialog.show();

        numberPickerHour = numberPickerDialog.findViewById(R.id.customDialog_hour);
        numberPickerMinute = numberPickerDialog.findViewById(R.id.customDialog_minute);
        Button successButton = numberPickerDialog.findViewById(R.id.customDialog_successButton);
        Button cancelButton = numberPickerDialog.findViewById(R.id.customDialog_cancel);

        numberPickerHour.setMinValue(0);
        numberPickerHour.setMaxValue(24);
        numberPickerMinute.setMinValue(0);
        numberPickerMinute.setMaxValue(60);
        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hourLimit = numberPickerHour.getValue() + checkTimeHour;
                minuteLimit = numberPickerMinute.getValue() + checkTimeMinute;
                if (hourLimit > 24) {
                    hourLimit = hourLimit % 24;
                }
                if (minuteLimit > 60) {
                    minuteLimit = minuteLimit % 60;
                }
                timerFinish.setText(hourLimit + "시 " + minuteLimit + "분 까지");
                numberPickerDialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPickerDialog.dismiss();
                hourLimit = 0;
                minuteLimit = 0;
            }
        });
    }

    void putShared() {
        SharedPreferences sharedPreferences = getSharedPreferences("habitFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONObject jsonObject = new JSONObject();

        if(habitAdapterDataFlag == false){
        long alarmLong = System.currentTimeMillis() / 1000L;
        alarmId = (int) alarmLong;}
//        Log.e("현재시간", "" + System.currentTimeMillis());
        try {
            jsonArray = new JSONArray(sharedPreferences.getString(keyId, ""));
            jsonObject.put("title", title.getText().toString());
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            jsonObject.put("radius", radius);
            jsonObject.put("placeNameOrAddress", placeNameOrAddress);
            jsonObject.put("checkTimeHour", checkTimeHour);
            jsonObject.put("checkTimeMinute", checkTimeMinute);
            jsonObject.put("hourLimit", hourLimit);
            jsonObject.put("minuteLimit", minuteLimit);
            jsonObject.put("preNoticeTime", preNoticeTime.getText().toString());
            jsonObject.put("timerStart", timerStart.getText().toString());
            jsonObject.put("timerFinish", timerFinish.getText().toString());
            jsonObject.put("noticeHour", noticeHour);
            jsonObject.put("noticeMinute", noticeMinute);
            if (everyDayCheckButton.getTextColors().getDefaultColor() == -1) {
                jsonObject.put("mon", true);
                jsonObject.put("tue", true);
                jsonObject.put("wed", true);
                jsonObject.put("thu", true);
                jsonObject.put("fri", true);
                jsonObject.put("sat", true);
                jsonObject.put("sun", true);
            } else {
                jsonObject.put("mon", monCheckBox.isChecked());
                jsonObject.put("tue", tueCheckBox.isChecked());
                jsonObject.put("wed", wedCheckBox.isChecked());
                jsonObject.put("thu", thuCheckBox.isChecked());
                jsonObject.put("fri", friCheckBox.isChecked());
                jsonObject.put("sat", satCheckBox.isChecked());
                jsonObject.put("sun", sunCheckBox.isChecked());
            }
            jsonObject.put("check", false);
            jsonObject.put("alarmId", 0);
            if(habitAdapterDataFlag == true){ //수정창에서 확인 눌렀을 경우 타지는 조건
                jsonArray.put(position,jsonObject);
                habitAdapterDataFlag = false;
            }else { jsonArray.put(jsonObject); }

            alarmIdObject = new JSONObject(sharedPreferences.getString("alarm" + keyId, ""));
            alarmIdObject.put("" + alarmId, alarmId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.putString(keyId, jsonArray.toString());
        editor.putString("alarm" + keyId, alarmIdObject.toString());
        editor.apply();
    }
}
