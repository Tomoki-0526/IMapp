package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Base.CustomizedClass.ChatItem;
import com.tsinghua.course.Base.CustomizedClass.GroupItem;
import com.tsinghua.course.Base.CustomizedClass.MemberItem;
import com.tsinghua.course.Base.CustomizedClass.MsgItem;
import com.tsinghua.course.Base.Error.CourseWarn;
import com.tsinghua.course.Base.Error.UserWarnEnum;
import com.tsinghua.course.Base.Model.*;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.In.*;
import com.tsinghua.course.Biz.Controller.Params.ChatParams.Out.*;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.tsinghua.course.Base.Constant.GlobalConstant.*;
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

            // 检验可见性
            MsgVisibility msgVisibility = chatProcessor.getVisibility(msgId, username);
            if (!msgVisibility.isVisible())
                continue;

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

            String fromUsername = message.getUsername();
            msgItem.setUsername(fromUsername);
            User fromUser = userProcessor.getUserByUsername(fromUsername);
            msgItem.setNickname(fromUser.getNickname());
            String fromAvatar = fromUser.getAvatar();
            int fromIndex = fromAvatar.indexOf(AVATAR_RELATIVE_PATH);
            String fromAvatarUrl = FILE_URL + fromAvatar.substring(fromIndex);
            msgItem.setAvatar(fromAvatarUrl);
            msgItem.setRemark(friendship.getRemark());

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
        GroupLink groupLink = null;
        String usernameA = null;
        String usernameB = null;
        List<GroupMember> groupMemberList = new ArrayList<>();
        if (isMultiple) {
            // 群聊
            groupLink = chatProcessor.getGroupById(linkId);
            if (groupLink == null)
                throw new CourseWarn(UserWarnEnum.GROUP_NO_EXIST);
            GroupMember groupMember = chatProcessor.getGroupMemberByUsername(linkId, username);
            if (groupMember == null)
                throw new CourseWarn(UserWarnEnum.NOT_MEMBER);
            groupMemberList = chatProcessor.getGroupMembers(linkId);
        }
        else {
            chatLink = chatProcessor.getChatLinkById(linkId);
            if (chatLink == null)
                throw new CourseWarn(UserWarnEnum.NOT_FRIEND);
            usernameA = chatLink.getUsernameA();
            usernameB = chatLink.getUsernameB();
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

        /* 建立可见性表并定向推送 */
        if (isMultiple) {
            // 群聊
            for (GroupMember member: groupMemberList) {
                chatProcessor.createVisibility(message.getId(), member.getUsername());

                /* ws推送 */
                if (member.getUsername().equals(username))
                    continue;
                int unread = member.getUnread();
                if (!member.isInWindow()) {
                    unread++;
                    chatProcessor.addGroupUnread(member.getId(), unread);
                }

                SendMessageWsOutParams outParams = new SendMessageWsOutParams();
                outParams.setLinkId(linkId);
                outParams.setName(groupLink.getGroupName());
                outParams.setMultiple(true);
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
                    if (type == 0)
                        outParams.setFlag(4);
                    else
                        outParams.setFlag(8);
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
                    outParams.setFlag(5);
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
                    outParams.setFlag(6);
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
                    outParams.setFlag(7);
                }

                SocketUtil.sendMessageToUser(member.getUsername(), outParams);
            }
        }
        else {
            chatProcessor.createVisibility(message.getId(), usernameA);
            chatProcessor.createVisibility(message.getId(), usernameB);

            /* ws推送 */
            String toUsername = usernameA.equals(username) ? usernameB : usernameA;
            // 更新未读数
            ChatManager chatManager = chatProcessor.getChatManager(linkId, toUsername);
            int unread = chatManager.getUnread();
            if (!chatManager.isInWindow()) {
                unread++;
                chatProcessor.addUnread(chatManager.getId(), unread);
            }

            SendMessageWsOutParams outParams = new SendMessageWsOutParams();
            outParams.setLinkId(linkId);
            User user = userProcessor.getUserByUsername(username);
            outParams.setName(user.getNickname());
            Friendship friendship = friendProcessor.getFriendshipByUsername(toUsername, username);
            if (friendship != null)
                outParams.setName(friendship.getRemark());
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
                if (type == 0)
                    outParams.setFlag(4);
                else
                    outParams.setFlag(8);
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
                outParams.setFlag(5);
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
                outParams.setFlag(6);
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
                outParams.setFlag(7);
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
            // BUG: 如果最新的消息被删掉了，仍然会显示在聊天项目中
            // TODO: 检验可见性
            MsgVisibility msgVisibility = chatProcessor.getVisibility(message.getId(), username);
            if (!msgVisibility.isVisible()) {
                List<Message> messageList = chatProcessor.getMessages(linkId);
                for (int i = 0; i < messageList.size(); ++i) {
                    String msgId = messageList.get(i).getId();
                    MsgVisibility msgV = chatProcessor.getVisibility(msgId, username);
                    if (!msgV.isVisible())
                        messageList.remove(messageList.get(i));
                }
                MessageSort(messageList);
                message = messageList.get(0);
            }

            int type = message.getType();
            String latestMsg;
            if (type == 0)
                latestMsg = message.getText();
            else if(type == 1)
                latestMsg = "[图片]";
            else if(type == 2)
                latestMsg = "[音频]";
            else if(type == 3)
                latestMsg = "[视频]";
            else
                latestMsg = "[定位信息]";

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
            chatItem.setUsername(toUsername);
            chatItem.setName(name);
            chatItem.setType(type);
            chatItem.setLatestMsg(latestMsg);
            chatItem.setSendTime(sendTimeStr);
            chatItemList.add(chatItem);
        }
        // 群聊
        List<GroupLink> groupLinkList = chatProcessor.getGroupsByUsername(username);
        for (GroupLink groupLink: groupLinkList) {
            String groupLinkId = groupLink.getId();

            Message message = chatProcessor.getLatestMessage(groupLinkId);
            if (message == null)
                continue;
            MsgVisibility msgVisibility = chatProcessor.getVisibility(message.getId(), username);
            if (!msgVisibility.isVisible()) {
                List<Message> messageList = chatProcessor.getGroupMessages(groupLinkId);
                for (int i = 0; i < messageList.size(); ++i) {
                    String msgId = messageList.get(i).getId();
                    MsgVisibility msgV = chatProcessor.getVisibility(msgId, username);
                    if (!msgV.isVisible())
                        messageList.remove(messageList.get(i));
                }
                MessageSort(messageList);
                message = messageList.get(0);
            }

            int type = message.getType();
            String latestMsg;
            if (type == 0)
                latestMsg = message.getText();
            else if(type == 1)
                latestMsg = "[图片]";
            else if(type == 2)
                latestMsg = "[音频]";
            else if(type == 3)
                latestMsg = "[视频]";
            else
                latestMsg = "[定位信息]";

            Date sendTime = message.getSendTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            String sendTimeStr = dateFormat.format(sendTime);

            String avatar = groupLink.getAvatar();
            int index = avatar.indexOf(AVATAR_RELATIVE_PATH);
            String avatar_url = FILE_URL + avatar.substring(index);
            String groupName = groupLink.getGroupName();

            ChatItem chatItem = new ChatItem();
            chatItem.setAvatar(avatar_url);
            chatItem.setMultiple(true);
            chatItem.setLinkId(groupLinkId);
            chatItem.setUsername("");
            chatItem.setName(groupName);
            chatItem.setType(type);
            chatItem.setLatestMsg(latestMsg);
            chatItem.setSendTime(sendTimeStr);
            chatItemList.add(chatItem);
        }

        ChatItemSort(chatItemList);
        ChatItem[] chatItems = new ChatItem[chatItemList.size()];
        chatItemList.toArray(chatItems);

        GetChattingsOutParams outParams = new GetChattingsOutParams();
        outParams.setChattings(chatItems);
        return outParams;
    }

    /** 删除聊天记录 */
    @BizType(BizTypeEnum.CHAT_REMOVE_MESSAGES)
    @NeedLogin
    public CommonOutParams chatRemoveMessages(RemoveMessagesInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String[] msgs = inParams.getMsgs();

        for (String msgId: msgs) {
            chatProcessor.modifyVisibilityToFalse(msgId, username);
        }

        return new CommonOutParams(true);
    }

    /** 创建群聊 */
    @BizType(BizTypeEnum.CHAT_CREATE_GROUP)
    @NeedLogin
    public CommonOutParams chatCreateGroup(CreateGroupInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String groupName = inParams.getGroupName();
        String[] members = inParams.getMembers();
        if (groupName == null)
            groupName = DEFAULT_GROUP;

        /* 创建群组 */
        GroupLink groupLink = chatProcessor.createGroup(groupName);

        /* 创建群成员表 */
        chatProcessor.createGroupMember(groupLink.getId(), username);
        for (String member: members) {
            chatProcessor.createGroupMember(groupLink.getId(), member);
        }

        /* ws出参 */
        for (String member: members) {
            CreateGroupWsOutParams outParams = new CreateGroupWsOutParams();
            outParams.setFlag(9);
            outParams.setGroupName(groupName);
            SocketUtil.sendMessageToUser(member, outParams);
        }

        return new CommonOutParams(true);
    }

    /** 查看所有群 */
    @BizType(BizTypeEnum.CHAT_GET_GROUPS)
    @NeedLogin
    public GetGroupsOutParams chatGetGroups(CommonInParams inParams) throws Exception {
        String username = inParams.getUsername();

        List<GroupLink> groupLinkList = chatProcessor.getGroupsByUsername(username);
        List<GroupItem> groupItemList = new ArrayList<>();
        for (GroupLink groupLink: groupLinkList) {
            GroupItem groupItem = new GroupItem();
            groupItem.setGroupId(groupLink.getId());
            groupItem.setGroupName(groupLink.getGroupName());

            String avatar = groupLink.getAvatar();
            int index = avatar.indexOf(AVATAR_RELATIVE_PATH);
            String avatar_url = FILE_URL + avatar.substring(index);
            groupItem.setAvatar(avatar_url);

            groupItemList.add(groupItem);
        }
        GroupItem[] groupItems = new GroupItem[groupItemList.size()];
        groupItemList.toArray(groupItems);

        GetGroupsOutParams outParams = new GetGroupsOutParams();
        outParams.setGroups(groupItems);
        return outParams;
    }

    /** 进入群聊 */
    @BizType(BizTypeEnum.CHAT_ENTER_GROUP_CHAT)
    @NeedLogin
    public EnterGroupChatOutParams chatEnterGroupChat(EnterGroupChatInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String groupLinkId = inParams.getGroupId();
        GroupLink groupLink = chatProcessor.getGroupById(groupLinkId);

        /* 修改窗口值和未读数 */
        chatProcessor.updateGroupWindowUnread(groupLinkId, username, true);

        /* 获取聊天内的所有消息 */
        EnterGroupChatOutParams outParams = new EnterGroupChatOutParams();
        List<Message> messageList = chatProcessor.getMessages(groupLinkId);
        List<MsgItem> msgItemList = new ArrayList<>();
        for (Message message: messageList) {
            String msgId = message.getId();

            // 检验可见性
            MsgVisibility msgVisibility = chatProcessor.getVisibility(msgId, username);
            if (!msgVisibility.isVisible())
                continue;

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

            int index = 0;
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

            String fromUsername = message.getUsername();
            msgItem.setUsername(fromUsername);
            User fromUser = userProcessor.getUserByUsername(fromUsername);
            msgItem.setNickname(fromUser.getNickname());

            String fromAvatar = fromUser.getAvatar();
            int fromIndex = fromAvatar.indexOf(AVATAR_RELATIVE_PATH);
            String fromAvatarUrl = FILE_URL + fromAvatar.substring(fromIndex);
            msgItem.setAvatar(fromAvatarUrl);

            Friendship friendship = friendProcessor.getFriendshipByUsername(username, fromUsername);
            if (friendship != null)
                msgItem.setRemark(friendship.getRemark());
            else
                msgItem.setRemark("");

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
        outParams.setGroupName(groupLink.getGroupName());
        String avatar = groupLink.getAvatar();
        int index = avatar.indexOf(AVATAR_RELATIVE_PATH);
        String avatar_url = FILE_URL + avatar.substring(index);
        outParams.setAvatar(avatar_url);

        return outParams;
    }

    /** 离开群聊界面 */
    @BizType(BizTypeEnum.CHAT_QUIT_GROUP_CHAT)
    @NeedLogin
    public CommonOutParams chatExitGroupChat(QuitGroupChatInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String groupLinkId = inParams.getGroupId();
        /* 修改窗口值和未读数 */
        chatProcessor.updateGroupWindowUnread(groupLinkId, username, false);

        return new CommonOutParams(true);
    }

    /** 查看群成员 */
    @BizType(BizTypeEnum.CHAT_GET_GROUP_MEMBERS)
    @NeedLogin
    public GetGroupMembersOutParams chatGetGroupMembers(GetGroupMembersInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String groupLinkId = inParams.getGroupId();

        /* 判断当前用户是不是群成员 */
        GroupMember self = chatProcessor.getGroupMemberByUsername(groupLinkId, username);
        if (self == null)
            throw new CourseWarn(UserWarnEnum.NOT_MEMBER);

        List<GroupMember> groupMemberList = chatProcessor.getGroupMembers(groupLinkId);
        List<MemberItem> memberItemList = new ArrayList<>();
        for (GroupMember groupMember: groupMemberList) {
            String memberUsername = groupMember.getUsername();
            User member = userProcessor.getUserByUsername(memberUsername);

            MemberItem memberItem = new MemberItem();
            String avatar = member.getAvatar();
            int index = avatar.indexOf(AVATAR_RELATIVE_PATH);
            String avatarUrl = FILE_URL + avatar.substring(index);
            memberItem.setAvatar(avatarUrl);
            memberItem.setUsername(memberUsername);
            memberItem.setNickname(member.getNickname());

            Friendship friendship = friendProcessor.getFriendshipByUsername(username, memberUsername);
            if (friendship == null) {
                memberItem.setFriend(false);
                memberItem.setRemark("");
            }
            else {
                memberItem.setFriend(true);
                memberItem.setRemark(friendship.getRemark());
            }

            memberItemList.add(memberItem);
        }
        MemberItem[] members = new MemberItem[memberItemList.size()];
        memberItemList.toArray(members);
        GetGroupMembersOutParams outParams = new GetGroupMembersOutParams();
        outParams.setMembers(members);
        return outParams;
    }

    /** 邀请好友进入群 */
    @BizType(BizTypeEnum.CHAT_INVITE_TO_GROUP)
    @NeedLogin
    public CommonOutParams chatInviteToGroup(InviteToGroupInParams inParams) throws Exception {
        String groupLinkId = inParams.getGroupId();
        String[] usernames = inParams.getUsernames();

        for (String user: usernames) {
            /* 检查是否已经是群成员 */
            GroupMember groupMember = chatProcessor.getGroupMemberByUsername(groupLinkId, user);
            if (groupMember != null)
                continue;
            chatProcessor.createGroupMember(groupLinkId, user);
            /* ws出参 */
            InviteToGroupWsOutParams outParams = new InviteToGroupWsOutParams();
            GroupLink groupLink = chatProcessor.getGroupById(groupLinkId);
            outParams.setFlag(10);
            outParams.setGroupName(groupLink.getGroupName());
            SocketUtil.sendMessageToUser(user, outParams);
        }

        return new CommonOutParams(true);
    }

    /** 退出群聊 */
    @BizType(BizTypeEnum.CHAT_QUIT_GROUP)
    @NeedLogin
    public CommonOutParams chatQuitGroup(QuitGroupInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String groupLinkId = inParams.getGroupId();

        chatProcessor.removeMember(groupLinkId, username);

        return new CommonOutParams(true);
    }

    /** 查看历史记录 */
    @BizType(BizTypeEnum.CHAT_GET_HISTORY)
    @NeedLogin
    public GetHistoryOutParams chatGetHistory(GetHistoryInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String linkId = inParams.getLinkId();

        List<Message> messageList = chatProcessor.getMessages(linkId);
        List<MsgItem> msgItemList = new ArrayList<>();
        for (Message message: messageList) {
            String msgId = message.getId();

            // 检验可见性
            MsgVisibility msgVisibility = chatProcessor.getVisibility(msgId, username);
            if (!msgVisibility.isVisible())
                continue;

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

            int index;
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

            String fromUsername = message.getUsername();
            msgItem.setUsername(fromUsername);
            User fromUser = userProcessor.getUserByUsername(fromUsername);
            msgItem.setNickname(fromUser.getNickname());

            String fromAvatar = fromUser.getAvatar();
            int fromIndex = fromAvatar.indexOf(AVATAR_RELATIVE_PATH);
            String fromAvatarUrl = FILE_URL + fromAvatar.substring(fromIndex);
            msgItem.setAvatar(fromAvatarUrl);

            Friendship friendship = friendProcessor.getFriendshipByUsername(username, fromUsername);
            if (friendship != null)
                msgItem.setRemark(friendship.getRemark());
            else
                msgItem.setRemark("");

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

        GetHistoryOutParams outParams = new GetHistoryOutParams();
        outParams.setMsgs(msgs);
        return outParams;
    }

    /** 聊天项按最新消息时间排序 */
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

    /** 消息列表按发送时间排序 */
    private static void MessageSort(List<Message> list) {
        list.sort((o1, o2) -> {
            Date dt1 = o1.getSendTime();
            Date dt2 = o2.getSendTime();
            if (dt1.before(dt2))
                return 1;
            else
                return -1;
        });
    }
}

