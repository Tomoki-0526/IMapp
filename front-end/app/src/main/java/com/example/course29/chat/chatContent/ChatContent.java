package com.example.course29.chat.chatContent;

public class ChatContent {
    private String username;
    private String nickname;
    private String remark;
    private String avatar;
    private String msgId;
    private String sendTime;
    private String fromMyself;
    private String type;
    private String text;
    private String image;
    private String audio;
    private String video;
    private String longitude;
    private String latitude;

    public ChatContent(String username, String nickname, String remark,
                       String avatar, String msgId, String sendTime,
                       String fromMyself, String type, String text,
                       String image, String audio, String video, String longitude,
                       String latitude) {
        this.username = username;
        this.nickname = nickname;
        this.remark = remark;
        this.avatar = avatar;
        this.msgId = msgId;
        this.sendTime = sendTime;
        this.fromMyself = fromMyself;
        this.type = type;
        this.text = text;
        this.image = image;
        this.audio = audio;
        this.video = video;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public String getAvatar() {
        return avatar;
    }

    public String getMsgId() {
        return msgId;
    }

    public String getSendTime() {
        return sendTime;
    }

    public String getFromMyself() {
        return fromMyself;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }

    public String getAudio() {
        return audio;
    }

    public String getVideo() {
        return video;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
