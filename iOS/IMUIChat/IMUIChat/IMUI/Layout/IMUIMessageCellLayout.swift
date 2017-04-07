//
//  IMUIMessageCellLayout.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/6.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit


struct IMUIMessageCellLayout: IMUIMessageCellLayoutProtocal {

  
  static var avatarSize: CGSize = CGSize.zero
  
  static var avatarOffsetToCell:UIOffset = UIOffset.zero
  
  static var timeLabelFrame: CGRect = CGRect.zero
  
  static var nameLabelFrame: CGRect = CGRect.zero
  
  static var bubbleOffsetToAvatar: UIOffset = UIOffset.zero
  
  static var CellWidth: CGFloat = 0
  
  static var bubbleMaxWidth: CGFloat = 500.0
  static var isNeedShowInComingAvatar = true
  static var isNeedShowOutGoingAvtar = true
  
  var isOutGoingMessage: Bool
  
  var bubbleSize: CGSize {
    let bubbleWidth = bubbleContentSize.width +
      bubbleContentInset.left +
      bubbleContentInset.right
    
    let bubbleHeight = bubbleContentSize.height +
      bubbleContentInset.top +
      bubbleContentInset.bottom
    
    return CGSize(width: bubbleWidth, height: bubbleHeight)
  }
  
  var bubbleContentFrame: CGRect {
    let bubbleContentPostion = CGPoint(x: self.bubbleContentInset.left, y: self.bubbleContentInset.top)
    return CGRect(origin: bubbleContentPostion, size: self.bubbleContentSize)
  }
  
  internal var bubbleContentSize: CGSize
  
  internal var bubbleContentInset = {
    return UIEdgeInsets.zero
  }()
  
  
  var isNeedShowTime: Bool
  
  
  // MARK - IMUIMessageCellLayoutProtocal
  var avatarFrame: CGRect {
    
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
  
  var timeLabelFrame: CGRect {
    if self.isNeedShowTime {
      return CGRect(x: 0, y: 0, width: IMUIMessageCellLayout.CellWidth, height: 20) // TODO: TEST code!
    } else {
      return CGRect.zero
    }
  }
  
  var cellHeight: CGFloat {
    return  IMUIMessageCellLayout.bubbleOffsetToAvatar.vertical +
      IMUIMessageCellLayout.timeLabelFrame.size.height +
      self.avatarFrame.origin.y +
      self.bubbleSize.height
  }
  
  var bubbleFrame: CGRect {
    
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
