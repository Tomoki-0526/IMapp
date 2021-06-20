package com.tsinghua.course.Biz.Controller.Params.ChatParams.Out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.CustomizedClass.MsgItem;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 进入群聊出参
 */
@BizType(BizTypeEnum.CHAT_ENTER_GROUP_CHAT)
public class EnterGroupChatOutParams extends CommonOutParams {
    // 群聊名称
    String groupName;
    // 群头像
    String avatar;
    // 消息记录
    MsgItem[] msgs;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public MsgItem[] getMsgs() {
        return msgs;
    }

    public void setMsgs(MsgItem[] msgs) {
        this.msgs = msgs;
    }
}
