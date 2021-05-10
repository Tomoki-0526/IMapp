# 登录

Method: POST  
URL: /user/login  
Request:  
```coffeescript
{
    'username': 长度不超过20的字符串（英文、数字、下划线）,
    'password': 同上
}
```
Correct Response:
```coffeescript
{
    'msg': '登录成功'
}
```
Error Response:
```coffeescript
{
    'msg': '用户名或密码错误'
}
```

# 注册

Method: POST  
URL: /user/register  
Request:  
```coffeescript
{
    'username': 长度不超过20的字符串（英文、数字、下划线）,
    'password': 同上,
    'nickname': 长度不超过20的字符串（任意字符）
    'telephone': 11位特定数字
}
```
Correct Response:
```coffeescript
{
    'msg': '注册成功'
}
```
Error Response:
```coffeescript
{
    'msg': '注册失败'
}
```

# 登出

Method: POST  
URL: /user/logout  
Correct Response:  
```coffeescript
{
    'msg': '成功退出登录'
}
```
Error Response:
```coffeescript
{
    'msg': '登出失败，请检查登录状态'
}
```

# 修改密码

Method: POST  
URL: /user/modifyPassword  
Request:  
```coffeescript
{
    'old_pwd': '',
    'new_pwd': 长度不超过20的字符串（英文、数字、下划线）,
    'confirm_pwd': 同上
}
```
Correct Response:  
```coffeescript
{
    'msg': '密码修改成功'
}
```
Error Response:
```coffeescript
{
    'msg': '密码修改失败'
}
```

# 查看个人信息

Method: POST  
URL: /user/getInfo  
Correct Response:  
```coffeescript
{
    'avatar': 图片路径,
    'nickname': 昵称,
    'username': 用户名,
    'gender': boolean（0-男 1-女）,
    'age': int,
    'birthday': datetime,
    'telephone': 手机号码,
    'signature': 个性签名
}
```
Error Response:
```coffeescript
{
    'msg': '获取信息失败'
}
```

# 更新个人信息

Method: POST  
URL: /user/updateInfo  
Request:  
```coffeescript
{
    'avatar': 图片路径,
    'username': 长度不超过20的字符串（英文、数字、下划线）,
    'nickname': 长度不超过20的字符串（任意字符）,
    'gender': boolean（0-男 1-女）,
    'birthday': datetime,
    'signature': 长度不超过100的字符串
}
```
Correct Response:  
```coffeescript
{
    'msg': '更新成功'
}
```
Error Response:
```coffeescript
{
    'msg': '更新失败'
}
```

# 更换手机号码

Method: POST  
URL: /user/changeTelNum  
Request:  
```coffeescript
{
    'telephone': 11位数字
}
```
Correct Response:  
```coffeescript
{
    'msg': '手机号更新成功'
}
```
Error Response:
```coffeescript
{
    'msg': '手机号更新失败'
}
```

# 生成二维码

Method: POST  
URL: /user/createQR  
Correct Response:  
```coffeescript
{
    'msg': '生成二维码成功'
}
```
Error Response:
```coffeescript
{
    'msg': '生成二维码失败'
}
```