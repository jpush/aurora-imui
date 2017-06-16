//
//  IMUIMessageModelProtocol.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

/**
 *  The `IMUIMessageModelProtocol` protocol defines the common interface with message model objects
 *  It declares the required and optional methods which model should implement it
 */
@objc public protocol IMUIMessageModelProtocol: IMUIMessageProtocol {
  
  
  /**
   *  @required function
   *
   *  @return the user who sended this message
   */
  var fromUser: IMUIUserProtocol { get }
  
  /**
   *  @required function
   *
   *  @return the layout, `IMUIBaseMessageCell` will use it to layout message cell items
   */
  var layout: IMUIMessageCellLayoutProtocol { get }
 
  /**
   *  @required function
   *
   *  @return the bubble background image
   *
   *  @warning the image must be resizable
   *   just like that: bubbleImg.resizableImage(withCapInsets: UIEdgeInsetsMake(24, 15, 9, 10), resizingMode: .tile)
   */
  var resizableBubbleImage: UIImage { get }
  
  /**
   *  @optional function
   *
   *  @return time lable string
   */
  var timeString: String { get }
  
  /**
   *  text of message.
   *  @return string
   *  @optional function if message type is text implement message text in this function
   */
  func text() -> String

  /**
   * If message type is photo, voice, video or file,
   * get file path through this method.
   * @return media file path
   */
  func mediaFilePath() -> String
  
  /**
   * If message type is voice or video, get duration through this method.
   * @return duration of audio or video
   */
  @objc var duration: CGFloat { get }
  
  /**
   *  @optional get function
   *
   * return
   */
  var isOutGoing: Bool { get }
  
  /**
   *  @optional get function
   *
   * return get current message status
   */
  var messageStatus: IMUIMessageStatus { get }

}

//public extension IMUIMessageModelProtocol {
//  
//  var timeString: String {
//    return ""
//  }
//  
//  func text() -> String {
//    return ""
//  }
//  
//  func mediaFilePath() -> String {
//    return ""
//  }
//  
//  var messageStatus: IMUIMessageStatus {
//    return IMUIMessageStatus.success
//  }
//}
