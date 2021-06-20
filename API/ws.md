# ws

※ flag字段可能是多余的，如果前端会指定biztype可能就不需要这个字段

## 1. 接受好友申请

### Biztype

```java
@BizType(BizTypeEnum.FRIEND_CHECK_FRIEND_REQUEST)

FRIEND_CHECK_FRIEND_REQUEST(FriendController.class, "/friend/checkFriendRequest", "审核好友申请")
```

### Response

```coffeescript
{
    "flag": int 0
    "from_username": 发送者用户名
    "from_nickname": 发送者昵称
    "from_avatar": 发送者头像
    "extra": 申请理由
    "status: 等待状态
}
```

## 1. 发出好友申请  

### Biztype

```java
@BizType(BizTypeEnum.FRIEND_NEW_FRIEND_REQUEST)  

FRIEND_NEW_FRIEND_REQUEST(FriendController.class, "/friend/newFriendRequest", "添加陌生用户为好友")
```

### Response

```coffeescript
{
    "flag": int 1
    "to_username": 接收方用户名
    "extra": 附加信息
}
```

## 3. 点赞动态

### Biztype

```java
@BizType(BizTypeEnum.MOMENT_LIKE_MOMENT)

MOMENT_LIKE_MOMENT(MomentController.class, "/moment/likeMoment", "点赞动态")
```

### Response

```coffeescript
{
    "flag": int 2
    "username": 点赞的用户
    "nickname": 点赞用户的昵称
    "remark": 点赞用户备注
}
```

## 4. 评论动态

### Biztype

```java
@BizType(BizTypeEnum.MOMENT_COMMENT_ON_MOMENT)

MOMENT_COMMENT_ON_MOMENT(MomentController.class, "/moment/commentOnMoment", "评论动态"),
```

### Response

```coffeescript
{
    "flag": int 3
    "username": 评论的用户
    "nickname": 昵称
    "remark": 备注
    "content": 评论内容
}
```

## 5. 发送消息

### Biztype

```java
@BizType(BizTypeEnum.CHAT_SEND_MESSAGE)

CHAT_SEND_MESSAGE(ChatController.class, "/chat/sendMessage", "发送消息"),
```

### Response

```coffeescript
{
    "flag": int 4 5 6 7 8 分别对应文本 图片 音频 视频 定位
    "linkId": 私聊或群聊的id
    "name": 消息发送方名称，私聊就是对方的昵称或备注，群聊是群名称
    "isMultiple": boolean 是否是群聊
    "type": int 0 1 2 3 4 分别对应文本 图片 音频 视频 定位
    "msgId": 
    "sendTime": 
    "text": 
    "image": 
    "audio": 
    "video": 
    "longitude": 
    "latitude": 
    "unread": 
}
```

## 6. 创建群聊

### Biztype

```java
@BizType(BizTypeEnum.CHAT_CREATE_GROUP)

CHAT_CREATE_GROUP(ChatController.class, "/chat/createGroup", "创建群聊"),

```

### Response

```coffeescript
{
    "flag": 9
    "groupName": 
}
```

## 7. 邀请好友进入群聊

### Biztype

```java
@BizType(BizTypeEnum.CHAT_INVITE_TO_GROUP)

CHAT_INVITE_TO_GROUP(ChatController.class, "/chat/inviteToGroup", "邀请好友进入群聊"),
```

### Response

```coffeescript
{
    "flag": 10
    "groupName": 
}
```