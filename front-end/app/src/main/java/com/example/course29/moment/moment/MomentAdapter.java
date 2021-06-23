package com.example.course29.moment.moment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.course29.R;
import com.example.course29.contact.friend.FriendActivity;
import com.example.course29.moment.MomentFragment;
import com.example.course29.moment.MomentPictureActivity;
import com.example.course29.moment.VideoActivity;
import com.example.course29.util.GlobalVariable;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.ToastUtil;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MomentAdapter extends BaseMultiItemQuickAdapter<Moment, BaseViewHolder> {
    private PopupWindow mPopWindow;

    public MomentAdapter(List<Moment> data) {
        super(data);
        addItemType(0, R.layout.item_moment_text);
        addItemType(3, R.layout.item_moment_video);
        addItemType(4, R.layout.item_moment_picture1);
        addItemType(5, R.layout.item_moment_picture2);
        addItemType(6, R.layout.item_moment_picture3);
        addItemType(7, R.layout.item_moment_picture4);
    }

    @Override
    protected void convert(BaseViewHolder helper, Moment item) {
        String isLiked = "";
        LikeAdapter likeAdapter;
        CommentAdapter commentAdapter;

        if(item.getLikes()!=null && item.getLikes().size()!=0) {
            for(int i=0;i < item.getLikes().size() ;i++) {
                Like x =item.getLikes().get(i);
                if(x.getLikeUsername().equals(GlobalVariable.getGlobalUsername())) {
                    isLiked = x.getLikeId();
                    break;
                }
            }
        }
        TextView textViewNickname = helper.itemView.findViewById(R.id.tv_itemMoment_nickname);
        ImageView imageViewAvatar = helper.itemView.findViewById(R.id.iv_itemMoment_avatar);
        textViewNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FriendActivity.class);
                intent.putExtra("strUsername",item.getUsername());
                mContext.startActivity(intent);
            }
        });
        imageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FriendActivity.class);
                intent.putExtra("strUsername",item.getUsername());
                mContext.startActivity(intent);
            }
        });

        helper.setText(R.id.tv_itemMoment_nickname, !item.getRemark().equals("") ? item.getRemark() : item.getNickname());
        helper.setText(R.id.tv_itemMoment_time, item.getPublishTime());
        Glide.with(mContext)
                .load(item.getAvatar())
                .into(imageViewAvatar);
        ImageView imageViewLike = helper.itemView.findViewById(R.id.iv_itemMoment_like);
        ImageView imageViewComment = helper.itemView.findViewById(R.id.iv_itemMoment_comment);
        ImageView imageViewDelete = helper.itemView.findViewById(R.id.iv_itemMoment_delete);
        RecyclerView recyclerViewLike = helper.itemView.findViewById(R.id.rv_itemMoment_likeList);
        RecyclerView recyclerViewComment = helper.itemView.findViewById(R.id.rv_itemMoment_commentList);

        if(!item.getUsername().equals(GlobalVariable.getGlobalUsername())) {
            imageViewDelete.setVisibility(View.GONE);
        }
        else {
            imageViewDelete.setVisibility(View.VISIBLE);
        }

        imageViewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(helper,item);
            }
        });
        if(!isLiked.equals("")) {
            imageViewLike.setColorFilter(mContext.getResources().getColor(R.color.SecondaryRed));
        }
        else {
            imageViewLike.setColorFilter(Color.BLACK);
        }
        String finalIsLiked = isLiked;
        imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalIsLiked.equals("")) {
                    Map map = new HashMap();
                    map.put("momentId", item.getMomentId());
                    map.put("momentUsername", item.getUsername());
                    Map res = HttpUtil.post("/moment/likeMoment", map, mContext);
                    if (res.get("success").toString() == "true") {
                        ToastUtil.showMsg(mContext, "点赞成功");
//                overridePendingTransition(0,0);
                    } else {
                        ToastUtil.showMsg(mContext,
                                res.get("msg") != null ? res.get("msg").toString() : "Unknown Error");
                    }
                    try {
                        MomentFragment.refresh(helper.getLayoutPosition(),item.getMomentId(),mContext);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Map map = new HashMap();
                    map.put("momentId", item.getMomentId());
                    map.put("likeId", finalIsLiked);
                    Map res = HttpUtil.post("/moment/cancelLikeMoment", map, mContext);
                    if (res.get("success").toString() == "true") {
                        ToastUtil.showMsg(mContext, "取消点赞成功");
//                overridePendingTransition(0,0);
                    } else {
                        ToastUtil.showMsg(mContext,
                                res.get("msg") != null ? res.get("msg").toString() : "Unknown Error");
                    }
                }
                try {
                    MomentFragment.refresh(helper.getLayoutPosition(),item.getMomentId(),mContext);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm,null);
        AlertDialog.Builder layoutDialog = new AlertDialog.Builder(mContext);
        TextView dialogTvText = contentView.findViewById(R.id.tv_dialogConfirm_text);
        dialogTvText.setText("确认删除该动态？");
        Button dialogBtnConfirm = contentView.findViewById(R.id.btn_dialogConfirm_confirm);
        Button dialogBtnCancel = contentView.findViewById(R.id.btn_dialogConfirm_cancel);
        layoutDialog.setView(contentView);
        Dialog dialog = layoutDialog.create();
        dialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map map = new HashMap();
                map.put("momentId",item.getMomentId());
                Map res = HttpUtil.post("/moment/removeMoment",map, mContext);
                if (res.get("success").toString() == "true") {
                    MomentFragment.remove(helper.getLayoutPosition());
                    ToastUtil.showMsg(mContext, "删除成功");
                }
                else {
                    ToastUtil.showMsg(mContext,
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
        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        switch (helper.getItemViewType()) {
            case 0: {
                helper.setText(R.id.tv_itemMomentText_content, item.getTextContent());
                break;
            }
            case 3: {
                ImageView imageViewVideo = helper.itemView.findViewById(R.id.iv_itemMomentVideo_video);
                Glide.with(mContext)
                        .setDefaultRequestOptions(
                                new RequestOptions()
                                        .frame(1000000)
                                        .centerCrop()
                        )
                        .load(item.getVideo())
                        .override(200,200)
                        .into(imageViewVideo);


                imageViewVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(mContext, VideoActivity.class);
//                        intent.putExtra("video",item.getVideo());
//                        mContext.startActivity(intent);
                        View viewVideo = LayoutInflater.from(mContext).inflate(R.layout.dialog_video,null);
                        AlertDialog.Builder layoutDialogVideo = new AlertDialog.Builder(mContext);

                        VideoView vvVideoView = viewVideo.findViewById(R.id.vv_dialogVideo_video);
                        vvVideoView.setVideoPath(item.getVideo());
                        vvVideoView.seekTo(0);
                        vvVideoView.start();
                        MediaController mediaController = new MediaController(mContext);
                        vvVideoView.setMediaController(mediaController);
                        mediaController.setMediaPlayer(vvVideoView);

                        layoutDialogVideo.setView(viewVideo);
                        Dialog dialogVideo = layoutDialogVideo.create();

                        dialogVideo.show();
                    }
                });
                break;
            }
            case 4: {
                TextView textView = helper.itemView.findViewById(R.id.tv_itemMomentPicture1_content);
                Glide.with(mContext)
                        .load(item.getImages().get(0))
                        .override(200,200)
                        .into((ImageView) helper.itemView.findViewById(R.id.iv_itemMomentPicture1_picture1));
                if(item.getTextContent().equals("")) {
                    textView.setVisibility(View.GONE);
                }
                else {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(item.getTextContent());
                }
                break;
            }
            case 5: {
                TextView textView = helper.itemView.findViewById(R.id.tv_itemMomentPicture2_content);
                Glide.with(mContext)
                        .load(item.getImages().get(0))
                        .override(200,200)
                        .into((ImageView) helper.itemView.findViewById(R.id.iv_itemMomentPicture2_picture1));
                Glide.with(mContext)
                        .load(item.getImages().get(1))
                        .override(200,200)
                        .into((ImageView) helper.itemView.findViewById(R.id.iv_itemMomentPicture2_picture2));
                if(item.getTextContent().equals("")) {
                    textView.setVisibility(View.GONE);
                }
                else {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(item.getTextContent());
                }
                break;
            }
            case 6: {
                TextView textView = helper.itemView.findViewById(R.id.tv_itemMomentPicture3_content);
                Glide.with(mContext)
                        .load(item.getImages().get(0))
                        .override(200,200)
                        .into((ImageView) helper.itemView.findViewById(R.id.iv_itemMomentPicture3_picture1));
                Glide.with(mContext)
                        .load(item.getImages().get(1))
                        .override(200,200)
                        .into((ImageView) helper.itemView.findViewById(R.id.iv_itemMomentPicture3_picture2));
                Glide.with(mContext)
                        .load(item.getImages().get(2))
                        .override(200,200)
                        .into((ImageView) helper.itemView.findViewById(R.id.iv_itemMomentPicture3_picture3));
                if(item.getTextContent().equals("")) {
                    textView.setVisibility(View.GONE);
                }
                else {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(item.getTextContent());
                }
                break;
            }
            case 7: {
                TextView textView = helper.itemView.findViewById(R.id.tv_itemMomentPicture4_content);
                Glide.with(mContext)
                        .load(item.getImages().get(0))
                        .override(200,200)
                        .into((ImageView) helper.itemView.findViewById(R.id.iv_itemMomentPicture4_picture1));
                Glide.with(mContext)
                        .load(item.getImages().get(1))
                        .override(200,200)
                        .into((ImageView) helper.itemView.findViewById(R.id.iv_itemMomentPicture4_picture2));
                Glide.with(mContext)
                        .load(item.getImages().get(2))
                        .override(200,200)
                        .into((ImageView) helper.itemView.findViewById(R.id.iv_itemMomentPicture4_picture3));
                Glide.with(mContext)
                        .load(item.getImages().get(3))
                        .override(200,200)
                        .into((ImageView) helper.itemView.findViewById(R.id.iv_itemMomentPicture4_picture4));
                if(item.getTextContent().equals("")) {
                    textView.setVisibility(View.GONE);
                }
                else {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(item.getTextContent());
                }
                break;
            }
        }

//        recyclerViewLike.setLayoutManager(new LinearLayoutManager(mContext));
        likeAdapter = new LikeAdapter(R.layout.item_like,item.getLikes());;
        recyclerViewLike.setAdapter(likeAdapter);

        recyclerViewComment.setLayoutManager(new LinearLayoutManager(mContext));
        commentAdapter = new CommentAdapter(R.layout.item_comment,item.getComments());;
        recyclerViewComment.setAdapter(commentAdapter);
        commentAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                PopupMenu popup = new PopupMenu(mContext, view);//第二个参数是绑定的那个view
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.delete_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem it) {
                        if (it.getItemId() == R.id.delete_delete)
                        {
                            Map map = new HashMap();
                            map.put("momentId",item.getMomentId());
                            map.put("commentId",item.getComments().get(position).getCommentId());
                            Map res = HttpUtil.post("/moment/removeComment",map, mContext);
                            if (res.get("success").toString() == "true") {
                                ToastUtil.showMsg(mContext, "删除成功");
                                try {
                                    MomentFragment.refresh(helper.getLayoutPosition(),item.getMomentId(),mContext);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
//                overridePendingTransition(0,0);
                            }
                            else {
                                ToastUtil.showMsg(mContext,
                                        res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                            }
                        }
                        return false;
                    }
                });
                popup.show();

                return false;
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void showPopupWindow(BaseViewHolder helper, Moment item) {
        //设置contentView
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_popup, null);
        mPopWindow = new PopupWindow(contentView,
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        //防止PopupWindow被软件盘挡住（可能只要下面一句，可能需要这两句）
        mPopWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置软键盘弹出
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(1000, InputMethodManager.HIDE_NOT_ALWAYS);//这里给它设置了弹出的时间
        //设置各个控件的点击响应
        EditText editText = contentView.findViewById(R.id.pop_editText);
        Button btn = contentView.findViewById(R.id.pop_btn);
        editText.requestFocus();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputString = editText.getText().toString();
                Map map = new HashMap();
                map.put("momentId",item.getMomentId());
                map.put("content",inputString);
                map.put("momentUsername",item.getUsername());
                Map res = HttpUtil.post("/moment/commentOnMoment",map, mContext);
                if (res.get("success").toString() == "true") {
                    ToastUtil.showMsg(mContext, "评论成功");
                    try {
                        MomentFragment.refresh(helper.getLayoutPosition(),item.getMomentId(),mContext);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                overridePendingTransition(0,0);
                }
                else {
                    ToastUtil.showMsg(mContext,
                            res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                }
                mPopWindow.dismiss();//让PopupWindow消失
            }
        });
        //是否具有获取焦点的能力
        mPopWindow.setFocusable(true);
        //显示PopupWindow
        View rootview = LayoutInflater.from(mContext).inflate(R.layout.activity_menu, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }



}
