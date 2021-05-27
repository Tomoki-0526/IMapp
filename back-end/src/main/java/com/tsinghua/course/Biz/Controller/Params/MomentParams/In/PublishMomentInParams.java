package com.tsinghua.course.Biz.Controller.Params.MomentParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;
import org.springframework.web.multipart.MultipartFile;

/**
 * @描述 发布动态的入参
 */
@BizType(BizTypeEnum.MOMENT_PUBLISH_MOMENT)
public class PublishMomentInParams extends CommonInParams {
    // 动态类型
    @Required
    String type;
    // 文本内容
    String content;
    // 图片数组
    MultipartFile[] images;
    // 视频
    MultipartFile video;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public MultipartFile[] getImages() {
        return images;
    }
    public void setImages(MultipartFile[] images) {
        this.images = images;
    }

    public MultipartFile getVideo() {
        return video;
    }
    public void setVideo(MultipartFile video) {
        this.video = video;
    }
}
