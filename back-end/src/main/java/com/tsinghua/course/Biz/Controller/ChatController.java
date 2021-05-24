package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Base.CustomizedClass.HistoryItem;
import com.tsinghua.course.Base.Model.ChatItem;
import com.tsinghua.course.Base.Model.ChatMessage;
import com.tsinghua.course.Base.Model.ChatUserLink;
import com.tsinghua.course.Base.Model.User;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.in.*;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.out.GetChatUserLinkOutParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.out.GetHistoryOutParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.out.SendMessageOutParams;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;
import com.tsinghua.course.Biz.Processor.ChatProcessor;
import com.tsinghua.course.Biz.Processor.UserProcessor;
import com.tsinghua.course.Frame.Util.SocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tsinghua.course.Base.Constant.GlobalConstant.*;

/**
 * @描述 聊天控制器，用于执行即时通讯相关的业务
 **/
@Component
public class ChatController {

    @Autowired
    ChatProcessor chatProcessor;
    @Autowired
    UserProcessor userProcessor;

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
            chatProcessor.updateChatItem(chatUserLink.getId(), from_username, true);
        }

        GetChatUserLinkOutParams outParams = new GetChatUserLinkOutParams();
        outParams.setLinkId(chatUserLink.getId());
        return outParams;
    }

    /** 退出聊天 */
    @BizType(BizTypeEnum.CHAT_QUIT_CHAT)
    @NeedLogin
    public CommonOutParams chatQuitChat(QuitChatInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String link_id = inParams.getLinkId();

        chatProcessor.updateChatItem(link_id, username, false);
        return new CommonOutParams(true);
    }

    /** 发送消息给指定用户 */
    @BizType(BizTypeEnum.CHAT_SEND_MESSAGE)
    @NeedLogin
    public CommonOutParams chatSendMessage(SendMessageInParams inParams) throws Exception {
        String link_id = inParams.getLinkId();
        String from_username = inParams.getUsername();
        String to_username = inParams.getToUsername();
        String content = inParams.getContent();
        String send_time_str = inParams.getSendTime();
        int type = inParams.getType();

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
        Date send_time = dateFormat.parse(send_time_str);

        /* 把消息插入数据库 */
        // 把上一条消息的最新状态置否
        chatProcessor.updateLastMsgLatest(link_id);
        // 插入新消息
        chatProcessor.addChatMessage(link_id, from_username, to_username, content, send_time, type);

        /* 定向给接收方发送推送 */
        // 获取对面是否在聊天窗口
        // 如果不在，未读数加1
        ChatItem chatItem = chatProcessor.getChatItem(link_id, to_username);
        boolean to_window = chatItem.getToWindow();
        int unread = chatItem.getUnread();
        if (!to_window) {
            chatItem.setUnread(unread + 1);
        }
        // 组织推送
        SendMessageOutParams outParams = new SendMessageOutParams();
        outParams.setContent(content);
        outParams.setType(type);
        outParams.setWindow(to_window);
        outParams.setUnread(unread);
        SocketUtil.sendMessageToUser(to_username, outParams);

        return new CommonOutParams(true);
    }

    /** 查看历史记录 */
    @BizType(BizTypeEnum.CHAT_GET_HISTORY)
    @NeedLogin
    public GetHistoryOutParams chatGetHistory(GetHistoryInParams inParams) throws Exception {
        String link_id = inParams.getLinkId();
        List<ChatMessage> chatMessageList = chatProcessor.getAllLinkMessages(link_id);

        List<HistoryItem> historyItemList = new ArrayList<>();
        for (ChatMessage chatMessage: chatMessageList) {
            String msg_id = chatMessage.getId();
            String from_username = chatMessage.getFromUsername();
            String content = chatMessage.getContent();
            int type = chatMessage.getType();
            Date send_time = chatMessage.getSendTime();

            User user = userProcessor.getUserByUsername(from_username);
            String avatar = user.getAvatar();
            int index = avatar.indexOf(RELATIVE_PATH);
            String avatar_url = "http://" + SERVER_IP + ":" + FILE_PORT + avatar.substring(index);

            HistoryItem historyItem = new HistoryItem();
            historyItem.setMsgId(msg_id);
            historyItem.setFromUsername(from_username);
            historyItem.setFromAvatar(avatar_url);
            historyItem.setContent(content);
            historyItem.setType(type);

            SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
            String send_time_str = dateFormat.format(send_time);
            historyItem.setSendTime(send_time_str);
            historyItemList.add(historyItem);
        }
        HistoryItem[] historyItems = new HistoryItem[historyItemList.size()];
        historyItemList.toArray(historyItems);

        GetHistoryOutParams outParams = new GetHistoryOutParams();
        outParams.setHistory(historyItems);
        return outParams;
    }

    /** 删除历史记录 */
    @BizType(BizTypeEnum.CHAT_REMOVE_HISTORY)
    @NeedLogin
    public CommonOutParams chatRemoveHistory(RemoveHistoryInParams inParams) throws Exception {
        String link_id = inParams.getLinkId();
        String[] msgs = inParams.getMsgs();

        chatProcessor.removeMessages(link_id, msgs);
        return new CommonOutParams(true);
    }
}
