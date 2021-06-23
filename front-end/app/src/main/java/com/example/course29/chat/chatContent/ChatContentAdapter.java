package com.example.course29.chat.chatContent;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.course29.R;
import com.example.course29.chat.map.AmapActivity;
import com.example.course29.chat.record.AudioPlayerView;
import com.example.course29.contact.friend.FriendActivity;

import java.util.List;

public class ChatContentAdapter extends BaseQuickAdapter<ChatContent, BaseViewHolder> {
    public ChatContentAdapter(int layoutResId, @Nullable List<ChatContent> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatContent item) {
        ImageView imageViewImage = helper.itemView.findViewById(R.id.iv_itemChatContent_image);
        ImageView imageViewVideo = helper.itemView.findViewById(R.id.iv_itemChatContent_video);
        AudioPlayerView audioPlayerView = helper.itemView.findViewById(R.id.apv_itemChatContent_audio);
        TextView textViewText = helper.itemView.findViewById(R.id.tv_itemChatContent_text);
        TextView textViewTime = helper.itemView.findViewById(R.id.tv_itemChatContent_time);
        TextView textViewNickname = helper.itemView.findViewById(R.id.tv_itemChatContent_nickname);
        TextView textViewPosition = helper.itemView.findViewById(R.id.tv_itemChatContent_position);

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
