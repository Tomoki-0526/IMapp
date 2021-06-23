package com.example.course29.chat.chatContent;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.course29.R;
import com.example.course29.UserInfo.UserActivity;
import com.example.course29.chat.chatContent.ChatContent;
import com.example.course29.chat.map.AmapActivity;
import com.example.course29.chat.record.AudioPlayerView;
import com.example.course29.contact.friend.FriendActivity;
import com.example.course29.util.GlobalVariable;

import java.util.List;

public class ChatHistoryAdapter extends BaseQuickAdapter<ChatContent, BaseViewHolder> {
    public ChatHistoryAdapter(int layoutResId, @Nullable List<ChatContent> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatContent item) {
        ImageView imageViewImage = helper.itemView.findViewById(R.id.iv_itemChatHistory_image);
        ImageView imageViewVideo = helper.itemView.findViewById(R.id.iv_itemChatHistory_video);
        TextView textViewText = helper.itemView.findViewById(R.id.tv_itemChatHistory_text);
        AudioPlayerView audioPlayerView = helper.itemView.findViewById(R.id.apv_itemHistory_audio);
        TextView textViewTime = helper.itemView.findViewById(R.id.tv_itemChatHistory_time);
        TextView textViewNickname = helper.itemView.findViewById(R.id.tv_itemChatHistory_nickname);
        CheckBox checkBox = helper.itemView.findViewById(R.id.cb_itemChatHistory_check);
        TextView textViewPosition = helper.itemView.findViewById(R.id.tv_itemChatHistory_position);

        checkBox.setChecked(false);

        imageViewImage.setVisibility(View.GONE);
        imageViewVideo.setVisibility(View.GONE);
        textViewText.setVisibility(View.GONE);
        audioPlayerView.setVisibility(View.GONE);
        textViewPosition.setVisibility(View.GONE);
        if (item.getFromMyself().equals("true")) {
            textViewNickname.setTextColor(mContext.getResources().getColor(R.color.primary));
        }
        else {
            textViewNickname.setTextColor(mContext.getResources().getColor(R.color.blue));
        }
        textViewNickname.setText(item.getRemark().equals("")?item.getNickname():item.getRemark());
        textViewTime.setText(item.getSendTime());
        switch (item.getType()){
            case "0" : {
                textViewText.setVisibility(View.VISIBLE);
                textViewText.setText(item.getText());
                break;
            }
            case "1": {
                imageViewImage.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(item.getImage())
                        .override(200, 200)
                        .into(imageViewImage);
                break;
            }
            case "2": {
                audioPlayerView.setVisibility(View.VISIBLE);
                audioPlayerView.setUrl(item.getAudio());
            }
            case "3": {
                imageViewVideo.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .setDefaultRequestOptions(
                                new RequestOptions()
                                        .frame(1000000)
                                        .centerCrop()
                        )
                        .load(item.getVideo())
                        .override(200, 200)
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
            case "4": {
                textViewPosition.setVisibility(View.VISIBLE);
                Drawable leftDrawable = mContext.getResources().getDrawable(R.mipmap.icon_nearby);
                leftDrawable.setBounds(0, 0, 50, 50);
                textViewPosition.setCompoundDrawables(leftDrawable, null, null, null);
                textViewPosition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, AmapActivity.class);
                        intent.putExtra("longitude",item.getLongitude());
                        intent.putExtra("latitude",item.getLatitude());
                        intent.putExtra("strLinkId","");
                        intent.putExtra("strMultiple","");
                        mContext.startActivity(intent);
                    }
                });
            }
            default:
                break;
        }
    }

}
