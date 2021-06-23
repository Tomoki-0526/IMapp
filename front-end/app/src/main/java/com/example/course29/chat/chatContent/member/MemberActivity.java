package com.example.course29.chat.chatContent.member;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.course29.R;
import com.example.course29.chat.Chat;
import com.example.course29.chat.ChatAdapter;
import com.example.course29.contact.friend.Friend;
import com.example.course29.contact.friend.FriendActivity;
import com.example.course29.contact.newFriend.StrangerInfoActivity;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.JsonMapUtil;
import com.example.course29.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberActivity extends AppCompatActivity {
    private ImageView mIvMemberReturn;
    private RecyclerView mRvMemberMemberList;
    private MemberAdapter adapter;
    private String mStrGroupId;
    private ArrayList<Member> Members = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        Intent getIntent = getIntent();
        mStrGroupId = getIntent.getStringExtra("strGroupId");

        mIvMemberReturn = findViewById(R.id.iv_member_return);
        mRvMemberMemberList = findViewById(R.id.rv_member_memberList);

        mIvMemberReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRvMemberMemberList.setLayoutManager(new LinearLayoutManager(MemberActivity.this));
        try {
            initList();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new MemberAdapter(R.layout.item_friend, Members);
        mRvMemberMemberList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = null;
                if (Members.get(position).getIsFriend().equals("true")) {
                    intent = new Intent(MemberActivity.this, FriendActivity.class);
                    intent.putExtra("strUsername",Members.get(position).getUsername());
                }
                else {
                    intent = new Intent(MemberActivity.this, StrangerInfoActivity.class);
                    intent.putExtra("strUsername",Members.get(position).getUsername());
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            initList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    private void initList() throws JSONException {
        Map map1 = new HashMap();
        map1.put("groupId",mStrGroupId);
        Map res = HttpUtil.post("/chat/getGroupMembers",map1,MemberActivity.this);
//                Log.e("11", String.valueOf(res.get("msg")==null));
        if (res.get("success").toString() == "true") {
//            ToastUtil.showMsg(NewFriendActivity.this, getResources().getString(R.string.login_successfully));
            JSONArray jsonArray = (JSONArray) res.get("members");
            List<Map<String,Object>> list = JsonMapUtil.jsonArrayToList(jsonArray);
            Members.clear();
            if(list == null) return;
            for (int i=0; i<list.size(); i++)
            {
                Map<String,Object> map = list.get(i);
                Members.add(new Member(map.get("avatar").toString(),
                        map.get("username").toString(),
                        map.get("nickname").toString(),
                        map.get("remark").toString(),
                        map.get("friend").toString()));
            }
        }
        else {
            ToastUtil.showMsg(MemberActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }
}