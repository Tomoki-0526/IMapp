package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Base.CustomizedClass.CommentItem;
import com.tsinghua.course.Base.CustomizedClass.LikeItem;
import com.tsinghua.course.Base.CustomizedClass.MomentItem;
import com.tsinghua.course.Base.Error.CourseWarn;
import com.tsinghua.course.Base.Error.UserWarnEnum;
import com.tsinghua.course.Base.Model.*;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;
import com.tsinghua.course.Biz.Controller.Params.MomentParams.In.*;
import com.tsinghua.course.Biz.Controller.Params.MomentParams.Out.CommentOnMomentWsOutParams;
import com.tsinghua.course.Biz.Controller.Params.MomentParams.Out.GetMomentsOutParams;
import com.tsinghua.course.Biz.Controller.Params.MomentParams.Out.GetSingleMomentOutParams;
import com.tsinghua.course.Biz.Controller.Params.MomentParams.Out.LikeMomentWsOutParams;
import com.tsinghua.course.Biz.Processor.FriendProcessor;
import com.tsinghua.course.Biz.Processor.MomentProcessor;
import com.tsinghua.course.Biz.Processor.UserProcessor;
import com.tsinghua.course.Frame.Util.FileUtil;
import com.tsinghua.course.Frame.Util.SocketUtil;
import io.netty.handler.codec.http.multipart.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.tsinghua.course.Base.Constant.GlobalConstant.*;
import static com.tsinghua.course.Base.Constant.NameConstant.OS_NAME;
import static com.tsinghua.course.Base.Constant.NameConstant.WIN;

/**
 * @描述 动态控制器
 */
@Component
public class MomentController {

    @Autowired
    UserProcessor userProcessor;
    @Autowired
    FriendProcessor friendProcessor;
    @Autowired
    MomentProcessor momentProcessor;

    /** 发布动态 */
    @BizType(BizTypeEnum.MOMENT_PUBLISH_MOMENT)
    @NeedLogin
    public CommonOutParams momentPublishMoment(PublishMomentInParams inParams) throws Exception {
        String username = inParams.getUsername();
        int type = Integer.parseInt(inParams.getType());

        /* 根据动态类型不同获取字段 */
        // 根据Windows和Linux配置不同的头像保存路径
        String OSName = System.getProperty(OS_NAME);
        String momentPath = OSName.toLowerCase().startsWith(WIN) ? WINDOWS_MOMENT_PATH : LINUX_MOMENT_PATH;
        File dir = new File(momentPath);
        if (!dir.exists())
            dir.mkdirs();
        // 文本
        if (type == 0) {
            String content = inParams.getContent();
            momentProcessor.publishTextMoment(username, type, content);
        }
        // 图片
        else if (type == 1) {
            FileUpload[] images = inParams.getImages();
            List<String> imgPathList = new ArrayList<>();
            for (FileUpload fileUpload : images) {
                String imgPath = FileUtil.fileUploadToFile(fileUpload, momentPath);
                imgPathList.add(imgPath);
            }
            String[] imgs = new String[imgPathList.size()];
            imgPathList.toArray(imgs);

            momentProcessor.publishImgMoment(username, type, imgs);
        }
        // 图文
        else if (type == 2) {
            String content = inParams.getContent();
            FileUpload[] images = inParams.getImages();
            List<String> imgPathList = new ArrayList<>();
            for (FileUpload fileUpload : images) {
                String imgPath = FileUtil.fileUploadToFile(fileUpload, momentPath);
                imgPathList.add(imgPath);
            }
            String[] imgs = new String[imgPathList.size()];
            imgPathList.toArray(imgs);

            momentProcessor.publishTextAndImgMoment(username, type, content, imgs);
        }
        // 视频
        else if (type == 3) {
            FileUpload fileUpload = inParams.getVideo();
            String video = FileUtil.fileUploadToFile(fileUpload, momentPath);
            momentProcessor.publishVideoMoment(username, type, video);
        }

        return new CommonOutParams(true);
    }

    /** 删除动态 */
    @BizType(BizTypeEnum.MOMENT_REMOVE_MOMENT)
    @NeedLogin
    public CommonOutParams momentRemoveMoment(RemoveMomentInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String momentId = inParams.getMomentId();

        Moment moment = momentProcessor.getMoment(momentId);
        if (moment == null)
            throw new CourseWarn(UserWarnEnum.NO_SUCH_MOMENT);
        if (!moment.getUsername().equals(username))
            throw new CourseWarn(UserWarnEnum.MOMENT_USER_UNMATCHED);

        /* 在删除数据之前先删除文件 */
        int momentType = moment.getType();
        if (momentType == 1 || momentType == 2) {
            String[] imagesPath = moment.getImagesPath();
            for (String imagePath : imagesPath) {
                File file = new File(imagePath);
                file.delete();
            }
        }
        else if(momentType == 3) {
            String videoPath = moment.getVideoPath();
            File file = new File(videoPath);
            file.delete();
        }

        momentProcessor.removeLikesOfMoment(momentId);
        momentProcessor.removeCommentsOnMoment(momentId);
        momentProcessor.removeMoment(momentId);
        return new CommonOutParams(true);
    }

    /** 查看动态列表 */
    @BizType(BizTypeEnum.MOMENT_GET_MOMENTS)
    @NeedLogin
    public GetMomentsOutParams momentGetMoments(CommonInParams inParams) throws Exception {
        String username = inParams.getUsername();
        GetMomentsOutParams outParams = new GetMomentsOutParams();

        /* 找到当前用户所有好友（包括自己）的动态 */
        List<Moment> momentList = momentProcessor.getFriendMoments(username);
        /* myMomentsSet存放已经找过的自己发过的动态，进行去重 */
        Set<String> myMomentsSet = new HashSet<>();

        /* 组织数据 */
        List<MomentItem> momentItemList = new ArrayList<>();
        for (Moment moment: momentList) {
            if (myMomentsSet.contains(moment.getId()))
                continue;
            MomentItem momentItem = new MomentItem();
            String publisherUsername = moment.getUsername();
            if (publisherUsername.equals(username))
                myMomentsSet.add(moment.getId());
            User publisher = userProcessor.getUserByUsername(publisherUsername);
            Friendship friendship = friendProcessor.getFriendshipByUsername(username, publisherUsername);
            // 动态id
            String momentId = moment.getId();
            momentItem.setMomentId(momentId);
            // 头像
            String avatar = publisher.getAvatar();
            int index = avatar.indexOf(AVATAR_RELATIVE_PATH);
            String avatarUrl = FILE_URL + avatar.substring(index);
            momentItem.setAvatar(avatarUrl);
            // 发布者用户名
            momentItem.setUsername(publisherUsername);
            // 发布者昵称
            momentItem.setNickname(publisher.getNickname());
            // 发布者备注
            momentItem.setRemark(friendship.getRemark());
            // 发布时间
            Date publishTime = moment.getPublishTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            String publishTimeStr = dateFormat.format(publishTime);
            momentItem.setPublishTime(publishTimeStr);
            // 动态类型
            momentItem.setType(moment.getType());
            // 文本内容
            momentItem.setTextContent(moment.getTextContent());
            // 图片数组
            String[] imagesPath = moment.getImagesPath();
            for (int i = 0; i < imagesPath.length; ++i) {
                index = imagesPath[i].indexOf(MOMENT_RELATIVE_PATH);
                imagesPath[i] = FILE_URL + imagesPath[i].substring(index);
            }
            momentItem.setImages(imagesPath);
            // 视频
            String videoPath = moment.getVideoPath();
            momentItem.setVideo("");
            if (!videoPath.equals("")) {
                index = videoPath.indexOf(MOMENT_RELATIVE_PATH);
                videoPath = FILE_URL + videoPath.substring(index);
                momentItem.setVideo(videoPath);
            }
            // 点赞数
            momentItem.setLikesNum(moment.getLikesNum());
            // 点赞用户数组
            List<Like> likeList = momentProcessor.getMomentLikes(momentId);
            List<LikeItem> likeItemList = new ArrayList<>();
            for (Like like: likeList) {
                LikeItem likeItem = new LikeItem();
                String likeUsername = like.getUsername();
                User likeUser = userProcessor.getUserByUsername(likeUsername);
                avatar = likeUser.getAvatar();
                index = avatar.indexOf(AVATAR_RELATIVE_PATH);
                avatarUrl = FILE_URL + avatar.substring(index);
                likeItem.setLikeId(like.getId());
                likeItem.setLikeAvatar(avatarUrl);
                likeItem.setLikeUsername(likeUsername);
                likeItem.setLikeNickname(likeUser.getNickname());
                likeItem.setLikeTime(dateFormat.format(like.getLikeTime()));
                likeItem.setLikeRemark("");
                likeItem.setFriend(false);
                // 判断这个人是不是本用户的好友
                // 如果是的话获取备注
                Friendship friendship1 = friendProcessor.getFriendshipByUsername(username, likeUsername);
                if (friendship1 != null) {
                    likeItem.setLikeRemark(friendship1.getRemark());
                    likeItem.setFriend(true);
                }
                likeItemList.add(likeItem);
            }
            LikeItem[] likes = new LikeItem[likeItemList.size()];
            likeItemList.toArray(likes);
            momentItem.setLikes(likes);
            // 评论数
            momentItem.setCommentsNum(moment.getCommentsNum());
            // 评论用户数组
            List<Comment> commentList = momentProcessor.getMomentComments(momentId);
            List<CommentItem> commentItemList = new ArrayList<>();
            for (Comment comment: commentList) {
                CommentItem commentItem = new CommentItem();
                String commentUsername = comment.getUsername();
                User commentUser = userProcessor.getUserByUsername(commentUsername);
                commentItem.setCommentId(comment.getId());
                commentItem.setCommentUsername(commentUsername);
                commentItem.setCommentNickname(commentUser.getNickname());
                commentItem.setCommentContent(comment.getContent());
                commentItem.setCommentTime(dateFormat.format(comment.getCommentTime()));
                commentItem.setCommentRemark("");
                commentItem.setFriend(false);
                // 判断这个人是不是本用户的好友
                // 如果是的话获取备注
                Friendship friendship1 = friendProcessor.getFriendshipByUsername(username, commentUsername);
                if (friendship1 != null) {
                    commentItem.setCommentRemark(friendship1.getRemark());
                    commentItem.setFriend(true);
                }
                commentItemList.add(commentItem);
            }
            CommentItem[] comments = new CommentItem[commentItemList.size()];
            commentItemList.toArray(comments);
            momentItem.setComments(comments);

            // 加入item数组
            momentItemList.add(momentItem);
        }
        MomentItem[] moments = new MomentItem[momentItemList.size()];
        momentItemList.toArray(moments);

        outParams.setMoments(moments);
        return outParams;
    }

    /** 点赞动态 */
    @BizType(BizTypeEnum.MOMENT_LIKE_MOMENT)
    @NeedLogin
    public CommonOutParams momentLikeMoment(LikeMomentInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String momentId = inParams.getMomentId();
        String momentUsername = inParams.getMomentUsername();

        /* 如果已经点过赞了直接返回 */
        Like like = momentProcessor.getLike(username, momentId);
        if (like != null) {
            throw new CourseWarn(UserWarnEnum.LIKE_EXISTS);
        }

        /* 新建Like对象 */
        momentProcessor.addLike(username, momentId);
        /* 更新点赞数 */
        momentProcessor.updateLikesNum(momentId, true);

        /* 定向发送给被点赞用户 */
        LikeMomentWsOutParams wsOutParams = new LikeMomentWsOutParams();
        User user = userProcessor.getUserByUsername(username);
        Friendship friendship = friendProcessor.getFriendshipByUsername(momentUsername, username);
        wsOutParams.setType(2);
        wsOutParams.setUsername(username);
        wsOutParams.setNickname(user.getNickname());
        wsOutParams.setRemark(friendship.getRemark());
        SocketUtil.sendMessageToUser(momentUsername, wsOutParams);

        return new CommonOutParams(true);
    }

    /** 撤销点赞 */
    @BizType(BizTypeEnum.MOMENT_CANCEL_LIKE_MOMENT)
    @NeedLogin
    public CommonOutParams momentCancelLikeMoment(CancelLikeMomentInParams inParams) throws Exception {
        String momentId = inParams.getMomentId();
        String likeId = inParams.getLikeId();
        String username = inParams.getUsername();

        /* 如果还没点过赞直接返回 */
        Like like = momentProcessor.getLike(username, momentId);
        if (like == null) {
            throw new CourseWarn(UserWarnEnum.LIKE_EXISTS);
        }

        /* 删除对应的Like对象 */
        momentProcessor.removeLike(likeId);
        /* 更新点赞数 */
        momentProcessor.updateLikesNum(momentId, false);

        return new CommonOutParams(true);
    }

    /** 评论动态 */
    @BizType(BizTypeEnum.MOMENT_COMMENT_ON_MOMENT)
    @NeedLogin
    public CommonOutParams momentCommentOnMoment(CommentOnMomentInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String momentId = inParams.getMomentId();
        String content = inParams.getContent();
        String momentUsername = inParams.getMomentUsername();

        /* 新建Comment对象 */
        momentProcessor.addComment(username, momentId, content);
        /* 更新评论数 */
        momentProcessor.updateCommentsNum(momentId, true);

        /* 及时推送 */
        CommentOnMomentWsOutParams wsOutParams = new CommentOnMomentWsOutParams();
        User user = userProcessor.getUserByUsername(username);
        Friendship friendship = friendProcessor.getFriendshipByUsername(momentUsername, username);
        wsOutParams.setType(3);
        wsOutParams.setUsername(username);
        wsOutParams.setNickname(user.getNickname());
        wsOutParams.setRemark(friendship.getRemark());
        wsOutParams.setContent(content);
        SocketUtil.sendMessageToUser(momentUsername, wsOutParams);

        return new CommonOutParams(true);
    }

    /** 删除动态 */
    @BizType(BizTypeEnum.MOMENT_REMOVE_COMMENT)
    @NeedLogin
    public CommonOutParams momentRemoveComment(RemoveCommentInParams inParams) throws Exception {
        String momentId = inParams.getMomentId();
        String commentId = inParams.getCommentId();

        /* 删除对应的Comment对象 */
        momentProcessor.removeComment(commentId);
        /* 更新点赞数 */
        momentProcessor.updateCommentsNum(momentId, false);

        return new CommonOutParams(true);
    }

    /** 查看一条动态 */
    @BizType(BizTypeEnum.MOMENT_GET_SINGLE_MOMENT)
    @NeedLogin
    public GetSingleMomentOutParams momentGetSingleMoment(GetSingleMomentInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String momentId = inParams.getMomentId();
        Moment moment = momentProcessor.getMoment(momentId);

        GetSingleMomentOutParams outParams = new GetSingleMomentOutParams();
        MomentItem momentItem = new MomentItem();
        String publisherUsername = moment.getUsername();
        User publisher = userProcessor.getUserByUsername(publisherUsername);
        Friendship friendship = friendProcessor.getFriendshipByUsername(username, publisherUsername);
        // 动态id
        momentItem.setMomentId(momentId);
        // 头像
        String avatar = publisher.getAvatar();
        int index = avatar.indexOf(AVATAR_RELATIVE_PATH);
        String avatarUrl = FILE_URL + avatar.substring(index);
        momentItem.setAvatar(avatarUrl);
        // 发布者用户名
        momentItem.setUsername(publisherUsername);
        // 发布者昵称
        momentItem.setNickname(publisher.getNickname());
        // 发布者备注
        momentItem.setRemark(friendship.getRemark());
        // 发布时间
        Date publishTime = moment.getPublishTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String publishTimeStr = dateFormat.format(publishTime);
        momentItem.setPublishTime(publishTimeStr);
        // 动态类型
        momentItem.setType(moment.getType());
        // 文本内容
        momentItem.setTextContent(moment.getTextContent());
        // 图片数组
        String[] imagesPath = moment.getImagesPath();
        for (int i = 0; i < imagesPath.length; ++i) {
            index = imagesPath[i].indexOf(MOMENT_RELATIVE_PATH);
            imagesPath[i] = FILE_URL + imagesPath[i].substring(index);
        }
        momentItem.setImages(imagesPath);
        // 视频
        String videoPath = moment.getVideoPath();
        momentItem.setVideo("");
        if (!videoPath.equals("")) {
            index = videoPath.indexOf(MOMENT_RELATIVE_PATH);
            videoPath = FILE_URL + videoPath.substring(index);
            momentItem.setVideo(videoPath);
        }
        // 点赞数
        momentItem.setLikesNum(moment.getLikesNum());
        // 点赞用户数组
        List<Like> likeList = momentProcessor.getMomentLikes(momentId);
        List<LikeItem> likeItemList = new ArrayList<>();
        for (Like like: likeList) {
            LikeItem likeItem = new LikeItem();
            String likeUsername = like.getUsername();
            User likeUser = userProcessor.getUserByUsername(likeUsername);
            avatar = likeUser.getAvatar();
            index = avatar.indexOf(AVATAR_RELATIVE_PATH);
            avatarUrl = FILE_URL + avatar.substring(index);
            likeItem.setLikeId(like.getId());
            likeItem.setLikeAvatar(avatarUrl);
            likeItem.setLikeUsername(likeUsername);
            likeItem.setLikeNickname(likeUser.getNickname());
            likeItem.setLikeTime(dateFormat.format(like.getLikeTime()));
            likeItem.setLikeRemark("");
            likeItem.setFriend(false);
            // 判断这个人是不是本用户的好友
            // 如果是的话获取备注
            Friendship friendship1 = friendProcessor.getFriendshipByUsername(username, likeUsername);
            if (friendship1 != null) {
                likeItem.setLikeRemark(friendship1.getRemark());
                likeItem.setFriend(true);
            }
            likeItemList.add(likeItem);
        }
        LikeItem[] likes = new LikeItem[likeItemList.size()];
        likeItemList.toArray(likes);
        momentItem.setLikes(likes);
        // 评论数
        momentItem.setCommentsNum(moment.getCommentsNum());
        // 评论用户数组
        List<Comment> commentList = momentProcessor.getMomentComments(momentId);
        List<CommentItem> commentItemList = new ArrayList<>();
        for (Comment comment: commentList) {
            CommentItem commentItem = new CommentItem();
            String commentUsername = comment.getUsername();
            User commentUser = userProcessor.getUserByUsername(commentUsername);
            commentItem.setCommentId(comment.getId());
            commentItem.setCommentUsername(commentUsername);
            commentItem.setCommentNickname(commentUser.getNickname());
            commentItem.setCommentContent(comment.getContent());
            commentItem.setCommentTime(dateFormat.format(comment.getCommentTime()));
            commentItem.setCommentRemark("");
            commentItem.setFriend(false);
            // 判断这个人是不是本用户的好友
            // 如果是的话获取备注
            Friendship friendship1 = friendProcessor.getFriendshipByUsername(username, commentUsername);
            if (friendship1 != null) {
                commentItem.setCommentRemark(friendship1.getRemark());
                commentItem.setFriend(true);
            }
            commentItemList.add(commentItem);
        }
        CommentItem[] comments = new CommentItem[commentItemList.size()];
        commentItemList.toArray(comments);
        momentItem.setComments(comments);

        outParams.setMoment(momentItem);
        return outParams;
    }
}