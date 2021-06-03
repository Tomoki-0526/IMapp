package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Base.CustomizedClass.FriendItem;
import com.tsinghua.course.Base.CustomizedClass.FriendRequestItem;
import com.tsinghua.course.Base.Error.CourseWarn;
import com.tsinghua.course.Base.Error.UserWarnEnum;
import com.tsinghua.course.Base.Model.*;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;
import com.tsinghua.course.Biz.Controller.Params.FriendParams.in.*;
import com.tsinghua.course.Biz.Controller.Params.FriendParams.out.*;
import com.tsinghua.course.Biz.Processor.ChatProcessor;
import com.tsinghua.course.Biz.Processor.FriendProcessor;
import com.tsinghua.course.Biz.Processor.UserProcessor;
import com.tsinghua.course.Frame.Util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.tsinghua.course.Base.Constant.GlobalConstant.*;

/**
 * @描述 好友控制器，用于执行通讯录相关的业务
 **/
@Component
public class FriendController {

    @Autowired
    UserProcessor userProcessor;
    @Autowired
    FriendProcessor friendProcessor;
    @Autowired
    ChatProcessor chatProcessor;

    /** 搜索陌生人 */
    @BizType(BizTypeEnum.FRIEND_FIND_STRANGER)
    @NeedLogin
    public FindStrangerOutParams friendFindStranger(FindStrangerInParams inParams) throws Exception {
        String stranger_username = inParams.getStrangerUsername();
        User stranger = userProcessor.getUserByUsername(stranger_username);

        /* 如果没找到该用户、找到自己或者好友，均视为没有找到结果 */
        String username = inParams.getUsername();
        Friendship friendship = friendProcessor.getFriendshipByUsername(username, stranger_username);
        if (stranger == null) {
            throw new CourseWarn(UserWarnEnum.FIND_STRANGER_NO_RESULT);
        }
        if (stranger.getUsername().equals(username) || friendship != null) {
            throw new CourseWarn(UserWarnEnum.ALREADY_FRIEND);
        }
        String nickname = stranger.getNickname();
        String avatar = stranger.getAvatar();
        int index = avatar.indexOf(AVATAR_RELATIVE_PATH);
        String avatar_url = FILE_URL + avatar.substring(index);

        FindStrangerOutParams outParams = new FindStrangerOutParams();
        outParams.setStrangerUsername(stranger_username);
        outParams.setStrangerNickname(nickname);
        outParams.setStrangerAvatar(avatar_url);

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
        int index = stranger_avatar.indexOf(AVATAR_RELATIVE_PATH);
        String stranger_avatar_url = FILE_URL + stranger_avatar.substring(index);
        String stranger_gender = stranger.getGender();
        String stranger_signature = stranger.getSignature();

        GetStrangerInfoOutParams outParams = new GetStrangerInfoOutParams();
        outParams.setStrangerUsername(stranger_username);
        outParams.setStrangerNickname(stranger_nickname);
        outParams.setStrangerAvatar(stranger_avatar_url);
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
        friendship_list.addAll(friendProcessor.getFriendsByUsernameFuzzy(username, content));
        /* 根据备注查找 */
        friendship_list.addAll(friendProcessor.getFriendsByRemarkFuzzy(username, content));
        /* 根据昵称查找 */
        friendship_list.addAll(friendProcessor.getFriendsByNicknameFuzzy(username, content));
        /* 去重 */
        List<Friendship> friendshipWithoutDuplicates = new ArrayList<>();
        Set<String> set = new HashSet<String>();
        for (Friendship friendship: friendship_list) {
            if (set.add(friendship.getFriendUsername()))
                friendshipWithoutDuplicates.add(friendship);
        }

        if (friendship_list.isEmpty()) {
            throw new CourseWarn(UserWarnEnum.FIND_FRIEND_NO_RESULT);
        }
        else {
            List<FriendItem> temp = new ArrayList<>();
            for (Friendship friendship: friendshipWithoutDuplicates) {
                String friend_username = friendship.getFriendUsername();
                String friend_remark = friendship.getRemark();
                User friend = userProcessor.getUserByUsername(friend_username);
                String friend_nickname = friend.getNickname();
                String friend_avatar = friend.getAvatar();
                int index = friend_avatar.indexOf(AVATAR_RELATIVE_PATH);
                String friend_avatar_url = FILE_URL + friend_avatar.substring(index);

                FriendItem item = new FriendItem();
                item.setFriendUsername(friend_username);
                item.setFriendAvatar(friend_avatar_url);
                item.setFriendNickname(friend_nickname);
                item.setFriendRemark(friend_remark);

                temp.add(item);
            }
            FriendItem[] result = new FriendItem[temp.size()];
            temp.toArray(result);

            FindFriendOutParams outParams = new FindFriendOutParams();
            outParams.setFriends(result);
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
            throw new CourseWarn(UserWarnEnum.NO_STAR_FRIENDS);
        }
        List<FriendItem> temp = new ArrayList<>();
        for (Friendship starFriend: starFriendsList) {
            String friend_username = starFriend.getFriendUsername();
            String friend_remark = starFriend.getRemark();
            User friend = userProcessor.getUserByUsername(friend_username);
            String friend_avatar = friend.getAvatar();
            int index = friend_avatar.indexOf(AVATAR_RELATIVE_PATH);
            String friend_avatar_url = FILE_URL + friend_avatar.substring(index);
            String friend_nickname = friend.getNickname();

            FriendItem friendItem = new FriendItem();
            friendItem.setFriendUsername(friend_username);
            friendItem.setFriendAvatar(friend_avatar_url);
            friendItem.setFriendNickname(friend_nickname);
            friendItem.setFriendRemark(friend_remark);

            temp.add(friendItem);
        }
        FriendItem[] result = new FriendItem[temp.size()];
        temp.toArray(result);
        GetStarFriendsOutParams outParams = new GetStarFriendsOutParams();
        outParams.setStarFriendsList(result);
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
        int index = avatar.indexOf(AVATAR_RELATIVE_PATH);
        String avatar_url = FILE_URL + avatar.substring(index);
        String nickname = friend.getNickname();
        String remark = friendship.getRemark();
        String gender = friend.getGender();
        int age = friend.getAge();
        String telephone = friend.getTelephone();

        boolean star = friendship.isStar();
        String signature = friend.getSignature();

        Date birthday = friend.getBirthday();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        String birthday_str = dateFormat.format(birthday);

        GetFriendInfoOutParams outParams = new GetFriendInfoOutParams();
        outParams.setAge(age);
        outParams.setAvatar(avatar_url);
        outParams.setBirthday(birthday_str);
        outParams.setGender(gender);
        outParams.setNickname(nickname);
        outParams.setUsername(friend_username);
        outParams.setRemark(remark);
        outParams.setTelephone(telephone);
        outParams.setSignature(signature);
        outParams.setStar(star);

        return outParams;
    }

    /** 好友申请 */
    @BizType(BizTypeEnum.FRIEND_NEW_FRIEND_REQUEST)
    @NeedLogin
    public CommonOutParams friendNewFriendRequest(NewFriendRequestInParams inParams) throws Exception {
        /* 添加申请到数据库 */
        String from_username = inParams.getUsername();
        String to_username = inParams.getToUsername();
        String extra = inParams.getExtra();

        User to_user = userProcessor.getUserByUsername(to_username);
        if (to_user == null) {
            throw new CourseWarn(UserWarnEnum.USER_NO_EXIST);
        }
        friendProcessor.createFriendRequest(from_username, to_username, extra);

        // TODO
        /* 调用websocket给接收者客户端定向发送消息 */
        NewFriendRequestOutParams outParams = new NewFriendRequestOutParams();
        User from_user = userProcessor.getUserByUsername(from_username);
        outParams.setFromUsername(from_username);
        outParams.setFromNickname(from_user.getNickname());
        outParams.setFromAvatar(from_user.getAvatar());
        outParams.setExtra(extra);
        outParams.setStatus(0);
        outParams.setType(0);

        SocketUtil.sendMessageToUser(to_username, outParams);

        return new CommonOutParams(true);
    }

    /** 删除好友 */
    @BizType(BizTypeEnum.FRIEND_REMOVE_FRIEND)
    @NeedLogin
    public CommonOutParams friendRemoveFriend(RemoveFriendInParams inParams) throws Exception {
        /* 删除好友关系 */
        String username = inParams.getUsername();
        String friend_username = inParams.getFriendUsername();
        friendProcessor.removeFriend(username, friend_username);

        /* 更新好友请求状态 */
        friendProcessor.updateFriendRequestStatus(username, friend_username);
        friendProcessor.updateFriendRequestStatus(friend_username, username);

        /* 删除聊天条目（单向）和关系 */
        ChatUserLink chatUserLink = chatProcessor.getChatUserLink(username, friend_username);
        if (chatUserLink != null) {
            String link_id = chatUserLink.getId();
            chatProcessor.removeChatItem(link_id, username);
            chatProcessor.removeChatUserLink(link_id);
        }

        return new CommonOutParams(true);
    }

    /** 设置星标好友 */
    @BizType(BizTypeEnum.FRIEND_SET_STAR_FRIEND)
    @NeedLogin
    public CommonOutParams friendSetStarFriend(SetStarFriendInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String friend_username = inParams.getFriendUsername();
        friendProcessor.setStarFriend(username, friend_username, true);

        return new CommonOutParams(true);
    }

    /** 取消星标好友 */
    @BizType(BizTypeEnum.FRIEND_CANCEL_STAR_FRIEND)
    @NeedLogin
    public CommonOutParams friendCancelStarFriend(CancelStarFriendInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String friend_username = inParams.getFriendUsername();
        friendProcessor.setStarFriend(username, friend_username, false);

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

    /** 查看好友申请 */
    @BizType(BizTypeEnum.FRIEND_GET_FRIEND_REQUEST)
    @NeedLogin
    public GetFriendRequestOutParams friendGetFriendRequest(CommonInParams inParams) throws Exception {
        String username = inParams.getUsername();
        List<FriendRequest> friendRequestList = friendProcessor.getFriendRequest(username);
        if (friendRequestList.size() == 0) {
            throw new CourseWarn(UserWarnEnum.NO_FRIEND_REQUEST);
        }

        List<FriendRequestItem> friendRequestItemList = new ArrayList<>();
        for (FriendRequest friendRequest: friendRequestList) {
            String fromUsername = friendRequest.getFromUsername();
            User fromUser = userProcessor.getUserByUsername(fromUsername);

            FriendRequestItem friendRequestItem = new FriendRequestItem();
            friendRequestItem.setFromUsername(fromUsername);
            friendRequestItem.setFromNickname(fromUser.getNickname());
            String avatar = fromUser.getAvatar();
            int index = avatar.indexOf(AVATAR_RELATIVE_PATH);
            String avatar_url = FILE_URL + avatar.substring(index);
            friendRequestItem.setFromAvatar(avatar_url);
            friendRequestItem.setExtra(friendRequest.getExtra());
            friendRequestItem.setStatus(friendRequest.getStatus());

            friendRequestItemList.add(friendRequestItem);
        }

        FriendRequestItem[] friendRequestItems = new FriendRequestItem[friendRequestItemList.size()];
        Collections.reverse(friendRequestItemList);
        friendRequestItemList.toArray(friendRequestItems);

        GetFriendRequestOutParams outParams = new GetFriendRequestOutParams();
        outParams.setFriendRequest(friendRequestItems);
        return outParams;
    }

    /** 审核好友申请 */
    @BizType(BizTypeEnum.FRIEND_CHECK_FRIEND_REQUEST)
    @NeedLogin
    public CommonOutParams friendCheckFriendRequest(CheckFriendRequestInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String from_username = inParams.getFromUsername();
        String result_str = inParams.getResult();
        boolean result = result_str.equals("1");

        User from_user = userProcessor.getUserByUsername(from_username);
        if (from_user == null) {
            throw new CourseWarn(UserWarnEnum.USER_NO_EXIST);
        }

        /* 如果发起者已经是好友了则返回 */
        Friendship friendship = friendProcessor.getFriendshipByUsername(username, from_username);
        if (friendship != null) {
            throw new CourseWarn(UserWarnEnum.ALREADY_FRIEND);
        }
        /* 审核好友申请 */
        friendProcessor.checkFriendRequest(from_username, username, result);

        if (result) {
            friendProcessor.addFriendship(username, from_username);
            friendProcessor.addFriendship(from_username, username);

            /* 给对方返回结果 */
            CheckFriendRequestOutParams outParams = new CheckFriendRequestOutParams();
            outParams.setToUsername(username);
            outParams.setExtra("对方已接受您的好友申请");
            outParams.setType(1);
            SocketUtil.sendMessageToUser(from_username, outParams);
        }

        return new CommonOutParams(true);
    }

//    /** 查看某一个用户的信息 */
//    @BizType(BizTypeEnum.FRIEND_GET_USER_INFO)
//    @NeedLogin
//    public GetUserInfoOutParams friendGetUserInfo(GetUserInfoInParams inParams) throws Exception {
//        String username = inParams.getUsername();
//        String oppositeUsername = inParams.getOppositeUsername();
//        User user = userProcessor.getUserByUsername(oppositeUsername);
//
//        Friendship friendship = friendProcessor.getFriendshipByUsername(username, oppositeUsername);
//        GetUserInfoOutParams outParams = new GetUserInfoOutParams();
//        String avatar = user.getAvatar();
//        int index = avatar.indexOf(AVATAR_RELATIVE_PATH);
//        String avatar_url = FILE_URL + avatar.substring(index);
//        outParams.setAvatar(avatar_url);
//        outParams.setUsername(oppositeUsername);
//        outParams.setNickname(user.getNickname());
//        outParams.setGender(user.getGender());
//        outParams.setSignature(user.getSignature());
//
//        if (friendship != null) {
//            outParams.setFriend(true);
//            outParams.setRemark(friendship.getRemark());
//            outParams.setStar(friendship.isStar());
//            outParams.setAge(user.getAge());
//            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
//            String birthdayStr = dateFormat.format(user.getBirthday());
//            outParams.setBirthday(birthdayStr);
//            outParams.setTelephone(user.getTelephone());
//        }
//        else {
//            outParams.setFriend(false);
//            outParams.setRemark("");
//            outParams.setAge(0);
//            outParams.setBirthday("");
//            outParams.setTelephone("");
//            outParams.setStar(false);
//        }
//
//        return outParams;
//    }

    /** 获取通讯录 */
    @BizType(BizTypeEnum.FRIEND_GET_FRIENDS)
    @NeedLogin
    public GetFriendsOutParams friendGetFriends(CommonInParams inParams) throws Exception {
        String username = inParams.getUsername();
        List<Friendship> friendshipList = friendProcessor.getAllFriendship(username);

        List<FriendItem> friendItemList = new ArrayList<>();
        for (Friendship friendship: friendshipList) {
            String friend_username = friendship.getFriendUsername();
            User friend = userProcessor.getUserByUsername(friend_username);

            String friend_avatar = friend.getAvatar();
            int index = friend_avatar.indexOf(AVATAR_RELATIVE_PATH);
            String avatar_url = FILE_URL + friend_avatar.substring(index);
            String friend_nickname = friend.getNickname();
            String friend_remark = friendship.getRemark();

            FriendItem friendItem = new FriendItem();
            friendItem.setFriendUsername(friend_username);
            friendItem.setFriendAvatar(avatar_url);
            friendItem.setFriendNickname(friend_nickname);
            friendItem.setFriendRemark(friend_remark);
            friendItemList.add(friendItem);
        }

        FriendItem[] result = new FriendItem[friendItemList.size()];
        friendItemList.toArray(result);

        GetFriendsOutParams outParams = new GetFriendsOutParams();
        outParams.setFriends(result);
        return outParams;
    }
}
