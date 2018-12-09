import React, {Component} from "react";
import {Platform, View, Dimensions, Text, Image, TouchableHighlight, TextInput,Keyboard, KeyboardAvoidingView} from "react-native";
import PropTypes from 'prop-types';

import MessageList from "./MessageList";
import InputView from "./InputView";
import InputItem from "./InputItem";


export default class AuroraIMUI extends Component {
    constructor(props) {
        super(props);
        this.scrollAnimated = false
        this.state = {
            extendedState: {ids: []},
            messageList: props.initialMessages,
        }

        this.insertMessagesToTop = this.insertMessagesToTop.bind(this);
        this.appendMessages = this.appendMessages.bind(this);
        this.updateMessage = this.updateMessage.bind(this)
        this.removeMessage = this.removeMessage.bind(this)

        this.renderRight = this.renderRight.bind(this);
        this.renderLeft = this.renderLeft.bind(this);
        
        this._onInputViewSizeChanged = this._onInputViewSizeChanged.bind(this);
        this._messageListScrollToBottom = this._messageListScrollToBottom.bind(this);
        this._onFocus = this._onFocus.bind(this);
    }

    componentDidMount() {
        this.setState({
            messageList: [...this.state.messageList]
        }, () => {
            
            setTimeout(() => {
                this.setState({
                    messageList: [...this.state.messageList]
                })       
            }, 50)
        })
    }
    _onInputViewSizeChanged() {
        if (!this.messageList) {
            return
        }
        
        this.props.onInputViewSizeChanged && 
            this.props.onInputViewSizeChanged.constructor === Function &&
            this.props.onInputViewSizeChanged()
    }

    insertMessagesToTop(msgArray = []) {
        if (!this.state.messageList) {
            return
        }

        const isNeedKeepCusrrentPosition = this.state.messageList.length > 0

        const newMsgs = msgArray.map((msg) => {
            return {
                type: msg.msgType,
                values: {...msg}
            }
        })

        this.state.messageList.unshift(...newMsgs)
        this.setState(() => {
            return {
              messageList: this.state.messageList,
              extendedState: this.state.extendedState
            }
        },
          () => {
                if (isNeedKeepCusrrentPosition) {
                    const topRowIndex = this.messageList.messageListRef.findApproxFirstVisibleIndex();
                    const currentOffset = this.messageList.messageListRef.getCurrentScrollOffset();
                    const topRowOffset = this.messageList.messageListRef._virtualRenderer.getLayoutManager().getOffsetForIndex(topRowIndex);
                    this.messageList.messageListRef._virtualRenderer.getLayoutManager().getOffsetForIndex(topRowIndex);
                    const diff = currentOffset - topRowOffset.y;
    
                    this.messageList.messageListRef._checkAndChangeLayouts(this.messageList.messageListRef.props)
                    const topRowNewOffset = this.messageList.messageListRef._virtualRenderer.getLayoutManager().getOffsetForIndex(newMsgs.length);
                    this.messageList.messageListRef.scrollToOffset(0, topRowNewOffset.y + diff);
                }
        })
    }

    appendMessages(msgArray = []) {
        const newMsgs = msgArray.map((msg) => {
            return {
                type: msg.msgType,
                values: {...msg}
            }
        })
        
        this.state.messageList.push(...newMsgs)
        this.setState({
            messageList: this.state.messageList,
            extendedState: {ids: []}
        }, () => {
            // RecycleListView will excute setTimeout(() => {this.scrollToOffset},0) in componentDidUpdate
            // So here are add setTimeout to scroll to bottom
            // It's ugly but work fine
            setTimeout(() => {
                this._messageListScrollToBottom(true)
            }, 20)
        })

    }

    updateMessage(msg) {
        if (this.state.messageList) {
            const index =this.state.messageList.findIndex((item) => msg.msgId === item.values.msgId)
            this.state.messageList[index] = {type: msg.msgType, values: {...msg}}
            this.setState({
                messageList: this.state.messageList
            })
        }
    }

    removeMessage(msg) {
        if (this.state.messageList) {
            const index =this.state.messageList.findIndex((item) => msg.msgId === item.values.msgId)
            this.state.messageList.splice(index, 1)
            this.setState({
                messageList: this.state.messageList
            })
        }
    }

    removeAllMessage() {
        if (this.state.messageList) {
            this.state.messageList.splice(0, this.state.messageList.length);
            this.setState({
                messageList: this.state.messageList
            })
        }
    }

    renderRight() {
        return <InputItem
            source={require('./assert/send_message_selected.png')}
            onPress={() => {
                // TODO: sendText
                if (!this.inputView || !this.inputView.state.text) {
                    return
                }

                const text = this.inputView.state.text
                this.props.onSendText &&
                    this.props.onSendText.constructor === Function &&
                    this.props.onSendText(text)

                this.inputView.setState({
                    text: "",
                })
            }}
        />
    }


    renderLeft() {
        return <InputItem
            source={require("./assert/send_message_selected.png")}
            onPress={() => {
                this._messageListScrollToBottom(true)
            }}
        />
    }

    _messageListScrollToBottom(animated = false) {    
        if (this.state.messageList && 
            this.state.messageList.constructor === Array &&
            this.state.messageList.length > 0) 
            {   
                const contentHeight = this.messageList.messageListRef._virtualRenderer.getLayoutDimension().height
                this.messageList.messageListRef.scrollToOffset(0, contentHeight - this.messageListHeight, animated)
        }
    }

    _onFocus() {
        this.scrollAnimated = true
        this._messageListScrollToBottom(true)
    }

    render() {
        const AuroraIMUIContainer = Platform.select({
            ios: () => require('KeyboardAvoidingView'),
            android: () => require('View'),
          })();

        return <AuroraIMUIContainer 
            style={styles.container}
            behavior="padding"
        >
            <MessageList
                ref={(component) => {this.messageList = component}}
                messageList={this.state.messageList}
                extendedState={this.state.extendedState}
                onPullToRefresh={this.props.onPullToRefresh}
                {...this.props}
                onLayout={({nativeEvent: {layout}}) => {
                    this.messageListHeight = layout.height
                    this._messageListScrollToBottom(this.scrollAnimated)
                }}
            />

            <InputView 
                ref={(component) => {this.inputView = component}}
                inputViewStyle={{}}
                renderRight={this.renderRight}
                renderLeft={this.renderLeft}
                {...this.props}
                onInputViewSizeChanged={this._onInputViewSizeChanged}
                onFocus={this._onFocus}
            />
        </AuroraIMUIContainer>
    }
}
const styles = {
    container: {
        flex: 1,
        backgroundColor: '#F3F3F3',
    }
}

AuroraIMUI.propTypes = {
    textInputProps: PropTypes.object,
    maxInputViewHeight: PropTypes.number,// It‘s useful when you want to add a bottom bar in inputView, Usually you need use it to adjust maxInputView's height.
    initialMessages: PropTypes.array,

    onInputTextChanged: PropTypes.func,
    onInputViewSizeChanged: PropTypes.func,
    onPullToRefresh: PropTypes.func,
    onAvatarClick: PropTypes.func,
    onMsgClick: PropTypes.func,
    onStatusViewClick: PropTypes.func,
    onMsgContentClick: PropTypes.func,
    onMsgContentLongClick: PropTypes.func,
};

AuroraIMUI.defaultProps = {
    textInputProps: {},
    initialMessages: [],
    maxInputViewHeight: 120,

    onInputTextChanged: () => {},
    onInputViewSizeChanged: () => {},
    onPullToRefresh: () => {},
    
    onAvatarClick: () => {},
    onMsgClick: () => {},
    onStatusViewClick: () => {},
    onMsgContentClick: () => {},
    onMsgContentLongClick: () => {},
};

// TODO:
//=== appendMessages
//- updateMessage
//- insertMessagesToTop
//- removeMessage
//- removeAllMessage

// MessageList
//- onAvatarClick
//- onMsgClick
//- onStatusViewClick
//- onMsgContentClick
//- onMsgContentLongClick


//- onPullToRefresh
// onTouchMsgList

// 
//- onSendText
//- onTouchEditText