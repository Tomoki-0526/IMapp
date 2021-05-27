package com.tsinghua.course.Biz.Controller;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.NeedLogin;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonOutParams;
import com.tsinghua.course.Biz.Controller.Params.MomentParams.In.PublishMomentInParams;
import com.tsinghua.course.Biz.Processor.FriendProcessor;
import com.tsinghua.course.Biz.Processor.MomentProcessor;
import com.tsinghua.course.Biz.Processor.UserProcessor;
import com.tsinghua.course.Frame.Util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.tsinghua.course.Base.Constant.GlobalConstant.*;
import static com.tsinghua.course.Base.Constant.NameConstant.OS_NAME;
import static com.tsinghua.course.Base.Constant.NameConstant.WIN;

/**
 * @描述 动态控制器
 */
@Component
public class MomentController {

    @Autowired
    UserProcessor userProcessor;
    @Autowired
    FriendProcessor friendProcessor;
    @Autowired
    MomentProcessor momentProcessor;

    /** 发布动态 */
    @BizType(BizTypeEnum.MOMENT_PUBLISH_MOMENT)
    @NeedLogin
    public CommonOutParams momentPublishMoment(PublishMomentInParams inParams) throws Exception {
        String username = inParams.getUsername();
        int type = Integer.parseInt(inParams.getType());

        /* 根据动态类型不同获取字段 */
        // 根据Windows和Linux配置不同的头像保存路径
        String OSName = System.getProperty(OS_NAME);
        String momentPath = OSName.toLowerCase().startsWith(WIN) ? WINDOWS_MOMENT_PATH : LINUX_MOMENT_PATH;
        // 文本
        if (type == 0) {
            String content = inParams.getContent();
            momentProcessor.publishTextMoment(username, type, content);
        }
        // 图片
        else if (type == 1) {
            MultipartFile[] images = inParams.getImages();
            List<String> imgPathList = new ArrayList<>();
            for (MultipartFile file: images) {
                // 获取原始文件名
                String originalFilename = file.getOriginalFilename();
                // 生成UUID名称
                assert originalFilename != null;
                String uuidFilename = FileUtil.getUUIDName(originalFilename);
                // 产生一个随机目录
                String randomDir = FileUtil.getDir();
                File fileDir = new File(momentPath + randomDir);
                if (!fileDir.exists())
                    fileDir.mkdirs();
                // 创建新的文件
                File newFile = new File(momentPath + randomDir, uuidFilename);
                // 将文件输出到目标文件中
                file.transferTo(newFile);

                // 获取路径
                String img = momentPath + randomDir + "/" + uuidFilename;
                imgPathList.add(img);
            }
            String[] imgs = new String[imgPathList.size()];
            imgPathList.toArray(imgs);

            momentProcessor.publishImgMoment(username, type, imgs);
        }
        // 图文
        else if (type == 2) {
            String content = inParams.getContent();
            MultipartFile[] images = inParams.getImages();
            List<String> imgPathList = new ArrayList<>();
            for (MultipartFile file: images) {
                // 获取原始文件名
                String originalFilename = file.getOriginalFilename();
                // 生成UUID名称
                assert originalFilename != null;
                String uuidFilename = FileUtil.getUUIDName(originalFilename);
                // 产生一个随机目录
                String randomDir = FileUtil.getDir();
                File fileDir = new File(momentPath + randomDir);
                if (!fileDir.exists())
                    fileDir.mkdirs();
                // 创建新的文件
                File newFile = new File(momentPath + randomDir, uuidFilename);
                // 将文件输出到目标文件中
                file.transferTo(newFile);

                // 获取路径
                String img = momentPath + randomDir + "/" + uuidFilename;
                imgPathList.add(img);
            }
            String[] imgs = new String[imgPathList.size()];
            imgPathList.toArray(imgs);

            momentProcessor.publishTextAndImgMoment(username, type, content, imgs);
        }
        // 视频
        else if (type == 3) {
            MultipartFile file = inParams.getVideo();
            // 获取原始文件名
            String originalFilename = file.getOriginalFilename();
            // 生成UUID名称
            assert originalFilename != null;
            String uuidFilename = FileUtil.getUUIDName(originalFilename);
            // 产生一个随机目录
            String randomDir = FileUtil.getDir();
            File fileDir = new File(momentPath + randomDir);
            if (!fileDir.exists())
                fileDir.mkdirs();
            // 创建新的文件
            File newFile = new File(momentPath + randomDir, uuidFilename);
            // 将文件输出到目标文件中
            file.transferTo(newFile);

            // 写入数据库
            String video = momentPath + randomDir + "/" + uuidFilename;
            momentProcessor.publishVideoMoment(username, type, video);
        }

        return new CommonOutParams(true);
    }
}
