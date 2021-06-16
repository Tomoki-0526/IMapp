package com.tsinghua.course.Biz.Controller.Params.ChatParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 退出聊天入参
 */
@BizType(BizTypeEnum.CHAT_QUIT_CHAT)
public class QuitChatInParams extends CommonInParams {
    // 聊天id
    @Required
    String linkId;

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }
}
