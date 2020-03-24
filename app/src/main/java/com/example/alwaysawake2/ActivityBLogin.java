package com.example.alwaysawake2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.NotificationClass.AlarmReceiver;
import com.example.alwaysawake2.NotificationClass.AlarmService;
import com.example.alwaysawake2.RecyclerPlan.PlanItem;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

public class ActivityBLogin extends BaseActivity {


   EditText id, password;

    /**로그인버튼, 회원가입창 이동 버튼**/
   Button loginButton, toSignUpButton;
   //쉐어드에서 꺼내온 제이슨에서 꺼낸 패스워드, 아이디
    String jsonPassword, jsonId;;
    String userInformationJsonObject;

    BroadcastReceiver alarmReceiver = new AlarmReceiver();
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_login);



        /**뷰 매칭**/
        id = findViewById(R.id.login_id);
        password = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_loginButton);
        toSignUpButton = findViewById(R.id.login_toSignUpButton);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        /**버튼 클릭 이벤트**/
        Button.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.login_loginButton:

                        SharedPreferences  sharedPreferences = getSharedPreferences("userListFile",MODE_PRIVATE);
                        userInformationJsonObject = sharedPreferences.getString(id.getText().toString(),"");
                        JSONObject jsonObject;

                        try {
                            jsonObject = new JSONObject(userInformationJsonObject);
                            jsonId = jsonObject.getString("id");
                            jsonPassword = jsonObject.getString("password");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(userInformationJsonObject.equals("")){
                            Toast.makeText(ActivityBLogin.this, "아이디를 정확히 입력 해주세요",Toast.LENGTH_SHORT).show();
                        }else if(!password.getText().toString().equals(jsonPassword)){
                            Toast.makeText(ActivityBLogin.this, "아이디와 비밀번호가 일치하지 않습니다",Toast.LENGTH_SHORT).show();
                        }else if(jsonPassword.equals(password.getText().toString())){
                            try {
                                getUserInfo();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            keyId = id.getText().toString();
//                            Intent serviceIntent = new Intent(getApplicationContext(), AlarmService.class);
//                            startService(serviceIntent);
                        startActivity(new Intent(ActivityBLogin.this,ActivityDMain.class));
                        finish();
                        }
                        break;

                    case R.id.login_toSignUpButton:
                        startActivity(new Intent(ActivityBLogin.this,ActivityCSignUp.class));
                        break;


                }
            }
        };
        loginButton.setOnClickListener(onClickListener);
        toSignUpButton.setOnClickListener(onClickListener);
        /**버튼 클릭 이벤트**/

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    void getUserInfo() throws JSONException{
        SharedPreferences sharedPreferences = getSharedPreferences("userListFile", MODE_PRIVATE);
        String user = sharedPreferences.getString(id.getText().toString(),"");
        JSONObject jsonObject = new JSONObject(user);
        keyFilePath = jsonObject.getString("filePath");
        keyPhotoUri = jsonObject.getString("photoUri");
        keyNick = jsonObject.getString("nickName");
    }
}
