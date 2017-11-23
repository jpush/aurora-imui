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

// render() 中加入视图标签
<MessageListView />
<InputView />
```
详情可以参考 iOS Android 示例
> [Android Example 用法](./sample/react-native-android/pages/chat_activity.js)
> [iOS Example usage](./sample/index.ios.js)
- [AuroraIMUIController](#auroraimuicontroller)
  - [appendMessages](#appendmessages)
  - [updateMessage](#updatemessage)
  - [insertMessagesToTop](#insertmessagestotop)
  - [stopPlayVoice](#stopplayvoice)
  - [Event](#auroraimuicontrollerevent)
    - [MessageListDidLoadListener](#messagelistdidloadlistener)


- [MessageList](#messagelist)

  - [回调事件](#messagelistevent)
    - [onAvatarClick](#onavatarclick)
    - [onMsgClick](#onmsgclick)
    - [onStatusViewClick](#onstatusviewclick)
    - [onPullToRefresh](#onpulltorefresh)
    - [onTouchMsgList](#ontouchmsglist) 
  - [样式](#样式)

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

  - [回调事件](#chatinputevent)
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

  ​

## 数据格式

使用 MessageList，你需要定义 [message](./Models.md#message) 对象和 [fromUser](./Models.md#userinfo) 对象。



### AuroraIMUIController

  插入，更新，增加消息到 MessageList, 你需要使用 AuroraIMUIController (Native Module) 来发送事件到 Native。

#### appendMessages([message])

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

#### updateMessage(message)

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

#### insertMessagesToTop([message])

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

#### 

### MessageList

#### MessageList 事件回调

##### onAvatarClick 

点击头像触发。

example:

```javascript
<MessageListView onAvatarClick={
  (result) => { }
}/>

```

回调参数：

- result

  - message: message 对象

    ​

##### onMsgClick 

{message: {message json} :  点击消息气泡触发

##### onStatusViewClick

 {message: {message json}}  点击消息状态按钮触发

##### onPullToRefresh

滚动 MessageList 到顶部时，下拉触发, 案例用法: 参考 sample 中的聊天组件中的 onPullToRefresh  方法。

##### onTouchMsgList

点击消息列表触发

##### onTouchMsgList

（Android only）点击聊天列表触发

##### onBeginDragMessageList (iOS only)

 用于调整布局



#### MessageList 自定义样式

**在 Android 中，如果你想要自定义消息气泡，你需要将一张点九图放在 drawable 文件夹下。 [点九图介绍](https://developer.android.com/reference/android/graphics/drawable/NinePatchDrawable.html)，sample 项目的 drawable-xhdpi 文件夹下有示例。**
**在 iOS 中，如果想自定义消息气泡，需要把消息气泡图片加入到工程中，然后再 sendBubble.imageName 指定图片名字。 如果需要替换默认头像，需要把自己的默认头像加入到 xcode 工程中，并且图片名字改为 defoult_header ,详情参考 sample。**

##### sendBubble: PropTypes.object:

```
// eg:
	{ 
		imageName:"inComing_bubble",
		padding:{left:10,top:10,right:15,bottom:10}
	}
```

##### receiveBubble: PropTypes.object — 同上
##### sendBubbleTextColor: PropTypes.string,
##### receiveBubbleTextColor: PropTypes.string,
##### sendBubbleTextSize: PropTypes.number,
##### receiveBubbleTextSize: PropTypes.number,

padding 对象包括四个属性: left, top, right, bottom. 

```
 // eg:
 {
 	left: 5, 
 	top: 5, 
 	right: 15, 
 	bottom: 5
 }
```

##### sendBubblePadding: PropTypes.object
##### receiveBubblePadding: PropTypes.object
##### dateTextSize: PropTypes.number,
##### dateTextColor: PropTypes.string,
##### datePadding: PropTypes.number -- 与上面的不同，这个属性内边距是一样的


##### avatarSize: PropTypes.object -- 这个对象有宽高两个属性，Example: avatarSize = {width: 50, height: 50}
##### avatarCornerRadius: PropTypes.number — 设置头像圆角半径Example: avatarCornerRadius = {6}
##### showDisplayName: PropTypes.bool,

### ChatInput

#### ChatInput 事件回调

- onSendText 输入文字后点击发送按钮触发
- onSendGalleryFiles 选中视频或图片后点击发送按钮触发
- onTakePicture 点击拍照按钮触发
- onStartRecordVideo 点击录制视频按钮触发
- onFinishRecordVideo 完成录制视频触发
- onCancelRecordVideo 取消录制视频触发
- onStartRecordVoice 点击录音按钮触发
- onFinishRecordVoice 录音完成后松开手指触发
- onCancelRecordVoice 手指移动到取消录音区域后，抬起手指触发
- onSwitchToMicrophoneMode 点击菜单栏麦克风按钮触发
- onSwitchToGalleryMode 点击菜单栏图片按钮触发
- onSwitchToCameraMode 点击菜单栏拍照按钮触发
- onSwitchToEmojiMode 点击菜单栏表情按钮触发
- onTouchEditText（Android only）点击输入框触发

