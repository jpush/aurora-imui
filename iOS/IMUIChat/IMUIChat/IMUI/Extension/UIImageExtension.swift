//
//  UIImageExtension.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/2.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

extension UIImage {
  class func imuiImage(with name: String) -> UIImage? {
    return UIImage(named: "IMUIAssets.bundle/image/\(name).png")
  }
}
