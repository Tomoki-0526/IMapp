package com.tsinghua.course.Biz.Controller.Params.UserParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;

/**
 * @描述 修改密码的入参
 **/
@BizType(BizTypeEnum.USER_MODIFY_PASSWORD)
public class ModifyPasswordParams extends CommonInParams {
    // 旧密码
    @Required
    private String old_pwd;

    // 新密码
    @Required
    private String new_pwd;

    // 确认密码
    @Required
    private String confirm_pwd;

    public String getOldPassword() { return old_pwd; }
    public void setOldPassword(String old_pwd) { this.old_pwd = old_pwd; }

    public String getNewPassword() { return new_pwd; }
    public void setNewPassword(String new_pwd) { this.new_pwd = new_pwd; }

    public String getConfirmPassword() { return confirm_pwd; }
    public void setConfirmPassword(String confirm_pwd) { this.confirm_pwd = confirm_pwd; }
}
