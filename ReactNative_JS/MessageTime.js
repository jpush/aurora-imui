import React, {Component} from 'react'
import { View, Text, StyleSheet } from 'react-native'

const styles = StyleSheet.create({
  timeContainer: {
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
  },
  time: {
    paddingVertical: 2.0,
    paddingHorizontal: 4.0,
    borderRadius: 4.0,
    marginBottom: 8.0,
    overflow: 'hidden',
    color: 'white',
    backgroundColor: '#CECECE',
    fontSize: 10.0,
  }
})

export default class MessageTime extends Component {
  render() {
    return <View
      style={styles.timeContainer}
    >
      <Text style={styles.time}>{this.props.timeString}</Text>
    </View>
  }
}