package com.example.course29.chat.chatContent.member;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.course29.R;

import java.util.List;

public class MemberAdapter extends BaseQuickAdapter<Member, BaseViewHolder> {
    public MemberAdapter(int layoutResId, @Nullable List<Member> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Member item) {
        ImageView imageView = helper.itemView.findViewById(R.id.iv_itemFriend_profile);
        Glide.with(mContext)
                .load(item.getAvatar())
                .into(imageView);
        helper.setText(R.id.tv_itemFriend_remark,(item.getRemark().equals("")||item.getRemark()==null) ?
                item.getNickname()
                :
                item.getRemark());
    }
}
