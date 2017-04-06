//
//  MessageModel.swift
//  IMUIChat
//
//  Created by oshumini on 2017/2/24.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit


public enum IMUIMessageType {
  case text
  case image
  case voice
  case video
  case location
  case custom
}


public enum IMUIMessageStatus {
  case failed
  case sending
  case success
}


public enum IMUIMessageReceiveStatus {
  case failed
  case sending
  case success
}


protocol IMUIMessageModelProtocol {
  func mediaData() -> Data
  
  func textMessage() -> String
  
  var msgId: String { get }
  
  var fromUser: IMUIUser { get }
  
  var videoPath: String? { get }
  
  var layout: IMUIMessageCellLayoutProtocal? { get }
}


extension IMUIMessageModelProtocol {
  
  func mediaData() -> Data {
    return Data()
  }
  
  func textMessage() -> String {
    return ""
  }
  
  var videoPath: String? {
    get {
      return nil
    }
  }
  
  var layout: IMUIMessageCellLayoutProtocal? {
    get {
      return nil
    }
  }
}


protocol IMUIMessageDataSource {
  func messageArray(with offset:NSNumber, limit:NSNumber) -> [IMUIMessageModelProtocol]
  
}


// MARK: - IMUIMessageModelProtocol
class IMUIMessageModel: IMUIMessageModelProtocol {

  internal var msgId = {
    return ""
  }()
  
  internal var fromUser = {
     return IMUIUser()
  }()
  
  internal var isOutGoing: Bool = true
  internal var date: Date
  
  open var isNeedShowTime: Bool = false {
    didSet {
      layout?.isNeedShowTime = isNeedShowTime
    }
  }
  
  open var status: IMUIMessageStatus
  open var type: IMUIMessageType
  var layout: IMUIMessageCellLayout?
  
  public func textMessage() -> String {
    return ""
  }
  
  public func mediaData() -> Data {
    return Data()
  }
  
  public var videoPath: String? {
    return nil
  }
  
  open func calculateBubbleContentSize() -> CGSize {
    var bubbleContentSize: CGSize!
    switch type {
    case .image:
      var imgHeight:CGFloat?
      var imgWidth:CGFloat?
      
      let img:UIImage = UIImage(data: self.mediaData() as Data)!
      
      if img.size.height >= img.size.width {
        imgHeight = CGFloat(135)
        imgWidth = img.size.width/img.size.height * imgHeight!
        imgWidth = (imgWidth! < 55) ? 55 : imgWidth
      } else {
        imgWidth = CGFloat(135)
        imgHeight = img.size.height/img.size.width * imgWidth!
        imgHeight = (imgHeight! < 55) ? 55 : imgHeight!
      }
      
      bubbleContentSize = CGSize(width: imgWidth!, height: imgHeight!)
      break
    case .text:
      let textSize  = self.textMessage().sizeWithConstrainedWidth(with: IMUIMessageCellLayout.bubbleMaxWidth, font: UIFont.systemFont(ofSize: 18))
      
      bubbleContentSize = textSize
      break
    case .voice:
      bubbleContentSize = CGSize(width: 200, height: 37)
      break
    case .video:
      bubbleContentSize = CGSize(width: 200, height: 200)
      break
    case .location:
      bubbleContentSize = CGSize(width: 200, height: 200)
      break
    default:
      break
    }
    
    return bubbleContentSize
  }
  
  
  public init(msgId: String, fromUser: IMUIUser, isOutGoing: Bool, date: Date, status: IMUIMessageStatus, type: IMUIMessageType, cellLayout: IMUIMessageCellLayout?) {
    self.msgId = msgId
    self.fromUser = fromUser
    self.isOutGoing = isOutGoing
    self.date = date
    self.status = status
    self.type = type
    
    if let layout = cellLayout {
      self.layout = layout
    } else {
      let bubbleSize = self.calculateBubbleContentSize()
      self.layout = IMUIMessageCellLayout(isOutGoingMessage: isOutGoing, bubbleContentSize: bubbleSize, bubbleContentInset: UIEdgeInsets.zero, isNeedShowTime: isNeedShowTime)
      
    }
  }
  
}
