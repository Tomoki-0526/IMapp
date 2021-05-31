package com.tsinghua.course.Biz.Controller.Params.ChatParams.in;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 删除历史记录的入参
 */
@BizType(BizTypeEnum.CHAT_REMOVE_HISTORY)
public class RemoveHistoryInParams extends CommonInParams {
    // 聊天关系id
    @Required
    private String link_id;
    // 聊天记录id数组
    @Required
    private String[] msgs;

    public String getLinkId() { return link_id; }
    public void setLinkId(String link_id) { this.link_id = link_id; }

    public String[] getMsgs() { return msgs; }
    public void setMsgs(String[] msgs) { this.msgs = msgs;}
}
