package com.example.alwaysawake2.BaseActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.alwaysawake2.ActivityG3Plan;
import com.example.alwaysawake2.R;
import com.example.alwaysawake2.RecyclerProject.ProjectAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PopUpMenu {


    /**
     *
     * 아이템 클릭 메소드
     *
     */
   public void itemClick(final RecyclerView.ViewHolder viewHolder, final Context context, final int position,final Class cls){
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, cls);
                intent.putExtra("projectPosition", position);
                //플랜에드버튼 true시 프로젝트 생성란에 들어갔을 때 조건이 작동하고 false 일 때는 생성된 프로젝트 클릭 했을 경우 조건이 발동
                BaseActivity.planAdd = false;
                context.startActivity(intent);
            }
        });
    }


}
