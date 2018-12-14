import React, {Component} from 'react'
import { View, StyleSheet, TextInput } from 'react-native'

const styles = StyleSheet.create({
  container: {
    bottom: 0,
    left: 0,
    right: 0,
  },
  inputView: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
  },
  textContainer: {
    flex: 1,
    backgroundColor: 'white',
    borderRadius: 4.0,
    overflow: 'hidden',
    marginHorizontal: 8.0,
    marginBottom: 4.0,
    paddingHorizontal: 4.0,
  },
  textInput: {
    flex: 1,
    color: '#7487A8',
    fontSize: 15.0,
    margin: 0,
    padding: 0,
  },
  rightItem: {
    width: 30.0,
    height: 30.0,
  }
})

export default class InputView extends Component {
  constructor(props) {
    super(props)
    this.state = {
      inputViewHeight: undefined,
      position: 'absolute',
      text: ''
    }

    this._onContentSizeChange = this._onContentSizeChange.bind(this)
    this._onFocus = this._onFocus.bind(this)
    this._onChangeText = this._onChangeText.bind(this)
  }
  
  _onFocus({nativeEvent: event}) {

    if (this.recordCurrentHeight === undefined) {
      this.setState({
        inputViewHeight: this.inputViewHeight,
      })
    } else {
      this.setState({
        inputViewHeight: this.recordCurrentHeight,
      })
    }

    this.props.onFocus && 
      this.props.onFocus.constructor === Function &&
      this.props.onFocus()

    this.props.onTouchEditText &&
      this.props.onTouchEditText.constructor === Function &&
      this.props.onTouchEditText()
  }

  _onChangeText(newText) {
    this.setState({
      text: newText
    })
  }

  _onContentSizeChange({nativeEvent: { contentSize: { height } }}) {
    this.textInputContentHeight = height 
    
    if (this.inputViewHeight !== undefined) {
      if (this.diffHeight === undefined) {
        this.diffHeight = this.inputViewHeight - this.textInputContentHeight
      } else {
        // this.recordCurrentHeight just use to set height when onFocus inputText
        const maxInputViewHeight = this.props.maxInputViewHeight
        this.recordCurrentHeight = (this.diffHeight + height) < maxInputViewHeight ? (this.diffHeight + height) : maxInputViewHeight
        
        this.setState({
          inputViewHeight: (this.diffHeight + height) < maxInputViewHeight ? (this.diffHeight + height) : maxInputViewHeight,
        }, () => {
          this.props.onInputViewSizeChanged &&
            this.props.onInputViewSizeChanged.constructor === Function &&
            this.props.onInputViewSizeChanged()
        })
      }
    }
  }

  renderTextInput() {
    
    if (this.props.renderTextInput &&
        this.props.renderTextInput.constructor === Function) {
      return this.props.renderTextInput()
    }
    
    return (
      <View 
        style={[styles.textContainer]}
      >
        <TextInput 
          style={[styles.textInput]}
          maxLength={2000}
          // multiline={true}
          enablesReturnKeyAutomatically={true}
          onContentSizeChange={this._onContentSizeChange}
          onFocus={this._onFocus}
          onChangeText={this._onChangeText}
          value={this.state.text}
          autoFocus={true}
          {...this.props.textInputProps}
        />
      </View>
    )
  }

  renderLeft() {
    if (this.props.renderLeft &&
        this.props.renderLeft.constructor === Function) {
      return (
        this.props.renderLeft()
      )
    }
    return null
  }

  renderRight() {
    if (this.props.renderRight && 
        this.props.renderRight.constructor === Function) {
      return (
        this.props.renderRight()
      )
    }

    return <View style={styles.rightItem}></View>
  }

  renderBottom() {
    if (this.props.renderBottom &&
        this.props.renderBottom.constructor === Function) {
        return this.props.renderBottom()
    }

    return null
  }

  render() {
    return <View
      style={[
        styles.container,
        this.state.inputViewHeight? {height: this.state.inputViewHeight} : {},
        this.props.inputViewStyle,
        { position: this.state.position },
      ]}

      onLayout={({nativeEvent: {layout: {height}}}) => {
        console.log('input view on layout')
        if ('absolute' === this.state.position) {
          this.state.position = 'relative'
          this.setState({
            position: 'relative'
          })
        }

        if (this.inputViewHeight === undefined) {
          this.inputViewHeight = height
        }
      }}
    >
      <View
        style={[styles.inputView]}
      >
        {this.renderLeft()}
        {this.renderTextInput()}
        {this.renderRight()}
      </View>
      {this.renderBottom()}
    </View>
  }
}