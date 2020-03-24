package com.example.alwaysawake2;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.RecyclerPlan.PlanItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ActivityG5Picture extends BaseActivity {

    Button backButton, successButton, cameraButton, galleryButton;
    ImageView pictureView;

    public static Uri photoUri;
    JSONObject parentObject = null;
    JSONArray noticeArray = new JSONArray();
    ArrayList<PlanItem> arrayList;
    JSONArray childArray = new JSONArray();
    /**
     * 타이틀에서 인텐트로 넘긴 값 받아줄 변수
     **/
    String title, filePath;
    int year, month, day;
    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_7_5_picture);

        //액티비티 한꺼번에 종료
        acticityArray.add(this);

        /**뷰 매칭**/
        backButton = findViewById(R.id.picture_backButton);
        successButton = findViewById(R.id.picture_successButton);
        cameraButton = findViewById(R.id.picture_cameraButton);
        galleryButton = findViewById(R.id.picture_galleryButton);
        pictureView = findViewById(R.id.picture_pictureView);
        //타이틀하고 날짜정보 인텐트로 받아옴
        getIntentData();

        /**버튼 클릭 이벤트**/
        Button.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.picture_backButton:
                        onBackPressed();
                        break;
                    case R.id.picture_successButton:

                        if (photoUri != null) {
                            arrayToJsonArray();
                            //안에 미리알림 서비스에서 돌릴 때 쓸 밀리타임 제이슨 어레이 변환 함수 들어있음(projectList)
                            projectList();
                            activityFinish();
                        } else {
                            Toast.makeText(ActivityG5Picture.this, "이미지를 선택해주세요", Toast.LENGTH_SHORT).show();
                        }
                        //데이터와 같이 전송
                        break;
                    case R.id.picture_cameraButton:
                        photoUri = cameraRequestPermission(ActivityG5Picture.this, ActivityG5Picture.this, photoUri);
                        break;
                    case R.id.picture_galleryButton:
                        startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_PICK).setType("image/*"), "Choose an image"), REQUEST_DATA_GALLERY);
                        break;
                }
            }
        };
        backButton.setOnClickListener(onClickListener);
        successButton.setOnClickListener(onClickListener);
        cameraButton.setOnClickListener(onClickListener);
        galleryButton.setOnClickListener(onClickListener);
        /**버튼 클릭 이벤트**/


    }

    /**
     * 권한 결과값 받아오기
     **/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onRequestPermissionsResultBody(requestCode, grantResults, ActivityG5Picture.this);
    }
    /**권한 결과값 받아오기**/


    /**
     * 결과값 받아오기
     **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_DATA_CAMERA: {
                Bitmap bitmapRotated = imageRotated(getImageFilePath(), ActivityG5Picture.this, photoUri);
                pictureView.setImageBitmap(bitmapRotated);
                pictureView.setScaleType(ImageView.ScaleType.FIT_XY);
                cameraOrgallery = true;
            }
            break;
            case REQUEST_DATA_GALLERY: {
                //갤러리에서 uri 가져오기
                photoUri = data.getData();
                Bitmap bitmapRotated = galleryCursor(ActivityG5Picture.this, photoUri);
                pictureView.setImageBitmap(bitmapRotated);
                cameraOrgallery = false;
            }
            break;
        }
    }

    /**
     * 결과값 받아오기
     **/
    void getIntentData() {
        Intent intent = getIntent();
        if (projectAdapterDataFlag) {
            String uriString = intent.getStringExtra("uriString");
            position = intent.getIntExtra("position", 0);
            filePath = intent.getStringExtra("filePath");
            setImageFilePath(filePath);
            photoUri = Uri.parse(uriString);
            Bitmap bitmapRotated = imageRotated(getImageFilePath(), ActivityG5Picture.this, photoUri);
            pictureView.setImageBitmap(bitmapRotated);
            pictureView.setScaleType(ImageView.ScaleType.FIT_XY);
            cameraOrgallery = true;
        }
        title = intent.getStringExtra("title");
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        day = intent.getIntExtra("day", 0);
        arrayList = (ArrayList<PlanItem>) intent.getSerializableExtra("array");

    }

    void arrayToJsonArray() {
        try {
            for (int i = 0; i < arrayList.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", arrayList.get(i).getTitle());
                jsonObject.put("bodyText", arrayList.get(i).getBodyText());
                jsonObject.put("noticeTime", arrayList.get(i).getNoticeTime());
                jsonObject.put("importance", arrayList.get(i).getImportance());
                jsonObject.put("deadlineYear", arrayList.get(i).getDeadlineYear());
                jsonObject.put("deadlineMonth", arrayList.get(i).getDeadlineMonth());
                jsonObject.put("deadlineDay", arrayList.get(i).getDeadlineDay());
                jsonObject.put("successPlan", arrayList.get(i).isSuccessPLan());
                childArray.put(jsonObject);
            }
//            Log.d("JSON Test", childArray.toString()) 로그삭제;

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void projectList() {
        SharedPreferences sharedPreferences = getSharedPreferences("projectListFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String parentObjectString = sharedPreferences.getString(keyId, "");
        String uriString = photoUri.toString();

        JSONArray jsonArray;
        JSONObject childObject = new JSONObject();

        //화원 가입할 때 저장 시킨 플랜 어레이 불러올 어레이
        JSONArray parentArray;
        try {
            parentObject = new JSONObject(parentObjectString);
            jsonArray = parentObject.getJSONArray("projectList");
            childObject.put("title", title);
            childObject.put("year", year);
            childObject.put("month", month);
            childObject.put("day", day);
            childObject.put("uriString", uriString);
            childObject.put("filePath", getImageFilePath());
            if (projectAdapterDataFlag) {
                jsonArray.put(position,childObject);
            }else {
                jsonArray.put(childObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        arraySortToJsonArray();
        /**플랜 목록 저장**/
        try {
            parentArray = parentObject.getJSONArray("plan");
            if(projectAdapterDataFlag){
                parentArray.put(position,childArray);
            }else { parentArray.put(childArray);}
        } catch (JSONException e) { e.printStackTrace(); }
        /**플랜 목록 저장**/


        editor.putString(keyId, parentObject.toString());
        editor.apply();
    }


    void arraySortToJsonArray() {

        try {
            noticeArray = parentObject.getJSONArray("planNoticeTime");
            for (int i = 0; i < arrayList.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", arrayList.get(i).getTitle());
                jsonObject.put("noticeMilliTime", arrayList.get(i).getNoticeMilliTime());
                jsonObject.put("noticeTimeHour", arrayList.get(i).getNoticeTimeHour());
                jsonObject.put("noticeTimeMinute", arrayList.get(i).getNoticeTimeMinute());
                noticeArray.put(jsonObject);
            }
//            Log.d("JSON Test", childArray.toString()) 로그삭제;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
