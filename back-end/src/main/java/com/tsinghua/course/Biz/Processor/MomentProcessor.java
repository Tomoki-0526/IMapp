package com.tsinghua.course.Biz.Processor;

import com.tsinghua.course.Base.Constant.KeyConstant;
import com.tsinghua.course.Base.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

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
        moment.setLikes(0);
        moment.setComments(0);
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
        moment.setLikes(0);
        moment.setComments(0);
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
        moment.setLikes(0);
        moment.setComments(0);
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
        moment.setLikes(0);
        moment.setComments(0);
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
}
