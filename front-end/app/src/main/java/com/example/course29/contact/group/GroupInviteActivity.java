package com.example.course29.contact.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.course29.R;
import com.example.course29.contact.friend.Friend;
import com.example.course29.util.GlobalVariable;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.JsonMapUtil;
import com.example.course29.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupInviteActivity extends AppCompatActivity {
    private ImageView mIvGroupInviteReturn;
    private RecyclerView mRvGroupInviteFriendList;
    private ImageView mIvGroupInviteConfirm;
    private FriendSelectAdapter adapter;
    private ArrayList<Friend> Friends = new ArrayList<>();
    private ArrayList<String> checks = new ArrayList<>();
    private String mStrGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_invite);

        Intent getIntent = getIntent();
        mStrGroupId = getIntent.getStringExtra("strGroupId");

        mIvGroupInviteReturn = findViewById(R.id.iv_groupInvite_return);
        mRvGroupInviteFriendList = findViewById(R.id.rv_groupInvite_friendList);
        mIvGroupInviteConfirm = findViewById(R.id.iv_groupInvite_confirm);

        mIvGroupInviteReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRvGroupInviteFriendList.setLayoutManager(new LinearLayoutManager(GroupInviteActivity.this));
        try {
            initList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new FriendSelectAdapter(R.layout.item_friend, Friends);
        mRvGroupInviteFriendList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TextView textView = (TextView) adapter.getViewByPosition(mRvGroupInviteFriendList,position,R.id.tv_itemFriend_remark);
                String item = Friends.get(position).getUsername();
                if(!checks.contains(item)) {
                    textView.setTextColor(getResources().getColor(R.color.blue));
                    checks.add(item);
                }
                else {
                    textView.setTextColor(getResources().getColor(R.color.gray));
                    checks.remove(item);
                }
            }
        });
        mIvGroupInviteConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
    }

    private void initList() throws JSONException {
        checks.clear();
        Map res = HttpUtil.get("/friend/getFriends",GroupInviteActivity.this);
//                Log.e("11", String.valueOf(res.get("msg")==null));
        if (res.get("success").toString() == "true") {
//            ToastUtil.showMsg(NewFriendActivity.this, getResources().getString(R.string.login_successfully));
            JSONArray jsonArray = (JSONArray) res.get("friends");
            List<Map<String,Object>> list = JsonMapUtil.jsonArrayToList(jsonArray);
            Friends = new ArrayList<>();
            if(list == null) return;
            for (int i=0; i<list.size(); i++)
            {
                Map<String,Object> map = list.get(i);
                if(map.get("friendUsername").equals(GlobalVariable.getGlobalUsername())) continue;
                Friends.add(new Friend(map.get("friendAvatar").toString(),
                        map.get("friendUsername").toString(),
                        map.get("friendNickname").toString(),
                        map.get("friendRemark").toString()));
            }
        }
        else {
            ToastUtil.showMsg(GroupInviteActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }

    private void confirm() {
        if(checks.size()<1) {
            ToastUtil.showMsg(GroupInviteActivity.this,"请选择好友");
            return;
        }
        String str = "{\"groupId\":"+"\""+mStrGroupId+"\","+
                "\"usernames\":"+"[";
        for(int i = 0; i < checks.size()-1;i++) {
            str += "\""+checks.get(i)+"\"" + ",";
        }
        str+="\""+checks.get(checks.size()-1)+"\"]}";
        Map res = HttpUtil.postArray("/chat/inviteToGroup", str, GroupInviteActivity.this);
        if (res.get("success").toString() == "true") {
//               overridePendingTransition(0,0);
            ToastUtil.showMsg(GroupInviteActivity.this,"添加成功");
            finish();
        }
        else {
            ToastUtil.showMsg(GroupInviteActivity.this,
                    res.get("msg") != null ? res.get("msg").toString() : "Unknown Error");
        }
    }
}