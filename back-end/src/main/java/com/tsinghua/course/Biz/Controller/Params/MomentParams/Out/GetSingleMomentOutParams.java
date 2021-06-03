package com.tsinghua.course.Biz.Controller.Params.MomentParams.Out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.CustomizedClass.MomentItem;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 查看一条动态的出参
 */
@BizType(BizTypeEnum.MOMENT_GET_SINGLE_MOMENT)
public class GetSingleMomentOutParams extends CommonOutParams {
    // 一条动态
    private MomentItem moment;

    public MomentItem getMoment() {
        return moment;
    }

    public void setMoment(MomentItem moment) {
        this.moment = moment;
    }
}
