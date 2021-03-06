package com.tsinghua.course.Biz.Controller.Params.FriendParams.in;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 审核好友申请的入参
 **/
@BizType(BizTypeEnum.FRIEND_CHECK_FRIEND_REQUEST)
public class CheckFriendRequestInParams extends CommonInParams {
    // 对方用户名
    @Required
    private String from_username;
    // 结果
    @Required
    private String result;

    public String getFromUsername() { return from_username; }
    public void setFromUsername(String from_username) { this.from_username = from_username; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}
