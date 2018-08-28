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

export default class MessageList extends Component {

  constructor(props) {
    super(props);
    this._onMsgClick = this._onMsgClick.bind(this);
    this._onMsgLongClick = this._onMsgLongClick.bind(this);
    this._onAvatarClick = this._onAvatarClick.bind(this);
    this._onStatusViewClick = this._onStatusViewClick.bind(this);
    this._onPullToRefresh = this._onPullToRefresh.bind(this);
  }

  _onMsgClick(event: Event) {
    if (!this.props.onMsgClick) {
      return;
    }
    this.props.onMsgClick(event.nativeEvent.message);
  }

  _onMsgLongClick(event: Event) {
    if (!this.props.onMsgLongClick) {
      return;
    }
    this.props.onMsgLongClick(event.nativeEvent.message);
  }

  _onAvatarClick(event: Event) {
    if (!this.props.onAvatarClick) {
      return;
    }
    this.props.onAvatarClick(event.nativeEvent.message);
  }

  _onStatusViewClick(event: Event) {
    if (!this.props.onStatusViewClick) {
      return;
    }
    this.props.onStatusViewClick(event.nativeEvent.message);
  }

  _onBeginDragMessageList(event: Event) {
    if (!this.props.onStatusViewClick) {
      return;
    }
    this.props.onBeginDragMessageList();
  }

  _onPullToRefresh(event: Event) {
    if (!this.props.onPullToRefresh) {
      return;
    }
    this.props.onPullToRefresh();
  }

  render() {
    return (
      <RCTMessageList 
          {...this.props} 
          onMsgClick={this._onMsgClick}
          onAvatarClick={this._onAvatarClick}
          onMsgLongClick={this._onMsgLongClick}
          onStatusViewClick={this._onStatusViewClick}
      />
    );
  }

}

MessageList.propTypes = {
  onMsgClick: PropTypes.func,
  onMsgLongClick: PropTypes.func,
  onAvatarClick: PropTypes.func,
  onStatusViewClick: PropTypes.func,
  onBeginDragMessageList: PropTypes.func,
  onTouchMsgList: PropTypes.func,
  onPullToRefresh: PropTypes.func,
  messageListBackgroundColor: PropTypes.string,
  sendBubble: PropTypes.object,
  receiveBubble: PropTypes.object,
  sendBubbleTextColor: PropTypes.string,
  receiveBubbleTextColor: PropTypes.string,
  sendBubbleTextSize: PropTypes.number,
  receiveBubbleTextSize: PropTypes.number,
  sendBubblePadding: PropTypes.object,
  receiveBubblePadding: PropTypes.object,
  avatarSize: PropTypes.object,
  avatarCornerRadius: PropTypes.number,
  isShowDisplayName: PropTypes.bool,
  isShowIncomingDisplayName: PropTypes.bool,
  isShowOutgoingDisplayName: PropTypes.bool,
  isAllowPullToRefresh: PropTypes.bool,
  dateTextSize: PropTypes.number,
  dateTextColor: PropTypes.string,
  datePadding: PropTypes.object,
  dateBackgroundColor: PropTypes.string,
  dateCornerRadius: PropTypes.number,
  eventTextPadding: PropTypes.object,
  eventBackgroundColor: PropTypes.string,
  eventTextColor: PropTypes.string,
  eventTextSize: PropTypes.number,
  eventCornerRadius: PropTypes.number,
  displayNameTextSize: PropTypes.number,
  displayNameTextColor: PropTypes.string,
  displayNamePadding: PropTypes.object,
  maxBubbleWidth: PropTypes.number,
  eventTextLineHeight: PropTypes.number, //TODO:
  messageTextLineHeight: PropTypes.number, //TODO:
  ...ViewPropTypes
};

var RCTMessageList = requireNativeComponent('RCTMessageListView', MessageList);

var styles = StyleSheet.create({

});