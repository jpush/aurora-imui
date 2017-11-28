[中文文档](./Models_zh.md)

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

**status must be one of the following values: "send_succeed", "send_failed", "send_going", "download_failed"，if you don't specify it, default value will be "send_succeed".**

#### TextMessage

```javascript
  textMessage = {
    msgId: "msgid",
    status: "send_going",
    msgType: "text",
    isOutgoing: true,
    text: "text",
    fromUser: {},
    extras: {}// option
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
    extras: {}// option
}
```

####  VoiceMessage

```javascript
message = {
    msgId: "msgid",
    msgType: "voice",
    isOutGoing: true,
    duration: number, // duration of voice message, second
    mediaPath: "voice path",
    fromUser: {},
    extras: {}// option
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
    extras: {}// option
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
    content: "<h1>custom message will render html string</h1>", // content is html format, avoid to use <script>
    fromUser: {}, 
    extras: {}// option
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



