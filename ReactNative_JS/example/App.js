import React, {Component} from "react";
import {AuroraIMUI, Message} from "aurora-imui";
import MessageImageContent from "./MessageImageContent";
import { View, Image } from "react-native";
import { initialMessages, imageUrlArray } from "./ConstData";

var lastId = 8;

export default class App extends Component {
  constructor(props) {
    super(props)
    this.loadHistoryMessage = this.loadHistoryMessage.bind(this)
    this.sendText = this.sendText.bind(this)
    this.renderCustomRow = this.renderCustomRow.bind(this)
    this.onMsgClick = this.onMsgClick.bind(this)
    this.onAvatarClick = this.onAvatarClick.bind(this)
    this.onStatusViewClick = this.onStatusViewClick.bind(this)
    this.avatarContent = this.avatarContent.bind(this)
  }
  
  // If you want to add a custom message 
  // You can pass a function to renderRow which return a component 
  renderCustomRow(message) {
    switch(message.msgType) {
      case 'image':
        // custom message 
        return <Message {...{...message, messageContent: (message) => {return <MessageImageContent {...message}/>}}}
          avatarContent={(userInfo) => (<Image source={require('./assert/ironman.png')} />)}
          onMsgClick={this.onMsgClick}
          onAvatarClick={this.onAvatarClick}
          onStatusViewClick={this.onStatusViewClick}
        />
      default:
        return null
    }
  }

  avatarContent(userInfo) {
    console.log(`avatarContent ${JSON.stringify(userInfo)}`)

    return <Image source={require('./assert/ironman.png')} />
  }

  onMsgClick(msg) {
    if (this.imui) {
      const message = {...msg, status: 'send_failed'}
      this.imui.updateMessage(message)
      
      // this.imui.removeMessage(message)
      // this.imui.removeAllMessage()
    }
  }

  onAvatarClick(userInfo) {
    console.log('onAvatarClick' + JSON.stringify(userInfo))
  }

  onStatusViewClick(msg) {
    console.log('onStatusViewClick', JSON.stringify(msg))
    const message = {...msg, status: 'send_succeed'}
    this.imui.updateMessage(message)
  }

  async loadHistoryMessage() {
    if (this.imui) {

      var msgArr = imageUrlArray.map((url,index) => {
        if (index % 2 === 0) {
          return {
            msgType: 'text',
            msgId: (++lastId).toString(),
            text: url
          }
        }
        return {
          msgType: 'image',
          msgId: (++lastId).toString(),
          mediaPath: url
        }
      })
      
      return new Promise((resolve) => {
          setTimeout(() => {
              resolve(msgArr)
          },
          1000)
      })
      .then((msgArr) => {
        this.imui.insertMessagesToTop(msgArr)
      })
    }
  }

  sendText(text) {
    if (!this.imui) {
      return;
    }

    this.imui.appendMessages([
      {
        text,
        msgType: 'text',
        msgId: (++lastId).toString(),
        isOutgoing: true
      }
    ])
  }

  render() {
    return <View
      style={{
        flex: 1
      }}
    >
      <View
        style={{
          height: 64,
          backgroundColor: 'white',
        }}
      />
      <AuroraIMUI
        style={styles.imui}
        ref={(imui) => {this.imui = imui}}
        initialMessages={initialMessages}
        renderRow={this.renderCustomRow}
        
        // event callback
        onPullToRefresh={this.loadHistoryMessage}
        onSendText={this.sendText}
        onMsgClick={this.onMsgClick}
        onMsgContentClick={msg => console.log(JSON.stringify(msg))}
        onMsgContentClick={msg => console.log(msg)}
        onMsgContentLongClick={msg => console.log(JSON.stringify(msg))}
        avatarContent={this.avatarContent}
        onAvatarClick={this.onAvatarClick}
        onStatusViewClick={this.onStatusViewClick}

        stateContainerStyles={{justifyContent: 'center'}}
        avatarContainerStyles={{justifyContent: 'flex-start'}}
        textInputProps={{
          placeholder: 'Input Text message',
          multiline: true,
        }}
        // maxInputViewHeight={120}
        // renderBottom={() => {
        //   return <View
        //   style={{
        //     backgroundColor: 'red',
        //     height: 100,
        //   }}
        // />
        // }}
      />
    
      {/* 
      // You can add bottom component here.
      // NOTE: bottom component need have a explicit height
      // It's different to AuroraIMUI.props.renderBottom, 
      // when keyboard triggers, it will cover the bottom component，But AuroraIMUI.props.renderBottom will not
      <View
        style={{
          backgroundColor: 'yellow',
          height: 100,
        }}
      /> */}
    </View>
  }
}

const styles = {
  imui: {
    flex: 1,
  }
}