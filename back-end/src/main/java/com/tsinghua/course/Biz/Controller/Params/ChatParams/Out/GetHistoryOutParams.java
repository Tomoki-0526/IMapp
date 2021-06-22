package com.tsinghua.course.Biz.Controller.Params.ChatParams.Out;

import com.tsinghua.course.Base.CustomizedClass.MsgItem;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 查看历史纪录出参
 */
public class GetHistoryOutParams extends CommonOutParams {
    // 消息列表
    MsgItem[] msgs;

    public MsgItem[] getMsgs() {
        return msgs;
    }

    public void setMsgs(MsgItem[] msgs) {
        this.msgs = msgs;
    }
}
