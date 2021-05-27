package com.tsinghua.course.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @描述 图片动态
 * 最多支持四张图片
 */
@Document("ImgMoment")
public class ImgMoment extends Moment {
    // 图片路径数组
    String[] imgPath;

    public String[] getImgPath() {
        return imgPath;
    }
    public void setImgPath(String[] imgPath) {
        this.imgPath = imgPath;
    }
}
