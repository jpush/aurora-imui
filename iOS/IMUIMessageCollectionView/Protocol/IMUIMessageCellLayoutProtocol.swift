//
//  IMUIMessageCellLayoutProtocol.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

/**
 *  each IMUIMessageBaseCell need IMUIMessageCellLayoutProtocol to display message cell item
 */
@objc public protocol IMUIMessageCellLayoutProtocol: NSObjectProtocol {
  
  /**
   *  return message cell height
   */
  var cellHeight: CGFloat { get }
  
  /**
   *  return avatar frame
   */
  var avatarFrame: CGRect { get }
  
  /**
   *  return time label frame in the layout
   */
  var timeLabelFrame: CGRect { get }
  
  /**
   *  return message bubble frame in the layout
   */
  var bubbleFrame: CGRect { get }
  
  /**
   *  return contents size in message bubble
   */
  var bubbleContentSize: CGSize { get }
  
  /**
   *  return contents inset in message bubble
   */
  var bubbleContentInset: UIEdgeInsets { get }
  
  /**
   *  return IMUIMessageBaseCell content inset
   */
  var cellContentInset: UIEdgeInsets { get }
  
  /**
   *  return IMUIMessageBaseCell's status View
   */
  var statusView: IMUIMessageStatusViewProtocol { get }
  
  /**
   *  return statusView's frame
   */
  var statusViewFrame: CGRect { get }
  
  /**
   *  return nameLabel's frame
   */
  var nameLabelFrame: CGRect { get }
  
  /**
   *  return bubble content's View
   */
  var bubbleContentView: IMUIMessageContentViewProtocol { get }
  
  /**
   *  return bubble content's type
   */
  var bubbleContentType: String { get }
}

//  IMUIMessageCellLayoutProtocol default value
public extension IMUIMessageCellLayoutProtocol {
  var avatarFrame: CGRect {
    return CGRect.zero
  }
  
  var timeLabelFrame: CGRect {
    return CGRect.zero
  }
  
  var bubbleContentInset: UIEdgeInsets {
    return UIEdgeInsets.zero
  }
  
  var cellContentInset: UIEdgeInsets {
    return UIEdgeInsets.zero
  }
  
  var statusViewFrame: CGRect {
    return CGRect.zero
  }
}
