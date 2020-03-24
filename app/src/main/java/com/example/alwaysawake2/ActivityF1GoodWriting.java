package com.example.alwaysawake2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.RecyclerGoodWriting.GoodWritingAdapter;
import com.example.alwaysawake2.RecyclerGoodWriting.GoodWritingItem;

import java.util.ArrayList;

public class ActivityF1GoodWriting extends BaseActivity {

    Button addWriting;
    Button toHomeButton, toProjectButton, toHabitButton, toLikeButton;

    GoodWritingAdapter goodWritingAdapter = new GoodWritingAdapter(ActivityF1GoodWriting.this);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityF1GoodWriting.this);
    RecyclerView recyclerView;
    CheckTypesTask checkTypesTask;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6_1_goodwriting);
        checkTypesTask = new CheckTypesTask();
        checkTypesTask.execute();
        /**뷰 매칭**/
        addWriting = findViewById(R.id.goodWriting_addWriting);
        toHomeButton = findViewById(R.id.goodWriting_toHomeButton);
        toProjectButton = findViewById(R.id.goodWriting_toProjectButton);
        toHabitButton = findViewById(R.id.goodWriting_toHabitButton);
        toLikeButton = findViewById(R.id.goodWriting_toLikeButton);
        recyclerView = findViewById(R.id.goodWriting_recyclerView);

        /**리사이클러뷰 세팅**/
        recyclerView.setAdapter(goodWritingAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        /**버튼 이벤트**/
        Button.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.goodWriting_addWriting:
                        startActivity(new Intent(ActivityF1GoodWriting.this, ActivityF2Writing.class));
                        break;
                    case R.id.goodWriting_toHomeButton:
//                        startActivity(new Intent(ActivityF1GoodWriting.this,ActivityDMain.class));
                        finish();
                        break;
                    case R.id.goodWriting_toProjectButton:
                        startActivity(new Intent(ActivityF1GoodWriting.this,ActivityG1Project.class));
                        finish();
                        break;
                    case R.id.goodWriting_toHabitButton:
                        startActivity(new Intent(ActivityF1GoodWriting.this,ActivityE1Habit.class));
                        finish();
                        break;
                    case R.id.goodWriting_toLikeButton:
                        startActivity(new Intent(ActivityF1GoodWriting.this,ActivityH1Like.class));
                        finish();
                        break;
                }
            }
        };
        addWriting.setOnClickListener(onClickListener);
        toHomeButton.setOnClickListener(onClickListener);
        toProjectButton.setOnClickListener(onClickListener);
        toHabitButton.setOnClickListener(onClickListener);
        toLikeButton.setOnClickListener(onClickListener);
        /**버튼 이벤트**/

    }

    @Override
    protected void onResume() {
        super.onResume();
        goodWritingAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkTypesTask.cancel(true);
    }

    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                ActivityF1GoodWriting.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중입니다..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 5; i++) {
                    //asyncDialog.setProgress(i * 30);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }
}
