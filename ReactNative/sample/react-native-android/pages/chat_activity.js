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
const AuroraIMUIModule = NativeModules.AuroraIMUIModule;

export default class ChatActivity extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			single: this.props.groupId === "",
			groupNum: '(1)',
			inputContent: '',
			recordText: '按住 说话',
			menuContainerHeight: 800,
			chatInputStyle: {
				width: Dimensions.get('window').width,
				height: 100
			}

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
				avatarPath: "ironman"
			},
			timeString: "10:00",
		}];
		AuroraIMUIModule.appendMessages(messages);
	}

	onSendGalleryFiles(mediaFiles) {
		console.log("will send media files: " + mediaFiles);
		for (var i = 0; i < mediaFiles.length; i++) {
			var mediaFile = mediaFiles[i];
			console.log("mediaFile: " + mediaFile);
			var messages;
			if (mediaFile.mediaType == "image") {
				messages = [{
					msgId: "1",
					status: "send_going",
					msgType: "image",
					isOutgoing: true,
					mediaPath: mediaFile.mediaPath,
					fromUser: {
						userId: "1",
						displayName: "ken",
						avatarPath: "ironman"
					},
					timeString: "10:00"
				}];
			} else {
				messages = [{
					msgId: "1",
					status: "send_going",
					msgType: "video",
					isOutgoing: true,
					mediaPath: mediaFile.mediaPath,
					duration: mediaFile.duration,
					fromUser: {
						userId: "1",
						displayName: "ken",
						avatarPath: "ironman"
					},
					timeString: "10:00"
				}];
			}
			AuroraIMUIModule.appendMessages(messages);
		}
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
		console.log("switch to microphone mode, set menuContainerHeight : " + this.state.menuContainerHeight);
		this.setState({
			chatInputStyle: {
				width: Dimensions.get('window').width,
				height: 300
			},
			menuContainerHeight: 800,
		});
	}

	onSwitchToGalleryMode() {
		console.log("switch to gallery mode");
		this.setState({
			chatInputStyle: {
				width: Dimensions.get('window').width,
				height: 300
			},
			menuContainerHeight: 800
		});
	}

	onSwitchToCameraMode() {
		console.log("switch to camera mode");
	}

	componentDidMount() {
		this.timer = setTimeout(() => {
			console.log("updating message! ");
			var messages = [{
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
			}];
			AuroraIMUIModule.appendMessages(messages);
		}, 5000);

	}

	componentWillUnmount() {
		this.timer && clearTimeout(this.timer);
	}

	render() {
		return (
			<View style = { styles.container }>
				<MessageList
					style = {{flex: 1}}
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
					<ChatInput
						style = {this.state.chatInputStyle}
						menuContainerHeight = {this.state.menuContainerHeight}
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
		);
	}
}

var styles = StyleSheet.create({
	container: {
		flex: 1,
	},
});