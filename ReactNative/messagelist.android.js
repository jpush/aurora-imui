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

  render() {
    return (
      <RCTMessageList 
          {...this.props} 
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
  datePadding: PropTypes.number,
  avatarSize: PropTypes.object,
  isShowDisplayName: PropTypes.bool,
  ...View.propTypes
};

var RCTMessageList = requireNativeComponent('RCTMessageList', MessageList);

var styles = StyleSheet.create({

});