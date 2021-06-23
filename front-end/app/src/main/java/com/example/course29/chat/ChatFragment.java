package com.example.course29.chat;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.course29.MenuActivity;
import com.example.course29.R;
import com.example.course29.WebSocketService;
import com.example.course29.chat.chatContent.ChatContentActivity;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.JsonMapUtil;
import com.example.course29.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    private SwipeRefreshLayout mSrlChatRefresh;
    private RecyclerView mRvChatChatList;
    private ChatAdapter adapter;
    private ArrayList<Chat> Chats = new ArrayList<>();

    private ChatFragment.InnerServiceConnection serviceConnection = new ChatFragment.InnerServiceConnection();
    private WebSocketService.InnerIBinder binder;
    private WebSocketService webSocketService;
    private ChatFragment.ChatMessageReceiver chatMessageReceiver;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //启动service
        startWebSocketService();
        //绑定服务
        bindService();
        doRegisterReceiver();

        mRvChatChatList = view.findViewById(R.id.rv_chat_chatList);
        mRvChatChatList.setLayoutManager(new LinearLayoutManager(getContext()));
        mSrlChatRefresh = view.findViewById(R.id.srl_chat_refresh);
        try {
            initList();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new ChatAdapter(R.layout.item_chat, Chats);
        mRvChatChatList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), ChatContentActivity.class);
//                if (Chats.get(position).getMultiple().equals("false")){
//                    intent = new Intent(getActivity(), ChatContentActivity.class);
//                }
//                else {
//                    intent = new Intent(getActivity(), ChatMoreContentActivity.class);
//                }
                intent.putExtra("strUsername",Chats.get(position).getUsername());
                intent.putExtra("strName",Chats.get(position).getName());
                intent.putExtra("strMultiple",Chats.get(position).getMultiple());
                intent.putExtra("strLinkId",Chats.get(position).getLinkId());
                startActivity(intent);
            }
        });
        mSrlChatRefresh.setColorSchemeResources(
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_green_light);
        mSrlChatRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                upLoad();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        upLoad();
    }

    private void initList() throws JSONException {
        Map res = HttpUtil.get("/chat/getChattings",getActivity());
//                Log.e("11", String.valueOf(res.get("msg")==null));
        if (res.get("success").toString() == "true") {
            JSONArray jsonArray = (JSONArray) res.get("chattings");
            List<Map<String,Object>> list = JsonMapUtil.jsonArrayToList(jsonArray);
            Chats.clear();
            if(list == null) return;
            for (int i=0; i<list.size(); i++)
            {
                Map<String,Object> map = list.get(i);
                Chats.add(new Chat(map.get("avatar").toString(),
                        map.get("linkId").toString(),
                        map.get("multiple").toString(),
                        map.get("name").toString(),
                        map.get("latestMsg").toString(),
                        map.get("sendTime").toString(),
                        map.get("type").toString(),
                        map.get("username").toString(),
                        map.get("unread").toString()));
            }
        }
        else {
            ToastUtil.showMsg(getActivity(),
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }

    private void upLoad() {
        mSrlChatRefresh.setRefreshing(true);
        new Thread(new Runnable() {
            // ------------------- 开启子线程
            @Override public void run() {
                try {
                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        // ------------- 主线程
                        try {
                            initList();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                        // 停止转动
                        mSrlChatRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private class ChatMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message=intent.getStringExtra("message");
            Log.e("message",message);
            try {
                initList();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ((SimpleItemAnimator)mRvChatChatList.getItemAnimator()).setSupportsChangeAnimations(false);
            adapter.notifyItemRangeChanged(0,Chats.size());
//            adapter.notifyDataSetChanged();
        }
    }

    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatMessageReceiver();
        IntentFilter filter = new IntentFilter("CCTV5");
        getContext().registerReceiver(chatMessageReceiver, filter);
    }

    private void startWebSocketService() {
        Intent intent = new Intent(getContext(), WebSocketService.class);
        getContext().startService(intent);
    }

    private void bindService() {
        Intent bindIntent = new Intent(getContext(), WebSocketService.class);
        getContext().bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    private class InnerServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("Service","onServiceConnected()->当Activity和Service连接");
            binder = (WebSocketService.InnerIBinder) service;
            webSocketService = binder.getService();
//            client = jWebSClientService.client;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("Service","onServiceConnected()->当Activity和Service断开连接");
        }
    }
}