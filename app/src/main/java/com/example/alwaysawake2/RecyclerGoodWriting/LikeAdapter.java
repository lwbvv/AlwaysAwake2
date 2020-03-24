package com.example.alwaysawake2.RecyclerGoodWriting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alwaysawake2.ActivityComment;
import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.alwaysawake2.BaseActivity.BaseActivity.keyId;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.LikeViewHolder> {
    class LikeViewHolder extends RecyclerView.ViewHolder {
        TextView nickName, likeCount, bodyText, commentCount;
        ImageView pictureView, heartView, commentView, profileImage;
        Button menu;
        LinearLayout expend;
        public LikeViewHolder(@NonNull View itemView) {
            super(itemView);
            expend = itemView.findViewById(R.id.expend);
            nickName = itemView.findViewById(R.id.item_goodLike_nickName);
            likeCount = itemView.findViewById(R.id.item_goodLike_likeCount);
            bodyText = itemView.findViewById(R.id.item_goodLike_bodyText);
            commentCount = itemView.findViewById(R.id.item_goodLike_commentCount);
            pictureView = itemView.findViewById(R.id.item_goodLike_pictureView);
            heartView = itemView.findViewById(R.id.item_goodLike_heartView);
            commentView = itemView.findViewById(R.id.item_goodLike_commentView);
            menu = itemView.findViewById(R.id.item_goodLike_menu);
            profileImage = itemView.findViewById(R.id.item_goodLike_profileImage);
        }
    }

    Context context; //굿라이팅 컨텍스트 얻어오는 작업
    SharedPreferences getMemberInfoReferShared;
    SharedPreferences sharedGoodWriting;
    JSONArray jsonArray = null;

    public LikeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public LikeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_goodwritinglist_likelist, viewGroup, false);
        return new LikeAdapter.LikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LikeViewHolder likeViewHolder, final int i) {
        JSONArray likeCheckArray = new JSONArray();
        JSONObject object = new JSONObject();
        boolean heart = false;
        try {
            likeCheckArray = new JSONArray(sharedGoodWriting.getString("goodWritingHeart",""));
            object = likeCheckArray.getJSONObject(i);
            heart = object.getBoolean(keyId+"heart");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(heart) {
            JSONObject jsonObject = null;
            jsonObject = getNickNameAndImage(likeViewHolder, i);
            try {
                likeViewHolder.bodyText.setText(jsonObject.getString("writing"));
                JSONArray parentArray = new JSONArray(sharedGoodWriting.getString("comment", ""));
                JSONArray childArray = new JSONArray();
                childArray = parentArray.getJSONArray(i);
                likeViewHolder.commentCount.setText("댓글 모두보기 " + childArray.length() + "개");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                heartCheck(likeViewHolder, i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//        goodWritingViewHolder.bodyText.setText(arrayList.get(i).getBodyText());
//        goodWritingViewHolder.commentCount.setText("댓글 모두보기 " + arrayList.get(i).getCommentCount() + "개");
//        goodWritingViewHolder.pictureView.setImageBitmap(arrayList.get(i).getPictureView());
//        goodWritingViewHolder.likeCount.setText("좋아요 " + arrayList.get(i).getLikeCount() + "개");

            Button.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.item_goodLike_commentView: {
                            Intent intent = new Intent(v.getContext(), ActivityComment.class);
                            intent.putExtra("position", i);
                            v.getContext().startActivity(intent);
                        }
                        break;

                        case R.id.item_goodLike_commentCount: {
                            Intent intent = new Intent(v.getContext(), ActivityComment.class);
                            intent.putExtra("position", i);
                            v.getContext().startActivity(intent);
                        }
                        break;
                    }
                }
            };
            likeViewHolder.commentView.setOnClickListener(onClickListener);
            likeViewHolder.commentCount.setOnClickListener(onClickListener);
        }else{
            ViewGroup.LayoutParams params = likeViewHolder.itemView.getLayoutParams();
            params.height = 0;
            params.width = 0;
            likeViewHolder.itemView.setLayoutParams(params);
        }

    }

    @Override
    public int getItemCount() {
        getMemberInfoReferShared = context.getSharedPreferences("userListFile", Context.MODE_PRIVATE);
        sharedGoodWriting = context.getSharedPreferences("goodWriting", Context.MODE_PRIVATE);
        String goodWritingString = sharedGoodWriting.getString("goodWriting", "");
        try {
            jsonArray = new JSONArray(goodWritingString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null != jsonArray ? jsonArray.length() : 0;
    }


    JSONObject getNickNameAndImage(LikeViewHolder likeViewHolder, int position) {
        JSONObject jsonObject = null;
        JSONObject member = null;
        String memberString = null;
        try {
            //좋은글귀 제이슨 어레이에서 포지션에 있는 오브젝트 추출
            jsonObject = jsonArray.getJSONObject(position);
            //멤버정보에 접근하기 위해 좋은글귀 제이슨 오브젝트에서 아이디값 추출하여 멤버의 키값과 비교해서 멤버 정보 추출
            memberString = getMemberInfoReferShared.getString(jsonObject.getString("keyId"), "");
            //추출한 멤버 정보
            member = new JSONObject(memberString);
            //추출한 정보들을 이용하여 닉네임, 이미지 뿌려주기
            likeViewHolder.nickName.setText(member.getString("nickName"));
            BaseActivity baseActivity = new BaseActivity();
            Uri photoUri = Uri.parse(member.getString("photoUri"));
            Bitmap profileImage = baseActivity.imageRotated(member.getString("filePath"), context, photoUri);
            likeViewHolder.profileImage.setImageBitmap(profileImage);
            baseActivity.circleImage(likeViewHolder.profileImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    void heartCheck(final LikeViewHolder likeViewHolder, final int position) throws JSONException {

        /**
         * 쉐어드에서 기존 값 빼내오는 부분
         */
        String heartArrayString = sharedGoodWriting.getString("goodWritingHeart", ""); //좋은글귀 좋아요 불린 값 들어있는 어레이 불러오기
        JSONArray heartArray = new JSONArray(heartArrayString); // 좋아요 어레이 매칭
        JSONObject heartObject = heartArray.getJSONObject(position);//해당 포지션의 오브젝트 불러온다
        final boolean heartCheck = heartObject.getBoolean(keyId + "heart");// 해당 어레이 포지션에 있는 오브젝트에서 로그인 한 아이디와 맞는 boolean값을 불러온다

        String goodWritingString = sharedGoodWriting.getString("goodWriting", ""); //좋은 글귀 목록 array를 String으로 불러온다
        JSONArray goodWritingArray = new JSONArray(goodWritingString);//  좋은 글귀 어레이 매칭
        final JSONObject goodWritingObject = goodWritingArray.getJSONObject(position); //좋은 글귀 어레이에서 해당 포지션의 값을 불러와 준다

        final int heartCount = goodWritingObject.getInt("likeCount"); // 좋아요 카운트 불어와서 변수에 담아준다
        if(heartObject.getBoolean(keyId+"heart") == true) {
            likeViewHolder.heartView.setImageResource(R.drawable.heart_red);
        }else if(heartObject.getBoolean(keyId+"heart") == false){
            likeViewHolder.heartView.setImageResource(R.drawable.heart);
        }
        String uriString;
        Uri uri;
        BaseActivity baseActivity = new BaseActivity();
        uriString = goodWritingObject.getString("writingPhotoUri");
        String filePath = goodWritingObject.getString("filePath");
        uri = Uri.parse(uriString);
        likeViewHolder.pictureView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Bitmap bitmapRotated = baseActivity.imageRotated(filePath, context, uri);
        likeViewHolder.pictureView.setImageBitmap(bitmapRotated);
        likeViewHolder.likeCount.setText("좋아요 " + heartCount + "개");
        likeViewHolder.heartView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (heartCheck == false) {
                    try {
                        //현재 하트 값 반환
                        int heartCountUp = heartCount + 1;
                        String heartArrayString = sharedGoodWriting.getString("goodWritingHeart", ""); //좋은글귀 좋아요 불린 값 들어있는 어레이 불러오기
                        String goodWritingString = sharedGoodWriting.getString("goodWriting", ""); //좋은 글귀 목록 array를 String으로 불러온다
                        JSONArray jsonArray = new JSONArray(goodWritingString);
                        JSONObject jsonObject = jsonArray.getJSONObject(position);
                        jsonObject.put("likeCount", heartCountUp);
                        jsonArray.put(position,jsonObject);

                        JSONArray heartJArray = new JSONArray(heartArrayString);
                        JSONObject heartJObject = heartJArray.getJSONObject(position);
                        //클릭시 트루값으로 변환
                        heartJObject.put(keyId+"heart", true);

                        heartJArray.put(position, heartJObject);
                        SharedPreferences.Editor editor = sharedGoodWriting.edit();
                        editor.putString("goodWriting", jsonArray.toString());
                        editor.putString("goodWritingHeart", heartJArray.toString());
                        editor.apply();
                        likeViewHolder.heartView.setImageResource(R.drawable.heart_red);
                        likeViewHolder.likeCount.setText("좋아요 " + heartCountUp + "개");
                        notifyItemChanged(position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (heartCheck == true) {
                    try {
                        //현재 하트 값 반환
                        int heartCountDown = heartCount - 1;
                        String heartArrayString = sharedGoodWriting.getString("goodWritingHeart", ""); //좋은글귀 좋아요 불린 값 들어있는 어레이 불러오기
                        String goodWritingString = sharedGoodWriting.getString("goodWriting", ""); //좋은 글귀 목록 array를 String으로 불러온다
                        JSONArray jsonArray = new JSONArray(goodWritingString); //좋은글귀 어레이 리스트
                        JSONObject jsonObject = jsonArray.getJSONObject(position); //좋은글귀 어레이에서 해당 포지션의 오브젝트 추출
                        jsonObject.put("likeCount", heartCountDown); //오브젝트의 좋아요 수를 갱신시켜준다
                        jsonArray.put(position,jsonObject); //갱신 시킨 후 해당 포지션에 다시 저장시켜 준다

                        JSONArray heartJArray = new JSONArray(heartArrayString); //좋아요 boolean값을 담아 놓은 어레이리스트를 추출한다.
                        JSONObject heartJObject = heartJArray.getJSONObject(position);// 어레이에서 포지션의 오브젝트를 추출한다
                        //클릭시 트루값으로 변환
                        heartJObject.put(keyId+"heart", false); //오브젝트에서 리스트의 좋아요 값을 id를 키로 잡아 true였던 벨류를 다시 false로 바꾼다
                        heartJArray.put(position, heartJObject); //해당 포지션에 벨류값을 다시 담아준다
                        SharedPreferences.Editor editor = sharedGoodWriting.edit();
                        editor.putString("goodWriting", jsonArray.toString()); //쉐어드에 좋아요 카운트가 담김 어레이 저장
                        editor.putString("goodWritingHeart", heartJArray.toString()); //쉐어드에 좋아요 불린값 갱신 저장
                        editor.apply();
                        likeViewHolder.heartView.setImageResource(R.drawable.heart);
                        likeViewHolder.likeCount.setText("좋아요 " + heartCountDown + "개");
                        notifyItemChanged(position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        });



    }
}
