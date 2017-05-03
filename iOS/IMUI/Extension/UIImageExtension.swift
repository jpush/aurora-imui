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
  class func imuiImage(with name: String) -> UIImage? {
    return UIImage(named: "IMUIAssets.bundle/image/\(name).png")
  }
  
  var jpegRepresentationData: Data! {
    return UIImageJPEGRepresentation(self, 1.0)    // QUALITY min = 0 / max = 1
  }
  
  var pngRepresentationData: Data! {
    return UIImagePNGRepresentation(self)
  }
}
