import { AppRegistry } from 'react-native';
import App from './App';

if (!__DEV__) {
    globel.console = {
        info: () => {},
        log: () => {},
        warn: () => {},
        error: () => {},
    };
}

AppRegistry.registerComponent('App', () => App);