package com.tsinghua.course.Biz.Controller.Params.ChatParams.Out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.CustomizedClass.ChatItem;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 获取所有聊天的出参
 */
@BizType(BizTypeEnum.CHAT_GET_CHATTINGS)
public class GetChattingsOutParams extends CommonOutParams {
    // 聊天项目列表
    ChatItem[] chattings;

    public ChatItem[] getChattings() {
        return chattings;
    }

    public void setChattings(ChatItem[] chattings) {
        this.chattings = chattings;
    }
}
