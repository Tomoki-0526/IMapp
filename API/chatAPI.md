# 聊天功能API

## 1. 获取聊天关系（私聊） √

※ 用户进入聊天界面时调用  

Method: POST  
URL: /chat/getChatLink  
Request:
```coffeescript
{
    "toUsername": string 接收方用户名
}
```
Correct Response:
```coffeescript
{
    "linkId": string 聊天关系id
    "username": 
    "nickname":
    "remark":
    "avatar":
    "msgs": msgItem[] 消息列表
}
```
一条msgItem:
```coffeescript
{
    "msgId": string 消息id
    "sendTime": 
    "fromMyself": boolean 是不是由自己发出的
    "type": 0-文本 1-图片 ……
    "text":
    "image":
    "audio":
    "video":
    "longitude": double 经度
    "latitude": double 纬度
}
```

## 2. 退出聊天界面（私聊） √

※ 用户退出聊天界面时调用

Method: POST  
URL: /chat/quitChat  
Request:
```coffeescript
{
    "linkId": 聊天关系id
}
```

## 3. 发送消息（私聊群聊都可） √

Method: POST  
URL: /chat/sendMessage  
Request:
```coffeescript
{
    // 这几个必须有
    "linkId": string 聊天关系id(如果是群聊就传群聊的id，字段名不变)
    "isMultiple": string 是否是群聊 false-私聊 true-群聊 传这个字符串给我
    "type": string 消息类型 0-文本 1-图片 2-音频 3-视频 4-定位
    // 这几个看着给
    "text": 文本
    "image": FileUpload
    "audio":
    "video":
    "longtitude": 经度
    "latitude": 纬度
}
```
定向发送给接收方的推送
```coffeescript
{
    "linkId": string 聊天id
    "username": 发送方用户名
    "isMultiple": boolean 是不是群聊 0-私聊 1-群聊
    "msgId": string 消息id
    "sendTime": string
    "text":
    "image":
    "audio":
    "video":
    "longitude": double 经度
    "latitude": double 纬度
    "unread": 未读数
}
```

## 4. 删除历史聊天记录（私聊群聊） √

Method: POST  
URL: /chat/removeMessages  
Request:
```coffeescript
{
    "linkId": 聊天关系id,
    "msgs": string[] 聊天记录id数组
}
```

## 5. 查看当前所有聊天 √

Method: GET
URL: /chat/getChattings
Correct Response:
```coffeescript
{
    "chattings": ChatItem[]
}
```
一条ChatItem:  
```coffeescript
{
    "avatar": 
    "linkId": 
    "multiple": 这里我写的是isMultiple但是不知道怎么就变了
    "name": 
    "latestMsg": 
    "sendTime": 
}
```

---

## 6. 创建群聊 √

Method: POST  
URL: /chat/createGroup  
Request:
```coffeescript
{
    "groupName": 群名称
    "members": string[] 拉进来的用户名，不包含自己
}
```
ws出参
```coffeescript
{
    "flag": ws出参类型
    "groupName": 群名称
}
```

## 7. 查看所有群 √

Method: GET  
URL: /chat/getGroups  
Correct Response:
```coffeescript
{
    "groups": GroupItem[]
}
```
一条GroupItem:
```coffeescript
{
    "groupId": 群聊id
    "groupAvatar": 群头像
    "groupName": 群名称
}
```

## 8. 进入群聊 √

Method: POST  
URL: /chat/enterGroupChat  
Request:
```coffeescript
{
    "groupId": 
}
```
Correct Response:
```coffeescript
{
    "groupAvatar": 群头像
    "groupName": 群名称
    "msgs": 群消息记录
}
```

## 9. 离开群聊界面 √

Method: POST  
URL: /chat/quitGroupChat  
Request:
```coffeescript
{
    "groupId": 群聊id
}
```

## 10. 查看群成员 √

Method: GET  
URL: /chat/getGroupMembers  
Request:
```coffeescript
{
    "groupId": 
}
```
Correct Response:
```coffeescript
{
    "members": memberItem[]
}
```
一条memberItem:
```coffeescript
{
    "avatar":
    "username":
    "nickname":
    "remark":
    "isFriend": boolean
}
```

## 11. 邀请好友进入群聊 √

Method: POST  
URL: /chat/inviteToGroup  
Request:
```coffeescript
{
    "groupId": 
    "usernames": 
}
```
ws出参
```coffeescript
{
    "flag": ws出参类型
    "groupName": 群名称
}
```

## 12. 退出群聊 √

Method: POST
URL: /chat/quitGroup
Request:
```coffeescript
{
    "groupId": 
}
```