package com.tsinghua.course.Biz.Controller.Params.FriendParams.in;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 查找陌生人的入参
 **/
@BizType(BizTypeEnum.FRIEND_FIND_STRANGER)
public class FindStrangerInParams extends CommonInParams {
    // 陌生人用户名
    @Required
    String stranger_username;

    public String getStrangerUsername() { return stranger_username; }
    public void setStrangerUsername(String stranger_username) { this.stranger_username = stranger_username; }
}
