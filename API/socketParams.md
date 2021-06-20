# 指定Biztype

1. 发出好友申请  
```java
@BizType(BizTypeEnum.FRIEND_NEW_FRIEND_REQUEST)  

FRIEND_NEW_FRIEND_REQUEST(FriendController.class, "/friend/newFriendRequest", "添加陌生用户为好友")
```

2. 接受好友申请
```java
@BizType(BizTypeEnum.FRIEND_CHECK_FRIEND_REQUEST)  

FRIEND_CHECK_FRIEND_REQUEST(FriendController.class, "/friend/checkFriendRequest", "审核好友申请")
```

3. 点赞动态
```java
@BizType(BizTypeEnum.MOMENT_LIKE_MOMENT)

MOMENT_LIKE_MOMENT(MomentController.class, "/moment/likeMoment", "点赞动态")
```

4. 评论动态
```java
@BizType(BizTypeEnum.MOMENT_COMMENT_ON_MOMENT)

MOMENT_COMMENT_ON_MOMENT(MomentController.class, "/moment/commentOnMoment", "评论动态"),
```

5. 发送消息
```java
@BizType(BizTypeEnum.CHAT_SEND_MESSAGE)

CHAT_SEND_MESSAGE(ChatController.class, "/chat/sendMessage", "发送消息"),
```

6. 邀请好友进入群聊
```java
```

关于socket定向推送的出参  

含有字段flag int

0：收到好友申请
1：好友申请通过
2：动态被点赞
3：动态被评论
4：收到文本消息
5: 收到图片消息
6: 收到音频消息
7: 收到视频消息
8: 收到定位消息
9: 被拉入群聊

