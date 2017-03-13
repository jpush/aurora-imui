//
//  IMUIGalleryDataManager.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

class IMUIGalleryDataManager: NSObject {
  var allItemArr = [IMUIGalleryCellModel]()
  var selectedItemArr = [IMUIGalleryCellModel]()
  
  subscript(index: Int) -> IMUIGalleryCellModel {
    if index < 0 {
      return allItemArr[index]
    } else {
      return allItemArr[allItemArr.endIndex + index]
    }
  }
  
  var count: Int {
    return allItemArr.count
  }
  
  
}
