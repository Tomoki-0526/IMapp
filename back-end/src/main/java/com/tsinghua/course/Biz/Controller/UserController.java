package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Base.Error.CourseWarn;
import com.tsinghua.course.Base.Error.UserWarnEnum;
import com.tsinghua.course.Base.Model.User;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;
import com.tsinghua.course.Biz.Controller.Params.UserParams.In.*;
import com.tsinghua.course.Biz.Controller.Params.UserParams.Out.GetInfoOutParams;
import com.tsinghua.course.Biz.Processor.UserProcessor;
import com.tsinghua.course.Frame.Util.*;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.tsinghua.course.Base.Constant.GlobalConstant.LINUX_AVATAR_PATH;
import static com.tsinghua.course.Base.Constant.GlobalConstant.WINDOWS_AVATAR_PATH;

/**
 * @描述 用户控制器，用于执行用户相关的业务
 **/
@Component
public class UserController {

    @Autowired
    UserProcessor userProcessor;

    /** 用户登录业务 */
    @BizType(BizTypeEnum.USER_LOGIN)
    public CommonOutParams userLogin(LoginInParams inParams) throws Exception {
        String username = inParams.getUsername();
        if (username == null)
            throw new CourseWarn(UserWarnEnum.LOGIN_FAILED);
        User user = userProcessor.getUserByUsername(username);
        if (user == null || !user.getPassword().equals(inParams.getPassword()))
            throw new CourseWarn(UserWarnEnum.LOGIN_FAILED);

        /** 登录成功，记录登录状态 */
        ChannelHandlerContext ctx = ThreadUtil.getCtx();
        /** ctx不为空记录WebSocket状态，否则记录http状态 */
        if (ctx != null)
            SocketUtil.setUserSocket(username, ctx);
        else {
            HttpSession httpSession = ThreadUtil.getHttpSession();
            if (httpSession != null) {
                httpSession.setUsername(username);
            }
        }
        return new CommonOutParams(true);
    }

    /** 用户注册业务 */
    @BizType(BizTypeEnum.USER_REGISTER)
    public CommonOutParams userRegister(RegisterInParams inParams) throws Exception {
        /* 用户名 */
        String username = inParams.getUsername();
        if (username == null)
            throw new CourseWarn(UserWarnEnum.NEED_USERNAME);
        String regex = "^[0-9A-Za-z_]{8,20}$";
        if (!username.matches(regex))
            throw new CourseWarn(UserWarnEnum.INVALID_USERNAME);
        User user = userProcessor.getUserByUsername(username);
        if (user != null)
            throw new CourseWarn(UserWarnEnum.DUPLICATE_USERNAME);

        /* 密码 */
        String password = inParams.getPassword();
        if (password == null)
            throw new CourseWarn(UserWarnEnum.NEED_PASSWORD);
        regex = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)[0-9A-Za-z\\W]{6,18}$";
        if (!password.matches(regex))
            throw new CourseWarn(UserWarnEnum.INVALID_PASSWORD);

        /* 创建新用户并登录 */
        userProcessor.createUser(username, password);
        ChannelHandlerContext ctx = ThreadUtil.getCtx();
        if (ctx != null)
            SocketUtil.setUserSocket(username, ctx);
        else {
            HttpSession httpSession = ThreadUtil.getHttpSession();
            if (httpSession != null) {
                httpSession.setUsername(username);
            }
        }

        return new CommonOutParams(true);
    }

    /** 用户登出业务 */
    @BizType(BizTypeEnum.USER_LOGOUT)
    @NeedLogin
    public CommonOutParams userLogout(CommonInParams inParams) throws Exception {

        return new CommonOutParams(true);
    }

    /** 修改密码业务 */
    @BizType(BizTypeEnum.USER_MODIFY_PASSWORD)
    @NeedLogin
    public CommonOutParams userModifyPassword(ModifyPasswordInParams inParams) throws Exception {
        /* 获取用户 */
        String username = inParams.getUsername();
        User user = userProcessor.getUserByUsername(username);

        /* 匹配旧密码 */
        String input_old_pwd = inParams.getOldPassword();
        String real_old_pwd = user.getPassword();
        if (!real_old_pwd.equals(input_old_pwd))
            throw new CourseWarn(UserWarnEnum.MISMATCHED_PASSWORD);

        /* 检验新密码的合法性 */
        String new_pwd = inParams.getNewPassword();
        String reg = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)[0-9A-Za-z\\W]{6,18}$";
        if (!new_pwd.matches(reg))
            throw new CourseWarn(UserWarnEnum.INVALID_NEW_PASSWORD);

        /* 检查两次输入的密码是否匹配 */
        String confirm_pwd = inParams.getConfirmPassword();
        if (!confirm_pwd.equals(new_pwd))
            throw new CourseWarn(UserWarnEnum.MISMATCHED_NEW_PASSWORD);

        userProcessor.updateUserPassword(username, new_pwd);
        return new CommonOutParams(true);
    }

    /** 查看个人信息 */
    @BizType(BizTypeEnum.USER_GET_INFO)
    @NeedLogin
    public GetInfoOutParams userGetInfo(CommonInParams inParams) throws Exception {
        /* 获取用户 */
        String username = inParams.getUsername();
        User user = userProcessor.getUserByUsername(username);

        /* 获取个人信息 */
        String avatar = user.getAvatar();
        String nickname = user.getNickname();
        String gender = user.getGender();
        int age = user.getAge();
        Date birthday = user.getBirthday();
        String telephone = user.getTelephone();
        String signature = user.getSignature();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String birthday_str = dateFormat.format(birthday);

        /* 组织Response */
        GetInfoOutParams outParams = new GetInfoOutParams(true);
        outParams.setAvatar(avatar);
        outParams.setUsername(username);
        outParams.setNickname(nickname);
        outParams.setGender(gender);
        outParams.setAge(age);
        outParams.setBirthday(birthday_str);
        outParams.setTelephone(telephone);
        outParams.setSignature(signature);

        return outParams;
    }

    /** 更新个人信息 */
    @BizType(BizTypeEnum.USER_UPDATE_INFO)
    @NeedLogin
    public CommonOutParams userUpdateInfo(UpdateInfoInParams inParams) throws Exception {
        /* 用户名 */
        String username = inParams.getUsername();

        /* 昵称 */
        String nickname = inParams.getNickname();

        /* 性别 */
        String gender = inParams.getGender();
        if (!gender.equals("male") && !gender.equals("female"))
            throw new CourseWarn(UserWarnEnum.INVALID_GENDER);

        /* 生日字符串 */
        String birthday_str = inParams.getBirthdayStr();
        String regex = "^\\d{4}-\\d{2}-\\d{2}$";
        if (!birthday_str.matches(regex))
            throw new CourseWarn(UserWarnEnum.INVALID_BIRTHDAY);

        /* 手机号码 */
        String telephone = inParams.getTelephone();
        regex = "^1[0-9]{10}$";
        if (!telephone.matches(regex))
            throw new CourseWarn(UserWarnEnum.INVALID_TELEPHONE);

        /* 个性签名 */
        String signature = inParams.getSignature();

        userProcessor.updateUserInfo(username, nickname, gender, birthday_str, telephone, signature);
        return new CommonOutParams(true);
    }

    /** 上传头像 */
    @BizType(BizTypeEnum.USER_UPLOAD_AVATAR)
    @NeedLogin
    public CommonOutParams userUploadAvatar(UploadAvatarInParams inParams) throws Exception {
        // 根据Windows和Linux配置不同的头像保存路径
        String OSName = System.getProperty("os.name");
        String avatarPath = OSName.toLowerCase().startsWith("win") ? WINDOWS_AVATAR_PATH : LINUX_AVATAR_PATH;

        // 获取文件内容
        MultipartFile file = inParams.getAvatar();
        InputStream inputStream = file.getInputStream();
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();

        // 生成uuid名称
        assert originalFilename != null;
        String uuidFilename = FileUtil.getUUIDName(originalFilename);

        // 产生一个随机目录
        String randomDir = FileUtil.getDir();

        File fileDir = new File(avatarPath + randomDir);
        // 若文件夹不存在，则创建文件夹
        if (!fileDir.exists())
            fileDir.mkdirs();
        // 创建新的文件
        File newFile = new File(avatarPath + randomDir, uuidFilename);
        // 将文件输出到目标文件中
        file.transferTo(newFile);

        // 将保存的文件路径更新到用户信息中
        String avatar = randomDir + "/" + uuidFilename;
        String username = inParams.getUsername();
        userProcessor.updateAvatar(username, avatar);

        return new CommonOutParams(true);
    }
}
