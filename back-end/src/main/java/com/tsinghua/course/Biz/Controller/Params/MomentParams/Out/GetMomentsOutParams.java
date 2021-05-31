package com.tsinghua.course.Biz.Controller.Params.MomentParams.Out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.CustomizedClass.MomentItem;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 查看动态的出参
 */
@BizType(BizTypeEnum.MOMENT_GET_MOMENTS)
public class GetMomentsOutParams extends CommonOutParams {
    // 动态数组
    private MomentItem[] moments;

    public MomentItem[] getMoments() {
        return moments;
    }

    public void setMoments(MomentItem[] moments) {
        this.moments = moments;
    }
}
