package com.example.course29.moment.moment;

public class Like {
    private String likeId;
    private String likeAvatar;
    private String likeUsername;
    private String likeNickname;
    private String likeRemark;
    private String likeTime;
    private String isFriend;

    public Like(String likeId, String likeAvatar, String likeUsername,String likeNickname,
                String likeRemark,String likeTime, String isFriend) {
        this.likeId = likeId;
        this.likeAvatar = likeAvatar;
        this.likeUsername = likeUsername;
        this.likeNickname = likeNickname;
        this.likeRemark = likeRemark;
        this.likeTime = likeTime;
        this.isFriend = isFriend;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public String getLikeAvatar() {
        return likeAvatar;
    }

    public String getLikeId() {
        return likeId;
    }

    public String getLikeNickname() {
        return likeNickname;
    }


    public String getLikeRemark() {
        return likeRemark;
    }

    public String getLikeTime() {
        return likeTime;
    }

    public String getLikeUsername() {
        return likeUsername;
    }
}
