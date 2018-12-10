import React, {Component} from 'react'
import { View, Dimensions, RefreshControl } from 'react-native'
import {RecyclerListView, LayoutProvider, DataProvider, BaseItemAnimator} from 'recyclerlistview'
import Message from './Message'
import Event from './Event'
import MessageTextContent from './MessageTextContent'

let {width} = Dimensions.get('window')

export default class MessageList extends Component {
  constructor(props) {
    super(props)
    this.state = {
        dataProvider: new DataProvider((r1, r2) => {
          return r1 !== r2
        }, (index) => {
          return this.props.messageList[index].values.msgId
        }).cloneWithRows(props.messageList),
        refreshing: false
    }
    
    this._layoutProvider = new LayoutProvider((i) => {
        return this.state.dataProvider.getDataForIndex(i).type
    }, (type, dim, index) => {
        dim.height = 300
        dim.width = width
    })
    
    this._renderRow = this._renderRow.bind(this)
    this.scrollToIndex = this.scrollToIndex.bind(this)
  }

  scrollToIndex(index, animate = false) {
    if (this.messageListRef) {
      this.messageListRef.scrollToIndex(index, animate)
    }
  }

  scrollToEnd(animate = false) {
    if (this.messageListRef) {
      this.messageListRef.scrollToEnd(animate)
    }
  }

  scrollToTop(animate = false) {
    if (this.messageListRef) {
      this.messageListRef.scrollToTop(animate)
    }
  }

  scrollToOffset(offset, animate) {
    if (this.messageListRef) {
      this.messageListRef.scrollToOffset(offset, animate)
    }
  }

  scrollToItem(item, animate) {
    if (this.messageListRef) {
      this.messageListRef.scrollToItem(item)
    }
  }

  _renderRow(type, data) {
    const message = {...data.values}
    if (this.props.renderRow &&
        this.props.renderRow.constructor === Function) {
      const customRow = this.props.renderRow(message)
      if (customRow) {
        return customRow
      }
    }

    switch (type) {
        case 'event':
          return <Event {...data.values}/>
        case 'text':
          return <Message {...{...message, messageContent: (message) => {return <MessageTextContent {...message}/>}}}
              onMsgClick={this.props.onMsgClick}
              onAvatarClick={this.props.onAvatarClick}
              onStatusViewClick={this.props.onStatusViewClick}
              onMsgContentClick={this.props.onMsgContentClick}
              onMsgContentLongClick={this.props.onMsgContentLongClick}
              avatarContent={this.props.avatarContent}
              stateContainerStyles={this.props.stateContainerStyles}
              avatarContainerStyles={this.props.avatarContainerStyles}
          />
        default:
          return null
    }
  }

  static getDerivedStateFromProps(props, state) {
    return {
      dataProvider: state.dataProvider.cloneWithRows(props.messageList)
    }
  }

  render() {
    return (<View style={{flex: 1}}
      {...this.props}
    >
      <RecyclerListView
        contentContainerStyle={[this.props.messageListStyle ? this.props.messageListStyle : {}]}
        // set renderAheadOffset 1000000 to disable Recycle row
        // but waste more memmory, there a bug when set to 2000 ,
        // insert message on top will scroll to error position.
        renderAheadOffset={1000000}
        ref={(messageListRef) => {this.messageListRef = messageListRef}}
        rowRenderer={this._renderRow} 
        dataProvider={this.state.dataProvider}
        extendedState={this.props.extendedState}
        layoutProvider={this._layoutProvider}
        forceNonDeterministicRendering={true}
        itemAnimator={BaseItemAnimator()}
        optimizeForInsertDeleteAnimations={true}
        scrollViewProps={{
          refreshControl:  
              <RefreshControl
                refreshing={this.state.refreshing} 
                onRefresh={ async () => {
                    this.setState({
                      refreshing: true
                    })
                    await this.props.onPullToRefresh()
                    this.setState({
                      refreshing: false
                    })
                }}
              />
      }}
    />
    </View>)
  }
}