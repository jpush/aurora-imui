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

const PHOTO_BROWSER = "photo_browser";

export default class PhotoBrowser extends Component {

	constructor(props) {
		super(props);
	}

	setPhotoPath(photoArr, idArr, clickMsgId) {
		UIManager.dispatchViewManagerCommand(findNodeHandle(this.refs[PHOTO_BROWSER]), 0, [photoArr, idArr, clickMsgId]);
	}

	render() {
		return (
			<RCTPhotoBrowser
				ref={PHOTO_BROWSER}
				{...this.props}
			/>
		);
	}

}

PhotoBrowser.propTypes = {
	chatInputBackgroupColor: PropTypes.string,
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
	inputViewHeight: PropTypes.number,
	onClickSelectAlbum: PropTypes.func,
	showSelectAlbumBtn: PropTypes.bool,
	inputPadding: PropTypes.object,
	inputTextColor: PropTypes.string,
	inputTextSize: PropTypes.number,
	inputTextLineHeight: PropTypes.number,
	...ViewPropTypes
};

var RCTPhotoBrowser = requireNativeComponent('RCTPhotoBrowser', PhotoBrowser);