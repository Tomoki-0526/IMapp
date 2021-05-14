# 获取通讯录

Method: GET  
URL: /friend/getFriends
Correct Response:
```coffeescript
{
    'friends': {
        '分组1名称': [朋友列表],
        '分组2名称': [朋友列表],
        ...
    }
}
```
一个朋友的格式:
```coffeescript
{
    'friend_username': 朋友的用户名,
    'friend_avatar': 朋友的头像,
    'friend_name': 朋友的昵称或备注
}
```
Error Response:
```coffeescript
{
    'msg': '获取通讯录失败'
}
```

# 查看新的朋友列表

Method: GET  
URL: /friend/getNewFriends
Correct Response:
```coffeescript
{
    'new_friends': [新朋友列表]
}
```
一个新朋友的格式:
```coffeescript
{
    'friend_id': 新朋友的ID,
    'avatar': 新朋友的头像,
    'nickname': 新朋友的昵称,
    'reason': 申请理由,
    'status': 状态（0-等待 1-接受 2-拒绝）
}
```
Error Response:
```coffeescript
{
    'msg': '获取失败'
}
```

# 审核好友申请

Method: POST
URL: /friend/checkNewFriend
Request:
```coffeescript
{
    'friend_id': 新朋友ID,
    'result': boolean，0-拒绝 1-接受
}
```
Correct Response:
```coffeescript
{
    'msg': '添加好友成功'
}
```
Error Response:
```coffeescript
{
    'msg': '添加好友失败'
}
```

# 查看星标朋友

Method: GET  
URL: /friend/getStarFriends
Correct Response:
```coffeescript
{
    'star_friends': [星标朋友列表]
}
```
一个星标朋友的格式与普通朋友相同
Error Response:
```coffeescript
{
    'msg': '获取失败'
}
```

# 查看某一个好友的信息

Method: GET  
URL: /friend/getFriendInfo
Request:
```coffeescript
{
    'friend_username': 该用户的用户名
}
```
Correct Response:  
```coffeescript
{
    'avatar': 图片路径,
    'nickname': 昵称,
    'username': 用户名,
    'remark': 备注名,
    'gender': boolean（0-男 1-女）,
    'age': int,
    'birthday': datetime,
    'telephone': 手机号码,
    'group': 分组名称,
    'star': 是否标星,
    'signature': 个性签名
}
```
Error Response:
```coffeescript
{
    'msg': '获取信息失败'
}
```

# 设置朋友备注

Method: POST  
URL: /friend/setFriendRemark/{friend_id}/
Request:
```coffeescript
{
    'remark': 备注名
}
```
Correct Response:
```coffeescript
{
    'msg': '修改备注名成功'
}
```
Error Response:
```coffeescript
{
    'msg': '修改备注名失败'
}
```

# 设置星标朋友

Method: POST  
URL: /friend/setFriendStar/{friend_id}/
Correct Response:
```coffeescript
{
    'msg': '设置星标成功'
}
```
Error Response:
```coffeescript
{
    'msg': '设置星标失败'
}
```

# 添加分组

Method: POST
URL: /friend/addGroup
Request:
```coffeescript
{
    'group_name': 分组名称
}
```
Correct Response:
```coffeescript
{
    'msg': '添加分组成功'
}
```
Error Response:
```coffeescript
{
    'msg': '添加分组失败'
}
```

# 查看所有分组

Method: GET
URL: /friend/getGroups
Correct Response:
```coffeescript
{
    'group': [分组列表]
}
```
一个分组的格式:
```coffeescript
{
    'id': 分组ID,
    'name': 分组名称
}
```
Error Response:
```coffeescript
{
    'msg': '获取分组失败'
}
```

# 将好友添加到分组

Method: POST
URL: /friend/addFriendToGroup/{friend_id}/
Request:
```coffeescript
{
    'group_id': 分组ID
}
```
Correct Response:
```coffeescript
{
    'msg': '添加成功'
}
```
Error Response:
```coffeescript
{
    'msg': '添加失败'
}
```

# 删除好友

Method: POST
URL: /friend/removeFriend/{friend_id}/
Correct Response:
```coffeescript
{
    'msg': '删除好友成功'
}
```
Error Response:
```coffeescript
{
    'msg': '删除好友失败'
}
```

# 查找已有联系人

Method: GET
URL: /friend/findFriend
Request:
```coffeescript
{
    'content': 字符串（用户名、昵称、备注、手机号）
}
```
Correct Response:
```coffeescript
{
    'result': [搜索结果列表]
}
```
一个结果的格式:
```coffeescript
{
    'friend_id': 好友ID,
    'avatar': 头像,
    'name': 昵称或备注
}
```
Error Response:
```coffeescript
{
    'msg': '查找失败'
}
```

# 搜索陌生用户

Method: GET
URL: /friend/findStranger
Request:
```coffeescript
{
    'stranger_username': 查找的联系人
}
```
Correct Response:
```coffeescript
{
    'stranger_username': 陌生人用户名,
    'stranger_nickname': 陌生人昵称,
    'stranger_avatar': 陌生人头像
}
```
Error Response:
```coffeescript
{
    'msg': '查找失败'
}
```

# 获取陌生用户信息

Method: GET
URL: /friend/getStrangerInfo
Request:
```coffeescript
{
    'stranger_username': 陌生人用户名
}
```
Correct Response:
```coffeescript
{
    'stranger_username': 陌生人用户名,
    'stranger_nickname': 昵称,
    'stranger_avatar': 头像,
    'stranger_gender': 性别,
    'stranger_signature': 个性签名
}
```
Error Response:
```coffeescript
{
    'msg': '获取失败'
}
```

# 添加陌生用户为好友

Method: POST
URL: /friend/newFriendRequest
Request:
```coffeescript
{
    'username': 新朋友用户名
}
```
Correct Response:
```coffeescript
{
    'msg': '提交申请成功'
}
```
Error Response:
```coffeescript
{
    'msg': '提交申请失败'
}
```