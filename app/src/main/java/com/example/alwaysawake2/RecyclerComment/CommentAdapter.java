package com.example.alwaysawake2.RecyclerComment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.R;
import com.example.alwaysawake2.TouchIvent.OnItemMoveListener;

import java.util.ArrayList;
import java.util.Collections;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> implements OnItemMoveListener {
    Context context;
    class CommentViewHolder extends RecyclerView.ViewHolder{
        TextView id, comment, date;
        EditText commentEdit;
        TextView editButton;
        ImageView profileImage;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.item_comment_id);
            comment = itemView.findViewById(R.id.item_comment_comment);
            commentEdit = itemView.findViewById(R.id.item_comment_commentEdit);
            editButton = itemView.findViewById(R.id.item_comment_editButton);
            profileImage = itemView.findViewById(R.id.item_comment_profileImage);
            date = itemView.findViewById(R.id.item_comment_date);
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(arrayList,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
        CommentItem commentItem = arrayList.get(fromPosition);
        arrayList.remove(fromPosition);
        arrayList.add(toPosition, commentItem);
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





    ArrayList<CommentItem> arrayList = new ArrayList<>();

    public CommentAdapter(Context context, ArrayList<CommentItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment,viewGroup,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder commentViewHolder,final int i) {
        commentViewHolder.id.setText(arrayList.get(i).getNick());
        commentViewHolder.comment.setText(arrayList.get(i).getComment());
        imageSet(commentViewHolder,i);
        commentViewHolder.editButton.setText("");
        if(BaseActivity.keyNick.equals(arrayList.get(i).getNick())){
            commentViewHolder.editButton.setText("수정");
        }
        commentViewHolder.date.setText(arrayList.get(i).getYear()+ "/"+arrayList.get(i).getMonth()+"/"+arrayList.get(i).getDay());
        commentViewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentViewHolder.commentEdit.setText(arrayList.get(i).getComment());
                commentViewHolder.commentEdit.setVisibility(View.VISIBLE);
                commentViewHolder.comment.setVisibility(View.GONE);
                commentViewHolder.editButton.setVisibility(View.VISIBLE);
                commentViewHolder.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentViewHolder.commentEdit.setVisibility(View.GONE);
                        commentViewHolder.comment.setVisibility(View.VISIBLE);
                        commentViewHolder.editButton.setVisibility(View.GONE);
                        CommentItem commentItem = new CommentItem(arrayList.get(i).getId(),commentViewHolder.commentEdit.getText().toString(),arrayList.get(i).getFilePath(),arrayList.get(i).getPhotoUri()
                        ,arrayList.get(i).getNick(), BaseActivity.cyear,BaseActivity.cmonth,BaseActivity.cday);
                        arrayList.set(i,commentItem);
                        notifyItemChanged(i);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return null != arrayList ? arrayList.size() : 0;
    }


    void imageSet(CommentViewHolder commentViewHolder, int position){
        BaseActivity baseActivity = new BaseActivity();
        Uri uri = Uri.parse(arrayList.get(position).getPhotoUri());
        Bitmap profileImage = baseActivity.imageRotated(arrayList.get(position).getFilePath(),context,uri);
        commentViewHolder.profileImage.setImageBitmap(profileImage);
        commentViewHolder.profileImage = baseActivity.circleImage(commentViewHolder.profileImage);
    }
}
