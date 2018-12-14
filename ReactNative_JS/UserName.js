import React, {Component} from 'react'
import {  StyleSheet, Text } from 'react-native'

const styles = StyleSheet.create({
  userName: {
    height: 20.0,
    color: '#7487A8'
  },
})

export default class UserName extends Component {
  render() {
    return <Text style={[styles.userName]}>{this.props.displayName}</Text>
  }
}