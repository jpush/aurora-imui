//
//  IMUIMessageStatusViewProtocal.swift
//  IMUIChat
//
//  Created by oshumini on 2017/5/14.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

@objc public protocol IMUIMessageStatusViewProtocal: NSObjectProtocol {
  // outGoing message
  func layoutFailedStatus()
  func layoutSendingStatus()
  func layoutSuccessStatus()
  
  // inComming message
  func layoutMediaDownloading()
  func layoutMediaDownloadFail()
  
  var statusViewID: String { get }
}

//public extension IMUIMessageStatusViewProtocal {
//  func layoutFailedStatus() {}
//  func layoutSendingStatus() {}
//  func layoutSuccessStatus() {}
//  func layoutMediaDownloading() {}
//  func layoutMediaDownloadFail(){}
//  
//  var statusViewID: String { return "" }
//}
