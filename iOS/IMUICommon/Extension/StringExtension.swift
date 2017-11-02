//
//  StringExtension.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/7.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

public extension String {
  func heightWithConstrainedWidth(width: CGFloat, font: UIFont) -> CGFloat {
    let boundingBox = self.sizeWithConstrainedWidth(with: width, font: font)
    return boundingBox.height
  }
  
  func sizeWithConstrainedWidth(with width: CGFloat, font: UIFont) -> CGSize {
    let constraintRect = CGSize(width: width, height: .greatestFiniteMagnitude)
    let boundingBox = self.boundingRect(with: constraintRect, options: .usesLineFragmentOrigin, attributes: [NSAttributedStringKey.font: font], context: nil)
    return CGSize(width: boundingBox.width, height: boundingBox.height)
  }
  

}
