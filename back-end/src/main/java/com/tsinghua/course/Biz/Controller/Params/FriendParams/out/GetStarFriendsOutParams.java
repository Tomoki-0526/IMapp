package com.tsinghua.course.Biz.Controller.Params.FriendParams.out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.CustomizedClass.FriendItem;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 查看星标朋友的出参
 **/
@BizType(BizTypeEnum.FRIEND_GET_STAR_FRIENDS)
public class GetStarFriendsOutParams extends CommonOutParams {
    // 星标好友列表
    private FriendItem[] star_friends;

    public FriendItem[] getStarFriendsList() { return star_friends; }
    public void setStarFriendsList(FriendItem[] star_friends) { this.star_friends = star_friends; }
}
