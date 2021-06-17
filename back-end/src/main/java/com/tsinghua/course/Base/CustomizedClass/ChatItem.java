package com.tsinghua.course.Base.CustomizedClass;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 一个聊天项
 */
@Document("ChatItem")
public class ChatItem {
    // 头像
    String avatar;
    // 聊天id
    String linkId;
    // 是否是群聊
    boolean isMultiple;
    // 聊天名称（私聊优先显示备注，其次昵称，群聊显示群名称）
    String name;
    // 最新消息类型
    int type;
    // 最新消息
    String latestMsg;
    // 发送时间
    String sendTime;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public void setMultiple(boolean isMultiple) {
        this.isMultiple = isMultiple;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLatestMsg() {
        return latestMsg;
    }

    public void setLatestMsg(String latestMsg) {
        this.latestMsg = latestMsg;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
