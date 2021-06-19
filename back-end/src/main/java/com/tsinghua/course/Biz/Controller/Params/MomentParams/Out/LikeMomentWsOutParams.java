package com.tsinghua.course.Biz.Controller.Params.MomentParams.Out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 点赞定向出参
 */
@BizType(BizTypeEnum.MOMENT_LIKE_MOMENT)
public class LikeMomentWsOutParams extends CommonOutParams {
    // 消息类型
    private int flag;
    // 点赞用户名
    private String username;
    // 点赞用户昵称
    private String nickname;
    // 点赞用户备注
    private String remark;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
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
}
