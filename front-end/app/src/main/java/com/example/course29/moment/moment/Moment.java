package com.example.course29.moment.moment;

import java.util.List;

public class Moment {
    private String momentId; // 昵称
    private String avatar; // 头像
    private String username; // 用户名
    private String remark; // 备注
    private String publishTime; //发布时间
    private String type; //类型
    private String textContent; //文本
    private String[] images; //图片
    private String video; //视频
    private String likesNum; //喜欢数
    private List<Like> likes; //喜欢列表
    private String commentsNum; //评论数
    private List<Comment> comments; //评论列表

    public Moment(String avatar, String username, String momentId, String remark,
                  String publishTime, String type, String textContent, String[] images,
                  String video, String likesNum, List<Like> likes, String commentsNum,
                  List<Comment> comments) {
        this.momentId = momentId;
        this.avatar = avatar;
        this.remark = remark;
        this.username = username;
        this.publishTime = publishTime;
        this.type = type;
        this.textContent = textContent;
        this.images = images;
        this.video = video;
        this.likesNum = likesNum;
        this.likes = likes;
        this.commentsNum = commentsNum;
        this.comments = comments;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getMomentId() {
        return momentId;
    }

    public String getUsername() {
        return username;
    }

    public String getRemark() {
        return remark;
    }

    public String getPublishTime() { return publishTime; }

    public String getType() { return type; }

    public String getTextContent() { return textContent; }

    public String getVideo() { return video; }

    public String getLikesNum() { return likesNum; }

    public String getCommentsNum() { return commentsNum; };

    public List<Like> getLikes() { return likes; }

    public List<Comment> getComments() { return comments; };
}
