//
//  MessageEventModel.swift
//  sample
//
//  Created by oshumini on 2017/6/16.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation

class MessageEventModel: NSObject, IMUIMessageProtocol {

  var msgId: String = ""
  var eventText: String = ""
  
  init(msgId: String, eventText: String) {
    super.init()
    self.msgId = msgId
    self.eventText = eventText
  }
}
