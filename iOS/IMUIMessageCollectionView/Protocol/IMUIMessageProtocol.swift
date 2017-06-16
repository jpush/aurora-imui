//
//  IMUIMessageProtocol.swift
//  sample
//
//  Created by oshumini on 2017/6/16.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation

@objc public protocol IMUIMessageProtocol: NSObjectProtocol {
  /**
   *  @required function
   *
   *  @return message id to identifies this message
   */
  var msgId: String { get }
}
