package com.tsinghua.course.Biz.Controller.Params.FriendParams.out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.CustomizedClass.FriendsByGroup;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 获取通讯录的出参
 */
@BizType(BizTypeEnum.FRIEND_GET_FRIENDS)
public class GetFriendsOutParams extends CommonOutParams {
    // 所有好友
    FriendsByGroup[] friends;

    public FriendsByGroup[] getFriends() { return friends; }
    public void setFriends(FriendsByGroup[] friends) { this.friends = friends; }
}
