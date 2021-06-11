package com.tsinghua.course.Biz.Processor;

import com.tsinghua.course.Base.Constant.KeyConstant;
import com.tsinghua.course.Base.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @描述 动态原子处理器
 */
@Component
public class MomentProcessor {
    @Autowired
    MongoTemplate mongoTemplate;

    /** 发布文字动态 */
    public void publishTextMoment(String username, int type, String content) {
        Moment moment = new Moment();
        moment.setPublishTime(new Date());
        moment.setUsername(username);
        moment.setType(type);
        moment.setLikesNum(0);
        moment.setCommentsNum(0);
        moment.setTextContent(content);
        moment.setImagesPath(new String[0]);
        moment.setVideoPath("");

        mongoTemplate.insert(moment);
    }

    /** 发布图片动态 */
    public void publishImgMoment(String username, int type, String[] imgs) {
        Moment moment = new Moment();
        moment.setPublishTime(new Date());
        moment.setUsername(username);
        moment.setType(type);
        moment.setLikesNum(0);
        moment.setCommentsNum(0);
        moment.setTextContent("");
        moment.setImagesPath(imgs);
        moment.setVideoPath("");

        mongoTemplate.insert(moment);
    }

    /** 发布图文动态 */
    public void publishTextAndImgMoment(String username, int type, String content, String[] imgs) {
        Moment moment = new Moment();
        moment.setPublishTime(new Date());
        moment.setUsername(username);
        moment.setType(type);
        moment.setLikesNum(0);
        moment.setCommentsNum(0);
        moment.setTextContent(content);
        moment.setImagesPath(imgs);
        moment.setVideoPath("");

        mongoTemplate.insert(moment);
    }

    /** 发布视频动态 */
    public void publishVideoMoment(String username, int type, String video) {
        Moment moment = new Moment();
        moment.setPublishTime(new Date());
        moment.setUsername(username);
        moment.setType(type);
        moment.setLikesNum(0);
        moment.setCommentsNum(0);
        moment.setTextContent("");
        moment.setImagesPath(new String[0]);
        moment.setVideoPath(video);

        mongoTemplate.insert(moment);
    }

    /** 通过id获取动态 */
    public Moment getMoment(String momentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.ID).is(momentId));

        return mongoTemplate.findOne(query, Moment.class);
    }

    /** 删除动态 */
    public void removeMoment(String momentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.ID).is(momentId));

        mongoTemplate.remove(query, Moment.class);
    }

    /** 查找当前用户的所有好友（包括自己）的动态 */
    public List<Moment> getFriendMoments(String username) {
        /* 列出所有好友 */
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        List<Friendship> friendships = mongoTemplate.find(query, Friendship.class);

        /* 列出所有好友的动态 */
        List<Moment> momentList = new ArrayList<>();
        for (Friendship friend: friendships) {
            String friendUsername = friend.getFriendUsername();
            Query query1 = new Query();
            query1.addCriteria(Criteria.where(KeyConstant.USERNAME).is(friendUsername));
            momentList.addAll(mongoTemplate.find(query1, Moment.class));
        }

        /* 时间排序 */
        MomentSort(momentList);
        return momentList;
    }

    /** 动态时间排序 */
    private static void MomentSort(List<Moment> list) {
        Collections.sort(list, new Comparator<Moment>() {
            @Override
            public int compare(Moment o1, Moment o2) {
                Date dt1 = o1.getPublishTime();
                Date dt2 = o2.getPublishTime();
                if (dt1.getTime() < dt2.getTime())
                    return 1;
                else
                    return -1;
            }
        });
    }

    /** 获取一条动态的点赞用户 */
    public List<Like> getMomentLikes(String momentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.MOMENT_ID).is(momentId));

        return mongoTemplate.find(query, Like.class);
    }

    /** 获取一条动态的评论 */
    public List<Comment> getMomentComments(String momentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.MOMENT_ID).is(momentId));

        return mongoTemplate.find(query, Comment.class);
    }

    /** 新增点赞 */
    public void addLike(String username, String momentId) {
        Like like = new Like();
        like.setLikeTime(new Date());
        like.setMomentId(momentId);
        like.setUsername(username);

        mongoTemplate.insert(like);
    }

    /** 删除点赞 */
    public void removeLike(String likeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.ID).is(likeId));

        mongoTemplate.remove(query, Like.class);
    }

    /** 更新点赞数 */
    public void updateLikesNum(String momentId, boolean like) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.ID).is(momentId));
        Moment moment = mongoTemplate.findOne(query, Moment.class);
        int oldLikesNum = moment.getLikesNum();
        Update update = new Update();
        if (like) {
            update.set(KeyConstant.LIKES_NUM, oldLikesNum + 1);
        }
        else {
            update.set(KeyConstant.LIKES_NUM, oldLikesNum - 1);
        }

        mongoTemplate.upsert(query, update, Moment.class);
    }

    /** 新增评论 */
    public void addComment(String username, String momentId, String content) {
        Comment comment = new Comment();
        comment.setCommentTime(new Date());
        comment.setMomentId(momentId);
        comment.setContent(content);
        comment.setUsername(username);

        mongoTemplate.insert(comment);
    }

    /** 删除评论 */
    public void removeComment(String commentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.ID).is(commentId));

        mongoTemplate.remove(query, Comment.class);
    }

    /** 更新评论数 */
    public void updateCommentsNum(String momentId, boolean comment) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.ID).is(momentId));
        Moment moment = mongoTemplate.findOne(query, Moment.class);
        int oldCommentsNum = moment.getCommentsNum();
        Update update = new Update();
        if (comment) {
            update.set(KeyConstant.COMMENTS_NUM, oldCommentsNum + 1);
        }
        else {
            update.set(KeyConstant.COMMENTS_NUM, oldCommentsNum - 1);
        }
        mongoTemplate.upsert(query, update, Moment.class);
    }
}
