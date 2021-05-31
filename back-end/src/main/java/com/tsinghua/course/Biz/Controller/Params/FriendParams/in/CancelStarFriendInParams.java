package com.tsinghua.course.Biz.Controller.Params.FriendParams.in;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 取消星标好友入参
 */
@BizType(BizTypeEnum.FRIEND_CANCEL_STAR_FRIEND)
public class CancelStarFriendInParams extends CommonInParams {
    // 好友用户名
    @Required
    private String friend_username;

    public String getFriendUsername() { return friend_username; }
    public void setFriendUsername(String friend_username) { this.friend_username = friend_username; }
}
