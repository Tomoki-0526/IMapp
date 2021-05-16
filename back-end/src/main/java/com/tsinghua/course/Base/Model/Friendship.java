package com.tsinghua.course.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 Friendship 好友关系
 **/
@Document("Friendship")
public class Friendship {
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
    // 本用户名（外键）
    String username;
    // 好友用户名（外键）
    String friend_username;
    // 备注
    String remark;
    // 星标
    boolean star;
    // 分组
    String group;
    // 测试对象
    SubObj subObj;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFriendUsername() { return friend_username; }
    public void setFriendUsername(String friend_username) { this.friend_username = friend_username; }

    public String getRemark() { return remark; }
    public void setRemark() { this.remark = remark; }

    public boolean isStar() { return star; }
    public void setStar(boolean star) { this.star = star; }

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public SubObj getSubObj() { return subObj; }
    public void setSubObj(SubObj subObj) { this.subObj = subObj; }
}
