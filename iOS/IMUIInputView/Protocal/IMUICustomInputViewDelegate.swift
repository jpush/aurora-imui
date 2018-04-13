//
//  IMUICustomInputViewDelegate.swift
//  sample
//
//  Created by oshumini on 2018/4/12.
//  Copyright © 2018年 HXHG. All rights reserved.
//

import Foundation
import UIKit

@objc public protocol IMUICustomInputViewDelegate: NSObjectProtocol {
  
  /**
   *  Tells the delegate when inputTextview text did change
   */
  @objc optional func textDidChange(text: String)
  
  /**
   *  Tells the delegate when keyboard will show
   */
  @objc optional func keyBoardWillShow(height: CGFloat, durationTime: Double)
}
