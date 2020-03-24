package com.example.alwaysawake2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.RecyclerGoodWriting.LikeAdapter;


public class ActivityH1Like extends BaseActivity {

    RecyclerView recyclerView;

    Button toHomeButton, toProjectButton, toHabitButton, toGoodWritingButton;

    LikeAdapter likeAdapter = new LikeAdapter(this);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_8_1_like);


        /**뷰 매칭**/
        toHomeButton = findViewById(R.id.like_toHomeButton);
        toProjectButton = findViewById(R.id.like_toProjectButton);
        toHabitButton = findViewById(R.id.like_toHabitButton);
        toGoodWritingButton = findViewById(R.id.like_toGoodWritingButton);
        recyclerView = findViewById(R.id.like_recyclerView);
        recyclerView.setAdapter(likeAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);


        /**버튼 클릭 이벤트**/
        Button.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.like_toHomeButton:
                        finish();
                        break;
                    case R.id.like_toProjectButton:
                        startActivity(new Intent(ActivityH1Like.this,ActivityG1Project.class));
                        finish();
                        break;
                    case R.id.like_toHabitButton:
                        startActivity(new Intent(ActivityH1Like.this,ActivityE1Habit.class));
                        finish();
                        break;
                    case R.id.like_toGoodWritingButton:
                        startActivity(new Intent(ActivityH1Like.this,ActivityF1GoodWriting.class));
                        finish();
                        break;
                }
            }
        };
        toHomeButton.setOnClickListener(onClickListener);
        toProjectButton.setOnClickListener(onClickListener);
        toHabitButton.setOnClickListener(onClickListener);
        toGoodWritingButton.setOnClickListener(onClickListener);
        /**버튼 클릭 이벤트**/


    }

    @Override
    protected void onResume() {
        super.onResume();
        likeAdapter.notifyDataSetChanged();
    }
}
