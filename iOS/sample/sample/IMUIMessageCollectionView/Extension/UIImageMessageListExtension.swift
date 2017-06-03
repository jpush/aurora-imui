//
//  UIImageMessageListExtension.swift
//  sample
//
//  Created by oshumini on 2017/5/19.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

public extension UIImage {
  class func imuiImage(with name: String) -> UIImage? {
    let bundle = Bundle.imuiBundle()
    let imagePath = bundle.path(forResource: "IMUIAssets.bundle/image/\(name)", ofType: "png")
    return UIImage(contentsOfFile: imagePath!)
  }
}
