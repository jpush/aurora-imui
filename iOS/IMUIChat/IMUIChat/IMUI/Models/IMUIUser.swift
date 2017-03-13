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
