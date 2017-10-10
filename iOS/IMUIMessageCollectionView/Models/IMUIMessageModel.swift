//
//  MessageModel.swift
//  IMUIChat
//
//  Created by oshumini on 2017/2/24.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit


@objc public enum IMUIMessageStatus: UInt {
  // Sending message status
  case failed
  case sending
  case success
  
  // received message status
  case mediaDownloading
  case mediaDownloadFail
}

public protocol IMUIMessageDataSource {
  func messageArray(with offset:NSNumber, limit:NSNumber) -> [IMUIMessageModelProtocol]
  
}


// MARK: - IMUIMessageModelProtocol

/**
 *  The class `IMUIMessageModel` is a concrete class for message model objects that represent a single user message
 *  The message can be text \ voice \ image \ video \ message
 *  It implements `IMUIMessageModelProtocol` protocol
 *
 */
open class IMUIMessageModel: NSObject, IMUIMessageModelProtocol {
  
  @objc public var duration: CGFloat

  
  open var msgId = {
    return ""
  }()

  open var messageStatus: IMUIMessageStatus
  
  open var fromUser: IMUIUserProtocol
  
  open var isOutGoing: Bool = true
  open var time: String
  
  open var timeString: String {
    return time
  }
  
  open var isNeedShowTime: Bool  {
    if timeString != "" {
      return true
    } else {
      return false
    }
  }
  
  open var type: String
  
  open var layout: IMUIMessageCellLayoutProtocol {
    return cellLayout!
  }
  open var cellLayout: IMUIMessageCellLayoutProtocol?
  
  open func text() -> String {
    return ""
  }
  
  open func mediaFilePath() -> String {
    return ""
  }
  
  public init(msgId: String, messageStatus: IMUIMessageStatus, fromUser: IMUIUserProtocol, isOutGoing: Bool, time: String, type: String, cellLayout: IMUIMessageCellLayoutProtocol, duration: CGFloat?) {
    self.msgId = msgId
    self.fromUser = fromUser
    self.isOutGoing = isOutGoing
    self.time = time
    self.type = type
    self.messageStatus = messageStatus
    
    self.duration = duration ?? 0.0
    
    super.init()
      self.cellLayout = cellLayout
  }
  
  open var resizableBubbleImage: UIImage {
    var bubbleImg: UIImage?
    if isOutGoing {
      bubbleImg = UIImage.imuiImage(with: "outGoing_bubble")
      bubbleImg = bubbleImg?.resizableImage(withCapInsets: UIEdgeInsetsMake(24, 10, 9, 15), resizingMode: .tile)
    } else {
      bubbleImg = UIImage.imuiImage(with: "inComing_bubble")
      bubbleImg = bubbleImg?.resizableImage(withCapInsets: UIEdgeInsetsMake(24, 15, 9, 10), resizingMode: .tile)
    }
    
    return bubbleImg!
  }
  
}
