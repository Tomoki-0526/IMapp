package com.example.course29.chat.chatContent.member;

public class Member {
    private String avatar;
    private String username;
    private String nickname;
    private String remark;
    private String isFriend;

    public Member(String avatar, String username, String nickname,
                  String remark, String isFriend) {
        this.avatar = avatar;
        this.username = username;
        this.nickname = nickname;
        this.remark = remark;
        this.isFriend = isFriend;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRemark() {
        return remark;
    }

    public String getIsFriend() {
        return isFriend;
    }
}
