[English Document](./Models.md)

## Contents

- [UserInfo](#userinfo)
- [Message](#message)
  - [TextMessage](#textmessage)
  - [ImageMessage](#imagemessage)
  - [VoiceMessage](#voiceMessage)
  - [VideoMessage](#videomessage)
  - [CustomMessage](#customessage)
  - [EventMessage](#eventmessage)

### UserInfo

```javascript
fromUser = {
  userId: ""
  displayName: ""
  avatarPath: "avatar image path"
}
```



### Message

**status 必须为以下四个值之一: "send_succeed", "send_failed", "send_going", "download_failed"，如果没有定义这个属性， 默认值是 "send_succeed".**

#### TextMessage

```javascript
  textMessage = {
    msgId: "msgid",
    status: "send_going",
    msgType: "text",
    isOutgoing: true,
    text: "text",
    fromUser: {},
    extras: {}// 选填，可以在消息中添加附加字段
}
```

#### ImageMessage

```javascript
imageMessage = {
    msgId: "msgid",
    msgType: "image",
    isOutGoing: true,
    progress: "progress string",
    mediaPath: "image path",
    fromUser: {}，
    extras: {}// 选填，可以在消息中添加附加字段
}
```

####  VoiceMessage

```javascript
message = {
    msgId: "msgid",
    msgType: "voice",
    isOutGoing: true,
    duration: number, // 注意这个值有用户自己设置时长，单位秒
    mediaPath: "voice path",
    fromUser: {},
    extras: {}// 选填，可以在消息中添加附加字段
}
```

#### VideoMessage

```javascript
videoMessage = {  // video message
    msgId: "msgid",
    status: "send_failed",
    msgType: "video",
    isOutGoing: true,
    druation: number,
    mediaPath: "voice path",
    fromUser: {},
    extras: {}// 选填，可以在消息中添加附加字段
}s
```

####  CustomMessage

```javascript
customMessage = {  // custom message
    msgId: "msgid",
    msgType: "custom",
    status: "send_failed",
    isOutgoing: true,
    contentSize: {height: 100, width: 100},
    content: "<h1>custom message will render html string</h1>", // content 为 html 字符串，应尽量避免 <script> 标签
    fromUser: {}, 
    extras: {}// 选填，可以在消息中添加附加字段
}
```

#### EventMessage

```javascript
eventMessage = {  // event message
    msgId: "msgid",
    msgType: "event",
    text: "the event text"
}
```



