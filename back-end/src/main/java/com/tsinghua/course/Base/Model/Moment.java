package com.tsinghua.course.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @描述 动态表，基类
 */
@Document("Moment")
public class Moment {
    // mongodb唯一id
    String id;
    // 发布者用户名
    String username;
    // 动态类型（0-文字 1-图片 2-图文 3-视频）
    int type;
    // 发布时间
    Date publishTime;
    // 点赞数
    int likesNum;
    // 评论数
    int commentsNum;
    // 文本内容
    String textContent;
    // 图片路径
    String[] imagesPath;
    // 视频路径
    String videoPath;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public Date getPublishTime() { return publishTime; }
    public void setPublishTime(Date publishTime) { this.publishTime = publishTime; }

    public int getLikesNum() { return likesNum; }
    public void setLikesNum(int likesNum) { this.likesNum = likesNum; }

    public int getCommentsNum() { return commentsNum; }
    public void setCommentsNum(int comments) { this.commentsNum = commentsNum; }

    public String getTextContent() { return textContent; }
    public void setTextContent(String textContent) { this.textContent = textContent; }

    public String[] getImagesPath() { return imagesPath; }
    public void setImagesPath(String[] imagesPath) { this.imagesPath = imagesPath; }

    public String getVideoPath() { return videoPath; }
    public void setVideoPath(String videoPath) { this.videoPath = videoPath; }
}
