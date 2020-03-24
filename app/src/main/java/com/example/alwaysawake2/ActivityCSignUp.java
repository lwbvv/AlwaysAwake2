package com.example.alwaysawake2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.BaseActivity.TextFilter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityCSignUp extends BaseActivity {


    EditText id, nickName, password, passwordCheck;

    Button successButton, cancelButton;
    ImageView profileImage;
    Uri photoUri = Uri.parse("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQ2-gnBL2LYQFu_TT2KfG8MzmLFfurXzbZlcQyeq_95Ls8Mxu6r");
    TextFilter textFilter = new TextFilter();
    InputFilter[] idFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_signup);

        /**뷰 매칭**/
        id = findViewById(R.id.signUp_id);
        nickName = findViewById(R.id.signUp_nickName);
        password = findViewById(R.id.signUp_password);
        passwordCheck = findViewById(R.id.signUp_passwordCheck);
        successButton = findViewById(R.id.signUp_successButton);
        cancelButton = findViewById(R.id.signUp_cancelButton);
        profileImage = findViewById(R.id.signUp_profileImage);
       profileImage = circleImage(profileImage);

        idFilter = textFilter.setIdFilter(ActivityCSignUp.this);
        id.setFilters(idFilter);
        nickName.setFilters(textFilter.setNickFilter(ActivityCSignUp.this));
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password.setFilters(textFilter.setPassFilter(ActivityCSignUp.this));
        passwordCheck.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordCheck.setFilters(textFilter.setPassFilter(ActivityCSignUp.this));
        /**버튼 클릭 이벤트**/
        Button.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.signUp_successButton:
                        if (photoUri == null) {
                            Toast.makeText(ActivityCSignUp.this, "프로필 사진을 선택하세요", Toast.LENGTH_SHORT).show();
                        } else if (id.getText().length() < 4) {
                            Toast.makeText(ActivityCSignUp.this, "ID는 4자 이상 입력하십시오", Toast.LENGTH_SHORT).show();
                        } else if (nickName.getText().length() < 4) {
                            Toast.makeText(ActivityCSignUp.this, "닉네입 4자 이상 입력하십시오", Toast.LENGTH_SHORT).show();
                        } else if (password.getText().length() < 6) {
                            Toast.makeText(ActivityCSignUp.this, "패스워드 6자 이상 입력하세요", Toast.LENGTH_SHORT).show();
                        } else if (!password.getText().toString().equals(passwordCheck.getText().toString())) {
                            Toast.makeText(ActivityCSignUp.this, "패스워드가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                        } else {
                            membershipFile();
                            projectListFile();
                            goodWritingFile();
                            habitFile();
                            try {
                                putGoodWritingHeart();
                            } catch (JSONException e) { e.printStackTrace(); }
                            finish();
                        }
                        break;

                    case R.id.signUp_cancelButton:
                        startActivity(new Intent(ActivityCSignUp.this, ActivityBLogin.class));
                        break;

                    case R.id.signUp_profileImage:
                        final CharSequence[] charSequences = new CharSequence[]{"갤러리", "카메라"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityCSignUp.this);
                        builder.setTitle("이미지 선택");
                        builder.setItems(charSequences, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                switch (which) {
                                    case 0://갤러리
                                        startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_PICK).setType("image/*"), "Choose an image"), REQUEST_DATA_GALLERY);
                                        Toast.makeText(ActivityCSignUp.this, charSequences[0].toString(), Toast.LENGTH_SHORT).show();
                                        break;

                                    case 1://카메라
                                        cameraRequestPermission(ActivityCSignUp.this, ActivityCSignUp.this, photoUri);
                                        Toast.makeText(ActivityCSignUp.this, charSequences[1].toString(), Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                        builder.show();
                        break;
                }
            }
        };
        successButton.setOnClickListener(onClickListener);
        cancelButton.setOnClickListener(onClickListener);
        profileImage.setOnClickListener(onClickListener);
        /**버튼 클릭 이벤트**/

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_DATA_CAMERA: {
                Bitmap bitmapRotated = imageRotated(getImageFilePath(), ActivityCSignUp.this, photoUri);
                profileImage.setImageBitmap(bitmapRotated);
            }
            break;
            case REQUEST_DATA_GALLERY: {
                //갤러리에서 uri 가져오기
                photoUri = data.getData();
                Bitmap bitmapRotated = galleryCursor(ActivityCSignUp.this, photoUri);
                profileImage.setImageBitmap(bitmapRotated);
            }
            break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onRequestPermissionsResultBody(requestCode, grantResults, ActivityCSignUp.this);
    }

    public void membershipFile() {
        SharedPreferences sharedPreferences = getSharedPreferences("userListFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String getIdArray = sharedPreferences.getString("idList", "[]");
        JSONObject jsonObject = new JSONObject();
        JSONArray idArray = new JSONArray();
        try {
            idArray = new JSONArray(getIdArray);
            jsonObject.put("id", id.getText().toString());
            jsonObject.put("password", password.getText().toString());
            jsonObject.put("nickName", nickName.getText().toString());
            jsonObject.put("photoUri", photoUri.toString());
            jsonObject.put("filePath", getImageFilePath());
            idArray.put(id.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**아이디, 비밀번호, 패스워드 데이터를 담은 오브젝트를 userListFile 쉐어드에 key값을 id로 하여 담는다**/
        editor.putString(id.getText().toString(), jsonObject.toString());
        editor.putString("idList", idArray.toString());
        editor.apply();
    }

    public void projectListFile() {
        SharedPreferences sharedPreferences = getSharedPreferences("projectListFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONObject parentObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray planArray = new JSONArray();
        JSONArray planNoticeTime = new JSONArray();
        try {
            parentObject.put("projectList", jsonArray);
            parentObject.put("plan", planArray);
            parentObject.put("planNoticeTime", planNoticeTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.putString(id.getText().toString(), parentObject.toString());
        editor.apply();
    }


    public void goodWritingFile() {
        SharedPreferences sharedPreferences = getSharedPreferences("goodWriting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String goodWriting = sharedPreferences.getString("goodWriting", "[]");
        String heart = sharedPreferences.getString("goodWritingHeart", "[]");
        String comment = sharedPreferences.getString("comment", "[]");
        JSONArray jsonArray = new JSONArray();
        JSONArray parentArray = new JSONArray();

        //아이디 마다 하트 불린 값 넣을 어레이와 오브젝트
        JSONArray heartArray = new JSONArray();
        try {
            jsonArray = new JSONArray(goodWriting);
            parentArray = new JSONArray(comment);
            heartArray = new JSONArray(heart);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        /**아이디, 비밀번호, 패스워드 데이터를 담은 오브젝트를 userListFile 쉐어드에 key값을 id로 하여 담는다**/
        editor.putString("goodWriting", jsonArray.toString());
        editor.putString("goodWritingHeart", heartArray.toString());
        editor.putString("comment", parentArray.toString());
        editor.apply();
    }


    //아이디에 좋은 글귀 리스트의 모든 heart를 넣어준다
    void putGoodWritingHeart() throws JSONException {
        SharedPreferences sharedGoodWriting = getSharedPreferences("goodWriting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedGoodWriting.edit();
        String goodWritingList = sharedGoodWriting.getString("goodWriting", "");
        JSONArray list = new JSONArray(goodWritingList);
        String heartArrayString = sharedGoodWriting.getString("goodWritingHeart", "");
        JSONArray heartArray = new JSONArray(heartArrayString);
        JSONObject heartObject;

        /**
         * 좋은 글귀의 수 만큼 하트 불린 값을 담아놓은 어레이의 길이만큼 for 문을 돌리고 어레이 안에서 object를 꺼낸뒤에 id+"heart"로 키값을 줘서 생성한 아이디의 불린값을 리스트마다 담아준다.
         */
        for (int i = 0; i < list.length(); i++) {
            heartObject = heartArray.getJSONObject(i);
            heartObject.put(id.getText().toString() + "heart", false);
            heartArray.put(i, heartObject);
        }
        editor.putString("goodWritingHeart", heartArray.toString());
        editor.apply();
    }


    private void habitFile() {
        SharedPreferences sharedPreferences = getSharedPreferences("habitFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray jsonArray = new JSONArray();
        JSONObject alarmId = new JSONObject();
        editor.putString(id.getText().toString(), jsonArray.toString());
        editor.putString("alarm" + id.getText().toString(), alarmId.toString());
        editor.apply();
    }

}
