package com.example.course29.contact.group;

public class Group {
    private String groupId;
    private String groupAvatar;
    private String groupName;

    public Group(String groupId, String groupAvatar, String groupName) {
        this.groupId = groupId;
        this.groupAvatar = groupAvatar;
        this.groupName = groupName;
    }

    public String getGroupAvatar() {
        return groupAvatar;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }
}

