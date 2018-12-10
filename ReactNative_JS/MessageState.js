import React, {Component} from 'react'
import { View, Image, ActivityIndicator, StyleSheet, TouchableWithoutFeedback } from 'react-native'

const styles = StyleSheet.create({
  stateContainer: {
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    margin: 10.0,
  },
  stateView: {
    width: 20.0,
    height: 20.0,
  }
})

export default class MessageState extends Component {
  constructor(props) {
    super(props)
    this._renderStateView = this._renderStateView.bind(this)
    this._onClick = this._onClick.bind(this)
  }
  
  // 'send_succeed', 'send_failed', 'send_going', 'download_failed'
  _renderStateView() {
    switch(this.props.status) {
      case 'send_failed':
        return <Image style={styles.stateView} source={require('./assert/fail.png')}/>
      case 'send_going':
        return <ActivityIndicator style={styles.stateView} color='red'/>
      case 'send_succeed':
      default:
        return null
    }
  }

  _onClick() {
    this.props.onStatusViewClick && 
      this.props.onStatusViewClick.constructor === Function &&
      this.props.onStatusViewClick({...this.props})
  }

  render() {
    return <TouchableWithoutFeedback
      onPress={this._onClick}
    >
      <View
        style={[styles.stateContainer, this.props.stateContainerStyles]}
      >
        {this._renderStateView()}
      </View>
    </TouchableWithoutFeedback>
  }
}