import React, { Component } from 'react'

import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  TouchableHighlight,
  NativeModules,
  requireNativeComponent,
  Alert,
  Dimensions,
  Button,
  DeviceEventEmitter,
  Platform,
  PermissionsAndroid
} from 'react-native'

var RNFS = require('react-native-fs')

var ReactNative = require('react-native')
import IMUI from 'aurora-imui-react-native'
var InputView = IMUI.ChatInput
var MessageListView = IMUI.MessageList
var AndroidPtrLayout = IMUI.AndroidPtrLayout
const AuroraIController = IMUI.AuroraIMUIController
const window = Dimensions.get('window')
const getInputTextEvent = "getInputText"
const MessageListDidLoadEvent = "IMUIMessageListDidLoad"

var themsgid = 1
const menuHeight = 625

function constructNormalMessage() {

  var message = {}
  message.msgId = themsgid.toString()
  themsgid += 1
  message.status = "send_going"
  message.isOutgoing = true
  message.timeString = ""
  var user = {
    userId: "",
    displayName: "replace your nickname",
    avatarPath: "ironman"
  }
  user.avatarPath = RNFS.MainBundlePath + '/defoult_header.png'
  message.fromUser = user

  return message
}

class CustomVew extends Component {
  constructor(props) {
    super(props);
    this.state = {
    };
  }
  render() {
    return (<img src={RNFS.MainBundlePath + '/defoult_header.png'}></img>)
  }
}

export default class TestRNIMUI extends Component {
  constructor(props) {
    super(props);
    let initHeight;
    if (Platform.OS === "ios") {
      initHeight = 86
    } else{
      initHeight = 120
    }
    this.state = {
      inputHeight: initHeight,
      inputViewLayout: { width: window.width, height: initHeight + 80, },
      isAllowPullToRefresh: true,
      lineCount: 1,
      shouldExpandMenuContainer: false,
      inputText: "",
    }

    this.updateLayout = this.updateLayout.bind(this);
    
  }

  componentDidMount() {
    
    this.resetMenu()

    AuroraIController.addMessageListDidLoadListener(() => {
      // messagelist is ready to insert message.
      this.getHistoryMessage()
    });
    if (Platform.OS == "android") {
      AuroraIController.addGetInputTextListener((text) => {
        this.setState({
          inputText: text
        })
      })
    }
  }

  getHistoryMessage() {
    var messages = []
    for (var i = 0; i < 14; i++) {
      var message = constructNormalMessage()
      message.msgType = "text"
      message.text = "" + i
      AuroraIController.insertMessagesToTop([message])
    }
  }

  componentWillUnmount() {
      AuroraIController.removeMessageListDidLoadListener(MessageListDidLoadEvent)
      if (Platform.OS == "android") {
        AuroraIController.removeGetInputTextListener(getInputTextEvent)
      }
  }

  expendMenu() {
    if (Platform.OS === "android") {
      this.setState({
        shouldExpandMenuContainer: true,
        inputViewLayout: { width: window.width, height: this.state.inputHeight + 80 + menuHeight }
      })
    } else {
      this.setState({
        inputViewLayout: { width: window.width, height: 338 }
      })
    }
  }

  resetMenu() {
    if (Platform.OS === "android") {
      console.log("reset menu, count: " + this.state.lineCount)
      if (this.lineCount == 1) {
        this.setState({
          inputHeight: 120,
          inputViewLayout: { width: window.width, height: 200 }
        })
      } else {
        this.setState({
          inputHeight: 80 + this.state.lineCount * 40,
          inputViewLayout: { width: window.width, height: 160 + 40 * this.state.lineCount }
        })
      }
      this.setState({
        shouldExpandMenuContainer: false,
      })
    } else {
      this.setState({
        inputViewLayout: { width: window.width, height: 86 }
      })
    }
  }

  onTouchEditText = () => {
    if (this.state.shouldExpandMenuContainer) {
      console.log("on touch input, expend menu")
      this.expendMenu()
    }
  }

  onAvatarClick = (message) => {
    Alert.alert()
    AuroraIController.removeMessage(message.msgId)
  }

  onMsgClick = (message) => {
    console.log(message)
    Alert.alert("message", JSON.stringify(message))
  }

  onMsgLongClick = (message) => {
    Alert.alert('message bubble on long press', 'message bubble on long press')
  }

  onStatusViewClick = (message) => {
    message.status = 'send_succeed'
    AuroraIController.updateMessage(message)
  }

  onBeginDragMessageList = () => {
    this.resetMenu()
    AuroraIController.hidenFeatureView(true)
  }

  onTouchMsgList = () => {
    if (Platform.OS == "android") {
      this.refs["ChatInput"].getInputText()
      if (this.state.inputText == "") {
        this.setState({
          lineCount: 1
        })
      }
    }
    this.resetMenu()
    AuroraIController.hidenFeatureView(true)
  }

  onPullToRefresh = () => {
    console.log("on pull to refresh")
    var messages = []
    for (var i = 0; i < 14; i++) {
      var message = constructNormalMessage()
      // if (index%2 == 0) {
      message.msgType = "text"
      message.text = "" + i
      // }

      if (i % 3 == 0) {
        message.msgType = "event"
        message.text = "" + i
      }

      AuroraIController.insertMessagesToTop([message])
    }
    AuroraIController.insertMessagesToTop(messages)
    this.refs["PtrLayout"].refreshComplete()
  }

  onSendText = (text) => {
    var message = constructNormalMessage()
    var evenmessage = constructNormalMessage()

    message.msgType = 'text'
    message.text = text

    AuroraIController.appendMessages([message])
    AuroraIController.scrollToBottom(true)

    if (Platform.OS === 'android') {
      this.setState({
        inputText: "",
        lineCount: 1,
        inputHeight: 120,
        inputViewLayout: { width: window.width, height: 825 }
      })
    }
  }

  onTakePicture = (mediaPath) => {

    var message = constructNormalMessage()
    message.msgType = 'image'
    message.mediaPath = mediaPath
    AuroraIController.appendMessages([message])
    AuroraIController.scrollToBottom(true)
  }

  onStartRecordVoice = (e) => {
    console.log("on start record voice")
  }

  onFinishRecordVoice = (mediaPath, duration) => {
    var message = constructNormalMessage()
    message.msgType = "voice"
    message.mediaPath = mediaPath
    message.timeString = "safsdfa"
    message.duration = duration
    AuroraIController.appendMessages([message])
  }

  onCancelRecordVoice = () => {
    console.log("on cancel record voice")
  }

  onStartRecordVideo = () => {
    console.log("on start record video")
  }

  onFinishRecordVideo = (mediaPath, duration) => {
    var message = constructNormalMessage()

    message.msgType = "video"
    message.mediaPath = mediaPath
    AuroraIController.appendMessages([message])
  }

  onSendGalleryFiles = (mediaFiles) => {


    /**
     * WARN: This callback will return original image, 
     * if insert it directly will high memory usage and blocking UI。
     * You should crop the picture before insert to messageList。
     * 
     * WARN: 这里返回的是原图，直接插入大会话列表会很大且耗内存.
     * 应该做裁剪操作后再插入到 messageListView 中，
     * 一般的 IM SDK 会提供裁剪操作，或者开发者手动进行裁剪。
     * 
     * 代码用例不做裁剪操作。
     */
    for (index in mediaFiles) {
      var message = constructNormalMessage()
      message.msgType = "image"
      message.mediaPath = mediaFiles[index].mediaPath
      message.timeString = "8:00"
      AuroraIController.appendMessages([message])
      AuroraIController.scrollToBottom(true)
    }
  }

  onSwitchToMicrophoneMode = async () => {
    this.expendMenu()
    AuroraIController.scrollToBottom(true);
    try {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.RECORD_AUDIO, {
          'title': 'IMUI needs Record Audio Permission',
          'message': 'IMUI needs record audio ' +
            'so you can send voice message.'
        });
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        console.log("You can record audio");
      } else {
        console.log("Record Audio permission denied");
      }
    } catch (err) {
      console.warn(err)
    }
  }

  onSwitchToEmojiMode = () => {
    this.expendMenu()
  }
  onSwitchToGalleryMode = async () => {
    this.expendMenu()
    AuroraIController.scrollToBottom(true);
    try {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE, {
          'title': 'IMUI needs Read External Storage Permission',
          'message': 'IMUI needs access to your external storage ' +
            'so you select pictures.'
        }
      )
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        console.log("You can select pictures");
      } else {
        console.log("Read External Storage permission denied");
      }
    } catch (err) {
      console.warn(err)
    }
  }

  onSwitchToCameraMode = async () => {
    this.expendMenu()
    AuroraIController.scrollToBottom(true);
    try {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.CAMERA, {
          'title': 'IMUI needs Camera Permission',
          'message': 'IMUI needs access to your camera ' +
            'so you can take awesome pictures.'
        }
      )
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        console.log("You can use the camera")
      } else {
        console.log("Camera permission denied")
      }
    } catch (err) {
      console.warn(err)
    }
  }

  onShowKeyboard = (keyboard_height) => {
    var inputViewHeight = keyboard_height + 86
    this.updateLayout({ width: window.width, height: inputViewHeight, })
  }

  updateLayout(layout) {
    this.setState({ inputViewLayout: layout })
  }

  onInitPress() {
    console.log('on click init push ');
    this.updateAction();
  }

  onLineChanged = (line) => {
    console.log("line count: " + line)
    if (this.state.lineCount < 4) {
      this.setState({
        lineCount: line,
      })
    } else{
      this.setState({
        lineCount: 4
      })
    }
    if (this.state.shouldExpandMenuContainer) {
      this.setState({
        inputHeight: 80 + this.state.lineCount * 40,
        inputViewLayout: { width: window.width, height: 80 + this.state.inputHeight + menuHeight },
      })
    } else {
      this.setState({
        inputHeight: 80 + this.state.lineCount * 40,
        inputViewLayout: { width: window.width, height: 80 + this.state.inputHeight },
      })
    }
  }

  generateAndroidView() {
    return (
      <View style={styles.container}>
        <AndroidPtrLayout
          ref="PtrLayout"
          backgroundColor={"#ffffff"}
          onPullToRefresh={this.onPullToRefresh}>
          <MessageListView style={styles.messageList}
            ref="MessageList"
            onAvatarClick={this.onAvatarClick}
            onMsgClick={this.onMsgClick}
            onStatusViewClick={this.onStatusViewClick}
            onTouchMsgList={this.onTouchMsgList}
            onTapMessageCell={this.onTapMessageCell}
            onBeginDragMessageList={this.onBeginDragMessageList}
            avatarSize={{ width: 40, height: 40 }}
            sendBubbleTextSize={18}
            sendBubbleTextColor={"#000000"}
            sendBubblePadding={{ left: 10, top: 10, right: 20, bottom: 10 }}
          />
        </AndroidPtrLayout>
        <InputView style={this.state.inputViewLayout}
          ref="ChatInput"
          menuContainerHeight={this.state.menuContainerHeight}
          isDismissMenuContainer={this.state.isDismissMenuContainer}
          inputViewHeight={this.state.inputHeight}
          onSendText={this.onSendText}
          onTakePicture={this.onTakePicture}
          onStartRecordVoice={this.onStartRecordVoice}
          onFinishRecordVoice={this.onFinishRecordVoice}
          onCancelRecordVoice={this.onCancelRecordVoice}
          onStartRecordVideo={this.onStartRecordVideo}
          onFinishRecordVideo={this.onFinishRecordVideo}
          onSendGalleryFiles={this.onSendGalleryFiles}
          onSwitchToEmojiMode={this.onSwitchToEmojiMode}
          onSwitchToMicrophoneMode={this.onSwitchToMicrophoneMode}
          onSwitchToGalleryMode={this.onSwitchToGalleryMode}
          onSwitchToCameraMode={this.onSwitchToCameraMode}
          onTouchEditText={this.onTouchEditText}
          onFullScreen={this.onFullScreen}
          onRecoverScreen={this.onRecoverScreen}
          onLineChanged={this.onLineChanged}
        />
      </View>
    )
  }

  generateIOSView() {
    return (
      <View style={styles.container}>
        <MessageListView style={styles.messageList}
          ref="MessageList"
          onAvatarClick={this.onAvatarClick}
          onMsgClick={this.onMsgClick}
          onStatusViewClick={this.onStatusViewClick}
          onTouchMsgList={this.onTouchMsgList}
          onTapMessageCell={this.onTapMessageCell}
          onBeginDragMessageList={this.onBeginDragMessageList}
          onPullToRefresh={this.onPullToRefresh}
          avatarSize={{ width: 40, height: 40 }}
          sendBubbleTextSize={18}
          sendBubbleTextColor={"#000000"}
          sendBubblePadding={{ left: 10, top: 10, right: 15, bottom: 10 }}
        />
        
        <InputView style={this.state.inputViewLayout}
          menuContainerHeight={this.state.menuContainerHeight}
          isDismissMenuContainer={this.state.isDismissMenuContainer}
          onSendText={this.onSendText}
          onTakePicture={this.onTakePicture}
          onStartRecordVoice={this.onStartRecordVoice}
          onFinishRecordVoice={this.onFinishRecordVoice}
          onCancelRecordVoice={this.onCancelRecordVoice}
          onStartRecordVideo={this.onStartRecordVideo}
          onFinishRecordVideo={this.onFinishRecordVideo}
          onSendGalleryFiles={this.onSendGalleryFiles}
          onSwitchToEmojiMode={this.onSwitchToEmojiMode}
          onSwitchToMicrophoneMode={this.onSwitchToMicrophoneMode}
          onSwitchToGalleryMode={this.onSwitchToGalleryMode}
          onSwitchToCameraMode={this.onSwitchToCameraMode}
          onShowKeyboard={this.onShowKeyboard}
          onTouchEditText={this.onTouchEditText}
          onFullScreen={this.onFullScreen}
          onRecoverScreen={this.onRecoverScreen}
        />
      </View>
    )
  }

  render() {
    let chat;
    if (Platform.OS === "android") {
      chat = this.generateAndroidView()
    } else {
      chat = this.generateIOSView()
    }
    return (
      <View style={styles.container}>
        <View style={styles.navigationBar}>
          <Button
            style={styles.sendCustomBtn}
            title="Custom Message"
            onPress={() => {
              if (Platform.OS === 'ios') {
                var message = constructNormalMessage()
                message.msgType = 'custom'
                message.content = '<h5>This is a custom message. </h5>\
                                      <img src="file://'
                message.content += RNFS.MainBundlePath + '/defoult_header.png' + '\"></img>'
                message.contentSize = { 'height': 100, 'width': 200 }
                message.extras = { "extras": "fdfsf" }
                AuroraIController.appendMessages([message])
                AuroraIController.scrollToBottom(true)
              } else {
                var message = constructNormalMessage()
                message.msgType = "custom"
                message.msgId = "10"
                message.status = "send_going"
                message.isOutgoing = true
                message.content = '<body bgcolor="#ff3399"><h5>This is a custom message. </h5>\
                  <img src="/storage/emulated/0/XhsEmoticonsKeyboard/Emoticons/wxemoticons/icon_040_cover.png"></img></body>'
                message.contentSize = { 'height': 400, 'width': 400 }
                message.extras = { "extras": "fdfsf" }
                var user = {
                  userId: "1",
                  displayName: "",
                  avatarPath: ""
                }
                user.displayName = "0001"
                user.avatarPath = "ironman"
                message.fromUser = user
                AuroraIController.appendMessages([message]);
              }
            }}>
          </Button>
        </View>
        {chat}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  navigationBar: {
    height: 64,
    justifyContent: 'center'
  },
  sendCustomBtn: {

  },
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  messageList: {
    flex: 1,
    marginTop: 0,
    width: window.width,
    margin: 0,
  },
  inputView: {
    backgroundColor: 'green',
    width: window.width,
    height: 100,

  },
  btnStyle: {
    marginTop: 10,
    borderWidth: 1,
    borderColor: '#3e83d7',
    borderRadius: 8,
    backgroundColor: '#3e83d7'
  }
});

