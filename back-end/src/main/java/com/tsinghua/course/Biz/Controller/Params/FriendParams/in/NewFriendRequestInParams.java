package com.tsinghua.course.Biz.Controller.Params.FriendParams.in;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 好友申请的入参
 **/
@BizType(BizTypeEnum.FRIEND_NEW_FRIEND_REQUEST)
public class NewFriendRequestInParams extends CommonInParams {
    // 对方的用户名
    @Required
    private String to_username;

    // 附加信息
    @Required
    private String extra;

    public String getToUsername() { return to_username; }
    public void setToUsername(String to_username) { this.to_username = to_username; }

    public String getExtra() { return extra; }
    public void setExtra(String extra) { this.extra = extra; }
}
