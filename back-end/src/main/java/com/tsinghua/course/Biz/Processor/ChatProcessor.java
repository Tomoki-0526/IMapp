package com.tsinghua.course.Biz.Processor;

import com.tsinghua.course.Base.Constant.KeyConstant;
import com.tsinghua.course.Base.Model.ChatItem;
import com.tsinghua.course.Base.Model.ChatUserLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Date;

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
        chatItem.setStatus(false);
        mongoTemplate.insert(chatItem);
    }

    /** 更新在线状态 */
    public void updateChatItemWindow(String link_id, String from_username, boolean window) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.LINK_ID).is(link_id)
                                    .and(KeyConstant.FROM_USERNAME).is(from_username));
        Update update = new Update();
        update.set(KeyConstant.FROM_WINDOW, window);
        mongoTemplate.upsert(query, update, ChatItem.class);

        Query query1 = new Query();
        query1.addCriteria(Criteria.where(KeyConstant.LINK_ID).is(link_id)
                                    .and(KeyConstant.TO_USERNAME).is(from_username));
        Update update1 = new Update();
        update1.set(KeyConstant.TO_WINDOW, window);
    }
}
