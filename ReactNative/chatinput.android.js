'use strict';

import React from 'react';
import ReactNative from 'react-native';

var {
  PropTypes,
  Component,
} = React;

var {
  StyleSheet,
  View,
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
    this._cancelVideoRecord = this._cancelVideoRecord.bind(this);
    this._onStartRecordVoice = this._onStartRecordVoice.bind(this);
    this._onFinishRecordVoice = this._onFinishRecordVoice.bind(this);
    this._onCancelRecordVoice = this._onCancelRecordVoice.bind(this);
    this._onSwitchToMicrophoneMode = this._onSwitchToMicrophoneMode.bind(this);
    this._onSwitchGalleryMode = this._onSwitchGalleryMode.bind(this);
    this._onSwitchToCameraMode = this._onSwitchToCameraMode.bind(this);
    this._onTouchEditText = this._onTouchEditText.bind(this);
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
    this.props.onTakePicture(event.nativeEvent.mediaPath);
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
    this.props.onFinishRecordVideo(event.nativeEvent.mediaPath);
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

  _onTouchEditText() {
    if (!this.props.onTouchEditText) {
      return;
    }
    this.props.onTouchEditText();
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
          onCancelRecordVideo={this._cancelVideoRecord}
          onStartRecordVoice={this._onStartRecordVoice}
          onFinishRecordVoice={this._onFinishRecordVoice}
          onCancelRecordVoice={this._onCancelRecordVoice}
          onSwitchToMicrophoneMode={this._onSwitchToMicrophoneMode}
          onSwitchToGalleryMode={this._onSwitchGalleryMode}
          onSwitchToCameraMode={this._onSwitchToCameraMode}
          onTouchEditText={this._onTouchEditText}
      />
    );
  }

}

ChatInput.propTypes = {
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
  onTouchEditText: PropTypes.func,
  ...View.propTypes
};

var RCTChatInput = requireNativeComponent('RCTChatInput', ChatInput);