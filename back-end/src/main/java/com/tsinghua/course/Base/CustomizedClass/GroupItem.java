package com.tsinghua.course.Base.CustomizedClass;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 一个群聊项目
 */
@Document("GroupItem")
public class GroupItem {
    // 群id
    String groupId;
    // 群名称
    String groupName;
    // 群头像
    String avatar;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
