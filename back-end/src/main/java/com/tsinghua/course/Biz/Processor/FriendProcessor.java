package com.tsinghua.course.Biz.Processor;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.internal.bulk.DeleteRequest;
import com.tsinghua.course.Base.Constant.KeyConstant;
import com.tsinghua.course.Base.Model.FriendRequest;
import com.tsinghua.course.Base.Model.Friendship;
import com.tsinghua.course.Base.Model.User;
import com.tsinghua.course.Biz.Controller.Params.FriendParams.out.FindFriendOutParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tsinghua.course.Base.Constant.GlobalConstant.DATETIME_PATTERN;

/**
 * @描述 好友原子处理器
 **/
@Component
public class FriendProcessor {
    @Autowired
    MongoTemplate mongoTemplate;

    /** 查找自己所有的好友关系 */
    public List<Friendship> getAllFriendship(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        return mongoTemplate.find(query, Friendship.class);
    }

    /** 根据用户名查找自己的某一位好友 */
    public Friendship getFriendshipByUsername(String username, String friend_username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username)
                                    .and(KeyConstant.FRIEND_USERNAME).is(friend_username));
        return mongoTemplate.findOne(query, Friendship.class);
    }

    /** 根据昵称查找自己的好友 */
    public List<Friendship> getFriendshipByNickname(String username, String friend_nickname) {
        List<Friendship> friendship_list = getAllFriendship(username);
        Query query = new Query();
        List<Friendship> result = new ArrayList<>();
        for (Friendship friendship: friendship_list) {
            String friend_username = friendship.getFriendUsername();
            query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(friend_username));
            User friend = mongoTemplate.findOne(query, User.class);
            assert friend != null;
            if (friend.getNickname().equals(friend_nickname))
                result.add(friendship);
        }
        return result;
    }

    /** 根据备注查找自己的好友 */
    public List<Friendship> getFriendshipByRemark(String username, String friend_remark) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username)
                                    .and(KeyConstant.REMARK).is(friend_remark));
        return mongoTemplate.find(query, Friendship.class);
    }

    /** 查找自己所有的星标好友 */
    public List<Friendship> getAllStarFriends(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        return mongoTemplate.find(query, Friendship.class);
    }

    /** 增加好友申请 */
    public void createFriendRequest(String from_username, String to_username, String extra) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setFromUsername(from_username);
        friendRequest.setToUsername(to_username);
        friendRequest.setExtra(extra);
        friendRequest.setStatus(0);

        FriendRequest.SubObj subObj = new FriendRequest.SubObj();
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
        subObj.setTime(dateFormat.format(now));
        friendRequest.setSubObj(subObj);

        mongoTemplate.insert(friendRequest);
    }

    /** 删除好友 */
    public void removeFriend(String username, String friend_username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username)
                                    .and(KeyConstant.FRIEND_USERNAME).is(friend_username));
        mongoTemplate.remove(query, Friendship.class);
    }

    /** 设置星标好友 */
    public void setStarFriend(String username, String friend_username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username)
                                    .and(KeyConstant.FRIEND_USERNAME).is(friend_username));
        Update update = new Update();
        update.set(KeyConstant.STAR, true);
        mongoTemplate.upsert(query, update, Friendship.class);
    }

    /** 设置好友备注 */
    public void setFriendRemark(String username, String friend_username, String remark) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username)
                                    .and(KeyConstant.FRIEND_USERNAME).is(friend_username));
        Update update = new Update();
        update.set(KeyConstant.REMARK, remark);
        mongoTemplate.upsert(query, update, Friendship.class);
    }

    /** 查看好友申请 */
    public List<FriendRequest> getFriendRequest(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.TO_USERNAME).is(username));
        return mongoTemplate.find(query, FriendRequest.class);
    }

    /** 审核好友申请 */
    public void checkFriendRequest(String from_username, String to_username, boolean result) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.FROM_USERNAME).is(from_username)
                                    .and(KeyConstant.TO_USERNAME).is(to_username));
        Update update = new Update();
        if (result) {
            update.set(KeyConstant.STATUS, 1);
        }
        else {
            update.set(KeyConstant.STATUS, 2);
        }
        mongoTemplate.upsert(query, update, FriendRequest.class);
    }
}
