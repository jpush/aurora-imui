//
//  IMUIProtocol.swift
//  IMUIChat
//
//  Created by oshumini on 2017/2/24.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

// MARK - IMUIMessageCellLayoutProtocal
protocol IMUIMessageCellLayoutProtocal {
  
  var cellHeight: CGFloat { get }
  
  var avatarFrame: CGRect { get }
  
  var timeLabelFrame: CGRect { get }
  
  var bubbleFrame: CGRect { get }
  
//  var isNeedShowAvatar: Bool { get }
}

extension IMUIMessageCellLayoutProtocal {
  var avatarFrame: CGRect {
    return CGRect.zero
  }
  
  var timeLabelFrame: CGRect {
    return CGRect.zero
  }
}

// MARK - IMUIMessageModelProtocol
protocol IMUIMessageModelProtocol {
  func mediaData() -> Data
  
  func textMessage() -> String
  
  var msgId: String { get }
  
  var fromUser: IMUIUser { get }
  
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
