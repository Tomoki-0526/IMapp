package com.tsinghua.course.Biz.Processor;

import com.tsinghua.course.Base.Constant.KeyConstant;
import com.tsinghua.course.Base.Enum.UserType;
import com.tsinghua.course.Base.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.querydsl.QuerydslRepositoryInvokerAdapter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import static com.tsinghua.course.Base.Constant.GlobalConstant.*;
import static com.tsinghua.course.Base.Constant.NameConstant.OS_NAME;
import static com.tsinghua.course.Base.Constant.NameConstant.WIN;

/**
 * @描述 用户原子处理器，所有与用户相关的原子操作都在此处理器中执行
 **/
@Component
public class UserProcessor {
    @Autowired
    MongoTemplate mongoTemplate;

    /** 创建新用户 */
    public void createUser(String username, String password, String nickname, String telephone) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setGender("");
        user.setAge(0);
        user.setBirthday("");
        user.setTelephone(telephone);
        user.setSignature("");
        user.setUserType(UserType.NORMAL);

        String OSName = System.getProperty(OS_NAME);
        String avatarPath = OSName.toLowerCase().startsWith(WIN) ? WINDOWS_AVATAR_PATH : LINUX_AVATAR_PATH;
        user.setAvatar(avatarPath + DEFAULT_AVATAR);

        User.SubObj subObj = new User.SubObj();
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_PATTERN);
        subObj.setTime(dateFormat.format(now));
        user.setSubObj(subObj);

        mongoTemplate.insert(user);
    }

    /** 根据用户名从数据库中获取用户 */
    public User getUserByUsername(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        return mongoTemplate.findOne(query, User.class);
    }

    /** 修改密码 */
    public void updateUserPassword(String username, String new_pwd) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        Update update = new Update();
        update.set(KeyConstant.PASSWORD, new_pwd);

        mongoTemplate.upsert(query, update, User.class);
    }

    /** 更新个人信息 */
    public void updateUserInfo(String username, String nickname, String gender,
                               String birthday_str, String telephone, String signature) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));

        Update update = new Update();
        update.set(KeyConstant.NICKNAME, nickname);
        update.set(KeyConstant.GENDER, gender);
        update.set(KeyConstant.TELEPHONE, telephone);
        update.set(KeyConstant.SIGNATURE, signature);
        /* 根据生日计算年龄 */
        if (birthday_str != null) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date birthday = dateFormat.parse(birthday_str);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(birthday);
                int year_birth = calendar.get(Calendar.YEAR);
                int month_birth = calendar.get(Calendar.MONTH);
                int day_birth = calendar.get(Calendar.DAY_OF_MONTH);
                Calendar today = Calendar.getInstance();
                int year_today = today.get(Calendar.YEAR);
                int month_today = today.get(Calendar.MONTH);
                int day_today = today.get(Calendar.DAY_OF_MONTH);
                int age = year_today - year_birth;
                if (month_today <= month_birth) {
                    if (month_today == month_birth) {
                        if (day_today < day_birth)
                            age--;
                    }
                    else
                        age--;
                }
                update.set(KeyConstant.BIRTHDAY, birthday);
                update.set(KeyConstant.AGE, age);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            update.set(KeyConstant.BIRTHDAY, "");
            update.set(KeyConstant.AGE, 0);
        }

        mongoTemplate.upsert(query, update, User.class);
    }

    /** 更新头像 */
    public void updateAvatar(String username, String avatar) {
        Query query = new Query();
        query.addCriteria(Criteria.where(KeyConstant.USERNAME).is(username));
        Update update = new Update();
        update.set(KeyConstant.AVATAR, avatar);
        mongoTemplate.upsert(query, update, User.class);
    }
}
