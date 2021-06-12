# 聊天功能API

## 1. 获取聊天关系 √

※ 用户点击“聊天”或聊天列表中的一项进入聊天界面时调用  

Method: POST  
URL: /chat/getChatUserLink  
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
}
```

## 2. 退出聊天界面 √

※ 用户退出聊天界面或删除聊天条目时调用

Method: POST  
URL: /chat/quitChat  
Request:
```coffeescript
{
    "linkId": 聊天关系id
}
```

## 3. 发送消息(私聊) √

Method: POST  
URL: /ws/chat/sendMessage  
Request:
```coffeescript
{
    "linkId": string 聊天关系id,
    "toUsername": string 接收方用户名,
    "content": string 消息内容,
    "sendTime": string 发送时间(yyyy-MM-dd hh:mm:ss),
    "type": int 消息类型(0-文本 1-图片 2-音频 3-视频 4-位置信息)
}
```
定向发送给接收方的推送
```coffeescript
{
    "content": string 消息内容,
    "type": int 消息类型,
    "window": boolean 接收方是否在聊天窗口,
    "unread": int 未读数
}
```

## 4. 查看历史聊天记录 √

Method: GET  
URL: /chat/getHistory  
Request:
```coffeescript
{
    "linkId": 聊天关系id
}
```
Correct Response:
```coffeescript
{
    "history": 按时间排序的历史记录数组
}
```
一条历史记录的格式为
```coffeescript
{
    "msgId": 聊天记录id,
    "fromAvatar": 发送方头像,
    "fromUsername": 发送方用户名,
    "content": 内容,
    "type": 类型,
    "send_time": 发送时间
}
```

## 5. 删除历史聊天记录(批量) √

Method: POST  
URL: /chat/removeHistory  
Request:
```coffeescript
{
    "linkId": 聊天关系id,
    "msgs": string[] 聊天记录id数组
}
```