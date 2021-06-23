package com.example.course29.contact.group;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.course29.R;
import com.example.course29.chat.Chat;

import java.util.List;

public class GroupAdapter extends BaseQuickAdapter<Group, BaseViewHolder> {
    public GroupAdapter(int layoutResId, @Nullable List<Group> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Group item) {
        TextView textViewGroupName = helper.itemView.findViewById(R.id.tv_itemGroup_groupName);
        ImageView imageViewGroupAvatar = helper.itemView.findViewById(R.id.iv_itemGroup_groupAvatar);
        textViewGroupName.setText(item.getGroupName());
        Glide.with(mContext)
                .load(item.getGroupAvatar())
                .into(imageViewGroupAvatar);
    }
}
