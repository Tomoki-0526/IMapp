package com.tsinghua.course.Base.Model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 聊天关系表
 */
@Document("ChatLink")
public class ChatLink {
    // mongodb唯一id
    String id;
    // 用户A
    String usernameA;
    // 用户B
    String usernameB;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsernameA() {
        return usernameA;
    }

    public void setUsernameA(String usernameA) {
        this.usernameA = usernameA;
    }

    public String getUsernameB() {
        return usernameB;
    }

    public void setUsernameB(String usernameB) {
        this.usernameB = usernameB;
    }
}
