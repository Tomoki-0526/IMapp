package com.example.course29.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.course29.R;
import com.example.course29.contact.friend.Friend;
import com.example.course29.contact.friend.FriendActivity;
import com.example.course29.contact.friend.FriendAdapter;
import com.example.course29.contact.newFriend.StrangerInfoActivity;
import com.example.course29.util.BitmapUtil;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.JsonMapUtil;
import com.example.course29.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    private ImageView mIvSearchReturn;
    private ImageView mIvSearchSearch;
    private EditText mEtSearchContent;
    private LinearLayout mLlSearchAdd;
    private String mStrContent;
    private RecyclerView mRvSearchFriendList;
    private FriendAdapter adapter;
    private ArrayList<Friend> Friends = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mIvSearchReturn = findViewById(R.id.iv_search_return);
        mIvSearchSearch = findViewById(R.id.iv_search_search);
        mEtSearchContent = findViewById(R.id.iv_search_content);
        mLlSearchAdd = findViewById(R.id.ll_search_add);
        mRvSearchFriendList = findViewById(R.id.rv_search_friendList);

        mIvSearchReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mIvSearchSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStrContent = mEtSearchContent.getText().toString();
                if(mStrContent != null && !mStrContent.equals("")) {
                    mLlSearchAdd.setVisibility(View.VISIBLE);
                    mRvSearchFriendList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                    try {
                        initList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter = new FriendAdapter(R.layout.item_friend, Friends);
                    mRvSearchFriendList.setAdapter(adapter);
                    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            Intent intent = new Intent(SearchActivity.this, FriendActivity.class);
                            intent.putExtra("strUsername",Friends.get(position).getUsername());
                            startActivity(intent);
                        }
                    });
                }
                else {
                    mLlSearchAdd.setVisibility(View.GONE);
                }
            }
        });

        mLlSearchAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                map.put("stranger_username",mStrContent);
                Map res = HttpUtil.post("/friend/findStranger",map, SearchActivity.this);
                if (res.get("success").toString() == "true") {
                    Intent intent = new Intent(SearchActivity.this, StrangerInfoActivity.class);
                    intent.putExtra("strUsername",res.get("stranger_username").toString());
                    startActivity(intent);
                }
                else {
                    ToastUtil.showMsg(SearchActivity.this,
                            res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                }
            }
        });
    }

    private void initList() throws JSONException {
        Map mmap = new HashMap();
        mmap.put("content",mStrContent);
        Map res = HttpUtil.post("/friend/findFriend",mmap,SearchActivity.this);
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
                Friends.add(new Friend(map.get("friendAvatar").toString(),
                        map.get("friendUsername").toString(),
                        map.get("friendNickname").toString(),
                        map.get("friendRemark").toString()));
            }
        }
        else {
            ToastUtil.showMsg(SearchActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }
}