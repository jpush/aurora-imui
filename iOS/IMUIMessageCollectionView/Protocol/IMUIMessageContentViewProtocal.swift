//
//  IMUIMessageContentViewProtocal.swift
//  sample
//
//  Created by oshumini on 2017/6/11.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

@objc public protocol IMUIMessageContentViewProtocal: NSObjectProtocol {
//  var contentType: String { get }
  
  func layoutContentView(message: IMUIMessageModelProtocol)
  
  @objc optional func Activity()
  @objc optional func inActivity ()
}
