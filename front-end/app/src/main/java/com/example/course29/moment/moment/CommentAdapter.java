package com.example.course29.moment.moment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.course29.R;
import com.example.course29.contact.friend.FriendActivity;
import com.example.course29.contact.newFriend.StrangerInfoActivity;

import java.util.List;

public class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {
    public CommentAdapter(int layoutResId, @Nullable List<Comment> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {
        helper.setText(R.id.tv_itemComment_nickname,
                !item.getCommentRemark().equals("") ? item.getCommentRemark() : item.getCommentNickname());
        helper.setText(R.id.tv_itemComment_time, item.getCommentTime());
        helper.setText(R.id.tv_itemComment_content, item.getCommentContent());
        TextView textView = helper.itemView.findViewById(R.id.tv_itemComment_nickname);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (item.getIsFriend() == "true") {
                    intent = new Intent(mContext, FriendActivity.class);
                }
                else {
                    intent = new Intent(mContext, StrangerInfoActivity.class);
                }
                intent.putExtra("strUsername",item.getCommentUsername());
                mContext.startActivity(intent);
            }
        });
    }
}
