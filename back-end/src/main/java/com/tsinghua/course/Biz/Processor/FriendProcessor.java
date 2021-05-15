package com.tsinghua.course.Biz.Processor;

import com.tsinghua.course.Base.Constant.KeyConstant;
import com.tsinghua.course.Base.Model.Friendship;
import com.tsinghua.course.Base.Model.User;
import com.tsinghua.course.Biz.Controller.Params.FriendParams.out.FindFriendOutParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

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
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username).
                                    and(KeyConstant.FRIEND_USERNAME).is(friend_username));
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
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username).and(KeyConstant.REMARK).is(friend_remark));
        return mongoTemplate.find(query, Friendship.class);
    }
}
