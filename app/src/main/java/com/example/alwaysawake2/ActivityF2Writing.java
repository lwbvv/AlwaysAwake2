package com.example.alwaysawake2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.alwaysawake2.BaseActivity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ActivityF2Writing extends BaseActivity {

    Button backButton, successButton, cameraButton, galleryButton;

    ImageView pictureView;

    EditText writing;

    public static Uri writingPhotoUri;

    Bitmap bitmapResizing;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6_2_writing);

        /**매칭**/
        backButton = findViewById(R.id.writing_backButton);
        successButton = findViewById(R.id.writing_successButton);
        cameraButton = findViewById(R.id.writing_cameraButton);
        galleryButton = findViewById(R.id.writing_galleryButton);
        pictureView = findViewById(R.id.writing_pictureView);
        writing = findViewById(R.id.writing_writing);


        /**버튼 이벤트**/
        Button.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.writing_backButton:
                        startActivity(new Intent(ActivityF2Writing.this, ActivityF1GoodWriting.class));
                        finish();
                        break;
                    case R.id.writing_successButton:
                        writingSaved();
                        putHeartBoolean();
                        finish();
                        break;
                    case R.id.writing_cameraButton:
                        writingPhotoUri = cameraRequestPermission(ActivityF2Writing.this, ActivityF2Writing.this, writingPhotoUri);
                        break;
                    case R.id.writing_galleryButton:
                        startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_PICK).setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*"), "Choose an image"), REQUEST_DATA_GALLERY);
                        break;
                }
            }
        };
        backButton.setOnClickListener(onClickListener);
        successButton.setOnClickListener(onClickListener);
        cameraButton.setOnClickListener(onClickListener);
        galleryButton.setOnClickListener(onClickListener);
        /**버튼 이벤트**/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_DATA_CAMERA: {
                Bitmap bitmapRotated = imageRotated(getImageFilePath(), ActivityF2Writing.this, writingPhotoUri);
                pictureView.setImageBitmap(bitmapRotated);
                pictureView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                cameraOrgallery = true;
            }
            break;
            case REQUEST_DATA_GALLERY: {
                //갤러리에서 uri 가져오기
                writingPhotoUri = data.getData();
                Bitmap bitmapRotated = galleryCursor(ActivityF2Writing.this, writingPhotoUri);
                pictureView.setImageBitmap(bitmapRotated);
                cameraOrgallery = false;
            }
            break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onRequestPermissionsResultBody(requestCode, grantResults, ActivityF2Writing.this);
    }


    void writingSaved() {
        sharedPreferences = getSharedPreferences("goodWriting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String goodWritingList = sharedPreferences.getString("goodWriting", "");
        JSONArray jsonArray = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonArray = new JSONArray(goodWritingList);
            jsonObject.put("writingPhotoUri", writingPhotoUri.toString()); //이미지 유알아이
            jsonObject.put("filePath", getImageFilePath()); //이미지 패치파일
            jsonObject.put("writing", writing.getText().toString());
            jsonObject.put("keyId", keyId);
            jsonObject.put("likeCount", 0);
            jsonArray.put(jsonObject);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        editor.putString("goodWriting", jsonArray.toString());
        editor.apply();
    }

    void putHeartBoolean(){
        sharedPreferences = getSharedPreferences("goodWriting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences getIdPreferences = getSharedPreferences("userListFile", MODE_PRIVATE);
        String getIdArrayString = getIdPreferences.getString("idList", "");
        String heartArrayString = sharedPreferences.getString("goodWritingHeart", "");


        JSONArray idArray = null;
        JSONArray heartArray = null;
        JSONObject heartObject = new JSONObject();
        try {
            //아이디 값 불러올 제이슨 어레이
            idArray = new JSONArray(getIdArrayString);

            //좋아요 불린값 아이디별로 오브젝트에 담은 뒤에 어레이에 담아주기
            heartArray = new JSONArray(heartArrayString);
            for(int i = 0; i < idArray.length(); i++){
                String id = idArray.getString(i);
                heartObject.put(id+"heart",false);
            }
            heartArray.put(heartObject);
            //좋아요 불린값 아이디별로 오브젝트에 담은 뒤에 어레이에 담아주기

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        editor.putString("goodWritingHeart", heartArray.toString());
        editor.apply();
    }


}
