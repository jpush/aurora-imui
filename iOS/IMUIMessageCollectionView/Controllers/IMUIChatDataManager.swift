//
//  IMUIChatDataManager.swift
//  IMUIChat
//
//  Created by oshumini on 2017/2/27.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

var needShowTimeInterval = Double.greatestFiniteMagnitude


class IMUIChatDataManager: NSObject {
  var allMsgidArr = [String]()
  var allMessageDic = [String:IMUIMessageProtocol]()
  
  var count: Int {
    return allMsgidArr.count
  }
  
  subscript(index: Int) -> IMUIMessageProtocol {
    let msgId = allMsgidArr[index]
    return allMessageDic[msgId]!
  }
  
  subscript(msgId: String) -> IMUIMessageProtocol? {
    return allMessageDic[msgId]
  }
  
  var endIndex: Int {
    return allMsgidArr.endIndex
  }
  
  func cleanCache() {
    allMsgidArr.removeAll()
    allMessageDic.removeAll()
  }
  
  func index(of message: IMUIMessageProtocol) -> Int? {
    return allMsgidArr.index(of: message.msgId)
  }
  
  @objc open func appendMessage(with message: IMUIMessageProtocol) {
    self.allMsgidArr.append(message.msgId)
    self.allMessageDic[message.msgId] = message
  }
  
  func updateMessage(with message: IMUIMessageProtocol) {
    if message.msgId == "" {
      print("the msgId is empty, cann't update message")
      return
    }
    
    allMessageDic[message.msgId] = message
  }
  
  func removeMessage(with messageId: String) {
    if messageId == "" {
      print("the msgId is empty, cann't update message")
      return
    }
    allMessageDic.removeValue(forKey: messageId)
    if let index = allMsgidArr.index(of: messageId) {
      allMsgidArr.remove(at: index)
    }
  }
  
  
  func insertMessage(with message: IMUIMessageProtocol) {
    if message.msgId == "" {
      print("the msgId is empty, cann't insert message")
      return
    }
    
    self.allMsgidArr.insert(message.msgId, at: 0)
    self.allMessageDic[message.msgId] = message
  }
  
  open func insertMessages(with messages:[IMUIMessageProtocol]) {
    for element in messages {
      self.insertMessage(with: element)
    }
  }
}
