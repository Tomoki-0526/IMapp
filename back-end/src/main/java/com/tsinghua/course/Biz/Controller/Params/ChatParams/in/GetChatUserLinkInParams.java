package com.tsinghua.course.Biz.Controller.Params.ChatParams.in;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 获取聊天关系入参
 */
@BizType(BizTypeEnum.CHAT_GET_CHAT_USER_LINK)
public class GetChatUserLinkInParams extends CommonInParams {
    // 接收方用户名
    String to_username;

    public String getToUsername() { return to_username; }
    public void setToUsername(String to_username) { this.to_username = to_username; }
}
