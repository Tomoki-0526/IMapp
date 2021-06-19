package com.tsinghua.course.Biz.Processor;

import com.tsinghua.course.Base.Constant.KeyConstant;
import com.tsinghua.course.Base.CustomizedClass.Location;
import com.tsinghua.course.Base.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @描述 聊天原子处理器
 */
@Component
public class ChatProcessor {
    @Autowired
    MongoTemplate mongoTemplate;

    /** 根据ID查找聊天关系 */
    public ChatLink getChatLinkById(String linkId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.ID).is(linkId));

        return mongoTemplate.findOne(query, ChatLink.class);
    }

    /** 根据两人用户名查找聊天关系 */
    public ChatLink getChatLinkByUsernames(String usernameA, String usernameB) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME_A).is(usernameA)
                                    .and(KeyConstant.USERNAME_B).is(usernameB));

        return mongoTemplate.findOne(query, ChatLink.class);
    }

    /** 根据用户名查找这个用户的所有聊天 */
    public List<ChatLink> getChatLinksByUsername(String username) {
        Criteria c1 = Criteria.where(KeyConstant.USERNAME_A).is(username);
        Criteria c2 = Criteria.where(KeyConstant.USERNAME_B).is(username);
        Criteria c = new Criteria();

        Query query = new Query();
        query.addCriteria(c.orOperator(c1, c2));

        return mongoTemplate.find(query, ChatLink.class);
    }

    /** 根据两人用户名创建聊天关系 */
    public ChatLink createChatLinkByUsernames(String usernameA, String usernameB) {
        ChatLink chatLink = new ChatLink();
        chatLink.setUsernameA(usernameA);
        chatLink.setUsernameB(usernameB);

        return mongoTemplate.insert(chatLink);
    }

    /** 删除聊天关系 */
    public void removeChatLink(String usernameA, String usernameB) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME_A).is(usernameA)
                                    .and(KeyConstant.USERNAME_B).is(usernameB));

        mongoTemplate.remove(query, ChatLink.class);
    }

    /** 为一个用户创建聊天管理器 */
    public ChatManager createChatManager(String linkId, String username, boolean window) {
        ChatManager chatManager = new ChatManager();
        chatManager.setLinkId(linkId);
        chatManager.setInWindow(window);
        chatManager.setUsername(username);
        chatManager.setUnread(0);

        return mongoTemplate.insert(chatManager);
    }

    /** 根据聊天id和用户名查找聊天管理器 */
    public ChatManager getChatManager(String linkId, String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.LINK_ID).is(linkId)
                                    .and(KeyConstant.USERNAME).is(username));

        return mongoTemplate.findOne(query, ChatManager.class);
    }

    /** 更新窗口值和未读数 */
    public void updateWindowUnread(String linkId, String username, boolean window) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.LINK_ID).is(linkId)
                                    .and(KeyConstant.USERNAME).is(username));
        Update update = new Update();
        update.set(KeyConstant.IN_WINDOW, window);
        if (window)
            update.set(KeyConstant.UNREAD, 0);

        mongoTemplate.upsert(query, update, ChatManager.class);
    }

    /** 未读数加一 */
    public void addUnread(String managerId, int newUnread) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.ID).is(managerId));
        Update update = new Update();
        update.set(KeyConstant.UNREAD, newUnread);

        mongoTemplate.upsert(query, update, ChatManager.class);
    }

    /** 新建文字消息 */
    public Message createTextMessage(String linkId, boolean isMultiple, String username, int type, String text) {
        Message message = new Message();
        message.setLinkId(linkId);
        message.setMultiple(isMultiple);
        message.setUsername(username);
        message.setType(type);
        message.setSendTime(new Date());
        message.setText(text);
        message.setImage("");
        message.setAudio("");
        message.setVideo("");
        message.setLocation(new Location());
        message.setLatest(true);

        return mongoTemplate.insert(message);
    }

    /** 新建图片消息 */
    public Message createImageMessage(String linkId, boolean isMultiple, String username, int type, String image) {
        Message message = new Message();
        message.setLinkId(linkId);
        message.setMultiple(isMultiple);
        message.setUsername(username);
        message.setType(type);
        message.setSendTime(new Date());
        message.setText("");
        message.setImage(image);
        message.setAudio("");
        message.setVideo("");
        message.setLocation(new Location());
        message.setLatest(true);

        return mongoTemplate.insert(message);
    }

    /** 新建音频消息 */
    public Message createAudioMessage(String linkId, boolean isMultiple, String username, int type, String audio) {
        Message message = new Message();
        message.setLinkId(linkId);
        message.setMultiple(isMultiple);
        message.setUsername(username);
        message.setType(type);
        message.setSendTime(new Date());
        message.setText("");
        message.setImage("");
        message.setAudio(audio);
        message.setVideo("");
        message.setLocation(new Location());
        message.setLatest(true);

        return mongoTemplate.insert(message);
    }

    /** 新建视频消息 */
    public Message createVideoMessage(String linkId, boolean isMultiple, String username, int type, String video) {
        Message message = new Message();
        message.setLinkId(linkId);
        message.setMultiple(isMultiple);
        message.setUsername(username);
        message.setType(type);
        message.setSendTime(new Date());
        message.setText("");
        message.setImage("");
        message.setAudio("");
        message.setVideo(video);
        message.setLocation(new Location());
        message.setLatest(true);

        return mongoTemplate.insert(message);
    }

    /** 新建定位消息 */
    public Message createLocationMessage(String linkId, boolean isMultiple, String username, int type, double longitude, double latitude) {
        Message message = new Message();
        message.setLinkId(linkId);
        message.setMultiple(isMultiple);
        message.setUsername(username);
        message.setType(type);
        message.setSendTime(new Date());
        message.setText("");
        message.setImage("");
        message.setAudio("");
        message.setVideo("");
        message.setLocation(new Location(longitude, latitude));
        message.setLatest(true);

        return mongoTemplate.insert(message);
    }

    /** 把之前的最新消息改为非最新消息 */
    public void revokeLatestMessage(String linkId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.LINK_ID).is(linkId)
                                    .and(KeyConstant.IS_LATEST).is(true));

        Message message = mongoTemplate.findOne(query, Message.class);
        if (message == null)
            return;

        Update update = new Update();
        update.set(KeyConstant.IS_LATEST, false);

        mongoTemplate.upsert(query, update, Message.class);
    }

    /** 获取最新消息 */
    public Message getLatestMessage(String linkId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.LINK_ID).is(linkId)
                                    .and(KeyConstant.IS_LATEST).is(true));

        return mongoTemplate.findOne(query, Message.class);
    }

    /** 获取一个聊天中的全部消息 */
    public List<Message> getMessages(String linkId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.LINK_ID).is(linkId));

        return mongoTemplate.find(query, Message.class);
    }

    /** 建立可见性表 */
    public void createVisibility(String msgId, String username) {
        MsgVisibility msgVisibility = new MsgVisibility();
        msgVisibility.setMsgId(msgId);
        msgVisibility.setUsername(username);
        msgVisibility.setVisible(true);

        mongoTemplate.insert(msgVisibility);
    }

    /** 修改一条消息对某一个用户的可见性 */
    public void modifyVisibility(String msgId, String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.MSG_ID).is(msgId)
                                    .and(KeyConstant.USERNAME).is(username));
        Update update = new Update();
        update.set(KeyConstant.VISIBLE, false);

        mongoTemplate.upsert(query, update, MsgVisibility.class);
    }

    /** 获得一条消息对某一个用户的可见性 */
    public MsgVisibility getVisibility(String msgId, String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.MSG_ID).is(msgId)
                .and(KeyConstant.USERNAME).is(username));

        return mongoTemplate.findOne(query, MsgVisibility.class);
    }

    /** 创建群聊 */
    public GroupLink createGroup(String groupName) {
        GroupLink groupLink = new GroupLink();
        groupLink.setGroupName(groupName);

        return mongoTemplate.insert(groupLink);
    }

    /** 创建群成员 */
    public void createGroupMember(String groupLinkId, String username) {
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupLinkId(groupLinkId);
        groupMember.setUsername(username);

        mongoTemplate.insert(groupMember);
    }
}
