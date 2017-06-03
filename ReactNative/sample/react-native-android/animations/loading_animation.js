'use strict'

import React from 'react';
import ReactNative from 'react-native';

var {
	View,
	Text,
	Image,
	Animated,
	NativeModules,
	StyleSheet
} = ReactNative;
const JMessageModule = NativeModules.JMessageModule;

class LoadingAnimation extends React.Component {

	constructor(props) {
		super(props);
	}

	componentDidMount() {
		Animated.timing(
			this.anim, {
				toValue: 360,
				duration: 2000,
			},
		).start();
	}

	startAnimation() {

	}

	render() {
		this.anim = this.anim || new Animated.Value(0);
		return (
			<View style = { styles.loginAnimation }>
					<Animated.View
						style = {{ 
							transform: [
								{rotate: this.anim.interpolate({
									inputRange: [0, 200],
									outputRange: ['0deg', '360deg'],
								})},
							]
						}}>
						<Image image = { this } style = {{ width: 30, height: 30 }} source = { {uri: 'loading_1'}}/>
					</Animated.View>
					<Text style = { {color: '#808080'} }>
						{ this.props.message }
					</Text>
				</View>
		);
	}
}

var styles = StyleSheet.create({
	loginAnimation: {
		alignItems: 'center',
	},

});

module.exports = LoadingAnimation