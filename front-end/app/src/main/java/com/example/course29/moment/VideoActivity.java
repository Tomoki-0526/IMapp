package com.example.course29.moment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.course29.R;

public class VideoActivity extends AppCompatActivity {
    private String video;
    private VideoView mVvVideoVideo;
    private ImageView mIvVideoReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent getIntent = getIntent();
        video = getIntent.getStringExtra("video");

        mIvVideoReturn = findViewById(R.id.iv_video_return);
        mIvVideoReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mVvVideoVideo = findViewById(R.id.vv_video_video);
        mVvVideoVideo.setVideoPath(video);
        mVvVideoVideo.start();
        MediaController mediaController = new MediaController(VideoActivity.this);
        mVvVideoVideo.setMediaController(mediaController);
        mediaController.setMediaPlayer(mVvVideoVideo);

    }
}