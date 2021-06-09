package com.example.course29.contact.newFriend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.course29.R;
import com.example.course29.contact.friend.FriendActivity;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.JsonMapUtil;
import com.example.course29.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewFriendActivity extends AppCompatActivity {

    private RecyclerView mRvNewFriendList;
    private NewFriendAdapter adapter;
    private ImageView mIvNewFriendReturn;
    private ArrayList<NewFriend> newFriends = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        setContentView(R.layout.activity_new_friend);

        mRvNewFriendList = findViewById(R.id.rv_newFriend_list);
        mIvNewFriendReturn = findViewById(R.id.iv_newFriend_return);

        mIvNewFriendReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRvNewFriendList.setLayoutManager(new LinearLayoutManager(this));
        try {
            initList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new NewFriendAdapter(R.layout.item_new_friend, newFriends);
        mRvNewFriendList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = null;
                switch (newFriends.get(position).getStatus()) {
                    case "0":
                        intent = new Intent(NewFriendActivity.this, StrangerAddActivity.class);
                        intent.putExtra("strUsername", newFriends.get(position).getUsername());
                        break;
                    case "1":
                        intent = new Intent(NewFriendActivity.this, FriendActivity.class);
                        intent.putExtra("strUsername", newFriends.get(position).getUsername());
                        break;
                    default:
                        intent = new Intent(NewFriendActivity.this, StrangerInfoActivity.class);
                        intent.putExtra("strUsername", newFriends.get(position).getUsername());
                        break;

                }
//                Log.e("tag",newFriends.get(position).getUsername());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            initList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new NewFriendAdapter(R.layout.item_new_friend, newFriends);
        mRvNewFriendList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = null;
                switch (newFriends.get(position).getStatus()) {
                    case "0":
                        intent = new Intent(NewFriendActivity.this, StrangerAddActivity.class);
                        intent.putExtra("strUsername", newFriends.get(position).getUsername());
                        break;
                    case "1":
                        intent = new Intent(NewFriendActivity.this, FriendActivity.class);
                        intent.putExtra("strUsername", newFriends.get(position).getUsername());
                        break;
                    default:
                        intent = new Intent(NewFriendActivity.this, StrangerInfoActivity.class);
                        intent.putExtra("strUsername", newFriends.get(position).getUsername());
                        break;

                }
//                Log.e("tag",newFriends.get(position).getUsername());
                startActivity(intent);
            }
        });
    }

    private void initList() throws JSONException {
        Map res = HttpUtil.get("/friend/getFriendRequest",NewFriendActivity.this);
//                Log.e("11", String.valueOf(res.get("msg")==null));
        if (res.get("success").toString() == "true") {
//            ToastUtil.showMsg(NewFriendActivity.this, getResources().getString(R.string.login_successfully));
            JSONArray jsonArray = (JSONArray) res.get("friend_request");
            List<Map<String,Object>> list = JsonMapUtil.jsonArrayToList(jsonArray);
            newFriends= new ArrayList<>();
            if(list == null) return;
            for (int i=0; i<list.size(); i++)
            {
                Map<String,Object> map = list.get(i);
                newFriends.add(new NewFriend(map.get("fromAvatar").toString(),
                        map.get("fromUsername").toString(),
                        map.get("fromNickname").toString(),
                        map.get("extra").toString(),
                        map.get("status").toString()));

            }
        }
        else {
            ToastUtil.showMsg(NewFriendActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }
}