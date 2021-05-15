package com.tsinghua.course.Biz.Controller.Params.FriendParams.in;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 搜索好友的入参
 **/
@BizType(BizTypeEnum.FRIEND_FIND_FRIEND)
public class FindFriendInParams extends CommonInParams {
    // 好友用户名、昵称或备注
    @Required
    String content;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
