package com.tsinghua.course.Biz.Controller.Params.ChatParams.Out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 获取聊天关系出参
 */
@BizType(BizTypeEnum.CHAT_GET_CHAT_LINK)
public class GetChatLinkOutParams extends CommonOutParams {
    // 聊天id
    String linkId;
    // 对方用户名
    String username;
    // 对方昵称
    String nickname;
    // 对方备注
    String remark;
    // 对方头像
    String avatar;

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
