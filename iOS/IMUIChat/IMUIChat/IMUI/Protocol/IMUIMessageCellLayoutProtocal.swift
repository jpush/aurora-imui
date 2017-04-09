//
//  IMUIMessageCellLayoutProtocal.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit


protocol IMUIMessageCellLayoutProtocal {
  
  var cellHeight: CGFloat { get }
  
  var avatarFrame: CGRect { get }
  
  var timeLabelFrame: CGRect { get }
  
  var bubbleFrame: CGRect { get }
  
  var bubbleContentInset: UIEdgeInsets { get }
}

extension IMUIMessageCellLayoutProtocal {
  var avatarFrame: CGRect {
    return CGRect.zero
  }
  
  var timeLabelFrame: CGRect {
    return CGRect.zero
  }
  
  var bubbleContentInset: UIEdgeInsets {
    return UIEdgeInsets.zero
  }
}
