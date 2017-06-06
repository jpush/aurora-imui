import {
	AppRegistry
} from 'react-native';
import App from './pages/chat_activity';

if (!__DEV__) {
	globel.console = {
		info: () => {},
		log: () => {},
		warn: () => {},
		error: () => {},
	};
}

AppRegistry.registerComponent('RNIMUIExample', () => App);