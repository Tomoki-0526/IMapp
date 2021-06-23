package com.example.course29.contact.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.course29.R;
import com.example.course29.chat.Chat;
import com.example.course29.chat.ChatAdapter;
import com.example.course29.chat.chatContent.ChatContent;
import com.example.course29.chat.chatContent.ChatContentActivity;
import com.example.course29.moment.MomentPictureActivity;
import com.example.course29.moment.MomentTextActivity;
import com.example.course29.moment.MomentVideoActivity;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.JsonMapUtil;
import com.example.course29.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupActivity extends AppCompatActivity {
    private ImageView mIvGroupReturn;
    private ImageView mIvGroupCreate;
    private RecyclerView mRvGroupGroupList;
    private GroupAdapter adapter;
    private ArrayList<Group> Groups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        mIvGroupReturn = findViewById(R.id.iv_group_return);
        mIvGroupCreate = findViewById(R.id.iv_group_create);
        mRvGroupGroupList = findViewById(R.id.rv_group_groupList);

        mIvGroupReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRvGroupGroupList.setLayoutManager(new LinearLayoutManager(GroupActivity.this));
        try {
            initList();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIvGroupCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建弹出式菜单对象（最低版本11）
                PopupMenu popup = new PopupMenu(GroupActivity.this, v);//第二个参数是绑定的那个view
                //获取菜单填充器
                MenuInflater inflater = popup.getMenuInflater();
                //填充菜单
                inflater.inflate(R.menu.group_create_menu, popup.getMenu());
                //绑定菜单项的点击事件
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = null;
                        switch (item.getItemId()) {
                            case R.id.groupMenu_create:
                                intent = new Intent(GroupActivity.this, GroupCreateActivity.class);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        adapter = new GroupAdapter(R.layout.item_group, Groups);
        mRvGroupGroupList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(GroupActivity.this, ChatContentActivity.class);
                intent.putExtra("strUsername","");
                intent.putExtra("strName",Groups.get(position).getGroupName());
                intent.putExtra("strMultiple","true");
                intent.putExtra("strLinkId",Groups.get(position).getGroupId());
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
        Map res = HttpUtil.get("/chat/getGroups",GroupActivity.this);
//                Log.e("11", String.valueOf(res.get("msg")==null));
        if (res.get("success").toString() == "true") {
            JSONArray jsonArray = (JSONArray) res.get("groups");
            List<Map<String,Object>> list = JsonMapUtil.jsonArrayToList(jsonArray);
            Groups.clear();
            if(list == null) return;
            for (int i=0; i<list.size(); i++)
            {
                Map<String,Object> map = list.get(i);
                Groups.add(new Group(map.get("groupId").toString(),
                        map.get("avatar").toString(),
                        map.get("groupName").toString()));
            }
        }
        else {
            ToastUtil.showMsg(GroupActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }
}