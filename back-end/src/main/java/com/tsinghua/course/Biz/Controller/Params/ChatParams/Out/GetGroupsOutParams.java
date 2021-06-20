package com.tsinghua.course.Biz.Controller.Params.ChatParams.Out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.CustomizedClass.GroupItem;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 查看所有群聊的出参
 */
@BizType(BizTypeEnum.CHAT_GET_GROUPS)
public class GetGroupsOutParams extends CommonOutParams {
    // 群聊
    GroupItem[] groups;

    public GroupItem[] getGroups() {
        return groups;
    }

    public void setGroups(GroupItem[] groups) {
        this.groups = groups;
    }
}
