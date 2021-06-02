package com.tsinghua.course.Biz.Controller.Params.MomentParams.Out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 评论动态定向推送出参
 */
@BizType(BizTypeEnum.MOMENT_COMMENT_ON_MOMENT)
public class CommentOnMomentOutParams extends CommonOutParams {
    // 消息类型
    private int type;
    // 评论用户名
    private String username;
    // 评论用户昵称
    private String nickname;
    // 评论用户备注
    private String remark;
    // 评论内容
    private String content;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
