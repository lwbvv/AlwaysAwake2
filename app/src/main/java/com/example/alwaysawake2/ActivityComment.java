package com.example.alwaysawake2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.RecyclerComment.CommentAdapter;
import com.example.alwaysawake2.RecyclerComment.CommentItem;
import com.example.alwaysawake2.TouchIvent.ItemTouchHelperCallback;
import com.google.gson.JsonObject;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class ActivityComment extends BaseActivity {
    TextView commentWriting, addCommentButton;
    Button backButton;
    RecyclerView recyclerView;
    int position;
    ArrayList<CommentItem> arrayList = new ArrayList<>();
    CommentAdapter commentAdapter = new CommentAdapter(ActivityComment.this,arrayList);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityComment.this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        Calendar calendar = Calendar.getInstance();
        cyear = calendar.get(Calendar.YEAR);
        cmonth = calendar.get(Calendar.MONTH);
        cday = calendar.get(Calendar.DATE);
        setContentView(R.layout.activity_comment);
        try {
            getArray();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /**매칭**/
        commentWriting = findViewById(R.id.comment_commentWriting);
        addCommentButton = findViewById(R.id.comment_addCommentButton);
        backButton = findViewById(R.id.comment_backButton);
        recyclerView = findViewById(R.id.comment_recyclerView);
        //스와이퍼 드래그
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(commentAdapter, arrayList));
        itemTouchHelper.attachToRecyclerView(recyclerView);


        /**리사이클러뷰 세팅**/
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(commentAdapter);


        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentWriting.getText().length() == 0) {
                } else {
                    CommentItem commentItem = new CommentItem(keyId,commentWriting.getText().toString(),keyFilePath,keyPhotoUri,keyNick,cyear,cmonth,cday);
                    arrayList.add(commentItem);
                    commentWriting.setText("");
                    commentAdapter.notifyDataSetChanged();
                }
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityComment.this, ActivityF1GoodWriting.class));
                finish();
            }
        });


    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            saved();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    void saved() throws JSONException {
        SharedPreferences sharedPreferences = getSharedPreferences("goodWriting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String cString = sharedPreferences.getString("comment", "");
        JSONArray parentArray = new JSONArray();
        JSONArray childArray = new JSONArray();
        parentArray = new JSONArray(cString);

        for(int i = 0; i < arrayList.size(); i++){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", arrayList.get(i).getId());
            jsonObject.put("comment", arrayList.get(i).getComment());
            jsonObject.put("year", arrayList.get(i).getYear());
            jsonObject.put("month", arrayList.get(i).getMonth());
            jsonObject.put("day", arrayList.get(i).getDay());
            jsonObject.put("filePath", arrayList.get(i).getFilePath());
            jsonObject.put("photoUri", arrayList.get(i).getPhotoUri());
            jsonObject.put("nick",arrayList.get(i).getNick());
            childArray.put(jsonObject);
        }
        parentArray.put(position,childArray);
        editor.putString("comment",parentArray.toString());
        editor.apply();
    }

    void getArray() throws JSONException{
        SharedPreferences sharedPreferences = getSharedPreferences("goodWriting", MODE_PRIVATE);
        String cString = sharedPreferences.getString("comment", "");
        JSONArray parentArray = new JSONArray();
        JSONArray childArray = new JSONArray();
        parentArray = new JSONArray(cString);
        childArray = parentArray.getJSONArray(position);

        for(int i = 0; i < childArray.length(); i++){
            JSONObject jsonObject = childArray.getJSONObject(i);
            CommentItem commentItem = new CommentItem(jsonObject.getString("id"),jsonObject.getString("comment"),jsonObject.getString("filePath"),jsonObject.getString("photoUri"),jsonObject.getString("nick")
            ,jsonObject.getInt("year"),jsonObject.getInt("month"),jsonObject.getInt("day"));
            arrayList.add(commentItem);
        }
    }
}
