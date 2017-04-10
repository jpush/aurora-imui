//
//  MyUser.swift
//  IMUIChat
//
//  Created by oshumini on 2017/4/9.
//  Copyright © 2017年 HXHG. All rights reserved.
//

import Foundation
import UIKit

class MyUser: IMUIUserProtocol {
  
  public init() {
    
  }
  
  func userId() -> String {
    return ""
  }
  
  func displayName() -> String {
    return ""
  }
  
  func Avatar() -> UIImage {
    return UIImage(named: "defoult_header")!
  }
}
