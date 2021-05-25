package com.tsinghua.course.Biz.Controller.Params.FriendParams.out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 审核好友申请的定向推送
 */
@BizType(BizTypeEnum.FRIEND_CHECK_FRIEND_REQUEST)
public class CheckFriendRequestOutParams extends CommonOutParams {
    // 接收方的用户名
    String from_username;

    // 附加信息
    String extra;

    public String getFromUsername() { return from_username; }
    public void setFromUsername(String from_username) { this.from_username = from_username; }

    public String getExtra() { return extra; }
    public void setExtra(String extra) { this.extra = extra; }
}
