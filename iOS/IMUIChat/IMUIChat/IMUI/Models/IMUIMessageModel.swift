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

protocol IMUIMessageCellLayoutProtocal {
  var avatarFrame: CGRect { get }
  var timeLabelFrame: CGRect { get }
  var bubbleFrame: CGRect { get }
  var cellHeight: CGFloat { get }
}

extension IMUIMessageCellLayoutProtocal {
  var avatarFrame: CGRect {
    get {
      return CGRect.zero
    }
  }
  
  var timeLabelFrame: CGRect {
    get {
      return CGRect.zero
    }
  }
  
  var bubbleFrame: CGRect {
    get {
      return CGRect.zero
    }
  }
}


struct IMUIMessageCellLayout {
  
  static var avatarSize: CGSize = CGSize.zero
  
  static var avatarOffsetToCell:UIOffset = UIOffset.zero
  
  static var timeLabelFrame: CGRect = CGRect.zero
  
  static var nameLabelFrame: CGRect = CGRect.zero
  
  static var bubbleOffsetToAvatar: UIOffset = UIOffset.zero
  
  static var CellWidth: CGFloat = 0
  
  static var bubbleMaxWidth: CGFloat = 500.0
  static var isNeedShowInComingAvatar = true
  static var isNeedShowOutGoingAvtar = true
  
  static var contentInset: UIEdgeInsets = UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10)
  
  var isOutGoingMessage: Bool
  var bubbleSize: CGSize
  var isNeedShowTime: Bool
  
  public static func calculateTextBubbleSize(with textSize: CGSize) -> CGSize {
    let bubbleX = textSize.width +
                  IMUIMessageCellLayout.contentInset.left +
                  IMUIMessageCellLayout.contentInset.right
    
    let bubbleY = textSize.height +
                  IMUIMessageCellLayout.contentInset.bottom +
                  IMUIMessageCellLayout.contentInset.top
    
    
    return CGSize(width: bubbleX, height: bubbleY)
  }
}


extension IMUIMessageCellLayout: IMUIMessageCellLayoutProtocal {
  
  var avatarFrame: CGRect {
    get {
      var avatarX: CGFloat
      if self.isOutGoingMessage {
        avatarX = IMUIMessageCellLayout.CellWidth -
          IMUIMessageCellLayout.avatarOffsetToCell.horizontal -
          IMUIMessageCellLayout.avatarSize.width
      } else {
        avatarX = IMUIMessageCellLayout.avatarOffsetToCell.horizontal
      }
      let avatarY = IMUIMessageCellLayout.avatarOffsetToCell.vertical +
        self.timeLabelFrame.size.height
      
      return CGRect(x: avatarX,
                    y: avatarY,
                    width: IMUIMessageCellLayout.avatarSize.width,
                    height: IMUIMessageCellLayout.avatarSize.height)
    }
  }
  
  var timeLabelFrame: CGRect {
    get {
      if self.isNeedShowTime {
        return CGRect(x: 0, y: 0, width: IMUIMessageCellLayout.CellWidth, height: 20) // TODO: TEST code!
      } else {
        return CGRect.zero
      }
    }
  }
  
  var cellHeight: CGFloat {
    get {
      return  IMUIMessageCellLayout.bubbleOffsetToAvatar.vertical +
        IMUIMessageCellLayout.timeLabelFrame.size.height +
        self.avatarFrame.origin.y +
        self.bubbleSize.height
    }
  }
  
  var bubbleFrame: CGRect {
    get {
      var bubbleX:CGFloat
      
      if self.isOutGoingMessage {
        bubbleX = IMUIMessageCellLayout.CellWidth -
          IMUIMessageCellLayout.avatarOffsetToCell.horizontal -
          IMUIMessageCellLayout.avatarSize.width -
          IMUIMessageCellLayout.bubbleOffsetToAvatar.horizontal -
          self.bubbleSize.width
      } else {
        bubbleX = IMUIMessageCellLayout.avatarOffsetToCell.horizontal +
          IMUIMessageCellLayout.avatarSize.width +
          IMUIMessageCellLayout.bubbleOffsetToAvatar.horizontal
      }
      let bubbleY = IMUIMessageCellLayout.bubbleOffsetToAvatar.vertical +
        self.avatarFrame.origin.y +
        IMUIMessageCellLayout.timeLabelFrame.size.height
      
      return CGRect(x: bubbleX,
                    y: bubbleY,
                    width: bubbleSize.width,
                    height: bubbleSize.height)
    }
  }
}


// MARK: - IMUIMessageModelProtocol
class IMUIMessageModel: IMUIMessageModelProtocol {
  
  open var msgId: String = ""
  open var fromUser: IMUIUser
  open var isOutGoing: Bool = true
  open var date: Date
  
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
  
  open func calculateBubbleSize() -> CGSize {
    var bubbleSize: CGSize!
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
      
      bubbleSize = CGSize(width: imgWidth!, height: imgHeight!)
      break
    case .text:
      
      let textSize  = self.textMessage().sizeWithConstrainedWidth(with: IMUIMessageCellLayout.bubbleMaxWidth, font: UIFont.systemFont(ofSize: 18))
      
      bubbleSize = IMUIMessageCellLayout.calculateTextBubbleSize(with: textSize)
      break
    case .voice:
      bubbleSize = CGSize(width: 200, height: 37)
      break
    case .video:
      bubbleSize = CGSize(width: 200, height: 200)
      break
    case .location:
      bubbleSize = CGSize(width: 200, height: 200)
      break
    default:
      break
    }
    return bubbleSize
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
      let bubbleSize = self.calculateBubbleSize()
      self.layout = IMUIMessageCellLayout(isOutGoingMessage: isOutGoing, bubbleSize: bubbleSize, isNeedShowTime: isNeedShowTime)
    }
    
  }
  
}
