import React, {Component} from 'react'
import { View, Text, StyleSheet } from 'react-native'

const styles = StyleSheet.create({
  eventContainer: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
    marginVertical: 8.0,
  },
  event: {
    paddingVertical: 2.0,
    paddingHorizontal: 4.0,
    borderRadius: 4.0,
    marginHorizontal: 20.0,
    marginBottom: 8.0,
    overflow: 'hidden',
    color: 'white',
    backgroundColor: '#CECECE',
    fontSize: 11.0,
  }
})

export default class Event extends Component {
  render() {
    return <View
      style={styles.eventContainer}
    >
      <Text style={styles.event}>{this.props.text}</Text>
    </View>
  }
}