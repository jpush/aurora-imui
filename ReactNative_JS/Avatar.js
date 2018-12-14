import React, {Component} from 'react'
import { View, StyleSheet, TouchableWithoutFeedback } from 'react-native'

const styles = StyleSheet.create({
  avatarContainer: {
    flexDirection: 'column',
    justifyContent: 'flex-start',
    alignItems: 'center',
  },
  avatar: {
    width: 40.0,
    height: 40.0,
    borderRadius: 20,
    marginHorizontal: 8.0,
  }
})

export default class Avatar extends Component {
  constructor(props) {
    super(props)
    this._onAvatarClick = this._onAvatarClick.bind(this)
    this._renderAvatarContent = this._renderAvatarContent.bind(this)
  }

  _renderAvatarContent() {

    if (this.props.avatarContent && 
        this.props.avatarContent.constructor === Function ) {
        const avatarContent = this.props.avatarContent({...this.props})
        if (avatarContent) {
          return avatarContent
        }
    }

    return <View style={{backgroundColor: 'red',width: 40.0,height:40.0}}/>
  }

  _onAvatarClick() {
    this.props.onAvatarClick && 
      this.props.onAvatarClick.constructor === Function &&
      this.props.onAvatarClick({...this.props})
  }

  render() {
    return <TouchableWithoutFeedback
      onPress={this._onAvatarClick}
    >
    <View style={[styles.avatarContainer, this.props.avatarContainerStyles]}>
      <View
        style={styles.avatar}
      >
      {this._renderAvatarContent()}
      </View>
    </View>
    </TouchableWithoutFeedback>
  }
}

