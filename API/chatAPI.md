# 聊天功能API

HttpRequest的URL为  http://8.140.133.34:7563 + /xxx  
Websocket的URL为    ws://8.140.133.34:7562 + /ws/xxx  

## 1. 获取聊天关系 √

※ 用户点击“聊天”或聊天列表中的一项进入聊天界面时调用  

Method: POST  
URL: /chat/getChatUserLink  
Request:
```coffeescript
{
    "to_username": string 接收方用户名
}
```
Correct Response:
```coffeescript
{
    "link_id": string 聊天关系id
}
```

## 2. 退出聊天界面

Method: POST  
URL: /chat/quitChat  
Request:
```coffeescript
{
    "link_id": 聊天关系id
}
```

## 3. 发送消息

Method: POST  
URL: /ws/chat/sendMessage  
Request:
```coffeescript
{
    "link_id": string 聊天关系id,
    "to_username": string 接收方用户名,
    "content": string 消息内容,
    "send_time": Date 发送时间,
    "type": int 消息类型(0-文本 1-图片 2-音频 3-视频 4-位置信息)
}
```

## 4. 删除聊天列表项

Method: POST  
URL: /chat/removeChatItem  
Request:
```coffeescript
{
    "link_id": 聊天关系id
}
```

## 5. 查看历史聊天记录

Method: GET  
URL: /chat/getHistory  
Request:
```coffeescript
{
    "link_id": 聊天关系id
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
    "msg_id": 聊天记录id,
    "content": 内容,
    "from_username": 发送方用户名,
    "to_username": 接收方用户名,
    "send_time": 发送时间
}
```

## 6. 删除历史聊天记录(批量)

Method: POST  
URL: /chat/removeHistory  
Request:
```coffeescript
{
    "link_id": 聊天关系id,
    "msgs": string[] 聊天记录id数组
}
```