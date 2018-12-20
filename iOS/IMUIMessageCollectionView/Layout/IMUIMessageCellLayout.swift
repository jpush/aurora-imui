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
 *  'IMUIMessageCellLayoutProtocol' protocol.
 *  each IMUIMessageBaseCell need IMUIMessageCellLayoutProtocol to layout cell's items
 */
@objc open class IMUIMessageCellLayout: NSObject, IMUIMessageCellLayoutProtocol {

  @objc public static var avatarSize: CGSize = CGSize(width: 40, height: 40)

  @objc public static var avatarPadding: UIEdgeInsets = UIEdgeInsets(top: 16, left: 16, bottom: 0, right: 16)
  
  @objc public static var timeLabelPadding: UIEdgeInsets = UIEdgeInsets(top: 4, left: 4, bottom: 4, right: 4)
  
  @objc public static var nameLabelSize: CGSize = CGSize(width: 200, height: 18)

  @objc public static var nameLabelPadding: UIEdgeInsets = UIEdgeInsets(top: 0, left: 8, bottom: 0, right: 8)

  @objc public static var bubblePadding: UIEdgeInsets = UIEdgeInsets(top: 4, left: 8, bottom: 0, right: 8)
  
  @objc public static var cellWidth: CGFloat = 0
  @objc public static var cellContentInset: UIEdgeInsets = UIEdgeInsets(top: 0, left: 0, bottom: 10, right: 0)
  
  @objc public static var statusViewSize: CGSize = CGSize(width: 30, height: 30)
  @objc public static var statusViewOffsetToBubble: UIOffset = UIOffset(horizontal: 12, vertical: 0)
  
  @objc public static var bubbleMaxWidth: CGFloat = 170.0
  @objc public static var isNeedShowInComingName = false
  @objc public static var isNeedShowOutGoingName = false
  
  @objc public static var isNeedShowInComingAvatar = true
  @objc public static var isNeedShowOutGoingAvatar = true
  
  @objc public static var nameLabelTextColor: UIColor = UIColor(netHex: 0x7587A8)
  @objc public static var nameLabelTextFont: UIFont = UIFont.systemFont(ofSize: 12)
  @objc public static var nameLablePadding: UIEdgeInsets = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)//
  
  
  @objc public static var timeStringColor: UIColor = UIColor(netHex: 0x90A6C4)
  @objc public static var timeStringFont: UIFont = UIFont.systemFont(ofSize: 12)
  @objc public static var timeStringBackgroundColor: UIColor = UIColor.clear//
//  @objc public static var timeStringPadding: UIEdgeInsets = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)//
  @objc public static var timeStringCornerRadius: CGFloat = 0.0//
  
  
  @objc public init(isOutGoingMessage: Bool,
                       isNeedShowTime: Bool,
                    bubbleContentSize: CGSize,
                  bubbleContentInsets: UIEdgeInsets,
                 timeLabelContentSize: CGSize) {
    self.isOutGoingMessage = isOutGoingMessage
    self.isNeedShowTime = isNeedShowTime
    self.bubbleContentSize = bubbleContentSize
    self.bubbleContentInsets = bubbleContentInsets
    self.timeLabelContentSize = timeLabelContentSize
  }
  
  open var isOutGoingMessage: Bool
  
  open var isNeedShowTime: Bool
  
  open var timeLabelContentSize: CGSize
  open var bubbleContentSize: CGSize
  open var bubbleContentInsets: UIEdgeInsets
  
  open var bubbleSize: CGSize {
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

  public var relativeStatusViewOffsetToBubble: UIOffset {
    if self.isOutGoingMessage {
      return UIOffset(horizontal: -IMUIMessageCellLayout.statusViewOffsetToBubble.horizontal, vertical: IMUIMessageCellLayout.statusViewOffsetToBubble.vertical)
    } else {
      return IMUIMessageCellLayout.statusViewOffsetToBubble
    }
  }
  
  // MARK - IMUIMessageCellLayoutProtocol
  open var bubbleContentInset: UIEdgeInsets {
    return bubbleContentInsets
  }
  
  open var nameLabelFrame: CGRect {
    var nameLabelX: CGFloat
    let nameLabelY = avatarFrame.top +
      IMUIMessageCellLayout.nameLabelPadding.top
    if isOutGoingMessage {

      nameLabelX = avatarFrame.left -
      IMUIMessageCellLayout.avatarPadding.left -
      IMUIMessageCellLayout.nameLabelPadding.right -
      IMUIMessageCellLayout.nameLabelSize.width
      
      if !IMUIMessageCellLayout.isNeedShowOutGoingName {
        return CGRect(x: nameLabelX,
                      y: nameLabelY,
                      width: 0,
                      height: 0)
      }
      
    } else {
      nameLabelX = avatarFrame.right +
        IMUIMessageCellLayout.avatarPadding.right +
        IMUIMessageCellLayout.nameLabelPadding.left
      
      if !IMUIMessageCellLayout.isNeedShowInComingName {
        return CGRect(x: nameLabelX,
                      y: nameLabelY,
                      width: 0,
                      height: 0)
      }
    }
    
    return CGRect(x: nameLabelX,
                  y: nameLabelY,
                  width: IMUIMessageCellLayout.nameLabelSize.width,
                  height: IMUIMessageCellLayout.nameLabelSize.height)
  }
  
  open var avatarFrame: CGRect {
    
    var avatarX: CGFloat
    if self.isOutGoingMessage {
      
      avatarX = IMUIMessageCellLayout.cellWidth -
        IMUIMessageCellLayout.avatarPadding.right -
        IMUIMessageCellLayout.avatarSize.width -
        cellContentInset.right

    } else {
      avatarX = IMUIMessageCellLayout.avatarPadding.left +
        cellContentInset.left
    }
    
    let avatarY = IMUIMessageCellLayout.avatarPadding.top +
      self.timeLabelFrame.bottom +
      cellContentInset.top
    
    if isOutGoingMessage {
      if !IMUIMessageCellLayout.isNeedShowOutGoingAvatar {
        return CGRect(x: avatarX, y: avatarY, width: 0, height: 0)
      }
    } else {
      if !IMUIMessageCellLayout.isNeedShowInComingAvatar {
        return CGRect(x: avatarX, y: avatarY, width: 0, height: 0)
      }
    }
    
    return CGRect(x: avatarX,
                  y: avatarY,
                  width: IMUIMessageCellLayout.avatarSize.width,
                  height: IMUIMessageCellLayout.avatarSize.height)
  }
  
  open var timeLabelFrame: CGRect {
    if self.isNeedShowTime {
      let timeWidth = (IMUIMessageCellLayout.timeLabelPadding.left +
      timeLabelContentSize.width +
      IMUIMessageCellLayout.timeLabelPadding.right + 0.5).rounded()
      
      let timeHeight = (IMUIMessageCellLayout.timeLabelPadding.top +
      timeLabelContentSize.height +
      IMUIMessageCellLayout.timeLabelPadding.bottom + 0.5).rounded()
      
      let timeX = (IMUIMessageCellLayout.cellWidth - timeWidth)/2
      
      return CGRect(x: timeX,
                    y: cellContentInset.top + 8,
                    width: timeWidth,
                    height: timeHeight)
    } else {
      return CGRect.zero
    }
  }
  
  open var cellHeight: CGFloat {
    let cellHeight = self.bubbleFrame.bottom +
      IMUIMessageCellLayout.bubblePadding.bottom +
      cellContentInset.bottom

    return cellHeight
  }
  
  open var bubbleFrame: CGRect {
    var bubbleX:CGFloat
    
    if self.isOutGoingMessage {
      bubbleX = IMUIMessageCellLayout.cellWidth -
        IMUIMessageCellLayout.avatarPadding.right -
        avatarFrame.width -
        IMUIMessageCellLayout.bubblePadding.right -
        cellContentInset.right -
        self.bubbleSize.width
    } else {
      bubbleX = IMUIMessageCellLayout.avatarPadding.left +
        avatarFrame.width +
        IMUIMessageCellLayout.bubblePadding.left +
        cellContentInset.left
    }
    let bubbleY = self.nameLabelFrame.bottom +
    IMUIMessageCellLayout.nameLabelPadding.bottom +
    IMUIMessageCellLayout.bubblePadding.top
    
    
    return CGRect(x: bubbleX,
                  y: bubbleY,
                  width: bubbleSize.width,
                  height: bubbleSize.height)
  }
  
  open var cellContentInset: UIEdgeInsets {
    return IMUIMessageCellLayout.cellContentInset
  }
  
  open var statusView: IMUIMessageStatusViewProtocol {
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
  
  open var bubbleContentView: IMUIMessageContentViewProtocol {
    return IMUIDefaultContentView()
  }
  
  open var bubbleContentType: String {
    return "default"
  }
}

class IMUIDefaultContentView: UIView, IMUIMessageContentViewProtocol{
  
  func layoutContentView(message: IMUIMessageModelProtocol) {
  
  }
  
  func Activity() {
  
  }
  
  func inActivity () {
  
  }
}
