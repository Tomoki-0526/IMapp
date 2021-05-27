package com.tsinghua.course.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

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
}
