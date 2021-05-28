package com.example.course29.contact.friend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.course29.R;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.JsonMapUtil;
import com.example.course29.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StarFriendsActivity extends AppCompatActivity {

    private FriendAdapter adapter;
    private ArrayList<Friend> Friends = new ArrayList<>();
    private RecyclerView mRvStarFriendsList;
    private ImageView mIvStarFriendReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_friends);
        mIvStarFriendReturn = findViewById(R.id.iv_starFriends_return);
        mRvStarFriendsList = findViewById(R.id.rv_starFriends_list);

        mIvStarFriendReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRvStarFriendsList.setLayoutManager(new LinearLayoutManager(StarFriendsActivity.this));
        try {
            initList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new FriendAdapter(R.layout.item_friend, Friends);
        mRvStarFriendsList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(StarFriendsActivity.this, FriendActivity.class);
                intent.putExtra("strUsername",Friends.get(position).getUsername());
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
        adapter = new FriendAdapter(R.layout.item_friend, Friends);
        mRvStarFriendsList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(StarFriendsActivity.this, FriendActivity.class);
                intent.putExtra("strUsername",Friends.get(position).getUsername());
                startActivity(intent);
            }
        });
    }

    private void initList() throws JSONException {
        Map res = HttpUtil.get("/friend/getStarFriends",StarFriendsActivity.this);
//                Log.e("11", String.valueOf(res.get("msg")==null));
        if (res.get("success").toString() == "true") {
//            ToastUtil.showMsg(NewFriendActivity.this, getResources().getString(R.string.login_successfully));
            JSONArray jsonArray = (JSONArray) res.get("star_friends");
            List<Map<String,Object>> list = JsonMapUtil.jsonArrayToList(jsonArray);
            Friends = new ArrayList<>();
            if(list == null) return;
            for (int i=0; i<list.size(); i++)
            {
                Map<String,Object> map = list.get(i);
                Friends.add(new Friend(map.get("friendAvatar").toString(),
                        map.get("friendUsername").toString(),
                        map.get("friendNickname").toString(),
                        map.get("friendRemark").toString()));
            }
        }
        else {
            ToastUtil.showMsg(StarFriendsActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }
}