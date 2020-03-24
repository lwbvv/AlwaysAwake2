package com.example.alwaysawake2.RecyclerProject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alwaysawake2.ActivityG1Project;
import com.example.alwaysawake2.ActivityG2Title;
import com.example.alwaysawake2.ActivityG3Plan;
import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    /**
     * 뷰홀더 클래스
     **/
    public class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView title, dDay;
        ImageView image;
        public Button menu;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_project_title);
            dDay = itemView.findViewById(R.id.item_project_dDay);
            image = itemView.findViewById(R.id.item_project_image);
            menu = itemView.findViewById(R.id.item_project_menu);

        }
    }

    /**
     * 뷰홀더 클래스
     **/

    JSONArray jsonArray;
    JSONObject parentObject;
    JSONObject childObject;
    SharedPreferences sharedPreferences;
    Context context;

    public ProjectAdapter(Context context) {
        this.context = context;
    }

    long toDateMilliTime;

    /**
     * 어댑터 메소드
     **/
    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_projectlist, viewGroup, false);

        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder projectViewHolder, final int i) {
        //목표일 담아준 변수
        try {
            childObject = new JSONObject(jsonArray.get(i).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            projectViewHolder.title.setText(childObject.getString("title"));
            dDayCount(projectViewHolder);
            getUri(projectViewHolder);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        menuClick(projectViewHolder, i, childObject);
        itemClick(projectViewHolder, context, i);

    }


    @Override
    public int getItemCount() {
        toDayMilliTime();
        sharedPreferences = context.getSharedPreferences("projectListFile", Context.MODE_PRIVATE);
        String projectList = sharedPreferences.getString(BaseActivity.keyId, "");
        try {
            parentObject = new JSONObject(projectList);
            jsonArray = (JSONArray) parentObject.get("projectList");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null != jsonArray ? jsonArray.length() : 0;
    }

    /**
     * 어댑터 메소드
     **/


    void toDayMilliTime() {
        int toDay, toMonth, toYear;
        Calendar toCalendar = Calendar.getInstance();
        toYear = toCalendar.get(Calendar.YEAR);
        toMonth = toCalendar.get(Calendar.MONTH);
        toDay = toCalendar.get(Calendar.DATE);
        toCalendar.set(toYear, toMonth, toDay);
        toDateMilliTime = toCalendar.getTimeInMillis();
    }

    void dDayCount(ProjectViewHolder projectViewHolder) {
        int dDay = 0, dMonth = 0, dYear = 0;
        long DdateMilliTime, resultMilliTime;
        Calendar deadlineCalendar = Calendar.getInstance();
        try {
            dYear = childObject.getInt("year");
            dMonth = childObject.getInt("month");
            dDay = childObject.getInt("day");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        deadlineCalendar.set(dYear, dMonth, dDay);
        DdateMilliTime = deadlineCalendar.getTimeInMillis();
        resultMilliTime = (DdateMilliTime - toDateMilliTime) / (24 * 60 * 60 * 1000);
        projectViewHolder.dDay.setText("D-DAY" + resultMilliTime);
    }

    void getUri(ProjectViewHolder projectViewHolder) {
        String uriString;
        Uri uri;
        BaseActivity baseActivity = new BaseActivity();
        try {
            uriString = childObject.getString("uriString");
            String filePath = childObject.getString("filePath");
            uri = Uri.parse(uriString);
            projectViewHolder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Bitmap bitmapRotated = baseActivity.imageRotated(filePath, context, uri);
            projectViewHolder.image.setImageBitmap(bitmapRotated);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 메뉴 클릭 함수
     **/
    void menuClick(ProjectViewHolder projectViewHolder, final int index, final JSONObject jsonObject) {
        projectViewHolder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                ((Activity) v.getContext()).getMenuInflater().inflate(R.menu.project_popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.popup_menu_remove:
                                projectRemove(context, index);
                                break;

                            case R.id.popup_menu_edit:
                                BaseActivity.projectAdapterDataFlag = true; //에딧 불린
                                String uriString = null, title = null, filePath = null;
                                int year = 0, month = 0, day = 0;
                                try {
                                    title = jsonObject.getString("title");
                                    uriString = jsonObject.getString("uriString");
                                    year = jsonObject.getInt("year");
                                    month = jsonObject.getInt("month");
                                    day = jsonObject.getInt("day");
                                    filePath = jsonObject.getString("filePath");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(context, ActivityG2Title.class);
                                intent.putExtra("position", index);
                                intent.putExtra("uriString", uriString);
                                intent.putExtra("year", year);
                                intent.putExtra("month", month);
                                intent.putExtra("day", day);
                                intent.putExtra("title", title);
                                intent.putExtra("filePath", filePath);
                                context.startActivity(intent);
                                break;

                        }

                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }
    /**메뉴 클릭 함수**/


    /**
     * 아이템 클릭 메소드
     */
    void itemClick(final ProjectViewHolder projectViewHolder, final Context projectContext, final int position) {
        projectViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(projectContext, ActivityG3Plan.class);
                intent.putExtra("projectPosition", position);
                //플랜에드버튼 true시 프로젝트 생성란에 들어갔을 때 조건이 작동하고 false 일 때는 생성된 프로젝트 클릭 했을 경우 조건이 발동
                BaseActivity.planAdd = false;
                projectContext.startActivity(intent);
            }
        });
    }


    void projectRemove(final Context context, final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("삭제");
        builder.setMessage("정말 삭제 하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //프로젝트리스트 삭제(쉐어드를 사용하여 어플이 껐을 때도 데이터가 안 날라감
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        JSONArray parentArray;
                        jsonArray.remove(index);
                        try {
                            parentArray = parentObject.getJSONArray("plan");
                            parentArray.remove(index);
                            parentObject.put("projectList", jsonArray);
                            parentObject.put("plan", parentArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        editor.putString(BaseActivity.keyId, parentObject.toString());
                        editor.apply();
                        notifyItemRemoved(index);
                        notifyDataSetChanged();
                        //
                        Toast.makeText(context, "삭제 되었습니다.", Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
}


