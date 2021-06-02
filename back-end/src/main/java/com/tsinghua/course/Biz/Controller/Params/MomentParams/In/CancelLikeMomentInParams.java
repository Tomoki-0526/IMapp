package com.tsinghua.course.Biz.Controller.Params.MomentParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 撤销点赞的入参
 */
@BizType(BizTypeEnum.MOMENT_CANCEL_LIKE_MOMENT)
public class CancelLikeMomentInParams extends CommonInParams {
    // 点赞id
    @Required
    private String likeId;
    // 动态id
    @Required
    private String momentId;

    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    public String getMomentId() {
        return momentId;
    }

    public void setMomentId(String momentId) {
        this.momentId = momentId;
    }
}
