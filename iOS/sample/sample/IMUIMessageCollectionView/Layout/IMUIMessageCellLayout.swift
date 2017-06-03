//
//  IMUIMessageCellLayout.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/6.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

/**
 *  The 'IMUIMessageCellLayout' is a concrete layout object comfort
 *  'IMUIMessageCellLayoutProtocal' protocol.
 *  each IMUIMessageBaseCell need IMUIMessageCellLayoutProtocal to layout cell's items
 */
open class IMUIMessageCellLayout: NSObject, IMUIMessageCellLayoutProtocal {

  public static var avatarSize: CGSize = CGSize(width: 40, height: 40)
  
  public static var avatarOffsetToCell: UIOffset = UIOffset(horizontal: 16, vertical: 16)
  
  public static var timeLabelFrame: CGRect = CGRect.zero
  
  public static var nameLabelFrame: CGRect = CGRect.zero
  
  public static var bubbleOffsetToAvatar: UIOffset = UIOffset(horizontal: 8, vertical: 0)
  
  public static var cellWidth: CGFloat = 0
  
  public static var cellContentInset: UIEdgeInsets = UIEdgeInsets(top: 0, left: 0, bottom: 10, right: 0)
  
  public static var statusViewSize: CGSize = CGSize(width: 30, height: 30)
  
  public static var statusViewOffsetToBubble: UIOffset = UIOffset(horizontal: 12, vertical: 0)
  
  public static var bubbleMaxWidth: CGFloat = 170.0
  public static var isNeedShowInComingAvatar = true
  public static var isNeedShowOutGoingAvtar = true
  
  public init(isOutGoingMessage: Bool,
                 isNeedShowTime: Bool,
              bubbleContentSize: CGSize) {
    self.isOutGoingMessage = isOutGoingMessage
    self.isNeedShowTime = isNeedShowTime
    self.bubbleContentSize = bubbleContentSize
    
  }
  
  open var isOutGoingMessage: Bool
  
  open var isNeedShowTime: Bool
  
  open var bubbleContentSize: CGSize
  
  var bubbleSize: CGSize {
    let bubbleWidth = bubbleContentSize.width +
      bubbleContentInset.left +
      bubbleContentInset.right
    
    let bubbleHeight = bubbleContentSize.height +
      bubbleContentInset.top +
      bubbleContentInset.bottom
    
    return CGSize(width: bubbleWidth, height: bubbleHeight)
  }
  
  open var bubbleContentFrame: CGRect {
    let bubbleContentPostion = CGPoint(x: bubbleContentInset.left,
                                       y: bubbleContentInset.top)
    return CGRect(origin: bubbleContentPostion, size: self.bubbleContentSize)
  }
  
  
  // MARK - IMUIMessageCellLayoutProtocal
  open var bubbleContentInset: UIEdgeInsets {
    return UIEdgeInsets.zero
  }
  
  open var avatarFrame: CGRect {
    
    var avatarX: CGFloat
    if self.isOutGoingMessage {
      avatarX = IMUIMessageCellLayout.cellWidth -
        IMUIMessageCellLayout.avatarOffsetToCell.horizontal -
        IMUIMessageCellLayout.avatarSize.width -
        cellContentInset.right
      
    } else {
      avatarX = IMUIMessageCellLayout.avatarOffsetToCell.horizontal +
        cellContentInset.left
    }
    
    let avatarY = IMUIMessageCellLayout.avatarOffsetToCell.vertical +
      self.timeLabelFrame.size.height +
      cellContentInset.top
    
    return CGRect(x: avatarX,
                  y: avatarY,
                  width: IMUIMessageCellLayout.avatarSize.width,
                  height: IMUIMessageCellLayout.avatarSize.height)
  }
  
  open var timeLabelFrame: CGRect {
    if self.isNeedShowTime {
      let timeWidth = IMUIMessageCellLayout.cellWidth -
        cellContentInset.left -
        cellContentInset.right
      
      return CGRect(x: cellContentInset.left,
                    y: cellContentInset.top,
                    width: timeWidth,
                    height: 20)
    } else {
      return CGRect.zero
    }
  }
  
  open var cellHeight: CGFloat {
    return  IMUIMessageCellLayout.bubbleOffsetToAvatar.vertical +
      IMUIMessageCellLayout.timeLabelFrame.size.height +
      self.avatarFrame.origin.y +
      self.bubbleSize.height +
      cellContentInset.top +
      cellContentInset.bottom
  }
  
  open var bubbleFrame: CGRect {
    var bubbleX:CGFloat
    
    if self.isOutGoingMessage {
      bubbleX = IMUIMessageCellLayout.cellWidth -
        IMUIMessageCellLayout.avatarOffsetToCell.horizontal -
        IMUIMessageCellLayout.avatarSize.width -
        IMUIMessageCellLayout.bubbleOffsetToAvatar.horizontal -
        cellContentInset.right -
        self.bubbleSize.width
    } else {
      bubbleX = IMUIMessageCellLayout.avatarOffsetToCell.horizontal +
        IMUIMessageCellLayout.avatarSize.width +
        IMUIMessageCellLayout.bubbleOffsetToAvatar.horizontal +
        cellContentInset.left
    }
    let bubbleY = IMUIMessageCellLayout.bubbleOffsetToAvatar.vertical +
      self.avatarFrame.origin.y +
      IMUIMessageCellLayout.timeLabelFrame.size.height
    
    return CGRect(x: bubbleX,
                  y: bubbleY,
                  width: bubbleSize.width,
                  height: bubbleSize.height)
  }
  
  open var cellContentInset: UIEdgeInsets {
    return IMUIMessageCellLayout.cellContentInset
  }
  
  open var statusView: IMUIMessageStatusViewProtocal {
    return IMUIMessageDefaultStatusView()
  }

  open var statusViewFrame: CGRect {
    
    var statusViewX: CGFloat = 0.0
    let statusViewY: CGFloat = bubbleFrame.origin.y +
    bubbleFrame.size.height/2 -
    IMUIMessageCellLayout.statusViewSize.height/2 -
    IMUIMessageCellLayout.statusViewOffsetToBubble.vertical
    
    if isOutGoingMessage {
      statusViewX = bubbleFrame.origin.x -
        IMUIMessageCellLayout.statusViewOffsetToBubble.horizontal -
        IMUIMessageCellLayout.statusViewSize.width
    } else {
      statusViewX = bubbleFrame.origin.x +
        bubbleFrame.size.width +
        IMUIMessageCellLayout.statusViewOffsetToBubble.horizontal
    }
    
    return CGRect(x: statusViewX,
                  y: statusViewY,
                  width: IMUIMessageCellLayout.statusViewSize.width,
                  height: IMUIMessageCellLayout.statusViewSize.height)
    
  }
}
