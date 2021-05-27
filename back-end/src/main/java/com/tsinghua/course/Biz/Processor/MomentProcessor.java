package com.tsinghua.course.Biz.Processor;

import com.tsinghua.course.Base.Model.ImgMoment;
import com.tsinghua.course.Base.Model.TextAndImgMoment;
import com.tsinghua.course.Base.Model.TextMoment;
import com.tsinghua.course.Base.Model.VideoMoment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

/**
 * @描述 动态原子处理器
 */
@Component
public class MomentProcessor {
    @Autowired
    MongoTemplate mongoTemplate;

    /** 发布文字动态 */
    public void publishTextMoment(String username, int type, String content) {
        TextMoment textMoment = new TextMoment();
        textMoment.setPublishTime(new Date());
        textMoment.setUsername(username);
        textMoment.setType(type);
        textMoment.setLikes(0);
        textMoment.setComments(0);
        textMoment.setContent(content);

        mongoTemplate.insert(textMoment);
    }

    /** 发布图片动态 */
    public void publishImgMoment(String username, int type, String[] imgs) {
        ImgMoment imgMoment = new ImgMoment();
        imgMoment.setPublishTime(new Date());
        imgMoment.setUsername(username);
        imgMoment.setType(type);
        imgMoment.setLikes(0);
        imgMoment.setComments(0);
        imgMoment.setImgPath(imgs);

        mongoTemplate.insert(imgMoment);
    }

    /** 发布图文动态 */
    public void publishTextAndImgMoment(String username, int type, String content, String[] imgs) {
        TextAndImgMoment textAndImgMoment = new TextAndImgMoment();
        textAndImgMoment.setPublishTime(new Date());
        textAndImgMoment.setUsername(username);
        textAndImgMoment.setType(type);
        textAndImgMoment.setLikes(0);
        textAndImgMoment.setComments(0);
        textAndImgMoment.setContent(content);
        textAndImgMoment.setImgPath(imgs);

        mongoTemplate.insert(textAndImgMoment);
    }

    /** 发布视频动态 */
    public void publishVideoMoment(String username, int type, String video) {
        VideoMoment videoMoment = new VideoMoment();
        videoMoment.setPublishTime(new Date());
        videoMoment.setUsername(username);
        videoMoment.setType(type);
        videoMoment.setLikes(0);
        videoMoment.setComments(0);
        videoMoment.setVideoPath(video);

        mongoTemplate.insert(videoMoment);
    }
}
