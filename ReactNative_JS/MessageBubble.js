import React, {Component} from 'react'
import { View, StyleSheet, TouchableWithoutFeedback } from 'react-native'

const styles = StyleSheet.create({
  bubble: {
    borderRadius: 8,
    overflow: 'hidden',
    backgroundColor: 'white',
  }
})

export default class MessageBubble extends Component {
  constructor(props) {
    super(props)
    this._onMsgContentClick = this._onMsgContentClick.bind(this)
    this._onMsgContentLongClick = this._onMsgContentLongClick.bind(this)
  }

  _onMsgContentClick() {
     
    this.props.onMsgContentClick && 
      this.props.onMsgContentClick.constructor === Function &&
      this.props.onMsgContentClick({...this.props, children: undefined})
  }
  
  _onMsgContentLongClick() {
    this.props.onMsgContentLongClick && 
      this.props.onMsgContentLongClick.constructor === Function &&
      this.props.onMsgContentLongClick({...this.props, children: undefined})
  }

  render() {
    return <TouchableWithoutFeedback
      onPress={this._onMsgContentClick}
      onLongPress={this._onMsgContentLongClick}
    >
      <View style={styles.bubble}>
        {this.props.children}
      </View>
    </TouchableWithoutFeedback>
    

  }
}