package com.tsinghua.course.Biz.Controller.Params.FriendParams.out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 查找新联系人的出参
 **/
@BizType(BizTypeEnum.FRIEND_GET_STRANGER_INFO)
public class GetStrangerInfoOutParams extends CommonOutParams {
    // 陌生人用户名
    private String stranger_username;
    // 陌生人昵称
    private String stranger_nickname;
    // 陌生人头像
    private String stranger_avatar;
    // 陌生人性别
    private String stranger_gender;
    // 陌生人个性签名
    private String stranger_signature;

    public GetStrangerInfoOutParams() { this.success = true; }
    public GetStrangerInfoOutParams(boolean success) { this.success = success; }

    public String getStrangerUsername() { return stranger_username; }
    public void setStrangerUsername(String stranger_username) { this.stranger_username = stranger_username; }

    public String getStrangerNickname() { return stranger_nickname; }
    public void setStrangerNickname(String stranger_nickname) { this.stranger_nickname = stranger_nickname; }

    public String getStrangerAvatar() { return stranger_avatar; }
    public void setStrangerAvatar(String stranger_avatar) { this.stranger_avatar = stranger_avatar; }

    public String getStrangerGender() { return stranger_gender; }
    public void setStrangerGender(String stranger_gender) { this.stranger_gender = stranger_gender; }

    public String getStrangerSignature() { return stranger_signature; }
    public void setStrangerSignature(String stranger_signature) { this.stranger_signature = stranger_signature; }
}
