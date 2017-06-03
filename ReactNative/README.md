## IMUI for React Native

## Install

```
npm install aurora-imui-react-native --save
react-native link
```

## Usage

- ### Android

  - Add Package:

  > MainApplication.java

  ```
  @Override
  protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
          new ReactIMUIPackage()
      );
  }
  ```

  - import IMUI from 'aurora-imui-react-native';

 > [Android Example usage](./sample/react-native-android/pages/chat_activity.js)

```
  import IMUI from 'aurora-imui-react-native';
  var MessageList = IMUI.MessageList;
  var ChatInput = IMUI.ChatInput;
  const ReactMsgListModule = NativeModules.MsgListModule; 
```


- ### iOS

  - PROJECT -> TARGETS -> Build Settings -> Enable Bitcode Set to No
  - Find PROJECT -> TARGETS -> General -> Embedded Binaries  and add RNTAuroraIMUI.framework
  - Before build you project ,you should build RNTAuroraIMUI.framework
  - Use it in ReactJS
>[iOS Example usage](./sample/index.ios.js)
```
// For use IMUI you should Use get InputView, MessageListView, and RNTAuroraIController

import IMUI from 'aurora-imui-react-native'
var InputView = IMUI.ChatInput; // the inputView component
var MessageListView = IMUI.MessageList; // the messageList component
const AuroraIController = NativeModules.RNTAuroraIController; //  the IMUI controller, use it to add message to messageList.
```



## Data format

By using MessageList, you need define `message` object and `fromUser` object.

- message object format:

**status must be one of the four values: "send_succeed", "send_failed", "send_going", "download_failed", 
if you haven't define this property, default value is "send_succeed".**

 ```
  message = {  // text message
    msgId: "msgid",
    status: "send_going",
    msgType: "text",
    isOutgoing: true,
    text: "text"
    fromUser: {}
}

message = {  // image message
    msgId: "msgid",
    msgType: "image",
    isOutGoing: true,
    progress: "progress string"
    mediaPath: "image path"
    fromUser: {}
}


message = {  // voice message
    msgId: "msgid",
    msgType: "voice",
    isOutGoing: true,
    duration: number
    mediaPath: "voice path"
    fromUser: {}
}

message = {  // video message
    msgId: "msgid",
    status: "send_failed",
    msgType: "video",
    isOutGoing: true,
    druation: number
    mediaPath: "voice path"
    fromUser: {}
}
 ```

-    fromUser object format:

  ```
  fromUser = {
    userId: ""
    displayName: ""
    avatarPath: "avatar image path"
  }
  ```


  ## Event Handling

  ### MessageList click event
    - onAvatarClick {message: {message json}} :Fires when click avatar

    - onMsgClick {message: {message json} : Fires when click message bubble

    - onStatusViewClick {message: {message json}}  Fires when click status view

    - onBeginDragMessageList (iOS only)

  ### MessageList append/update/insert message event:

  For append/update/insert message to MessageList, you will use `MsgListModule`(Native Module) to send event to native.

- appendMessages([message])

 example:

```
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
ReactMsgListModule.appendMessages(messages);
```

- updateMessage(message)

example:

```
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
ReactMsgListModule.updateMessage(message);
```

- insertMessagesToTop([message])

example:

```
var messages = [{
  msgId: "1",
	status: "send_succeed",
	msgType: "text",
	text: text,
	isOutgoing: true,
	fromUser: {
		userId: "1",
		displayName: "Ken",
		avatarPath: "ironman"
	},
	timeString: "10:00",
	},{
  msgId: "2",
	status: "send_going",
	msgType: "text",
	text: "Hello",
	isOutgoing: true,
	fromUser: {
		userId: "1",
		displayName: "Ken",
		avatarPath: "ironman"
  }];
ReactMsgListModule.insertMessagesToTop(messages);
```

## Style 

### MessageList custom style

**In android, if your want to define your chatting bubble, you need to put a drawable file 
in drawable folder, and that image file must be [nine patch drawable file](https://developer.android.com/reference/android/graphics/drawable/NinePatchDrawable.html), 
see our example for detail.**
- sendBubble: PropTypes.string -- The name of the nine patch file.

same to top

- receiveBubble: PropTypes.string,

- sendBubbleTextColor: PropTypes.string,

- receiveBubbleTextColor: PropTypes.string,

- sendBubbleTextSize: PropTypes.number,

- receiveBubbleTextSize: PropTypes.number,


This Padding object includes four properties: left, top, right, bottom. eg: {left 5, top: 5, right: 15, bottom: 5}

- sendBubblePadding: PropTypes.object

- receiveBubblePadding: PropTypes.object

- dateTextSize: PropTypes.number,

- dateTextColor: PropTypes.string,

- datePadding: PropTypes.number -- This is a number property, means padding left/top/right/bottom value is same.

Size object include width and height properties.

- avatarSize: PropTypes.object -- Example: avatarSize = {width: 50, height: 50}

- showDisplayName: PropTypes.bool, 

