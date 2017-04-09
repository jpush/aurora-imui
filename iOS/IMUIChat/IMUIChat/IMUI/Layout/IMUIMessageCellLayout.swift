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

  static var avatarSize: CGSize = CGSize(width: 40, height: 40)
  
  static var avatarOffsetToCell: UIOffset = UIOffset(horizontal: 16, vertical: 16)
  
  static var timeLabelFrame: CGRect = CGRect.zero
  
  static var nameLabelFrame: CGRect = CGRect.zero
  
  static var bubbleOffsetToAvatar: UIOffset = UIOffset(horizontal: 8, vertical: 0)
  
  static var cellWidth: CGFloat = 0
  
  static var cellContentInset: UIEdgeInsets = UIEdgeInsets(top: 0, left: 0, bottom: 10, right: 0)
  
  static var bubbleMaxWidth: CGFloat = 200.0
  static var isNeedShowInComingAvatar = true
  static var isNeedShowOutGoingAvtar = true
  
  var isOutGoingMessage: Bool
  
  var isNeedShowTime: Bool
  
  internal var bubbleContentSize: CGSize
  
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
    let bubbleContentPostion = CGPoint(x: bubbleContentInset.left,
                                       y: bubbleContentInset.top)
    return CGRect(origin: bubbleContentPostion, size: self.bubbleContentSize)
  }
  
  
  
  internal var bubbleContentInset = {
    return UIEdgeInsets.zero
  }()
  
  
  
  // MARK - IMUIMessageCellLayoutProtocal
  var avatarFrame: CGRect {
    
    var avatarX: CGFloat
    if self.isOutGoingMessage {
      avatarX = IMUIMessageCellLayout.cellWidth -
        IMUIMessageCellLayout.avatarOffsetToCell.horizontal -
        IMUIMessageCellLayout.avatarSize.width -
        IMUIMessageCellLayout.cellContentInset.right
      
    } else {
      avatarX = IMUIMessageCellLayout.avatarOffsetToCell.horizontal +
        IMUIMessageCellLayout.cellContentInset.left
    }
    
    let avatarY = IMUIMessageCellLayout.avatarOffsetToCell.vertical +
      self.timeLabelFrame.size.height +
      IMUIMessageCellLayout.cellContentInset.top
    
    return CGRect(x: avatarX,
                  y: avatarY,
                  width: IMUIMessageCellLayout.avatarSize.width,
                  height: IMUIMessageCellLayout.avatarSize.height)
  }
  
  var timeLabelFrame: CGRect {
    if self.isNeedShowTime {
      let timeWidth = IMUIMessageCellLayout.cellWidth -
        IMUIMessageCellLayout.cellContentInset.left -
        IMUIMessageCellLayout.cellContentInset.right
      
      return CGRect(x: IMUIMessageCellLayout.cellContentInset.left,
                    y: IMUIMessageCellLayout.cellContentInset.top,
                    width: timeWidth,
                    height: 20) // TODO: TEST code!
    } else {
      return CGRect.zero
    }
  }
  
  var cellHeight: CGFloat {
    return  IMUIMessageCellLayout.bubbleOffsetToAvatar.vertical +
      IMUIMessageCellLayout.timeLabelFrame.size.height +
      self.avatarFrame.origin.y +
      self.bubbleSize.height +
      IMUIMessageCellLayout.cellContentInset.top +
      IMUIMessageCellLayout.cellContentInset.bottom
  }
  
  var bubbleFrame: CGRect {
    
    var bubbleX:CGFloat
    
    if self.isOutGoingMessage {
      bubbleX = IMUIMessageCellLayout.cellWidth -
        IMUIMessageCellLayout.avatarOffsetToCell.horizontal -
        IMUIMessageCellLayout.avatarSize.width -
        IMUIMessageCellLayout.bubbleOffsetToAvatar.horizontal -
        IMUIMessageCellLayout.cellContentInset.right -
        self.bubbleSize.width
    } else {
      bubbleX = IMUIMessageCellLayout.avatarOffsetToCell.horizontal +
        IMUIMessageCellLayout.avatarSize.width +
        IMUIMessageCellLayout.bubbleOffsetToAvatar.horizontal +
        IMUIMessageCellLayout.cellContentInset.left
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
