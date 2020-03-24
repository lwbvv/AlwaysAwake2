package com.example.alwaysawake2.RecyclerPlan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.alwaysawake2.ActivityG4AddPlan;
import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.R;
import com.example.alwaysawake2.RecyclerComment.CommentItem;
import com.example.alwaysawake2.TouchIvent.OnItemMoveListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> implements OnItemMoveListener {

    ArrayList<PlanItem> arrayList = new ArrayList<>();
    Context context;
    Handler handler = new Handler();

    //오늘 연,월,일 밀리타임으로 변환한 값 담아줄 변수
    long toDateMilliTime;
int Color;
    boolean successPlan;
    public PlanAdapter(ArrayList<PlanItem> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    /**뷰홀더**/
    class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView title, bodyText, dDay, importance, success;
        Button editButton;
        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_plan_title);
            bodyText = itemView.findViewById(R.id.item_plan_bodyText);
            dDay = itemView.findViewById(R.id.item_plan_dDay);
            importance = itemView.findViewById(R.id.item_plan_importance);
            editButton = itemView.findViewById(R.id.item_plan_editButton);
            success = itemView.findViewById(R.id.item_plan_success);
        }
    }
    /**뷰홀더**/


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(arrayList,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
        PlanItem planItem = arrayList.get(fromPosition);
        arrayList.remove(fromPosition);
        arrayList.add(toPosition, planItem);
        return true;
    }

    @Override
    public void onItemRemove(int position) {
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);
        }
        catch(IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    /**
     *
     * 뷰홀더 메소드 오버라이딩
     *
     * */
    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_planlist, viewGroup, false);
        return new PlanViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final PlanViewHolder planViewHolder, final int i) {


        planViewHolder.title.setText(arrayList.get(i).getTitle());
        planViewHolder.bodyText.setText(arrayList.get(i).getBodyText());
        planViewHolder.importance.setText("" + arrayList.get(i).getImportance());
        dDayCount(planViewHolder, i);
//        dDayThread(planViewHolder.dDay, arrayList, i);
        //플랜에드버튼 true시 프로젝트 생성란에 들어갔을 때 조건이 작동하고 false 일 때는 생성된 프로젝트 클릭 했을 경우 조건이 발동
        if(BaseActivity.planAdd == false){
            planViewHolder.editButton.setVisibility(View.GONE);

            if(arrayList.get(i).isSuccessPLan() == true){
                    planViewHolder.success.setText("완료");
                    planViewHolder.success.setBackgroundResource(R.color.quantum_deeporange500);
                }else if(arrayList.get(i).isSuccessPLan() == false){
                planViewHolder.success.setText("진행중");
                    planViewHolder.success.setBackgroundResource(R.color.quantum_lime100);
            }

            planViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if(arrayList.get(i).isSuccessPLan() == false){
                        planViewHolder.success.setText("완료");
                        planViewHolder.success.setBackgroundResource(R.color.quantum_deeporange500);
                        successPlan = true;
                        arrayList.get(i).setSuccessPLan(successPlan);
                    }else if(arrayList.get(i).isSuccessPLan() == true){
                        planViewHolder.success.setText("진행중");
                        planViewHolder.success.setBackgroundResource(R.color.quantum_lime100);
                        successPlan = false;
                        arrayList.get(i).setSuccessPLan(successPlan);
                    }
                    return true;
                }
            });
        }
         editPlan(planViewHolder, i);


    }

    @Override
    public int getItemCount() {
        toDayMilliTime();
        return null != arrayList ? arrayList.size() : 0;
    }
    /**
     *
     * 뷰홀더 메소드 오버라이딩
     *
     * */


    /**
     * 오늘 연,월,일 밀리타임으로 변환 메소드
     */
    void toDayMilliTime() {
        int toDay, toMonth, toYear;
        Calendar toCalendar = Calendar.getInstance();
        toYear = toCalendar.get(Calendar.YEAR);
        toMonth = toCalendar.get(Calendar.MONTH);
        toDay = toCalendar.get(Calendar.DATE);
        toCalendar.set(toYear, toMonth, toDay);
        toDateMilliTime = toCalendar.getTimeInMillis();
    }

    void dDayCount(PlanViewHolder planViewHolder, int position) {
        int dDay = 0, dMonth = 0, dYear = 0;
        long DdateMilliTime, resultMilliTime;
        Calendar deadlineCalendar = Calendar.getInstance();
            dYear = arrayList.get(position).getDeadlineYear();
            dMonth = arrayList.get(position).getDeadlineMonth();
            dDay = arrayList.get(position).getDeadlineDay();

        deadlineCalendar.set(dYear, dMonth, dDay);
        DdateMilliTime = deadlineCalendar.getTimeInMillis();
        resultMilliTime = (DdateMilliTime - toDateMilliTime) / (24 * 60 * 60 * 1000);
        planViewHolder.dDay.setText(""+ resultMilliTime);
    }


//    /**
//     * D-Day 메소드
//     **/
//    void dDayThread(final TextView textView, final ArrayList<PlanItem> arrayList, final int position) {
//        /**D-day 표현 시커줄 서브 스레드와 핸들러**/
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                while (true) {
//                    final String dDayValue;
//                    int toDay, toMonth, toYear, deadlineDay, deadlineMonth, deadlineYear;
//                    Calendar calendar = Calendar.getInstance();
//                    toDay = calendar.get(Calendar.DATE);
//                    toMonth = calendar.get(Calendar.MONTH);
//                    toYear = calendar.get(Calendar.YEAR);
//                    calendar.set(toYear, toMonth, toDay);
//                    deadlineDay = arrayList.get(position).getDeadlineDay();
//                    deadlineMonth = arrayList.get(position).getDeadlineMonth();
//                    deadlineYear = arrayList.get(position).getDeadlineYear();
//                    Calendar deadlineDate = Calendar.getInstance();
//                    deadlineDate.set(deadlineYear, deadlineMonth, deadlineDay);
//                    long toDateTimeMillis, deadlineDateTimeMillis, convertAndSumDay;
//                    toDateTimeMillis = calendar.getTimeInMillis();
//                    deadlineDateTimeMillis = deadlineDate.getTimeInMillis();
//                    convertAndSumDay = (deadlineDateTimeMillis-toDateTimeMillis)/(24*60*60*1000);
//                    dDayValue = convertAndSumDay + " 일";
//
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            textView.setText(dDayValue);
//                        }
//                    });
//
//                    try {
//                        Thread.sleep(10000);
//                    } catch (InterruptedException e) {
//                        handler.removeCallbacks(this);
//                        handler.removeMessages(0);
//                        return;
//                    }
//
//                }
//            }
//        }).start();
//        /**D-day 표현 시커줄 서브 스레드와 핸들러**/
//    }
//    /**D-Day 메소드**/




    void editPlan(PlanViewHolder planViewHolder,final int i){
        planViewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**아이템 수정을 위해 array에 담긴 데이터들 인텐트로 전달**/
                Intent putPlanData = new Intent(context, ActivityG4AddPlan.class);
                putPlanData.putExtra("title", arrayList.get(i).getTitle());
                putPlanData.putExtra("bodyText", arrayList.get(i).getBodyText());
                putPlanData.putExtra("importance", arrayList.get(i).getImportance());
                putPlanData.putExtra("deadlineYear", arrayList.get(i).getDeadlineYear());
                putPlanData.putExtra("deadlineMonth", arrayList.get(i).getDeadlineMonth());
                putPlanData.putExtra("deadlineDay", arrayList.get(i).getDeadlineDay());
                putPlanData.putExtra("noticeTime", arrayList.get(i).getNoticeTime());
                putPlanData.putExtra("planGetCount", i);
                putPlanData.putExtra("noticeMilliTime",arrayList.get(i).getNoticeMilliTime());
                /**리퀘스트 코드, 플래그 사용하기 위해 객체 생성**/
                BaseActivity baseActivity = new BaseActivity();
                baseActivity.planAdapterDataFlag = true;
                ((Activity) v.getContext()).startActivityForResult(putPlanData, baseActivity.REQUEST_PLANEDIT);
            }
        });
    }



}
