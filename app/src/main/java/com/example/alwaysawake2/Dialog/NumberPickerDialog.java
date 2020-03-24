package com.example.alwaysawake2.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.alwaysawake2.R;

public class NumberPickerDialog {

    private Dialog dialog;
    private Context context;

    private NumberPicker numberPickerHour;
    private NumberPicker numberPickerMinute;

    private int hour, minute;
    private int[] limitTime = new int[2];
    public NumberPickerDialog(Context context) {
        this.context = context;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

//    public void settingNumber() {
//        dialog = new Dialog(context);
//        dialog.setContentView(R.layout.dialog_numberpicker);
//        dialog.show();
//
//        numberPickerHour = dialog.findViewById(R.id.customDialog_hour);
//        numberPickerMinute = dialog.findViewById(R.id.customDialog_minute);
//        Button successButton = dialog.findViewById(R.id.customDialog_successButton);
//        Button cancelButton = dialog.findViewById(R.id.customDialog_cancel);
//
//        numberPickerHour.setMinValue(0);
//        numberPickerHour.setMaxValue(24);
//        numberPickerMinute.setMinValue(0);
//        numberPickerMinute.setMaxValue(60);
//        successButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                limitTime[0] = numberPickerHour.getValue();
//                limitTime[1] = numberPickerMinute.getValue();
//                dialog.dismiss();
//            }
//        });
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                limitTime[0] = 0;
//                limitTime[1] = 0;
//            }
//        });
//
//
//    }
}
