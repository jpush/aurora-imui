## AuroraIMUI

[中文文档](./APIs_zh.md)

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
To use below APIs, You should get AuroraIMUI ref
```jsx
<AuroraIMUI ref={(imui) => {this.imui = imui}}/>
```
### initialMessages
**Param: PropTypes.array:** [{[message](../../ReactNative/docs/Models.md#message)}]
Used to specify a list of initialization messages for MessageList.
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

Append message array in MessageList bottom.
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

Update specify message(match msgId) in MessageList.
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

Insert messages to the top of the MessageList, usually use this method to load history messages. 
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

Remove message by message(match with msgId).
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
Clear all message in MessageList.
```jsx
  if(this.imui) { // this.imui is AuroraIMUI ref
    this.imui.removeAllMessage()
  }
```

### sendText
This function will clean InputView's TextInput.textvalue, and fire [onSendText](#onsendtext) callback.
This's useful when you want to custom renderRight to override default rightItem(send text item).
```jsx
  if(this.imui) { // this.imui is AuroraIMUI ref
    this.imui.sendText()
  }
```

## Event Callback

### onSendText
Trigger onSendText when default rightItem be clicked(or call [imui.sendText](#sendtext) manualy). 
```jsx
  <AuroraIMUI 
    onSendText={ (textStr) => console.log(textStr) }
  />
```

### onInputTextChanged
Trigger onInputTextChanged when InputView.TextInput.text did changed
```jsx
  <AuroraIMUI 
    onInputTextChanged={ (textStr) => console.log(textStr) }
  />
```
### onInputViewSizeChanged

### onPullToRefresh
Fires when pull MessageList to top, example usage: please refer sample's onPullToRefresh method.
```jsx
  <AuroraIMUI 
    onPullToRefresh={ () => { /* you can load history message here */}
  />
```
### onAvatarClick
**NOTE:** Invalid when return a [customMessage](#renderrow).

PropTypes.function: `( userinfo ) => { }`

Fires when click avatar.

### onMsgClick
**NOTE:** Invalid when return a [customMessage](#renderrow).

PropTypes.function: `(message) => { }`

Fires when click message row.

### onStatusViewClick
**NOTE:** Invalid when return a [customMessage](#renderrow).

PropTypes.function: `(message) => { }`

Fires when click message status view.


### onMsgContentClick
**NOTE:** Invalid when return a [customMessage](#renderrow).

PropTypes.function: `(message) => { }`

Fires when click message content view.

### onMsgContentLongClick
**NOTE:** Invalid when return a [customMessage](#renderrow).

PropTypes.function: `(message) => { }`

Fires when long press message content view.

## Custom Styles

## MessageList

### renderRow
**NOTE:** If you override message row, the message callback will be Invalid.

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
Used to set status View position.

```jsx
<AuroraIMUI 
  stateContainerStyles={{justifyContent: 'center'}}
/>
```
### avatarContainerStyles
Used to set avatar view position.

```jsx
<AuroraIMUI 
  avatarContainerStyles={{justifyContent: 'flex-start'}}
/>
```

## InputView

### textInputProps
This props is useful when you want to adjust TextInput props(like placeHolder or placeholder Color).

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

If you want to add item in InputView' left postion, you can use renderLeft to return a component.

```jsx
<AuroraIMUI
    renderLeft={ () => <View /> }
  />
```
### renderRight
If you want to custom send buttom you can use render (maybe you need [sendText](#sendtext) function).
```jsx
<AuroraIMUI
    renderRight={ () => <View /> }
  />
```

### renderBottom
you can add a bottom bar in InputView with this api,(NOTE: component need have a explicit height)
```jsx
<AuroraIMUI
    renderBottom={ () => <View style={{height: 20}}/> }
  />
```

### maxInputViewHeight
**Params: PropTypes.number** default value is 120.

It‘s useful when you want to add a bottom bar in inputView(use [renderBottom](#renderBottom) callback), 
Usually you need use it to adjust maxInputView's height.

