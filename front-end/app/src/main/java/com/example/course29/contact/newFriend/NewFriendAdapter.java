package com.example.course29.contact.newFriend;


import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.course29.R;
import com.example.course29.util.BitmapUtil;

import java.util.List;

public class NewFriendAdapter extends BaseQuickAdapter<NewFriend, BaseViewHolder> {

    public NewFriendAdapter(int layoutResId, @Nullable List<NewFriend> data){
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, NewFriend item) {
        ImageView imageView = helper.itemView.findViewById(R.id.iv_itemNewFriend_profile);
        helper.setText(R.id.tv_itemNewFriend_nickname, item.getNickname());
//        helper.setImageBitmap(R.id.iv_itemNewFriend_profile, BitmapUtil.getHttpBitmap(item.getAvatar()));
        Glide.with(mContext)
                .load(item.getAvatar())
                .into(imageView);
        helper.setText(R.id.tv_itemNewFriend_extra,item.getExtra());
        switch (item.getStatus()){
            case "0":
                helper.setText(R.id.tv_itemNewFriend_status,"等到验证");
//                helper.setTextColor(R.id.tv_itemNewFriend_status, mContext.getResources().getColor(R.color.teal_200));
                break;
            case "1":
                helper.setText(R.id.tv_itemNewFriend_status,"已通过");
                helper.setTextColor(R.id.tv_itemNewFriend_status, mContext.getResources().getColor(R.color.green));
                break;
            case "2":
                helper.setText(R.id.tv_itemNewFriend_status,"已拒绝");
                helper.setTextColor(R.id.tv_itemNewFriend_status, mContext.getResources().getColor(R.color.red));
                break;
            default:
                break;

        }
    }
}

