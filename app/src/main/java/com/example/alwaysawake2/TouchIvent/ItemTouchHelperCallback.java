package com.example.alwaysawake2.TouchIvent;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.RecyclerComment.CommentItem;
import com.example.alwaysawake2.RecyclerPlan.PlanItem;

import java.util.ArrayList;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback{

    OnItemMoveListener listener;
    ArrayList<CommentItem> arrayList = new ArrayList<>();
    ArrayList<PlanItem> arrayList2 = new ArrayList<>();
int aa;
    public ItemTouchHelperCallback(OnItemMoveListener listener, ArrayList<CommentItem> arrayList) {
        this.listener = listener;
        this.arrayList = arrayList;
    }

    public ItemTouchHelperCallback(OnItemMoveListener listener, ArrayList<PlanItem> arrayList2, int aa) {
        this.listener = listener;
        this.arrayList2 = arrayList2;
        this.aa = aa;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        if(BaseActivity.keyNick.equals(arrayList.get(viewHolder.getAdapterPosition()).getNick())) {
            int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlag = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlag,swipeFlag);
        }else {
        }
        return 0;
    }



    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder source, @NonNull RecyclerView.ViewHolder target) {
            listener.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());

        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            listener.onItemRemove(viewHolder.getAdapterPosition());
    }


//    @Override
//    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//
//    }

//    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
//        float buttonWidthWithoutPadding = buttonWidth - 20;
//        float corners = 16;
//
//        View itemView = viewHolder.itemView;
//        Paint p = new Paint();
//
//        RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom());
//        p.setColor(Color.BLUE);
//        c.drawRoundRect(leftButton, corners, corners, p);
//        drawText("EDIT", c, leftButton, p);
//
//        RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight(), itemView.getBottom());
//        p.setColor(Color.RED);
//        c.drawRoundRect(rightButton, corners, corners, p);
//        drawText("DELETE", c, rightButton, p);
//
//        buttonInstance = null;
//        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
//            buttonInstance = leftButton;
//        }
//        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
//            buttonInstance = rightButton;
//        }
//    }
}
