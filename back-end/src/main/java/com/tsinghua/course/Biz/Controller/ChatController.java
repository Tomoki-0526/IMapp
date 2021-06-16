package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Base.CustomizedClass.Location;
import com.tsinghua.course.Base.Error.CourseWarn;
import com.tsinghua.course.Base.Error.UserWarnEnum;
import com.tsinghua.course.Base.Model.*;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.In.GetChatLinkInParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.In.QuitChatInParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.In.SendMessageInParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.Out.GetChatLinkOutParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.Out.GetChattingsOutParams;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.Out.SendMessageWsOutParams;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;
import com.tsinghua.course.Biz.Processor.ChatProcessor;
import com.tsinghua.course.Biz.Processor.FriendProcessor;
import com.tsinghua.course.Biz.Processor.UserProcessor;
import com.tsinghua.course.Frame.Util.FileUtil;
import com.tsinghua.course.Frame.Util.SocketUtil;
import io.netty.handler.codec.http.multipart.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.tsinghua.course.Base.Constant.GlobalConstant.*;
import static com.tsinghua.course.Base.Constant.GlobalConstant.LINUX_MOMENT_PATH;
import static com.tsinghua.course.Base.Constant.NameConstant.OS_NAME;
import static com.tsinghua.course.Base.Constant.NameConstant.WIN;

/**
 * @描述 聊天控制器
 */
@Component
public class ChatController {
    @Autowired
    ChatProcessor chatProcessor;
    @Autowired
    UserProcessor userProcessor;
    @Autowired
    FriendProcessor friendProcessor;

    /** 获取聊天关系（暂时是私聊） */
    @BizType(BizTypeEnum.CHAT_GET_CHAT_LINK)
    @NeedLogin
    public GetChatLinkOutParams chatGetChatLink(GetChatLinkInParams inParams) throws Exception{
        String username = inParams.getUsername();
        String toUsername = inParams.getToUsername();

        /* 先检查这两个人的聊天关系是否已经存在 */
        ChatLink chatLink = chatProcessor.getChatLinkByUsernames(username, toUsername);
        if (chatLink == null)
            chatLink = chatProcessor.getChatLinkByUsernames(toUsername, username);
        // 如果不存在聊天关系，新建
        ChatManager chatManagerA;
        ChatManager chatManagerB;
        if (chatLink == null) {
            chatLink = chatProcessor.createChatLinkByUsernames(username, toUsername);
            // 两人的聊天管理器也要新建
            chatProcessor.createChatManager(chatLink.getId(), username, true);
            chatProcessor.createChatManager(chatLink.getId(), toUsername, false);
        }
        // 如果存在
        else {
            // 更新本人的聊天管理器的参数
            chatProcessor.updateWindowUnread(chatLink.getId(), username, true);
        }

        GetChatLinkOutParams outParams = new GetChatLinkOutParams();
        outParams.setLinkId(chatLink.getId());
        outParams.setUsername(toUsername);
        User toUser = userProcessor.getUserByUsername(toUsername);
        outParams.setNickname(toUser.getNickname());
        String avatar = toUser.getAvatar();
        int index = avatar.indexOf(AVATAR_RELATIVE_PATH);
        String avatar_url = FILE_URL + avatar.substring(index);
        outParams.setAvatar(avatar_url);
        Friendship friendship = friendProcessor.getFriendshipByUsername(username, toUsername);
        outParams.setRemark(friendship.getRemark());

        return outParams;
    }

    /** 退出聊天 */
    @BizType(BizTypeEnum.CHAT_QUIT_CHAT)
    @NeedLogin
    public CommonOutParams chatQuitChat(QuitChatInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String linkId = inParams.getLinkId();
        chatProcessor.updateWindowUnread(linkId, username, false);

        return new CommonOutParams(true);
    }

    /** 发送消息 */
    @BizType(BizTypeEnum.CHAT_SEND_MESSAGE)
    @NeedLogin
    public CommonOutParams chatSendMessage(SendMessageInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String linkId = inParams.getLinkId();
        boolean isMultiple = Boolean.parseBoolean(inParams.getIsMultiple());
        int type = Integer.parseInt(inParams.getType());

        /* 查找有没有该聊天id */
        ChatLink chatLink = null;
        if (isMultiple) {

        }
        else {
            chatLink = chatProcessor.getChatLinkById(linkId);
            if (chatLink == null)
                throw new CourseWarn(UserWarnEnum.NOT_FRIEND);
        }

        /* 把之前的最新消息改成非最新消息 */
        chatProcessor.revokeLatestMessage(linkId);

        /* 根据动态类型不同获取字段 */
        // 根据Windows和Linux配置不同的头像保存路径
        String OSName = System.getProperty(OS_NAME);
        String messagePath = OSName.toLowerCase().startsWith(WIN) ? WINDOWS_MESSAGE_PATH : LINUX_MESSAGE_PATH;

        /* 新建消息 */
        Message message = null;
        if (type == 0) {
            String text = inParams.getText();
            message = chatProcessor.createTextMessage(linkId, isMultiple, username, type, text);
        }
        else if (type == 1) {
            FileUpload file = inParams.getImage();
            String image = FileUtil.fileUploadToFile(file, messagePath);
            message = chatProcessor.createImageMessage(linkId, isMultiple, username, type, image);
        }
        else if(type == 2) {
            FileUpload file = inParams.getImage();
            String audio = FileUtil.fileUploadToFile(file, messagePath);
            message = chatProcessor.createAudioMessage(linkId, isMultiple, username, type, audio);
        }
        else if(type == 3) {
            FileUpload file = inParams.getImage();
            String video = FileUtil.fileUploadToFile(file, messagePath);
            message = chatProcessor.createVideoMessage(linkId, isMultiple, username, type, video);
        }
        else {
            double longitude = Double.parseDouble(inParams.getLongitude());
            double latitude = Double.parseDouble(inParams.getLatitude());
            message = chatProcessor.createLocationMessage(linkId, isMultiple, username, type, longitude, latitude);
        }

        /* 给接收方推送 */
        if (isMultiple) {
            // TODO: 群聊
        }
        else {
            String usernameA = chatLink.getUsernameA();
            String usernameB = chatLink.getUsernameB();
            String toUsername = usernameA.equals(username) ? usernameB : usernameA;

            // 更新未读数
            ChatManager chatManager = chatProcessor.getChatManager(linkId, toUsername);
            int unread = chatManager.getUnread();
            if (!chatManager.isInWindow()) {
                chatProcessor.addUnread(chatManager.getId(), unread + 1);
            }

            SendMessageWsOutParams outParams = new SendMessageWsOutParams();
            outParams.setLinkId(linkId);
            outParams.setUsername(username);
            outParams.setMultiple(false);
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            outParams.setSendTime(dateFormat.format(message.getSendTime()));
            outParams.setType(type);
            outParams.setText(message.getText());
            outParams.setImage(message.getImage());
            outParams.setAudio(message.getAudio());
            outParams.setVideo(message.getVideo());
            outParams.setLongitude(message.getLocation().getLongitude());
            outParams.setLatitude(message.getLocation().getLatitude());
            outParams.setUnread(unread);

            SocketUtil.sendMessageToUser(toUsername, outParams);
        }

        return new CommonOutParams(true);
    }

    /** 获取所有聊天 */
    @BizType(BizTypeEnum.CHAT_GET_CHATTINGS)
    @NeedLogin
    public GetChattingsOutParams chatGetChattings(CommonInParams inParams) throws Exception {
        String username = inParams.getUsername();

        // TODO
        GetChattingsOutParams outParams = new GetChattingsOutParams();
        return outParams;
    }
}
