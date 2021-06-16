package com.tsinghua.course.Biz.Controller.Params.ChatParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 获取聊天关系的入参
 */
@BizType(BizTypeEnum.CHAT_GET_CHAT_LINK)
public class GetChatLinkInParams extends CommonInParams {
    // 对方用户名
    @Required
    String toUsername;

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }
}
