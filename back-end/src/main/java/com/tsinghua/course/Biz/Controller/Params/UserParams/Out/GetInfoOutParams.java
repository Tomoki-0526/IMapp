package com.tsinghua.course.Biz.Controller.Params.UserParams.Out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

import java.util.Date;

/**
 * @描述 查看个人信息的出参
 **/
@BizType(BizTypeEnum.USER_GET_INFO)
public class GetInfoOutParams extends CommonOutParams {
    // 头像图片路径
    private String avatar;
    // 昵称
    private String nickname;
    // 用户名
    private String username;
    // 性别
    private String gender;
    // 年龄
    private int age;
    // 生日
    private Date birthday;
    // 手机号码
    private String telephone;
    // 个性签名
    private String signature;

    public GetInfoOutParams() { this.success = true; }
    public GetInfoOutParams(boolean success) { this.success = success; }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() { return gender; }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) { this.age = age; }

    public Date getBirthday() { return birthday; }
    public void setBirthday(Date birthday) { this.birthday = birthday; }

    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
}
