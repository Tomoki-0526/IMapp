package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Base.Model.Friendship;
import com.tsinghua.course.Base.Model.User;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.FriendParams.in.FindStrangerInParams;
import com.tsinghua.course.Biz.Controller.Params.FriendParams.in.GetStrangerInfoInParams;
import com.tsinghua.course.Biz.Controller.Params.FriendParams.out.FindStrangerOutParams;
import com.tsinghua.course.Biz.Controller.Params.FriendParams.out.GetStrangerInfoOutParams;
import com.tsinghua.course.Biz.Processor.FriendProcessor;
import com.tsinghua.course.Biz.Processor.UserProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    /** 搜索新联系人 */
    @BizType(BizTypeEnum.FRIEND_FIND_STRANGER)
    @NeedLogin
    public FindStrangerOutParams friendFindStranger(FindStrangerInParams inParams) throws Exception {
        String stranger_username = inParams.getStrangerUsername();
        User new_user = userProcessor.getUserByUsername(stranger_username);

        /* 如果没找到该用户、找到自己或者好友，均视为没有找到结果 */
        String username = inParams.getUsername();
        List<Friendship> friendships_list = friendProcessor.getAllFriendship(username);
        boolean is_friend = false;
        for (Friendship friendship: friendships_list) {
            if (friendship.getFriendUsername().equals(stranger_username)) {
                is_friend = true;
                break;
            }
        }
        if (new_user == null || new_user.getUsername().equals(username) || is_friend) {
            FindStrangerOutParams outParams = new FindStrangerOutParams(false);
            outParams.setStrangerUsername("");
            outParams.setStrangerNickname("");
            outParams.setStrangerAvatar("");
            outParams.setExtraInfo("未找到结果");
            return outParams;
        }
        String nickname = new_user.getNickname();
        String avatar = new_user.getAvatar();

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
}
