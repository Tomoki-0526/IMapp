package com.tsinghua.course.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 纯文字动态
 */
@Document("TextMoment")
public class TextMoment extends Moment {
    // 文字内容
    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
