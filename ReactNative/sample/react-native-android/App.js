'use strict';

import React from 'react';
import ReactNative from 'react-native';

var {
    Text,
    TextInput,
    View,
    Navigator,
    NativeModules,
    TouchableHighlight,
    BackAndroid,
    AppRegistry
} = ReactNative;

import ChatActivity from './pages/chat_activity.js';

var _navigator;
class IMUIReactDemo extends React.Component {

    constructor(props) {
        super(props);
        this.initialRoute = {
            name: 'chatActivity',
            component: ChatActivity,
        }

        this.renderScene = this.renderScene.bind(this);
    }


    componentWillMount() {

    }

    componentDidMount() {}

    componentWillUnmount() {}


    configureScene() {
        return Navigator.SceneConfigs.FloatFromRight;
    }

    renderScene(route, navigator) {
        _navigator = navigator;
        let Component = route.component;
        //navigator作为props传递给了这个component,在Component中就可以直接拿到: props.navigator
        return <Component
        {...route.params}
        navigator = { navigator }
            />
    }

    render() {
        return (
            <Navigator
        initialRoute = { this.initialRoute }
        configureScene = { this.configureScene }
        renderScene = { this.renderScene }
    />

        );
    }
}

AppRegistry.registerComponent('IMUIReactDemo', () => IMUIReactDemo);