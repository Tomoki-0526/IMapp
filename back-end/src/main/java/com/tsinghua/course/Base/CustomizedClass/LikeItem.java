package com.tsinghua.course.Base.CustomizedClass;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 一个点赞用户的格式
 */
@Document("LikeItem")
public class LikeItem {
    // 点赞id
    String likeId;
    // 头像
    String likeAvatar;
    // 用户名
    String likeUsername;
    // 昵称
    String likeNickname;
    // 备注
    String likeRemark;
    // 点赞时间
    String likeTime;

    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    public String getLikeAvatar() {
        return likeAvatar;
    }

    public void setLikeAvatar(String likeAvatar) {
        this.likeAvatar = likeAvatar;
    }

    public String getLikeUsername() {
        return likeUsername;
    }

    public void setLikeUsername(String likeUsername) {
        this.likeUsername = likeUsername;
    }

    public String getLikeNickname() {
        return likeNickname;
    }

    public void setLikeNickname(String likeNickname) {
        this.likeNickname = likeNickname;
    }

    public String getLikeRemark() {
        return likeRemark;
    }

    public void setLikeRemark(String likeRemark) {
        this.likeRemark = likeRemark;
    }

    public String getLikeTime() {
        return likeTime;
    }

    public void setLikeTime(String likeTime) {
        this.likeTime = likeTime;
    }
}
