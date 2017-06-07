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
  var allMessageDic = [String:IMUIMessageModelProtocol]()
  
  var count: Int {
    return allMsgidArr.count
  }
  
  subscript(index: Int) -> IMUIMessageModelProtocol {
    let msgId = allMsgidArr[index]
    return allMessageDic[msgId]!
  }
  
  subscript(msgId: String) -> IMUIMessageModelProtocol? {
    return allMessageDic[msgId]
  }
  
  var endIndex: Int {
    return allMsgidArr.endIndex
  }
  
  func cleanCache() {
    allMsgidArr.removeAll()
    allMessageDic.removeAll()
  }
  
  func index(of message: IMUIMessageModelProtocol) -> Int? {
    return allMsgidArr.index(of: message.msgId)
  }
  
  open func appendMessage(with message: IMUIMessageModelProtocol) {
    self.allMsgidArr.append(message.msgId)
    self.allMessageDic[message.msgId] = message
  }
  
  func updateMessage(with message: IMUIMessageModelProtocol) {
    if message.msgId == "" {
      print("the msgId is empty, cann't update message")
      return
    }
    
    allMessageDic[message.msgId] = message
  }
  
  func insertMessage(with message: IMUIMessageModelProtocol) {
    if message.msgId == "" {
      print("the msgId is empty, cann't insert message")
      return
    }
    
    self.allMsgidArr.insert(message.msgId, at: 0)
    self.allMessageDic[message.msgId] = message
  }
  
  open func insertMessages(with messages:[IMUIMessageModelProtocol]) {
    for element in messages {
      self.insertMessage(with: element)
    }
  }
}
