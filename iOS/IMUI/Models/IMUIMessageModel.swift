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


protocol IMUIMessageDataSource {
  func messageArray(with offset:NSNumber, limit:NSNumber) -> [IMUIMessageModelProtocol]
  
}


// MARK: - IMUIMessageModelProtocol

/**
 *  The class `IMUIMessageModel` is a concrete class for message model objects that represent a single user message
 *  The message can be text \ voice \ image \ video \ message
 *  It implements `IMUIMessageModelProtocol` protocal
 *
 */
class IMUIMessageModel: IMUIMessageModelProtocol {
  
  internal var msgId = {
    return ""
  }()
  
  internal var fromUser: IMUIUserProtocol
  
  internal var isOutGoing: Bool = true
  internal var date: Date
  
  internal var timeString: String {
    return date.parseDate
  }
  
  open var isNeedShowTime: Bool = false {
    didSet {
//      cellLayout?.isNeedShowTime = isNeedShowTime
    }
  }
  
  open var status: IMUIMessageStatus
  open var type: IMUIMessageType
  
  internal var layout: IMUIMessageCellLayoutProtocal {
    return cellLayout!
  }
  internal var cellLayout: IMUIMessageCellLayoutProtocal?
  
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
      bubbleContentSize = CGSize(width: 80, height: 37)
      break
    case .video:
      bubbleContentSize = CGSize(width: 120, height: 160)
      break
    case .location:
      bubbleContentSize = CGSize(width: 200, height: 200)
      break
    default:
      break
    }
    
    return bubbleContentSize
  }
  
  
  public init(msgId: String, fromUser: IMUIUserProtocol, isOutGoing: Bool, date: Date, status: IMUIMessageStatus, type: IMUIMessageType, cellLayout: IMUIMessageCellLayoutProtocal?) {
    self.msgId = msgId
    self.fromUser = fromUser
    self.isOutGoing = isOutGoing
    self.date = date
    self.status = status
    self.type = type
    
    if let layout = cellLayout {
      self.cellLayout = layout
    } else {
      let bubbleSize = self.calculateBubbleContentSize()
      self.cellLayout = IMUIMessageCellLayout(isOutGoingMessage: isOutGoing, isNeedShowTime: isNeedShowTime, bubbleContentSize: bubbleSize)
    }
  }
  
  var resizableBubbleImage: UIImage {
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
