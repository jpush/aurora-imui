//
//  UIImageExtension.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/2.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

public extension UIImage {
  
  var jpegRepresentationData: Data! {
    return self.jpegData(compressionQuality: 1.0)     // QUALITY min = 0 / max = 1
  }
  
  var pngRepresentationData: Data! {
    return self.pngData()
  }
}
