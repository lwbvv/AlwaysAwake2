package com.example.alwaysawake2.RecyclerHabit;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.alwaysawake2.ActivityE1Habit;
import com.example.alwaysawake2.ActivityE2HabitCreating;
import com.example.alwaysawake2.ActivityHabitDetailed;
import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.BaseActivity.MapClass;
import com.example.alwaysawake2.BaseActivity.PopUpMenu;
import com.example.alwaysawake2.R;
import com.example.alwaysawake2.RecyclerProject.ProjectAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.ext.EntityResolver2;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static com.example.alwaysawake2.BaseActivity.BaseActivity.allPlan;
import static com.example.alwaysawake2.BaseActivity.BaseActivity.keyId;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHoler> {
    EditText editText;
    ArrayList<HabitItem> arrayList = new ArrayList();
    MapClass mapClass = new MapClass();
    Context context;

    public HabitAdapter(ArrayList<HabitItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    int currentHour, currentMinute;

    class HabitViewHoler extends RecyclerView.ViewHolder {
        TextView disable, title;
        CheckBox checkBox;
        Button editOrRemove;
        LinearLayout habitView;

        public HabitViewHoler(@NonNull View itemView) {
            super(itemView);
            habitView = itemView.findViewById(R.id.item_habit_habitView);
            checkBox = itemView.findViewById(R.id.item_habit_checkBox);
            disable = itemView.findViewById(R.id.item_habit_disable);
            title = itemView.findViewById(R.id.item_habit_title);
            editOrRemove = itemView.findViewById(R.id.item_habit_editOrRemove);
        }
    }


    @NonNull
    @Override
    public HabitViewHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_habitlist, viewGroup, false);
        return new HabitViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HabitViewHoler habitViewHoler, final int i) {
            weekVisibility(habitViewHoler, i); //요일별로 표시되는 리사이클러뷰 아이템 분류
        habitViewHoler.title.setText(arrayList.get(i).getTitle());
        habitViewHoler.checkBox.setChecked(arrayList.get(i).isCheck()); //체크 시간 or 위치에 따라 활성화
        habitViewHoler.disable.setVisibility(View.GONE);
        habitViewHoler.checkBox.setVisibility(View.VISIBLE);
        checkBoxVisibility(habitViewHoler, i); //체크박스 활성화 조건
        habitViewHoler.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 체크박스 클릭
                 */
                if (arrayList.get(i).isCheck() == true) {
                    arrayList.get(i).setCheck(false);
                } else {
                    arrayList.get(i).setCheck(true);
                }
            }
        });
        /**
         * 상세보기 들어가기
         */
      habitViewHoler.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(v.getContext(), ActivityHabitDetailed.class);
              intent.putExtra("position", i);
              intent.putExtra("arrayList", arrayList);
              v.getContext().startActivity(intent);
          }
      });

      menuClick(habitViewHoler,i);
    }

    @Override
    public int getItemCount() {
        return null != arrayList ? arrayList.size() : 0;
    }


    /**
     * 리사이클러뷰에서 요일별로 표시 함수
     *
     * @param habitViewHoler 뷰홀더
     * @param position       해당 아이템 포지션
     */
    void weekVisibility(HabitViewHoler habitViewHoler, int position) {
        Calendar calendar = Calendar.getInstance();
        habitViewHoler.itemView.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams params = habitViewHoler.itemView.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        habitViewHoler.itemView.setLayoutParams(params);
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);
        if(allPlan == false) {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                if (!arrayList.get(position).isSun()) {
                    habitViewHoler.itemView.setVisibility(View.GONE);
                    params.height = 0;
                    params.width = 0;
                    habitViewHoler.itemView.setLayoutParams(params);
                    return;
                }
                break;
            case 2:
                if (!arrayList.get(position).isMon()) {
                    habitViewHoler.itemView.setVisibility(View.GONE);
                    params.height = 0;
                    params.width = 0;
                    habitViewHoler.itemView.setLayoutParams(params);
                    return;
                }
                break;
            case 3:
                if (!arrayList.get(position).isTue()) {
                    habitViewHoler.itemView.setVisibility(View.GONE);
                    params.height = 0;
                    params.width = 0;
                    habitViewHoler.itemView.setLayoutParams(params);
                    return;
                }
                break;
            case 4:
                if (!arrayList.get(position).isWed()) {
                    habitViewHoler.itemView.setVisibility(View.GONE);
                    params.height = 0;
                    params.width = 0;
                    habitViewHoler.itemView.setLayoutParams(params);
                    return;
                }
                break;
            case 5:
                if (!arrayList.get(position).isThu()) {
                    habitViewHoler.itemView.setVisibility(View.GONE);
                    params.height = 0;
                    params.width = 0;
                    habitViewHoler.itemView.setLayoutParams(params);
                    return;
                }
                break;
            case 6:
                if (!arrayList.get(position).isFri()) {
                    habitViewHoler.itemView.setVisibility(View.GONE);
                    params.height = 0;
                    params.width = 0;
                    habitViewHoler.itemView.setLayoutParams(params);
                    return;
                }
                break;
            case 7:
                if (!arrayList.get(position).isSat()) {
                    habitViewHoler.itemView.setVisibility(View.GONE);
                    params.height = 0;
                    params.width = 0;
                    habitViewHoler.itemView.setLayoutParams(params);
                    return;
                }
                break;
        }
    }
    }

    /**
     *  체크박스 비저블 상태 설정
     * @param habitViewHoler 습관 뷰홀더
     * @param position 아이템의 포지션값
     */
    void checkBoxVisibility(HabitViewHoler habitViewHoler, int position) {
        double distance = mapClass.getDistance(habitViewHoler.itemView.getContext(),arrayList.get(position).getLatitude(),arrayList.get(position).getLongitude());
        Calendar checkCalendar = Calendar.getInstance();
        checkCalendar.set(Calendar.HOUR_OF_DAY, arrayList.get(position).getCheckTimeHour());
        checkCalendar.set(Calendar.MINUTE, arrayList.get(position).getCheckTimeMinute());
        int currentTime = (currentHour * 60) + currentMinute;
        int checkTime = (arrayList.get(position).getCheckTimeHour() * 60) + arrayList.get(position).getCheckTimeMinute();
        int limitTime = (arrayList.get(position).getHourLimit() * 60) + arrayList.get(position).getMinuteLimit();

        if (currentTime < checkTime || currentTime > limitTime && !arrayList.get(position).getTimerStart().equals("시작시간")) {
            habitViewHoler.disable.setVisibility(View.VISIBLE);
            habitViewHoler.checkBox.setVisibility(View.GONE);
        }
        if(arrayList.get(position).getRadius() != 0 && distance > arrayList.get(position).getRadius()){
            habitViewHoler.disable.setVisibility(View.VISIBLE);
            habitViewHoler.checkBox.setVisibility(View.GONE);
        }
    }

    /**
     * 메뉴 클릭 함수
     **/
    void menuClick(final HabitViewHoler habitViewHoler, final int index) {
        habitViewHoler.editOrRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                ((Activity) v.getContext()).getMenuInflater().inflate(R.menu.project_popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.popup_menu_remove:
                                arrayList.remove(index);
                                SharedPreferences sharedPreferences = context.getSharedPreferences("habitFile", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = new JSONArray(sharedPreferences.getString(keyId, ""));
                                    jsonArray.remove(index);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                editor.putString(keyId, jsonArray.toString());
                                editor.apply();
                                notifyDataSetChanged();
                                break;

                            case R.id.popup_menu_edit:
                                BaseActivity.habitAdapterDataFlag = true;
                                Intent intent = new Intent(habitViewHoler.itemView.getContext(),ActivityE2HabitCreating.class);
                                intent.putExtra("array", arrayList);
                                intent.putExtra("position", index);
                                habitViewHoler.itemView.getContext().startActivity(intent);
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



}
