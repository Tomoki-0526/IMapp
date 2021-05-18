package com.tsinghua.course.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 FriendRequest 好友申请
 **/
@Document
public class FriendRequest {
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

    // mongo唯一id
    String id;
    // 发起人用户名
    String from_username;
    // 接收人用户名
    String to_username;
    // 状态（0-等待 1-接受 2-拒绝）
    int status;
    // 附加信息
    String extra;
    // 测试对象
    SubObj subObj;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFromUsername() { return from_username; }
    public void setFromUsername(String from_username) { this.from_username = from_username; }

    public String getToUsername() { return to_username; }
    public void setToUsername(String to_username) { this.to_username = to_username; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getExtra() { return extra; }
    public void setExtra(String extra) { this.extra = extra; }

    public SubObj getSubObj() { return subObj; }
    public void setSubObj(SubObj subObj) { this.subObj = subObj; }
}
