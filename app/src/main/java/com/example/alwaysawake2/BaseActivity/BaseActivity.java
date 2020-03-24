package com.example.alwaysawake2.BaseActivity;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.icu.text.SimpleDateFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alwaysawake2.ActivityF2Writing;
import com.example.alwaysawake2.Dialog.NumberPickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BaseActivity extends AppCompatActivity {

   public final int REQUEST_SIGNUP = 1000;
   public final int REQUEST_CREATEHABIT = 2000;
   public final int REQUEST_WRITING = 3000;
   public final int REQUEST_ADDPLAN = 4000;
   public final int REQUEST_HABITEDIT = 5000;
   public final int REQUEST_PLANEDIT = 6000;
   public final int REQUEST_ADDPROJECT = 7000;
   public final int REQUEST_PERMISSION_CAMERA = 8000;
   public final int REQUEST_DATA_CAMERA = 8001;
   public final int REQUEST_DATA_GALLERY = 8002;
   public static boolean habitAdapterDataFlag;
   public static boolean planAdapterDataFlag;
   public static boolean projectAdapterDataFlag;
   public final int REQUEST_LOCATION = 9000;
   public final int REQUEST_PERMISSION_LOCATION = 9001;
   //트루일경우 카메라에 있는 파일패치 가져오고 false일 경우 갤러리의 uri가져옴
   public static boolean cameraOrgallery;
   public final int REQUEST_ALARM = 3000;
   public static String keyId;
   public static String keyFilePath;
   public static String keyPhotoUri;
   public static String keyNick;
   public static Boolean allPlan = false;
   public static int cyear, cmonth, cday;

   //프로젝트에서 아이템을 클릭해서 계획목록으로 갔을 때 사용 될 불린 값 true일 경우 에드쪽 활성화 false일경우 보여주기쪽 활성화
   public static boolean planAdd;


   private int hourLimit, minuteLimit;
   private String msg;

   public int getHourLimit() {
      return hourLimit;
   }

   public void setHourLimit(int hourLimit) {
      this.hourLimit = hourLimit;
   }

   public int getMinuteLimit() {
      return minuteLimit;
   }

   public void setMinuteLimit(int minuteLimit) {
      this.minuteLimit = minuteLimit;
   }

   public String getMsg() {
      return msg;
   }

   public void setMsg(String msg) {
      this.msg = msg;
   }





   /**카메라 권한 요청**/
  public Uri cameraRequestPermission(Context context, Activity activity, Uri photoUri) {
     int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
     if(permissionCheck == PackageManager.PERMISSION_DENIED){
        //권한 없음
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA},REQUEST_PERMISSION_CAMERA);
     }else{
        photoUri = sendTakePhotoIntent(context,photoUri);
     }
     return photoUri;
  }
   /**카메라 권한 요청**/

   /**카메라 권한 받아왔는지 확인??**/
  public void onRequestPermissionsResultBody(int requestCode, int[] granResults, Context context){
      switch (requestCode){
         //리퀘스트 코드와 일치 한다면
         case REQUEST_PERMISSION_CAMERA:
            if(granResults.length > 0 && granResults[0] == PackageManager.PERMISSION_GRANTED){
               //승인결과값이 0보다 클경우 그리고 승인결과의 0번째 배열의 값이 패키지매니저의 퍼미션 승인 상수와 동일하면 권한 승인이 이루어 진다
               //권한 승인
            }else{
               Toast.makeText(context, "권한을 받아 오십시오", Toast.LENGTH_SHORT).show();
            }break;
      }
   }


   protected Uri sendTakePhotoIntent(Context context, Uri photoUri){
      Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      if(takePictureIntent.resolveActivity(getPackageManager())!=null){
         File photoFile = null;
         try{
            photoFile = createImageFile();
         }catch (IOException ex){
            // Error occurred while creating the File
         }

         if(photoFile != null){
            //FileProvider는 AndroidManifest에서 추가했던 <Provider>요소를 이용해 uri를 불러오는 역할을 한다.
            photoUri = FileProvider.getUriForFile(context, "com.example.alwaysawake2", photoFile);
            //이미지가 저장 될 uri를 같이 넘긴다
            // photoUri는 createImageFile로 만들어진 파일의 객체에서 나온다
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
            startActivityForResult(takePictureIntent,REQUEST_DATA_CAMERA);
         }
      }
return photoUri;
   }



   private static String imageFilePath;

   public static String getImageFilePath() {
      return imageFilePath;
   }
   public static void setImageFilePath(String imageFilePath) {
      BaseActivity.imageFilePath = imageFilePath;
   }

   private  File createImageFile() throws  IOException {

      //이미지파일의 이름이 겹치지 않도록 이미지파일 네임의 뒤에 찍은 날짜와 시간을 표시해 주는 변수 생성
      String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
      String imageFileName = "JPEG_" + timeStamp + "_";

      //외부 파일 디렉토리(사진 디렉토리의 정보 파일에 담아준다)
      File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

      //임시파일 만들기
      File image = File.createTempFile(
              imageFileName,     //접두사
              ".jpg",      //접미사
              storageDir         //디렉토리
      );
      imageFilePath = image.getAbsolutePath();//getAbsolutePath 이미지 파일의 절대 경로를 얻어다가 스트링 값으로 넣어준다
      return image;
   }


   public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

      Matrix matrix = new Matrix();
      switch (orientation) {
         case ExifInterface.ORIENTATION_NORMAL:
            return bitmap;
         case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
            matrix.setScale(-1, 1);
            break;
         case ExifInterface.ORIENTATION_ROTATE_180:
            matrix.setRotate(180);
            break;
         case ExifInterface.ORIENTATION_FLIP_VERTICAL:
            matrix.setRotate(180);
            matrix.postScale(-1, 1);
            break;
         case ExifInterface.ORIENTATION_TRANSPOSE:
            matrix.setRotate(90);
            matrix.postScale(-1, 1);
            break;
         case ExifInterface.ORIENTATION_ROTATE_90:
            matrix.setRotate(90);
            break;
         case ExifInterface.ORIENTATION_TRANSVERSE:
            matrix.setRotate(-90);
            matrix.postScale(-1, 1);
            break;
         case ExifInterface.ORIENTATION_ROTATE_270:
            matrix.setRotate(-90);
            break;
         default:
            return bitmap;
      }
      try {
         Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
         bitmap.recycle();
         return bmRotated;
      }
      catch (OutOfMemoryError e) {
         e.printStackTrace();
         return null;
      }
   }





  protected Bitmap BitmapImageResizing(Context context, Uri photoURI){
      Bitmap bitmap = null;
      Bitmap bitmapResize = null;
      try {
         bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),photoURI);
         bitmapResize = bitmap;
         while(bitmapResize.getHeight() > 256) {
            bitmapResize = Bitmap.createScaledBitmap(bitmapResize, bitmapResize.getWidth() /2, bitmapResize.getHeight() /2, true);
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      return bitmapResize;
   }

   public Bitmap galleryCursor(Context context,Uri photoUri) {
//      String path = PhotoUri.getPath();
//      String toString =PhotoUri.toString();
//      File file = new File(path);
//      String imagePath = file.getAbsolutePath();
      String[] filePath = {MediaStore.Images.Media.DATA};
      Cursor cursor = getContentResolver().query(photoUri, filePath, null, null, null);
      cursor.moveToFirst();
      imageFilePath  = cursor.getString(cursor.getColumnIndex(filePath[0]));
      cursor.close();
      return imageRotated(imageFilePath,context,photoUri);
   }

public Bitmap imageRotated(String imagePath, Context context, Uri photoUri) {
   //ExifInterface 디지털 사진의 이미지 정보
   ExifInterface exif = null;
   try {
      //카메라에서 사진 찍을 때 가져온 파일패치(string값)를 넣어준다
      exif = new ExifInterface(imagePath);
   } catch (IOException e) {
      e.printStackTrace();
   }
   //ExifInterface에서 사진의 방향에 관한 정보를 가져온다
   int Orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
   //비트맵 리사이징
   //사진의 방향을 원래대로 바꿔줌
   Bitmap resizing = BitmapImageResizing(context, photoUri);
   Bitmap bitmapRotated = rotateBitmap(resizing, Orientation);
   return bitmapRotated;
}




public static ArrayList<Activity> acticityArray = new ArrayList<>();

   public void activityFinish(){
      for(int i = 0; i < acticityArray.size(); i++){
         acticityArray.get(i).finish();
      }
   }

public ImageView circleImage(ImageView imageView){
   imageView.setBackground(new ShapeDrawable(new OvalShape()));
   if(Build.VERSION.SDK_INT >= 21){
      imageView.setClipToOutline(true);
      imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
   }
   return imageView;
}


   /**
    *
    * @param id 아이디 값으로 판별하여 프로필 이미지 uri, 파일패치, 닉네임을 뿌려준다
    * @return 유저의 정보를 배열로 리턴해준다
    */
   String[] getMemberInformation(String id){
      SharedPreferences memberInformation = getSharedPreferences("userListFile", MODE_PRIVATE);
      String memberString = memberInformation.getString(id,"");
      JSONObject jsonObject = null;
      //0번 닉네임값, 1번 이미지uri, 2번 패치파일;
      String[] information = null;
      try {
         jsonObject = new JSONObject(memberString);
         information[0] = jsonObject.getString("nickName");
         information[1] = jsonObject.getString("filePath");
         information[2] = jsonObject.getString("photoUri");
      } catch (JSONException e) {
         e.printStackTrace();
      }
      return information;
   }
}
