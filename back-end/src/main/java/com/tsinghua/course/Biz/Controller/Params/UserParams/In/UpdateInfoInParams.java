package com.tsinghua.course.Biz.Controller.Params.UserParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

import java.util.Date;

/**
 * @描述 更新个人信息的入参
 **/
@BizType(BizTypeEnum.USER_UPDATE_INFO)
public class UpdateInfoInParams extends CommonInParams {
    // 昵称
    @Required
    private String nickname;

    // 性别
    @Required
    private String gender;

    // 生日
    @Required
    private String birthday;

    // 手机号码
    @Required
    private String telephone;

    // 个性签名
    @Required
    private String signature;

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }

}
