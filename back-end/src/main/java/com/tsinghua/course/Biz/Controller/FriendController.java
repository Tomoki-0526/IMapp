package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Base.CustomizedClass.FriendItem;
import com.tsinghua.course.Base.CustomizedClass.FriendRequestItem;
import com.tsinghua.course.Base.Error.CourseWarn;
import com.tsinghua.course.Base.Error.UserWarnEnum;
import com.tsinghua.course.Base.Model.FriendGroup;
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

import static com.tsinghua.course.Base.Constant.GlobalConstant.*;
import static com.tsinghua.course.Base.Constant.KeyConstant.GROUP;

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
            throw new CourseWarn(UserWarnEnum.FIND_STRANGER_NO_RESULT);
        }
        String nickname = stranger.getNickname();
        String avatar = stranger.getAvatar();
        String avatar_url = "http://" + SERVER_IP + ":" + FILE_PORT + avatar;

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
        String stranger_avatar_url = "http://" + SERVER_IP + ":" + FILE_PORT + stranger_avatar;
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
        Friendship friendship_by_username = friendProcessor.getFriendshipByUsername(username, content);
        if (friendship_by_username != null)
            friendship_list.add(friendship_by_username);
        /* 根据昵称查找 */
        friendship_list.addAll(friendProcessor.getFriendshipByNickname(username, content));
        /* 根据备注查找 */
        friendship_list.addAll(friendProcessor.getFriendshipByRemark(username, content));

        if (friendship_list.isEmpty()) {
            throw new CourseWarn(UserWarnEnum.FIND_FRIEND_NO_RESULT);
        }
        else {
            List<FriendItem> temp = new ArrayList<>();
            for (Friendship friendship: friendship_list) {
                String friend_username = friendship.getFriendUsername();
                String friend_remark = friendship.getRemark();
                User friend = userProcessor.getUserByUsername(friend_username);
                String friend_nickname = friend.getNickname();
                String friend_avatar = friend.getAvatar();
                String friend_avatar_url = "http://" + SERVER_IP + ":" + FILE_PORT + friend_avatar;

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
            String friend_avatar_url = "http://" + SERVER_IP + ":" + FILE_PORT + friend_avatar;
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
        String avatar_url = "http://" + SERVER_IP + ":" + FILE_PORT + avatar;
        String nickname = friend.getNickname();
        String remark = friendship.getRemark();
        String gender = friend.getGender();
        int age = friend.getAge();
        String telephone = friend.getTelephone();

        String groupID = friendship.getGroupID();
        FriendGroup friendGroup = friendProcessor.getGroupByID(groupID);

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
        outParams.setGroup(friendGroup.getGroupName());
        outParams.setStar(star);
        outParams.setSignature(signature);

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

        SocketUtil.sendMessageToUser(to_username, outParams);

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
            String avatar_url = "http://" + SERVER_IP + ":" + FILE_PORT + avatar;
            friendRequestItem.setFromAvatar(avatar_url);
            friendRequestItem.setExtra(friendRequest.getExtra());
            friendRequestItem.setStatus(friendRequest.getStatus());

            friendRequestItemList.add(friendRequestItem);
        }

        FriendRequestItem[] friendRequestItems = new FriendRequestItem[friendRequestItemList.size()];
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
        boolean result = inParams.getResult();

        friendProcessor.checkFriendRequest(from_username, username, result);
        // TODO
        /*
          如果通过，则给发送方返回推送
          以新消息的形式发送
         */
        if (result) {
            friendProcessor.addFriendship(username, from_username);
            friendProcessor.addFriendship(from_username, username);
            friendProcessor.addFriendToGroup(username, from_username, GROUP);
            friendProcessor.addFriendToGroup(from_username, username, GROUP);
        }

        return new CommonOutParams(true);
    }

    /** 添加分组 */
    @BizType(BizTypeEnum.FRIEND_ADD_GROUP)
    @NeedLogin
    public CommonOutParams friendAddGroup(AddGroupInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String group_name = inParams.getGroupName();

        FriendGroup friendGroup = friendProcessor.getGroupByUsernameAndGroupName(username, group_name);
        if (friendGroup != null) {
            throw new CourseWarn(UserWarnEnum.DUPLICATE_GROUP);
        }

        friendProcessor.addFriendGroup(username, group_name);
        return new CommonOutParams(true);
    }

    /** 查看所有分组 */
    @BizType(BizTypeEnum.FRIEND_GET_GROUPS)
    @NeedLogin
    public GetGroupsOutParams friendGetGroups(CommonInParams inParams) throws Exception {
        String username = inParams.getUsername();
        List<FriendGroup> friendGroupList = friendProcessor.getAllGroups(username);
        List<String> groupName = new ArrayList<>();
        for (FriendGroup friendGroup: friendGroupList) {
            groupName.add(friendGroup.getGroupName());
        }
        String[] result = new String[groupName.size()];
        groupName.toArray(result);

        GetGroupsOutParams outParams = new GetGroupsOutParams();
        outParams.setGroups(result);
        return outParams;
    }

    /** 将好友添加到分组 */
    @BizType(BizTypeEnum.FRIEND_ADD_FRIEND_TO_GROUP)
    @NeedLogin
    public CommonOutParams friendAddFriendToGroup(AddFriendToGroupInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String friend_username = inParams.getFriendUsername();
        String group_name = inParams.getGroupName();

        FriendGroup friendGroup = friendProcessor.getGroupByUsernameAndGroupName(username, group_name);
        if (friendGroup == null) {
            throw new CourseWarn(UserWarnEnum.NO_SUCH_GROUP);
        }

        friendProcessor.addFriendToGroup(username, friend_username, group_name);
        return new CommonOutParams(true);
    }

    /** 修改分组名称 */
    @BizType(BizTypeEnum.FRIEND_SET_GROUP_NAME)
    @NeedLogin
    public CommonOutParams friendSetGroupName(SetGroupNameInParams inParams) throws Exception {
        String username = inParams.getUsername();
        String old_group_name = inParams.getOldGroupName();
        String new_group_name = inParams.getNewGroupName();

        FriendGroup friendGroup = friendProcessor.getGroupByUsernameAndGroupName(username, old_group_name);
        if (friendGroup == null) {
            throw new CourseWarn(UserWarnEnum.NO_SUCH_GROUP);
        }
        friendProcessor.setGroupName(username, old_group_name, new_group_name);
        return new CommonOutParams(true);
    }
}
