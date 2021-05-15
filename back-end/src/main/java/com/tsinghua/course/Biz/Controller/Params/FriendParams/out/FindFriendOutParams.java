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
    FriendItem[] friends;
    // 附加信息
    String extra_info;

    public FindFriendOutParams() { this.success = true; }
    public FindFriendOutParams(boolean success) { this.success = success; }

    public FriendItem[] getFriends() { return friends; }
    public void setFriends(FriendItem[] friends) { this.friends = friends; }

    public String getExtraInfo() { return extra_info; }
    public void setExtraInfo(String extra_info) { this.extra_info = extra_info; }
}