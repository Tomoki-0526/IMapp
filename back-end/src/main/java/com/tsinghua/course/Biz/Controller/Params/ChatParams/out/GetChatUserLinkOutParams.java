package com.tsinghua.course.Biz.Controller.Params.ChatParams.out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 获取聊天关系出参
 */
@BizType(BizTypeEnum.CHAT_GET_CHAT_USER_LINK)
public class GetChatUserLinkOutParams extends CommonOutParams {
    // 聊天关系id
    String link_id;

    public String getLinkId() { return link_id; }
    public void setLinkId(String link_id) { this.link_id = link_id; }
}
