# API

#### 用法

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

> [Android Example usage](./sample/react-native-android/pages/chat_activity.js)
>
> [iOS Example usage](./sample/index.ios.js)

- [AuroraIMUIController](#auroraimuicontroller)
  - [appendMessages](#appendmessages)
  - [updateMessage](#updatemessage)
  - [insertMessagesToTop](#insertmessagestotop)
  - [stopPlayVoice](#stopplayvoice)
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
    - [showDisplayName](#showdisplayname)
- [ChatInput](#chatinput)
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
    - [onTouchEditText](#ontouchedittext)

## AuroraIMUIController

For append/update/insert message to MessageList, you will use `AuroraIMUIController`(Native Module) to send event to native.

#### appendMessages

param: [{[message](./Models.md#message)}]

添加消息到 MessageList 底部，顺序为数组顺序。

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

更新消息，可以使用该方法更新消息状态。

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

插入顺序会根据传入的消息数组顺序来排序。

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

停止正在播放的音频，这里会停止所有的声音，包括 ChatInput 和 MessageList 正在播放的声音。

example:

```
AuroraIMUIController.stopPlayVoice()
```

#### MessageListDidLoadListener

- addMessageListDidLoadListener(cb)

  `AuroraIMUIController` 初始化会先于 `MessageListView` 完成，如果需要调用对 `MessageListView` 的所有操作(添加消息，删除消息，更新消息)需要在 `MessageListDidLoad`事件触发后才会起作用。

  example:

  ```javascript
  AuroraIMUIController.addMessageListDidLoadListener(()=> {
    // do something ex: insert message to top
  })
  ```

- removeMessageListDidLoadListener(cb)

  取消对 `MessageListDidLoad` 事件的监听。

  example:

  ```javascript
  AuroraIMUIController.removeMessageListDidLoadListener(cb)
  ```



## MessageList

#### MessageList 事件回调

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

**In iOS, if your want to define your chatting bubble,you need to put a image file to you xcode,and specifies sendBubble.imageName or receiveBubble.imageName to image name. if you need to set the default avatar, you need put you default avatar image to you xcode,and adjust the image name to defoult_header,see our example for detail.**

------

#### sendBubble

**PropTypes.object：**```{ imageName: string,  padding: { left: number,top: number,right: number,bottom: number}```

------

#### receiveBubble

**PropTypes.object：**```{ imageName: string,  padding: { left: number,top: number,right: number,bottom: number}```

------

#### sendBubbleTextColor

**PropTypes.string:** 

设置发送消息的文本颜色，```sendBubbleTextColor="#000000"```。

------

#### receiveBubbleTextColor

**PropTypes.string**

设置接收消息的文本颜色，```sendBubbleTextColor="#000000"```。

------

#### sendBubbleTextSize

**PropTypes.number**

设置发送消息的文本大小，单位点。

------

#### receiveBubbleTextSize

**PropTypes.number**

设置接收消息的文本大小，单位点。

------

#### sendBubblePadding

**PropTypes.object：** ```{ left: number, top: number, right: number, bottom: number }```

发送消息泡泡的内边距。

------

#### receiveBubblePadding

**PropTypes.object:** ```{ left: number, top: number, right: number, bottom: number }```

接收消息泡泡的内边距。

------

#### dateTextSize

**PropTypes.number:** 

消息时间文字的尺寸大小，单位点。

------

#### dateTextColor

**PropTypes.string:**

消息时间文字的颜色, ```dateTextColor="#000000"```。

------

#### datePadding

**PropTypes.number:** 

与上面的不同，这个属性内边距是一样的。

------

#### avatarSize

**PropTypes.object:**  ```{ width: number, height: number }```

这个对象有宽高两个属性，Example: ```avatarSize = {width: 50, height: 50}```。

------

#### avatarCornerRadius

**PropTypes.number:** 

设置头像圆角半径, Example: ```avatarCornerRadius = {6}```。

------

#### showDisplayName

**PropTypes.bool:**

是否显示消息的发送方的名字，Example: ```showDisplayName={ture}```。

------



## ChatInput

#### ChatInput 事件回调

------

#### onSendText

**PropTypes.function:** ```(result) => {}```

输入文字后点击发送按钮触发，result 参数为 ```{text: string}```。

------

#### onSendGalleryFiles

**PropTypes.function:** ```(result) => {}```

 选中视频或图片后点击发送按钮触发，result 参数为 ```{mediaFiles: [string]}```, 图片路径数组。

------

#### onTakePicture

**PropTypes.function:** ```(result) => {}```

 点击拍照按钮触发， result 参数为 ```{mediaPath: string}```。

------

#### onStartRecordVideo

**PropTypes.function:** ``` () => {}```

 点击录制视频按钮触发。

------

#### onFinishRecordVideo

**PropTypes.function:**``` (result) => {}```

 完成录制视频触发，result 参数为 ```{mediaPath: string, durationTime: number}```。

------

#### onCancelRecordVideo

**PropTypes.function:**

 取消录制视频触发。

------

#### onStartRecordVoice

**PropTypes.function:**```() => {} ```

 点击录音按钮触发。

------

#### onFinishRecordVoice

**PropTypes.function:**``` (result) => {}```

录音完成后松开手指触发，result 参数为 ```{mediaPath: string, duration: number}```。

------

#### onCancelRecordVoice

**PropTypes.function:**```() => {} ```

 手指移动到取消录音区域后，抬起手指触发。

------

#### onSwitchToMicrophoneMode

**PropTypes.function:** ```() => {} ```

 点击菜单栏麦克风按钮触发。

------

#### onSwitchToGalleryMode

**PropTypes.function:** ```() => {} ```

 点击菜单栏图片按钮触发。

------

#### onSwitchToCameraMode

**PropTypes.function:** ```() => {} ```

 点击菜单栏拍照按钮触发。

------

#### onSwitchToEmojiMode

**PropTypes.function:** ```() => {} ```

 点击菜单栏表情按钮触发。

------

#### onTouchEditText

**PropTypes.function:** （Android only）

点击输入框触发。

