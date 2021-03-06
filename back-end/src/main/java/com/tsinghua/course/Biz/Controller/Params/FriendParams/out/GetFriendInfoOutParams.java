package com.tsinghua.course.Biz.Controller.Params.FriendParams.out;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;

/**
 * @描述 查看好友信息的出参
 **/
@BizType(BizTypeEnum.FRIEND_GET_FRIEND_INFO)
public class GetFriendInfoOutParams extends CommonOutParams {
    // 头像
    private String avatar;
    // 昵称
    private String nickname;
    // 用户名
    private String username;
    // 备注
    private String remark;
    // 性别
    private String gender;
    // 年龄
    private int age;
    // 生日
    private String birthday;
    // 手机号
    private String telephone;
    // 分组名
    private String group;
    // 星标
    private boolean star;
    // 个性签名
    private String signature;

    public GetFriendInfoOutParams() { this.success = true; }
    public GetFriendInfoOutParams(boolean success) { this.success = success; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public boolean getStar() { return star; }
    public void setStar(boolean star) { this.star = star; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
}
