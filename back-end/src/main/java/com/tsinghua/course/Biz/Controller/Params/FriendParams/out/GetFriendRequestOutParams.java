package com.tsinghua.course.Biz.Controller.Params.FriendParams.out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.CustomizedClass.FriendRequestItem;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 查看好友申请的出参
 **/
@BizType(BizTypeEnum.FRIEND_GET_FRIEND_REQUEST)
public class GetFriendRequestOutParams extends CommonOutParams {
    // 好友申请列表
    private FriendRequestItem[] friend_request;

    public FriendRequestItem[] getFriendRequest() { return friend_request; }
    public void setFriendRequest(FriendRequestItem[] friend_request) { this.friend_request = friend_request; }
}
