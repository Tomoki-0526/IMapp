package com.tsinghua.course.Biz.Controller.Params.ChatParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 删除消息记录入参
 */
@BizType(BizTypeEnum.CHAT_REMOVE_MESSAGES)
public class RemoveMessagesInParams extends CommonInParams {
    // 消息id数组
    @Required
    String[] msgs;

    public String[] getMsgs() {
        return msgs;
    }

    public void setMsgs(String[] msgs) {
        this.msgs = msgs;
    }
}
