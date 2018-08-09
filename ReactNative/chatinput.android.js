'use strict';

import React from 'react';
import ReactNative from 'react-native';
import PropTypes from 'prop-types';
import {ViewPropTypes} from 'react-native';

var {
	Component,
} = React;

var {
	StyleSheet,
	View,
	Dimensions,
	requireNativeComponent,
	UIManager,
	findNodeHandle,
  } = ReactNative;

const CHAT_INPUT = "chat_input";

export default class ChatInput extends Component {

	constructor(props) {
		super(props);
		this._onSendText = this._onSendText.bind(this);
		this._onSendFiles = this._onSendFiles.bind(this);
		this._takePicture = this._takePicture.bind(this);
		this._startVideoRecord = this._startVideoRecord.bind(this);
		this._finishVideoRecord = this._finishVideoRecord.bind(this);
		this._cancelVideoRecord = this._cancelVideoRecord.bind(this);
		this._onStartRecordVoice = this._onStartRecordVoice.bind(this);
		this._onFinishRecordVoice = this._onFinishRecordVoice.bind(this);
		this._onCancelRecordVoice = this._onCancelRecordVoice.bind(this);
		this._onSwitchToMicrophoneMode = this._onSwitchToMicrophoneMode.bind(this);
		this._onSwitchGalleryMode = this._onSwitchGalleryMode.bind(this);
		this._onSwitchToCameraMode = this._onSwitchToCameraMode.bind(this);
		this._onSwitchToEmojiMode = this._onSwitchToEmojiMode.bind(this);
		this._onTouchEditText = this._onTouchEditText.bind(this);
		this._onFullScreen = this._onFullScreen.bind(this);
		this._onRecoverScreen = this._onRecoverScreen.bind(this);
		this._onSizeChange = this._onSizeChange.bind(this);
		this._onClickSelectAlbum = this._onClickSelectAlbum.bind(this);
		this._closeCamera = this._closeCamera.bind(this);
		this._switchCameraMode = this._switchCameraMode.bind(this);
	}

	_onSendText(event: Event) {
		if (!this.props.onSendText) {
			return;
		}
		this.props.onSendText(event.nativeEvent.text);
	}

	_onSendFiles(event: Event) {
		if (!this.props.onSendGalleryFiles) {
			return;
		}
		this.props.onSendGalleryFiles(event.nativeEvent.mediaFiles);
	}

	_takePicture(event: Event) {
		if (!this.props.onTakePicture) {
			return;
		}
		this.props.onTakePicture(event.nativeEvent);
	}

	_startVideoRecord() {
		if (!this.props.onStartRecordVideo) {
			return;
		}
		this.props.onStartRecordVideo();
	}

	_finishVideoRecord(event: Event) {
		if (!this.props.onFinishRecordVideo) {
			return;
		}
		this.props.onFinishRecordVideo(event.nativeEvent);
	}

	_cancelVideoRecord() {
		if (!this.props.onCancelRecordVideo) {
			return;
		}
		this.props.onCancelRecordVideo();
	}

	_onStartRecordVoice() {
		if (!this.props.onStartRecordVoice) {
			return;
		}
		this.props.onStartRecordVoice();
	}

	_onFinishRecordVoice(event: Event) {
		if (!this.props.onFinishRecordVoice) {
			return;
		}
		this.props.onFinishRecordVoice(event.nativeEvent.mediaPath, event.nativeEvent.duration);
	}

	_onCancelRecordVoice() {
		if (!this.props.onCancelRecordVoice) {
			return;
		}
		this.props.onCancelRecordVoice();
	}

	_onSwitchToMicrophoneMode() {
		if (!this.props.onSwitchToMicrophoneMode) {
			return;
		}
		this.props.onSwitchToMicrophoneMode();
	}

	_onSwitchGalleryMode() {
		if (!this.props.onSwitchToGalleryMode) {
			return;
		}
		this.props.onSwitchToGalleryMode();
	}

	_onSwitchToCameraMode() {
		if (!this.props.onSwitchToCameraMode) {
			return;
		}
		this.props.onSwitchToCameraMode();
	}

	_onSwitchToEmojiMode() {
		if (!this.props.onSwitchToEmojiMode) {
			return;
		}
		this.props.onSwitchToEmojiMode();
	}

	_onTouchEditText() {
		if (!this.props.onTouchEditText) {
			return;
		}
		this.props.onTouchEditText();
	}

	_onFullScreen() {
		if (!this.props.onFullScreen) {
			return;
		}
		this.props.onFullScreen();
	}

	_onRecoverScreen() {
		if (!this.props.onRecoverScreen) {
			return;
		}
		this.props.onRecoverScreen();
	}

	_onSizeChange(event: Event) {
		if (!this.props.onSizeChange) {
			return;
		}
		this.props.onSizeChange({ width: Dimensions.get('window').width, height: event.nativeEvent.height });
	}

	_onClickSelectAlbum(event: Event) {
		if (!this.props.onClickSelectAlbum) {
			return;
		}
		this.props.onClickSelectAlbum();
	}

	_closeCamera(event: Event) {
		if (!this.props.closeCamera) {
			return;
		}
		this.props.closeCamera();
	}

	_switchCameraMode(event: Event) {
		if (!this.props.switchCameraMode) {
			return;
		}
		this.props.switchCameraMode(event.nativeEvent.isRecordVideoMode);
	}

	setMenuContainerHeight(height) {
		UIManager.dispatchViewManagerCommand(findNodeHandle(this.refs[CHAT_INPUT]), 99, [height]);
	}

	closeSoftInput() {
		UIManager.dispatchViewManagerCommand(findNodeHandle(this.refs[CHAT_INPUT]), 100, null);
	}

	getInputText() {
		UIManager.dispatchViewManagerCommand(findNodeHandle(this.refs[CHAT_INPUT]), 101, null);
	}

	showMenu(flag) {
		UIManager.dispatchViewManagerCommand(findNodeHandle(this.refs[CHAT_INPUT]), 102, [flag]);
	}

	render() {
		return (
			<RCTChatInput
				ref={CHAT_INPUT}
				{...this.props}
				onSendText={this._onSendText}
				onSendGalleryFiles={this._onSendFiles}
				onTakePicture={this._takePicture}
				onStartRecordVideo={this._startVideoRecord}
				onFinishRecordVideo={this._finishVideoRecord}
				onCancelRecordVideo={this._cancelVideoRecord}
				onStartRecordVoice={this._onStartRecordVoice}
				onFinishRecordVoice={this._onFinishRecordVoice}
				onCancelRecordVoice={this._onCancelRecordVoice}
				onSwitchToMicrophoneMode={this._onSwitchToMicrophoneMode}
				onSwitchToGalleryMode={this._onSwitchGalleryMode}
				onSwitchToCameraMode={this._onSwitchToCameraMode}
				onTouchEditText={this._onTouchEditText}
				onFullScreen={this._onFullScreen}
				onRecoverScreen={this._onRecoverScreen}
				onSizeChange={this._onSizeChange}
				onClickSelectAlbum={this._onClickSelectAlbum}
				closeCamera={this._closeCamera}
				switchCameraMode={this._switchCameraMode}
			/>
		);
	}

}

ChatInput.propTypes = {
  chatInputBackgroundColor: PropTypes.string,
  menuContainerHeight: PropTypes.number,
  isDismissMenuContainer: PropTypes.bool,
  onSendText: PropTypes.func,
  onSendGalleryFiles: PropTypes.func,
  onTakePicture: PropTypes.func,
  onStartRecordVideo: PropTypes.func,
  onFinishRecordVideo: PropTypes.func,
  onCancelRecordVideo: PropTypes.func,
  onStartRecordVoice: PropTypes.func,
  onFinishRecordVoice: PropTypes.func,
  onCancelRecordVoice: PropTypes.func,
  onSwitchToMicrophoneMode: PropTypes.func,
  onSwitchToGalleryMode: PropTypes.func,
  onSwitchToCameraMode: PropTypes.func,
  onSwitchToEmojiMode: PropTypes.func,
  onTouchEditText: PropTypes.func,
  onFullScreen: PropTypes.func,
  onRecoverScreen: PropTypes.func,
  onSizeChange: PropTypes.func,
  closeCamera: PropTypes.func,
  switchCameraMode: PropTypes.func,
  inputViewHeight: PropTypes.number,
  onClickSelectAlbum: PropTypes.func,
  showSelectAlbumBtn: PropTypes.bool,
  showRecordVideoBtn: PropTypes.bool,
  inputPadding: PropTypes.object,
  inputTextColor: PropTypes.string,
  inputTextSize: PropTypes.number,
  inputTextLineHeight: PropTypes.number,
  hideCameraButton: PropTypes.bool,
  hideVoiceButton: PropTypes.bool,
  hideEmojiButton: PropTypes.bool,
  hidePhotoButton: PropTypes.bool,
  ...ViewPropTypes
};

var RCTChatInput = requireNativeComponent('RCTChatInput', ChatInput);