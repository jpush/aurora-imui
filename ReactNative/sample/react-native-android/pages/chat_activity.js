'use strict';

import React from 'react';
import ReactNative from 'react-native';
import IMUI from 'aurora-imui-react-native';
import TimerMixin from 'react-timer-mixin';
var {
	View,
	Text,
	Image,
	TouchableHighlight,
	TextInput,
	Dimensions,
	NativeModules,
	StyleSheet
} = ReactNative;

var MessageList = IMUI.MessageList;
var ChatInput = IMUI.ChatInput;
const ReactMsgListModule = NativeModules.MsgListModule;

export default class ChatActivity extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			single: this.props.groupId === "",
			groupNum: '(1)',
			inputContent: '',
			recordText: '按住 说话',
		};

		this.onMsgClick = this.onMsgClick.bind(this);
		this.onAvatarClick = this.onAvatarClick.bind(this);
		this.onMsgLongClick = this.onMsgLongClick.bind(this);
		this.onMsgResend = this.onMsgResend.bind(this);
		this.onSendText = this.onSendText.bind(this);
		this.onSendGalleryFiles = this.onSendGalleryFiles.bind(this);
		this.onStartRecordVideo = this.onStartRecordVideo.bind(this);
		this.onFinishRecordVideo = this.onFinishRecordVideo.bind(this);
		this.onCancelRecordVideo = this.onCancelRecordVideo.bind(this);
		this.onStartRecordVoice = this.onStartRecordVoice.bind(this);
		this.onFinishRecordVoice = this.onFinishRecordVoice.bind(this);
		this.onCancelRecordVoice = this.onCancelRecordVoice.bind(this);
		this.onSwitchToMicrophoneMode = this.onSwitchToMicrophoneMode.bind(this);
		this.onSwitchToGalleryMode = this.onSwitchToGalleryMode.bind(this);
		this.onSwitchToCameraMode = this.onSwitchToCameraMode.bind(this);
	}

	componentWillMount() {}

	onMsgClick(message) {
		console.log("message click! " + message);
	}

	onMsgLongClick(message) {
		console.log("message long click " + message);
	}

	onAvatarClick(fromUser) {
		console.log("Avatar click! " + fromUser);
	}

	onMsgResend(message) {
		console.log("on message resend! " + message);
	}

	onSendText(text) {
		console.log("will send text: " + text);
		var messages = [{
			msgId: "1",
			status: "send_going",
			msgType: "text",
			text: text,
			isOutgoing: true,
			fromUser: {
				userId: "1",
				displayName: "Ken",
				avatarPath: "R.drawable.ironman"
			},
			timeString: "10:00",
		}];
		ReactMsgListModule.appendMessages(messages);
	}

	onSendGalleryFiles(mediaFiles) {
		console.log("will send media files: " + mediaFiles);
	}

	onStartRecordVideo() {
		console.log("start record video");
	}

	onFinishRecordVideo(mediaPath) {
		console.log("finish record video, Path: " + mediaPath);
	}

	onCancelRecordVideo() {
		console.log("cancel record video");
	}

	onStartRecordVoice() {
		console.log("start record voice");
	}

	onFinishRecordVoice(mediaPath, duration) {
		console.log("finish record voice, mediaPath: " + mediaPath + " duration: " + duration);
	}

	onCancelRecordVoice() {
		console.log("cancel record voice");
	}

	onSwitchToMicrophoneMode() {
		console.log("switch to microphone mode");
	}

	onSwitchToGalleryMode() {
		console.log("switch to gallery mode");
	}

	onSwitchToCameraMode() {
		console.log("switch to camera mode");
	}

	componentDidMount() {
		this.timer = setTimeout(() => {
			console.log("updating message! ");
			this.setState({
				action: {
					"actionType": "update_message",
					messages: [{
						msgId: "1",
						status: "send_succeed",
						msgType: "text",
						text: "Hello world",
						isOutgoing: true,
						fromUser: {
							userId: "1",
							displayName: "Ken",
							avatarPath: "ironman"
						},
						timeString: "10:00",
					}]
				}
			})
		}, 5000);
	}

	componentWillUnmount() {
		this.timer && clearTimeout(this.timer);
	}

	render() {
		return (
			<View style = { styles.container }>
				<MessageList
					style = {{width: Dimensions.get('window').width, height: 500}}
					onMsgClick = {this.onMsgClick}
					onMsgLongClick = {this.onMsgLongClick}
					onAvatarClick = {this.onAvatarClick} 
					onMsgResend = {this.onMsgResend}
					sendBubble = {"send_msg"}
					receiveBubble = {"null"}
					receiveBubbleTextColor = {'#ffffff'}
					sendBubbleTextSize = {18}
					receiveBubbleTextSize = {14}
					sendBubblePressedColor = {'#dddddd'}
				/>
				<View style= {{flex:1}}>
					<ChatInput
						style = {{width: Dimensions.get('window').width, height: 200}}
						menuContainerHeight = {200}
						onSendText = {this.onSendText}
						onSendGalleryFiles = {this.onSendGalleryFiles}
						onTakePicture = {this.onTakePicture}
						onStartRecordVideo = {this.onStartRecordVideo}
						onFinishRecordVideo = {this.onFinishRecordVideo}
						onCancelRecordVideo = {this.onCancelRecordVideo}
						onStartRecordVoice = {this.onStartRecordVoice}
						onFinishRecordVoice = {this.onFinishRecordVoice}
						onCancelRecordVoice = {this.onCancelRecordVoice}
						onSwitchToMicrophoneMode = {this.onSwitchToMicrophoneMode}
						onSwitchToGalleryMode = {this.onSwitchToGalleryMode}
						onSwitchToCameraMode = {this.onSwitchToCameraMode}

					/>
				</View>
			</View>
		);
	}
}

var styles = StyleSheet.create({
	container: {
		flex: 1,
	},
	titlebar: {
		height: 40,
		flexDirection: 'row',
		backgroundColor: '#3f80dc',
		justifyContent: 'space-between',
		alignItems: 'center',
	},
	backBtn: {
		left: 0,
		width: 40,
		height: 40,
		alignItems: 'center',
		justifyContent: 'center'
	},
	title: {
		fontSize: 20,
		color: '#ffffff',
	},
	rightBtn: {
		right: 0,
		width: 40,
		height: 40,
		alignItems: 'center',
		justifyContent: 'center'
	},
	content: {
		flex: 1,
	},
	inputContent: {
		flexDirection: 'row',
		alignItems: 'center',
		height: 50,
		backgroundColor: '#e5e5e5',
	},
	voiceBtn: {
		marginLeft: 10,
	},
	voice: {
		width: 25,
		height: 30,
	},
	textInput: {
		flex: 1,
		fontSize: 16,
		padding: 5,
	},
	moreMenu: {
		marginLeft: 5,
		marginRight: 10,
		alignItems: 'center',
		justifyContent: 'center',
	},
	sendBtn: {
		backgroundColor: '#3f80dc',
		borderRadius: 5,
		marginLeft: 5,
		marginRight: 5,
		padding: 5,
		justifyContent: 'center',
		alignItems: 'center'
	},
	sendText: {
		color: '#ffffff',
		fontSize: 14,
	},
	keyboardBtn: {
		marginLeft: 10,
		marginRight: 10,
	},
	recordBtn: {
		flex: 1,
		borderRadius: 5,
		marginRight: 5,
		backgroundColor: '#3f80dc',
		padding: 5,
		alignItems: 'center',
		justifyContent: 'center',
	},
	recordText: {
		fontSize: 14,
		color: '#ffffff',
	}
});