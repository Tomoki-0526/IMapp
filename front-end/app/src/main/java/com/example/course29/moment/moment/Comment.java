package com.example.course29.moment.moment;

public class Comment {
    private String commentUsername; //用户名
    private String commentNickname; //昵称
    private String commentRemark; //备注
    private String commentContent; //评论内容
    private String commentTime; //评论时间
    private String isFriend; //是否朋友

    public Comment(String commentUsername, String commentNickname, String commentRemark,
                   String commentContent, String commentTime, String isFriend) {
        this.commentUsername = commentUsername;
        this.commentNickname = commentNickname;
        this.commentRemark = commentRemark;
        this.commentTime = commentTime;
        this.commentContent = commentContent;
        this.isFriend = isFriend;
    }

    public String getCommentUsername() {
        return commentUsername;
    }

    public String getCommentNickname() {
        return commentNickname;
    }

    public String getCommentRemark() {
        return commentRemark;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public String getCommentTime() { return commentTime; }

    public String getIsFriend() { return isFriend; }
}
