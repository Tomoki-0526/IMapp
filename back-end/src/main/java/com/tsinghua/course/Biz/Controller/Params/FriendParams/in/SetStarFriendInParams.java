package com.tsinghua.course.Biz.Controller.Params.FriendParams.in;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 设置星表好友的入参
 **/
@BizType(BizTypeEnum.FRIEND_SET_STAR_FRIEND)
public class SetStarFriendInParams extends CommonInParams {
    // 好友用户名
    @Required
    String friend_username;

    public String getFriendUsername() { return friend_username; }
    public void setFriendUsername(String friend_username) { this.friend_username = friend_username; }
}
