package com.tsinghua.course.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @描述 评论表
 */
@Document("Comment")
public class Comment {
    // mongodb唯一id
    String id;
    // 动态id（外键）
    String momentId;
    // 评论者（外键）
    String username;
    // 评论内容
    String content;
    // 评论时间
    Date commentTime;

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

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Date getCommentTime() { return commentTime; }
    public void setCommentTime(Date commentTime) { this.commentTime = commentTime; }
}
