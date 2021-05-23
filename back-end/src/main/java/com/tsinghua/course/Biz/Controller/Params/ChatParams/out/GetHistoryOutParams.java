package com.tsinghua.course.Biz.Controller.Params.ChatParams.out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.CustomizedClass.HistoryItem;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 查看历史纪录的出参
 */
@BizType(BizTypeEnum.CHAT_GET_HISTORY)
public class GetHistoryOutParams extends CommonOutParams {
    // 历史记录列表
    HistoryItem[] history;

    public HistoryItem[] getHistory() { return history; }
    public void setHistory(HistoryItem[] history) { this.history = history; }
}
