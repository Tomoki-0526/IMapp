package com.tsinghua.course.Biz.Controller.Params.MomentParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 点赞动态的入参
 */
@BizType(BizTypeEnum.MOMENT_LIKE_MOMENT)
public class LikeMomentInParams extends CommonInParams {
    // 动态id
    @Required
    private String momentId;
    // 发布动态的用户名
    @Required
    private String momentUsername;

    public String getMomentId() {
        return momentId;
    }

    public void setMomentId(String momentId) {
        this.momentId = momentId;
    }

    public String getMomentUsername() {
        return momentUsername;
    }

    public void setMomentUsername(String momentUsername) {
        this.momentUsername = momentUsername;
    }
}
