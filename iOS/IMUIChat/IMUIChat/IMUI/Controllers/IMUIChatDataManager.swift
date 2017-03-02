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
  
  subscript(index: Int) -> IMUIMessageModel {
    return allMessageArr[index]
  }
  
  subscript(msgId: String) -> IMUIMessageModel? {
    return allMessageDic[msgId]
  }
  
  func cleanCache() {
    self.allMessageArr.removeAll()
    self.allMessageDic.removeAll()
  }
  
  open func appendMessage(with message: IMUIMessageModel) {
    self.allMessageArr.append(message)
    self.allMessageDic[message.msgId] = message
    self.setupNeedShowTime(between: message, and: self.allMessageArr[-1])
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
