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
  requireNativeComponent,
} = ReactNative;

export default class ChatInput extends Component {

  constructor(props) {
    super(props);
    this._onSendText = this._onSendText.bind(this);
    this._onSendFiles = this._onSendFiles.bind(this);
    this._takePicture = this._takePicture.bind(this);
    this._startVideoRecord = this._startVideoRecord.bind(this);
    this._finishVideoRecord = this._finishVideoRecord.bind(this);
    this._onStartRecordVoice = this._onStartRecordVoice.bind(this);
    this._onFinishRecordVoice = this._onFinishRecordVoice.bind(this);
    this._onCancelRecordVoice = this._onCancelRecordVoice.bind(this);
    this._onSwitchToMicrophoneMode = this._onSwitchToMicrophoneMode.bind(this);
    this._onSwitchToEmojiMode = this._onSwitchToEmojiMode.bind(this);
    this._onSwitchGalleryMode = this._onSwitchGalleryMode.bind(this);
    this._onSwitchToCameraMode = this._onSwitchToCameraMode.bind(this);
    this._onShowKeyboard = this._onShowKeyboard.bind(this);
    this._onSizeChange = this._onSizeChange.bind(this);
    this._onFullScreen = this._onFullScreen.bind(this);
		this._onRecoverScreen = this._onRecoverScreen.bind(this);
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

  _onSwitchToEmojiMode() {
    if (!this.props.onSwitchToEmojiMode) {
      return;
    }
    this.props.onSwitchToEmojiMode();
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

  _onShowKeyboard(event: Event) {
    if (!this.props.onShowKeyboard) {
      return;
    }

    this.props.onShowKeyboard(event.nativeEvent.keyboard_height);
  }

  _onSizeChange(event: Event) {
    if (!this.props.onSizeChange) {
      return;
    }

    this.props.onSizeChange(event.nativeEvent);
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

  render() {
    return (
      <RCTChatInput 
          {...this.props} 
          onSendText={this._onSendText}
          onSendGalleryFiles={this._onSendFiles}
          onTakePicture={this._takePicture}
          onStartRecordVideo={this._startVideoRecord}
          onFinishRecordVideo={this._finishVideoRecord}
          onStartRecordVoice={this._onStartRecordVoice}
          onFinishRecordVoice={this._onFinishRecordVoice}
          onCancelRecordVoice={this._onCancelRecordVoice}
          onSwitchToMicrophoneMode={this._onSwitchToMicrophoneMode}
          onSwitchToEmojiMode={this._onSwitchToEmojiMode}
          onSwitchToGalleryMode={this._onSwitchGalleryMode}
          onSwitchToCameraMode={this._onSwitchToCameraMode}
          onShowKeyboard={this._onShowKeyboard}
          onSizeChange={this._onSizeChange}
          onFullScreen={this._onFullScreen}
          onRecoverScreen={this._onRecoverScreen}
      />
    );
  }

}

ChatInput.propTypes = {
  chatInputBackgroupColor: PropTypes.string,
  menuContainerHeight: PropTypes.number,
  onSendText: PropTypes.func,
  onSendGalleryFiles: PropTypes.func,
  onTakePicture: PropTypes.func,
  onStartRecordVideo: PropTypes.func,
  onFinishRecordVideo: PropTypes.func,
  onStartRecordVoice: PropTypes.func,
  onFinishRecordVoice: PropTypes.func,
  onCancelRecordVoice: PropTypes.func,
  onSwitchToMicrophoneMode: PropTypes.func,
  onSwitchToEmojiMode: PropTypes.func,
  onSwitchToGalleryMode: PropTypes.func,
  onSwitchToCameraMode: PropTypes.func,
  onShowKeyboard: PropTypes.func,
  onSizeChange: PropTypes.func,
  onFullScreen: PropTypes.func,
	onRecoverScreen: PropTypes.func,
  galleryScale: PropTypes.number,
  compressionQuality: PropTypes.number,
  customLayoutItems: PropTypes.object,
  inputPadding: PropTypes.object,
	inputTextColor: PropTypes.string,
	inputTextSize: PropTypes.number,
  ...ViewPropTypes
};

var RCTChatInput = requireNativeComponent('RCTInputView', ChatInput);