package com.tsinghua.course.Biz.Processor;

import com.tsinghua.course.Base.Constant.KeyConstant;
import com.tsinghua.course.Base.Model.ChatItem;
import com.tsinghua.course.Base.Model.ChatMessage;
import com.tsinghua.course.Base.Model.ChatUserLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    /** 创建聊天关系并返回 */
    public ChatUserLink createChatUserLink(String from_username, String to_username) {
        ChatUserLink chatUserLink = new ChatUserLink();
        chatUserLink.setFromUsername(from_username);
        chatUserLink.setToUsername(to_username);
        chatUserLink.setCreateTime(new Date());
        mongoTemplate.insert(chatUserLink);
        return chatUserLink;
    }

    /** 根据双方用户名查询聊天关系 */
    public ChatUserLink getChatUserLink(String from_username, String to_username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.FROM_USERNAME).is(from_username)
                                    .and(KeyConstant.TO_USERNAME).is(to_username));
        return mongoTemplate.findOne(query, ChatUserLink.class);
    }

    /** 创建聊天条目 */
    public void createChatItem(String link_id, String from_username, String to_username) {
        ChatItem chatItem = new ChatItem();
        chatItem.setLinkId(link_id);
        chatItem.setFromUsername(from_username);
        chatItem.setToUsername(to_username);
        chatItem.setFromWindow(true);
        chatItem.setToWindow(false);
        chatItem.setUnread(0);
        mongoTemplate.insert(chatItem);
    }

    /** 更新在线状态 */
    public void updateChatItem(String link_id, String from_username, boolean window) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.LINK_ID).is(link_id)
                                    .and(KeyConstant.FROM_USERNAME).is(from_username));
        Update update = new Update();
        update.set(KeyConstant.FROM_WINDOW, window);
        if (window)
            update.set(KeyConstant.UNREAD, 0);
        mongoTemplate.upsert(query, update, ChatItem.class);

        Query query1 = new Query();
        query1.addCriteria(Criteria.where(KeyConstant.LINK_ID).is(link_id)
                                    .and(KeyConstant.TO_USERNAME).is(from_username));
        Update update1 = new Update();
        update1.set(KeyConstant.TO_WINDOW, window);
    }

    /** 添加新消息 */
    public void addChatMessage(String link_id, String from_username, String to_username,
                               String content, Date send_time, int type) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setLinkId(link_id);
        chatMessage.setFromUsername(from_username);
        chatMessage.setToUsername(to_username);
        chatMessage.setContent(content);
        chatMessage.setSendTime(send_time);
        chatMessage.setType(type);
        chatMessage.setLatest(true);

        mongoTemplate.insert(chatMessage);
    }

    /** 更新上一条消息的最新状态 */
    public void updateLastMsgLatest(String link_id) {
        /* 获取当前时间最晚的一条消息 */
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.LINK_ID).is(link_id));
        query.with(Sort.by(Sort.Order.desc(KeyConstant.SEND_TIME)));
        List<ChatMessage> chatMessageList = mongoTemplate.find(query, ChatMessage.class);
        String latest_id = chatMessageList.get(0).getId();

        /* 将这条消息设置为非最新消息 */
        Query query1 = new Query();
        query.addCriteria(Criteria.where(KeyConstant.ID).is(latest_id));
        Update update = new Update();
        update.set(KeyConstant.LATEST, false);
        mongoTemplate.upsert(query1, update, ChatMessage.class);
    }

    /** 根据link_id和from_username查询聊天条目 */
    public ChatItem getChatItem(String link_id, String to_username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.LINK_ID).is(link_id)
                                    .and(KeyConstant.TO_USERNAME).is(to_username));
        return mongoTemplate.findOne(query, ChatItem.class);
    }

    /** 移除聊天条目 */
    public void removeChatItem(String link_id, String from_username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.LINK_ID).is(link_id)
                                    .and(KeyConstant.FROM_USERNAME).is(from_username));
        mongoTemplate.remove(query, ChatItem.class);
    }

    /** 删除聊天关系 */
    public void removeChatUserLink(String link_id) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.LINK_ID).is(link_id));
        mongoTemplate.remove(query, ChatUserLink.class);
    }
}
