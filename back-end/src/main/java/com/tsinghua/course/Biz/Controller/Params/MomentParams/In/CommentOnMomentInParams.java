package com.tsinghua.course.Biz.Controller.Params.MomentParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 评论动态的入参
 */
@BizType(BizTypeEnum.MOMENT_COMMENT_ON_MOMENT)
public class CommentOnMomentInParams extends CommonInParams {
    // 动态id
    @Required
    private String momentId;
    // 评论内容
    @Required
    private String content;
    // 发布动态的用户名
    @Required
    private String momentUsername;


    public String getMomentId() {
        return momentId;
    }

    public void setMomentId(String momentId) {
        this.momentId = momentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMomentUsername() {
        return momentUsername;
    }

    public void setMomentUsername(String momentUsername) {
        this.momentUsername = momentUsername;
    }
}
