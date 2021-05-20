package com.tsinghua.course.Biz.Controller.Params.FriendParams.in;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 添加分组的入参
 **/
@BizType(BizTypeEnum.FRIEND_ADD_GROUP)
public class AddGroupInParams extends CommonInParams {
    // 分组名称
    @Required
    String group_name;

    public String getGroupName() { return group_name; }
    public void setGroupName(String group_name) { this.group_name = group_name; }
}
