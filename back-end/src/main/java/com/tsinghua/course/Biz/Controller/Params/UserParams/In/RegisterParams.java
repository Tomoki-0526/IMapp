package com.tsinghua.course.Biz.Controller.Params.UserParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 用户注册的入参
 **/
@BizType(BizTypeEnum.USER_REGISTER)
public class RegisterParams extends CommonInParams {
    // 密码
    @Required
    private String password;

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
