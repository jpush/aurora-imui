//
//  IMUIMessageModelProtocol.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

protocol IMUIMessageModelProtocol {
  func mediaData() -> Data
  
  func textMessage() -> String
  
  var msgId: String { get }
  
  var fromUser: IMUIUserProtocol { get }
  
  var videoPath: String? { get }
  
  var layout: IMUIMessageCellLayoutProtocal { get }
}


extension IMUIMessageModelProtocol {
  
  func mediaData() -> Data {
    return Data()
  }
  
  func textMessage() -> String {
    return ""
  }
  
  var videoPath: String? {
    return nil
  }
  
}
