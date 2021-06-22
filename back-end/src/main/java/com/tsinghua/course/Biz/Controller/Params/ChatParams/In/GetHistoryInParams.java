package com.tsinghua.course.Biz.Controller.Params.ChatParams.In;

import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 查看历史纪录的入参
 */
public class GetHistoryInParams extends CommonInParams {
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
