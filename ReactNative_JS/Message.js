import React, {Component} from 'react'
import PropTypes from 'prop-types'
import { View, StyleSheet, TouchableWithoutFeedback} from 'react-native'

import Avatar from './Avatar'
import MessageBubble from './MessageBubble'
import MessageState from './MessageState'
import UserName from './UserName'
import MessageTime from './MessageTime'

const styles = StyleSheet.create({
  container: {
    flex: 1,
    marginVertical: 8.0
  },
  outGoing: {
    flex: 1,
    flexDirection: 'row',
    
  },
  inComing: {
    flex: 1,
    flexDirection: 'row-reverse',
  },
  time: {
    flex: 1,
    alignContent: 'center',
  },
  reverse: {
    flexDirection: 'row-reverse',
  },
  row: {
    flexDirection: 'row',
  },
  defaultMessageBubbel: {
    width: 200.0,
    height: 200.0,
    backgroundColor: 'white',
  }
})

export default class Message extends Component {

  constructor(props) {
    super(props)

    this._renderTime = this._renderTime.bind(this)
    this._renderUserName = this._renderUserName.bind(this)
    this._renderAvatar = this._renderAvatar.bind(this)
    this._renderStateView = this._renderStateView.bind(this)
    this._renderMessageContent = this._renderMessageContent.bind(this)
    this._onMsgClick = this._onMsgClick.bind(this)
  }

  shouldComponentUpdate() {
    return true
  }
  
  _renderTime() {
    if (!this.props.timeString) {
      return null
    }

    if (this.props.renderTime && 
        this.props.renderTime.constructor === Function) {
      return this.props.renderTime({...this.props})
    } else {
      return <MessageTime {...this.props}/>
    }
  }

  _renderUserName() {
    if (!this.props.fromUser ||
        !this.props.fromUser.displayName) {
      return null
    }

    if (this.props.renderUserName &&
        this.props.renderUserName.constructor === Function) {
      return this.props.renderUserName({...this.props.fromUser})
    } else {
      return <UserName {...this.props.fromUser}/>
    }
  }

  _renderAvatar() {
    if (this.props.renderAvatar &&
        this.props.renderAvatar.constructor === Function) {
      return this.props.renderAvatar({...this.props.fromUser})
    } else {
      return <Avatar 
        {...this.props}
        {...this.props.fromUser}
      />
    }
  }

  _renderStateView() {
    if (this.props.renderStateView &&
        this.props.renderStateView.constructor === Function) {
      return this.props.renderStateView({...this.props})
    } else {
      return <MessageState {...this.props}/>
    }
  }

  _renderMessageContent() {
    if (this.props.messageContent &&
        this.props.messageContent.constructor === Function) {
      return (
        this.props.messageContent(this.props)
      )
    }
    return <View style={styles.defaultMessageBubbel}></View>
  }

  _onMsgClick() {
    this.props.onMsgClick &&
      this.props.onMsgClick.constructor === Function &&
      this.props.onMsgClick({...this.props})
  }

  render() {
    return <View 
      style={styles.container}
    >
      <TouchableWithoutFeedback
        onPress={this._onMsgClick}
        onLongPress={this._onMsgClick}
      >
        <View style={{}}>
          {this._renderTime()}
          <View
            style={!this.props.isOutgoing ? styles.outGoing : styles.inComing}
          >
            {this._renderAvatar()}
            <View>
              <View  style={!this.props.isOutgoing ? styles.row : styles.reverse}>
                {this._renderUserName()}
              </View>
              <View style={!this.props.isOutgoing ? styles.outGoing : styles.inComing}>
                <MessageBubble {...this.props}>
                  {this._renderMessageContent()}
                </MessageBubble>
                {this._renderStateView()}
              </View>
            </View>
          </View>
        </View>
      </TouchableWithoutFeedback>
    </View>
  }
}


Message.propTypes = {
  msgId: PropTypes.string.isRequired,
  status: PropTypes.string,
  fromUser: PropTypes.object,
  isOutGoing: PropTypes.bool,

  renderTime: PropTypes.func,
  renderUserName: PropTypes.func,
  renderAvatar: PropTypes.func,
  avatarContent: PropTypes.func,
  renderStateView: PropTypes.func,
  messageContent: PropTypes.func,

  onAvatarClick: PropTypes.func,
  onMsgClick: PropTypes.func,
  onStatusViewClick: PropTypes.func,

  stateContainerStyles: PropTypes.object,
  
}

Message.defaultProps = {
  status: 'send_succeed',
  fromUser: {},
  isOutGoing: true,

  onAvatarClick: () => {},
  onMsgClick: () => {},
  onStatusViewClick: () => {},
}