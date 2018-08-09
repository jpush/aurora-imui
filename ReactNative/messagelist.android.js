'use strict';

import React from 'react';
import ReactNative from 'react-native';
import {ViewPropTypes} from 'react-native';
import PropTypes from 'prop-types';

var {
  Component,
} = React;

var {
  StyleSheet,
  UIManager,
  findNodeHandle,
  requireNativeComponent,
} = ReactNative;
const PTR_LAYOUT = "ptr_layout";

export default class MessageList extends Component {

  constructor(props) {
    super(props);
    this._onMsgClick = this._onMsgClick.bind(this);
    this._onMsgLongClick = this._onMsgLongClick.bind(this);
    this._onAvatarClick = this._onAvatarClick.bind(this);
    this._onStatusViewClick = this._onStatusViewClick.bind(this);
    this._onTouchMsgList = this._onTouchMsgList.bind(this);
    this._onPullToRefresh = this._onPullToRefresh.bind(this);
  }

  _onMsgClick(event: Event) {
    if (!this.props.onMsgClick) {
      return;
    }
    this.props.onMsgClick(JSON.parse(event.nativeEvent.message));
  }

  _onMsgLongClick(event: Event) {
    if (!this.props.onMsgLongClick) {
      return;
    }
    this.props.onMsgLongClick(JSON.parse(event.nativeEvent.message));
  }

  _onAvatarClick(event: Event) {
    if (!this.props.onAvatarClick) {
      return;
    }
    this.props.onAvatarClick(JSON.parse(event.nativeEvent.message));
  }

  _onStatusViewClick(event: Event) {
    if (!this.props.onStatusViewClick) {
      return;
    }
    this.props.onStatusViewClick(JSON.parse(event.nativeEvent.message));
  }

  _onTouchMsgList() {
    if (!this.props.onTouchMsgList) {
      return;
    }
    this.props.onTouchMsgList();
  }

  _onPullToRefresh() {
    if (!this.props.onPullToRefresh) {
      return;
    }
    this.props.onPullToRefresh();
  }

  refreshComplete() {
    UIManager.dispatchViewManagerCommand(findNodeHandle(this.refs[PTR_LAYOUT]), 0, null);
  }

  render() {
    return (
      <RCTMessageList 
          {...this.props} 
          ref={PTR_LAYOUT} 
          onMsgClick={this._onMsgClick}
          onAvatarClick={this._onAvatarClick}
          onMsgLongClick={this._onMsgLongClick}
          onStatusViewClick={this._onStatusViewClick}
          onTouchMsgList={this._onTouchMsgList}
          onPullToRefresh={this._onPullToRefresh}
        />
    );
  }

}

MessageList.propTypes = {
  messageListBackgroundColor: PropTypes.string,
  onMsgClick: PropTypes.func,
  onMsgLongClick: PropTypes.func,
  onAvatarClick: PropTypes.func,
  onStatusViewClick: PropTypes.func,
  onTouchMsgList: PropTypes.func,
  onPullToRefresh: PropTypes.func,
  sendBubble: PropTypes.object,
  receiveBubble: PropTypes.object,
  sendBubbleTextColor: PropTypes.string,
  receiveBubbleTextColor: PropTypes.string,
  sendBubbleTextSize: PropTypes.number,
  receiveBubbleTextSize: PropTypes.number,
  sendBubblePadding: PropTypes.object,
  receiveBubblePadding: PropTypes.object,
  dateTextSize: PropTypes.number,
  dateTextColor: PropTypes.string,
  datePadding: PropTypes.object,
  dateBackgroundColor: PropTypes.string,
  dateCornerRadius: PropTypes.number,
  avatarSize: PropTypes.object,
  isShowDisplayName: PropTypes.bool,
  eventTextColor: PropTypes.string,
  eventTextPadding: PropTypes.object,
  eventBackgroundColor: PropTypes.string,
  eventCornerRadius: PropTypes.number,
  eventTextLineHeight: PropTypes.number,
  eventTextSize: PropTypes.number,
  avatarCornerRadius: PropTypes.number,
  isShowIncomingDisplayName: PropTypes.bool,
  isShowOutgoingDisplayName: PropTypes.bool,
  displayNameTextSize: PropTypes.number,
  displayNameTextColor: PropTypes.string,
  displayNamePadding: PropTypes.object,
  displayNameEmsNumber: PropTypes.number,
  isAllowPullToRefresh: PropTypes.bool,
  maxBubbleWidth: PropTypes.number,
  messageTextLineHeight: PropTypes.number,
  videoMessageRadius: PropTypes.number,
  videoDurationTextColor: PropTypes.string,
  videoDurationTextSize: PropTypes.string,
  photoMessageRadius: PropTypes.number,
  ...ViewPropTypes
};

var RCTMessageList = requireNativeComponent('RCTMessageList', MessageList);

var styles = StyleSheet.create({

});