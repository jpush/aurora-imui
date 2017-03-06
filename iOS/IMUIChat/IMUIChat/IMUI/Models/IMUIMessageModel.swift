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



public protocol IMUIMessageModelProtocol {
}

extension IMUIMessageModelProtocol {
  
  func mediaData() -> Data {
    return Data()
  }
  
  func textMessage() -> String {
    return ""
  }
}

public protocol IMUIMessageDataSource {
  func messageArray(with offset:NSNumber, limit:NSNumber) -> [IMUIMessageModelProtocol]
  
}

struct IMUIMessageCellLayout {
//  static var cellEdgeInsets: UIOffset {
//    set {
//      IMUIMessageCellLayout.cellEdgeInsets = newValue
//    }
//    
//    get {
//      return IMUIMessageCellLayout.cellEdgeInsets
//    }
//  }
  
  static var avatarSize: CGSize = CGSize.zero
  
  static var avatarOffsetToCell:UIOffset = UIOffset.zero
  
  static var timeLabelFrame: CGRect = CGRect.zero
  
  static var nameLabelFrame: CGRect = CGRect.zero
  
  static var bubbleOffsetToAvatar: UIOffset = UIOffset.zero
  
  static var CellWidth: CGFloat = 0
  
  static var bubbleMaxWidth = 500
  static var isNeedShowInComingAvatar = true
  static var isNeedShowOutGoingAvtar = true

  var avatarFrame: CGRect {
    get {
      var avatarX: CGFloat
      if self.isOutGoingMessage {
        avatarX = IMUIMessageCellLayout.CellWidth - IMUIMessageCellLayout.avatarOffsetToCell.horizontal - IMUIMessageCellLayout.avatarSize.width
      } else {
        avatarX = IMUIMessageCellLayout.avatarOffsetToCell.horizontal
      }
      
      return CGRect(x: avatarX,
                    y: IMUIMessageCellLayout.avatarOffsetToCell.vertical + self.timeLabelFrame.size.height,
                    width: IMUIMessageCellLayout.avatarSize.width,
                    height: IMUIMessageCellLayout.avatarSize.height)
    }
  }
  
  var timeLabelFrame: CGRect {
    get {
      if self.isNeedShowTime {
        return IMUIMessageCellLayout.timeLabelFrame
      } else {
        return CGRect.zero
      }
    }
  }
  
  var bubbleFrame: CGRect {
    get {
      var bubbleX:CGFloat
      
      if self.isOutGoingMessage {
        bubbleX = IMUIMessageCellLayout.avatarOffsetToCell.horizontal + IMUIMessageCellLayout.avatarSize.width + IMUIMessageCellLayout.bubbleOffsetToAvatar.horizontal
      } else {
        bubbleX = IMUIMessageCellLayout.CellWidth -
          IMUIMessageCellLayout.avatarOffsetToCell.horizontal -
          IMUIMessageCellLayout.avatarSize.width -
          IMUIMessageCellLayout.bubbleOffsetToAvatar.horizontal -
          self.bubbleSize.width
      }
      return CGRect(x: bubbleX,
                    y: IMUIMessageCellLayout.bubbleOffsetToAvatar.vertical + self.avatarFrame.origin.y,
                    width: self.bubbleSize.width,
                    height: self.bubbleSize.height)
    }
  }
  
  var isOutGoingMessage: Bool
  var bubbleSize: CGSize
  var isNeedShowTime: Bool
  
  var cellHeight: CGFloat {
    get {
      return IMUIMessageCellLayout.bubbleOffsetToAvatar.vertical +
        self.avatarFrame.origin.y +
        self.bubbleSize.height
    }
  }

}


public class IMUIMessageModel: IMUIMessageModelProtocol {
  
  open var msgId: String = ""
  open var fromUser: IMUIUser
  open var isOutGoing: Bool = true
  open var date: Date
  open var isNeedShowTime: Bool = false
  open var status: IMUIMessageStatus
  open var type: IMUIMessageType
  var layout: IMUIMessageCellLayout!
  
//  func textMessage() -> String {
//    return ""
//  }
//  
//  func mediaData() -> Data {
//    return Data()
//  }
  
  open var bubbleSize: CGSize {
    get {
      if self.layout.bubbleSize.height == 0 {
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
          
          self.bubbleSize = CGSize(width: imgWidth!, height: imgHeight!)
          self.layout.bubbleSize = self.bubbleSize
          break
        case .text:
          self.bubbleSize = CGSize(width: 200, height: 200)
          self.layout.bubbleSize = self.bubbleSize
          break
        case .voice:
          self.bubbleSize = CGSize(width: 200, height: 37)
          self.layout.bubbleSize = self.bubbleSize
          break
        case .location:
          self.bubbleSize = CGSize(width: 200, height: 200)
          self.layout.bubbleSize = self.bubbleSize
          break
        default:
          break
        }
        
        return self.layout.bubbleSize
      } else {
        return self.layout.bubbleSize
      }
    }
    
    set {
      self.bubbleSize = newValue
    }
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
      
//      self.bubbleSize = CGSize(width: imgWidth!, height: imgHeight!)
//      self.layout.bubbleSize = self.bubbleSize
      bubbleSize = CGSize(width: imgWidth!, height: imgHeight!)
      break
    case .text:
//      self.bubbleSize = CGSize(width: 200, height: 200)
//      self.layout.bubbleSize = self.bubbleSize
      bubbleSize = CGSize(width: 200, height: 200)
      break
    case .voice:
//      self.bubbleSize = CGSize(width: 200, height: 37)
//      self.layout.bubbleSize = self.bubbleSize
      bubbleSize = CGSize(width: 200, height: 37)
      break
    case .location:
//      self.bubbleSize = CGSize(width: 200, height: 200)
//      self.layout.bubbleSize = self.bubbleSize
      bubbleSize = CGSize(width: 200, height: 200)
      break
    default:
      break
    }
    return bubbleSize
  }
  
  public init(msgId: String, fromUser: IMUIUser, isOutGoing: Bool, date: Date, status: IMUIMessageStatus, type: IMUIMessageType) {
    self.msgId = msgId
    self.fromUser = fromUser
    self.isOutGoing = isOutGoing
    self.date = date
    self.status = status
    self.type = type
    
    let bubbleSize = self.calculateBubbleSize()
    self.layout = IMUIMessageCellLayout(isOutGoingMessage: isOutGoing, bubbleSize: bubbleSize, isNeedShowTime: isNeedShowTime)
  }
  
}
