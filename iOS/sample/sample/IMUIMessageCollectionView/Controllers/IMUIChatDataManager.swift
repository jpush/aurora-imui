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
  var allMessageDic = [String:IMUIMessageModel]()
  
  var count: Int {
    return allMsgidArr.count
  }
  
  subscript(index: Int) -> IMUIMessageModel {
    let msgId = allMsgidArr[index]
    return allMessageDic[msgId]!
  }
  
  subscript(msgId: String) -> IMUIMessageModel? {
    return allMessageDic[msgId]
  }
  
  var endIndex: Int {
    return allMsgidArr.endIndex
  }
  
  func cleanCache() {
    allMsgidArr.removeAll()
    allMessageDic.removeAll()
  }
  
  func index(of message: IMUIMessageModel) -> Int? {
    return allMsgidArr.index(of: message.msgId)
  }
  
  open func appendMessage(with message: IMUIMessageModel) {
    if self.count >= 1 {
        self.setupNeedShowTime(between: message, and: self.allMessageDic[allMsgidArr.last!]!)
    }
    
    self.allMsgidArr.append(message.msgId)
    self.allMessageDic[message.msgId] = message
  }
  
  func updateMessage(with message: IMUIMessageModel) {
    if message.msgId == "" {
      print("the msgId is empty, cann't update message")
      return
    }
    
    allMessageDic[message.msgId] = message
  }
  
  func insertMessage(with message: IMUIMessageModel) {
    if message.msgId == "" {
      print("the msgId is empty, cann't insert message")
      return
    }
    
    self.allMsgidArr.insert(message.msgId, at: 0)
    self.allMessageDic[message.msgId] = message
    self.setupNeedShowTime(between: message, and: allMessageDic[allMsgidArr[1]]!)
  }
  
  open func insertMessages(with messages:[IMUIMessageModel]) {
    for element in messages {
      self.insertMessage(with: element)
    }
  }
  
  open func setupNeedShowTime(between earlyMessage: IMUIMessageModel, and lateMessage: IMUIMessageModel) {
    let earlyDate = earlyMessage.date
    let lateDate = lateMessage.date
    let timeInterval = earlyDate.timeIntervalSince(lateDate)
    if timeInterval >= needShowTimeInterval {
      lateMessage.isNeedShowTime = true
    }
    
  }
}
