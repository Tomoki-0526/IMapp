package com.tsinghua.course.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @描述 点赞表
 */
@Document("Like")
public class Like {
    // mongodb唯一id
    String id;
    // 动态id
    String momentId;
    // 点赞人
    String username;
    // 点赞时间
    Date likeTime;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getMomentId() {
        return momentId;
    }
    public void setMomentId(String momentId) {
        this.momentId = momentId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLikeTime() { return likeTime; }
    public void setLikeTime(Date likeTime) { this.likeTime = likeTime; }
}
