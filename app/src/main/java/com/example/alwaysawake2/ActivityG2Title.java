package com.example.alwaysawake2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.alwaysawake2.BaseActivity.BaseActivity;

public class ActivityG2Title extends BaseActivity {

    Button backButton, nextButton;

    EditText title;

    DatePicker finishDatePicker;
    int year, month, day;
    String uriString, filePath;
    int position;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_7_2_title);

        //액티비티 한꺼번에 종료
        acticityArray.add(this);

        /**뷰 매칭**/
        backButton = findViewById(R.id.title_backButton);
        nextButton = findViewById(R.id.title_nextButton);
        title = findViewById(R.id.title_title);
        finishDatePicker = findViewById(R.id.title_finishDatePicker);

        if(projectAdapterDataFlag){
            Intent getIntent = getIntent();
            title.setText(getIntent.getStringExtra("title"));
            position = getIntent.getIntExtra("position", 0);
            year = getIntent.getIntExtra("year", 2019);
            month = getIntent.getIntExtra("month", 1);
            day = getIntent.getIntExtra("day", 1);
            uriString = getIntent.getStringExtra("uriString");
            filePath = getIntent.getStringExtra("filePath");
            finishDatePicker.updateDate(year, month, day);
        }


        /**버튼 클릭 이벤트**/
        Button.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.title_backButton:
                        projectAdapterDataFlag = false; //수정 취소 불린
                        onBackPressed();
                        break;

                    case R.id.title_nextButton:
                        /**타이틀과 데이트피커의 날짜를 인텐트로 날려줌**/
                        year = finishDatePicker.getYear();
                        month = finishDatePicker.getMonth();
                        day = finishDatePicker.getDayOfMonth();
                        Intent intent = new Intent(ActivityG2Title.this, ActivityG3Plan.class);
                        if(projectAdapterDataFlag){
                            intent.putExtra("uriString", uriString);
                            intent.putExtra("position", position);
                            intent.putExtra("filePath",filePath);
                        }
                        intent.putExtra("title",title.getText().toString());
                        intent.putExtra("year",year);
                        intent.putExtra("month",month);
                        intent.putExtra("day",day);

                        //플랜에드버튼 true시 프로젝트 생성란에 들어갔을 때 조건이 작동하고 false 일 때는 생성된 프로젝트 클릭 했을 경우 조건이 발동
                        planAdd = true;
                        startActivity(intent);
                        break;
                }
            }
        };
        backButton.setOnClickListener(onClickListener);
        nextButton.setOnClickListener(onClickListener);
        /**버튼 클릭 이벤트**/

    }
}
