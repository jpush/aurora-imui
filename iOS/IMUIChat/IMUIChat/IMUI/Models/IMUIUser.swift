//
//  IMUIUser.swift
//  IMUIChat
//
//  Created by oshumini on 2017/2/24.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import UIKit

public protocol IMUIUserProtocol {
  func userId() -> String
  func displayName() -> String
  func Avatar() -> UIImage
}

open class IMUIUser: IMUIUserProtocol {
//  open var userId: String
//  open var displayName: String
  
//  public init(userId: String, displayName: String) {
//    self.userId = userId
//    self.displayName = displayName
//  }

  public init() {

  }
  
  open func userId() -> String {
    return ""
  }
  
  open func displayName() -> String {
    return ""
  }
  
  open func Avatar() -> UIImage {
    return UIImage()
  }
}
