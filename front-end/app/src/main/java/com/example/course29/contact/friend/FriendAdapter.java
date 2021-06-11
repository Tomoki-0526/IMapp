package com.example.course29.contact.friend;


import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.course29.R;
import com.example.course29.util.BitmapUtil;

import java.util.List;

public class FriendAdapter extends BaseQuickAdapter<Friend, BaseViewHolder> {

    public FriendAdapter(int layoutResId, @Nullable List<Friend> data){
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, Friend item) {
        ImageView imageView = helper.itemView.findViewById(R.id.iv_itemFriend_profile);
        Glide.with(mContext)
                .load(item.getAvatar())
                .into(imageView);
//        helper.setImageBitmap(R.id.iv_itemFriend_profile, BitmapUtil.getHttpBitmap(item.getAvatar()));
        helper.setText(R.id.tv_itemFriend_remark,(item.getRemark().equals("")||item.getRemark()==null) ?
                item.getNickname()
                :
                item.getRemark());
    }
}