package com.tsinghua.course.Biz.Controller.Params.UserParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;
import org.springframework.web.multipart.MultipartFile;

/**
 * @描述 上传头像的入参
 **/
@BizType(BizTypeEnum.USER_UPLOAD_AVATAR)
public class UploadAvatarInParams extends CommonInParams {
    // 头像
    @Required
    private MultipartFile avatar;

    public MultipartFile getAvatar() { return avatar; }
    public void setAvatar(MultipartFile avatar) { this.avatar = avatar; }
}
