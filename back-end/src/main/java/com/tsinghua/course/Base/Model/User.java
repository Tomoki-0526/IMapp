package com.tsinghua.course.Base.Model;

import com.tsinghua.course.Base.Enum.UserType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import static com.tsinghua.course.Base.Constant.GlobalConstant.DATE_PATTERN;

/**
 * @描述 对应mongodb中的User集合，mongodb是非关系型数据库，可以存储的对象类型很丰富，使用起来方便很多
 **/
@Document("User")
public class User {
    /** 子对象文档 */
    public static class SubObj {
        /** 存储的时间 */
        String time;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
    // mongodb唯一id
    String id;
    // 用户名
    String username;
    // 密码
    String password;
    // 昵称
    String nickname;
    // 性别（0-男 1-女）
    String gender;
    // 年龄
    int age;
    // 生日
    Date birthday;
    // 电话
    String telephone;
    // 头像（路径）
    String avatar;
    // 个性签名
    String signature;
    // 用户类型
    UserType userType;
    // 测试数组
    String[] testArr;
    // 测试对象
    Map<String, String> testObj;
    // 另一个测试对象，和 Map<String, String> 方式存储的格式是一样的，但是直观很多
    SubObj subObj;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public Date getBirthday() { return birthday; }
    public void setBirthday(String birthday_str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        try {
            if (birthday_str.equals(""))
                birthday = dateFormat.parse("1970-01-01");
            else
                birthday = dateFormat.parse(birthday_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void setBirthday(Date birthday) { this.birthday = birthday; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }

    public String[] getTestArr() { return testArr; }
    public void setTestArr(String[] testArr) { this.testArr = testArr; }

    public Map<String, String> getTestObj() { return testObj; }
    public void setTestObj(Map<String, String> testObj) { this.testObj = testObj; }

    public SubObj getSubObj() { return subObj; }
    public void setSubObj(SubObj subObj) { this.subObj = subObj; }
}
