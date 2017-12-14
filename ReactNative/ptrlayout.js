'use strict';

import React from 'react';
import ReactNative from 'react-native';
import PropTypes from 'prop-types';
var {
  Component,
} = React;

var {
  StyleSheet,
  ViewPropTypes,
  UIManager,
  findNodeHandle,
  requireNativeComponent,
} = ReactNative;
const PTR_LAYOUT = "ptr_layout";

export default class AndroidPtrFrameLayout extends Component {

  constructor(props) {
    super(props);
    this._onPullToRefresh = this._onPullToRefresh.bind(this);
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
      <PtrFrameLayout
          ref={PTR_LAYOUT} 
          {...this.props} 
          onPullToRefresh={this._onPullToRefresh}
        />
    );
  }

}


AndroidPtrFrameLayout.propTypes = {
  onPullToRefresh: PropTypes.func,
  messageListBackgroundColor: PropTypes.string,
  ...ViewPropTypes
};

var PtrFrameLayout = requireNativeComponent('PtrLayout', AndroidPtrFrameLayout);

var styles = StyleSheet.create({

});