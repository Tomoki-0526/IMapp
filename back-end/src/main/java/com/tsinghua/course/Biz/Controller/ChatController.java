package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Base.CustomizedClass.ChatItem;
import com.tsinghua.course.Base.CustomizedClass.Location;
import com.tsinghua.course.Base.CustomizedClass.MsgItem;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

        /* 获取聊天内的所有消息 */
        List<Message> messageList = chatProcessor.getMessages(chatLink.getId());
        List<MsgItem> msgItemList = new ArrayList<>();
        for (Message message: messageList) {
            String msgId = message.getId();
            Date sendTime = message.getSendTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            String sendTimeStr = dateFormat.format(message.getSendTime());
            boolean fromMyself = message.getUsername().equals(username);
            int type = message.getType();
            String text = message.getText();
            String image = message.getImage();
            String audio = message.getAudio();
            String video = message.getVideo();
            double longitude = message.getLocation().getLongitude();
            double latitude = message.getLocation().getLatitude();

            if(type == 1){
                index = image.indexOf(MESSAGE_RELATIVE_PATH);
                image = FILE_URL + image.substring(index);
            }
            else if(type == 2) {
                index = audio.indexOf(MESSAGE_RELATIVE_PATH);
                audio = FILE_URL + audio.substring(index);
            }
            else if(type == 3) {
                index = video.indexOf(MESSAGE_RELATIVE_PATH);
                video = FILE_URL + video.substring(index);
            }

            MsgItem msgItem = new MsgItem();
            msgItem.setMsgId(msgId);
            msgItem.setSendTime(sendTimeStr);
            msgItem.setFromMyself(fromMyself);
            msgItem.setType(type);
            msgItem.setText(text);
            msgItem.setImage(image);
            msgItem.setAudio(audio);
            msgItem.setVideo(video);
            msgItem.setLongitude(longitude);
            msgItem.setLatitude(latitude);
            msgItemList.add(msgItem);
        }

        MsgItem[] msgs = new MsgItem[msgItemList.size()];
        msgItemList.toArray(msgs);
        outParams.setMsgs(msgs);

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
            // TODO: 群聊
        }
        else {
            chatLink = chatProcessor.getChatLinkById(linkId);
            if (chatLink == null)
                throw new CourseWarn(UserWarnEnum.NOT_FRIEND);
        }
        String usernameA = chatLink.getUsernameA();
        String usernameB = chatLink.getUsernameB();

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
            FileUpload file = inParams.getAudio();
            String audio = FileUtil.fileUploadToFile(file, messagePath);
            message = chatProcessor.createAudioMessage(linkId, isMultiple, username, type, audio);
        }
        else if(type == 3) {
            FileUpload file = inParams.getVideo();
            String video = FileUtil.fileUploadToFile(file, messagePath);
            message = chatProcessor.createVideoMessage(linkId, isMultiple, username, type, video);
        }
        else {
            double longitude = Double.parseDouble(inParams.getLongitude());
            double latitude = Double.parseDouble(inParams.getLatitude());
            message = chatProcessor.createLocationMessage(linkId, isMultiple, username, type, longitude, latitude);
        }

        /* 建立可见性表 */
        if (isMultiple) {
            // TODO: 群聊
        }
        else {
            chatProcessor.createVisibility(message.getId(), usernameA);
            chatProcessor.createVisibility(message.getId(), usernameB);
        }

        /* 给接收方推送 */
        if (isMultiple) {
            // TODO: 群聊
        }
        else {
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
            outParams.setMsgId(message.getId());
            outParams.setUnread(unread);

            if (type == 0 || type == 4) {
                outParams.setText(message.getText());
                outParams.setImage(message.getImage());
                outParams.setAudio(message.getAudio());
                outParams.setVideo(message.getVideo());
                outParams.setLongitude(message.getLocation().getLongitude());
                outParams.setLatitude(message.getLocation().getLatitude());
            }
            else if(type == 1){
                outParams.setText(message.getText());
                String image = message.getImage();
                int index = image.indexOf(MESSAGE_RELATIVE_PATH);
                String imageUrl = FILE_URL + image.substring(index);
                outParams.setImage(imageUrl);
                outParams.setAudio(message.getAudio());
                outParams.setVideo(message.getVideo());
                outParams.setLongitude(message.getLocation().getLongitude());
                outParams.setLatitude(message.getLocation().getLatitude());
            }
            else if(type == 2) {
                outParams.setText(message.getText());
                outParams.setImage(message.getImage());
                String audio = message.getAudio();
                int index = audio.indexOf(MESSAGE_RELATIVE_PATH);
                String audioUrl = FILE_URL + audio.substring(index);
                outParams.setAudio(audioUrl);
                outParams.setVideo(message.getVideo());
                outParams.setLongitude(message.getLocation().getLongitude());
                outParams.setLatitude(message.getLocation().getLatitude());
            }
            else if(type == 3) {
                outParams.setText(message.getText());
                outParams.setImage(message.getImage());
                outParams.setAudio(message.getAudio());
                String video = message.getVideo();
                int index = video.indexOf(MESSAGE_RELATIVE_PATH);
                String videoUrl = FILE_URL + video.substring(index);
                outParams.setVideo(videoUrl);
                outParams.setLongitude(message.getLocation().getLongitude());
                outParams.setLatitude(message.getLocation().getLatitude());
            }

            SocketUtil.sendMessageToUser(toUsername, outParams);
        }

        return new CommonOutParams(true);
    }

    /** 获取所有聊天 */
    @BizType(BizTypeEnum.CHAT_GET_CHATTINGS)
    @NeedLogin
    public GetChattingsOutParams chatGetChattings(CommonInParams inParams) throws Exception {
        String username = inParams.getUsername();

        /* 获取所有聊天关系并组织出参 */
        // 私聊
        List<ChatLink> chatLinkList = chatProcessor.getChatLinksByUsername(username);
        List<ChatItem> chatItemList = new ArrayList<>();
        for (ChatLink chatLink: chatLinkList) {
            String linkId = chatLink.getId();

            Message message = chatProcessor.getLatestMessage(linkId);
            // 如果从没发过消息，不显示在前端
            if (message == null)
                continue;

            int type = message.getType();
            String latestMsg;
            if (type == 0)
                latestMsg = message.getText();
            else if(type == 1)
                latestMsg = "图片";
            else if(type == 2)
                latestMsg = "音频";
            else if(type == 3)
                latestMsg = "视频";
            else
                latestMsg = "定位信息";

            Date sendTime = message.getSendTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            String sendTimeStr = dateFormat.format(sendTime);

            String usernameA = chatLink.getUsernameA();
            String usernameB = chatLink.getUsernameB();
            String toUsername = usernameA.equals(username) ? usernameB : usernameA;
            User toUser = userProcessor.getUserByUsername(toUsername);
            String avatar = toUser.getAvatar();
            int index = avatar.indexOf(AVATAR_RELATIVE_PATH);
            String avatar_url = FILE_URL + avatar.substring(index);
            String name = toUser.getNickname();
            Friendship friendship = friendProcessor.getFriendshipByUsername(username, toUsername);
            if (!friendship.getRemark().equals(""))
                name = friendship.getRemark();

            ChatItem chatItem = new ChatItem();
            chatItem.setAvatar(avatar_url);
            chatItem.setMultiple(false);
            chatItem.setLinkId(linkId);
            chatItem.setName(name);
            chatItem.setType(type);
            chatItem.setLatestMsg(latestMsg);
            chatItem.setSendTime(sendTimeStr);
            chatItemList.add(chatItem);
        }

        // TODO: 群聊

        ChatItemSort(chatItemList);
        ChatItem[] chatItems = new ChatItem[chatItemList.size()];
        chatItemList.toArray(chatItems);

        GetChattingsOutParams outParams = new GetChattingsOutParams();
        outParams.setChattings(chatItems);
        return outParams;
    }

    private static void ChatItemSort(List<ChatItem> list) {
        list.sort((o1, o2) -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
            Date dt1 = null;
            Date dt2 = null;
            try {
                dt1 = dateFormat.parse(o1.getSendTime());
                dt2 = dateFormat.parse(o2.getSendTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (dt1.before(dt2))
                return 1;
            else
                return -1;
        });
    }
}

