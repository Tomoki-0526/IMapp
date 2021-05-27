package com.tsinghua.course.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 图文混合动态
 */
@Document("TextAndImgMoment")
public class TextAndImgMoment extends Moment {
    // 文字内容
    String content;

    // 图片路径数组
    String[] imgPath;

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String[] getImgPath() {
        return imgPath;
    }
    public void setImgPath(String[] imgPath) {
        this.imgPath = imgPath;
    }
}
