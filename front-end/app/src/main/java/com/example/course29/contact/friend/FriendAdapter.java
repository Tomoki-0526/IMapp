package com.example.course29.contact.friend;


import android.util.Log;

import androidx.annotation.Nullable;

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
        helper.setImageBitmap(R.id.iv_itemFriend_profile, BitmapUtil.getHttpBitmap(item.getAvatar()));
        helper.setText(R.id.tv_itemFriend_remark,(item.getRemark().equals("")||item.getRemark()==null) ?
                item.getNickname()
                :
                item.getRemark());
    }
}