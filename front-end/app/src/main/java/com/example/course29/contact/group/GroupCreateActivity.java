package com.example.course29.contact.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.course29.R;
import com.example.course29.chat.chatContent.ChatContentActivity;
import com.example.course29.contact.friend.Friend;
import com.example.course29.contact.friend.FriendAdapter;
import com.example.course29.contact.newFriend.StrangerInfoActivity;
import com.example.course29.util.GlobalVariable;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.JsonMapUtil;
import com.example.course29.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupCreateActivity extends AppCompatActivity {
    private ImageView mIvGroupCreateReturn;
    private RecyclerView mRvGroupCreateFriendList;
    private ImageView mIvGroupCreateConfirm;
    private FriendSelectAdapter adapter;
    private ArrayList<Friend> Friends = new ArrayList<>();
    private ArrayList<String> checks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        mIvGroupCreateReturn = findViewById(R.id.iv_groupCreate_return);
        mRvGroupCreateFriendList = findViewById(R.id.rv_groupCreate_friendList);
        mIvGroupCreateConfirm = findViewById(R.id.iv_groupCreate_confirm);

        mIvGroupCreateReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRvGroupCreateFriendList.setLayoutManager(new LinearLayoutManager(GroupCreateActivity.this));
        try {
            initList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new FriendSelectAdapter(R.layout.item_friend, Friends);
        mRvGroupCreateFriendList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TextView textView = (TextView) adapter.getViewByPosition(mRvGroupCreateFriendList,position,R.id.tv_itemFriend_remark);
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
        mIvGroupCreateConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });


    }

    private void initList() throws JSONException {
        Map res = HttpUtil.get("/friend/getFriends",GroupCreateActivity.this);
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
            ToastUtil.showMsg(GroupCreateActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }

    private void confirm() {
        if(checks.size()<1) {
            ToastUtil.showMsg(GroupCreateActivity.this,"请选择好友");
            return;
        }
        View contentView = LayoutInflater.from(GroupCreateActivity.this).inflate(R.layout.dialog_add_stranger,null);
        AlertDialog.Builder layoutDialog = new AlertDialog.Builder(GroupCreateActivity.this);
        Button dialogBtnConfirm = contentView.findViewById(R.id.btn_dialogStrangerAdd_confirm);
        Button dialogBtnCancel = contentView.findViewById(R.id.btn_dialogStrangerAdd_cancel);
        EditText dialogEtExtra = contentView.findViewById(R.id.et_dialogStrangerAdd_extra);
        layoutDialog.setView(contentView);
        Dialog dialog = layoutDialog.create();
        dialogEtExtra.setHint("请输入群名...");
        dialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogEtExtra.getText().toString().equals("")) {
                    ToastUtil.showMsg(GroupCreateActivity.this,"请输入群名");
                    return;
                }
                String str = "{\"groupName\":"+"\""+dialogEtExtra.getText().toString()+"\","+
                        "\"members\":"+"[";
                for(int i = 0; i < checks.size()-1;i++) {
                    str += "\""+checks.get(i)+"\"" + ",";
                }
                str+="\""+checks.get(checks.size()-1)+"\"]}";
                Map res = HttpUtil.postArray("/chat/createGroup", str, GroupCreateActivity.this);
                if (res.get("success").toString() == "true") {
//                overridePendingTransition(0,0);
                    ToastUtil.showMsg(GroupCreateActivity.this,"创建成功");
                    checks.clear();
                    dialog.dismiss();
                    finish();
                }
                else {
                    ToastUtil.showMsg(GroupCreateActivity.this,
                            res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                }
            }
        });
        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}