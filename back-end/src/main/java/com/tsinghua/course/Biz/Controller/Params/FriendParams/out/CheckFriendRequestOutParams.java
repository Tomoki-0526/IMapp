package com.tsinghua.course.Biz.Controller.Params.FriendParams.out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 审核好友申请的定向推送
 */
@BizType(BizTypeEnum.FRIEND_CHECK_FRIEND_REQUEST)
public class CheckFriendRequestOutParams extends CommonOutParams {
    // 消息类型
    private int type;
    // 接收方的用户名
    private String to_username;
    // 附加信息
    private String extra;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getToUsername() { return to_username; }
    public void setToUsername(String to_username) { this.to_username = to_username; }

    public String getExtra() { return extra; }
    public void setExtra(String extra) { this.extra = extra; }
}
