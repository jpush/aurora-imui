## AuroraIMUI
[English Document](./APIs.md)

- [Useful APIs](#usefulapis)
  - [initialMessages](#initialMessages)
  - [appendMessages](#appendmessages)
  - [updateMessage](#updatemessage)
  - [insertMessagesToTop](#insertMessagesToTop)
  - [removeMessage](#removemessage)
  - [removeAllMessage](#removeAllMessage)
  - [sendText](#sendtext)

- [Event Callback](#eventcallback)
  - [onSendText](#onsendtext)
  - [onInputTextChanged](#oninputtextchanged)
  - [onInputViewSizeChanged](#oninputviewsizechanged)
  - [onPullToRefresh](#onpulltorefresh)
  - [onAvatarClick](#onavatarclick)
  - [onMsgClick](#onmsgclick)
  - [onStatusViewClick](#onstatusviewclick)
  - [onMsgContentClick](#onmsgcontentclick)
  - [onMsgContentLongClick](#oncsgcontentlongclick)

- [Custom Styles](#customstyles)
  - [MessageList]()
    - [renderRow](#renderrow)
    - [stateContainerStyles](#statecontainerstyles)
    - [avatarContainerStyles](#avatarcontainerstyles)
  - [InputView]()
    - [textInputProps](#textinputprops)
    - [renderLeft](#renderleft)
    - [renderRight](#renderright)
    - [renderBottom](#renderbottom)
    - [maxInputViewHeight](#maxinputviewheight)



## Useful APIs
**注意： 在使用下面的之前，先要获取 AuroraIMUI ref**
```jsx
<AuroraIMUI ref={(imui) => {this.imui = imui}}/>
```
### initialMessages
**Param: PropTypes.array:** [{[message](../../ReactNative/docs/Models.md#message)}]
用于设置 MessageList 的初始化消息。
ex:
```jsx
<AuroraIMUI 
  initialMessages={[
    {msgId: '0',msgType: "event",text: "Text message 1"},
    {msgId: '1',msgType: "text", text: "Text message 2"},
  ]}
/>
```

### appendMessages
**Param: PropTypes.array:**  [{[message](../../ReactNative/docs/Models.md#message)}]

在 MessageList 的底部插入消息。
```jsx
  if(this.imui) { // this.imui is AuroraIMUI ref
    this.imui.appendMessages([
      {
        text,
        msgType: 'text',
        msgId: 'id_3', // NOTO: the msgId must be unique.
      }
    ])
  }
```

### updateMessage
**Param: PropTypes.object:** {[message](../../ReactNative/docs/Models.md#message)}

更新指定消息，该方法会匹配相同的 msgId（如果消息列表中没有该 msgId，则不做处理）。
```jsx
  if(this.imui) { // this.imui is AuroraIMUI ref
    this.imui.updateMessage({
        text,
        msgType: 'text',
        msgId: 'id_4', // NOTO: the msgId must be unique.
      })
  }
```

### insertMessagesToTop
**Param: PropTypes.array:** [{[message](../../ReactNative/docs/Models.md#message)}]

插入消息到 MessageList 的顶部（当触发[下拉刷新回调](#onpulltorefresh)时可以用该方法添加历史消息）。
```jsx
  if(this.imui) { // this.imui is AuroraIMUI ref
    this.imui.insertMessagesToTop([
      {
        text,
        msgType: 'text',
        msgId: 'id_1', // NOTO: the msgId must be unique.
      }
    ])
  }
```

### removeMessage
**Param: PropTypes.object:** {[message](../../ReactNative/docs/Models.md#message)}

移除 MessageList 中指定的消息，该方法会匹配删除相同的 msgId（如果消息列表中没有该 msgId，则不做处理）。
```jsx
  if(this.imui) { // this.imui is AuroraIMUI ref
    this.imui.removeMessage({
        text,
        msgType: 'text',
        msgId: 'id_1', // NOTO: the msgId must exit in MessageList.
      })
  }
```
### removeAllMessage

清楚 MessageList 中所有的消息
```jsx
  if(this.imui) { // this.imui is AuroraIMUI ref
    this.imui.removeAllMessage()
  }
```

### sendText
这个方法会清空 InputView's TextInput.textvalue 的内容，并且触发 [onSendText](#onsendtext) 事件回调，
一般来说不需要调用这个方法，如果你想要自定义发送文本按钮这个方法就很有用。

```jsx
  if(this.imui) { // this.imui is AuroraIMUI ref
    this.imui.sendText()
  }
```

### Event Callback

### onSendText
当按下默认的发送按钮，会触发这个回调。当主动调用 [AuroraIMUI.sendText](#sendtext) 方法也会触发这个回调。
```jsx
  <AuroraIMUI 
    onSendText={ (textStr) => console.log(textStr) }
  />
```

### onInputTextChanged
当 InputView.TextInput.text 内容发生变化的时候回触发这个回调
```jsx
  <AuroraIMUI 
    onInputTextChanged={ (textStr) => console.log(textStr) }
  />
```
### onInputViewSizeChanged

### onPullToRefresh
当下拉刷新 MessageList 的时候回触发这个回调。
```jsx
  <AuroraIMUI 
    onPullToRefresh={ () => { /* you can load history message here */}
  />
```
### onAvatarClick
**NOTE:** 当使用自定义消息的时候 AuroraIMUI.onAvatarClick 会失效 [customMessage](#renderrow)，需要 Message 中重新传入回调.

PropTypes.function: `( userinfo ) => { }`

点击头像的时候会触发这个回调。

### onMsgClick
**NOTE:** 当使用自定义消息的时候 AuroraIMUI.onAvatarClick 会失效 [customMessage](#renderrow)，需要 Message 中重新传入回调.

PropTypes.function: `(message) => { }`

点击消息行的时候回触发这个回调。

### onStatusViewClick
**NOTE:** 当使用自定义消息的时候 AuroraIMUI.onAvatarClick 会失效 [customMessage](#renderrow)，需要 Message 中重新传入回调.

PropTypes.function: `(message) => { }`

点击 status view 的时候会触发这个回调。

### onMsgContentClick
**NOTE:** 当使用自定义消息的时候 AuroraIMUI.onAvatarClick 会失效 [customMessage](#renderrow)，需要 Message 中重新传入回调.

PropTypes.function: `(message) => { }`

点击消息内容（泡泡中的内容）的时候回触发这个回调，如果 MessageContent 中有添加手势，可能会截获该事件回调。

### onMsgContentLongClick
**NOTE:** 当使用自定义消息的时候 AuroraIMUI.onAvatarClick 会失效 [customMessage](#renderrow)，需要 Message 中重新传入回调.

PropTypes.function: `(message) => { }`

长按消息内容（泡泡中的内容）的时候回触发这个回调，如果 MessageContent 中有添加手势，可能会截获该事件回调。

## Custom Styles

## MessageList

### renderRow
**NOTE:** 使用 renderRow 返回自定义的消息，AuroraIMUI message row 相关的事件回调会失效，需要重新传入，参考如下示例。

```jsx
  import {AuroraIMUI, Message} from "aurora-imui";

  // If you want to add a custom message 
  // You can pass a function to renderRow which return a component 
  renderCustomRow(message) {
    switch(message.msgType) {
      case 'image':
        // custom message 
        return <Message 
          {...{ ...message, 
                messageContent: (message) => {return <MessageImageContent {...message}/>}
                }}
          avatarContent={(userInfo) => (<Image source={require('./assert/ironman.png')} />)}
          onMsgClick={this.onMsgClick} // you need pass callback
          onAvatarClick={this.onAvatarClick} // you need pass callback
          onStatusViewClick={this.onStatusViewClick} // you need pass callback
        />
      default:
        return null
    }
  }

  <AuroraIMUI
    renderRow={this.renderCustomRow}
  />
```

### stateContainerStyles
可以用指定 state view 的位置。

```jsx
<AuroraIMUI 
  stateContainerStyles={{justifyContent: 'center'}}
/>
```
### avatarContainerStyles
用来指定头像的位置。

```jsx
<AuroraIMUI 
  avatarContainerStyles={{justifyContent: 'flex-start'}}
/>
```

## InputView

### textInputProps
如果你想要调整 TextInput 的属性或者样式（例如 placeHolder / placeHoder color  / style），可以使用这个属性。

```jsx
<AuroraIMUI
    textInputProps={{
          placeholder: 'Input Text message',
          multiline: true,
          style: {margin: 8, color: 'red'}
        }}
  />
```
### renderLeft
PropTypes.function: `() => Component`

如果希望在 InputView’s textInput 的左侧添加额外的 item， 可以使用这个方法换回自己的 component。
```jsx
<AuroraIMUI
    renderLeft={ () => <View /> }
  />
```
### renderRight

如果希望在 InputView’s textInput 的右侧添加额外的 item，可以使用这个方法换回自己的 component，通常自定义发送按钮可以使用这个属性。

```jsx
<AuroraIMUI
    renderRight={ () => <View /> }
  />
```

### renderBottom

如果希望在 InputView' textInput 下面添加 item bar 可以使用这个属性返回 component，（注意：返回的 component 需要明确指定高度）
```jsx
<AuroraIMUI
    renderBottom={ () => <View style={{height: 20}}/> }
  />
```

### maxInputViewHeight
**Params: PropTypes.number** 默认为 120

调整 InputView 的最大高度，当你希望跳转 InputView‘ textInput 的最大行数，肯可能需要用到这个参数。
当你用到 [renderBottom](#renderBottom) 这个属性返回自定义的 bottomBar 时，maxInputViewHeight 属性很可能会被用到这个参数。


