import {
	NativeModules,
	Platform,
	DeviceEventEmitter
} from 'react-native';

import ChatInput from './ReactNative/chatinput';
import MessageList from './ReactNative/messagelist';

const AuroraIMUIModule = NativeModules.AuroraIMUIModule;

const listeners = {};
const IMUIMessageListDidLoad = "IMUIMessageListDidLoad";
const GET_INPUT_TEXT_EVENT = "getInputText";

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
	 * insert messages into messageList's top, the message list to be inserted must be sorted chronologically.
	 * @param {Array} messageList  [message]
	 */
	static insertMessagesToTop(messageList) {
		AuroraIMUIModule.insertMessagesToTop(messageList)
	}

	/**
	 * remove message from messageList
	 * @param {String} messageId
	 */
	static removeMessage(messageId) {
		AuroraIMUIModule.removeMessage(messageId)
	}

	/**
	 * stop play voice 
	 */
	static stopPlayVoice() {
		AuroraIMUIModule.stopPlayVoice()
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
	 * layout input view
	 */
	static layoutInputView() {
		AuroraIMUIModule.layoutInputView()
	}
	
	/**
	 * add listener: messageList did Loaded will call cb
	 * @param {Function} cb 
	 */
	static addMessageListDidLoadListener(cb) {
		listeners[cb] = DeviceEventEmitter.addListener(IMUIMessageListDidLoad,
			() => {
				cb();
			});
	}

	/**
	 * Get Input text, Android Only
	 * @param {Function} cb
	 * @param {String} text 
	 */
	static addGetInputTextListener(cb) {
		listeners[cb] = DeviceEventEmitter.addListener(GET_INPUT_TEXT_EVENT, (text) => {
			cb(text);
		});
	}

	/**
	 * remove listener:
	 * @param {Function} cb 
	 */
	static removeMessageListDidLoadListener(cb) {
		if (!listeners[cb]) {
			return;
		}
		listeners[cb].remove();
		listeners[cb] = null;
	}

	static removeGetInputTextListener(cb) {
		if (!listeners[cb]) {
			return;
		}
		listeners[cb].remove();
		listeners[cb] = null;
	}

	/**
	 * 清空所有消息
	 */
	static removeAllMessage() {
		AuroraIMUIModule.removeAllMessage();
	}

    /**
     * 裁剪图片，将图片裁剪成 width * height 大小
     * param = { "path": String, "width": number, "height": number }
     * result = { "code": number(0 表示裁剪成功，否则不成功), "thumbPath": String }
     */
	static scaleImage(param, cb) {
	    AuroraIMUIModule.scaleImage(param, (result) => {
	        cb(result);
	    });
	}

	/**
     * 压缩图片，将图片压缩成指定质量的大小
     * param = { "path": String, "compressionQuality": number } // compressionQuality = {0 - 1} 
     * result = { "code": number(0 表示压缩成功，否则不成功), "thumbPath": String }
     */
	static compressImage(param, cb) {
	    AuroraIMUIModule.compressImage(param, (result) => {
	        cb(result);
	    });
	}
	
	
}

module.exports = {
	ChatInput: ChatInput,
	MessageList: MessageList,
	AuroraIMUIController: AuroraIMUIController,
};