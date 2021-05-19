package com.tsinghua.course.Biz.Controller.Params.FriendParams.out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.CustomizedClass.FriendItem;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 搜索好友的出参
 **/
@BizType(BizTypeEnum.FRIEND_FIND_FRIEND)
public class FindFriendOutParams extends CommonOutParams {
    // 好友列表
    private FriendItem[] friends;

    public FriendItem[] getFriends() { return friends; }
    public void setFriends(FriendItem[] friends) { this.friends = friends; }
}