package com.tsinghua.course.Base.CustomizedClass;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 消息项
 */
@Document("MsgItem")
public class MsgItem {
    // 消息id
    String msgId;
    // 发送时间
    String sendTime;
    // 发送人
    String username;
    // 发送人昵称
    String nickname;
    // 发送人备注
    String remark;
    // 发送人头像
    String avatar;
    // 是不是由自己发出的
    boolean fromMyself;
    // 消息类型
    int type;
    // 文本内容
    String text;
    // 图片
    String image;
    // 音频
    String audio;
    // 视频
    String video;
    // 经度
    double longitude;
    // 纬度
    double latitude;
    // 定位信息
    String locationInfo;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
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

    public void setFromMyself(boolean fromMyself) {
        this.fromMyself = fromMyself;
    }

    public boolean isFromMyself() {
        return fromMyself;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }
}
