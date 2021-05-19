package com.tsinghua.course.Biz.Controller.Params.FriendParams.in;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 设置好友备注的入参
 */
@BizType(BizTypeEnum.FRIEND_SET_FRIEND_REMARK)
public class SetFriendRemarkInParams extends CommonInParams {
    // 好友用户名
    @Required
    private String friend_username;
    // 备注
    @Required
    private String remark;

    public String getFriendUsername() { return friend_username; }
    public void setFriendUsername(String friend_username) { this.friend_username = friend_username; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
