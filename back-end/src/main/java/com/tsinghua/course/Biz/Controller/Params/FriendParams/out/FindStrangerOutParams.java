package com.tsinghua.course.Biz.Controller.Params.FriendParams.out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 查找新联系人的出参
 **/
@BizType(BizTypeEnum.FRIEND_FIND_STRANGER)
public class FindStrangerOutParams extends CommonOutParams {
    // 用户名
    String stranger_username;
    // 昵称
    String stranger_nickname;
    // 头像
    String stranger_avatar;

    public String getStrangerUsername() { return stranger_username; }
    public void setStrangerUsername(String stranger_username) { this.stranger_username = stranger_username; }

    public String getStrangerNickname() { return stranger_nickname; }
    public void setStrangerNickname(String stranger_nickname) { this.stranger_nickname = stranger_nickname; }

    public String getStrangerAvatar() { return stranger_avatar; }
    public void setStrangerAvatar(String avatar) { this.stranger_avatar = stranger_avatar; }
}
