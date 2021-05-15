package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Base.CustomizedClass.FriendItem;
import com.tsinghua.course.Base.Model.Friendship;
import com.tsinghua.course.Base.Model.User;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.FriendParams.in.FindFriendInParams;
import com.tsinghua.course.Biz.Controller.Params.FriendParams.in.FindStrangerInParams;
import com.tsinghua.course.Biz.Controller.Params.FriendParams.in.GetStrangerInfoInParams;
import com.tsinghua.course.Biz.Controller.Params.FriendParams.out.FindFriendOutParams;
import com.tsinghua.course.Biz.Controller.Params.FriendParams.out.FindStrangerOutParams;
import com.tsinghua.course.Biz.Controller.Params.FriendParams.out.GetStrangerInfoOutParams;
import com.tsinghua.course.Biz.Processor.FriendProcessor;
import com.tsinghua.course.Biz.Processor.UserProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
}
