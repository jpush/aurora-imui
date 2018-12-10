import React, {Component} from 'react'
import { View, Image, TouchableOpacity, StyleSheet } from 'react-native'

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-end',
    alignItems: 'center',
    padding: 4.0,
    marginBottom: 6.0
  },
  item: {
    width: 22.0,
    height: 22.0,
    marginRight: 4.0,
  }
})

export default class InputItem extends Component {
  render() {
    return <TouchableOpacity
      onPress={this.props.onPress}
      activeOpacity={0.3}
    >
      <View
        style={styles.container}
      >
        <Image
          style={[styles.item, {...this.props.style}]}
          source={this.props.source}
        ></Image>
      </View>
    </TouchableOpacity>
  }
}

