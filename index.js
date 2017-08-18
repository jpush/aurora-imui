import {
	NativeModules,
	Platform,
	DeviceEventEmitter
} from 'react-native';

import ChatInput from './ReactNative/chatinput';
import MessageList from './ReactNative/messagelist';

const AuroraIMUIModule = NativeModules.AuroraIMUIModule;

const listeners = {};
const IMUIMessageListDidLoaded = "IMUIMessageListDidLoad";

class AuroraIMUIController {
	/**
	 * append messages into messageList's bottom
	 * 
	 * @param {Array} messageList  [message]
	 */
	static appendMessages(messageList) {
		AuroraIMUIModule.appendMessages(messageList)
	}

	/**
	 * update Messages. NOTE: It will replace message according to msgID.
	 * @param {Array} messageList  [message]
	 */
	static updateMessage(messageList) {
		AuroraIMUIModule.updateMessage(messageList)
	}

	/**
	 * insert messages into messageList's top
	 * @param {Array} messageList  [message]
	 */
	static insertMessagesToTop(messageList) {
		AuroraIMUIModule.insertMessagesToTop(messageList)
	}

	/**
	 * scroll messageList to bottom
	 * @param {Boolean} animate 
	 */
	static scrollToBottom(animate) {
		AuroraIMUIModule.scrollToBottom(animate)
	}

	/**
	 * hiden featureView
	 * @param {Boolean} animate 
	 */
	static hidenFeatureView(animate) {
		AuroraIMUIModule.hidenFeatureView(animate)
	}

	/**
	 * add listener: messageList did Loaded will call cb
	 * @param {Function} cb 
	 */
	static addMessageListDidLoadedListener(cb) {
		listeners[cb] = DeviceEventEmitter.addListener(IMUIMessageListDidLoaded,
			() => {
				cb();
			});
	}

	/**
	 * remove listener:
	 * @param {Function} cb 
	 */
	static removeMessageListDidLoadedListener(cb) {
		if (!listeners[cb]) {
			return;
		}
		listeners[cb].remove();
		listeners[cb] = null;
	}
}

module.exports = {
	ChatInput: ChatInput,
	MessageList: MessageList,
	AuroraIMUIController: AuroraIMUIController
};