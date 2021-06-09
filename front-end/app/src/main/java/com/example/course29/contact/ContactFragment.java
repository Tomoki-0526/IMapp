package com.example.course29.contact;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.course29.R;
import com.example.course29.contact.friend.Friend;
import com.example.course29.contact.friend.FriendActivity;
import com.example.course29.contact.friend.FriendAdapter;
import com.example.course29.contact.friend.StarFriendsActivity;
import com.example.course29.contact.newFriend.NewFriendActivity;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.JsonMapUtil;
import com.example.course29.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContactFragment extends Fragment {
    private LinearLayout mLlContactNewFriends;
    private RecyclerView mRvContactFriendList;
    private LinearLayout mLlContactSearch;
    private LinearLayout mLlContactStarFriends;
    private FriendAdapter adapter;
    private ArrayList<Friend> Friends = new ArrayList<>();

    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLlContactNewFriends = view.findViewById(R.id.ll_contact_newFriends);
        mRvContactFriendList = view.findViewById(R.id.rv_contact_friendList);
        mLlContactSearch = view.findViewById(R.id.ll_contact_search);
        mLlContactStarFriends = view.findViewById(R.id.ll_contact_starFriends);

        mLlContactSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        mLlContactNewFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewFriendActivity.class);
                startActivity(intent);
            }
        });
        mLlContactStarFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StarFriendsActivity.class);
                startActivity(intent);
            }
        });
        mRvContactFriendList.setLayoutManager(new LinearLayoutManager(getContext()));
        try {
            initList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new FriendAdapter(R.layout.item_friend, Friends);
        mRvContactFriendList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), FriendActivity.class);
                intent.putExtra("strUsername",Friends.get(position).getUsername());
                startActivity(intent);
            }
        });
    }

    //TODO: onResume重新做

    @Override
    public void onResume() {
        super.onResume();
        try {
            initList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new FriendAdapter(R.layout.item_friend, Friends);
        mRvContactFriendList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), FriendActivity.class);
                intent.putExtra("strUsername",Friends.get(position).getUsername());
                startActivity(intent);
            }
        });
    }

    private void initList() throws JSONException {
        Map res = HttpUtil.get("/friend/getFriends",getActivity());
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
            ToastUtil.showMsg(getActivity(),
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }
}