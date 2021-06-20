package com.tsinghua.course.Biz.Controller.Params.ChatParams.Out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.CustomizedClass.MemberItem;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 查看群成员入参
 */
@BizType(BizTypeEnum.CHAT_GET_GROUP_MEMBERS)
public class GetGroupMembersOutParams extends CommonOutParams {
    // 群成员
    MemberItem[] members;

    public MemberItem[] getMembers() {
        return members;
    }

    public void setMembers(MemberItem[] members) {
        this.members = members;
    }
}
