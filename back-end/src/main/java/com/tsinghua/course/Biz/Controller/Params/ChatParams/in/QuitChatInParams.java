package com.tsinghua.course.Biz.Controller.Params.ChatParams.in;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 退出聊天界面的入参
 */
@BizType(BizTypeEnum.CHAT_QUIT_CHAT)
public class QuitChatInParams extends CommonInParams {
    // 聊天关系id
    @Required
    String link_id;

    public String getLinkId() { return link_id; }
    public void setLinkId(String link_id) { this.link_id = link_id; }
}
