package com.tsinghua.course.Biz.Controller.Params.MomentParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 查看一条动态的入参
 */
@BizType(BizTypeEnum.MOMENT_GET_SINGLE_MOMENT)
public class GetSingleMomentInParams extends CommonInParams {
    // 动态id
    @Required
    private String momentId;

    public String getMomentId() {
        return momentId;
    }

    public void setMomentId(String momentId) {
        this.momentId = momentId;
    }
}
