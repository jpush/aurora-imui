import React, {Component} from 'react'
import { StyleSheet, Text } from 'react-native'

const styles = StyleSheet.create({

  text: {
    flex: 1,
    maxWidth: 200.0,
    padding: 8.0,
    fontSize: 14.0,
    justifyContent: 'center',
    alignItems: 'center',
    textAlign: 'left',
  },
  inComming: {
    color: 'white',    
    backgroundColor: '#2977FA', 
  },
  outGoing: {
    color: '#7587A8',
    backgroundColor: '#E7EBEF', 
  }
})

export default class MessageTextContent extends Component {
  render() {
    return <Text style={[styles.text, this.props.isOutgoing ? styles.outGoing : styles.inComming]}>{this.props.text}</Text>
  }
}