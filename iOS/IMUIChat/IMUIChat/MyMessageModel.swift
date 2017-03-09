//
//  MyMessageModel.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/5.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit



//extension MyMessageModel {
//  func textMessage() -> String {
//    return self.myTextMessage
//  }
//  
//  func mediaData() -> Data {
//    return Data()
//  }
//}

class MyMessageModel: IMUIMessageModel {
  open var myTextMessage: String = ""
  
  init(msgId: String, fromUser: IMUIUser, isOutGoing: Bool, date: Date, status: IMUIMessageStatus, type: IMUIMessageType, text: String) {
    self.myTextMessage = text
    super.init(msgId: msgId, fromUser: fromUser, isOutGoing: isOutGoing, date: date, status: status, type: type)
  }
  
  convenience init(with text: String) {
    self.init(msgId: "", fromUser: IMUIUser(), isOutGoing: true, date: Date(), status: .success, type: .text, text: text)
  }
  
  override func textMessage() -> String {
    return self.myTextMessage
  }
  
  override func mediaData() -> Data {
    return Data()
  }
  
}
