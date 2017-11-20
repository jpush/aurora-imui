/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
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
	DeviceEventEmitter,
	Platform,
	UIManager,
	findNodeHandle,
} from 'react-native';

import IMUI from 'aurora-imui-react-native'
var InputView = IMUI.ChatInput;
var MessageListView = IMUI.MessageList;
var AndroidPtrLayout = IMUI.AndroidPtrLayout;
const AuroraIMUIController = IMUI.AuroraIMUIController;
const window = Dimensions.get('window');

export default class ChatActivity extends Component<{}> {

	constructor(props) {
		super(props);
		this.state = {
			inputViewLayout: { width: window.width, height: 200, },
			menuContainerHeight: 1000,
			isDismissMenuContainer: false,
			shouldExpandMenuContainer: false,
		};

		this.updateLayout = this.updateLayout.bind(this);
		this.onMsgClick = this.onMsgClick.bind(this);
		this.onAvatarClick = this.onAvatarClick.bind(this);
		this.onMsgLongClick = this.onMsgLongClick.bind(this);
		this.onStatusViewClick = this.onStatusViewClick.bind(this);
		this.onTouchMsgList = this.onTouchMsgList.bind(this);
		this.onSendText = this.onSendText.bind(this);
		this.onSendGalleryFiles = this.onSendGalleryFiles.bind(this);
		this.onStartRecordVideo = this.onStartRecordVideo.bind(this);
		this.onFinishRecordVideo = this.onFinishRecordVideo.bind(this);
		this.onCancelRecordVideo = this.onCancelRecordVideo.bind(this);
		this.onStartRecordVoice = this.onStartRecordVoice.bind(this);
		this.onFinishRecordVoice = this.onFinishRecordVoice.bind(this);
		this.onTakePicture = this.onTakePicture.bind(this);
		this.onCancelRecordVoice = this.onCancelRecordVoice.bind(this);
		this.onSwitchToMicrophoneMode = this.onSwitchToMicrophoneMode.bind(this);
		this.onSwitchToGalleryMode = this.onSwitchToGalleryMode.bind(this);
		this.onSwitchToCameraMode = this.onSwitchToCameraMode.bind(this);
		this.onTouchEditText = this.onTouchEditText.bind(this);
		this.onPullToRefresh = this.onPullToRefresh.bind(this);
		this.onFullScreen = this.onFullScreen.bind(this);
	}

	componentWillMount() { }

	onMsgClick(message) {
		console.log("message click! " + message);
	}

	onMsgLongClick(message) {
		console.log("message long click " + message);
	}

	onAvatarClick(fromUser) {
		console.log("Avatar click! " + fromUser);
	}

	onStatusViewClick(message) {
		console.log("on message resend! " + message);
	}

	onTouchMsgList() {
		console.log("Touch msg list, hidding soft input and dismiss menu");
		this.setState({
			isDismissMenuContainer: true,
			inputViewLayout: {
				width: Dimensions.get('window').width,
				height: 200
			},
			shouldExpandMenuContainer: false,
		});
	}

	onPullToRefresh() {
		console.log("pull to refresh! Will load history messages insert to top of MessageList");
		var messages = [{
			msgId: "1",
			status: "send_succeed",
			msgType: "text",
			text: "history1",
			isOutgoing: false,
			fromUser: {
				userId: "1",
				displayName: "Ken",
				avatarPath: "ironman"
			},
			timeString: "9:00",
		}, {
			msgId: "2",
			status: "send_succeed",
			msgType: "text",
			text: "history2",
			isOutgoing: true,
			fromUser: {
				userId: "1",
				displayName: "Ken",
				avatarPath: "ironman"
			},
			timeString: "9:20",
		}, {
			msgId: "3",
			status: "send_succeed",
			msgType: "text",
			text: "history3",
			isOutgoing: false,
			fromUser: {
				userId: "1",
				displayName: "Ken",
				avatarPath: "ironman"
			},
			timeString: "9:30",
		}];
		AuroraIMUIController.insertMessagesToTop(messages);
		this.timer = setTimeout(() => {
			this.refs["PtrLayout"].refreshComplete()
		}, 2000)
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
		AuroraIMUIController.appendMessages(messages);
		this.setState({
			menuContainerHeight: this.state.menuContainerHeight == 1000 ? 999 : 1000
		});
	}

	onSendGalleryFiles(mediaFiles) {
		console.log("will send media files: " + mediaFiles);
		AuroraIMUIController.scrollToBottom(true);
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
			AuroraIMUIController.appendMessages(messages);
		}
	}

	onStartRecordVideo() {
		console.log("start record video");
		AuroraIMUIController.scrollToBottom(true);
	}

	onFinishRecordVideo(mediaPath, duration) {
		console.log("finish record video, Path: " + mediaPath + " duration: " + duration);
		var messages = [{
			msgId: "1",
			status: "send_going",
			msgType: "video",
			isOutgoing: true,
			mediaPath: mediaPath,
			duration: duration,
			fromUser: {
				userId: "1",
				displayName: "ken",
				avatarPath: "ironman"
			},
			timeString: "10:00"
		}];
		AuroraIMUIController.appendMessages(messages);
	}

	onCancelRecordVideo() {
		console.log("cancel record video");
	}

	onStartRecordVoice() {
		console.log("start record voice");

	}

	onFinishRecordVoice(mediaPath, duration) {
		console.log("finish record voice, mediaPath: " + mediaPath + " duration: " + duration);
		var messages = [{
			msgId: "1",
			status: "send_going",
			msgType: "voice",
			isOutgoing: true,
			mediaPath: mediaPath,
			duration: duration,
			fromUser: {
				userId: "1",
				displayName: "ken",
				avatarPath: "ironman"
			},
			timeString: "10:00"
		}];
		AuroraIMUIController.appendMessages(messages);
	}

	onCancelRecordVoice() {
		console.log("cancel record voice");
	}

	onTakePicture(mediaPath) {
		console.log("finish take picture, mediaPath: " + mediaPath);
		var messages = [{
			msgId: "1",
			status: "send_going",
			msgType: "image",
			isOutgoing: true,
			mediaPath: mediaPath,
			fromUser: {
				userId: "1",
				displayName: "ken",
				avatarPath: "ironman"
			},
			timeString: "10:00"
		}];
		AuroraIMUIController.appendMessages(messages);
	}

	async onSwitchToMicrophoneMode() {
		console.log("switch to microphone mode, set menuContainerHeight : " + this.state.menuContainerHeight);
		AuroraIMUIController.scrollToBottom(true);
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
		this.updateLayout({
			width: window.width,
			height: 825
		})
		this.setState({
			shouldExpandMenuContainer: true
		})
	}

	async onSwitchToGalleryMode() {
		console.log("switch to gallery mode");
		AuroraIMUIController.scrollToBottom(true);
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
		this.updateLayout({
			width: window.width,
			height: 825
		})
		this.setState({
			shouldExpandMenuContainer: true
		})
	}

	updateLayout(layout) {
		this.setState({
			inputViewLayout: layout
		})
	}

	async onSwitchToCameraMode() {
		console.log("switch to camera mode");
		AuroraIMUIController.scrollToBottom(true);
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
		this.updateLayout({
			width: window.width,
			height: 825
		})
		this.setState({
			shouldExpandMenuContainer: true
		})
	}

	onSwitchToEmojiMode = () => {
		this.updateLayout({
			width: window.width,
			height: 825
		})
		this.setState({
			shouldExpandMenuContainer: true
		})
	}

	onTouchEditText() {
		console.log("will scroll to bottom");
		AuroraIMUIController.scrollToBottom(true);
		if (this.state.shouldExpandMenuContainer) {
			this.setState({
				inputViewLayout: {
					width: window.width,
					height: 825,
				}
			})
		}
	}

	onFullScreen() {
		this.setState({
			inputViewLayout: {
				width: window.width,
				height: window.height
			}
		})
		console.log("Set screen height to full screen");
	}

	onRecoverScreen = () => {
		this.setState({
			inputViewLayout: {
				width: window.width,
				height: 825
			}
		})
	}

	componentDidMount() {
		AuroraIMUIController.addMessageListDidLoadListener(() => {
			console.log("MessageList did load !");
		});
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
			AuroraIMUIController.appendMessages(messages);
		}, 5000);

	}

	componentWillUnmount() {
		AuroraIController.removeMessageListDidLoadListener(this.messageListDidLoadCallback)
		if (Platform.OS === 'android') {
			UIManager.dispatchViewManagerCommand(findNodeHandle(this.refs["PtrLayout"]), 1, null)
			UIManager.dispatchViewManagerCommand(findNodeHandle(this.refs["MessageList"]), 100, null)
		}
		this.timer && clearTimeout(this.timer);

	}

	render() {
		return (
			<View style={styles.container}>
				<AndroidPtrLayout
					ref="PtrLayout"
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
					onTouchEditText={this.onTouchEditText}
					onFullScreen={this.onFullScreen}
					onRecoverScreen={this.onRecoverScreen}
				/>
			</View>
		);
	}
}

const styles = StyleSheet.create({
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
