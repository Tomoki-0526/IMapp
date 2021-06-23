package com.example.course29.moment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.course29.R;
import com.example.course29.contact.friend.Friend;
import com.example.course29.contact.friend.FriendAdapter;
import com.example.course29.moment.moment.Comment;
import com.example.course29.moment.moment.Like;
import com.example.course29.moment.moment.Moment;
import com.example.course29.moment.moment.MomentAdapter;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.JsonMapUtil;
import com.example.course29.util.ToastUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MomentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MomentFragment extends Fragment {
    private FloatingActionButton mfbMomentAddMoment;
    private FloatingActionButton mfbMomentAddToTop;
    private RecyclerView mRvMomentMomentList;
    private SwipeRefreshLayout mSrlMomentRefresh;
    private static MomentAdapter adapter;
    private static ArrayList<Moment> moments = new ArrayList<>();

    public MomentFragment() {
        // Required empty public constructor
    }

    public static MomentFragment newInstance(String param1, String param2) {
        MomentFragment fragment = new MomentFragment();
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
        return inflater.inflate(R.layout.fragment_moment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mfbMomentAddMoment = view.findViewById(R.id.fb_moment_addMoment);
        mfbMomentAddToTop = view.findViewById(R.id.fb_moment_toTop);
        mRvMomentMomentList = view.findViewById(R.id.rv_moment_momentList);
        mSrlMomentRefresh = view.findViewById(R.id.srl_moment_refresh);
        mSrlMomentRefresh.setColorSchemeResources(
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_green_light);
        mSrlMomentRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                upLoad();
            }
        });
        mfbMomentAddToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRvMomentMomentList.scrollToPosition(0);
                upLoad();
            }
        });

        mfbMomentAddMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建弹出式菜单对象（最低版本11）
                PopupMenu popup = new PopupMenu(getContext(), v);//第二个参数是绑定的那个view
                //获取菜单填充器
                MenuInflater inflater = popup.getMenuInflater();
                //填充菜单
                inflater.inflate(R.menu.pop_moment_menu, popup.getMenu());
                //绑定菜单项的点击事件
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = null;
                        switch (item.getItemId()) {
                            case R.id.moment_text:
                                intent = new Intent(getActivity(),MomentTextActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.moment_picture:
                                intent = new Intent(getActivity(),MomentPictureActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.moment_video:
                                intent = new Intent(getActivity(),MomentVideoActivity.class);
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

        try {
            initList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRvMomentMomentList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MomentAdapter(moments);
        mRvMomentMomentList.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        upLoad();
    }

    public static void remove (int x) {
        Log.e("222",String.valueOf(x)+" "+String.valueOf(moments.size()-x));
        moments.remove(x);
//        adapter.notifyItemChanged(x);
//        adapter.notifyItemRangeChanged(x,moments.size()-x);
        adapter.notifyDataSetChanged();
    }

    public static void refresh(int x, String momentId, Context context) throws JSONException {
        Map idMpap = new HashMap();
        idMpap.put("momentId",momentId);
        Map res = HttpUtil.post("/moment/getSingleMoment",idMpap, context);
//                Log.e("11", String.valueOf(res.get("msg")==null));
        if (res.get("success").toString() == "true") {
//            ToastUtil.showMsg(NewFriendActivity.this, getResources().getString(R.string.login_successfully));
            JSONObject jsonObject = (JSONObject) res.get("moment");
            Map<String, Object> map = JsonMapUtil.getMap(jsonObject.toString());
            List<String> imagesResult = new ArrayList<>();
            JSONArray images = (JSONArray) map.get("images");
            if (images != null && images.length() != 0) {
                for (int j = 0; j < images.length(); j++) {
                    imagesResult.add(images.getString(j));
                }
            }
            int itemType = 0;
            if (map.get("type").toString().equals("2")) {
                itemType = 3 + imagesResult.size();
            } else if (map.get("type").toString().equals("1")) {
                itemType = 3 + imagesResult.size();
            } else {
                itemType = Integer.parseInt(map.get("type").toString());
            }

            List<Like> likesResult = new ArrayList<>();
            List<Map<String, Object>> likes = JsonMapUtil.jsonArrayToList((JSONArray) map.get("likes"));
            if (likes != null && likes.size() != 0) {
                for (int j = 0; j < likes.size(); j++) {
                    Map<String, Object> likesMap = likes.get(j);
                    likesResult.add(new Like(likesMap.get("likeId").toString(),
                            likesMap.get("likeAvatar").toString(),
                            likesMap.get("likeUsername").toString(),
                            likesMap.get("likeNickname").toString(),
                            likesMap.get("likeRemark").toString(),
                            likesMap.get("likeTime").toString(),
                            likesMap.get("friend").toString()));
                }
            }

            List<Comment> commentsResult = new ArrayList<>();
            List<Map<String, Object>> comments = JsonMapUtil.jsonArrayToList((JSONArray) map.get("comments"));
            if (comments != null && comments.size() != 0) {
                for (int j = 0; j < comments.size(); j++) {
                    Map<String, Object> commentsMap = comments.get(j);
                    commentsResult.add(new Comment(commentsMap.get("commentId").toString(),
                            commentsMap.get("commentUsername").toString(),
                            commentsMap.get("commentNickname").toString(),
                            commentsMap.get("commentRemark").toString(),
                            commentsMap.get("commentContent").toString(),
                            commentsMap.get("commentTime").toString(),
                            commentsMap.get("friend").toString()));
                }
            }
            moments.remove(x);

            moments.add(x, new Moment(map.get("avatar").toString(),
                    map.get("username").toString(),
                    map.get("momentId").toString(),
                    map.get("nickname").toString(),
                    map.get("remark").toString(),
                    map.get("publishTime").toString(),
                    map.get("type").toString(),
                    map.get("textContent").toString(),
                    imagesResult,
                    map.get("video").toString(),
                    map.get("likesNum").toString(),
                    likesResult,
                    map.get("commentsNum").toString(),
                    commentsResult,
                    itemType));
            adapter.notifyItemChanged(x);
            adapter.notifyItemRangeChanged(x, moments.size() - x);
        }
        else {
            ToastUtil.showMsg(context,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }

    }

    private void initList() throws JSONException {
        Map res = HttpUtil.get("/moment/getMoments",getActivity());
//                Log.e("11", String.valueOf(res.get("msg")==null));
        if (res.get("success").toString() == "true") {
//            ToastUtil.showMsg(NewFriendActivity.this, getResources().getString(R.string.login_successfully));
            JSONArray jsonArray = (JSONArray) res.get("moments");
            List<Map<String,Object>> list = JsonMapUtil.jsonArrayToList(jsonArray);
            moments.clear();
            if(list == null) return;
            for (int i=0; i<list.size(); i++)
            {
                Map<String,Object> map = list.get(i);

                List<String> imagesResult = new ArrayList<>();
                JSONArray images = (JSONArray)map.get("images");
                if(images!=null && images.length()!=0) {
                    for (int j = 0; j < images.length(); j++) {
                        imagesResult.add(images.getString(j));
                    }
                }
                int itemType = 0;
                if(map.get("type").toString().equals("2") ) {
                    itemType = 3+imagesResult.size();
                }
                else if (map.get("type").toString().equals("1")){
                    itemType = 3+imagesResult.size();
                }
                else {
                    itemType = Integer.parseInt(map.get("type").toString());
                }

                List<Like> likesResult = new ArrayList<>();
                List<Map<String,Object>> likes = JsonMapUtil.jsonArrayToList((JSONArray)map.get("likes"));
                if(likes!=null && likes.size()!=0) {
                    for (int j = 0; j < likes.size(); j++) {
                        Map<String,Object> likesMap = likes.get(j);
                        likesResult.add(new Like(likesMap.get("likeId").toString(),
                                likesMap.get("likeAvatar").toString(),
                                likesMap.get("likeUsername").toString(),
                                likesMap.get("likeNickname").toString(),
                                likesMap.get("likeRemark").toString(),
                                likesMap.get("likeTime").toString(),
                                likesMap.get("friend").toString()));
                    }
                }

                List<Comment> commentsResult = new ArrayList<>();
                List<Map<String,Object>> comments = JsonMapUtil.jsonArrayToList((JSONArray)map.get("comments"));
                if(comments!=null && comments.size()!=0) {
                    for (int j = 0; j < comments.size(); j++) {
                        Map<String,Object> commentsMap = comments.get(j);
                        commentsResult.add(new Comment(commentsMap.get("commentId").toString(),
                                commentsMap.get("commentUsername").toString(),
                                commentsMap.get("commentNickname").toString(),
                                commentsMap.get("commentRemark").toString(),
                                commentsMap.get("commentContent").toString(),
                                commentsMap.get("commentTime").toString(),
                                commentsMap.get("friend").toString()));
                    }
                }
                moments.add(new Moment(map.get("avatar").toString(),
                        map.get("username").toString(),
                        map.get("momentId").toString(),
                        map.get("nickname").toString(),
                        map.get("remark").toString(),
                        map.get("publishTime").toString(),
                        map.get("type").toString(),
                        map.get("textContent").toString(),
                        imagesResult,
                        map.get("video").toString(),
                        map.get("likesNum").toString(),
                        likesResult,
                        map.get("commentsNum").toString(),
                        commentsResult,
                        itemType));
            }
        }
        else {
            ToastUtil.showMsg(getActivity(),
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }

    private void upLoad () {
        // 开始转动
        mSrlMomentRefresh.setRefreshing(true);
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
                        mSrlMomentRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

}