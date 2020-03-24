package com.example.alwaysawake2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.RecyclerPlan.PlanAdapter;
import com.example.alwaysawake2.RecyclerPlan.PlanItem;
import com.example.alwaysawake2.TouchIvent.ItemTouchHelperCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivityG3Plan extends BaseActivity {

    /**
     *
     *
     *
     *
     *
     */
    Button backButton, nextButton, addPlanButton;

    RecyclerView recyclerView;
    ArrayList<PlanItem> arrayList = new ArrayList<>();
    PlanAdapter planAdapter = new PlanAdapter(arrayList, ActivityG3Plan.this);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityG3Plan.this);

    /**타이틀에서 인텐트로 넘긴 값 받아줄 변수**/
    String title, uriString, filePath;
    int year, month, day;


    /**
     * 프로젝트 목록에서 플랜 띄워 줄 때 사용 될 변수, 객체
     */
    JSONObject parentObject;
    JSONArray parentArray;

    int projectPosition;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_7_3_plan);

        /**뷰 매칭**/
        backButton = findViewById(R.id.plan_backButton);
        nextButton = findViewById(R.id.plan_nextButton);
        addPlanButton = findViewById(R.id.plan_addPlanButton);
        recyclerView = findViewById(R.id.plan_recyclerView);

        /**리사이클러뷰 세팅**/
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ActivityG3Plan.this, 1);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(planAdapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
        if(planAdd == true) {
            //타이틀에서 인텐트 값 받기
            getIntentData();

            //액티비티 한꺼번에 종료
            acticityArray.add(this);

            //스와이퍼 드래그
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(planAdapter, arrayList,0));
            itemTouchHelper.attachToRecyclerView(recyclerView);

            if(projectAdapterDataFlag){
                Intent intent = getIntent();
                //해당 포지션 값의 플랜의 중간줄 조건을 주기위해 포지션 값도 같이 넣어준다
                uriString = intent.getStringExtra("uriString");
                projectPosition = intent.getIntExtra("position", 0);
                filePath = intent.getStringExtra("filePath");
                sharedDataLoad();
                setArrayList(projectPosition);
            }
            /**버튼 클릭 이벤트**/
            Button.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.plan_backButton:
                            onBackPressed();
                            break;
                        case R.id.plan_nextButton:
                            Intent intent = new Intent(ActivityG3Plan.this, ActivityG5Picture.class);
                            if(projectAdapterDataFlag){
                                intent.putExtra("uriString", uriString);
                                intent.putExtra("position", projectPosition);
                                intent.putExtra("filePath", filePath);
                            }
                            intent.putExtra("title", title);
                            intent.putExtra("year", year);
                            intent.putExtra("month", month);
                            intent.putExtra("day", day);
                            intent.putExtra("array", arrayList);

                            startActivity(intent);
                            break;
                        case R.id.plan_addPlanButton:
                            startActivityForResult(new Intent(ActivityG3Plan.this, ActivityG4AddPlan.class), REQUEST_ADDPLAN);
                            break;
                    }
                }
            };
            backButton.setOnClickListener(onClickListener);
            nextButton.setOnClickListener(onClickListener);
            addPlanButton.setOnClickListener(onClickListener);
            /**버튼 클릭 이벤트**/
        }
        else if(planAdd == false){
            sharedDataLoad();
            Intent intent = getIntent();
            //해당 포지션 값의 플랜의 중간줄 조건을 주기위해 포지션 값도 같이 넣어준다
            projectPosition = intent.getIntExtra("projectPosition", 0);
            setArrayList(projectPosition);
            //버튼 인비저블 처리
            nextButton.setVisibility(View.GONE);
            addPlanButton.setVisibility(View.GONE);



            /**뒤로가기 버튼**/
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("arrayList",""+ arrayList);
                    onBackPressed();
                }
            });

        }






    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_ADDPLAN) {
            int deadlineYear, deadlineMonth, deadlineDay, noticeTimeHour, noticeTimeMinute;

            /**addPlan에서 가져온 데이터들**/
            String title = data.getExtras().getString("title");
            String bodyText = data.getExtras().getString("bodyText");
            String noticeTime = data.getExtras().getString("noticeTime");
            float importance = data.getFloatExtra("importance", 0);
            deadlineYear = data.getIntExtra("deadlineYear", 0);
            deadlineMonth = data.getIntExtra("deadlineMonth", 0);
            deadlineDay = data.getIntExtra("deadlineDay", 0);
            noticeTimeHour = data.getIntExtra("noticeTimeHour",0);
            noticeTimeMinute = data.getIntExtra("noticeTimeMinute", 0);
            long noticeMilliTime = data.getLongExtra("noticeMilliTime", 0);
            /**addPlan에서 가져온 데이터들**/
            PlanItem planItem = new PlanItem(title, bodyText, noticeTime, importance, deadlineDay, deadlineMonth,
                    deadlineYear,false,noticeMilliTime, noticeTimeHour,noticeTimeMinute);
            Log.d("입력", "");
            arrayList.add(planItem);
            //어레이 사이즈가 같이 변하는 원인 파악이 아직 안됨
//            Log.d("arraySize", ""+arrayList.size());
            planAdapter.notifyDataSetChanged();
        } else if (requestCode == REQUEST_PLANEDIT) {
            int deadlineYear, deadlineMonth, deadlineDay, noticeTimeHour, noticeTimeMinute;
            String title = data.getExtras().getString("title");
            String bodyText = data.getExtras().getString("bodyText");
            String noticeTime = data.getExtras().getString("noticeTime");
            float importance = data.getFloatExtra("importance", 0);
            int position = data.getIntExtra("planGetCount", 0);
            deadlineYear = data.getIntExtra("deadlineYear", 0);
            deadlineMonth = data.getIntExtra("deadlineMonth", 0);
            deadlineDay = data.getIntExtra("deadlineDay", 0);
            noticeTimeHour = data.getIntExtra("noticeTimeHour",0);
            noticeTimeMinute = data.getIntExtra("noticeTimeMinute", 0);
            long noticeMilliTime = data.getLongExtra("noticeMilliTime", 0);
            PlanItem planItem = new PlanItem(title, bodyText, noticeTime, importance, deadlineDay, deadlineMonth,
                                            deadlineYear, false,noticeMilliTime, noticeTimeHour, noticeTimeMinute);
            arrayList.set(position, planItem);
            planAdapter.notifyItemChanged(position);

        }
    }


    void getIntentData(){
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month",0);
        day = intent.getIntExtra("day",0);
    }




    void sharedDataLoad(){
        SharedPreferences sharedPreferences = getSharedPreferences("projectListFile",MODE_PRIVATE);
        String jsonObjectString = sharedPreferences.getString(keyId, "");
        try {
            parentObject = new JSONObject(jsonObjectString);
            parentArray = parentObject.getJSONArray("plan");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void setArrayList(int position){
        JSONArray childArray;
        JSONObject jsonObject;
//        PlanItem planItem = new PlanItem();
        Intent intent = getIntent();
        //해당 포지션 값의 플랜의 중간줄 조건을 주기위해 포지션 값도 같이 넣어준다


        try {
            childArray = parentArray.getJSONArray(position);
            for(int i = 0; i < childArray.length(); i++){
                String jsonObjectString = childArray.getString(i);
                jsonObject = new JSONObject(jsonObjectString);
                PlanItem planItem = new PlanItem();
                planItem.setTitle(jsonObject.getString("title"));
                planItem.setBodyText(jsonObject.getString("bodyText"));
                planItem.setNoticeTime(jsonObject.getString("noticeTime"));
                planItem.setImportance((float) jsonObject.getDouble("importance"));
                planItem.setDeadlineYear(jsonObject.getInt("deadlineYear"));
                planItem.setDeadlineMonth(jsonObject.getInt("deadlineMonth"));
                planItem.setDeadlineDay(jsonObject.getInt("deadlineDay"));
                planItem.setSuccessPLan(jsonObject.getBoolean("successPlan"));
                arrayList.add(planItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        planAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(planAdd == false){ arrayToJsonArray();}
    }


    void arrayToJsonArray() {
        SharedPreferences sharedPreferences = getSharedPreferences("projectListFile",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray childArray;
        try {
            childArray = parentArray.getJSONArray(projectPosition);
            for(int i = 0; i < arrayList.size(); i++){
                JSONObject jsonObject = childArray.getJSONObject(i);
                jsonObject.put("successPlan", arrayList.get(i).isSuccessPLan());
                childArray.put(i,jsonObject);
            }
//            Log.d("JSON Test", childArray.toString()) 로그삭제;
            parentArray.put(projectPosition,childArray);
            parentObject.put("plan",parentArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.putString(keyId,parentObject.toString());
        editor.apply();
    }



}
