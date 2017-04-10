//
//  UICollectionViewExtension.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/3.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

extension UICollectionView {
  
  func reusableCell(withClass cellClass: AnyClass, for index: IndexPath) -> AnyObject {
    var cell = self.dequeueReusableCell(withReuseIdentifier: cellClass.description(), for: index)
    
    if cell == nil {
      self.register(cellClass, forCellWithReuseIdentifier: cellClass.description())
      cell = self.dequeueReusableCell(withReuseIdentifier: cellClass.description(), for: index)
    }
    
    return cell
  }
  
  
  func reusableCell(withNibName nibName: String, for index: IndexPath) -> AnyObject {
    var cell = self.dequeueReusableCell(withReuseIdentifier: nibName, for: index)
    
    if cell == nil {
      self.register(UINib(nibName: nibName, bundle: nil), forCellWithReuseIdentifier: nibName)
      cell = self.dequeueReusableCell(withReuseIdentifier: nibName, for: index)
      }
    
    return cell
  }
}
