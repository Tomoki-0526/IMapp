package com.tsinghua.course.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 FriendGroup 好友分组
 **/
@Document("FriendGroup")
public class FriendGroup {
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
    // 分组名称
    String group_name;
    // 所属用户（外键）
    User user;
    // 测试对象
    SubObj subObj;

    public String getGroupName() { return group_name; }
    public void setGroupName(String group_name) { this.group_name = group_name; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public SubObj getSubObj() { return subObj; }
    public void setSubObj(SubObj subObj) { this.subObj = subObj; }
}
