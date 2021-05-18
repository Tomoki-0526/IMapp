package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Base.CustomizedClass.FriendItem;
import com.tsinghua.course.Base.Model.FriendRequest;
import com.tsinghua.course.Base.Model.Friendship;
import com.tsinghua.course.Base.Model.User;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;
import com.tsinghua.course.Biz.Controller.Params.FriendParams.in.*;
import com.tsinghua.course.Biz.Controller.Params.FriendParams.out.*;
import com.tsinghua.course.Biz.Processor.FriendProcessor;
import com.tsinghua.course.Biz.Processor.UserProcessor;
import com.tsinghua.course.Frame.Util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tsinghua.course.Base.Constant.GlobalConstant.BIRTHDAY_PATTERN;

/**
 * @描述 好友控制器，用于执行通讯录相关的业务
 **/
@Component
public class FriendController {

    @Autowired
    UserProcessor userProcessor;
    @Autowired
    FriendProcessor friendProcessor;

    /** 搜索陌生人 */
    @BizType(BizTypeEnum.FRIEND_FIND_STRANGER)
    @NeedLogin
    public FindStrangerOutParams friendFindStranger(FindStrangerInParams inParams) throws Exception {
        String stranger_username = inParams.getStrangerUsername();
        User stranger = userProcessor.getUserByUsername(stranger_username);

        /* 如果没找到该用户、找到自己或者好友，均视为没有找到结果 */
        String username = inParams.getUsername();
        Friendship friendship = friendProcessor.getFriendshipByUsername(username, stranger_username);
        if (stranger == null || stranger.getUsername().equals(username) || friendship != null) {
            FindStrangerOutParams outParams = new FindStrangerOutParams(false);
            outParams.setStrangerUsername("");
            outParams.setStrangerNickname("");
            outParams.setStrangerAvatar("");
            outParams.setExtraInfo("未找到结果");
            return outParams;
        }
        String nickname = stranger.getNickname();
        String avatar = stranger.getAvatar();

        FindStrangerOutParams outParams = new FindStrangerOutParams();
        outParams.setStrangerUsername(stranger_username);
        outParams.setStrangerNickname(nickname);
        outParams.setStrangerAvatar(avatar);
        outParams.setExtraInfo("查找成功");

        return outParams;
    }

    /** 获取陌生人信息 */
    @BizType(BizTypeEnum.FRIEND_GET_STRANGER_INFO)
    @NeedLogin
    public GetStrangerInfoOutParams friendGetStrangerInfo(GetStrangerInfoInParams inParams) throws Exception {
        String stranger_username = inParams.getStrangerUsername();
        User stranger = userProcessor.getUserByUsername(stranger_username);

        String stranger_nickname = stranger.getNickname();
        String stranger_avatar = stranger.getAvatar();
        String stranger_gender = stranger.getGender();
        String stranger_signature = stranger.getSignature();

        GetStrangerInfoOutParams outParams = new GetStrangerInfoOutParams();
        outParams.setStrangerUsername(stranger_username);
        outParams.setStrangerNickname(stranger_nickname);
        outParams.setStrangerAvatar(stranger_avatar);
        outParams.setStrangerGender(stranger_gender);
        outParams.setStrangerSignature(stranger_signature);

        return outParams;
    }

    /** 搜索好友 */
    @BizType(BizTypeEnum.FRIEND_FIND_FRIEND)
    @NeedLogin
    public FindFriendOutParams friendFindFriend(FindFriendInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String content = inParams.getContent();

        List<Friendship> friendship_list = new ArrayList<>();
        /* 根据用户名查找 */
        Friendship friendship_by_username = friendProcessor.getFriendshipByUsername(username, content);
        if (friendship_by_username != null)
            friendship_list.add(friendship_by_username);
        /* 根据昵称查找 */
        friendship_list.addAll(friendProcessor.getFriendshipByNickname(username, content));
        /* 根据备注查找 */
        friendship_list.addAll(friendProcessor.getFriendshipByRemark(username, content));

        if (friendship_list.isEmpty()) {
            FindFriendOutParams outParams = new FindFriendOutParams();
            outParams.setFriends(new FriendItem[0]);
            outParams.setExtraInfo("未找到结果");
            return outParams;
        }
        else {
            List<FriendItem> temp = new ArrayList<>();
            for (Friendship friendship: friendship_list) {
                String friend_username = friendship.getFriendUsername();
                String friend_remark = friendship.getRemark();
                User friend = userProcessor.getUserByUsername(friend_username);
                String friend_nickname = friend.getNickname();
                String friend_avatar = friend.getAvatar();

                FriendItem item = new FriendItem();
                item.setFriendUsername(friend_username);
                item.setFriendAvatar(friend_avatar);
                item.setFriendNickname(friend_nickname);
                item.setFriendRemark(friend_remark);

                temp.add(item);
            }
            FriendItem[] result = new FriendItem[temp.size()];
            temp.toArray(result);

            FindFriendOutParams outParams = new FindFriendOutParams();
            outParams.setFriends(result);
            outParams.setExtraInfo("查找成功");
            return outParams;
        }
    }

    /** 查看星标好友 */
    @BizType(BizTypeEnum.FRIEND_GET_STAR_FRIENDS)
    @NeedLogin
    public GetStarFriendsOutParams friendGetStarFriends(CommonInParams inParams) throws Exception {
        String username = inParams.getUsername();
        List<Friendship> starFriendsList = friendProcessor.getAllStarFriends(username);
        if (starFriendsList.size() == 0) {
            GetStarFriendsOutParams outParams = new GetStarFriendsOutParams(false);
            outParams.setStarFriendsList(new FriendItem[0]);
            outParams.setExtraInfo("没有星标好友");
            return outParams;
        }
        List<FriendItem> temp = new ArrayList<>();
        for (Friendship starFriend: starFriendsList) {
            String friend_username = starFriend.getFriendUsername();
            String friend_remark = starFriend.getRemark();
            User friend = userProcessor.getUserByUsername(friend_username);
            String friend_avatar = friend.getAvatar();
            String friend_nickname = friend.getNickname();

            FriendItem friendItem = new FriendItem();
            friendItem.setFriendUsername(friend_username);
            friendItem.setFriendAvatar(friend_avatar);
            friendItem.setFriendNickname(friend_nickname);
            friendItem.setFriendRemark(friend_remark);

            temp.add(friendItem);
        }
        FriendItem[] result = new FriendItem[temp.size()];
        temp.toArray(result);
        GetStarFriendsOutParams outParams = new GetStarFriendsOutParams();
        outParams.setStarFriendsList(result);
        outParams.setExtraInfo("查找成功");
        return outParams;
    }

    /** 查看某一位好友的信息 */
    @BizType(BizTypeEnum.FRIEND_GET_FRIEND_INFO)
    @NeedLogin
    public GetFriendInfoOutParams friendGetFriendInfo(GetFriendInfoInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String friend_username = inParams.getFriendUsername();
        User friend = userProcessor.getUserByUsername(friend_username);
        Friendship friendship = friendProcessor.getFriendshipByUsername(username, friend_username);

        String avatar = friend.getAvatar();
        String nickname = friend.getNickname();
        String remark = friendship.getRemark();
        String gender = friend.getGender();
        int age = friend.getAge();
        String telephone = friend.getTelephone();
        String group = friendship.getGroup();
        boolean star = friendship.isStar();
        String signature = friend.getSignature();

        Date birthday = friend.getBirthday();
        SimpleDateFormat dateFormat = new SimpleDateFormat(BIRTHDAY_PATTERN);
        String birthday_str = dateFormat.format(birthday);

        GetFriendInfoOutParams outParams = new GetFriendInfoOutParams();
        outParams.setAge(age);
        outParams.setAvatar(avatar);
        outParams.setBirthday(birthday_str);
        outParams.setGender(gender);
        outParams.setNickname(nickname);
        outParams.setUsername(friend_username);
        outParams.setRemark(remark);
        outParams.setTelephone(telephone);
        outParams.setGroup(group);
        outParams.setStar(star);
        outParams.setSignature(signature);

        return outParams;
    }

    /** 好友申请 */
    @BizType(BizTypeEnum.FRIEND_NEW_FRIEND_REQUEST)
    @NeedLogin
    public CommonOutParams friendNewFriendRequest(NewFriendRequestInParams inParams) throws Exception {
        /* 添加申请到数据库 */
        String username = inParams.getUsername();
        String to_username = inParams.getToUsername();
        String extra = inParams.getExtra();
        friendProcessor.createFriendRequest(username, to_username, extra);

        // TODO
        /* 调用websocket给接收者客户端定向发送消息 */

        return new CommonOutParams(true);
    }

    /** 删除好友 */
    @BizType(BizTypeEnum.FRIEND_REMOVE_FRIEND)
    @NeedLogin
    public CommonOutParams friendRemoveFriend(RemoveFriendInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String friend_username = inParams.getFriendUsername();
        friendProcessor.removeFriend(username, friend_username);

        return new CommonOutParams(true);
    }

    /** 设置星标好友 */
    @BizType(BizTypeEnum.FRIEND_SET_STAR_FRIEND)
    @NeedLogin
    public CommonOutParams friendSetStarFriend(SetStarFriendInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String friend_username = inParams.getFriendUsername();
        friendProcessor.setStarFriend(username, friend_username);

        return new CommonOutParams(true);
    }

    /** 设置好友备注 */
    @BizType(BizTypeEnum.FRIEND_SET_FRIEND_REMARK)
    @NeedLogin
    public CommonOutParams friendSetFriendRemark(SetFriendRemarkInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String friend_username = inParams.getFriendUsername();
        String remark = inParams.getRemark();
        friendProcessor.setFriendRemark(username, friend_username, remark);

        return new CommonOutParams(true);
    }
}
