//
//  IMUIGalleryCellModel.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

enum IMUIGalleryType {
  case photo
  case video
}

class IMUIGalleryCellModel: NSObject {
  var type: IMUIGalleryType?
  var isSelected = false
  
  var mediaData: Data {
    return Data()
  }
  
}
