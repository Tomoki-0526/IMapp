package com.tsinghua.course.Biz.Controller.Params.FriendParams.out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 定向发送给接收方的好友申请
 */
@BizType(BizTypeEnum.FRIEND_NEW_FRIEND_REQUEST)
public class NewFriendRequestWsOutParams extends CommonOutParams {
    // 消息类型
    private int flag;
    // 发送者用户名
    private String from_username;
    // 发送者昵称
    private String from_nickname;
    // 发送者头像
    private String from_avatar;
    // 申请理由
    private String extra;
    // 状态
    private int status;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getFromUsername() { return from_username; }
    public void setFromUsername(String from_username) { this.from_username = from_username; }

    public String getFromNickname() { return from_nickname; }
    public void setFromNickname(String from_nickname) { this.from_nickname = from_nickname; }

    public String getFromAvatar() { return from_avatar; }
    public void setFromAvatar(String from_avatar) { this.from_avatar = from_avatar; }

    public String getExtra() { return extra; }
    public void setExtra(String extra) { this.extra = extra; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}
