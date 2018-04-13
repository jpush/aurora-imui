//
//  IMUIInputViewDataSource.swift
//  sample
//
//  Created by oshumini on 2018/4/4.
//  Copyright © 2018年 HXHG. All rights reserved.
//

import Foundation
import UIKit

@objc public protocol IMUICustomInputViewDataSource: NSObjectProtocol {
  
  /**
   *  return left/right/bottom inputBarItemListView's item number
   *  this function will be request thrice
   */
  @objc func imuiInputView(_ inputBarItemListView: UICollectionView,
                           numberForItemAt position: IMUIInputViewItemPosition) -> Int
  
  /**
   *  retuen left/right/bottom inputBarItemListView's item size
   */
  @objc func imuiInputView(_ inputBarItemListView: UICollectionView,
                           _ position: IMUIInputViewItemPosition,
                           sizeForIndex indexPath: IndexPath) -> CGSize
  
  /**
   *  return left/right/bottom inputBarItemListView's item cell
   */
  @objc func imuiInputView(_ inputBarItemListView: UICollectionView,
                           _ position: IMUIInputViewItemPosition,
                           cellForItemAt indexPath: IndexPath) -> UICollectionViewCell
  
  /**
   *  return featureView's cell
   */
  @objc func imuiInputView(_ featureView: UICollectionView,
                           cellForItem indexPath: IndexPath) -> UICollectionViewCell
  
  
  
}


