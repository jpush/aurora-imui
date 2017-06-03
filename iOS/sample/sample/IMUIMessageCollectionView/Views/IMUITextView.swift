//
//  IMUITextView.swift
//  IMUIChat
//
//  Created by oshumini on 2017/3/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

class IMUITextView: UILabel {

  var contentInset: UIEdgeInsets = .zero {
    didSet {
      setNeedsDisplay()
    }
  }
  
  override public var intrinsicContentSize: CGSize {
    let size = super.intrinsicContentSize
    return CGSize(width: size.width + contentInset.left + contentInset.right,
                  height: size.height + contentInset.top + contentInset.bottom)
  }
  
  override public func drawText(in rect: CGRect) {
    super.drawText(in: UIEdgeInsetsInsetRect(rect, contentInset))
  }
}
