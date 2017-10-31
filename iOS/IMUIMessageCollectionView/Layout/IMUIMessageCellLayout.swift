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
@objc public class IMUIMessageCellLayout: NSObject, IMUIMessageCellLayoutProtocol {

  @objc public static var avatarSize: CGSize = CGSize(width: 40, height: 40)
  @objc public static var avatarOffsetToCell: UIOffset = UIOffset(horizontal: 16, vertical: 16)
  
  @objc public static var timeLabelFrame: CGRect = CGRect.zero
  
  @objc public static var nameLabelSize: CGSize = CGSize(width: 200, height: 18)
  @objc public static var nameLabelOffsetToAvatar: UIOffset = UIOffset(horizontal: 8 , vertical: 0)
  
  @objc public static var bubbleOffsetToAvatar: UIOffset = UIOffset(horizontal: 8 , vertical: 0)
  
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
  
  @objc public static var timeStringColor: UIColor = UIColor(netHex: 0x90A6C4)
  @objc public static var timeStringFont: UIFont = UIFont.systemFont(ofSize: 12)
  
  @objc public init(isOutGoingMessage: Bool,
                 isNeedShowTime: Bool,
              bubbleContentSize: CGSize,
            bubbleContentInsets: UIEdgeInsets) {
    self.isOutGoingMessage = isOutGoingMessage
    self.isNeedShowTime = isNeedShowTime
    self.bubbleContentSize = bubbleContentSize
    self.bubbleContentInsets = bubbleContentInsets
    
  }
  
  open var isOutGoingMessage: Bool
  
  open var isNeedShowTime: Bool
  
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
  
  public var relativeAvatarOffsetToCell: UIOffset {
    
    if self.isOutGoingMessage {
      if IMUIMessageCellLayout.isNeedShowOutGoingAvatar {
        return UIOffset(horizontal: -IMUIMessageCellLayout.avatarOffsetToCell.horizontal, vertical: IMUIMessageCellLayout.avatarOffsetToCell.vertical)
      } else {
        return UIOffset.zero
      }
      
    } else {
      if IMUIMessageCellLayout.isNeedShowInComingAvatar {
        return IMUIMessageCellLayout.avatarOffsetToCell
      } else {
        return UIOffset.zero
      }
    }
  }
  
  public var relativeNameLabelOffsetToAvatar: UIOffset {
    if self.isOutGoingMessage {
      if IMUIMessageCellLayout.isNeedShowOutGoingName {
        return UIOffset(horizontal: -IMUIMessageCellLayout.nameLabelOffsetToAvatar.horizontal, vertical: IMUIMessageCellLayout.nameLabelOffsetToAvatar.vertical)
      } else {
        return UIOffset.zero
      }
      
    } else {
      if IMUIMessageCellLayout.isNeedShowInComingName {
        return IMUIMessageCellLayout.nameLabelOffsetToAvatar
      } else {
        return UIOffset.zero
      }
    }
  }
  
  public var relativeBubbleOffsetToAvatar: UIOffset {
    if self.isOutGoingMessage {
      return UIOffset(horizontal: -IMUIMessageCellLayout.bubbleOffsetToAvatar.horizontal, vertical: IMUIMessageCellLayout.bubbleOffsetToAvatar.vertical)
    } else {
      return IMUIMessageCellLayout.statusViewOffsetToBubble
    }
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
    var nameLabelY = avatarFrame.top + IMUIMessageCellLayout.nameLabelOffsetToAvatar.vertical
    if isOutGoingMessage {

      nameLabelX = IMUIMessageCellLayout.cellWidth -
        IMUIMessageCellLayout.nameLabelSize.width +
        relativeBubbleOffsetToAvatar.horizontal +
        relativeAvatarOffsetToCell.horizontal -
        avatarFrame.width +
        relativeNameLabelOffsetToAvatar.horizontal
      if !IMUIMessageCellLayout.isNeedShowOutGoingName {
        return CGRect(x: nameLabelX,
                      y: nameLabelY,
                      width: 0,
                      height: 0)
      }
      
    } else {
      nameLabelX = avatarFrame.right +
        relativeBubbleOffsetToAvatar.horizontal +
        relativeNameLabelOffsetToAvatar.horizontal
      
      if !IMUIMessageCellLayout.isNeedShowInComingName {
        nameLabelX = avatarFrame.right +
          relativeBubbleOffsetToAvatar.horizontal +
          relativeNameLabelOffsetToAvatar.horizontal
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
      
      avatarX = IMUIMessageCellLayout.cellWidth +
        relativeAvatarOffsetToCell.horizontal -
        IMUIMessageCellLayout.avatarSize.width -
        cellContentInset.right

    } else {
      avatarX = relativeAvatarOffsetToCell.horizontal +
        cellContentInset.left
    }
    
    let avatarY = relativeAvatarOffsetToCell.vertical +
      self.timeLabelFrame.size.height +
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
      let timeWidth = IMUIMessageCellLayout.cellWidth -
        cellContentInset.left -
        cellContentInset.right
      
      return CGRect(x: cellContentInset.left,
                    y: cellContentInset.top + 8,
                    width: timeWidth,
                    height: 20)
    } else {
      return CGRect.zero
    }
  }
  
  open var cellHeight: CGFloat {
    var cellHeight = IMUIMessageCellLayout.bubbleOffsetToAvatar.vertical +
      IMUIMessageCellLayout.timeLabelFrame.size.height +
      self.avatarFrame.origin.y +
      self.bubbleSize.height +
      cellContentInset.top +
      cellContentInset.bottom
    if self.isOutGoingMessage {
      if IMUIMessageCellLayout.isNeedShowOutGoingName {
        cellHeight += IMUIMessageCellLayout.nameLabelSize.height + IMUIMessageCellLayout.nameLabelOffsetToAvatar.vertical
      }
    } else {
      if IMUIMessageCellLayout.isNeedShowInComingName {
        cellHeight += IMUIMessageCellLayout.nameLabelSize.height + IMUIMessageCellLayout.nameLabelOffsetToAvatar.vertical
      }
    }
    return cellHeight
  }
  
  open var bubbleFrame: CGRect {
    var bubbleX:CGFloat
    
    if self.isOutGoingMessage {
      bubbleX = IMUIMessageCellLayout.cellWidth +
        relativeAvatarOffsetToCell.horizontal -
        avatarFrame.width +
        relativeBubbleOffsetToAvatar.horizontal -
        cellContentInset.right -
        self.bubbleSize.width
    } else {
      bubbleX = relativeAvatarOffsetToCell.horizontal +
        avatarFrame.width +
        relativeBubbleOffsetToAvatar.horizontal +
        cellContentInset.left
    }
    var bubbleY = relativeBubbleOffsetToAvatar.vertical +
      self.avatarFrame.top +
      IMUIMessageCellLayout.timeLabelFrame.size.height
    
    if isOutGoingMessage {
      if IMUIMessageCellLayout.isNeedShowOutGoingName {
        bubbleY += IMUIMessageCellLayout.nameLabelSize.height + IMUIMessageCellLayout.nameLabelOffsetToAvatar.vertical
      }
    } else {
      if IMUIMessageCellLayout.isNeedShowInComingName {
        bubbleY += IMUIMessageCellLayout.nameLabelSize.height + IMUIMessageCellLayout.nameLabelOffsetToAvatar.vertical
      }
    }
    
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
  
  func layoutContentView(message message: IMUIMessageModelProtocol) {
  
  }
  
  func Activity() {
  
  }
  
  func inActivity () {
  
  }
}
