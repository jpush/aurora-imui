//
//  MessageModel.swift
//  IMUIChat
//
//  Created by oshumini on 2017/2/24.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit


@objc public enum IMUIMessageType: Int {
  case text
  case image
  case voice
  case video
  case location
  case custom
}


@objc public enum IMUIMessageStatus: UInt {
  // Sending message status
  case failed
  case sending
  case success
  
  // received message status
  case mediaDownloading
  case mediaDownloadFail
}

//
//public enum IMUIMessageReceiveStatus {
//  case failed
//  case sending
//  case success
//}


public protocol IMUIMessageDataSource {
  func messageArray(with offset:NSNumber, limit:NSNumber) -> [IMUIMessageModelProtocol]
  
}


// MARK: - IMUIMessageModelProtocol

/**
 *  The class `IMUIMessageModel` is a concrete class for message model objects that represent a single user message
 *  The message can be text \ voice \ image \ video \ message
 *  It implements `IMUIMessageModelProtocol` protocal
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
  
  open var status: IMUIMessageStatus
  open var type: String
  
  open var layout: IMUIMessageCellLayoutProtocal {
    return cellLayout!
  }
  open var cellLayout: IMUIMessageCellLayoutProtocal?
  
  open func text() -> String {
    return ""
  }
  
  open func mediaFilePath() -> String {
    return ""
  }
  
//  open func calculateBubbleContentSize() -> CGSize {
//    var bubbleContentSize: CGSize!
//    if type == "image" {
//      do {
//        var imgHeight:CGFloat?
//        var imgWidth:CGFloat?
//        let imageData = try Data(contentsOf: URL(fileURLWithPath: self.mediaFilePath()))
//        let img:UIImage = UIImage(data: imageData)!
//        
//        if img.size.height >= img.size.width {
//          imgHeight = CGFloat(135)
//          imgWidth = img.size.width/img.size.height * imgHeight!
//          imgWidth = (imgWidth! < 55) ? 55 : imgWidth
//        } else {
//          imgWidth = CGFloat(135)
//          imgHeight = img.size.height/img.size.width * imgWidth!
//          imgHeight = (imgHeight! < 55) ? 55 : imgHeight!
//        }
//        
//        bubbleContentSize = CGSize(width: imgWidth!, height: imgHeight!)
//      } catch {
//        print("load image file fail")
//      }
//      
//    }
//    
//    if type == "text" {
//      
//      if isOutGoing {
//        bubbleContentSize = self.text().sizeWithConstrainedWidth(with: IMUIMessageCellLayout.bubbleMaxWidth, font: IMUITextMessageCell.outGoingTextFont)
//      } else {
//        bubbleContentSize = self.text().sizeWithConstrainedWidth(with: IMUIMessageCellLayout.bubbleMaxWidth, font: IMUITextMessageCell.inComingTextFont)
//      }
//    }
//    
//    if type == "voice" {
//      bubbleContentSize = CGSize(width: 80, height: 37)
//    }
//    
//    if type == "video" {
//      bubbleContentSize = CGSize(width: 120, height: 160)
//    }
//    
//
//    return bubbleContentSize
//  }
  
  
  public init(msgId: String, messageStatus: IMUIMessageStatus, fromUser: IMUIUserProtocol, isOutGoing: Bool, time: String, status: IMUIMessageStatus, type: String, cellLayout: IMUIMessageCellLayoutProtocal) {
    self.msgId = msgId
    self.fromUser = fromUser
    self.isOutGoing = isOutGoing
    self.time = time
    self.status = status
    self.type = type
    self.messageStatus = messageStatus
    
    self.duration = 0.0
    
    super.init()
    
//    if let layout = cellLayout {
      self.cellLayout = cellLayout
//    } else {
//      let bubbleSize = self.calculateBubbleContentSize()
//      self.cellLayout = IMUIMessageCellLayout(isOutGoingMessage: isOutGoing, isNeedShowTime: isNeedShowTime, bubbleContentSize: bubbleSize, bubbleContentInsets: UIEdgeInsets.zero)
//    }
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
