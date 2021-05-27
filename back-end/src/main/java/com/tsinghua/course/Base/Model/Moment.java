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
    int likes;
    // 评论数
    int comments;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public Date getPublishTime() { return publishTime; }
    public void setPublishTime(Date publishTime) { this.publishTime = publishTime; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public int getComments() { return comments; }
    public void setComments(int comments) { this.comments = comments; }
}
