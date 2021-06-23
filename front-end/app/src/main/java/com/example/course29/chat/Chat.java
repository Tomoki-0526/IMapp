package com.example.course29.chat;

public class Chat {
    private String avatar;
    private String linkId;
    private String multiple;
    private String name;
    private String latestMsg;
    private String sendTime;
    private String type;
    private String username;
    private String unread;

    public Chat(String avatar,String linkId,String multiple,
                String name,String latestMsg,String sendTime,
                String type,String username,String unread) {
        this.avatar = avatar;
        this.latestMsg = latestMsg;
        this.multiple = multiple;
        this.linkId = linkId;
        this.sendTime = sendTime;
        this.name = name;
        this.type = type;
        this.username = username;
        this.unread = unread;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getLinkId() {
        return linkId;
    }

    public String getName() {
        return name;
    }

    public String getLatestMsg() {
        return latestMsg;
    }

    public String getSendTime() {
        return sendTime;
    }

    public String getMultiple() {
        return multiple;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getUnread() {
        return unread;
    }
}
