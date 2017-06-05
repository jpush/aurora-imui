//
//  CGRectExtension.swift
//  RCTAuroraIMUI
//
//  Created by oshumini on 2017/6/3.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

extension CGRect {
  var right: CGFloat {
    return self.origin.x + self.size.width
  }
  
  var left: CGFloat {
    return self.origin.x
  }
  
  var bottom: CGFloat {
    return self.origin.y + self.size.height
  }
  
  var top: CGFloat {
    return self.origin.y
  }
  
  var width: CGFloat {
    return self.size.width
  }
  
  var height: CGFloat {
    return self.size.height
  }
}
