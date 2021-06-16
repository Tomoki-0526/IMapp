package com.tsinghua.course.Biz.Controller.Params.ChatParams.In;

import com.tsinghua.course.Base.Annotation.BizType;
import com.tsinghua.course.Base.Annotation.Required;
import com.tsinghua.course.Biz.BizTypeEnum;
import com.tsinghua.course.Biz.Controller.Params.CommonInParams;
import io.netty.handler.codec.http.multipart.FileUpload;

/**
 * @描述 发送消息的入参
 */
@BizType(BizTypeEnum.CHAT_SEND_MESSAGE)
public class SendMessageInParams extends CommonInParams {
    // 聊天id
    @Required
    String linkId;
    // 是否是群聊
    @Required
    String isMultiple;
    // 消息类型
    @Required
    String type;
    // 文本内容
    String text;
    // 图片
    FileUpload image;
    // 音频
    FileUpload audio;
    // 视频
    FileUpload video;
    // 经度
    String longitude;
    // 纬度
    String latitude;

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getIsMultiple() {
        return isMultiple;
    }

    public void setIsMultiple(String isMultiple) {
        this.isMultiple = isMultiple;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public FileUpload getImage() {
        return image;
    }

    public void setImage(FileUpload image) {
        this.image = image;
    }

    public FileUpload getAudio() {
        return audio;
    }

    public void setAudio(FileUpload audio) {
        this.audio = audio;
    }

    public FileUpload getVideo() {
        return video;
    }

    public void setVideo(FileUpload video) {
        this.video = video;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
