# 动态功能API

## 发布动态

Method: POST
URL: /moment/publishMoment
Request:
```coffeescript
{
    "type": 0-文本 1-图片 2-图文 3-视频
    "content": 文本内容，如果没有内容就不传该字段，下同
    "images": Multipartfile数组
    "video": Multipartfile
}
```

## 删除动态

Method: POST
URL: /moment/removeMoment
Request:
```coffeescript
{
    "momentId": string
}
```

## 查看动态

Method: GET
URL: /moment/getMoments
Correct Response:
```coffeescript
{
    "moments": 动态数组
}
```
一条动态的格式
```coffeescript
{
    "momentId": string
    "avatar": 发布者头像
    "nickname": 发布者昵称
    "remark": 发布者备注，如果没有则为空串
    "type": 0-文本 1-图片 2-图文 3-视频
    "content": string 文本内容，如果没有则为空串，下同
    "images": string 图片路径
    "video": string 视频路径
    "likes": int 点赞数
    "likeUsers": 点赞用户数组
    "comments": 评论数组
}
```
一个点赞用户的格式
```coffeescript
{
    "likeAvatar": 
    "likeUsername":
    "likeNickname":
    "likeRemark":
}
```
一个评论的格式
```coffeescript
{
    "commentUsername": 
    "commentContent":
}
```

## 点赞动态

Method: POST
URL: /moment/likeMoment
Request:
```coffeescript
{
    "momentId": string
}
```

## 撤销点赞动态

Method: POST
URL: /moment/cancelLikeMoment
Request:
```coffeescript
{
    "momentId": string
}
```

## 评论动态

Method: POST
URL: /moment/commentOnMoment
Request:
```coffeescript
{
    "momentId": string
    "content": string
}
```
Correct Response:
```coffeescript
{
    "commentId": string
}
```

## 删除评论

Method: POST
URL: /moment/removeComment
Request:
```coffeescript
{
    "commentId": string
}
```