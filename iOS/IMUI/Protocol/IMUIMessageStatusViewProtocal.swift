//
//  IMUIMessageStatusViewProtocal.swift
//  IMUIChat
//
//  Created by oshumini on 2017/5/14.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

public protocol IMUIMessageStatusViewProtocal {
  func layoutFailedStatus()
  func layoutSendingStatus()
  func layoutSuccessStatus()
  var statusViewID: String { get }
}

public extension IMUIMessageStatusViewProtocal {
  func layoutFailedStatus() {}
  func layoutSendingStatus() {}
  func layoutSuccessStatus() {}
  var statusViewID: String { return "" }
}
