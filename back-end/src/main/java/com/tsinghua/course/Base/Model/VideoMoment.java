package com.tsinghua.course.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 视频动态
 */
@Document("VideoMoment")
public class VideoMoment extends Moment {
    // 视频文件路径
    String videoPath;

    public String getVideoPath() {
        return videoPath;
    }
    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
}
