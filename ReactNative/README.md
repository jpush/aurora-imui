## IMUI for React Native

[中文文档](./README_zh.md)

## Install

```
npm install aurora-imui-react-native --save
react-native link
```

If link Android failed, you need modify `settings.gradle`:

```
include ':app', ':aurora-imui-react-native'
project(':aurora-imui-react-native').projectDir = new File(rootProject.projectDir, '../node_modules/aurora-imui-react-native/ReactNative/android')
```

And add dependency in your app's `build.gradle`:

```
dependencies {
    compile project(':aurora-imui-react-native')
}
```

**Attention（Android）：We are using support v4 & v7 version 25.3.1, so you should modify buildToolsVersion and compileSdkVersion to 25 or later, you can refer to sample's configuration.**

## Configuration

- ### Android

  - Add Package:

  > MainApplication.java

  ```java
  import cn.jiguang.imui.messagelist.ReactIMUIPackage;
  ...

  @Override
  protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
          new ReactIMUIPackage()
      );
  }
  ```

  - import IMUI from 'aurora-imui-react-native';


- ### iOS

  - Find PROJECT -> TARGETS -> General -> Embedded Binaries  and add RCTAuroraIMUI.framework
  - Find PROJECT -> TARGETS ->  Build Phases -> Target Dependencies and add RCTWebSocket

## Usage
```javascript
  import IMUI from 'aurora-imui-react-native';
  var MessageList = IMUI.MessageList;
  var ChatInput = IMUI.ChatInput;
  const AuroraIMUIController = IMUI.AuroraIMUIController;
```
Refer to iOS,Android example
> [iOS Example usage](./sample/app/app.js)
> [Android Example usage](./sample/app/app.js)
## Data format

By using MessageList, you need define `message` object and `fromUser` object.

- message object format:

**status must be one of the four values: "send_succeed", "send_failed", "send_going", "download_failed", 
if you haven't define this property, default value is "send_succeed".**

 ```json
  message = {  // text message
    msgId: "msgid",
    status: "send_going",
    msgType: "text",
    isOutgoing: true,
    text: "text",
    fromUser: {},
    extras: {}// Optional: you could add a extras object to this message
}

message = {  // image message
    msgId: "msgid",
    msgType: "image",
    isOutGoing: true,
    progress: "progress string",
    mediaPath: "image path",
    fromUser: {},
    extras: {}// Optional: you could add a extras object to this message
}

message = {  // voice message
    msgId: "msgid",
    msgType: "voice",
    isOutGoing: true,
    duration: number, // this property will show in voice message bubble
    mediaPath: "voice path",
    fromUser: {},
    extras: {}// Optional: you could add a extras object to this message
}

message = {  // video message
    msgId: "msgid",
    status: "send_failed",
    msgType: "video",
    isOutGoing: true,
    druation: number,
    mediaPath: "voice path",
    fromUser: {},   
    extras: {}// Optional: you could add a extras object to this message
}

message = {  // custom message
    msgId: "msgid",
    msgType: "custom",
    status: "send_failed",
    isOutgoing: true,
    contentSize: {height: 100, width: 100},
    content: "<h1>custom message will render html string</h1>" // content is html string, NOTE: Don't contain <script>. 
    fromUser: {}, 
    extras: {}// Optional: you could add a extras object to this message
}

message = {  // event message
    msgId: "msgid",
    msgType: "event",
    text: "the event text"
}
 ```

-    fromUser object format:

  ```json
  fromUser = {
    userId: "",
    displayName: "",
    avatarPath: "avatar image path"
  }
  ```


  ## Event Handling

  ### MessageList Event
- onAvatarClick {message: {message json}} :Fires when click avatar
- onMsgClick {message: {message json} : Fires when click message bubble
- onStatusViewClick {message: {message json}}  Fires when click status view
- onPullToRefresh  Fires when pull MessageList to top, example usage: please refer sample's onPullToRefresh method.
- onTouchMsgList  Fires when touch message list.


- onBeginDragMessageList (iOS only)

### MessageList append/update/insert message function:

  For append/update/insert message to MessageList, you will use `AuroraIMUIController`(Native Module) to send event to native.

- appendMessages([message])

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
	timeString: "10:00",
}];
AuroraIMUIController.appendMessages(messages);
```

- updateMessage(message)

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

- insertMessagesToTop([message])

  **Notice that the order of message array must be sorted in chronological order**

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

- addMessageListDidLoadListener(cb)

  AuroraIMUIController will be initialized first，show you need add this listener to get messageListDid load event. This is particularly useful in loading history messages.

  example:

  ```javascript
  AuroraIMUIController.addMessageListDidLoadListener(()=> {
    // do something ex: insert message to top
  })
  ```

- removeMessageListDidLoadListener(cb)

  remove MessageListDidLoad listener.

  example:

  ```javascript
  AuroraIMUIController.removeMessageListDidLoadListener(cb)
  ```

- stopPlayVoice()

  stop play voice.

  example:

  ```javascript
  AuroraIMUIController.stopPlayVoice()
  ```

  ​

### ChatInput Event

- onSendText: input the text and click send button
- onSendGalleryFiles: select gallery photo and click send button will send this event
- onTakePicture: take picture from carmera will send this event
- onStartRecordVideo: start to record video button will send this event
- onFinishRecordVideo: finish recorded video will send this event
- onCancelRecordVideo: cancel recorded video will send this event
- onStartRecordVoice: start to record voice will send this event 
- onFinishRecordVoice: finish  record voice will send this event
- onCancelRecordVoice: cancel recorded voice will send this event
- onSwitchToMicrophoneMode: click the microphone button in input view will send this event
- onSwitchToGalleryMode: click the gallery button in input view will send this event
- onSwitchToCameraMode: click the camera button in input view will send this event
- onSwitchToEmojiMode: click the emoji button in input view will send this event
- onTouchEditText:（Android only）click text input view will send this event

## Style 

### MessageList custom style

**In android, if your want to define your chatting bubble, you need to put a drawable file in drawable folder, and that image file must be [nine patch drawable file](https://developer.android.com/reference/android/graphics/drawable/NinePatchDrawable.html), see our example for detail.**



**In iOS, if your want to define your chatting bubble,you need to put a image file to you xcode,and specifies ` sendBubble.imageName` or `receiveBubble.imageName` to image name. if you need to set the default avatar, you need put you default avatar image to you xcode,and adjust the image name to `defoult_header`,see our example for detail.**

- sendBubble: PropTypes.object :
```
// eg:
	{ 
		imageName:"inComing_bubble",
		padding:{left:10,top:10,right:15,bottom:10}
	}
```

- receiveBubble: PropTypes.object,

- sendBubbleTextColor: PropTypes.string,

- receiveBubbleTextColor: PropTypes.string,

- sendBubbleTextSize: PropTypes.number,

- receiveBubbleTextSize: PropTypes.number,


This Padding object includes four properties: left, top, right, bottom. 
```
 // eg:
 {
 	left: 5, 
 	top: 5, 
 	right: 15, 
 	bottom: 5
 }
```
- sendBubblePadding: PropTypes.object

- receiveBubblePadding: PropTypes.object

- dateTextSize: PropTypes.number,

- dateTextColor: PropTypes.string,

- datePadding: PropTypes.number -- This is a number property, means padding left/top/right/bottom value is same.

Size object include width and height properties.

- avatarSize: PropTypes.object -- Example: avatarSize = {width: 50, height: 50}
- avatarCornerRadius: PropTypes.number — Example: avatarCornerRadius = {6}
- isShowDisplayName: PropTypes.bool, 

