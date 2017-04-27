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
protocol IMUIMessageMessageCollectionViewDelegate: NSObjectProtocol {
  /**
   *  Tells the delegate that user tap message cell
   */
  func messageCollectionView(_: UICollectionView, forItemAt: IndexPath, model: IMUIMessageModelProtocol)
  
  /**
   *  Tells the delegate that user tap message bubble
   */
  func messageCollectionView(didTapMessageBubbleInCell: UICollectionViewCell, model: IMUIMessageModelProtocol)
  
  /**
   *  Tells the delegate that user tap header image in message cell
   */
  func messageCollectionView(didTapHeaderImageInCell: UICollectionViewCell, model: IMUIMessageModelProtocol)
  /**
   *  Tells the delegate that the message cell will show in screen
   */
  func messageCollectionView(_: UICollectionView, willDisplayMessageCell: UICollectionViewCell, forItemAt: IndexPath, model: IMUIMessageModelProtocol)
  
  /**
   *  Tells the delegate that message cell end displaying
   */
  func messageCollectionView(_: UICollectionView, didEndDisplaying: UICollectionViewCell, forItemAt: IndexPath, model: IMUIMessageModelProtocol)
  
  /**
   *  Tells the delegate when messageCollection beginDragging
   */
  func messageCollectionView(_ willBeginDragging: UICollectionView)

}

extension IMUIMessageMessageCollectionViewDelegate {
  
  func messageCollectionView(_: UICollectionView, forItemAt: IndexPath, model: IMUIMessageModelProtocol) {}
  
  
  func messageCollectionView(didTapMessageBubbleInCell: UICollectionViewCell, model: IMUIMessageModelProtocol) {}
  
  func messageCollectionView(didTapHeaderImageInCell: UICollectionViewCell, model: IMUIMessageModelProtocol) {}
  
  func messageCollectionView(_: UICollectionView, willDisplayMessageCell: UICollectionViewCell, forItemAt: IndexPath, model: IMUIMessageModelProtocol) {}
  
  func messageCollectionView(_: UICollectionView, didEndDisplaying: UICollectionViewCell, forItemAt: IndexPath, model: IMUIMessageModelProtocol) {}
  
  func messageCollectionView(_ willBeginDragging: UICollectionView){}
}
