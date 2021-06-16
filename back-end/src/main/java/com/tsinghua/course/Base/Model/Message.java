package com.tsinghua.course.Base.Model;

import com.tsinghua.course.Base.CustomizedClass.Location;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @描述 消息表
 */
@Document("Message")
public class Message {
    // mongodb唯一id
    String id;
    // 聊天关系id
    String linkId;
    // 是否是群聊
    boolean isMultiple;
    // 消息发送方
    String username;
    // 消息类型（0-文本 1-图片 2-音频 3-视频 4-定位）
    int type;
    // 发送时间
    Date sendTime;
    // 文本内容
    String text;
    // 图片路径
    String image;
    // 音频路径
    String audio;
    // 视频路径
    String video;
    // 定位信息
    Location location;
    // 是否是最后一条消息
    boolean isLatest;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getType() {
        return type;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isLatest() {
        return isLatest;
    }

    public void setLatest(boolean latest) {
        isLatest = latest;
    }
}
