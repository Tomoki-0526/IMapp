package com.example.course29.chat.chatContent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.course29.R;
import com.example.course29.contact.group.GroupCreateActivity;
import com.example.course29.moment.MomentFragment;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.JsonMapUtil;
import com.example.course29.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatHistoryActivity extends AppCompatActivity {
    private ImageView mIvChatHistoryReturn;
    private ImageView mIvChatHistoryDelete;
    private RecyclerView mRvChatHistoryHistoryList;
    private String mStrLinkId;
    private List<String> checks = new ArrayList<>();
    private ChatHistoryAdapter adapter;
    private ArrayList<ChatContent> ChatContents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);

        Intent getIntent = getIntent();
        mStrLinkId = getIntent.getStringExtra("strLinkId");

        mIvChatHistoryReturn = findViewById(R.id.iv_chatHistory_return);
        mIvChatHistoryDelete = findViewById(R.id.iv_chatHistory_delete);
        mRvChatHistoryHistoryList = findViewById(R.id.rv_chatHistory_historyList);

        mIvChatHistoryReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mIvChatHistoryDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(v);
            }
        });

        try {
            init();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRvChatHistoryHistoryList.setLayoutManager(new LinearLayoutManager(ChatHistoryActivity.this));
        adapter = new ChatHistoryAdapter(R.layout.item_chat_history, ChatContents);
        mRvChatHistoryHistoryList.setAdapter(adapter);
        mRvChatHistoryHistoryList.scrollToPosition(ChatContents.size()-1);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CheckBox checkBox = (CheckBox) adapter.getViewByPosition(mRvChatHistoryHistoryList,position,R.id.cb_itemChatHistory_check);
                String item = ChatContents.get(position).getMsgId();
                Log.e("msg",item);
                if(!item.equals("")) {
                    if (!checks.contains(item)) {
                        checkBox.setChecked(true);
                        checks.add(item);
                    } else {
                        checkBox.setChecked(false);
                        checks.remove(item);
                    }
                }
            }
        });
    }

    private void delete(View v) {
        if(checks.size()<1) {
            ToastUtil.showMsg(ChatHistoryActivity.this,"请选择要删除的记录");
            return;
        }
        View contentView = LayoutInflater.from(ChatHistoryActivity.this).inflate(R.layout.dialog_confirm,null);
        AlertDialog.Builder layoutDialog = new AlertDialog.Builder(ChatHistoryActivity.this);
        TextView dialogTvText = contentView.findViewById(R.id.tv_dialogConfirm_text);
        dialogTvText.setText("确认删除聊天记录？");
        Button dialogBtnConfirm = contentView.findViewById(R.id.btn_dialogConfirm_confirm);
        Button dialogBtnCancel = contentView.findViewById(R.id.btn_dialogConfirm_cancel);
        layoutDialog.setView(contentView);
        Dialog dialog = layoutDialog.create();
        dialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "{\"linkId\":"+"\""+mStrLinkId+"\","+
                        "\"msgs\":"+"[";
                for(int i = 0; i < checks.size()-1;i++) {
                    str += "\""+checks.get(i)+"\"" + ",";
                }
                str+="\""+checks.get(checks.size()-1)+"\"]}";
                Map res = HttpUtil.postArray("/chat/removeMessages", str, ChatHistoryActivity.this);
                if (res.get("success").toString() == "true") {
//                overridePendingTransition(0,0);
                    ToastUtil.showMsg(ChatHistoryActivity.this,"删除成功");
                    try {
                        init();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();

                }
                else {
                    ToastUtil.showMsg(ChatHistoryActivity.this,
                            res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                }
                dialog.dismiss();
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

    private void init() throws JSONException {
        checks.clear();
        Map map1 = new HashMap();
        map1.put("linkId",mStrLinkId);
        Map res = HttpUtil.post("/chat/getHistory",map1, ChatHistoryActivity.this);
        if (res.get("success").toString() == "true") {
//                overridePendingTransition(0,0);
            JSONArray jsonArray = (JSONArray) res.get("msgs");
            List<Map<String,Object>> list = JsonMapUtil.jsonArrayToList(jsonArray);
            ChatContents.clear();
            if(list == null) return;
            for (int i=0; i<list.size(); i++)
            {
                Map<String,Object> map = list.get(i);
                ChatContents.add(new ChatContent(map.get("username").toString(),
                        map.get("nickname").toString(),
                        map.get("remark").toString(),
                        map.get("avatar").toString(),
                        map.get("msgId").toString(),
                        map.get("sendTime").toString(),
                        map.get("fromMyself").toString(),
                        map.get("type").toString(),
                        map.get("text").toString(),
                        map.get("image").toString(),
                        map.get("audio").toString(),
                        map.get("video").toString(),
                        map.get("longitude").toString(),
                        map.get("latitude").toString()));
            }
        }
        else {
            ToastUtil.showMsg(ChatHistoryActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }

}