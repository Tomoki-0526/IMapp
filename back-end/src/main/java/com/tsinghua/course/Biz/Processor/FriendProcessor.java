package com.tsinghua.course.Biz.Processor;

import com.tsinghua.course.Base.Constant.KeyConstant;
import com.tsinghua.course.Base.Model.FriendRequest;
import com.tsinghua.course.Base.Model.Friendship;
import com.tsinghua.course.Base.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

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

    /** 查找自己所有的星标好友 */
    public List<Friendship> getAllStarFriends(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username)
                                    .and(KeyConstant.STAR).is(true));
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

    /** 删除好友（单向） */
    public void removeFriend(String username, String friend_username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username)
                                    .and(KeyConstant.FRIEND_USERNAME).is(friend_username));
        mongoTemplate.remove(query, Friendship.class);
    }

    /** 设置星标好友 */
    public void setStarFriend(String username, String friend_username, boolean star) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username)
                                    .and(KeyConstant.FRIEND_USERNAME).is(friend_username));
        Update update = new Update();
        update.set(KeyConstant.STAR, star);
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

    /** 添加好友关系 */
    public void addFriendship(String username, String friend_username) {
        Friendship friendship = new Friendship();
        friendship.setUsername(username);
        friendship.setFriendUsername(friend_username);
        friendship.setRemark("");
        friendship.setStar(false);

//        FriendGroup friendGroup = getGroupByUsernameAndGroupName(username, DEFAULT_GROUP);
//        friendship.setGroupID(friendGroup.getId());

        Friendship.SubObj subObj = new Friendship.SubObj();
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
        subObj.setTime(dateFormat.format(now));
        friendship.setSubObj(subObj);

        mongoTemplate.insert(friendship);
    }

    /** 删除好友请求 */
    public void removeFriendRequest(String from_username, String to_username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.FROM_USERNAME).is(from_username)
                                    .and(KeyConstant.TO_USERNAME).is(to_username));
        mongoTemplate.remove(query, FriendRequest.class);
    }

    /** 更新好友请求状态（双向） */
    public void updateFriendRequestStatus(String from_username, String to_username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.FROM_USERNAME).is(from_username)
                                    .and(KeyConstant.TO_USERNAME).is(to_username));
        List<FriendRequest> friendRequestList = mongoTemplate.find(query, FriendRequest.class);
        if (!friendRequestList.isEmpty()) {
            Update update = new Update();
            update.set(KeyConstant.STATUS, 2);
            mongoTemplate.upsert(query, update, FriendRequest.class);
        }
    }

    /** 根据用户名搜索好友（模糊） */
    public List<Friendship> getFriendsByUsernameFuzzy(String username, String friendUsername) {
        Pattern pattern = Pattern.compile("^.*" + friendUsername + ".*$", Pattern.CASE_INSENSITIVE);
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username)
                                    .and(KeyConstant.FRIEND_USERNAME).regex(pattern));

        return mongoTemplate.find(query, Friendship.class);
    }

    /** 根据备注搜索好友（模糊） */
    public List<Friendship> getFriendsByRemarkFuzzy(String username, String remark) {
        Pattern pattern = Pattern.compile("^.*" + remark + ".*$", Pattern.CASE_INSENSITIVE);
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username)
                .and(KeyConstant.REMARK).regex(pattern));

        return mongoTemplate.find(query, Friendship.class);
    }

    /** 根据昵称搜索好友（模糊） */
    public List<Friendship> getFriendsByNicknameFuzzy(String username, String nickname) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        List<Friendship> friendshipList = mongoTemplate.find(query, Friendship.class);
        List<Friendship> result = new ArrayList<>();
        for (Friendship friendship: friendshipList) {
            String friend_username = friendship.getFriendUsername();
            Query query1 = new Query();
            query1.addCriteria(Criteria.where(KeyConstant.USERNAME).is(friend_username));
            User friend = mongoTemplate.findOne(query1, User.class);
            String realNickname = friend.getNickname();
            if (realNickname.contains(nickname)) {
                result.add(friendship);
            }
        }
        return result;
    }

//    /** 添加分组 */
//    public void addFriendGroup(String username, String group_name) {
//        FriendGroup friendGroup = new FriendGroup();
//        friendGroup.setUsername(username);
//        friendGroup.setGroupName(group_name);
//
//        FriendGroup.SubObj subObj = new FriendGroup.SubObj();
//        Date now = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
//        subObj.setTime(dateFormat.format(now));
//        friendGroup.setSubObj(subObj);
//
//        mongoTemplate.insert(friendGroup);
//    }

//    /** 添加默认分组 */
//    public void addDefaultFriendGroup(String username) {
//        FriendGroup friendGroup = new FriendGroup();
//        friendGroup.setUsername(username);
//        friendGroup.setGroupName(DEFAULT_GROUP);
//
//        FriendGroup.SubObj subObj = new FriendGroup.SubObj();
//        Date now = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
//        subObj.setTime(dateFormat.format(now));
//        friendGroup.setSubObj(subObj);
//
//        mongoTemplate.insert(friendGroup);
//    }

//    /** 将好友添加到分组 */
//    public void addFriendToGroup(String username, String friend_username, String group_name) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username)
//                                    .and(KeyConstant.GROUP_NAME).is(group_name));
//        FriendGroup friendGroup = mongoTemplate.findOne(query, FriendGroup.class);
//
//        Query query1 = new Query();
//        query1.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username)
//                                    .and(KeyConstant.FRIEND_USERNAME).is(friend_username));
//        Update update = new Update();
//        update.set(KeyConstant.GROUP_ID, friendGroup.getId());
//        mongoTemplate.upsert(query1, update, Friendship.class);
//    }

//    /** 获取所有分组 */
//    public List<FriendGroup> getAllGroups(String username) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
//        return mongoTemplate.find(query, FriendGroup.class);
//    }

//    /** 根据用户名和分组名查找分组 */
//    public FriendGroup getGroupByUsernameAndGroupName(String username, String group_name) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username)
//                                    .and(KeyConstant.GROUP_NAME).is(group_name));
//        return mongoTemplate.findOne(query, FriendGroup.class);
//    }

//    /** 根据ID查找分组 */
//    public FriendGroup getGroupByID(String id) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where(KeyConstant.ID).is(id));
//        return mongoTemplate.findOne(query, FriendGroup.class);
//    }

//    /** 修改分组名 */
//    public void setGroupName(String username, String old_group_name, String new_group_name) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where(KeyConstant.GROUP_NAME).is(old_group_name)
//                                    .and(KeyConstant.USERNAME).is(username));
//        Update update = new Update();
//        update.set(KeyConstant.GROUP_NAME, new_group_name);
//        mongoTemplate.upsert(query, update, FriendGroup.class);
//    }
}
