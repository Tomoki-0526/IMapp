package com.example.course29.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.course29.contact.friend.Friend;

import java.util.List;

import com.example.course29.R;

public class ChatAdapter extends BaseQuickAdapter<Chat, BaseViewHolder> {
    public ChatAdapter(int layoutResId, @Nullable List<Chat> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Chat item) {
        helper.setText(R.id.tv_itemChat_name,item.getName());
        ImageView imageView = helper.itemView.findViewById(R.id.iv_itemChat_avatar);
        Glide.with(mContext)
                .load(item.getAvatar())
                .into(imageView);
        helper.setText(R.id.tv_itemChat_sendTime,item.getSendTime());
        TextView textView = helper.itemView.findViewById(R.id.tv_itemChat_unread);
        if(!item.getUnread().equals("0")) {
            textView.setVisibility(View.VISIBLE);
            textView.setText("["+item.getUnread()+"条未读]");
        }
        else {
            textView.setVisibility(View.GONE);
        }

        switch (item.getType()) {
            case "0":
                helper.setText(R.id.tv_itemChat_lastedMsg,item.getLatestMsg());
                break;
            case "1":
                helper.setText(R.id.tv_itemChat_lastedMsg,"[图片]");
                break;
            case "2":
                helper.setText(R.id.tv_itemChat_lastedMsg,"[语音]");
                break;
            case "3":
                helper.setText(R.id.tv_itemChat_lastedMsg,"[视频]");
                break;
            case "4":
                helper.setText(R.id.tv_itemChat_lastedMsg,"[位置]");
                break;
        }

    }
}
