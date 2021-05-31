package com.tsinghua.course.Base.CustomizedClass;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 返回前端的一条动态
 */
@Document("MomentItem")
public class MomentItem {
    // 动态id
    String momentId;
    // 发布者头像
    String avatar;
    // 发布者用户名
    String username;
    // 发布者昵称
    String nickname;
    // 发布者备注
    String remark;
    // 发布时间
    String publishTime;
    // 动态类型
    int type;
    // 文字内容
    String textContent;
    // 图片数组
    String[] images;
    // 视频
    String video;
    // 点赞数
    int likesNum;
    // 点赞用户数组
    LikeItem[] likes;
    // 评论数
    int commentsNum;
    // 评论用户数组
    CommentItem[] comments;

    public String getMomentId() {
        return momentId;
    }

    public void setMomentId(String momentId) {
        this.momentId = momentId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getLikesNum() {
        return likesNum;
    }

    public void setLikesNum(int likesNum) {
        this.likesNum = likesNum;
    }

    public LikeItem[] getLikes() {
        return likes;
    }

    public void setLikes(LikeItem[] likes) {
        this.likes = likes;
    }

    public int getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(int commentsNum) {
        this.commentsNum = commentsNum;
    }

    public CommentItem[] getComments() {
        return comments;
    }

    public void setComments(CommentItem[] comments) {
        this.comments = comments;
    }
}
