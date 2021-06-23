package com.example.course29.contact.group;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.course29.R;
import com.example.course29.contact.friend.Friend;

import java.util.List;

public class FriendSelectAdapter extends BaseQuickAdapter<Friend, BaseViewHolder> {

    public FriendSelectAdapter(int layoutResId, @Nullable List<Friend> data){
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, Friend item) {
        LinearLayout linearLayout = helper.itemView.findViewById(R.id.ll_itemFriend_all);
        ImageView imageView = helper.itemView.findViewById(R.id.iv_itemFriend_profile);
        Glide.with(mContext)
                .load(item.getAvatar())
                .into(imageView);
//        helper.setImageBitmap(R.id.iv_itemFriend_profile, BitmapUtil.getHttpBitmap(item.getAvatar()));
        helper.setText(R.id.tv_itemFriend_remark,(item.getRemark().equals("")||item.getRemark()==null) ?
                item.getNickname()
                :
                item.getRemark());
//        linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                helper.setTextColor(R.id.tv_itemFriend_remark,mContext.getResources().getColor(R.color.blue));
//            }
//        });
    }
}
