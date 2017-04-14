//
//  IMUIMessageMessageCollectionViewDelegate.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/13.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation

/**
 *  The `IMUIMessageMessageCollectionViewDelegate` protocol defines the even callback delegate
 */
protocol IMUIMessageMessageCollectionViewDelegate: NSObjectProtocol {
  /**
   *  Tells the delegate that user tap message cell
   */
  func didTapMessageCell(_ model: IMUIMessageModelProtocol)
  
  /**
   *  Tells the delegate that user tap message bubble
   */
  func didTapMessageBubble(_ model: IMUIMessageModelProtocol)
  
  /**
   *  Tells the delegate that the message cell will show in screen
   */
  func willDisplayMessageCell(_ model: IMUIMessageModelProtocol, cell: Any)
  
  /**
   *  Tells the delegate that message cell end displaying
   */
  func didEndDisplaying(_ model: IMUIMessageModelProtocol, cell: Any)
}

extension IMUIMessageMessageCollectionViewDelegate {
  func didTapMessageCell(_ model: IMUIMessageModelProtocol) {}
  func didTapMessageBubble(_ model: IMUIMessageModelProtocol){}
  func willDisplayMessageCell(_ model: IMUIMessageModelProtocol, cell: Any) {}
  func didEndDisplaying(_ model: IMUIMessageModelProtocol, cell: Any) {}
}
