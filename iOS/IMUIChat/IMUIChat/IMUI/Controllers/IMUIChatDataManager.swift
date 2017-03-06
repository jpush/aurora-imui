//
//  IMUIChatDataManager.swift
//  IMUIChat
//
//  Created by oshumini on 2017/2/27.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

var needShowTimeInterval = 10 * 60.0

class IMUIChatDataManager: NSObject {
  var allMessageArr = [IMUIMessageModel]()
  var allMessageDic = [String:IMUIMessageModel]()
  
  var count: Int {
    get {
      return allMessageArr.count
    }
  }
  
  subscript(index: Int) -> IMUIMessageModel {
    return allMessageArr[index]
  }
  
  subscript(msgId: String) -> IMUIMessageModel? {
    return allMessageDic[msgId]
  }
  
  var endIndex: Int {
    get {
      return self.allMessageArr.endIndex
    }
  }
  
  func cleanCache() {
    self.allMessageArr.removeAll()
    self.allMessageDic.removeAll()
  }
  
  
  open func appendMessage(with message: IMUIMessageModel) {
    self.allMessageArr.append(message)
    self.allMessageDic[message.msgId] = message
    
    if self.count > 1 {
      self.setupNeedShowTime(between: message, and: self.allMessageArr[allMessageArr.endIndex - 1])
    }
  }
  
  func insertMessage(with message: IMUIMessageModel) {
    self.allMessageArr.insert(message, at: 0)
    self.allMessageDic[message.msgId] = message
    self.setupNeedShowTime(between: message, and: self.allMessageArr[1])
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
    if timeInterval > needShowTimeInterval {
      lateMessage.isNeedShowTime = true
    }
    
  }
}
