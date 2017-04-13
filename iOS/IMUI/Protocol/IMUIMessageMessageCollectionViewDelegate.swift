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
  func didTapMessageCell(_ model: IMUIMessageModel)
  
  /**
   *  Tells the delegate that user tap message bubble
   */
  func didTapMessageBubble(_ model: IMUIMessageModel)
  
  /**
   *  Tells the delegate that the message cell will show in screen
   */
  func willDisplayMessageCell(_ model: IMUIMessageModel, cell: Any)
  
  /**
   *  Tells the delegate that message cell end displaying
   */
  func didEndDisplaying(_ model: IMUIMessageModel, cell: Any)
}

extension IMUIMessageMessageCollectionViewDelegate {
  func didTapMessageCell(_ model: IMUIMessageModel) {}
  func didTapMessageBubble(_ model: IMUIMessageModel){}
  func willDisplayMessageCell(_ model: IMUIMessageModel, cell: Any) {}
  func didEndDisplaying(_ model: IMUIMessageModel, cell: Any) {}
}
