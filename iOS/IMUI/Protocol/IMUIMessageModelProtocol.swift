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
protocol IMUIMessageModelProtocol {
  
  /**
   *  @required function
   *
   *  @return message id to identifies this message
   */
  var msgId: String { get }
  
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
  var layout: IMUIMessageCellLayoutProtocal { get }
  
  /**
   *  @required function
   *
   *  @return the bubble background image
   *
   *  @warning the image must be resizable
   *  just like that: bubbleImg.resizableImage(withCapInsets: UIEdgeInsetsMake(24, 15, 9, 10), resizingMode: .tile)
   */
  var resizableBubbleImage: UIImage { get }
  
  
  /**
   *  @optional function return media data
   *
   *  @discussion don't get video data from this function
   */
  func mediaData() -> Data
  
  /**
   *  @optional function if message type is text implement message text in this function
   */
  func textMessage() -> String

  /**
   *  @optional function 
   *  if the message type is video ,should implement this function
   */
  var videoPath: String? { get }

}

extension IMUIMessageModelProtocol {
  
  func mediaData() -> Data {
    return Data()
  }
  
  func textMessage() -> String {
    return ""
  }
  
  var videoPath: String? {
    return nil
  }
  
}
