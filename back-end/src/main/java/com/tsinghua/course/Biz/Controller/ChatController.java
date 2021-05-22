package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Base.Model.ChatUserLink;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.in.GetChatUserLinkInParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.out.GetChatUserLinkOutParams;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;
import com.tsinghua.course.Biz.Processor.ChatProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @描述 聊天控制器，用于执行即时通讯相关的业务
 **/
@Component
public class ChatController {

    @Autowired
    ChatProcessor chatProcessor;

    /** 获取聊天关系 */
    @BizType(BizTypeEnum.CHAT_GET_CHAT_USER_LINK)
    @NeedLogin
    public GetChatUserLinkOutParams chatGetChatUserLink(GetChatUserLinkInParams inParams) throws Exception {
        String from_username = inParams.getUsername();
        String to_username = inParams.getToUsername();

        ChatUserLink chatUserLink = chatProcessor.getChatUserLink(from_username, to_username);
        /* 如果当前不存在这个聊天关系 */
        if (chatUserLink == null) {
            /* 创建新的聊天关系 */
            chatUserLink = chatProcessor.createChatUserLink(from_username, to_username);
            /* 创建双方的聊天列表 */
            chatProcessor.createChatItem(chatUserLink.getId(), from_username, to_username);
            chatProcessor.createChatItem(chatUserLink.getId(), to_username, from_username);
        }
        /* 否则，调整聊天条目的在线状态 */
        else {
            chatProcessor.updateChatItemWindow(chatUserLink.getId(), from_username, true);
        }

        GetChatUserLinkOutParams outParams = new GetChatUserLinkOutParams();
        outParams.setLinkId(chatUserLink.getId());
        return outParams;
    }

    /** 退出聊天 */
    
}
