package com.example.course29.moment.moment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.course29.R;
import com.example.course29.contact.friend.Friend;
import com.example.course29.contact.friend.FriendActivity;
import com.example.course29.contact.newFriend.StrangerInfoActivity;

import java.util.List;

public class LikeAdapter extends BaseQuickAdapter<Like, BaseViewHolder> {
    public LikeAdapter(int layoutResId, @Nullable List<Like> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Like item) {
        ImageView imageView = helper.itemView.findViewById(R.id.iv_itemLike_avatar);
        Glide.with(mContext)
                .load(item.getLikeAvatar())
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (item.getIsFriend() == "true") {
                    intent = new Intent(mContext, FriendActivity.class);
                }
                else {
                    intent = new Intent(mContext, StrangerInfoActivity.class);
                }
                intent.putExtra("strUsername",item.getLikeUsername());
                mContext.startActivity(intent);
            }
        });
    }
}
