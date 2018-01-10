# API

[中文文档](./APIs_zh.md)

#### Usage

```javascript
import {
  NativeModules,
} from 'react-native';

import IMUI from 'aurora-imui-react-native';
var MessageList = IMUI.MessageList;
var ChatInput = IMUI.ChatInput;
const AuroraIMUIController = IMUI.AuroraIMUIController; // the IMUI controller, use it to operate  messageList and ChatInput.

// add MessageList and ChatInput in render() 
<MessageList />
<ChatInput />
```

Refer to iOS,Android example

> [Example usage](./../sample/app/app.js)



- [AuroraIMUIController](#auroraimuicontroller)
  - [appendMessages](#appendmessages)
  - [updateMessage](#updatemessage)
  - [insertMessagesToTop](#insertmessagestotop)
  - [stopPlayVoice](#stopplayvoice)
  - [removeMessage](#removeMessage)
  - [removeAllMessage](#removeAllMessage)
  - [Event](#auroraimuicontrollerevent)
    - [MessageListDidLoadListener](#messagelistdidloadlistener)


- [MessageList](#messagelist)
  - [Props Event]()
    - [onAvatarClick](#onavatarclick)
    - [onMsgClick](#onmsgclick)
    - [onStatusViewClick](#onstatusviewclick)
    - [onPullToRefresh](#onpulltorefresh)
    - [onTouchMsgList](#ontouchmsglist) 
  - [Props customizable style]()
    - [sendBubble](#sendbubble)
    - [receiveBubble](#receivebubble)
    - [sendBubbleTextColor](#sendbubbletextcolor)
    - [receiveBubbleTextColor](#receivebubbletextcolor)
    - [sendBubbleTextSize](#sendbubbletextsize)
    - [receiveBubbleTextSize](#receivebubbletextsize)
    - [sendBubblePadding](#sendbubblepadding)
    - [receiveBubblePadding](#receivebubblepadding)
    - [dateTextSize](#datetextsize)
    - [dateTextColor](#datetextcolor)
    - [datePadding](#datepadding)
    - [avatarSize](#avatarsize)
    - [avatarCornerRadius](#avatarcornerradius)
    - [isShowDisplayName](#isShowdisplayname)
    - [messageListBackgroundColor](#messagelistbackgroundcolor)
    - [isAllowPullToRefresh](#isallowpulltorefresh)
- [ChatInput](#chatinput)
  - [Props customizable style]()
    - [chatInputBackgroupColor](#chatInputbackgroupcolor)
    - [showSelectAlbumBtn](#showSelectAlbumBtn)
  - [Props Event]()
    - [onSendText](#onsendtext)
    - [onSendGalleryFile](#onsendgalleryfile)
    - [onTakePicture](#ontakepicture)
    - [onStartRecordVideo](#onstartrecordvideo)
    - [onFinishRecordVideo](#onfinishrecordvideo)
    - [onCancelRecordVideo](#oncancelrecordvideo)
    - [onStartRecordVoice](#onstartrecordvoice)
    - [onFinishRecordVoice](#onfinishrecordvoice)
    - [onCancelRecordVoice](#oncancelrecordvoice)
    - [onSwitchToMicrophoneMode](#onswitchtomicrophonemode)
    - [onSwitchToGalleryMode](#onswitchtogallerymode)
    - [onSwitchToCameraMode](#onswitchtocameramode)
    - [onSwitchToEmojiMode](#onswitchtoemojimode)
    - [onSizeChange](#onsizechange)
    - [onTouchEditText](#ontouchedittext)
    - [onClickSelectAlbum](#onClickSelectAlbum)

## AuroraIMUIController

For append/update/insert message to MessageList, you will use `AuroraIMUIController`(Native Module) to send event to native.

#### appendMessages

param: [{[message](./Models.md#message)}]

Append message to bottom of the MessageList, the display order will be the same as the array of message's.

example:

```javascript
var messages = [{
	msgId: "1",
	status: "send_going",
	msgType: "text",
	text: "Hello world",
	isOutgoing: true,
	fromUser: {
		userId: "1",
		displayName: "Ken",
		avatarPath: "ironman"
	},
	timeString: "10:00"
}];
AuroraIMUIController.appendMessages(messages);
```

#### updateMessage

param: {[message](./Models.md#message)}

Update message, since the message status was changed, you should use this method to update your message.

example:

```javascript
var message = {
	msgId: "1",
	status: "send_going",
	msgType: "text",
	text: text,
	isOutgoing: true,
	fromUser: {
		userId: "1",
		displayName: "Ken",
		avatarPath: "ironman"
	},
	timeString: "10:00",
};
AuroraIMUIController.updateMessage(message);
```

#### insertMessagesToTop

param: [{[message](./Models.md#message)}]

Insert messages to the top of the MessageList, usually use this method to load history messages.

example:

```javascript
var messages = [{
    msgId: "1",
    status: "send_succeed",
    msgType: "text",
    text: "This",
    isOutgoing: true,
    fromUser: {
	  userId: "1",
	  displayName: "Ken",
	  avatarPath: "ironman"
    },
    timeString: "10:00",
  },{
    msgId: "2",
	status: "send_succeed",
	msgType: "text",
	text: "is",
	isOutgoing: true,
	fromUser: {
		userId: "1",
		displayName: "Ken",
		avatarPath: "ironman"
    },
    timeString: "10:10",
},{
    msgId: "3",
	status: "send_succeed",
	msgType: "text",
	text: "example",
	isOutgoing: true,
	fromUser: {
		userId: "1",
		displayName: "Ken",
		avatarPath: "ironman"
    },
    timeString: "10:20",
}];
AuroraIMUIController.insertMessagesToTop(messages);
```

#### stopPlayVoice

Stop playing voice, including media players in ChatInput and MessageList.

example:

```
AuroraIMUIController.stopPlayVoice()
```

#### removeMessage
param: string

Remove message by message id.

example:
```js
AuroraIMUIController.removeMessage("1")
```

#### removeAllMessage
Remove all messages.

example:
```js
AuroraIMUIController.removeAllMessage()
```

#### MessageListDidLoadListener

- addMessageListDidLoadListener(cb)

  The initialization of `AuroraIMUIController` will finish before `MessageListView` 's，If you need do something to `MessageListView` (such as append, insert, update and remove) you need to wait untill `MessageListDidLoad` has been called.

  example:

  ```javascript
  AuroraIMUIController.addMessageListDidLoadListener(()=> {
    // do something ex: insert message to top
  })
  ```

- removeMessageListDidLoadListener(cb)

  Remove listener of `MessageListDidLoad` 

  example:

  ```javascript
  AuroraIMUIController.removeMessageListDidLoadListener(cb)
  ```


## MessageList

#### MessageList Event Callback

------

#### onAvatarClick

**PropTypes.function:** ```( message ) => { }```

Fires when click avatar.

message param: { "message":  [message](./Models.md#message)  }

------

#### onMsgClick

**PropTypes.function:**  ```(message) => { } ```

Fires when click message bubble。

message param: { "message":  [message](./Models.md#message)  }

------

#### onStatusViewClick

**PropTypes.function:**  ```(message) => { } ```

Fires when click status view.

message param: { "message":  [message](./Models.md#message)  }

------

#### onPullToRefresh

**PropTypes.function:** ```() => { } ```

Fires when pull MessageList to top, example usage: please refer sample's onPullToRefresh method.

**In Android, you should put `onPullToRefresh` in `AndroidPtrLayout`, please refer sample/app/app.js for more detail.**

------

#### onTouchMsgList

**PropTypes.function:** ```() => { } ```

Fires when touch message list.

------

#### onBeginDragMessageList (iOS only)

**PropTypes.function**

------



#### MessageList custom style

**In android, if your want to define your chatting bubble, you need to put a drawable file in drawable folder, and that image file must be nine patch drawable file, see our example for detail.**

**In iOS, if your want to define your chatting bubble,you need to put a image file to you xcode,and specifies sendBubble.imageName or receiveBubble.imageName to image name.**

------

#### sendBubble

**PropTypes.object：**```{ imageName: string,  padding: { left: number,top: number,right: number,bottom: number}```

------

#### receiveBubble

**PropTypes.object：**```{ imageName: string,  padding: { left: number,top: number,right: number,bottom: number}```

------

#### sendBubbleTextColor

**PropTypes.string:** 

Set outgoing message's text color, ```sendBubbleTextColor="#000000"```.

------

#### receiveBubbleTextColor

**PropTypes.string**

Set incoming message's text color, ```sendBubbleTextColor="#000000"```.

------

#### sendBubbleTextSize

**PropTypes.number**

Set outgoing message's text size.

------

#### receiveBubbleTextSize

**PropTypes.number**

Set incoming message's text size.

------

#### sendBubblePadding

**PropTypes.object：** ```{ left: number, top: number, right: number, bottom: number }```

Set outgoing message's bubble padding.

------

#### receiveBubblePadding

**PropTypes.object:** ```{ left: number, top: number, right: number, bottom: number }```

Set incoming message's bubble padding.

------

#### dateTextSize

**PropTypes.number:** 

Set date text size of message.

------

#### dateTextColor

**PropTypes.string:**

Set date text color of message, ```dateTextColor="#000000"```。

------

#### datePadding

**PropTypes.number:** 

Set date padding, note this padding  type is not an object, means left, top, right and bottom padding is same.

------

#### avatarSize

**PropTypes.object:**  ```{ width: number, height: number }```

This property including width and height.

Example: ```avatarSize = {width: 50, height: 50}```。

------

#### avatarCornerRadius

**PropTypes.number:** 

Set fillet radius of avatar.

Example: ```avatarCornerRadius = {6}```。

------

#### isShowDisplayName

**PropTypes.bool:**

Show sender's display name or not.

Example: ```isShowDisplayName={ture}```。

------

#### messageListBackgroundColor

**PropTypes.string:**

Set messageList' background  color. **In Android, you should add this property in  `AndroidPtrlayout`.**

```
<AndroidPtrLayout
   ref="PtrLayout"
   messageListBackgroundColor={"#f3f3f3"}
/>
```



Example:  ```messageListBackgroundColor="#000000"```

------

#### isAllowPullToRefresh

**PropTypes.bool:**

Show pull-to-refresh or not.

Example: ```isAllowPullToRefresh={ture}```。

## ChatInput

### Props customizable style

***

#### chatInputBackgroupColor

**PropTypes.string:**

Set chatInput' background  color.

Example:  ```chatInputBackgroupColor="#000000"```

***

#### showSelectAlbumBtn
**PropTypes.bool:**

Set the visibility of the select album button.

Example: ```showSelectAlbumBtn={true}```

------

### ChatInput Event Callback

------

#### onSendText

**PropTypes.function:** ```(text) => {}```

After inputting text, fires when click send button, the text is the sending string(`text: string`).

------

#### onSendGalleryFiles

**PropTypes.function:** ```(result) => {}```

After choosing picutres or videos, fires when click send button.

 Result's type is ```{mediaFiles: [string]}```, including choosing files' path.

------

#### onTakePicture

**PropTypes.function:** ```(result) => {}```

Fires when click take picture button, result's type is ```{mediaPath: string}```。

------

#### onStartRecordVideo

**PropTypes.function:** ``` () => {}```

Fires when click record video button.

------

#### onFinishRecordVideo

**PropTypes.function:**``` (result) => {}```

 Fires when finished recording video. 

Result's type is  ```{mediaPath: string, durationTime: number}```。

------

#### onCancelRecordVideo

**PropTypes.function:**

Fires when click cancel record video button.

------

#### onStartRecordVoice

**PropTypes.function:**```() => {} ```

 Fires when click record audio button.

------

#### onFinishRecordVoice

**PropTypes.function:**``` (result) => {}```

After finishing recording audio, fires when finger left from screen,

Result's type is ```{mediaPath: string, duration: number}```。

------

#### onCancelRecordVoice

**PropTypes.function:**```() => {} ```

 Fires when finger move to cancel record zone, and left from screen.

------

#### onSwitchToMicrophoneMode

**PropTypes.function:** ```() => {} ```

Fires when click microphone button in menu.

------

#### onSwitchToGalleryMode

**PropTypes.function:** ```() => {} ```

Fires when click picture button in menu.

------

#### onSwitchToCameraMode

**PropTypes.function:** ```() => {} ```

 Fires when click camera button in menu.

------

#### onSwitchToEmojiMode

**PropTypes.function:** ```() => {} ```

Fires when click emoji button in menu.

------

#### onSizeChange

**PropTypes.function:** `({width: number, height: number}) => {}`

Fires when ChantInput's size change。

***

#### onTouchEditText

**PropTypes.function:** （Android only）

Fires when click input view.

#### onClickSelectAlbum

**PropTypes.function:** `() => {}`

Fires when click select album button(you can through [showSelectAlbumBtn](#showSelectAlbumBtn) change the visibility )

***