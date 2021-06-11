package com.example.course29.moment.moment;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class Moment implements MultiItemEntity {
    private int itemType;
    private String momentId; // 昵称
    private String avatar; // 头像
    private String username; // 用户名
    private String nickname; // 昵称
    private String remark; // 备注
    private String publishTime; //发布时间
    private String type; //类型
    private String textContent; //文本
    private List<String> images; //图片
    private String video; //视频
    private String likesNum; //喜欢数
    private List<Like> likes; //喜欢列表
    private String commentsNum; //评论数
    private List<Comment> comments; //评论列表

    public Moment(String avatar, String username, String momentId, String nickname, String remark,
                  String publishTime, String type, String textContent, List<String> images,
                  String video, String likesNum, List<Like> likes, String commentsNum,
                  List<Comment> comments, int itemType) {
        this.momentId = momentId;
        this.avatar = avatar;
        this.remark = remark;
        this.nickname = nickname;
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
        this.itemType = itemType;
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

    public String getNickname() { return nickname; }

    public String getRemark() {
        return remark;
    }

    public String getPublishTime() { return publishTime; }

    public String getType() { return type; }

    public String getTextContent() { return textContent; }

    public List<String> getImages() { return images; }

    public String getVideo() { return video; }

    public String getLikesNum() { return likesNum; }

    public String getCommentsNum() { return commentsNum; };

    public List<Like> getLikes() { return likes; }

    public List<Comment> getComments() { return comments; };

    @Override
    public int getItemType() {
        return itemType;
    }
}
