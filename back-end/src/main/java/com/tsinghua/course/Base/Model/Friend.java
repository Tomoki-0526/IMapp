package com.tsinghua.course.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 Friend 好友关系
 **/
@Document("Friend")
public class Friend {
    /** 子对象文档 */
    public static class SubObj {
        /** 存储的时间 */
        String time;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    // mongodb唯一id
    String id;
    // 本用户ID
    String user_id;
    // 好友ID
    String friend_id;
    // 备注
    String remark;
    // 星标
    boolean star;
    // 分组
    FriendGroup group;

    public String getUserID() { return user_id; }
    public void setUserID() { this.user_id = user_id; }

    public String getFriendID() { return friend_id; }
    public void setFriendID(String friend_id) { this.friend_id = friend_id; }

    public String getRemark() { return remark; }
    public void setRemark() { this.remark = remark; }

    public boolean getStar() { return star; }
    public void setStar(boolean star) { this.star = star; }

    public FriendGroup getGroup() { return group; }
    public void setGroup(FriendGroup group) { this.group = group; }
}
