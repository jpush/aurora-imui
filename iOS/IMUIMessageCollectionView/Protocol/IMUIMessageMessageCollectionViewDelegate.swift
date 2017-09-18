//
//  IMUIMessageMessageCollectionViewDelegate.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/13.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

/**
 *  The `IMUIMessageMessageCollectionViewDelegate` protocol defines the even callback delegate
 */
@objc public protocol IMUIMessageMessageCollectionViewDelegate: NSObjectProtocol {
  /**
   *  Tells the delegate that user tap message cell
   */
  @objc optional func messageCollectionView(_: UICollectionView, forItemAt: IndexPath, model: IMUIMessageProtocol)
  
  /**
   *  Tells the delegate that user tap message bubble
   */
  @objc optional func messageCollectionView(didTapMessageBubbleInCell: UICollectionViewCell, model: IMUIMessageProtocol)

  /**
   *  Tells the delegate that user long press message bubble
   */
  @objc optional func messageCollectionView(beganLongTapMessageBubbleInCell: UICollectionViewCell, model: IMUIMessageProtocol)
  /**
   *  Tells the delegate that user tap header image in message cell
   */
  @objc optional func messageCollectionView(didTapHeaderImageInCell: UICollectionViewCell, model: IMUIMessageProtocol)
  
  /**
   *  Tells the delegate that user tap statusView in message cell
   */
  @objc optional func messageCollectionView(didTapStatusViewInCell: UICollectionViewCell, model: IMUIMessageProtocol)
  
  /**
   *  Tells the delegate that the message cell will show in screen
   */
  @objc optional func messageCollectionView(_: UICollectionView, willDisplayMessageCell: UICollectionViewCell, forItemAt: IndexPath, model: IMUIMessageProtocol)
  
  /**
   *  Tells the delegate that message cell end displaying
   */
  @objc optional func messageCollectionView(_: UICollectionView, didEndDisplaying: UICollectionViewCell, forItemAt: IndexPath, model: IMUIMessageProtocol)
  
  /**
   *  Tells the delegate when messageCollection beginDragging
   */
  @objc optional func messageCollectionView(_ willBeginDragging: UICollectionView)
  
  /**
   *  return a messageCell, it will show in messageList. Can use it to show message event or anything.
   *  @optional function 
   *  (NOTE:  1. You need append a model in IMUIMessageMessageCollectionView frist.
   *          2. If it is not a custom message, you should return nil)
   */
  @objc optional func messageCollectionView(messageCollectionView: UICollectionView, forItemAt: IndexPath, messageModel: IMUIMessageProtocol) -> UICollectionViewCell?
  
  /**
   *  return a messageCell‘s height,
   *  @return messageCell height
   *  @optional function (NOTE: when you need custom message cell, you should implement this function and return the custom message cell's height, If it is not a custom message，you need return nil)
   */
  @objc optional func messageCollectionView(messageCollectionView: UICollectionView, heightForItemAtIndexPath forItemAt: IndexPath, messageModel: IMUIMessageProtocol) -> NSNumber?

}
